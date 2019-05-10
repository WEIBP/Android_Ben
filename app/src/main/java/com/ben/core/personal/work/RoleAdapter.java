package com.ben.core.personal.work;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;

import com.ben.R;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Ben on 2017/9/25.
 */

public class RoleAdapter extends BaseSectionQuickAdapter<RoleSection,BaseViewHolder> {
        protected Context mContext;
        public RoleAdapter(Context mContext, int layoutResId, int sectionHeadResId, List<RoleSection> data) {
                super(layoutResId, sectionHeadResId, data);
                this.mContext = mContext;
        }

        @Override
        protected void convertHead(BaseViewHolder helper, RoleSection item) {
                helper.setText(R.id.text_person,item.header);
        }

        @Override
        protected void convert(BaseViewHolder helper, RoleSection item) {
                helper.setText(R.id.button_person,item.t.getName())
                        .setOnClickListener(R.id.button_person,v->{
                                AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                                builder.setTitle("联系人");
                                builder.setMessage("确定联系"+item.t.getName()+"？");

                                //监听下方button点击事件
                                builder.setPositiveButton("确定", (dialogInterface, i) -> {

                                });
                                builder.setNegativeButton("取消", (dialogInterface, i) -> {

                                });
                                builder.show();
                        });

        }
}
