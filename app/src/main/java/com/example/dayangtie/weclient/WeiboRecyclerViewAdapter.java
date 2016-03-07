package com.example.dayangtie.weclient;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dayangtie on 3/03/16.
 */
public class WeiboRecyclerViewAdapter extends RecyclerView.Adapter {

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
            case 1:
                View v2 = inflater.inflate(R.layout.viewholder_text, parent, false);
                viewHolder = new ViewHolderWeiboText(v2);
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
        switch (holder.getItemViewType()){
            case 0:
                ViewHolderWeiboText viewHolderWeiboText = (ViewHolderWeiboText) holder;
                configViewHolderWeiboText(viewHolderWeiboText, position);
                break;
            case 1:
                break;
        }
    }

    @Override
    public int getItemCount() {
        //Log.d("???????", String.valueOf(weibosList.size()));
        return weibosList.size();
    }

    public void configViewHolderWeiboText(ViewHolderWeiboText holder, int position){
        Weibos weibos = weibosList.get(position);
        holder.getTv_userName().setText(weibos.getUserName());
        //Log.d("sssssssss", weibos.getUserName());
        holder.getTv_content().setText(weibos.getContent());
        holder.getTv_time().setText(weibos.getTime());
        Picasso.with(parent.getContext()).load(weibos.getAvator()).into(holder.getAvatorImage());
    }
}