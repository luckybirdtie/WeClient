package com.example.dayangtie.weclient;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dayangtie.weclient.sina.AccessTokenKeeper;
import com.example.dayangtie.weclient.sina.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dayangtie on 4/03/16.
 */
public class FirstPageFragment extends android.support.v4.app.Fragment {

    /** 当前 Token 信息 */
    private Oauth2AccessToken mAccessToken;
    /** 用于获取微博信息流等操作的API */
    private StatusesAPI mStatusesAPI;
    private List<Weibos> weiboList;
    private RecyclerView rv;

    public FirstPageFragment(){
    }

    public static FirstPageFragment newInstance(){
        FirstPageFragment fg = new FirstPageFragment();
        return fg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = (RecyclerView) inflater.inflate(R.layout.fragment_first_page, container, false);
        weiboList = new ArrayList<Weibos>();
        setupRecyclerView(rv);
        // 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(getActivity());
        // 对statusAPI实例化
        mStatusesAPI = new StatusesAPI(getActivity(), Constants.APP_KEY, mAccessToken);
        new MyAsynctask().execute(mStatusesAPI);
        return rv;
    }

    public void setupRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(new WeiboRecyclerViewAdapter(weiboList));
    }

    public class MyAsynctask extends AsyncTask <StatusesAPI, Void, String>{

        @Override
        protected String doInBackground(StatusesAPI... params) {
            String response = params[0].friendsTimeline(0L, 0L, 10, 1, false, 0, false);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            StatusList statuses = StatusList.parse(s);
            Weibos weibo;
            if (statuses != null && statuses.total_number > 0) {
                for (com.sina.weibo.sdk.openapi.models.Status status : statuses.statusList){
                    if (status.thumbnail_pic != null){
                        weibo = new Weibos(status.user.screen_name, status.created_at, status.text, status.user.profile_image_url, status.thumbnail_pic);
                        weiboList.add(weibo);
                    }else {
                        weibo = new Weibos(status.user.screen_name, status.created_at, status.text, status.user.profile_image_url);
                        weiboList.add(weibo);
                    }
                }
            }
           // Log.d("????????????", String.valueOf(weiboList.size()));
            setupRecyclerView(rv);
        }
    }

    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                //LogUtil.i(TAG, response);
                if (response.startsWith("{\"statuses\"")) {
                    // 调用 StatusList#parse 解析字符串成微博列表对象
                    StatusList statuses = StatusList.parse(response);
                    if (statuses != null && statuses.total_number > 0) {
                        for (Status s : statuses.statusList){
                            Weibos weibo = new Weibos(s.user.screen_name, s.created_at, s.text, s.user.profile_image_url);
                            weiboList.add(weibo);
//                            Log.d("??????????????", String.valueOf(weiboList.size()));
//                            Log.d("??????", String.valueOf(weibo.getType()));
//                            Log.d("???????????????????????", s.user.screen_name);
//                            Log.d("???????????????????????", s.created_at);
//                            Log.d("???????????????????????", s.text);
//                            Log.d("???????????????????????", s.user.profile_image_url);
                        }
                        Toast.makeText(getActivity(),
                                "获取微博信息流成功, 条数: " + statuses.statusList.size(),
                                Toast.LENGTH_LONG).show();
                    }
                } else if (response.startsWith("{\"created_at\"")) {
                    // 调用 Status#parse 解析字符串成微博对象
                    Status status = Status.parse(response);
                    Toast.makeText(getActivity(),
                            "发送一送微博成功, id = " + status.id,
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            //LogUtil.e(TAG, e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(getActivity(), info.toString(), Toast.LENGTH_LONG).show();
        }
    };
}
