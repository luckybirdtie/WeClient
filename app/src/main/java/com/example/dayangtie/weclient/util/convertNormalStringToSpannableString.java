package com.example.dayangtie.weclient.util;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dayangtie on 11/03/16.
 */
public class ConvertNormalStringToSpannableString {

    private static Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");
    private static FaceManager fm = FaceManager.getInstance();

    public static SpannableString convertNormalStringToSpannableString(Context context, String message) {
        SpannableString value = SpannableString.valueOf(message);
        Matcher localMatcher = EMOTION_URL.matcher(value);
        while (localMatcher.find()) {
            String str2 = localMatcher.group(0);
            int k = localMatcher.start();
            int m = localMatcher.end();
            if (m - k < 8) {
                int face = fm.getFaceId(str2);
                if(-1!=face){//wrapping with weakReference
                    WeakReference<AnimatedImageSpan> localImageSpanRef = new WeakReference<AnimatedImageSpan>(new AnimatedImageSpan(new AnimatedGifDrawable(context.getResources().openRawResource(face))));
                    value.setSpan(localImageSpanRef.get(), k, m, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                }
            }
        }
        return value;
    }

}
