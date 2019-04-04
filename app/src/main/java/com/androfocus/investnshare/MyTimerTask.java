package com.androfocus.investnshare;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.TimerTask;

public class MyTimerTask extends TimerTask {
    Activity activity;
    // Session Manager Class
    SessionManager session;
    int id = 0;
    String text = " ";

    public MyTimerTask(Activity activity) {
        this.activity = activity;
        // Session class instance
        session = new SessionManager(activity);
    }

    @Override public void run() {
        activity.runOnUiThread(new Runnable() {
            @Override public void run() {
                getId();
            }
        });
    }

    public void GetNotification() {
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(),R.mipmap.ic_launcher );
        //set intents and pending intents to call activity on click of "show activity" action button of notification
        Intent resultIntent = new Intent(activity,MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity,(int) Calendar.getInstance().getTimeInMillis(),resultIntent ,0 );
        //Assign BigText style notification
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Invest N Shares");
        bigTextStyle.bigText(text);
        //bigTextStyle.setSummaryText("By: Naveenkumar.K Android Developer");
        //build notification
        String CHANNEL_ID = "InvestNShares";// The id of the channel.
        NotificationCompat.Builder  builder = new NotificationCompat.Builder(activity)
                .setSmallIcon(R.drawable.ic_live_market)
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
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);


        //Notification in Oreo 8.0
        // Sets an ID for the notification, so it can be updated.
        int notifyID = 1;

        CharSequence name = "InvestNShares";// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Notification notification = builder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        //to post your notification to the notification bar
        notificationManager.notify(notifyID, notification);

    }

    private void getId() {
        class BackgroundWorkerJson extends AsyncTask<String,Void,String> {
            Context context;
            String type;
            //ProgressDialog loading;
            String json_string;
            JSONArray jsonArray;
            JSONObject jsonObject;

            @Override
            protected String doInBackground(String... params) {

                //String img_json_url ="http://arulaudios.com/InvestNShare/getImageBlop.php";
                String img_json_url ="http://investinshares.in.md-114.hostgatorwebservers.com/InvestNShare/getImageBlop.php";
                //String img_json_url ="https://investinshares.in/InvestNShare/getImageBlop.php";

                try {
                    URL url = new URL(img_json_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("type1", "UTF-8") + "=" + URLEncoder.encode("null", "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();


                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return stringBuilder.toString().trim();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(getContext(), "Fetching CommodityData...","Please Wait...",true,true);
            }

            @Override
            protected void onPostExecute(String result) {

                json_string = result;
//                Log.e("Id JSON", json_string);
                if(json_string == null){
                   // Toast.makeText(activity,"First Get json data",Toast.LENGTH_LONG).show();
                }
                else {

                    URL url = null;
                    try {
                        jsonObject = new JSONObject(json_string);
                        jsonArray = jsonObject.getJSONArray("call_details");

                        String buy, sell,name, type2, note;
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jo = jsonArray.getJSONObject(i);

                            id =  jo.getInt("id");
                            type2 = jo.getString("type2");
                            name = jo.getString("name");
                            note = jo.getString("note1");
                            buy = jo.getString("buy");
                            sell = jo.getString("sell");
                            Log.e("id = ", String.valueOf(id));

                            if(type2.equals("Buy")){
                                text = type2+" "+name+"@"+buy+"\n"+note;
                            }else if(type2.equals("Sell")){
                                text = type2+" "+name+"@"+buy+"\n"+note;
                            }else if(type2.equals("Stop Loss Hit")){
                                text = "Stop Loss "+" "+name+"@"+sell+"\n"+note;
                            }else {
                                text = note;
                            }
                        }
                        int prefId = session.getPrefId();
                        Log.e("prefId", String.valueOf(prefId));
                        Log.e("id = ", String.valueOf(id));
                        if(prefId == 0) {
                            session.createSession(id);
                            //GetNotification();
                        }
                        if(id > prefId && prefId != 0){
                            GetNotification();
                            session.createSession(id);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                backgroundWorker.execute();
            }
        });
        //thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

        /*
        BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
        backgroundWorker.execute();
        */
    }


}
