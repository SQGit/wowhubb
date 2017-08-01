package com.wowhubb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.sloop.fonts.FontsManager;

/**
 * Created by Ramya on 19-07-2017.
 */

public class SplashActivity extends Activity {

    private ViewPager mPager;
    private SimpleViewPagerIndicator pageIndicator;

    TextView splashcontent_tv;
    ImageView nextsub_iv;
    int i;

    private int resourceList[] = {R.drawable.splash_image1,
            R.drawable.splash_image2, R.drawable.splash_image3
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashcontent_tv = (TextView) findViewById(R.id.splashcontent);
        nextsub_iv = (ImageView) findViewById(R.id.next_sub_iv);
     //  FontsManager.initFormAssets(SplashActivity.this, "fonts/lato.ttf");
      //  FontsManager.changeFonts(SplashActivity.this);
        initView();
        i = 0;
        nextsub_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag","Value i------------>"+i);
                if (i == 0) {
                   // mPager.setCurrentItem(1);
                    mPager.setCurrentItem(1,true);
                    i = 1;
                    String splashone = getString(R.string.splashtwo);
                    splashcontent_tv.setText("");
                    splashcontent_tv.setText(splashone);
                    mPager.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.move_textview));
                    splashcontent_tv.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.move_textview));

                }

                else if (i == 1) {
                    //mPager.setCurrentItem(2);
                    mPager.setCurrentItem(2,true);
                    i = 2;
                    String splashtwo = getString(R.string.splashothree);
                    splashcontent_tv.setText("");
                    splashcontent_tv.setText(splashtwo);
                    mPager.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.move_textview));
                    splashcontent_tv.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.move_textview));
                }
                else if (i == 2) {

              //      mPager.setCurrentItem(0);
                //    i = 0;
                    String splashthree = getString(R.string.splashone);
                  //  splashcontent_tv.setText("");
                  //  splashcontent_tv.setText(splashthree);
                  //  mPager.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.move_textview));

                 //   splashcontent_tv.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.move_textview));

                    startActivity(new Intent(SplashActivity.this, RegisterCountryActivity.class));
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }


            }
        });
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
                resourceList, width, height);
        mPager.setOffscreenPageLimit(1);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(0); // current item number
        pageIndicator.setViewPager(mPager);
        pageIndicator.notifyDataSetChanged();
    }
}
