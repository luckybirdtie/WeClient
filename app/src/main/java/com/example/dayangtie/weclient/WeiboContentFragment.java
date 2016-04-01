package com.example.dayangtie.weclient;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dayangtie.weclient.util.ConvertNormalStringToSpannableString;
import com.example.dayangtie.weclient.util.DeviceDimensionsHelper;
import com.example.dayangtie.weclient.util.TimeCalculator;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeiboContentFragment extends Fragment {

    private static final String TAG = "WeiboContentFragment";
    private WeiboListApp mApp;
    private Weibos weibo;
    private Weibos retweetedWeibo;
    private ImageView avatorImage, contentImage;
    private TextView tv_userName, tv_time, tv_content, tv_content_retweet, tv_retweeted_user_profile;
    private GridLayout gridLayout, gridLayoutRetweet;
    private int SCREEN_WIDTH;
    private float screenWidthInDp;
    private int margin;


    public WeiboContentFragment() {
        // Required empty public constructor
    }

    public static WeiboContentFragment newInstance(int position){
        WeiboContentFragment fragment = new WeiboContentFragment();
        Bundle args = new Bundle();
        args.putInt("Position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int position = getArguments().getInt("Position");
        mApp = (WeiboListApp) getActivity().getApplicationContext();
        weibo = mApp.getList().get(position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_weibo_content, container, false);
        configContentView(itemView); //微博内容的显示碎片
        
        return itemView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void configContentView(View itemView) {
        avatorImage = (ImageView) itemView.findViewById(R.id.img_avator);
        tv_userName = (TextView) itemView.findViewById(R.id.txt_username);
        tv_time = (TextView) itemView.findViewById(R.id.txt_time);
        tv_content = (TextView) itemView.findViewById(R.id.txt_weibo_content);
        gridLayout = (GridLayout) itemView.findViewById(R.id.layout_grid);
        tv_userName.setText(weibo.getUserName());
        tv_content.setText(ConvertNormalStringToSpannableString.convertNormalStringToSpannableString(getActivity(), weibo.getContent()));
        tv_time.setText(timeDiff(weibo));
        Picasso.with(getActivity()).load(weibo.getAvator()).into(avatorImage);
        gridLayoutRetweet = (GridLayout) itemView.findViewById(R.id.layout_grid_retweet);
        tv_content_retweet = (TextView) itemView.findViewById(R.id.txt_weibo_content_retweet);
        tv_retweeted_user_profile = (TextView) itemView.findViewById(R.id.retweeted_user_profile);
        gridLayout.removeAllViews();
        try {
            if (weibo.getPics() != null )
                configLayoutParams(gridLayout.getContext(), gridLayout, weibo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (weibo.getRetweetedStatus() != null){
            itemView.findViewById(R.id.retweet_content).setVisibility(View.VISIBLE);
            retweetedWeibo = weibo.getRetweetedWeibos();
            tv_retweeted_user_profile.setText(ConvertNormalStringToSpannableString.convertNormalStringToSpannableString(getActivity(), "@" + weibo.getRetweetedStatus().user.screen_name));
            tv_content_retweet.setText(ConvertNormalStringToSpannableString.convertNormalStringToSpannableString(getActivity(), weibo.getRetweetedStatus().text));
            gridLayoutRetweet.removeAllViews();

            try {
                if (retweetedWeibo.getPics() != null)
                    configLayoutParams(gridLayoutRetweet.getContext(), gridLayoutRetweet, retweetedWeibo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String timeDiff(Weibos weibos){
        String dateString = TimeCalculator.getDateString(getActivity(), (TimeCalculator.stringToDate(weibos.getTime(), "EEE MMM dd HH:mm:ss Z yyyy")));
        return TimeCalculator.timeSpanTillNow(getActivity(), dateString);
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
}
