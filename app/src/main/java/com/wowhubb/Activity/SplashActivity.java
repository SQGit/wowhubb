package com.wowhubb.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import com.wowhubb.Adapter.ViewPagerAdapter;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.Pager.SimpleViewPagerIndicator;
import com.wowhubb.R;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Ramya on 19-07-2017.
 */

public class SplashActivity extends Activity {

    final long DELAY_MS = 300;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    int currentPage = 0;
    Timer timer;
    TextView splashcontent_tv;
    int i;
    TextView registertv, logintv;
    int NUM_PAGES = 4;
    String status;
    private ViewPager mPager;
    private SimpleViewPagerIndicator pageIndicator;
    private int resourceList[] = {R.drawable.splash_image1,
            R.drawable.splash_image2, R.drawable.splash_image3
    };
    private int List[] = {R.string.splashtwo, R.string.splashtwo, R.string.splashtwo};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        splashcontent_tv = (TextView) findViewById(R.id.splashcontent);
        status = sharedPreferences.getString("status", "");

        registertv = findViewById(R.id.registertv);
        logintv = findViewById(R.id.logintv);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(SplashActivity.this, v1);
        initView();



        registertv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, RegisterCountryActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        logintv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });


        if (status == "") {
            logintv.setVisibility(View.VISIBLE);
            registertv.setVisibility(View.VISIBLE);
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {

                    if (currentPage == 1)
                    {
                        String splashone = getString(R.string.splashtwo);
                        splashcontent_tv.setText("");
                        splashcontent_tv.setText(splashone);

                    } else if (currentPage == 2) {

                        String splashtwo = getString(R.string.splashothree);
                        splashcontent_tv.setText("");
                        splashcontent_tv.setText(splashtwo);
                    } else if (currentPage == 3) {

                        String splashthree = getString(R.string.splashone);
                        splashcontent_tv.setText("");
                        splashcontent_tv.setText(splashthree);

                    }


                    if (currentPage == NUM_PAGES - 1) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                }
            };

            timer = new Timer(); // This will create a new Thread
            timer.schedule(new TimerTask() { // task to be scheduled

                @Override
                public void run() {

                    handler.post(Update);

                }
            }, DELAY_MS, PERIOD_MS);


        }

        else if(status.equals("false"))
        {

            logintv.setVisibility(View.VISIBLE);
            registertv.setVisibility(View.VISIBLE);
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {

                    if (currentPage == 1) {

                        Log.e("tag", "page1---------->" + currentPage);
                        String splashone = getString(R.string.splashtwo);
                        splashcontent_tv.setText("");
                        splashcontent_tv.setText(splashone);


                    } else if (currentPage == 2) {

                        Log.e("tag", "page2---------->" + currentPage);
                        String splashtwo = getString(R.string.splashothree);
                        splashcontent_tv.setText("");
                        splashcontent_tv.setText(splashtwo);

                    } else if (currentPage == 3) {

                        Log.e("tag", "page3---------->" + currentPage);
                        String splashthree = getString(R.string.splashone);
                        splashcontent_tv.setText("");
                        splashcontent_tv.setText(splashthree);


                    }


                    if (currentPage == NUM_PAGES - 1) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                }
            };

            timer = new Timer(); // This will create a new Thread
            timer.schedule(new TimerTask() { // task to be scheduled

                @Override
                public void run() {

                    handler.post(Update);

                }
            }, DELAY_MS, PERIOD_MS);







        }








        else {
            logintv.setVisibility(View.INVISIBLE);
            registertv.setVisibility(View.INVISIBLE);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                        startActivity(new Intent(SplashActivity.this, LandingPageActivity.class));
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

                }
            }, 2000);

        }
    }

    public void initView() {
        // TODO Auto-generated method stub
        mPager = (ViewPager) findViewById(R.id.pager);
        pageIndicator = (SimpleViewPagerIndicator) findViewById(R.id.page_indicator);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        ViewPagerAdapter mPagerAdapter = new ViewPagerAdapter(this,
                resourceList, List, width, height);
        mPager.setOffscreenPageLimit(1);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(0); // current item number
        pageIndicator.setViewPager(mPager);
        pageIndicator.notifyDataSetChanged();
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.RECORD_AUDIO);

        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED)) {
            Log.e("tag", "Permission is granted");
            return true;


        } else {
            Log.e("tag", "Permission is revoked");
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;

        }
    }
}
