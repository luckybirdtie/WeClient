package com.example.dayangtie.weclient;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.SimpleTimeZone;

/**
 * Created by dayangtie on 3/03/16.
 * Data structure for Weibo that will be parsed and used in item view.
 */
public class Weibos {

    private String id;
    private String userName;
    private String time;
    private String content;
    private String avator;
    private String imageThumbnail = null;
    private ArrayList<String> PicUrls = null;
    private int repostCount;
    private int commentsCount;
    private int attitudesCount;
    private String source;
    private boolean favorited;
    private Object retweetedStatus;
    private int type;

    public Weibos(String id, String userName, String time, String content, String avator, String imageThumbnail,
                  ArrayList<String> picUrls, int repostCount, int commentsCount, int attitudesCount, String source, boolean favorited, Object retweetedStatus){
        this.id = id;
        this.userName = userName;
        this.time = time;
        this.content = content;
        this.avator = avator;
        this.imageThumbnail = imageThumbnail;
        this.PicUrls = picUrls;
        this.repostCount = repostCount;
        this.commentsCount = commentsCount;
        this.attitudesCount = attitudesCount;
        this.source = source;
        this.favorited = favorited;
        this.retweetedStatus = retweetedStatus;
    }

    public String getUserName(){
        return userName;
    }

    public String getId() { return id; }

    public String getTime(){
        return time;
    }

    public String getContent(){
        return content;
    }

    public String getAvator(){
        return avator;
    }

    public String getContentImage(){
        return imageThumbnail;
    }

    public ArrayList<String> getPics() {return PicUrls;}

    public ArrayList<String> getLargePics(){
        ArrayList<String> largePicUrls = new ArrayList<>();
        for (String s : PicUrls){
            largePicUrls.add(s.replace("thumbnail", "large"));
        }
        return largePicUrls;
    }

    public int getRepostCount() {return repostCount;}

    public int getCommentsCount() {return commentsCount;}

    public int getAttitudesCount() {return attitudesCount;}

    public String getSource() {return source;}

    public boolean isFavorited() {return favorited;}

    //Four type of ViewHolder
    //0: Text tweet with no associated forwarded tweet, corresponding ViewHolder is ViewHolderOriginalPureText;
    //1: Text tweet with an associated forwarded tweet, corresponding ViewHolder is ;
    //2: Image tweet with no associated forwarded tweet, corresponding ViewHolder is ViewHolderOriginalWithImage;
    //3: Image tweet with an associated forwarded tweet, corresponding ViewHolder is ;
    public int getType(){
        if (PicUrls != null){
            if (retweetedStatus != null)
            {return 2;}else
            {return 2;}
        }else{
            if (retweetedStatus != null)
            {return 0;}else
            {return 0;}
        }
    }
}
