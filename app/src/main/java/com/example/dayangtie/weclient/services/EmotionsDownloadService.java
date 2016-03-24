package com.example.dayangtie.weclient.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.dayangtie.weclient.sina.AccessTokenKeeper;
import com.example.dayangtie.weclient.sina.Constants;
import com.example.dayangtie.weclient.sina.Emotion;
import com.example.dayangtie.weclient.util.FaceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.StatusesAPI;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by dayangtie on 17/03/16.
 * Download the emotion imgages.
 */
public class EmotionsDownloadService extends IntentService{

    public static final String TAG = "EmotionsDownloadService";

    /** 表情类别，face：普通表情、ani：魔法表情、cartoon：动漫表情，默认为face。 */
    public static final String EMOTION_TYPE_FACE    = "face";
    public static final String EMOTION_TYPE_ANI     = "ani";
    public static final String EMOTION_TYPE_CARTOON = "cartoon";
    public static final String LANGUAGE_CNNAME = "cnname";

    String[] types = {EMOTION_TYPE_CARTOON, EMOTION_TYPE_ANI, EMOTION_TYPE_FACE};

    private StatusesAPI mStatusesAPI;
    private Oauth2AccessToken mAccessToken;
    private String emotions;
    //private static final String IMG_PATH
    private String emoPath;
    private String iconURL;

    public EmotionsDownloadService() {
        super("EmotionDownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mAccessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
        mStatusesAPI = new StatusesAPI(getApplicationContext(), Constants.APP_KEY, mAccessToken);

        for(int i = 0; i < 3; i++){
            Log.d(TAG, "onHandleIntent: " + types[i]);
            startExecution(types[i]);
        }

        FaceManager.printMap();
    }

    public void startExecution(String type){
        emotions = mStatusesAPI.emotions(type, LANGUAGE_CNNAME);
        Log.d(TAG, emotions);

        Type listType = new TypeToken<LinkedList<Emotion>>(){}.getType();
        Gson gson = new Gson();
        LinkedList<Emotion> emotionsList = gson.fromJson(emotions, listType);

        for (Emotion emo : emotionsList){

            iconURL = emo.getIcon();

            emo.setToLowerCase();

            if (Character.isDigit(emo.getIconName().charAt(0)))
                emo.setIconNameFirstChar();

            if (!emo.getIconName().endsWith(".gif")){
                if (emo.getIconName().contains("&"))
                    emo.removeSpecialChar();
                emo.appendGifSuffix();
            }

            FaceManager.addFace(emo);
            saveFacesToLocal(emo.getPhrase(), emo.getIconName().substring(0, emo.getIconName().length() - 4));
            //saveEmoImages(emo);
//            Log.d(TAG, ":phrase" + emo.getPhrase());
//            Log.d(TAG, ":icon is - " + emo.getIcon());
//            Log.d(TAG, ":url is -" + emo.getUrl());
        }
    }

    private void saveEmoImages(Emotion emo){
        emoPath = getExternalFilesDir(null) + "/emotions/";
        File emoFile = new File(emoPath);
        if (!emoFile.exists())
            emoFile.mkdir();
        File fileName = new File(emoPath + emo.getIconName());
        URL imgURL = null;
        HttpURLConnection connection = null;

        try {
            imgURL = new URL(iconURL);
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try{
            connection = (HttpURLConnection) imgURL.openConnection();
            connection.setRequestMethod("GET");
            //connection.setConnectTimeout(16000);
            connection.setReadTimeout(5000);
            //connection.setDoInput(true);
            //connection.connect();
            if (connection.getResponseCode() == 200) {
                BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
                //int length = connection.getContentLength();
                //byte[] buffer = new byte[length];
                byte[] data = readStream(bis);
               // bis.read(buffer);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
                bos.write(data);
                //bis.close();
                //bos.flush();
                bos.close();
            }
        }catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    private byte[] readStream(InputStream is) throws Exception{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = 0;
        while ((len = is.read(buffer)) != -1)
            bos.write(buffer, 0, len);
        is.close();
        return bos.toByteArray();
    }

    private void saveFacesToLocal(String faceName, String sourceName){
        SharedPreferences sp = getApplicationContext().getSharedPreferences("Faces", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(faceName, sourceName);
        editor.apply();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

//        try {
//            JsonReader reader = new JsonReader(new StringReader(emotions_face));
//            reader.beginArray();
//            while(reader.hasNext()){
//                reader.beginObject();
//                while(reader.hasNext()){
//                    String tagName = reader.nextName();
//                    if(tagName.equals("username")){
//                        System.out.println(reader.nextString());
//                    }
//                    else if(tagName.equals("userId")){
//                        System.out.println(reader.nextString());
//                    }
//                }
//                reader.endObject();
//            }
//            reader.endArray();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
