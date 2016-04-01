package com.example.dayangtie.weclient;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by dayangtie on 3/03/16.
 */
public class ViewHolderRetweet extends RecyclerView.ViewHolder implements View.OnClickListener{

    private static final String TAG = "ViewHolderRetweet";
    private ImageView avatorImage;
    private TextView tv_userName, tv_time, tv_content, tv_content_retweet, tv_retweeted_user_profile;
    private GridLayout gridLayout, gridLayoutRetweet;
    private int position;
    public IMyViewHolderClicks mListener;

    public ViewHolderRetweet(View itemView, IMyViewHolderClicks listener) {
        super(itemView);
        mListener = listener;
        avatorImage = (ImageView) itemView.findViewById(R.id.img_avator);
        tv_userName = (TextView) itemView.findViewById(R.id.txt_username);
        tv_time = (TextView) itemView.findViewById(R.id.txt_time);
        tv_content = (TextView) itemView.findViewById(R.id.txt_weibo_content);
        gridLayout = (GridLayout) itemView.findViewById(R.id.layout_grid);
        gridLayoutRetweet = (GridLayout) itemView.findViewById(R.id.layout_grid_retweet);
        tv_content_retweet = (TextView) itemView.findViewById(R.id.txt_weibo_content_retweet);
        tv_retweeted_user_profile = (TextView) itemView.findViewById(R.id.retweeted_user_profile);
        itemView.setOnClickListener(this);
       // avatorImage.setOnClickListener(this);
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

    public GridLayout getGridLayout(){
        return gridLayout;
    }

    public GridLayout getGridLayoutRetweet() { return gridLayoutRetweet; }

    public TextView getTv_content_retweet() { return tv_content_retweet; }

    public TextView getTv_retweeted_user_profile() { return tv_retweeted_user_profile; }

    public void setPosition(int position){
        this.position = position;
    }



    @Override
    public void onClick(View v) {
        mListener.onViewHolderClick(v, position);
    }

    public static interface IMyViewHolderClicks{
        public void onViewHolderClick(View caller, int position);
    }
}
