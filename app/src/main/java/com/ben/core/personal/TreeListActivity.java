package com.ben.core.personal;

import com.ben.R;
import com.ben.base.BaseActivity;

public class TreeListActivity extends BaseActivity {

        @Override
        protected int getLayoutId() {
                return R.layout.activity_tree_list;
        }

        @Override
        protected void initView() {
                setContentView(true,true,"树状列表");
        }
}
