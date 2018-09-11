package com.ben.core.worktest;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ben.R;
import com.ben.base.BaseActivity;
import com.ben.common.Log.L;
import com.ben.common.net.RetrofitManager;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import org.greenrobot.eventbus.EventBus;

public class PeopleAddActivity extends BaseActivity {

        @BindView(R.id.edit_name) EditText editName;
        @BindView(R.id.edit_call) EditText editCall;
        @BindView(R.id.button_add) Button buttonAdd;

        @Override protected int getLayoutId() {
                return R.layout.activity_people_add;
        }

        @Override protected void initView() {
                setContentView(true, true, "新增");
        }


        @OnClick(R.id.button_add) public void onViewClicked() {
                if (editCall.getText().toString().trim().length()==0 || editCall.getText().toString().trim().length()==0){
                        ToastUtils.showShort("请填入信息！");
                        return;
                }
                PeopleBean peopleBean = new PeopleBean();
                peopleBean.setCellphone(editCall.getText().toString());
                peopleBean.setName(editName.getText().toString());
                showProgressDialog();
                RetrofitManager.builder()
                    .addPeople(peopleBean)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bean -> {
                            if (bean.getCode().equals("OK")){
                                    L.object(bean);
                                    EventBus.getDefault().post(PeopleListActivity.EventBus_PeopleList);
                                    finish();
                            } else {
                                    ToastUtils.showShort(bean.getMsg());
                            }
                            closeProgressDialog();
                    }, throwable -> {
                            ToastUtils.showShort("网络异常，请检查!");
                            closeProgressDialog();
                    });
        }
}
