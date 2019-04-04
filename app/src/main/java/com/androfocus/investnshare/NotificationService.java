package com.androfocus.investnshare;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
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
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    //Timer timer;
    boolean stop = false;
    //TimerTask timerTask;
    String TAG = "Timers";
    int Your_X_SECS = 1;

    Context activity;
    // Session Manager Class
    SessionManager session;
    int id = 0;
    String text = " ";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        activity = getApplicationContext();
        // Session class instance
        session = new SessionManager(activity);

        startTimer();

        return START_STICKY;
    }

    @Override
    public boolean stopService(Intent name) {
        stoptimertask();
        stopSelf();
        return super.stopService(name);
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");


    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        stoptimertask();
        super.onDestroy();


    }

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();


    public void startTimer() {
        //set a new Timer
        //timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        //timer.schedule(timerTask, 0, Your_X_SECS * 1000); //
        //timer.schedule(timerTask, 5000,1000); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
/*        if (timer != null) {
            Log.e(TAG, "stop timer");
            timer.cancel();
            timerTask.cancel();
            timer = null;
        }
  */
        Log.e(TAG, "stop timer");
    }

    public void initializeTimerTask() {
        /*
        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {

                        //TODO CALL NOTIFICATION FUNC
                        getId();

                    }
                });
            }
        };
        */
        getId();
    }

    public void GetNotification() {
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(),R.mipmap.ic_launcher );
        //set intents and pending intents to call activity on click of "show activity" action button of notification
        Intent resultIntent = new Intent(activity,MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity,(int) Calendar.getInstance().getTimeInMillis(),resultIntent ,0 );
        //Assign BigText style notification
        /*
      NotificationCompat.InboxStyle bigTextStyle = new NotificationCompat.InboxStyle();
        bigTextStyle.setBigContentTitle("Invest N Shares");
        bigTextStyle.addLine(text);

        */
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Invest N Shares");
        bigTextStyle.bigText(text);

        //bigTextStyle.setSummaryText("By: Naveenkumar.K Android Developer");
        //build notification
        String CHANNEL_ID = "InvestNShares";// The id of the channel.
        NotificationCompat.Builder  builder = new NotificationCompat.Builder(activity)
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
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);


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
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        //to post your notification to the notification bar
        notificationManager.notify((int) notifyID, notification);

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

                    //byte[] bytes = json.getBytes("UTF-8");

                    //httpCon.setFixedLengthStreamingMode(bytes.length);

                    //os.write(bytes);

                    httpURLConnection.setChunkedStreamingMode(0);

                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setUseCaches(false);
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
                    //stopService(new Intent(NotificationService.this, NotificationService.class));
                    e.printStackTrace();
                    /*
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    */
                    Log.e("Exit0","App closed" );
                    /*
                     * Notify the system to finalize and collect all objects of the
                     * application on exit so that the process running the application can
                     * be killed by the system without causing issues. NOTE: If this is set
                     * to true then the process will not be killed until all of its threads
                     * have closed.
                     */
                    //System.runFinalizersOnExit(true);

                    /*
                     * Force the system to close the application down completely instead of
                     * retaining it in the background. The process that runs the application
                     * will be killed. The application will be completely created as a new
                     * application in a new process if the user starts the application
                     * again.
                     */
                    //System.exit(0);
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
                     //Toast.makeText(activity,"First Get json data", Toast.LENGTH_LONG).show();
                }
                else {

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

        /*

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                backgroundWorker.execute();
            }
        });
        //thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();

        */
        BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
        backgroundWorker.execute();
    }
}