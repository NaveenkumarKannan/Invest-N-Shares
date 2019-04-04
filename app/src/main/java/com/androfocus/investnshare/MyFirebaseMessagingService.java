package com.androfocus.investnshare;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    //this method will display the notification
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    private void sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");
            //String imageUrl = data.getString("image");

            Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.mipmap.ic_launcher );
            //set intents and pending intents to call activity on click of "show activity" action button of notification
            Intent resultIntent = new Intent(getApplicationContext(),MainActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),(int) Calendar.getInstance().getTimeInMillis(),resultIntent ,0 );
            //Assign BigText style notification
        /*
      NotificationCompat.InboxStyle bigTextStyle = new NotificationCompat.InboxStyle();
        bigTextStyle.setBigContentTitle("Invest N Shares");
        bigTextStyle.addLine(text);

        */
            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.setBigContentTitle(title);
            bigTextStyle.bigText(message);

            //bigTextStyle.setSummaryText("By: Naveenkumar.K Android Developer");
            //build notification
            String CHANNEL_ID = "InvestNShares";// The id of the channel.
            NotificationCompat.Builder  builder = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.img4)
                    .setLargeIcon(bitmap)
                    .setContentTitle("Invest N Shares")
                    .setContentText("Today trade calls")
                    .setStyle(bigTextStyle)
                    .setChannelId(CHANNEL_ID)
                    //.addAction(R.drawable.ic_eye,"Open App",pendingIntent)
                    .setAutoCancel(true)
                    ;
            builder.setContentIntent(pendingIntent);
            //Get an instance of the NotificationManager service
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


            //Notification in Oreo 8.0
            // Sets an ID for the notification, so it can be updated.
            long notifyID = Calendar.getInstance().getTimeInMillis();

            CharSequence name = "InvestNShares";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            Notification notification = builder.build();
            notification.sound = Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.incoming);
            //notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_LIGHTS;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            //to post your notification to the notification bar
            notificationManager.notify((int) notifyID, notification);

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

}