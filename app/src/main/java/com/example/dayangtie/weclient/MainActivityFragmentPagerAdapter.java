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
    private String[] tabTitles = {"微博", "公共微博", "用户"};
    private Context activityContext;
    private FragmentManager fm;

    public MainActivityFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.fm = fm;
        activityContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return FirstPageFragment.newInstance(fm, activityContext);
            case 1:
                return PageFragment.newInstance(position + 1);
            case 2:
                return PageFragment.newInstance(position + 1);
            default:
                return null;
        }

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
