package com.androfocus.investnshare;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class NewsFeedActivity extends AppCompatActivity {
    Timer myTimer;
    MyTimerTask myTimerTask;
    NewsFeedAdapter newsFeedAdapter;
    ListView listView;
    TextView tvNewsFeedLink;
    List list = new ArrayList();
    String type;
    ProgressDialog loading;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        listView = (ListView) findViewById(R.id.lvNewsFeed);
        newsFeedAdapter = new NewsFeedAdapter(NewsFeedActivity.this, R.layout.news_feed_row_layout);
        loading = ProgressDialog.show(NewsFeedActivity.this, "Fetching NewsFeedData...","Please Wait...",true,true);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                BackgroundWorkerJson backgroundWorkerJson = new BackgroundWorkerJson(NewsFeedActivity.this);
                backgroundWorkerJson.execute();
            }
        });
        //loading.dismiss();
        //thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

        myTimer = new Timer();
        myTimerTask = new MyTimerTask(this);
        //myTimer.scheduleAtFixedRate(myTimerTask,10000,10000);

        refreshLayout = findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newsFeedAdapter.list.clear();
                //newsFeedAdapter.notifyDataSetChanged();
                //refreshLayout.setRefreshing(false);
                //loading = ProgressDialog.show(NewsFeedActivity.this, "Fetching NewsFeedData...","Please Wait...",true,true);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BackgroundWorkerJson backgroundWorkerJson = new BackgroundWorkerJson(NewsFeedActivity.this);
                        backgroundWorkerJson.execute();
                    }
                });
                //loading.dismiss();
                //thread.setPriority(Thread.MAX_PRIORITY);
                thread.start();
            }
        });
    }
    @Override
    public void onBackPressed() {
        myTimerTask.cancel();
        myTimer.cancel();
        myTimer.purge();
        Intent intent = new Intent(NewsFeedActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void goBack(View view) {
        myTimerTask.cancel();
        myTimer.cancel();
        myTimer.purge();
        Intent intent = new Intent(NewsFeedActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public class BackgroundWorkerJson extends AsyncTask<String,Void,String> {
        Context context;
        String type;
        //ProgressDialog loading;
        String json_string;
        JSONArray jsonArray;
        JSONObject jsonObject;

        public BackgroundWorkerJson(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {

            //String img_json_url ="http://arulaudios.com/InvestNShare/getNewsFeed.php";
            String img_json_url ="http://androfocus.com.md-ht-6.hostgatorwebservers.com/InvestNShare/getNewsFeed.php";
            //String img_json_url ="http://investinshares.in.md-114.hostgatorwebservers.com/InvestNShare/getNewsFeed.php";
            //String img_json_url ="https://investinshares.in/InvestNShare/getNewsFeed.php";

            try {
                URL url = new URL(img_json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                /*
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type1", "UTF-8") + "=" + URLEncoder.encode("Equity", "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                */

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
            //loading = ProgressDialog.show(getContext(), "Fetching NewsFeedData...","Please Wait...",true,true);
        }

        @Override
        protected void onPostExecute(String result) {

            json_string = result;
      //      Log.e("Image JSON", json_string);
            if(json_string == null){
                //Toast.makeText(context,"First Get JSON data",Toast.LENGTH_LONG).show();
            }
            else {

                URL url = null;
                Bitmap bitmap = null;
                try {
                    jsonObject = new JSONObject(json_string);
                    jsonArray = jsonObject.getJSONArray("news_feed");

                    String title = null, content = null, link = null, date = null, image = null;
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jo = jsonArray.getJSONObject(i);

                        title = jo.getString("title");
                        content = jo.getString("content");
                        image = jo.getString("image1");
                        link = jo.getString("link1");
                        date = jo.getString("date1");

                        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                        bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        NewsFeedData newsFeedData = new NewsFeedData(title,content,link,date, bitmap);
                        newsFeedAdapter.add(newsFeedData);
                    }
                    loading.dismiss();
                    //imageView.setImageBitmap(image);
                    Log.e("Data", title+content+link+date+ image);
                    listView.setAdapter(newsFeedAdapter);

                    newsFeedAdapter.notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
