package com.ben.core;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;
import com.ben.R;
import com.ben.common.Log.L;
import com.ben.common.net.RetrofitManager;
import com.ben.core.personal.PersonalFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
                implements NavigationView.OnNavigationItemSelectedListener {

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

                RetrofitManager.builder()
                                .getList()
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
}