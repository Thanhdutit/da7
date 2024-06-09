package com.example.doan7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.doan7.fragment.MyViewpager2Adapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity2 extends AppCompatActivity {

    private ViewPager2 mViewPage2;
    private BottomNavigationView mBottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mViewPage2 = findViewById(R.id.view_pager_2);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        mViewPage2.setPageTransformer(new DepthPageTransformer());

        MyViewpager2Adapter adapter =  new MyViewpager2Adapter(this);
        mViewPage2.setAdapter(adapter);

        mViewPage2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.navigation_chan_doan).setChecked(true);
                        break;

                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.navigation_history).setChecked(true);
                        break;

                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.navigation_cam_nang).setChecked(true);
                        break;
                }
            }
        });

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.navigation_chan_doan:
                        mViewPage2.setCurrentItem(0);
                        break;
                    case R.id.navigation_history:
                        mViewPage2.setCurrentItem(1);
                        break;
                    case R.id.navigation_cam_nang:
                        mViewPage2.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
    }
}