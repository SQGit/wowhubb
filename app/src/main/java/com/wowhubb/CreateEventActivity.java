package com.wowhubb;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.badoualy.stepperindicator.StepperIndicator;
import com.wowhubb.Adapter.PagerAdapter;
import com.wowhubb.Fonts.FontsOverride;


/**
 * Created by Ramya on 25-07-2017.
 */

public class CreateEventActivity extends AppCompatActivity {

    static ViewPager pager;
    FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        // FontsManager.initFormAssets(CreateEventActivity.this, "fonts/lato.ttf");
        //   FontsManager.changeFonts(CreateEventActivity.this);
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/lato.ttf");
        pager = findViewById(R.id.pager);
        fab = findViewById(R.id.next_fb);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        final StepperIndicator indicator = findViewById(R.id.stepper_indicator);
        // We keep last page for a "finishing" page

        indicator.setViewPager(pager);
        indicator.addOnStepClickListener(new StepperIndicator.OnStepClickListener() {
            @Override
            public void onStepClicked(int step) {
                pager.setCurrentItem(step, true);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indicator.getCurrentStep();
                Log.d("tag", "Current step----------->" + indicator.getCurrentStep() + indicator.getStepCount());
                pager.setCurrentItem(indicator.getCurrentStep() + 1, true);
            }
        });


    }

}
