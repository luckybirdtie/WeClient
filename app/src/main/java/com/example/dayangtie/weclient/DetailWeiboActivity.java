package com.example.dayangtie.weclient;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DetailWeiboActivity extends AppCompatActivity {
    private static final String TAG = "DetailWeiboActivity";
    private int position;
    private Weibos weibo;
    private Weibos retweetedWeibo;
    private WeiboContentFragment weiboContentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_weibo);
        position = getIntent().getIntExtra("Position", 0);
//        WeiboListApp mApp = (WeiboListApp) getApplicationContext();
//        weibo = mApp.getList().get(position);
        weiboContentFragment = WeiboContentFragment.newInstance(position);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_content, weiboContentFragment);
        ft.commit();



    }
}
