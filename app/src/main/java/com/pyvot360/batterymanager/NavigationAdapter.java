package com.pyvot360.batterymanager;

import android.content.Context;
import android.provider.Settings;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class NavigationAdapter extends FragmentPagerAdapter {

    Context context;
    int totalTabs;

    public NavigationAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Stats statsFragment = new Stats();
                return statsFragment;
            case 1:
                Running runningFragment = new Running();
                return runningFragment;
            case 2:
                Setting settingsFragment = new Setting();
                return settingsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
