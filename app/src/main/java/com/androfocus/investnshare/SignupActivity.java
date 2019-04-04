package com.androfocus.investnshare;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import java.util.Timer;

public class SignupActivity extends AppCompatActivity {
    String name, ph_no, business, addr;
    boolean terms;
    EditText etName, etPh_no, etBusiness, etAddr;
    private static CheckBox terms_and_conditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //To avoid automatically appear android keyboard when activity start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        etName = findViewById(R.id.etName);
        etPh_no = findViewById(R.id.etPh_no);
        etBusiness = findViewById(R.id.etBusiness);
        etAddr = findViewById(R.id.etAddr);

        terms_and_conditions = (CheckBox) findViewById(R.id.cbTerms);
        terms_and_conditions
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {
                            terms = true;

                        } else {
                            terms = false;
                        }

                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignupActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void onSignup(View view) {
        name = etName.getText().toString();
        ph_no = etPh_no.getText().toString();
        business = etBusiness.getText().toString();
        addr = etAddr.getText().toString();

        if(name.trim().length()>0 && ph_no.trim().length()>0 && business.trim().length()>0 && addr.trim().length()>0){
            if(terms){
                BackgroundWorker backgroundWorker = new BackgroundWorker(SignupActivity.this);
                backgroundWorker.execute();
            }
            else {
                Toast.makeText(SignupActivity.this,"Accep out terms and conditions",Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(SignupActivity.this,"Enter all the details",Toast.LENGTH_LONG).show();
        }
    }
    public class BackgroundWorker extends AsyncTask<String,Void,String> {
        Context context;
        String type;
        //ProgressDialog loading;
        String json_string;
        JSONArray jsonArray;
        JSONObject jsonObject;

        public BackgroundWorker(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {

            //String img_json_url ="http://arulaudios.com/InvestNShare/signup.php";
            String img_json_url ="http://androfocus.com.md-ht-6.hostgatorwebservers.com/InvestNShare/signup.php";
            //String img_json_url ="http://investinshares.in.md-114.hostgatorwebservers.com/InvestNShare/signup.php";
            //String img_json_url ="http://investinshares.in.md-114.hostgatorwebservers.com/InvestNShare/signup.php";
            //String img_json_url ="https://investinshares.in/InvestNShare/signup.php";

            try {
                URL url = new URL(img_json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8")+"&"
                        +URLEncoder.encode("ph_no", "UTF-8") + "=" + URLEncoder.encode(ph_no, "UTF-8")+"&"
                        +URLEncoder.encode("business", "UTF-8") + "=" + URLEncoder.encode(business, "UTF-8")+"&"
                        +URLEncoder.encode("addr", "UTF-8") + "=" + URLEncoder.encode(addr, "UTF-8");
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
            //loading = ProgressDialog.show(getContext(), "Fetching NewsFeedData...","Please Wait...",true,true);
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.charAt(0) == 'Y'){
                Toast.makeText(context,result,Toast.LENGTH_LONG).show();
                startActivity(new Intent(context,Main3Activity.class));
            }else if(result.charAt(0) == 'E'){
                Toast.makeText(context,"You are already signed up. Please Contact Our Branch Manager",Toast.LENGTH_LONG).show();
                startActivity(new Intent(context,Contact.class));
            }
            else {
                Toast.makeText(context,"Error in connection: "+result,Toast.LENGTH_LONG).show();
            }
        }

    }
}
