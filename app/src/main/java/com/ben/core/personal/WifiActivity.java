package com.ben.core.personal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ben.R;
import com.ben.base.BaseActivity;
import java.io.DataOutputStream;
import java.io.IOException;

public class WifiActivity extends BaseActivity {

        @BindView(R.id.text_result) TextView textResult;

        private WifiManager wifiManager;

        @Override protected int getLayoutId() {
                return R.layout.activity_wifi;
        }

        @Override protected void initView() {
                setContentView(true, true, "Wifi调试");
                wifiManager = (WifiManager) getApplicationContext().getSystemService(
                        Context.WIFI_SERVICE);
        }

        @OnClick(R.id.button_openwifi) void openwifi() {
                DataOutputStream os = null;
                try {
                        Process localProcess = Runtime.getRuntime().exec("su");

                        os = new DataOutputStream(localProcess.getOutputStream());
                        os.writeBytes("setprop service.adb.tcp.port 5555\n");
                        os.writeBytes("stop adbd\n");
                        os.writeBytes("start adbd\n");
                        os.flush();
                        WifiManager wifiManager =
                                (WifiManager) getApplicationContext().getSystemService(
                                        Context.WIFI_SERVICE);
                        //判断wifi是否开启
                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        int ipAddress = wifiInfo.getIpAddress();
                        String ip = intToIp(ipAddress);
                        textResult.setText("在电脑端输入命令 adb connect " + ip);
                } catch (IOException e) {
                        textResult.setText(e.getMessage());
                        e.printStackTrace();
                } finally {
                        try {
                                if (os != null) {
                                        os.close();
                                }
                        } catch (IOException e) {

                        }
                }
        }

        public static String intToIp(int ipInt) {
                StringBuilder sb = new StringBuilder();
                sb.append(ipInt & 0xFF).append(".");
                sb.append((ipInt >> 8) & 0xFF).append(".");
                sb.append((ipInt >> 16) & 0xFF).append(".");
                sb.append((ipInt >> 24) & 0xFF);
                return sb.toString();
        }

        @OnClick({ R.id.button_setwifi, R.id.button_wifihot })
        public void onViewClicked(View view) {
                switch (view.getId()) {
                        case R.id.button_setwifi:
                                wifiManager.setWifiEnabled(!wifiManager.isWifiEnabled());
                                textResult.setText(String.valueOf(wifiManager.isWifiEnabled()));
                                break;
                        case R.id.button_wifihot:
                                textResult.setText("button_wifihot");

                                //跳转到WiFi热点设置界面
                                Intent intent = new Intent();
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                //打开网络共享与热点设置页面
                                ComponentName comp = new ComponentName("com.android.settings",
                                        "com.android.settings.Settings$TetherSettingsActivity");
                                intent.setComponent(comp);
                                startActivity(intent);
                                break;
                }
        }
}
