package com.ben.core;


import android.content.Intent;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.ben.base.BaseFragment;

import butterknife.OnClick;
import com.ben.R;
import com.ben.core.location.Location_Activity;

/**
 * A simple {@link Fragment} subclass.
 */
public class DevelopFragment extends BaseFragment {

        @Override
        protected int getLayoutId() {
                return R.layout.fragment_develop;
        }

        @Override
        protected void initView(View view) {

        }

        @OnClick(R.id.btn_location) void location(){
                startActivity(new Intent(getActivity(), Location_Activity.class));
        }

}
