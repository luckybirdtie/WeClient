package com.example.dayangtie.weclient.util;

import android.content.Context;

import android.text.TextUtils;
import android.text.format.DateFormat; //not java.text.DateFormat
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dayangtie on 10/03/16.
 */
public class TimeCalculator {

    private static Calendar calendar;
    private static final String DATE_FORMAT_24 = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT_12 = "yyyy-MM-dd hh:mm:ss";
    //private static String[] formats;


    /** 判断是否是24小时制**/
    public static boolean is24Hours(Context context){
        return DateFormat.is24HourFormat(context);
    }

    /** 返回日期字符串 */
    public static String getDateString(Context context, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        if(is24Hours(context)) {
            // HH:mm中的代表24小时制，hh:mm代表12小时制
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            // 形式为：00:30   31/12/2013, 6:54 am
            return dateFormat.format(date) + getTimeFormat(dateFormat);
        }
        return dateFormat.format(date);
    }

    //The method return a Date instance in comply with the input string of date and its format .
    public static Date stringToDate(String dateString, String dateFormat){
        Date date = null;
        if (!TextUtils.isEmpty(dateString)){
           // SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("ENGLISH", "CHINA"));
            SimpleDateFormat df = new SimpleDateFormat(dateFormat, Locale.ENGLISH);

            try {
                date = df.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return date;
    }

    public static String timeSpanTillNow(Context context, String dateString){

        Calendar calendar = Calendar.getInstance();
        Long now = calendar.getTimeInMillis();
        Date date = null;
       // Log.d("now:", String.valueOf(now));
        if (is24Hours(context)) {
            date = stringToDate(dateString, DATE_FORMAT_24);
        }else{
            date = stringToDate(dateString, DATE_FORMAT_12);
        }
        calendar.setTime(date);
        long past = calendar.getTimeInMillis();
       // Log.d("past:", String.valueOf(past));
        /**相差的秒数**/
        long timeDiff = (now - past)/1000;
       // Log.d("timedifff:", String.valueOf(timeDiff));

        StringBuffer sb = new StringBuffer();
        if (timeDiff > 0 && timeDiff < 60) { // 1小时内
            return sb.append(timeDiff + "秒前").toString();
        } else if (timeDiff > 60 && timeDiff < 3600) {
            return sb.append(timeDiff / 60+"分钟前").toString();
        } else if (timeDiff >= 3600 && timeDiff < 3600 * 24) {
            return sb.append(timeDiff / 3600 +"小时前").toString();
        }else if (timeDiff >= 3600 * 24 && timeDiff < 3600 * 48) {
            return sb.append("昨天").toString();
        }else if (timeDiff >= 3600 * 48 && timeDiff < 3600 * 72) {
            return sb.append("前天").toString();
        }else if (timeDiff >= 3600 * 72) {
            return dateString;
        }
        return dateString;
    }

    /** 返回当前时间是上午还是下午**/
    public static String getTimeFormat(SimpleDateFormat dateFormat) {
        int ap = dateFormat.getCalendar().get(Calendar.AM_PM);
        String amStr = "";
        if(ap== 0) {
            amStr = " AM";
        } else if(ap == 1) {
            amStr = " PM";
        }
        return amStr;
    }


}
