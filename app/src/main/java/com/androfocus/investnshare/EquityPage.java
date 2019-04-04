package com.androfocus.investnshare;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Timer;

public class EquityPage extends AppCompatActivity {
    Timer myTimer;
    MyTimerTask myTimerTask;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    Button b1;
    //private static final Integer[] IMAGES= {R.drawable.b11,R.drawable.b12,R.drawable.b13,R.drawable.b11};
    //private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equity_page);
        //init();
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Equity"));
        tabLayout.addTab(tabLayout.newTab().setText("Commodity"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        final EquityPagerAdapter adapter = new EquityPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));



        //mPager.setAdapter(new ViewPageAdapter(CommodityPage.this, ImagesArray));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        myTimer = new Timer();
        myTimerTask = new MyTimerTask(this);
        //myTimer.scheduleAtFixedRate(myTimerTask,10000,10000);
        //myTimerTask.run();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //startService(new Intent(this, NotificationService.class));
    }

    @Override
    public void onBackPressed() {
        myTimerTask.cancel();
        myTimer.cancel();
        myTimer.purge();
        Intent intent = new Intent(EquityPage.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void goBack(View view) {
        myTimerTask.cancel();
        myTimer.cancel();
        myTimer.purge();
        Intent intent = new Intent(EquityPage.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    /*private void init() {
        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);

        mPager = (ViewPager) findViewById(R.id.pager);

    }*/

}

