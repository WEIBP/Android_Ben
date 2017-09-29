package com.ben.core.location;

import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ben.base.BaseActivity;
import com.ben.R;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class Location_Activity extends BaseActivity {

        @BindView(R.id.tv_info)
        TextView tvInfo;
        @BindView(R.id.et_interval)
        EditText etInterval;
        @BindView(R.id.et_port)
        EditText etPort;
        @BindView(R.id.text_result)
        TextView textResult;
        @BindView(R.id.et_time)
        EditText etTime;
        @BindView(R.id.btn_save)
        Button btnSave;
        @BindView(R.id.btn_start)
        Button btnStart;
        @BindView(R.id.et_deviceno)
        TextView etDeviceno;

        @Override
        protected int getLayoutId() {
                return R.layout.activity_location;
        }

        @Override
        protected void initView() {
                EventBus.getDefault().register(this);
                setContentView(true, true, "定位");
                if (getForSharedPreference(Consts.locationIP) != null) {
                        etInterval.setText(getForSharedPreference(Consts.locationIP));
                        etPort.setText(getForSharedPreference(Consts.locationPort));
                        etTime.setText(getForSharedPreference(Consts.locationCyle));
                } else {
                        etInterval.setText(Consts.Default_locationurl);
                        etPort.setText(Consts.Default_port + "");
                        etTime.setText(Consts.Default_time + "");
                }
                startLocationService();
                setButtenStart();

                new RxPermissions(this)
                        .request(Manifest.permission.READ_PHONE_STATE)
                        .subscribe(granted -> {
                                if (granted) {
                                        etDeviceno.setText(PhoneUtils.getIMEI());
                                } else {

                                }
                        });
        }

        private void startLocationService() {
                LogUtils.d(getSharedPreference().getBoolean(Consts.is_first, true) && !ServiceUtils.isServiceRunning(Consts.location_service_name));
                if (getSharedPreference().getBoolean(Consts.is_first, true)
                        && !ServiceUtils.isServiceRunning(Consts.location_service_name)) {

                        new RxPermissions(this)
                                .request(Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.READ_PHONE_STATE)
                                .subscribe(granted -> {
                                        if (granted) {
                                                startService(new Intent(this, LocationService.class));
                                                setButtenStart();
                                        } else {

                                        }
                                });

                        getSharedPreference().edit().putBoolean(Consts.is_first, false).apply();
                }
        }

        private void setButtenStart() {
                btnStart.setText(
                        ServiceUtils.isServiceRunning(Consts.location_service_name) ? "停止定位" : "开始定位"
                );
                textResult.setText(
                        ServiceUtils.isServiceRunning(Consts.location_service_name) ? "正在定位，请稍等" : "定位已停止，请点击开始定位"
                );
        }

        @OnClick({R.id.btn_start, R.id.btn_save})
        public void onViewClicked(View view) {
                switch (view.getId()) {
                        case R.id.btn_start:
                                if (ServiceUtils.isServiceRunning(Consts.location_service_name)) {
                                        stopLocation();
                                } else {
                                        startLocation();
                                }
                                setButtenStart();
                                break;
                        case R.id.btn_save:
                                if (StringUtils.isEmpty(etTime.getText()) || StringUtils.isEmpty(etPort.getText()) || StringUtils.isEmpty(etInterval.getText())) {
                                        ToastUtils.showShort("请输入正确的参数！");
                                        return;
                                }
                                saveForSharedPreference(Consts.locationIP, etInterval.getText().toString().trim());
                                saveForSharedPreference(Consts.locationPort, etPort.getText().toString().trim());
                                saveForSharedPreference(Consts.locationCyle, etTime.getText().toString().trim());

                                //重新启动定位服务
                                stopLocation();
                                startLocation();

                                setButtenStart();
                                break;
                }
        }

        private void startLocation(){
                startService(new Intent(this, LocationService.class));
//                ServiceUtil.invokeTimerPOIService(getBaseContext());
        }

        private void stopLocation(){
                stopService(new Intent(this, LocationService.class));
//                ServiceUtil.cancleAlarmManager(getBaseContext());
        }



        @Override
        protected void onDestroy() {
                EventBus.getDefault().unregister(this);
                super.onDestroy();
        }


        @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
        public void onDataSynEvent(LocationBean event) {
                textResult.setText(event.getData());
        }

}
