package com.example.dayangtie.weclient;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dayangtie.weclient.util.TimeCalculator;
import com.example.dayangtie.weclient.util.ConvertNormalStringToSpannableString;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dayangtie on 3/03/16.
 */
public class WeiboRecyclerViewAdapter extends RecyclerView.Adapter {
    private static final String TAG = "WeiboRecyclerViewAdapter";

    private List<Weibos> weibosList;
    private ViewGroup parent;

    public WeiboRecyclerViewAdapter(List<Weibos> weibosList){
        this.weibosList = weibosList;
    }

    @Override
    public int getItemViewType(int position) {
        return weibosList.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        this.parent = parent;
        switch (viewType){
            // case 0: returned weibo doesn't contain image
            case 0:
                //Log.d("view type is ", String.valueOf(viewType));
                View v1 = inflater.inflate(R.layout.viewholder_text, parent, false);
                viewHolder = new ViewHolderWeiboText(v1);
                break;
            // case 1: image weibo
            case 1:
                View v2 = inflater.inflate(R.layout.viewholder_image, parent, false);
                viewHolder = new ViewHolderWeiboImage(v2);
                break;
            default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                viewHolder = new ViewHolderWeiboText(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //Log.d(TAG, String.valueOf(holder.getItemViewType()));
        switch (holder.getItemViewType()){
            case 0:
                ViewHolderWeiboText viewHolderWeiboText = (ViewHolderWeiboText) holder;
                configViewHolderWeiboText(viewHolderWeiboText, position);
                break;
            case 1:
                ViewHolderWeiboImage viewHolderWeiboImage = (ViewHolderWeiboImage) holder;
                configViewHolderWeiboImage(viewHolderWeiboImage, position);
                break;
        }
    }


    @Override
    public int getItemCount() {
     //   Log.d(TAG, "getItemCount" + weibosList.size());
        return weibosList.size();
    }

    public void clear(){
      //  Log.d(TAG, "before clear" + weibosList.size());
        weibosList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Weibos> list){
       // Log.d(TAG, "addAll" + list.size());
        weibosList.addAll(list);
        notifyDataSetChanged();
    }

    public void configViewHolderWeiboText(ViewHolderWeiboText holder, int position){
        Weibos weibos = weibosList.get(position);
        holder.getTv_userName().setText(weibos.getUserName());
        holder.getTv_content().setText(ConvertNormalStringToSpannableString.convertNormalStringToSpannableString(parent.getContext(), weibos.getContent()));
        //holder.getTv_time().setText(weibos.getTime());
        holder.getTv_time().setText(timeDiff(weibos));
        Picasso.with(parent.getContext()).load(weibos.getAvator()).into(holder.getAvatorImage());
    }

    public void configViewHolderWeiboImage(ViewHolderWeiboImage holder, int position){
        Weibos weibos = weibosList.get(position);
        holder.getTv_userName().setText(weibos.getUserName());
        holder.getTv_content().setText(weibos.getContent());
//        String dateString = TimeCalculator.getDateString(parent.getContext(), (TimeCalculator.stringToDate(weibos.getTime(), "EEE MMM dd HH:mm:ss Z yyyy")));
//        holder.getTv_time().setText(TimeCalculator.timeSpanTillNow(parent.getContext(), dateString));
        holder.getTv_time().setText(timeDiff(weibos));
        Picasso.with(parent.getContext()).load(weibos.getAvator()).into(holder.getAvatorImage());
        //Picasso.with(parent.getContext()).load(weibos.getContentImage()).into(holder.getIvContent());
        Ion.with(holder.getIvContent()).load(weibos.getContentImage()); //able to process .gif file.
    }

    /**计算此微博发布时间与当前发布时间的差值； format of returned String from Weibo: "Fri Mar 11 00:29:09 +0800 2016"**/
    public String timeDiff(Weibos weibos){
        String dateString = TimeCalculator.getDateString(parent.getContext(), (TimeCalculator.stringToDate(weibos.getTime(), "EEE MMM dd HH:mm:ss Z yyyy")));
        return TimeCalculator.timeSpanTillNow(parent.getContext(), dateString);
    }
}
