package com.example.dayangtie.weclient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dayangtie.weclient.services.EmotionsDownloadService;
import com.example.dayangtie.weclient.sina.AccessTokenKeeper;
import com.example.dayangtie.weclient.sina.Constants;
import com.example.dayangtie.weclient.util.FaceManager;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.openapi.models.Tag;

import java.text.SimpleDateFormat;
import java.util.Map;

public class MainPageActivity extends AppCompatActivity {

    private static final String TAG = "MainPageActivity";

    private AuthInfo mAuthInfo;
    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;
    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;

    /** 显示认证后的信息，如 AccessToken */
    private TextView mTokenText;

    private Button logButton;
    private Button logOutButton;
    private Button enterButton;
    private Button downloadEmo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        logButton = (Button) findViewById(R.id.btn_login);
        logOutButton = (Button) findViewById(R.id.btn_logout);
        enterButton = (Button) findViewById(R.id.btn_enter);
        downloadEmo = (Button) findViewById(R.id.download_emo);
        mTokenText = (TextView) findViewById(R.id.token_text_view);

        initFaceManager();

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAuth();
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessTokenKeeper.clear(getApplicationContext());
                mAccessToken = new Oauth2AccessToken();
                updateTokenView(false);
            }
        });

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginValid()) {
                    Intent intent = new Intent(MainPageActivity.this, MainActivity.class);
                    intent.putExtra("access_token", mAccessToken.getToken());
                    startActivity(intent);
                }else{
                    Toast.makeText(MainPageActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });

        downloadEmo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, EmotionsDownloadService.class);
                startService(intent);
            }
        });

    }

    private void initFaceManager() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("Faces", 0);
        if (sp.getAll().size() != 0)
            FaceManager.addAll(sp.getAll());
    }


    private void startAuth() {
        mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(MainPageActivity.this, mAuthInfo);
       // mSsoHandler.authorizeWeb(new AuthListener()); //web authorization
       //mSsoHandler.authorizeClientSso(new AuthListener()); //client authorization
        mSsoHandler.authorize(new AuthListener()); //auto-detect if a Weibo client is installed on phone, if yes use client authorization, otherwise use web.

        //mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (isLoginValid()) {
            updateTokenView(true);
        }

    }

    //是否授权仍然VALID
    @Override
    protected void onResume() {
        super.onResume();
        //mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (isLoginValid()) {
            updateTokenView(true);
        }
    }

    //判断TOKEN SESSION是否仍然有效
    private boolean isLoginValid(){
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        return mAccessToken.isSessionValid();
    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            //从这里获取用户输入的 电话号码信息
            String  phoneNum =  mAccessToken.getPhoneNum();
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
                updateTokenView(false);

                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(MainPageActivity.this, mAccessToken);
                Toast.makeText(MainPageActivity.this,
                        "授权成功", Toast.LENGTH_SHORT).show();
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = "授权失败";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(MainPageActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            //System.out.println("??????????????????????????????????????????????????here??????????????????????");
            Toast.makeText(MainPageActivity.this,
                    "取消授权", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(MainPageActivity.this,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 显示当前 Token 信息。
     *
     * @param hasExisted 配置文件中是否已存在 token 信息并且合法
     */
    private void updateTokenView(boolean hasExisted) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
        mTokenText.setText(String.format(format, mAccessToken.getToken(), date));

        String message = String.format(format, mAccessToken.getToken(), date);
        if (hasExisted) {
            message = getString(R.string.weibosdk_demo_token_has_existed) + "\n" + message;
        }
        mTokenText.setText(message);
    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     *
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        //System.out.println("is it executed?  " + mSsoHandler != null);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "destroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "restart");
    }
}
