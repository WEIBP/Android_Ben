package com.ben.core.androidbase

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.LeScanCallback
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Handler
import android.view.View
import com.ben.R.layout
import com.ben.base.BaseActivity
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.activity_ble.connectBtn

/**
 * author: Ben
 * created on: 2020/3/10 16:29
 * description:
 */
class BLEKotlinActivity : BaseActivity() {

    private var mDevice: BluetoothDevice? = null
    private val REQUEST_ENBLE_BT = 1
    var mBluetoothAdapter: BluetoothAdapter? = null
    private var mScanning = false
    private val handler = Handler()

    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10000

    override fun getLayoutId(): Int {
        return layout.activity_ble
    }

    override fun initView() {
        setContentView(true, true, "蓝牙")
        connectBtn.setOnClickListener {
            val bluetoothGatt: BluetoothGatt = mDevice!!.connectGatt(this, true, mBluetoothGattCallback)

        }
    }

    fun openBlooth(view: View?) {
        if (BLEUtil.isSupportBle(baseContext)) {
            if (BLEUtil.isBleEnable(baseContext)) {
                ToastUtils.showShort("蓝牙已开启.....")
            } else {
                ToastUtils.showShort("蓝牙正在启动.....")
                BLEUtil.enableBluetooth(this, REQUEST_ENBLE_BT)
            }
        } else {
            ToastUtils.showShort("当前设备不支持蓝牙BLE")
        }
    }

    fun searchBlooth(view: View?) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = bluetoothManager.adapter
        scanLeDevice(true)

    }

    private fun scanLeDevice(enable: Boolean) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                mScanning = false
                mBluetoothAdapter!!.stopLeScan(leScanCallback)
            }, SCAN_PERIOD)
            mScanning = true
            mBluetoothAdapter!!.startLeScan(leScanCallback)
        } else {
            mScanning = false
            mBluetoothAdapter!!.stopLeScan(leScanCallback)
        }
    }

    private val leScanCallback = LeScanCallback { device, rssi, scanRecord ->
        // TODO: 2020/3/6   设备列表添加到list  暂时 打印
        val badata = BLEUtil.parseAdertisedData(scanRecord)
        var deviceName = device.name
        if (deviceName == null) {
            deviceName = badata.name
        }
        if (deviceName != null && filter(device)) {
            LogUtils.d(device.name)
            mDevice = device
        }
    }

    private fun filter(device: BluetoothDevice): Boolean {
        return device.name != null && device.name.startsWith("W0042")
    }

    var mBluetoothGattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(
            gatt: BluetoothGatt,
            status: Int,
            newState: Int
        ) {
            LogUtils.d("onConnectionStateChange")
            super.onConnectionStateChange(gatt, status, newState)
            //连接状态改变回调
        }

        override fun onServicesDiscovered(
            gatt: BluetoothGatt,
            status: Int
        ) {
            super.onServicesDiscovered(gatt, status)
            //连接回调，status为0时连接成功
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, status)
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            super.onCharacteristicChanged(gatt, characteristic)
        }

        override fun onDescriptorRead(
            gatt: BluetoothGatt,
            descriptor: BluetoothGattDescriptor,
            status: Int
        ) {
            super.onDescriptorRead(gatt, descriptor, status)
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt,
            descriptor: BluetoothGattDescriptor,
            status: Int
        ) {
            super.onDescriptorWrite(gatt, descriptor, status)
        }

        override fun onReliableWriteCompleted(
            gatt: BluetoothGatt,
            status: Int
        ) {
            super.onReliableWriteCompleted(gatt, status)
        }

        override fun onReadRemoteRssi(
            gatt: BluetoothGatt,
            rssi: Int,
            status: Int
        ) {
            super.onReadRemoteRssi(gatt, rssi, status)
        }

        override fun onMtuChanged(
            gatt: BluetoothGatt,
            mtu: Int,
            status: Int
        ) {
            super.onMtuChanged(gatt, mtu, status)
        }
    }

    override fun onDestroy() {
        scanLeDevice(false)
        super.onDestroy()
    }
}