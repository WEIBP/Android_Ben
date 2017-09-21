package com.ben.ben.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;

/**
 * Created by WBP on 16/5/20.
 */
public abstract class BaseFragment extends Fragment {

    protected LayoutInflater mInflater;
    protected ProgressDialog mProgressDialog;


    public boolean isOpenEvenBus() {
        return false;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflater = inflater;
        View view = inflater.inflate(getLayoutId(), container, false);
        if (isOpenEvenBus()){
            EventBus.getDefault().register(this);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isOpenEvenBus()){
            EventBus.getDefault().unregister(this);
        }
    }



    protected void showProgressDialog(){
        mProgressDialog= ProgressDialog.show(getActivity(),
                "", "加载中...", true, false);
    }
    protected void closeProgressDialog(){
        if (null != mProgressDialog){
            mProgressDialog.dismiss();
        }
    }

    protected abstract int getLayoutId();
    protected abstract void initView(View view);

    protected void goToActivitySimple(Class<?> activity){
        Intent intent = new Intent(getActivity(),activity);
        startActivity(intent);
    }

    public void saveForSharedPreference(String key, String value){
        SharedPreferences sps = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sps.edit().putString(key, value).commit();
    }

    protected String getDateNow() {
        SimpleDateFormat sDateFormat    =   new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
        return   sDateFormat.format(new Date());
    }

    protected String getForSharedPreference(String key) {
        SharedPreferences sps = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return sps.getString(key,null);
    }

    protected String getUUID() {
        return null;
    }

    protected void showToast(String msg, int time){
        Toast.makeText(getActivity(),msg,time).show();
    }
    protected void showToastShort(String msg){
        Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();
    }
}
