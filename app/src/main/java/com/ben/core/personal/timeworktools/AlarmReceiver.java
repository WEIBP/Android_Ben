package com.ben.core.personal.timeworktools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;

/**
 * author: Ben
 * created on: 2020/3/4 14:10
 * description:
 */
public class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

                LogUtils.d("定时任务执行： "+ TimeUtils.getNowString());
        }
}
