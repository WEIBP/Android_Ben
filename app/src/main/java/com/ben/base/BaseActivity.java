package com.ben.base;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ben.R;
import com.ben.app.AppManager;
import com.blankj.utilcode.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity
        implements Toolbar.OnMenuItemClickListener {

        private long mBackPressedTime;
        protected Bundle savedInstanceState;

        protected boolean isDoubleClick = false;
        protected ProgressDialog mProgressDialog;
        protected Toolbar toolbar;

        public boolean isOpenEvenBus() {
                return false;
        }

        @TargetApi(Build.VERSION_CODES.M) @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                this.savedInstanceState = savedInstanceState;

                //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置成全屏模式
                //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏

                if (getLayoutId() != 0) {
                        setContentView(getLayoutId());
                }
                //管理activity 用于退出时清理
                AppManager.getAppManager().addActivity(this);
                ButterKnife.bind(this);
                if (isOpenEvenBus()) {
                        EventBus.getDefault().register(this);
                }
                initView();
        }

        protected void setContentView(boolean hasToolbar, boolean hasBack) {
                toolbar = (Toolbar) findViewById(R.id.toolbar);
                if (hasToolbar) {
                        if (toolbar == null) {
                                LogUtils.d("toolbar 没有设置");
                                return;
                        }
                        toolbar.setTitle(getString(R.string.app_name));
                        if (hasBack) {
                                toolbar.setNavigationIcon(R.mipmap.back2);
                                toolbar.setNavigationOnClickListener(view -> finish());
                        }
                } else {
                        if (toolbar != null) {
                                toolbar.setVisibility(View.GONE);
                        }
                }
        }

        protected void setContentView(boolean hasToolbar, boolean hasBack, String title) {
                setContentView(hasToolbar, hasBack);
                if (title != null) {
                        toolbar.setTitle(title);
                        toolbar.setTitleTextColor(Color.WHITE);
                }
        }

        protected void setContentView(boolean hasToolbar, boolean hasBack, String title,
                int menuId) {
                setContentView(hasToolbar, hasBack, title);
                setUpMenu(menuId);
        }

        @Override protected void onDestroy() {
                super.onDestroy();
                AppManager.getAppManager().finishActivity(this);
                if (isOpenEvenBus()) {
                        EventBus.getDefault().unregister(this);
                }
        }

        protected void showProgressDialog() {
                mProgressDialog = ProgressDialog.show(this, "", "加载中...", true, false);
        }

        protected void closeProgressDialog() {
                if (null != mProgressDialog) {
                        mProgressDialog.dismiss();
                }
        }

        protected abstract int getLayoutId();

        protected abstract void initView();

        public void setDoubleClick(boolean doubleClick) {
                isDoubleClick = doubleClick;
        }

        @Override public void onBackPressed() {

                //BaseApplication.get("ifDoubleClickedBack", true);
                if (isDoubleClick) {

                        long curTime = SystemClock.uptimeMillis();
                        if ((curTime - mBackPressedTime) < (2 * 1000)) {
                                finish();
                                //activity栈管理
                                AppManager.getAppManager().AppExit();
                        } else {
                                mBackPressedTime = curTime;
                                Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
                        }
                } else {
                        finish();
                }
        }

        @Override public boolean onMenuItemClick(MenuItem item) {
                return false;
        }

        protected void setUpMenu(int menuId) {
                if (toolbar != null) {
                        toolbar.getMenu().clear();
                        if (menuId > 0) {
                                toolbar.inflateMenu(menuId);
                                toolbar.setOnMenuItemClickListener(this);
                        }
                }
        }

        public void saveForSharedPreference(int key, String value) {
                SharedPreferences sps = PreferenceManager.getDefaultSharedPreferences(this);
                sps.edit().putString(getString(key), value).commit();
        }

        public void saveForSharedPreference(String key, boolean value) {
                SharedPreferences sps = PreferenceManager.getDefaultSharedPreferences(this);
                sps.edit().putBoolean(key, value).commit();
        }

        public void saveForSharedPreference(String key, String value) {
                SharedPreferences sps = PreferenceManager.getDefaultSharedPreferences(this);
                sps.edit().putString(key, value).commit();
        }

        public String getForSharedPreference(int key) {
                SharedPreferences sps = PreferenceManager.getDefaultSharedPreferences(this);
                return sps.getString(getString(key), null);
        }

        public String getForSharedPreference(String key) {
                SharedPreferences sps = PreferenceManager.getDefaultSharedPreferences(this);
                return sps.getString(key, null);
        }

        public SharedPreferences getSharedPreference() {
                return PreferenceManager.getDefaultSharedPreferences(this);
        }

        protected String getUUID() {
                return java.util.UUID.randomUUID().toString();
        }

        public int getColorByID(int Id) {
                return ContextCompat.getColor(this, Id);
        }

        public Drawable getDrawableByID(int Id) {
                return ContextCompat.getDrawable(this, Id);
        }

        protected String getDateNow() {
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
                return sDateFormat.format(new Date());
        }

        protected void showToast(String msg, int time) {
                Toast.makeText(this, msg, time).show();
        }

        protected void showToastShort(String msg) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
}
