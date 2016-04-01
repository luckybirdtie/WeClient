package com.example.dayangtie.weclient;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dayangtie.weclient.util.DeviceDimensionsHelper;
import com.example.dayangtie.weclient.util.TimeCalculator;
import com.example.dayangtie.weclient.util.ConvertNormalStringToSpannableString;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dayangtie on 3/03/16.
 */
public class WeiboRecyclerViewAdapter extends RecyclerView.Adapter {
    private static final String TAG = "WeiboRecyclerViewAdapter";

    private int SCREEN_WIDTH;
    private float screenWidthInDp;
    private int margin;

    private List<Weibos> weibosList;
    private ViewGroup parent;

    private MyFragmentListener mFragmentListener;

    public WeiboRecyclerViewAdapter(List<Weibos> weibosList, MyFragmentListener listener){
        this.weibosList = weibosList;
        mFragmentListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return weibosList.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewHolder != null) {
            final int pos = viewHolder.getLayoutPosition();
        }
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //Log.d(TAG, String.valueOf(viewType));
        this.parent = parent;
        switch (viewType){
            case 0:
                View v1 = inflater.inflate(R.layout.viewholder_original, parent, false);
                viewHolder = new ViewHolderOriginal(v1, new ViewHolderOriginal.IMyViewHolderClicks() {
                    @Override
                    public void onViewHolderClick(View caller, int position) {
                        mFragmentListener.onHolderItemClicked(position);
                    }
                });
                break;
            case 1:
                View v2 = inflater.inflate(R.layout.viewholder_retweet, parent, false);
                viewHolder = new ViewHolderRetweet(v2, new ViewHolderRetweet.IMyViewHolderClicks() {
                    @Override
                    public void onViewHolderClick(View caller, int position) {
                        mFragmentListener.onHolderItemClicked(position);
                    }
                });
                break;
            default:
                try {
                    throw new Exception("incorrect input type");
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //Log.d(TAG, String.valueOf(holder.getItemViewType()));
        switch (holder.getItemViewType()){
            case 0:
                ViewHolderOriginal viewHolderOriginal = (ViewHolderOriginal) holder;
                configViewHolderOriginalWeibo(viewHolderOriginal, position);
                break;
            case 1:
                ViewHolderRetweet viewHolderRetweet = (ViewHolderRetweet) holder;
                configViewHolderRetweet(viewHolderRetweet, position);
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
        //Log.d(TAG, "addAll listsize" + list.size());
        //Log.d(TAG, "addAll: before weibolist" + weibosList.size());
        //weibosList.addAll(list);
        //Log.d(TAG, "addAll: " + weibosList.size());
        notifyDataSetChanged();
    }

//    public void configViewHolderWeiboText(ViewHolderRetweet holder, int position){
//
//        Weibos weibos = weibosList.get(position);
//        holder.getTv_userName().setText(weibos.getUserName());
//        holder.getTv_content().setText(ConvertNormalStringToSpannableString.convertNormalStringToSpannableString(parent.getContext(), weibos.getContent()));
//        //holder.getTv_time().setText(weibos.getTime());
//        holder.getTv_time().setText(timeDiff(weibos));
//        Picasso.with(parent.getContext()).load(weibos.getAvator()).into(holder.getAvatorImage());
//
//       Log.d(TAG, "text adapter position: " + position);
//       Log.d(TAG, "text layout position: " + holder.getLayoutPosition());
//    }

    public void configViewHolderOriginalWeibo(ViewHolderOriginal holder, int position){

        holder.setPosition(position);
        Weibos weibos = weibosList.get(position);
        holder.getTv_userName().setText(weibos.getUserName());
        holder.getTv_content().setText(ConvertNormalStringToSpannableString.convertNormalStringToSpannableString(parent.getContext(), weibos.getContent()));
        holder.getTv_time().setText(timeDiff(weibos));
        Picasso.with(parent.getContext()).load(weibos.getAvator()).into(holder.getAvatorImage());
        holder.getGridLayout().removeAllViews();
//
//        Log.d(TAG, "adapter position: " + position);
//        Log.d(TAG, "layout position: " + holder.getLayoutPosition());
        try {
            if (weibos.getPics() != null )
                configLayoutParams(holder.getGridLayout().getContext(), holder.getGridLayout(), weibos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void configViewHolderRetweet(ViewHolderRetweet holder, int position){

        holder.setPosition(position);
        Weibos weibos = weibosList.get(position);
        Weibos retweetedWeibos = weibos.getRetweetedWeibos();
        holder.getTv_userName().setText(weibos.getUserName());
        holder.getTv_content().setText(ConvertNormalStringToSpannableString.convertNormalStringToSpannableString(parent.getContext(), weibos.getContent()));
        holder.getTv_time().setText(timeDiff(weibos));
        Picasso.with(parent.getContext()).load(weibos.getAvator()).into(holder.getAvatorImage());
        holder.getGridLayout().removeAllViews();

        try {
            if (weibos.getPics() != null )
            configLayoutParams(holder.getGridLayout().getContext(), holder.getGridLayout(), weibos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.getTv_retweeted_user_profile().setText(ConvertNormalStringToSpannableString.convertNormalStringToSpannableString(parent.getContext(), "@" + weibos.getRetweetedStatus().user.screen_name));
        holder.getTv_content_retweet().setText(ConvertNormalStringToSpannableString.convertNormalStringToSpannableString(parent.getContext(), weibos.getRetweetedStatus().text));
        holder.getGridLayoutRetweet().removeAllViews();

        try {
            if (retweetedWeibos.getPics() != null)
            configLayoutParams(holder.getGridLayoutRetweet().getContext(), holder.getGridLayoutRetweet(), retweetedWeibos);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void configLayoutParams(Context context, GridLayout gridLayout, Weibos wb) throws Exception {
        int imgCount = wb.getPics().size();
        SCREEN_WIDTH = DeviceDimensionsHelper.getDisplayWidth(context);
        screenWidthInDp = DeviceDimensionsHelper.convertPixelsToDp(SCREEN_WIDTH, context);
        margin = SCREEN_WIDTH / 150;
        int imageWidthSingle = (int) (SCREEN_WIDTH / 2);
        int imageWidth = (SCREEN_WIDTH - 2 * margin) / 3;
        float tweakRatio;
        Bitmap bm;

        //GridLayout.LayoutParams glParams = new GridLayout.LayoutParams();
        switch (imgCount){
            case 1 :
                gridLayout.setColumnCount(1);
                ImageView iv = new ImageView(gridLayout.getContext());
                bm = Ion.with(context).load(wb.getImageOriginal()).asBitmap().get();
               // float ratio = bm.getWidth() / bm.getHeight();
                tweakRatio = imageWidthSingle / bm.getWidth();
                Ion.with(iv).resize(imageWidthSingle, (int) (imageWidthSingle * tweakRatio)).centerCrop().load(wb.getImageOriginal());
                //Ion.with(iv).load(wb.getImageOriginal());
                gridLayout.addView(iv);
                //glParams.columnSpec = GridLayout.
                break;
            case 2 :
            case 3 :
                gridLayout.setColumnCount(3);
                List<ImageView> ivs1 = new ArrayList<>();
                for (int k = 0; k < imgCount; k++){
                    ivs1.add(new ImageView(gridLayout.getContext()));
//                    bm = Ion.with(context).load(wb.getLargePics().get(k)).asBitmap().get();
//                    tweakRatio = imageWidth / bm.getWidth();
                    Ion.with(ivs1.get(k)).resize(imageWidth, imageWidth).centerCrop().load(wb.getLargePics().get(k));
                    GridLayout.LayoutParams glParams = new GridLayout.LayoutParams();
                    glParams.columnSpec = GridLayout.spec(k);
                    glParams.setGravity(Gravity.FILL);
                    glParams.width = imageWidth;
                    if ((k + 1) % 3 != 0){
                        glParams.setMarginEnd(margin);
                    }
                    //glParams.setGravity(Gravity.FILL);
                    gridLayout.addView(ivs1.get(k), glParams);
                }
                break;
            case 4 :
            case 5 :
            case 6 :
                gridLayout.setColumnCount(3);
                List<ImageView> ivs2 = new ArrayList<>();
                for (int k = 0; k < imgCount; k++){
                    ivs2.add(new ImageView(gridLayout.getContext()));
                    bm = Ion.with(context).load(wb.getLargePics().get(k)).asBitmap().get();
                    tweakRatio = imageWidth / bm.getWidth();
                    Ion.with(ivs2.get(k)).resize(imageWidth, imageWidth).centerCrop().load(wb.getLargePics().get(k));
                    GridLayout.LayoutParams glParams = new GridLayout.LayoutParams();
                    glParams.columnSpec = GridLayout.spec(k % 3);
                    glParams.setGravity(Gravity.FILL);
                    glParams.width = imageWidth;
                    glParams.setMargins(0, 0, 0, margin);
                    if ((k + 1) % 3 != 0) {
                        glParams.setMarginEnd(margin);
                    }
                    gridLayout.addView(ivs2.get(k), glParams);
                }
                break;
            case 7 :
            case 8 :
            case 9 :
                gridLayout.setColumnCount(3);
                //gridLayout.setOrientation(GridLayout.HORIZONTAL);
                List<ImageView> ivs3 = new ArrayList<>();
                for (int k = 0; k < imgCount; k++) {
                    ivs3.add(new ImageView(gridLayout.getContext()));
                    Ion.with(ivs3.get(k)).resize(imageWidth, imageWidth).centerCrop().load(wb.getLargePics().get(k));
                    GridLayout.LayoutParams glParams = new GridLayout.LayoutParams();
                    glParams.columnSpec = GridLayout.spec(k % 3);
                    glParams.setGravity(Gravity.FILL);
                    glParams.width = imageWidth;
                    glParams.setMargins(0, 0, 0, margin);
                    if ((k + 1) % 3 != 0) {
                        glParams.setMarginEnd(margin);
                    }
                    gridLayout.addView(ivs3.get(k), glParams);
                }
                break;
            default:
                throw new Exception();
        }
    }

    /**计算此微博发布时间与当前发布时间的差值； format of returned String from Weibo: "Fri Mar 11 00:29:09 +0800 2016"**/
    public String timeDiff(Weibos weibos){
        String dateString = TimeCalculator.getDateString(parent.getContext(), (TimeCalculator.stringToDate(weibos.getTime(), "EEE MMM dd HH:mm:ss Z yyyy")));
        return TimeCalculator.timeSpanTillNow(parent.getContext(), dateString);
    }

    public interface MyFragmentListener{

        public void onHolderItemClicked(int position);
    }
}
