package com.ben.core.worktest;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import com.ben.R;
import com.ben.common.net.RetrofitManager;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by Ben on 2018/2/28.
 */

public class PeopleAdapter extends BaseQuickAdapter<PeopleListBean.DataBean,BaseViewHolder> {

        private Context context;

        public PeopleAdapter(int layoutResId, @Nullable List<PeopleListBean.DataBean> data, Context context) {
                super(layoutResId, data);
                this.context = context;
        }

        @Override protected void convert(BaseViewHolder helper, PeopleListBean.DataBean item) {
                helper.setText(R.id.text_peopleid,item.getId()+"")
                    .setText(R.id.text_name,item.getName())
                    .setText(R.id.text_call,item.getCellphone())
                        .setOnClickListener(R.id.button_delete,V-> deletePeople(item));
        }

         void deletePeople(PeopleListBean.DataBean item) {
                 RetrofitManager.builder()
                     .deletePeople(item.getId())
                     .subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribe(bean -> {
                             if (bean.getCode().equals("OK")){
                                     ToastUtils.showShort("删除成功！");
                                     EventBus.getDefault().post(PeopleListActivity.EventBus_PeopleList);
                             } else {
                                     ToastUtils.showShort(bean.getMsg());
                             }

                     }, throwable -> {

                     });
        }
}
