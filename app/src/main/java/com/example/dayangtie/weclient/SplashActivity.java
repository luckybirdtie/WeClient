/*
 * this activity is the launcher activity. Through a sharedPreference instance it decides which activity to launch - if user launch the app
 * for the first time, the Welcome activity will be started, else it goes into the main page of this app.
 */

package com.example.dayangtie.weclient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.ref.WeakReference;

public class SplashActivity extends AppCompatActivity {

    private boolean isFirstLaunch = false;
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    private static final long SPLASH_DELAY_MILLIS = 3000;
    public static final String SHAREDPREFERENCES_NAME = "app_pref";
    private SharedPreferences mSharedPreferences;

//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case GO_HOME:
//                    goHome();
//                    break;
//                case GO_GUIDE:
//                    goGuide();
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };

    private MyHandler mHandler = new MyHandler(this);

    private void goGuide() {
        Intent intent = new Intent(this, GuideActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    private void goHome() {
        Intent intent = new Intent(this, MainPage.class);
        this.startActivity(intent);
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
    }

    private void init() {
        mSharedPreferences = getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        isFirstLaunch = mSharedPreferences.getBoolean("isFirstLaunch", true);
        if(!isFirstLaunch){
            mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
        else{
            mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
        }
    }
//建立一个弱引用的Handler; 在构建SERVICE的时候尤其应该注意，因为非STATIC的内部类将会依附于其外部类，导致当该HANDLER被保持引用的时候，其外部类无法被回收。
    static class MyHandler extends Handler {
        WeakReference <SplashActivity> mActivity;

        MyHandler (SplashActivity splashActivity) {
            mActivity = new WeakReference<SplashActivity>(splashActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            SplashActivity theActivity = mActivity.get();
            switch (msg.what) {
                case GO_HOME:
                    theActivity.goHome();
                    break;
                case GO_GUIDE:
                    theActivity.goGuide();
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
