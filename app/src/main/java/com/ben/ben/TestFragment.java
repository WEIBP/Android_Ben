package com.ben.ben;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ben.ben.base.BaseFragment;

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
