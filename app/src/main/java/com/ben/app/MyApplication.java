package com.ben.app;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;
import com.ben.library.BenUtils;
import com.blankj.utilcode.util.Utils;

/**
 * Created by Ben on 2017/9/21.
 */

public class MyApplication extends Application {
        private static Context mApplicationContext;


        @Override
        public void onCreate() {
                super.onCreate();
                mApplicationContext = this;
                Utils.init(mApplicationContext);
                BenUtils.init(mApplicationContext);
                MultiDex.install(this);
        }

        // 获取ApplicationContext
        public static Context getContext() {
                return mApplicationContext;
        }
}
