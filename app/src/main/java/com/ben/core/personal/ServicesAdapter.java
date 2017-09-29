package com.ben.core.personal;

import android.app.ActivityManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.ben.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Ben on 2017/9/21.
 */

public class ServicesAdapter extends BaseQuickAdapter<ActivityManager.RunningServiceInfo,BaseViewHolder> {
        public ServicesAdapter(@LayoutRes int layoutResId, @Nullable List<ActivityManager.RunningServiceInfo> data) {
                super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ActivityManager.RunningServiceInfo item) {
                helper.setText(R.id.text_describe,"pid："+item.pid+"     lastActivityTime:"+item.lastActivityTime)
                                .setText(R.id.text_name,item.service.getClassName());
        }
}
