package com.example.dayangtie.weclient;

import java.util.SimpleTimeZone;

/**
 * Created by dayangtie on 3/03/16.
 * Data structure for Weibo that will be parsed and used in item view.
 */
public class Weibos {

    private String userName;
    private String time;
    private String content;
    private String avator;
    private String image;
    private int type;

    public Weibos(String userName, String time, String content, String avator){
        this.userName = userName;
        this.time = time;
        this.content = content;
        this.avator = avator;
        image = null;
    }

    public Weibos(String userName, String time, String content, String avator, String image){
        this(userName, time, content, avator);
        this.image = image;
    }

    public String getUserName(){
        return userName;
    }

    public String getTime(){
        return time;
    }

    public String getContent(){
        return content;
    }

    public String getAvator(){
        return avator;
    }

    public String getImage(){
        return image;
    }

    public int getType(){
        if (image != null){
            return 1;
        }else{
            return 0;
        }
    }
}
