package com.example.dayangtie.weclient;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private ViewPager welcomViewPager;
    private WelcomViewPagerAdapter wvPagerAdapter;
    private List<View> views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initViews();
    }

    private void initViews() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(layoutInflater.inflate(R.layout.welcom_page_1, null));
        views.add(layoutInflater.inflate(R.layout.welcom_page_2, null));
        views.add(layoutInflater.inflate(R.layout.welcom_page_3, null));
        wvPagerAdapter = new WelcomViewPagerAdapter(views, this);
        welcomViewPager = (ViewPager) findViewById(R.id.welcome_view_pager);
        welcomViewPager.setAdapter(wvPagerAdapter);
        welcomViewPager.addOnPageChangeListener(this);


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
