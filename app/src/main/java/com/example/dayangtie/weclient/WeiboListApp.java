package com.example.dayangtie.weclient;

import android.app.Application;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dayangtie on 1/04/16.
 */
public class WeiboListApp extends Application {
    private List<Weibos> weiboList;

    public void setList(List<Weibos> weiboList){
        this.weiboList = weiboList;
    }

    public List<Weibos> getList(){
        return weiboList;
    }
}
