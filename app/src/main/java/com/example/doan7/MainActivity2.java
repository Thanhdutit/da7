//package com.example.doan7;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.viewpager2.widget.ViewPager2;
//
//import android.os.Bundle;
//import android.view.MenuItem;
//
//import com.example.doan7.fragment.MyViewpager2Adapter;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//public class MainActivity2 extends AppCompatActivity {
//    private String selectedLanguage;
//    private ViewPager2 mViewPage2;
//    private BottomNavigationView mBottomNavigationView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main2);
//
//        mViewPage2 = findViewById(R.id.view_pager_2);
//        mBottomNavigationView = findViewById(R.id.bottom_navigation);
//
//        selectedLanguage = MyApplication.getSelectedLanguage();
//
//        mViewPage2.setPageTransformer(new DepthPageTransformer());
//
//        MyViewpager2Adapter adapter =  new MyViewpager2Adapter(this);
//        mViewPage2.setAdapter(adapter);
//
//        mViewPage2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                switch (position){
//                    case 0:
//                        mBottomNavigationView.getMenu().findItem(R.id.navigation_chan_doan).setChecked(true);
//                        break;
//
//                    case 1:
//                        mBottomNavigationView.getMenu().findItem(R.id.navigation_history).setChecked(true);
//                        break;
//
//                    case 2:
//                        mBottomNavigationView.getMenu().findItem(R.id.navigation_cam_nang).setChecked(true);
//                        break;
//                }
//            }
//        });
//
//        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId())
//                {
//                    case R.id.navigation_chan_doan:
//                        mViewPage2.setCurrentItem(0);
//                        break;
//                    case R.id.navigation_history:
//                        mViewPage2.setCurrentItem(1);
//                        break;
//                    case R.id.navigation_cam_nang:
//                        mViewPage2.setCurrentItem(2);
//                        break;
//                }
//                return true;
//            }
//        });
//    }
//
//
//}

package com.example.doan7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.doan7.fragment.MyViewpager2Adapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity2 extends AppCompatActivity {
    private String selectedLanguage;
    private ViewPager2 mViewPage2;
    private BottomNavigationView mBottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mViewPage2 = findViewById(R.id.view_pager_2);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        selectedLanguage = MyApplication.getSelectedLanguage();
        updateMenuTitles();

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
                        mBottomNavigationView.getMenu().findItem(R.id.navigation_handbook).setChecked(true);
                        break;

                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.navigation_history).setChecked(true);
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
                    case R.id.navigation_handbook:
                        mViewPage2.setCurrentItem(1);
                        break;
                    case R.id.navigation_history:
                        mViewPage2.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
    }


    private void updateMenuTitles( ) {
        switch (selectedLanguage) {
            case "English":
                mBottomNavigationView.getMenu().findItem(R.id.navigation_chan_doan).setTitle(getString(R.string.nav_Diagnosis_en));
                mBottomNavigationView.getMenu().findItem(R.id.navigation_history).setTitle(getString(R.string.nav_History_en));
                mBottomNavigationView.getMenu().findItem(R.id.navigation_handbook).setTitle(getString(R.string.nav_Handbook_en));
                break;
            case "Vietnamese":
                mBottomNavigationView.getMenu().findItem(R.id.navigation_chan_doan).setTitle(getString(R.string.nav_Diagnosis_vn));
                mBottomNavigationView.getMenu().findItem(R.id.navigation_history).setTitle(getString(R.string.nav_History_vn));
                mBottomNavigationView.getMenu().findItem(R.id.navigation_handbook).setTitle(getString(R.string.nav_Handbook_vn));
                break;
            case "Japanese":
                mBottomNavigationView.getMenu().findItem(R.id.navigation_chan_doan).setTitle(getString(R.string.nav_Diagnosis_jp));
                mBottomNavigationView.getMenu().findItem(R.id.navigation_history).setTitle(getString(R.string.nav_History_jp));
                mBottomNavigationView.getMenu().findItem(R.id.navigation_handbook).setTitle(getString(R.string.nav_Handbook_jp));
                break;
            default:
                mBottomNavigationView.getMenu().findItem(R.id.navigation_chan_doan).setTitle(getString(R.string.nav_Diagnosis_vn));
                mBottomNavigationView.getMenu().findItem(R.id.navigation_history).setTitle(getString(R.string.nav_History_vn));
                mBottomNavigationView.getMenu().findItem(R.id.navigation_handbook).setTitle(getString(R.string.nav_Handbook_vn));
                break;
        }
    }
}
