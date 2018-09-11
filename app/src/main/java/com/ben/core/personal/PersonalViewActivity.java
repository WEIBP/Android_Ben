package com.ben.core.personal;

import android.app.Activity;
import android.os.Bundle;

import com.ben.R;
import com.ben.base.BaseActivity;

public class PersonalViewActivity extends BaseActivity {


        @Override
        protected int getLayoutId() {
                return R.layout.activity_personal_view;
        }

        @Override
        protected void initView() {
                setContentView(true,true,"自定义控件");
        }
}
