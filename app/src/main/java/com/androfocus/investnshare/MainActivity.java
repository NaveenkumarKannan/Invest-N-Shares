package com.androfocus.investnshare;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androfocus.investnshare.receiver.NetworkStateChangeReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
import java.util.HashMap;
import java.util.Timer;

import static com.androfocus.investnshare.receiver.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Session Manager Class
    SessionManager session;
    int id = 0;
    String text = " ";
    Timer myTimer;
    MyTimerTask myTimerTask;
    String PhNo, type;
    String name = null, phone_no = null,enabled1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter intentFilter = new IntentFilter(NetworkStateChangeReceiver.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                String networkStatus = isNetworkAvailable ? "connected" : "disconnected";

                Snackbar.make(findViewById(R.id.drawer_layout), "Network Status: " + networkStatus, Snackbar.LENGTH_LONG).show();
            }
        }, intentFilter);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        if(connected == false)
        {
            Intent intent = new Intent(MainActivity.this,Internet_Connection.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            finish();
        }

        // Session class instance
        session = new SessionManager(getApplicationContext());
        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();


        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        String name = user.get(SessionManager.KEY_NAME);
        PhNo = user.get(SessionManager.KEY_PHONE);

        TextView tvName = findViewById(R.id.tvName);
        tvName.setText("Welcome "+name);

        if(session.checkLogin())
        {
            finish();
        }else {
            Log.w("Alarm", "Start");

            //By using Broadcast
            //Intent ll24 = new Intent(this, NotificationAlarmReceiver.class);
            //PendingIntent recurringLl24 = PendingIntent.getBroadcast(this, 0, ll24, PendingIntent.FLAG_CANCEL_CURRENT);
            //By using service
            Intent ll24 = new Intent(this, NotificationService.class);
            PendingIntent recurringLl24 = PendingIntent.getService(this, 0, ll24, PendingIntent.FLAG_CANCEL_CURRENT);
            long currentTimeMillis = System.currentTimeMillis();
            AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            //alarms.setRepeating(AlarmManager.RTC_WAKEUP, currentTimeMillis, 60000, recurringLl24); // Log repetition

            BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
            type = "enable";
            backgroundWorker.execute();
            //startService(new Intent(this, NotificationService.class));
            myTimer = new Timer();
            myTimerTask = new MyTimerTask(this);
            //myTimer.scheduleAtFixedRate(myTimerTask,10000,10000);
            //myTimerTask.run();


        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Button equity, commodity;
        equity = (Button) findViewById(R.id.equity);
        equity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,EquityPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        commodity = (Button) findViewById(R.id.commodity);
        commodity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CommodityPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        /*
        Button b2 = (Button) findViewById(R.id.Equity);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,commodity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        */


    }

    @Override
    protected void onStop() {
        super.onStop();
        //startService(new Intent(this, NotificationService.class));

        //Intent ll24 = new Intent(this, NotificationAlarmReceiver.class);
        //PendingIntent recurringLl24 = PendingIntent.getBroadcast(this, 0, ll24, PendingIntent.FLAG_CANCEL_CURRENT);
        Intent ll24 = new Intent(this, NotificationService.class);
        PendingIntent recurringLl24 = PendingIntent.getService(this, 0, ll24, PendingIntent.FLAG_CANCEL_CURRENT);
        long currentTimeMillis = System.currentTimeMillis();
        //long nextUpdateTimeMillis = currentTimeMillis + 30 * DateUtils.SECOND_IN_MILLIS;
        AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //alarms.setRepeating(AlarmManager.RTC_WAKEUP, currentTimeMillis, nextUpdateTimeMillis, recurringLl24); // Log repetition
        //alarms.setRepeating(AlarmManager.RTC_WAKEUP, currentTimeMillis, 1000, recurringLl24); // Log repetition

    }


    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();

            if (exit) {
                finish(); // finish activity
            } else {
                Toast.makeText(this, "Press Back again to Exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the CommodityPage/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            stopService(new Intent(this, NotificationService.class));

            BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
            type = "status";
            backgroundWorker.execute(PhNo,type);


            myTimerTask.cancel();
            myTimer.cancel();
            myTimer.purge();
            session.logoutUser();
            //deleteCache();
            finish();
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteCache() {
        try {
            File dir = getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(MainActivity.this,NewsFeedActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_about_us) {
            Intent intent = new Intent(MainActivity.this,About.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_contact_us) {
            Intent intent = new Intent(MainActivity.this,Contact.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if (id == R.id.logout) {
            stopService(new Intent(this, NotificationService.class));

            BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
            type = "status";
            backgroundWorker.execute(PhNo,type);

            myTimerTask.cancel();
            myTimer.cancel();
            myTimer.purge();
            session.logoutUser();
            Toast.makeText(this,"Logout successful",Toast.LENGTH_LONG).show();
            //deleteCache();
            finish();
        }else if (id == R.id.nav_live_market) {
            Intent intent = new Intent(MainActivity.this,LiveMarketActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if (id == R.id.nav_web_site) {
            Intent intent = new Intent(this,WebViewActivity.class);
            intent.putExtra("NewsFeed","website" );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("Link","https://investinshares.in" );
            //intent.putExtra("Link","https://www.google.com" );
            startActivity(intent);
        }else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this,WebViewActivity.class);
            intent.putExtra("NewsFeed","profile" );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("Link","https://www.urbanpro.com/chennai/rajkumar-s/1957903" );
            startActivity(intent);
        }else if (id == R.id.nav_terms) {
            Intent intent = new Intent(MainActivity.this,TermsAndConditions.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class BackgroundWorkerJson extends AsyncTask<String,Void,String> {



        String json_string;
        JSONArray jsonArray;
        JSONObject jsonObject;


        @Override
        protected String doInBackground(String... params) {

            String login_url ="http://androfocus.com.md-ht-6.hostgatorwebservers.com/InvestNShare/Login.php";
            //String login_url ="http://investinshares.in.md-114.hostgatorwebservers.com/InvestNShare/Login.php";
            //String login_url ="https://investinshares.in/InvestNShare/Login.php";

            String status_url = "http://androfocus.com.md-ht-6.hostgatorwebservers.com/InvestNShare/LoginSetStatus.php";
            //String status_url = "http://investinshares.in.md-114.hostgatorwebservers.com/InvestNShare/LoginSetStatus.php";
            //String status_url = "https://investinshares.in/InvestNShare/LoginSetStatus.php";

            if(type.equals("status")){
                try {
                    //phone_no = params[0];
//                    type = params[1];
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

            }else if(type.equals("enable")){
                try {
                    //phone_no = params[0];
                //    type = params[1];
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

            if(type.equals("enable")){
                json_string = result;
//                Log.e("Image JSON", json_string);

                if(json_string == null){
                    //Toast.makeText(MainActivity.this,"First Get JSON data",Toast.LENGTH_LONG).show();
                }
                else {
                    Log.e("JSON", json_string);
                    try {
                        jsonObject = new JSONObject(json_string);
                        jsonArray = jsonObject.getJSONArray("Login");

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jo = jsonArray.getJSONObject(i);

                            name = jo.getString("name");
                            phone_no = jo.getString("phone_no");
                            enabled1 = jo.getString("enabled1");

                        }

                        if(PhNo.equals(phone_no)){
                            if(enabled1.equals("No")){
                                BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                                type = "status";
                                backgroundWorker.execute();
                                session.logoutUser();
                                Toast.makeText(MainActivity.this,"Please contact our branch manager to enable you to login",Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            Toast.makeText(MainActivity.this,"Your Mobile number is incorrect",Toast.LENGTH_LONG).show();
                        }

                        String versionNamePlay,versionNameApp = null;
                        versionNamePlay = jsonObject.getString("versionName");
                        Log.e("versionNamePlay", versionNamePlay);

                        try {
                            PackageInfo pInfo = MainActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
                            versionNameApp= pInfo.versionName;
                            Log.e("versionNameApp", versionNameApp);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        if(!versionNameApp.equals(versionNamePlay)){
                            final android.support.v7.app.AlertDialog.Builder notifyNewVersionApp = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                            notifyNewVersionApp.setTitle("Product update");
                            notifyNewVersionApp.setMessage("A new version is available. Would you like to upgrade now?\n(Current: "+versionNameApp+" Latest: "+versionNamePlay);
                            notifyNewVersionApp.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String url = "https://play.google.com/store/apps/details?id=com.androfocus.investnshare";
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                }
                            });
                            notifyNewVersionApp.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    android.support.v7.app.AlertDialog alert1 = notifyNewVersionApp.create();
                                    alert1.cancel();
                                }
                            });
                            notifyNewVersionApp.show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        }

    }
}
