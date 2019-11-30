package com.ben.core.nomalview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;

import com.ben.R;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

public class TimerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        initView();

        // TODO: 2019-08-16   1  显示时间   2 无响应60s  3 显示倒计时3s
    }

    private BenCountDownTimer endTimer;

    private void initView() {


    }


    void startEndTimer(){

        if (endTimer == null) {
            endTimer = new BenCountDownTimer(20*1000,2*1000) {
                @Override
                public void onTick(long l) {
                    ToastUtils.showShort(l/1000+"");
                    LogUtils.d(l/1000);
                }

                @Override
                public void onFinish() {
                    ToastUtils.showShort("onFinish");
                    showDialog();
                }
            };
        }

        endTimer.start();
    }

    private AlertDialog dialog;
    private CountDownTimer noteTimer;

    void showDialog(){

        if (dialog == null) {
            dialog = new AlertDialog.Builder(this).setMessage("测试").create();
        }

        if (noteTimer == null ) {
            noteTimer = new CountDownTimer(5*1000,1000) {
                @Override
                public void onTick(long l) {
                    dialog.setMessage(l/1000+"s  后结束");
                }

                @Override
                public void onFinish() {
                    dialog.hide();
                    ToastUtils.showShort("结束，跳");
                }
            };
        }

        noteTimer.start();
        dialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        startEndTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        endTimer.cancel();
        noteTimer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
        endTimer.cancel();
        noteTimer.cancel();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        endTimer.reSet();
        return super.onTouchEvent(event);
    }

    public void showDialog(View view) {
    }

    public void reset(View view) {
    }
}
