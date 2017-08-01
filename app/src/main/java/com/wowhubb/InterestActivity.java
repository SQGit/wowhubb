package com.wowhubb;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.sloop.fonts.FontsManager;

/**
 * Created by Ramya on 24-07-2017.
 */

public class InterestActivity extends Activity {
    TextView head_tv;
    Typeface latoheading, lato;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_interest);
        /*FontsManager.initFormAssets(InterestActivity.this, "fonts/lato.ttf");
        FontsManager.changeFonts(InterestActivity.this);*/
        latoheading = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/latoheading.ttf");
        lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        head_tv = (TextView) findViewById(R.id.head_tv);
        head_tv.setTypeface(latoheading);

        BusinessEventsFragment bef = new BusinessEventsFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, bef);
        ft.commit();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);


    }
}
