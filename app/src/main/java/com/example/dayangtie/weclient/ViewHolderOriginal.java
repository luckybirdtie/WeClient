package com.example.dayangtie.weclient;

import android.app.ActionBar;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by dayangtie on 3/03/16.
 */
public class ViewHolderOriginal extends RecyclerView.ViewHolder implements View.OnClickListener{

    private static final String TAG = "ViewHolderOriginal";

    private ImageView avatorImage, contentImage;
    private TextView tv_userName, tv_time, tv_content;
    private GridLayout gridLayout;
    public IMyViewHolderClicks mListener;
    private int position;


    public ViewHolderOriginal(View itemView, IMyViewHolderClicks listener) {
        super(itemView);
        mListener = listener;
        avatorImage = (ImageView) itemView.findViewById(R.id.img_avator);
        tv_userName = (TextView) itemView.findViewById(R.id.txt_username);
        tv_time = (TextView) itemView.findViewById(R.id.txt_time);
        tv_content = (TextView) itemView.findViewById(R.id.txt_weibo_content);
        gridLayout = (GridLayout) itemView.findViewById(R.id.layout_grid);
        itemView.setOnClickListener(this);
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

    public ImageView getIvContent(){
        return contentImage;
    }

    public void setContentImage(ImageView imageView){
        contentImage = imageView;
    }

    public GridLayout getGridLayout(){
        return gridLayout;
    }

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

