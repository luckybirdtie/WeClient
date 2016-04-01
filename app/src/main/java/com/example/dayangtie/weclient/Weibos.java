package com.example.dayangtie.weclient;

import com.sina.weibo.sdk.openapi.models.Status;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.SimpleTimeZone;

/**
 * Created by dayangtie on 3/03/16.
 * Data structure for Weibo that will be parsed and used in item view.
 */
public class Weibos implements Serializable{

    private static final long serialVersionUID = 1L;

    private String id;
    private String userName;
    private String time;
    private String content;
    private String avator;
    private String imageThumbnail = null;
    private String imageOriginal = null;
    private ArrayList<String> PicUrls = null;
    private int repostCount;
    private int commentsCount;
    private int attitudesCount;
    private String source;
    private boolean favorited;
    private Status retweetedStatus;
    private int type;

    public Weibos(String id, String userName, String time, String content, String avator, String imageThumbnail, String imageOriginal,
                  ArrayList<String> picUrls, int repostCount, int commentsCount, int attitudesCount, String source, boolean favorited, Status retweetedStatus){
        this.id = id;
        this.userName = userName;
        this.time = time;
        this.content = content;
        this.avator = avator;
        this.imageThumbnail = imageThumbnail;
        this.imageOriginal = imageOriginal;
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

    public String getImageOriginal() { return imageOriginal;}

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

    public Status getRetweetedStatus() { return retweetedStatus; }

    public Weibos getRetweetedWeibos(){
        return new Weibos(retweetedStatus.id, retweetedStatus.user.screen_name, retweetedStatus.created_at, retweetedStatus.text, retweetedStatus.user.profile_image_url, retweetedStatus.thumbnail_pic, retweetedStatus.original_pic, retweetedStatus.pic_urls,
                retweetedStatus.reposts_count, retweetedStatus.comments_count, retweetedStatus.attitudes_count, retweetedStatus.source, retweetedStatus.favorited, retweetedStatus.retweeted_status);
    }

    //Two types of ViewHolder
    //0: Text tweet with no associated forwarded tweet, corresponding ViewHolder is ViewHolderRetweet;
    //1: Text tweet with an associated forwarded tweet, corresponding ViewHolder is ViewHolderOriginal;

    public int getType(){
        if (retweetedStatus == null)
            return 0;
        else
            return 1;

    }
}
