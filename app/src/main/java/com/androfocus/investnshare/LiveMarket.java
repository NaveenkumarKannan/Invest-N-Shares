package com.androfocus.investnshare;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class LiveMarket extends Fragment {
    LiveMarketAdapter liveMarketAdapter;
    ListView listView;
    TextView tvNewsFeedLink;
    List list = new ArrayList();
    String type;
    ProgressDialog loading;
    private SwipeRefreshLayout refreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_live_market,container,false);
        listView = (ListView) view.findViewById(R.id.lvNewsFeed);
        liveMarketAdapter = new LiveMarketAdapter(getContext(), R.layout.live_market_row_layout);

        loading = ProgressDialog.show(getContext(), "Fetching Data...","Please Wait...",true,true);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                backgroundWorker.execute();
            }
        });
        //loading.dismiss();
        //thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                tvNewsFeedLink = (TextView) view.findViewById(R.id.tvNewsFeedLink);
                //final LiveMarketData liveMarketData = liveMarketAdapter.getItem(position);
                Toast.makeText(getContext(),"You selected : " + position,Toast.LENGTH_SHORT).show();
            }
        });
        */
        refreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                liveMarketAdapter.list.clear();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                        backgroundWorker.execute();
                    }
                });
                //loading.dismiss();
                //thread.setPriority(Thread.MAX_PRIORITY);
                thread.start();
            }
        });
        return view;
    }



    public class BackgroundWorkerJson extends AsyncTask<String,Void,String> {
        Context context;
        String type;
        //ProgressDialog loading;
        String json_string;
        JSONArray jsonArray;
        JSONObject jsonObject;

        @Override
        protected String doInBackground(String... params) {

            //String img_json_url ="http://arulaudios.com/InvestNShare/getLiveMarket.php";
            String img_json_url ="http://androfocus.com.md-ht-6.hostgatorwebservers.com/InvestNShare/getLiveMarket.php";
            //String img_json_url ="http://investinshares.in.md-114.hostgatorwebservers.com/InvestNShare/getLiveMarket.php";
            //String img_json_url ="https://investinshares.in/InvestNShare/getLiveMarket.php";

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
            //loading = ProgressDialog.show(getContext(), "Fetching LiveMarketData...","Please Wait...",true,true);
        }

        @Override
        protected void onPostExecute(String result) {

            json_string = result;
            //     Log.e("Image JSON", json_string);
            if(json_string == null){
                //Toast.makeText(getContext(),"First Get JSON data",Toast.LENGTH_LONG).show();
            }
            else {

                URL url = null;
                Bitmap bitmap = null;
                try {
                    jsonObject = new JSONObject(json_string);
                    jsonArray = jsonObject.getJSONArray("LiveMarket");

                    String title = null, content = null, link = null, date = null, image = null;

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jo = jsonArray.getJSONObject(i);

                        title = jo.getString("title");
                        content = jo.getString("content");
                        date = jo.getString("date1");

                        LiveMarketData liveMarketData = new LiveMarketData(title, content, date);
                        liveMarketAdapter.add(liveMarketData);
                    }
                    loading.dismiss();
                    //imageView.setImageBitmap(image);
                    Log.e("Data", title+content+link+date+ image);
                    listView.setAdapter(liveMarketAdapter);

                    liveMarketAdapter.notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
