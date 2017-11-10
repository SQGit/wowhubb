package com.wowhubb.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Cache;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;
import com.wowhubb.app.AppController;

/**
 * Created by Salman on 05-10-2017.
 */

public class LandingPageActivity extends Activity
{

    TextView viewfeed;
    LinearLayout createevents_lv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landingpage);
        viewfeed = findViewById(R.id.vieweventfeed);
        createevents_lv = findViewById(R.id.createevents_lv);

        final View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(LandingPageActivity.this, v1);

        viewfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                startActivity(new Intent(LandingPageActivity.this, EventFeedDashboard.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Cache cache = AppController.getInstance().getRequestQueue().getCache();
                cache.clear();
            }
        });


        createevents_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LandingPageActivity.this, CreateEventActivity.class));
            }
        });


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logout")
                .setMessage("Do You want to Logout this App?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent setIntent = new Intent(Intent.ACTION_MAIN);
                        setIntent.addCategory(Intent.CATEGORY_HOME);
                        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(setIntent);
                        finish();
                        SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor edit = sharedPrefces.edit();
                        ///edit.clear();
                        edit.putString("status", "false");
                        edit.commit();
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }
}
