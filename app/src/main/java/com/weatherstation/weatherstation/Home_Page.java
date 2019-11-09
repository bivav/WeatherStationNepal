package com.weatherstation.weatherstation;


import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class Home_Page extends Fragment {

    ViewPager viewPager;
    WeatherStation weatherStation;
    GreenHouse greenHouse;
    BottomNavigationView navigation;
    Bottom_Tab_Handler pagerAdapter;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        if (view == null) {

            view = inflater.inflate(R.layout.content_main, container, false);
            navigation = (BottomNavigationView)view.findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            viewPager = (ViewPager) view.findViewById(R.id.view_pager_bottom_navigation);
            pagerAdapter = new Bottom_Tab_Handler(getFragmentManager());

            initView();
        }
        return view;
    }

    private void initView() {

        weatherStation = new WeatherStation();
        greenHouse = new GreenHouse();

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
/*
                    weatherStation = new WeatherStation();
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.new_frame_activity, weatherStation);
                    transaction.commit();
*/
                    viewPager.setCurrentItem(0);
                    return true;

                case R.id.green_house:
                    viewPager.setCurrentItem(1);
                    return true;
            }
            return false;
        }
    };
}
