package com.ben.core.worktest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ben.R;
import com.ben.base.BaseActivity;
import com.ben.common.net.RetrofitManager;
import com.blankj.utilcode.util.ToastUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PeopleListActivity extends BaseActivity {

        @BindView(R.id.et_search) EditText textSearch;
        @BindView(R.id.button_search) Button buttonSearch;
        @BindView(R.id.button_add) Button buttonAdd;
        @BindView(R.id.main_list) RecyclerView mainList;
        @BindView(R.id.swipeLayout) SwipeRefreshLayout swipeLayout;

        private PeopleAdapter peopleAdapter;

        private List<PeopleListBean.DataBean> peoples = new ArrayList<>();

        @Override protected int getLayoutId() {
                return R.layout.activity_people_list;
        }

        @Override protected void initView() {
                setContentView(true, false, "测试");
                setDoubleClick(true);
                EventBus.getDefault().register(this);

                peopleAdapter = new PeopleAdapter(R.layout.item_people,peoples,this);

                LinearLayoutManager ms = new LinearLayoutManager(this);
                ms.setOrientation(LinearLayoutManager.VERTICAL);
                mainList.setLayoutManager(ms);
                mainList.setAdapter(peopleAdapter);
                swipeLayout.setOnRefreshListener(this::getList);
                swipeLayout.setColorSchemeColors(Color.BLUE);
                getList();
        }

        private void getList() {
                swipeLayout.setRefreshing(true);
                RetrofitManager.builder()
                    .getList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bean -> {
                            if (bean.getCode().equals("OK")){
                                    peoples = bean.getData();
                                    peopleAdapter.setNewData(bean.getData());
                            } else {
                                    ToastUtils.showShort(bean.getMsg());
                            }
                            swipeLayout.setRefreshing(false);
                    }, throwable -> {
                            ToastUtils.showShort("网络异常，请检查!");
                            swipeLayout.setRefreshing(false);
                    });
        }

        public final static String EventBus_PeopleList = "EventBus_PeopleList";
        @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
        public void onDataSynEvent(String event) {
                if (event.equals(EventBus_PeopleList)){
                        getList();
                }
        }

        @OnClick({ R.id.button_search, R.id.button_add }) public void onViewClicked(View view) {
                switch (view.getId()) {
                        case R.id.button_search:
                                peopleAdapter.setNewData( search(textSearch.getText().toString(),peoples));
                                break;
                        case R.id.button_add:
                                Intent intent = new Intent(this, PeopleAddActivity.class);
                                startActivity(intent);
                                break;
                }
        }

        public List<PeopleListBean.DataBean> search(String name,List<PeopleListBean.DataBean> list){
                List<PeopleListBean.DataBean> results = new ArrayList();
                Pattern pattern = Pattern.compile(name);
                for(int i=0; i < list.size(); i++){
                        Matcher matcher = pattern.matcher(list.get(i).getName());
                        if(matcher.find()){
                                results.add(list.get(i));
                        }
                }
                return results;
        }

        @Override protected void onDestroy() {
                super.onDestroy();
                EventBus.getDefault().unregister(this);
        }
}
