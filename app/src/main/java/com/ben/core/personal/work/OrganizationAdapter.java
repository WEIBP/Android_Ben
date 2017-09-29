package com.ben.core.personal.work;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ben.R;
import com.ben.app.MyApplication;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Ben on 2017/9/25.
 */

public class OrganizationAdapter extends BaseMultiItemQuickAdapter<OrganizationalBean,BaseViewHolder> {
        protected Context mContext;
        public OrganizationAdapter(Context mContext, List<OrganizationalBean> data) {
                super(data);
                this.mContext = mContext;
                addItemType(OrganizationalBean.left, R.layout.list_item_organizational_left);
                addItemType(OrganizationalBean.right, R.layout.list_item_organizational_right);
                addItemType(OrganizationalBean.nomal, R.layout.list_item_organizational);
                addItemType(OrganizationalBean.only_one, R.layout.list_item_organizational_onlyone);
        }

        @Override
        protected void convert(BaseViewHolder helper, OrganizationalBean item) {
                helper.setText(R.id.text_name,item.getOrganizational());
                RecyclerView personList = helper.getView(R.id.list_person);
                personList.setLayoutManager(new GridLayoutManager(MyApplication.getContext(),2));
                personList.setAdapter(new RoleAdapter(mContext,R.layout.list_item_person,R.layout.list_item_person_header,item.getRoleSections()));
        }
}
