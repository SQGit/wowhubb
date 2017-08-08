package com.wowhubb.Activity;

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
import com.wowhubb.R;


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
        getSupportActionBar().hide();

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(CreateEventActivity.this, v1);
        pager = findViewById(R.id.pager);
        fab = findViewById(R.id.next_fb);

        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        final StepperIndicator indicator = findViewById(R.id.stepper_indicator);
        //View view =Crea;;
        // View view = indicator.getRootView();
        //FontsOverride.overrideFonts(CreateEventActivity.this, view);
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
                indicator.setLabelColor(R.color.colorPrimary);
                Log.d("tag", "Current step----------->" + indicator.getCurrentStep() + indicator.getStepCount());
                pager.setCurrentItem(indicator.getCurrentStep() + 1, true);
            }
        });


    }

}
