package com.ben.core.personal;

import android.app.ActivityManager;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ben.R;
import com.ben.base.BaseActivity;
import com.blankj.utilcode.util.Utils;

import java.util.List;

import butterknife.BindView;

public class ServiceListActivity extends BaseActivity {
        @BindView(R.id.main_list)
        RecyclerView mainList;

        @Override
        protected int getLayoutId() {
                return R.layout.activity_service_list;
        }

        @Override
        protected void initView() {

                ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningServiceInfo> info = activityManager.getRunningServices(0x7FFFFFFF);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                ServicesAdapter adapter  = new ServicesAdapter(R.layout.item_functions,info);
                mainList.setAdapter(adapter);
                mainList.setLayoutManager(linearLayoutManager);
        }

}
