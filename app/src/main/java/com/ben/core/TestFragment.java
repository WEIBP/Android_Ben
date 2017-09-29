package com.ben.core;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;


import com.ben.base.BaseFragment;
import com.ben.R;
import com.ben.core.personal.TestActivity;

import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends BaseFragment {


        @Override
        protected int getLayoutId() {
                return R.layout.fragment_test;
        }

        @Override
        protected void initView(View view) {

        }

        @OnClick(R.id.btn_showinfo) void showinfo(){
                startActivity(new Intent(getActivity(),TestActivity.class));
        }
}
