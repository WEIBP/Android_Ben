package com.ben.core;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.ben.common.net.hikvision.HikRetrofitManager;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.FileUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ben.R;
import com.ben.common.net.RetrofitManager;
import com.ben.core.personal.PersonalFragment;
import com.ben.library.log.L;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
                implements NavigationView.OnNavigationItemSelectedListener {

        private static final String TAG = "MainActivity";
        private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 123;

        @SuppressLint("CheckResult")
        @Override protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View view) {
                                Snackbar.make(view, "Replace with your own action",
                                                Snackbar.LENGTH_LONG)
                                                .setAction("Action", null)
                                                .show();
                                testHik();
                        }
                });

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.setDrawerListener(toggle);
                toggle.syncState();

                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);

                //默认选择相关功能
                navigationView.setCheckedItem(R.id.nav_personal);
                if (personalFragment == null) {
                        personalFragment = new PersonalFragment();
                }
                getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_view, personalFragment)
                                .commit();

                applyPermission();


        }

        @SuppressLint("CheckResult")
        private void testHik() {

                HikRetrofitManager.builder()
                        .getCamera()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(bean -> {
                                L.object(bean);
                        }, throwable -> {

                        });



                RetrofitManager.builder()
                                .getStocks("")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(bean -> {
                                        L.object(bean);
                                }, throwable -> {

                                });
        }

        @Override public void onBackPressed() {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                } else {
                        super.onBackPressed();
                }
        }

        @Override public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.main, menu);
                return true;
        }

        @Override public boolean onOptionsItemSelected(MenuItem item) {
                // Handle action bar item clicks here. The action bar will
                // automatically handle clicks on the Home/Up button, so long
                // as you specify a parent activity in AndroidManifest.xml.
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.action_settings) {
                        return true;
                }

                return super.onOptionsItemSelected(item);
        }

        private TestFragment testFragment;
        private DevelopFragment developFragment;
        private PersonalFragment personalFragment;

        @SuppressWarnings("StatementWithEmptyBody") @Override
        public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_camera) {
                        // Handle the camera action
                } else if (id == R.id.nav_develop) {
                        if (developFragment == null) {
                                developFragment = new DevelopFragment();
                        }
                        getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_view, developFragment)
                                        .commit();
                } else if (id == R.id.nav_test) {
                        if (testFragment == null) {
                                testFragment = new TestFragment();
                        }
                        getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_view, testFragment)
                                        .commit();
                } else if (id == R.id.nav_share) {

                } else if (id == R.id.nav_send) {

                } else if (id == R.id.nav_personal) {
                        if (personalFragment == null) {
                                personalFragment = new PersonalFragment();
                        }
                        getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_view, personalFragment)
                                        .commit();
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
        }


        @Override
        public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {

                switch (requestCode) {
                        case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                                Map<String, Integer> perms = new HashMap<String, Integer>();
                                perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                                for (int i = 0; i < permissions.length; i++) {
                                        perms.put(permissions[i], grantResults[i]);
                                }

                                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && perms.get
                                        (Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
//                    FrameApplication.frameApplication.initInfo();
//                                        startLogin();
                                } else {
//                                        SplashActivity.this.finish();
                                }
                                break;

                        default:
                                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                                break;
                }
        }


        private void applyPermission() {
                List<String> permissions = new ArrayList<>();
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                        permissions.add(Manifest.permission.CAMERA);
                }

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager
                        .PERMISSION_GRANTED) {
                        permissions.add(Manifest.permission.RECORD_AUDIO);
                }

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                        .PERMISSION_GRANTED) {
                        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager
                        .PERMISSION_GRANTED) {
                        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                }

                String[] permissionArray = new String[permissions.size()];
                permissionArray = permissions.toArray(permissionArray);
                if (null == permissionArray || permissionArray.length == 0) {
//            FrameApplication.frameApplication.initInfo();
//                        startLogin();
                } else {
                        ActivityCompat.requestPermissions(this, permissionArray, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                }
        }

        public void testRxjava(){
                Observable<String> observable1 = Observable.create(e -> {
                        e.onNext("hello");
                        e.onComplete();
                });

                Observable<String> observable2 = Observable.just("hello");
                Observable<String> observable3 = Observable.just("hello","ben");

                observable1.subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String s) {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                });

        }
}
