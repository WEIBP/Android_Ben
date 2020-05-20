package com.ben.core.androidbase;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.RequiresApi;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * author: Ben
 * created on: 2020/3/6 14:02
 * description:
 */
public class BLEUtil {

        //是否支持
         static boolean isSupportBle(Context context) {
                if (context == null || !context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                        return false;
                }
                BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
                return manager.getAdapter() != null;
        }
        //是否开启
         static boolean isBleEnable(Context context) {
                if (!isSupportBle(context)) {
                        return false;
                }
                BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
                return manager.getAdapter().isEnabled();
        }
        //开启蓝牙
        static void enableBluetooth(Activity activity, int requestCode) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(intent, requestCode);
        }

        private final static String TAG=BLEUtil.class.getSimpleName();
        public static BleAdvertisedData parseAdertisedData(byte[] advertisedData) {
                List<UUID> uuids = new ArrayList<UUID>();
                String name = null;
                if( advertisedData == null ){
                        return new BleAdvertisedData(uuids, name);
                }

                ByteBuffer buffer = ByteBuffer.wrap(advertisedData).order(ByteOrder.LITTLE_ENDIAN);
                while (buffer.remaining() > 2) {
                        byte length = buffer.get();
                        if (length == 0) break;

                        byte type = buffer.get();
                        switch (type) {
                                case 0x02: // Partial list of 16-bit UUIDs
                                case 0x03: // Complete list of 16-bit UUIDs
                                        while (length >= 2) {
                                                uuids.add(UUID.fromString(String.format(
                                                        "%08x-0000-1000-8000-00805f9b34fb", buffer.getShort())));
                                                length -= 2;
                                        }
                                        break;
                                case 0x06: // Partial list of 128-bit UUIDs
                                case 0x07: // Complete list of 128-bit UUIDs
                                        while (length >= 16) {
                                                long lsb = buffer.getLong();
                                                long msb = buffer.getLong();
                                                uuids.add(new UUID(msb, lsb));
                                                length -= 16;
                                        }
                                        break;
                                case 0x09:
                                        byte[] nameBytes = new byte[length-1];
                                        buffer.get(nameBytes);
                                        try {
                                                name = new String(nameBytes, "utf-8");
                                        } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                        }
                                        break;
                                default:
                                        buffer.position(buffer.position() + length - 1);
                                        break;
                        }
                }
                return new BleAdvertisedData(uuids, name);
        }
}
