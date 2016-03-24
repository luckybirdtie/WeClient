package com.example.dayangtie.weclient.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.example.dayangtie.weclient.R;
import com.example.dayangtie.weclient.sina.Emotion;
import com.sina.weibo.sdk.openapi.models.Tag;


/**
 * 
 * @author tracyZhang  https://github.com/TracyZhangLei
 * @since  2014-4-4
 *
 */
public class FaceManager {

    public static final String TAG = "FaceManager";
	
	private FaceManager(){
	}
	
	private static FaceManager instance;

	public static FaceManager getInstance() {
		if(null == instance)
			instance = new FaceManager();
		return instance;
	}
	
	//private Map<String, Integer> mFaceMap;
	private static Map<String, String> mFaceMap = new LinkedHashMap<String, String>();;

	private Class drawable = R.drawable.class;
	private Field field = null;
	
	private void initFaceMap() {
//		mFaceMap = new LinkedHashMap<String, String>();
//		mFaceMap.put("[呲牙]", R.drawable.f000);
	}

    public static void addFace(Emotion emo){
        mFaceMap.put(emo.getPhrase(), emo.getIconName().substring(0, emo.getIconName().length() - 4));
    }

	//read from SharedPreferences
	public static void addAll(Map map){
		Object[] keys = map.keySet().toArray();
		for (int i = 0; i < map.size(); i++){
			mFaceMap.put((String) keys[i], (String) map.get(keys[i]));
		}
	}
	
	public int getFaceId(String faceStr) throws NoSuchFieldException, IllegalAccessException {
		if(mFaceMap.containsKey(faceStr)){
			field = drawable.getField(mFaceMap.get(faceStr));
			int res_ID = field.getInt(field.getName());
			return res_ID;
		}

		return -1;
	}

    public static int getMapSize(){

		return mFaceMap.size();
    }

    public static void printMap(){
        for (int i = 0; i < mFaceMap.keySet().size(); i++){
            System.out.println(mFaceMap.keySet().toArray()[i] + ":" + mFaceMap.get(mFaceMap.keySet().toArray()[i]));
        }
    }

}
