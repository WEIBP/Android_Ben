package com.ben.core.swr;

import com.ben.R;
import com.ben.base.BaseActivity;

public class SDKTestActivity extends BaseActivity {

        @Override
        protected int getLayoutId() {
                return R.layout.activity_sdktest;
        }

        @Override
        protected void initView() {
                setContentView(true, true, "iLab_SDK");
        }
}
