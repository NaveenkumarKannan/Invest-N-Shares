package com.androfocus.investnshare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationAlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "LL24";
    static Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.w(TAG, "Alarm for LifeLog...");

        Intent ll24Service = new Intent(context, NotificationService.class);
        context.startService(ll24Service);
    }
}
