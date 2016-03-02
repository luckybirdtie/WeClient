package com.example.dayangtie.weclient;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by dayangtie on 2/03/16.
 */
public class MainActivityFragmentPagerAdapter extends FragmentPagerAdapter {

    private int pageCount = 3;
    private String[] tabTitles = {"Tab1", "Tab2", "Tab3"};
    private Context context;

    public MainActivityFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return pageCount;
    }
}
