package com.example.dayangtie.weclient.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

/**
 * Created by dayangtie on 10/03/16.
 * The service will be invoked by AlarmManager periodically. When it is invoked, it will send network request and check if there is any new Weibo
 * against last retrieve. If there is update, it should check whether or not the app is running, and send out a notification if the app is not
 * currently running.
 */
public class AlarmManagedPullService extends IntentService {

    public AlarmManagedPullService(String name) {
        super("pull-service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
