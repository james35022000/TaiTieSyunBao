package com.AndroidProject.taitiesyunbao;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Overloading FragmentPagerAdapter to display my own pager.
 * Created by JCChen on 2017/4/8.
 */

public class MenuPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> list;

    // Constructor
    public MenuPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return list.get(arg0);
    }
}
