package com.androfocus.investnshare;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.util.HashMap;
import java.util.Map;

public class login  extends Fragment {
    String PhNo;
    int statusCheck= 1;
    String type;
    String name = null, phone_no = null,enabled1 = null;
    // Session Manager Class
    SessionManager session;

    private ProgressDialog progressDialog;
    private static final String URL_REGISTER_DEVICE = "http://androfocus.com.md-ht-6.hostgatorwebservers.com/InvestNShare/Firebase/RegisterDevice.php";
    //private static final String URL_REGISTER_DEVICE = "http://investinshares.in.md-114.hostgatorwebservers.com/InvestNShare/Firebase/RegisterDevice.php";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        // Session Manager
        session = new SessionManager(getContext());

        //Toast.makeText(getContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        TextView tvSignUp = v.findViewById(R.id.tvSignUp);
        TextView tvWebsite = v.findViewById(R.id.tvWebsite);
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SignupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
        tvWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),WebViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("Link","https://investinshares.in" );
                intent.putExtra("NewsFeed","NewsFee" );
                startActivity(intent);
            }
        });
        Button button = (Button) v.findViewById(R.id.login);
        final EditText etPhNo = v.findViewById(R.id.etPhNo);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PhNo = etPhNo.getText().toString();
                if(PhNo.trim().length()>0){
                    BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                    type = "login";
                    backgroundWorker.execute();
                }
                else{
                    Toast.makeText(getContext(),"Enter the Mobile number",Toast.LENGTH_LONG).show();
                }
            }
        });
        return v;
    }

    public class BackgroundWorkerJson extends AsyncTask<String,Void,String> {


        String json_string;
        JSONArray jsonArray;
        JSONObject jsonObject;



        @Override
        protected String doInBackground(String... params) {

            //String img_json_url ="http://arulaudios.com/InvestNShare/Login.php";
            String status_url = "http://androfocus.com.md-ht-6.hostgatorwebservers.com/InvestNShare/LoginStatus.php";
            //String status_url = "http://investinshares.in.md-114.hostgatorwebservers.com/InvestNShare/LoginStatus.php";
            //String status_url = "https://investinshares.in/InvestNShare/LoginStatus.php";

            String login_url = "http://androfocus.com.md-ht-6.hostgatorwebservers.com/InvestNShare/Login.php";
            //String login_url ="http://investinshares.in.md-114.hostgatorwebservers.com/InvestNShare/Login.php";
            //String login_url ="https://investinshares.in/InvestNShare/Login.php";

            if(type.equals("status")){
                try {
                    URL url = new URL(status_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("PhNo", "UTF-8") + "=" + URLEncoder.encode(PhNo, "UTF-8");
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

            }
            else if(type.equals("login")){
                try {
                    //PhNo = params[0];
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("PhNo", "UTF-8") + "=" + URLEncoder.encode(PhNo, "UTF-8");
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
            //Log.e("Image JSON", json_string);

            if(type.equals("login")){
                if(json_string == null){
                    //Toast.makeText(getContext(),"First Get JSON data",Toast.LENGTH_LONG).show();
                }
                else {

                    try {
                        jsonObject = new JSONObject(json_string);
                        jsonArray = jsonObject.getJSONArray("Login");

                        String status = null;
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jo = jsonArray.getJSONObject(i);

                            name = jo.getString("name");
                            phone_no = jo.getString("phone_no");
                            enabled1 = jo.getString("enabled1");
                            status = jo.getString("status");
                        }
                        if(enabled1.equals("Yes")){
                            if(status.equals("0")){
                                sendTokenToServer();

                            }else {
                                Toast.makeText(getContext(),"You have already logged in other device "+name,Toast.LENGTH_LONG).show();
                            }

                        }else {
                            Toast.makeText(getContext(),"Please contact our branch manager to enable you to login",Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
                    }

    }

    private void sendTokenToServer() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Login Please wait...");
        progressDialog.show();

        final String token = SharedPrefManager.getInstance(getContext()).getDeviceToken();
        final String email = PhNo;

        if (token == null) {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);

                            String message;
                            Boolean error;
                            message = obj.getString("message");
                            error = obj.getBoolean("error");

                            if(!error){
                                session.createLoginSession(name, phone_no);
                                Toast.makeText(getContext(),"You have logged as "+name /*+" and your "+ message*/,Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                getActivity().finish();
                            }else {
                                Toast.makeText(getContext(), message,Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("token", token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}