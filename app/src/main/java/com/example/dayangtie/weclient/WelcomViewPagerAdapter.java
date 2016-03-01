package com.example.dayangtie.weclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

/**
 * Created by dayangtie on 28/02/16.
 */
public class WelcomViewPagerAdapter extends PagerAdapter {

    private List<View> views;
    private Activity mActivity;

    public WelcomViewPagerAdapter(List<View> views, Activity activity){
        this.views = views;
        mActivity = activity;
    }

    @Override //每次滑动时销毁当前页面
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
        //super.destroyItem(container, position, object);

    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }


    @Override //每次滑动时生成的页面
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        if (position == (views.size() - 1)){
            Button enterMainPageButton = (Button) container.findViewById(R.id.button_enter_main_page);
            enterMainPageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setGuided();
                    goHome();
                }
            });
        }

        return views.get(position);

    }

    private void setGuided() {
        SharedPreferences mSharedPreferences = mActivity.getSharedPreferences(SplashActivity.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("isFirstLaunch", false);
        editor.apply();
    }

    private void goHome() {
        Intent intent = new Intent(mActivity, MainPage.class);
        mActivity.startActivity(intent);
        mActivity.finish();
    }


    @Override
    public int getCount() {

        if (views != null)
            return views.size();
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        //return false;
       return view == object;
    } //object is the key returned by intantiateItem. 这个OBJECT可以为ANYTHING， 这里因为返回的是该页面（View）, 直接跟当前页面对比就行了。如果MATCH了，返回TRUE。ß
}
