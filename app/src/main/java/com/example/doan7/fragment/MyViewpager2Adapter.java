package com.example.doan7.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyViewpager2Adapter extends FragmentStateAdapter {

    public MyViewpager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new HandBookFragment();
            case 2:
                return new HistoryFragment();
            default:
                return new HomeFragment();

        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
