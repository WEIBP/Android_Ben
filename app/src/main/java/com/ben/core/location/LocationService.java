package com.ben.core.location;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.ben.app.MyApplication;
import com.ben.R;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.TimeUtils;

import org.greenrobot.eventbus.EventBus;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class LocationService extends Service {

    PowerManager.WakeLock mWakeLock;


    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    private  String ieme;
    private String ip;
    private String port;
    private String time;

    private int portInt;
    private int timeInt;

    @SuppressLint("InvalidWakeLockTag")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_menu_camera)//设置小图标
                .setContentTitle("175指挥系统")
                .setContentText("正在定位....")
                .build();
        //把该service创建为前台service
        startForeground(1, notification);

        initData();
        initLocation();
        //锁屏保护
        if (null == mWakeLock) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
                    | PowerManager.ON_AFTER_RELEASE, "myService");
            if (null != mWakeLock) {
                mWakeLock.acquire();
            }
        }

    }

    private void initData() {
        ieme = PhoneUtils.getIMEI();

        SharedPreferences sps = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());

        if (sps.getString(Consts.locationIP,null) == null
                ||sps.getString(Consts.locationCyle,null) == null
                ||sps.getString(Consts.locationPort,null) == null){
            ip = Consts.Default_locationurl;
            portInt = Consts.Default_port;
            timeInt = Consts.Default_time;
        } else {
            ip = sps.getString(Consts.locationIP,null);
            port = sps.getString(Consts.locationPort,null);
            time = sps.getString(Consts.locationCyle,null);
            try {
                portInt = Integer.parseInt(port);
                timeInt = Integer.parseInt(time);
            } catch (NumberFormatException e) {
                portInt = Consts.Default_port;
                timeInt = Consts.Default_time;
                e.printStackTrace();
            }
        }
    }
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        // 启动定位
        locationClient.startLocation();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //保证服务重启
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        releaseWakeLock();
        // 一定要制空，否则内存泄漏
        super.onDestroy();
        destroyLocation();
        stopForeground(true);
    }

    /**
     * 销毁定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void destroyLocation(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * onDestroy时，释放设备电源锁
     */
    private void releaseWakeLock() {
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }
    /**
     * 默认的定位参数
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(10000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(timeInt);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if(location.getErrorCode() == 0){

                       Gps gps =   GpsConvert.gcj_To_Gps84(location.getLatitude(),location.getLongitude());
                        sb.append("定位成功" + "\n");
                        sb.append("设备IEME: " + PhoneUtils.getIMEI()+"\n");
                        sb.append("定位类型: " + location.getLocationType() + "\n");
//                        sb.append("经度(转换前): " + location.getLongitude() + "\n");
//                        sb.append("纬度(转换前)   : " + location.getLatitude() + "\n");
                        sb.append("经度(转换后)   : " + gps.getWgLon() + "\n");
                        sb.append("纬度(转换后)  : " + gps.getWgLat() + "\n");
                        sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                        sb.append("提供者    : " + location.getProvider() + "\n");

                        sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                        sb.append("角    度    : " + location.getBearing() + "\n");
                        // 获取当前提供定位服务的卫星个数
                        sb.append("星    数    : " + location.getSatellites() + "\n");
                        sb.append("国    家    : " + location.getCountry() + "\n");
                        sb.append("省            : " + location.getProvince() + "\n");
                        sb.append("市            : " + location.getCity() + "\n");
                        sb.append("城市编码 : " + location.getCityCode() + "\n");
                        sb.append("区            : " + location.getDistrict() + "\n");
                        sb.append("区域 码   : " + location.getAdCode() + "\n");
                        sb.append("地    址    : " + location.getAddress() + "\n");
                        //定位完成的时间
                        sb.append("定位时间: " + TimeUtils.getNowString() + "\n");
                        EventBus.getDefault().post(new LocationBean(sb.toString()));
                        String sendData = ieme + "," + gps.getWgLon() + "," + gps.getWgLat() +
                                "," + location.getSpeed() + "," + location.getBearing() + "," +
                                TimeUtils.getNowString();
                        for (int i = 0; i < 1 ; i++) {
                            sendLocation(sendData);
                        }
                } else {
                    //定位失败
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    LogUtils.d(sb.toString());
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                }
                //定位之后的回调时间
                sb.append("回调时间: " + TimeUtils.getNowString() + "\n");

                //解析定位结果，
                String result = sb.toString();

            } else {
                String result = "定位失败，loc is null";
                EventBus.getDefault().post(new LocationBean(result));
            }


        }
    };

    private DatagramSocket socket = null;
    private void sendLocation(String sendData){
        new Thread(new Runnable() {
            @Override
            public void run() {
//                LogUtils.d(sendData);
                byte data[] = sendData.getBytes();
                try {
                    InetAddress address = InetAddress.getByName(ip);  //服务器地址
                    //创建发送方的数据报信息
                    DatagramPacket dataGramPacket = new DatagramPacket(data, data.length, address, portInt);
                   if (socket == null){
                       socket = new DatagramSocket();  //创建套接字
                   }

                   socket.send(dataGramPacket);  //通过套接字发送数据

                } catch (Exception e) {
                    EventBus.getDefault().post(e.getMessage());
                    LogUtils.d(e.getMessage());
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
