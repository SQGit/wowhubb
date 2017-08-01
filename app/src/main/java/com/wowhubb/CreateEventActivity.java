package com.wowhubb;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.badoualy.stepperindicator.StepperIndicator;
import com.wowhubb.Adapter.PagerAdapter;


/**
 * Created by Ramya on 25-07-2017.
 */

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        final ViewPager pager = findViewById(R.id.pager);
        // assert pager != null;
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        StepperIndicator indicator = findViewById(R.id.stepper_indicator);
        // We keep last page for a "finishing" page
        indicator.setViewPager(pager);
        indicator.addOnStepClickListener(new StepperIndicator.OnStepClickListener()
        {
            @Override
            public void onStepClicked(int step) {
                pager.setCurrentItem(step, true);
            }
        });


    }

}
