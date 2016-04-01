package com.example.dayangtie.weclient;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dayangtie.weclient.sina.AccessTokenKeeper;
import com.example.dayangtie.weclient.sina.Constants;
import com.example.dayangtie.weclient.util.TimeCalculator;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.utils.LogUtil;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by dayangtie on 4/03/16.
 */
public class FirstPageFragment extends android.support.v4.app.Fragment implements WeiboRecyclerViewAdapter.MyFragmentListener{

    private static final String TAG = "FirstPageFragment";

    /** 当前 Token 信息 */
    private Oauth2AccessToken mAccessToken;
    /** 用于获取微博信息流等操作的API */
    private StatusesAPI mStatusesAPI;

    private List<Weibos> weiboList; //第一次load
    private List<Weibos> moreWeibo; //当下拉重新load数据时
    private RecyclerView rv;
    private WeiboRecyclerViewAdapter adapter;
    private long earliest_id; //每次Load之后，最早的一条微博所对应的id
    private boolean viewRenderedFirstTime = true;
    private boolean refreshingView = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private static FragmentManager mFragManager;
    private static Context activityContext;
    private ViewGroup container;
    WeiboListApp mApp;

    private static int i = 0;

    public FirstPageFragment(){
    }

    public static FirstPageFragment newInstance(FragmentManager fm, Context context){
        mFragManager = fm;
        activityContext = context;
        return new FirstPageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first_page, container, false);
        weiboList = new ArrayList<Weibos>();
        moreWeibo = new ArrayList<Weibos>();
        progressBar= (ProgressBar) view.findViewById(R.id.pbLoading);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Configure the refreshing colors of progress circle
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mApp = (WeiboListApp) getActivity().getApplicationContext();

        rv = (RecyclerView) swipeRefreshLayout.findViewById(R.id.firstpage_recyclerview);
        // 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(getActivity());
        // 对statusAPI实例化
        mStatusesAPI = new StatusesAPI(getActivity(), Constants.APP_KEY, mAccessToken);
        //初始化fragment
        new MyAsynctask().execute(mStatusesAPI);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewRenderedFirstTime = true;
                refreshingView = true;
                new MyAsynctask().execute(mStatusesAPI);
            }
        });

    }

    @Override
    public void onHolderItemClicked(int position) {
    }

    public class MyAsynctask extends AsyncTask <StatusesAPI, Void, String>{

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(StatusesAPI... params) {
            if (viewRenderedFirstTime) {
                weiboList.clear();
                return params[0].friendsTimeline(0L, 0L, 50, 1, false, 0, false);
            }else{
                return params[0].friendsTimeline(0L, earliest_id, 50, 1, false, 0, false);
            }
        }


        @Override
        protected void onPostExecute(String s) {
            StatusList statuses = StatusList.parse(s);
            Weibos weibo;
            progressBar.setVisibility(View.INVISIBLE);

            if (viewRenderedFirstTime) {
                if (statuses != null && statuses.total_number > 0) {
                    for (com.sina.weibo.sdk.openapi.models.Status status : statuses.statusList) {
                            weibo = new Weibos(status.id, status.user.screen_name, status.created_at, status.text, status.user.profile_image_url, status.thumbnail_pic, status.original_pic, status.pic_urls,
                                    status.reposts_count, status.comments_count, status.attitudes_count, status.source, status.favorited, status.retweeted_status);
                            weiboList.add(weibo);
                            earliest_id = Long.parseLong(status.id);
//                        Log.d(TAG, (i++) + " :onPostExecute: " + weibo.getContent() + " : " + earliest_id);
//                        if (status.pic_urls != null){
//                            for (int i = 0; i < status.pic_urls.size(); i++)
//                                Log.d(TAG, weibo.getLargePics().get(i));
//                        }

                    }
                    mApp.setList(weiboList);
                }
            }else{
                if (statuses != null && statuses.total_number > 0) {
                    moreWeibo.clear();
                    for (com.sina.weibo.sdk.openapi.models.Status status : statuses.statusList) {
                        weibo = new Weibos(status.id, status.user.screen_name, status.created_at, status.text, status.user.profile_image_url, status.thumbnail_pic, status.original_pic, status.pic_urls,
                                status.reposts_count, status.comments_count, status.attitudes_count, status.source, status.favorited, status.retweeted_status);
                        if (!weibo.getId().equals(String.valueOf(earliest_id))){
                                moreWeibo.add(weibo);
                                earliest_id = Long.parseLong(status.id);
//                            Log.d(TAG, (i++) + " :onPostExecute addmore after compare: " + weibo.getContent() + " : " + earliest_id);
                        }
                    }
                }
            }

            if (viewRenderedFirstTime && (!refreshingView)) {
                setupRecyclerView(rv, weiboList);
                viewRenderedFirstTime = false;
            }else if (viewRenderedFirstTime && refreshingView){
                onRefreshComplete(weiboList);
            } else{
                weiboList.addAll(moreWeibo);
                int curSize = adapter.getItemCount();
                //Log.d(TAG, String.valueOf(curSize));
                adapter.notifyItemRangeInserted(curSize, weiboList.size() - 1);
            }
        }
    }

    public void setupRecyclerView(RecyclerView rv, List<Weibos> list) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rv.getContext());
        rv.setLayoutManager(linearLayoutManager);
        adapter = new WeiboRecyclerViewAdapter(list, new WeiboRecyclerViewAdapter.MyFragmentListener() {
            @Override
            public void onHolderItemClicked(int position) {
                Log.d(TAG, "onHolderItemClicked: i am clicked");
                Intent i = new Intent(getActivity(), DetailWeiboActivity.class);
                i.putExtra("Position", position);
                startActivity(i);
            }
        });
        rv.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        rv.addItemDecoration(itemDecoration);
        rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                //Log.d(TAG, "yes i am executing:" + viewRenderedFirstTime);
                new MyAsynctask().execute(mStatusesAPI);
            }
        });

    }

    private void onRefreshComplete(List<Weibos> list){

        viewRenderedFirstTime = false;

        //cache returned new dataset
        List<Weibos> cacheList = new ArrayList<>();
        for (Weibos weibo : list){
            cacheList.add(weibo);
        }
        //clear old data associated with adapter

        adapter.clear();
//        Log.d(TAG, "onRefreshComplete: itemcount" + adapter.getItemCount());
        /** weiboList = localWeiboList; 最开始用了这种赋值，这个错误导致了刷新后，recyclerView的onScrollListener不起作用了。因为在adapter里的方法 getItemCount()使用的是原weiboList, 在此处，对weiboList实例重置，将导致weiboList的变化不再对adapter起作用。*/
        for (Weibos weibo : cacheList){
            weiboList.add(weibo);
        }

//        Log.d(TAG, "onRefreshComplete: " + weiboList.size());
//        Log.d(TAG, "onRefreshComplete: itemcount" + adapter.getItemCount());
        adapter.addAll(weiboList);
//        Log.d(TAG, "onRefreshComplete: adapter size" + adapter.getItemCount());
        //refresh completed, stop it
        swipeRefreshLayout.setRefreshing(false);
        refreshingView = false;
    }
}
