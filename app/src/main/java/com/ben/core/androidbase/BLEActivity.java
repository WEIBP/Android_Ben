package com.ben.core.androidbase;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ben.R;
import com.ben.base.BaseActivity;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.Set;

public class BLEActivity extends BaseActivity {

    private static final int REQUEST_ENBLE_BT = 1;
    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ble;
    }


    @Override
    protected void initView() {
        setContentView(true,true,"蓝牙");
        // TODO: 2019-12-01  检查蓝牙权限
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null ){
            ToastUtils.showShort("当前设备不支持蓝牙");
        }

        // TODO: 2019-12-01  设置蓝牙状态监听广播和蓝牙连接广播
        registerReceiver(mReceiver,filter);
    }

    public void openBlooth(View view) {
        // TODO: 2019-12-01 打开蓝牙两种方式
        if (!mBluetoothAdapter.isEnabled()) {
            LogUtils.d("openBLE");
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent,REQUEST_ENBLE_BT);

            mBluetoothAdapter.enable();
        }


    }

    public void searchBlooth(View view) {
        // TODO: 2019-12-01  搜索蓝牙列表
        //查询已连接设备
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device :
                devices) {
//            LogUtils.d(device.getName()+"\n"+device.getAddress()+"\n"+device.getUuids().toString());
        }
        //搜索设备
        boolean result =  mBluetoothAdapter.startDiscovery();
        LogUtils.d(result);
    }

    public void connectDevice(View view) {
        // TODO: 2019-12-01 连接
    }

    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device!=null) {
                    LogUtils.d("检测到新设别:"+device.getName()+"\n"+device.getAddress()+"\n" );
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
