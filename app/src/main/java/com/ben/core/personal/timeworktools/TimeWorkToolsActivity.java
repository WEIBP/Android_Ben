package com.ben.core.personal.timeworktools;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.ben.R;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;

public class TimeWorkToolsActivity extends AppCompatActivity {

        private TextView resultText;
        private Switch switchTotal;
        private boolean totalSwitch = false;

        private static int intervalTime = 10* 1000  ;//间隔时间

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_time_tools);

                initView();

                if (startTimeWork()) {
                        resultText.setText(TimeUtils.getNowString()+":  定时任务开启....");
                }

        }

        private void initView() {
                resultText = findViewById(R.id.resultText);
                switchTotal = findViewById(R.id.switch1);

                switchTotal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                totalSwitch = b;
                        }
                });
        }

        private  boolean  isHoliday(String date) {
                boolean result = false;

                result  = TimeUtils.getChineseWeek(date).equals("周六")
                                        || TimeUtils.getChineseWeek(date).equals("周日");
                // TODO: 2020/3/4  继续和手动录入的比对判断
                return result;
        }

        private  boolean startOperation(){
                if (totalSwitch) {
                        return  operationWifi(true);
                } else {
                        return  false;
                }
        }

        private  boolean stopOperation(){
                return  operationWifi(false);
        }

        private boolean   operationWifi(boolean isOpen ){
                boolean result = false;
                WifiManager mWifiManager= (WifiManager) getBaseContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (mWifiManager != null) {
                        result =   mWifiManager.setWifiEnabled(isOpen);
                        LogUtils.d(mWifiManager.getWifiState());
                }
                return result;
        }

        private boolean startTimeWork(){
                boolean success = true;

                // 建立Intent和PendingIntent来调用目标组件
                Intent intent = new Intent(TimeWorkToolsActivity.this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                        100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                // 获取闹钟管理的实例
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                // 设置闹钟
                if (Build.VERSION.SDK_INT < 19) {
                        //am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + intervalTime, pendingIntent);
                        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (10 * 1000)
                                ,intervalTime, pendingIntent);
                } else if(Build.VERSION.SDK_INT >= 23){
                        am.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                SystemClock.elapsedRealtime(), pendingIntent);
                }    else {
                        //am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + intervalTime, pendingIntent);
                       am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (10 * 1000)
                               ,intervalTime, pendingIntent);
                }

                LogUtils.d("设置闹钟时间为: " + TimeUtils.millis2String(System.currentTimeMillis() + (10 * 1000)));
                return  success;
        }

        public void cancleTimeWork(){
                Intent intent = new Intent(TimeWorkToolsActivity.this,
                        AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        TimeWorkToolsActivity.this, 0, intent, 0);
                // 获取闹钟管理实例
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                // 取消
                am.cancel(pendingIntent);
        }

        public void openBtnClick(View view) {

                if ( isHoliday(TimeUtils.getNowString())){
                        stopOperation();
                        return;
                }

                boolean result = startOperation();
                resultText.setText(resultText.getText()+"\n" + TimeUtils.getNowString()+":  打开wifi"+(result?"成功":"失败"));
        }

        public void closeBtnClick(View view) {
                boolean result = stopOperation();
                resultText.setText(resultText.getText()+"\n" + TimeUtils.getNowString()+":  关闭wifi"+(result?"成功":"失败"));
        }

        @Override protected void onDestroy() {
                cancleTimeWork();
                super.onDestroy();
        }
}
