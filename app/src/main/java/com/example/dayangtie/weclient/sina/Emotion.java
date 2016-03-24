package com.example.dayangtie.weclient.sina;

import android.util.Log;

import com.sina.weibo.sdk.openapi.models.Tag;

/**
 * Created by dayangtie on 18/03/16.
 */
public class Emotion {

    private String phrase;
    private String type;
    private String url;
    private String hot;
    private String common;
    private String category;
    private String icon;
    private String value;
    private String picid;

    public String getPhrase(){
        return phrase;
    }

    public String getIcon(){
        return icon;
    }

    public String getUrl(){
        return url;
    }

    public void setIconNameFirstChar(){
        StringBuilder sb = new StringBuilder(icon.substring(0, icon.lastIndexOf("/") + 1));
        sb.append("i");
        sb.append(icon.substring(icon.lastIndexOf("/") + 1, icon.length()));
        icon = sb.toString();
    }

    public void setToLowerCase(){
        icon = icon.toLowerCase();
    }

    public String getIconName(){
        return icon.substring(icon.lastIndexOf("/") + 1, icon.length());
    }

    public void removeSpecialChar(){
        icon = icon.replace('&', 'i');
    }

    public void appendGifSuffix(){
        StringBuilder sb = new StringBuilder(icon);
        sb.append(".gif");
        icon = sb.toString();
    }

}
