package com.example.dayangtie.weclient;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by dayangtie on 4/03/16.
 */
public class FirstPageFragment extends android.support.v4.app.Fragment{

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

    public FirstPageFragment(){
    }

    public static FirstPageFragment newInstance(){
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

//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        Log.d(TAG, TimeCalculator.getDateString(getActivity(), calendar.getTime()));

//        Log.d(TAG, String.valueOf(DateFormat.is24HourFormat(getActivity())));


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

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

    public class MyAsynctask extends AsyncTask <StatusesAPI, Void, String>{

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(StatusesAPI... params) {
            if (viewRenderedFirstTime) {
                weiboList.clear();
                return params[0].friendsTimeline(0L, 0L, 25, 1, false, 0, false);
            }else{
                return params[0].friendsTimeline(0L, earliest_id, 10, 1, false, 0, false);
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
                        if (status.thumbnail_pic != null) {
                            weibo = new Weibos(status.user.screen_name, status.created_at, status.text, status.user.profile_image_url, status.thumbnail_pic);
                            weiboList.add(weibo);
                            //Log.d("since_id", status.id);
                            earliest_id = Long.parseLong(status.id);
                        } else {
                            weibo = new Weibos(status.user.screen_name, status.created_at, status.text, status.user.profile_image_url);
                            weiboList.add(weibo);
                            earliest_id = Long.parseLong(status.id);
                            //Log.d("since_id", status.id);
                        }
                    }
                }
            }else{
                if (statuses != null && statuses.total_number > 0) {
                    moreWeibo.clear();
                    for (com.sina.weibo.sdk.openapi.models.Status status : statuses.statusList) {
                        if (status.thumbnail_pic != null) {
                            weibo = new Weibos(status.user.screen_name, status.created_at, status.text, status.user.profile_image_url, status.thumbnail_pic);
                            moreWeibo.add(weibo);
                            //Log.d("since_id", status.id);
                            earliest_id = Long.parseLong(status.id);
                        } else {
                            weibo = new Weibos(status.user.screen_name, status.created_at, status.text, status.user.profile_image_url);
                            moreWeibo.add(weibo);
                            earliest_id = Long.parseLong(status.id);
                            //Log.d("since_id", status.id);
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
                Log.d(TAG, String.valueOf(curSize));
                adapter.notifyItemRangeInserted(curSize, weiboList.size() - 1);
            }
        }
    }

    public void setupRecyclerView(RecyclerView rv, List<Weibos> list) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rv.getContext());
        rv.setLayoutManager(linearLayoutManager);
        adapter = new WeiboRecyclerViewAdapter(list);
        rv.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        rv.addItemDecoration(itemDecoration);
        rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d(TAG, "yes i am executing:" + viewRenderedFirstTime);
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

        /** weiboList = localWeiboList; 最开始用了这种赋值，这个错误导致了刷新后，recyclerView的onScrollListener不起作用了。因为在adapter里的方法 getItemCount()使用的是原weiboList, 在此处，对weiboList实例重置，将导致weiboList的变化不再对adapter起作用。*/
        for (Weibos weibo : cacheList){
            weiboList.add(weibo);
        }

        adapter.addAll(weiboList);
        //refresh completed, stop it
        swipeRefreshLayout.setRefreshing(false);
        refreshingView = false;
    }
}
