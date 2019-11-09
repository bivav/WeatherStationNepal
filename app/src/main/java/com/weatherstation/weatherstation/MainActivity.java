package com.weatherstation.weatherstation;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ViewPager viewPager;
    WeatherStation weatherStation;
    GreenHouse greenHouse;
    BottomNavigationView navigation;
    Bottom_Tab_Handler pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Calling the fragments
        weatherStation = new WeatherStation();
        greenHouse = new GreenHouse();

        viewPager = (ViewPager) findViewById(R.id.view_pager_bottom_navigation);
        pagerAdapter = new Bottom_Tab_Handler(getSupportFragmentManager());

        initView();

    }

    private void initView() {

        pagerAdapter.addFragment(weatherStation);
        pagerAdapter.addFragment(greenHouse);

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(pageChangeListener);

    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    navigation.setSelectedItemId(R.id.weather_station);
                    break;
                case 1:
                    navigation.setSelectedItemId(R.id.green_house);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.weather_station:

                    viewPager.setCurrentItem(0);
                    return true;

                case R.id.green_house:
                    viewPager.setCurrentItem(1);
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.about_us:
                startActivity(new Intent(this, AboutUs.class));
                break;
            case R.id.help:
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            RelativeLayout relativeLayout = findViewById(R.id.cons_container);
            relativeLayout.setVisibility(View.VISIBLE);

/*
            Forecast_Data forecast_data = new Forecast_Data();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.cons_container, forecast_data);
            transaction.commit();
*/

        } else if (id == R.id.nav_forecast) {

            RelativeLayout relativeLayout = findViewById(R.id.cons_container);
            relativeLayout.setVisibility(View.GONE);

            Forecast_Data forecast_data = new Forecast_Data();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.change, forecast_data);
            transaction.commit();

        } else if (id == R.id.nav_graph) {

            RelativeLayout relativeLayout = findViewById(R.id.cons_container);
            relativeLayout.setVisibility(View.GONE);

            Graph_Data graph_data = new Graph_Data();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.change, graph_data);
            transaction.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}