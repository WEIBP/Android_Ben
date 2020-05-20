package com.ben.core.androidbase;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.os.Build;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
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
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.Set;

public class BLEActivity extends BaseActivity {

        private static final int REQUEST_ENBLE_BT = 1;
        BluetoothAdapter mBluetoothAdapter;
        private boolean mScanning;
        private Handler handler = new Handler();
        // Stops scanning after 10 seconds.
        private static final long SCAN_PERIOD = 10000;

        private BluetoothDevice  mDevice;
        private TextView resultTV;
        private BluetoothGatt mBluetoothGatt;
        private BluetoothGattCharacteristic writeCharacteristic;
        private BluetoothGattCharacteristic notifyCharacteristic;

        @Override
        protected int getLayoutId() {
                return R.layout.activity_ble;
        }

        @Override
        protected void initView() {
                setContentView(true, true, "蓝牙");
                resultTV = findViewById(R.id.resultTV);
        }

        public void openBlooth(View view) {
                if (BLEUtil.isSupportBle(getBaseContext())) {
                        if (BLEUtil.isBleEnable(getBaseContext())) {
                                ToastUtils.showShort("蓝牙已开启.....");
                        } else {
                                ToastUtils.showShort("蓝牙正在启动.....");
                                BLEUtil.enableBluetooth(this, REQUEST_ENBLE_BT);
                        }
                } else {
                        ToastUtils.showShort("当前设备不支持蓝牙BLE");
                }
        }

        public void searchBlooth(View view) {
                BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                mBluetoothAdapter = bluetoothManager.getAdapter();
                scanLeDevice(true);
        }

        private void scanLeDevice(final boolean enable) {
                if (enable) {
                        // Stops scanning after a pre-defined scan period.
                        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                        mScanning = false;
                                        mBluetoothAdapter.stopLeScan(leScanCallback);
                                }
                        }, SCAN_PERIOD);
                        mScanning = true;
                        mBluetoothAdapter.startLeScan(leScanCallback);
                } else {
                        mScanning = false;
                        mBluetoothAdapter.stopLeScan(leScanCallback);
                }
        }

        private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
                @Override public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                        // TODO: 2020/3/6   设备列表添加到list  暂时 打印

                        final BleAdvertisedData badata = BLEUtil.parseAdertisedData(scanRecord);
                        String deviceName = device.getName();
                        if (deviceName == null) {
                                deviceName = badata.getName();
                        }
                        if (deviceName != null && filter(device)) {
                                LogUtils.d(deviceName + "   " + device.getAddress() + "   " + device.getUuids() + "   " + rssi);
                                mDevice = device;
                                resultTV.setText(TimeUtils.getNowString()+"    当前设备:  "  +mDevice.getName() );
                        }
                }
        };

        private boolean filter(BluetoothDevice device) {
               return  device.getName() != null && device.getName().startsWith("W0042");
        }

        public void connectBLE(View view) {
                 mBluetoothGatt = mDevice.connectGatt(this, true, gattCallback);
        }




        private BluetoothGattCallback gattCallback = new BluetoothGattCallback()
        {
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
                {
                        if(newState == BluetoothProfile.STATE_CONNECTED)
                        {
                                //连接成功
                                resultTV.setText(TimeUtils.getNowString()+mDevice.getName()+"   已连接");
                        }
                        else if(newState == BluetoothProfile.STATE_DISCONNECTED)
                        {
                                resultTV.setText(TimeUtils.getNowString()+mDevice.getName()+"   连接断开");
                        }
                }

                @Override
                public void onServicesDiscovered(BluetoothGatt gatt, int status)
                {
                        if(status==BluetoothGatt.GATT_SUCCESS && notifyCharacteristic == null && writeCharacteristic == null)
                        {
                                // TODO: 2020/3/11  设置连接服务处理 
                        }
                }

                @Override
                public void onCharacteristicWrite(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status)
                {

                }

                /**
                 * 特征值改变，主要用来接收设备返回的数据信息
                 * @param gatt GATT
                 * @param characteristic 特征值
                 */
                @Override
                public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
                {
                        LogUtils.d(characteristic.getValue());
                }

                @Override
                public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status)
                {
                       LogUtils.d(rssi);
                }
        };


        private void sendMessate(byte[] cmdByte){
                LogUtils.d(cmdByte);
                if (mBluetoothGatt != null )
                {
                        int count = cmdByte.length/20;
                        if(cmdByte.length%20>0)
                        {
                                count = count+1;
                        }
                        for(int i=0;i<count;i++)
                        {
                                int startIndex = i*20;
                                byte[] cmd = new byte[Math.min(20,cmdByte.length-i*20)];
                                System.arraycopy(cmdByte,startIndex,cmd,0,cmd.length);
                                writeCharacteristic.setValue(cmd);
                                mBluetoothGatt.writeCharacteristic(writeCharacteristic);
                                if(count>1)
                                {

                                }
                        }
                }
        }


        @Override
        protected void onDestroy() {
                scanLeDevice(false);
                super.onDestroy();
        }

        public void startCol(View view) {

        }
}
