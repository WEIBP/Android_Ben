package com.ben.core.personal;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.ben.R;
import com.ben.base.BaseActivity;
import com.ben.core.location.Gps;
import com.ben.core.location.GpsConvert;
import com.blankj.utilcode.util.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConvertLocationActivity extends BaseActivity {
        @BindView(R.id.et_location)
        EditText etLocation;
        @BindView(R.id.text_result)
        TextView textResult;

        @Override
        protected int getLayoutId() {
                return R.layout.activity_convert_location;
        }

        @Override
        protected void initView() {
                setContentView(true, true, "坐标系转换");

                double lat = 39.920924;
                double long1 = 116.41755;
                Gps gps = GpsConvert.bd09_To_Gps84(lat,long1);

                //long 精度 116
                etLocation.setText("39.39.9226226064,116.4087551220");
                etLocation.setText(gps.getWgLat()+","+gps.getWgLon());
                LogUtils.d(gps.getWgLat()+","+gps.getWgLon());
        }

}
