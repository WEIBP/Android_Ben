package com.ben.ben;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.waps.AppConnect;

public class MainActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                AppConnect.getInstance("394715c017e8e93a0e6959f16c8f3926","default",this);
                AppConnect.getInstance(this).showOffers(this);

        }
}
