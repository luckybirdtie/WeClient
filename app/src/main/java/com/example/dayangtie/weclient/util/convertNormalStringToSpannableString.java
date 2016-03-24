package com.example.dayangtie.weclient.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.TextView;

import com.koushikdutta.ion.LoadDeepZoom;
import com.sina.weibo.sdk.openapi.models.Tag;

import java.lang.ref.WeakReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dayangtie on 11/03/16.
 */
public class ConvertNormalStringToSpannableString {
    private static final String TAG = "ConvertNormalStringToSpannableString";

    private static Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");
    private static FaceManager fm = FaceManager.getInstance();

    public static SpannableString convertNormalStringToSpannableString(Context context, String message) {
        SpannableString value = SpannableString.valueOf(message);
        Matcher localMatcher = EMOTION_URL.matcher(value);
        while (localMatcher.find()) {
            String str2 = localMatcher.group(0);
            int k = localMatcher.start();
            int m = localMatcher.end();
            //Log.d(TAG, "m:" + m + " k:" + k);
            if (m - k < 20) {
                int face = 0;
                try {
                    //Log.d(TAG, str2);
                    face = fm.getFaceId(str2);
                   // Log.d(TAG, String.valueOf(face));
                } catch (NoSuchFieldException e) {
                    //Log.d(TAG, "cannot find this file");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if(-1!=face){//wrapping with weakReference
                    //Drawable drawable = new AnimatedGifDrawable(context.getResources().openRawResource(face));
                    Drawable drawable = ContextCompat.getDrawable(context, face);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                    value.setSpan(span, k, m, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // WeakReference<AnimatedImageSpan> localImageSpanRef = new WeakReference<AnimatedImageSpan>(new AnimatedImageSpan(new AnimatedGifDrawable(context.getResources().openRawResource(face))));
                    //value.setSpan(localImageSpanRef.get(), k, m, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                }
            }
        }
       // Log.d(TAG + "result:", String.valueOf(value));
        return value;
    }

}
