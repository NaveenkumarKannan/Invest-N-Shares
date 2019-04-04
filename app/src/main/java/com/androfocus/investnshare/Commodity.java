package com.androfocus.investnshare;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
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


public class Commodity extends Fragment {
    CommodityAdapter commodityAdapter;
    ListView listView;
    String type;
    ProgressDialog loading;
    TextView tvNoData;
    private SwipeRefreshLayout refreshLayout;
       @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_commodity,container,false);

           listView = (ListView) view.findViewById(R.id.lvcommodity);
           commodityAdapter = new CommodityAdapter(getContext(), R.layout.commodity_row_layout);

           tvNoData =(TextView) view.findViewById(R.id.tvNoData);

           loading = ProgressDialog.show(getContext(), "Fetching Data...","Please Wait...",true,true);
           Thread thread = new Thread(new Runnable() {
               @Override
               public void run() {
                   BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                   backgroundWorker.execute();
               }
           });

           thread.setPriority(Thread.MIN_PRIORITY);
           thread.start();

           refreshLayout = view.findViewById(R.id.swipe_refresh_layout);
           refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
               @Override
               public void onRefresh() {
                   commodityAdapter.list.clear();
                   Thread thread = new Thread(new Runnable() {
                       @Override
                       public void run() {
                           BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                           backgroundWorker.execute();
                       }
                   });

                   thread.setPriority(Thread.MIN_PRIORITY);
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

            //String img_json_url ="http://arulaudios.com/InvestNShare/getImageBlop.php";
            String img_json_url ="http://androfocus.com.md-ht-6.hostgatorwebservers.com/InvestNShare/getImageBlop.php";
            //String img_json_url ="http://investinshares.in.md-114.hostgatorwebservers.com/InvestNShare/getImageBlop.php";
            // String img_json_url ="https://investinshares.in/InvestNShare/getImageBlop.php";
            try {
                URL url = new URL(img_json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type1", "UTF-8") + "=" + URLEncoder.encode("Commodity", "UTF-8");
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
      //      Log.e("Image JSON", json_string);
            if(json_string == null){
                //Toast.makeText(getContext(),"First Get JSON commodityData",Toast.LENGTH_LONG).show();
            }
            else {

                URL url = null;
                Bitmap bitmap = null;
                try {
                    jsonObject = new JSONObject(json_string);
                    jsonArray = jsonObject.getJSONArray("call_details");

                    String id = null, type1, type2,type3,type4, name, target1, target2, target3, buy, sell, note, date, image,time;
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jo = jsonArray.getJSONObject(i);

                        type1 = jo.getString("type1");
                        type2 = jo.getString("type2");
                        name = jo.getString("name");
                        target1 = jo.getString("target1");
                        target2 = jo.getString("target2");
                        target3 = jo.getString("target3");
                        buy = jo.getString("buy1");
                        sell = jo.getString("sell1");
                        note = jo.getString("note1");
                        date = jo.getString("date");
                        image = jo.getString("image1");
                        time =  jo.getString("time");
                        type3 = jo.getString("type3");
                        type4 = jo.getString("type4");

                        id = jo.getString("id");

                        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                        bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        CommodityData commodityData = new CommodityData(type1, type2, name, target1, target2, target3, buy, sell, note, date, bitmap, time,type3,type4);
                        commodityAdapter.add(commodityData);
                    }
                    loading.dismiss();
                    if(id!=null){
                        listView.setVisibility(View.VISIBLE);
                        listView.setAdapter(commodityAdapter);
                        tvNoData.setVisibility(View.GONE);
                    }
                    else {
                        listView.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                    commodityAdapter.notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
