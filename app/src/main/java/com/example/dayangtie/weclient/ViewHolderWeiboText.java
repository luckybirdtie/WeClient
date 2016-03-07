package com.example.dayangtie.weclient;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by dayangtie on 3/03/16.
 */
public class ViewHolderWeiboText extends RecyclerView.ViewHolder {

    private ImageView avatorImage;
    private TextView tv_userName, tv_time, tv_content;

    public ViewHolderWeiboText(View itemView) {
        super(itemView);
        avatorImage = (ImageView) itemView.findViewById(R.id.img_avator);
        tv_userName = (TextView) itemView.findViewById(R.id.txt_username);
        tv_time = (TextView) itemView.findViewById(R.id.txt_time);
        tv_content = (TextView) itemView.findViewById(R.id.txt_weibo_content);
    }

    public ImageView getAvatorImage(){
        return avatorImage;
    }

    public void setAvatorImage(ImageView imageView){
        avatorImage = imageView;
    }

    public TextView getTv_userName(){
        return tv_userName;
    }

    public void setTv_userName(TextView textView){
        tv_userName = textView;
    }

    public TextView getTv_time(){
        return tv_time;
    }

    public void setTv_time(TextView textView){
        tv_time = textView;
    }

    public TextView getTv_content(){
        return tv_content;
    }

    public void setTv_content(TextView textView){
        tv_content = textView;
    }
}
