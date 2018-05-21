package com.wowhubb.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.Fragment.EmailInviteFragment;
import com.wowhubb.Fragment.SmsInviteFragment;
import com.wowhubb.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guna on 09-02-2018.
 */

public class EventInviteActivity extends AppCompatActivity

{
    public static String eventId, str_number,status,token;
    Typeface segoeui;
    ImageView wowtag_play, backiv;
    FloatingActionButton createfab;
    Bundle extras;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    SharedPreferences.Editor editor;
    Dialog dialog;
    SharedPreferences sharedPrefces;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.eventinvite);


        extras = getIntent().getExtras();
        if (extras != null) {
            eventId = extras.getString("eventId");
            str_number = extras.getString("str_number");
        }


        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(EventInviteActivity.this, v1);
        segoeui = Typeface.createFromAsset(getAssets(), "fonts/segoeui.ttf");
        wowtag_play = findViewById(R.id.wowtag_play);
        backiv = findViewById(R.id.backiv);

        backiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                startActivity(new Intent(EventInviteActivity.this, EventFeedDashboard.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                finish();

            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabTextColors(Color.BLACK, Color.WHITE);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new SmsInviteFragment(), "SMS Invite");
        adapter.addFrag(new EmailInviteFragment(), "Email Invite");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        View v2 = tabLayout.getRootView();
        FontsOverride.overrideFonts(EventInviteActivity.this, v2);

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabgroup_title, null);
        tabOne.setText("SMS Invite");
        tabOne.setMinimumWidth(0);
        tabOne.setTextSize(15);
        tabOne.setTypeface(segoeui);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabgroup_title, null);
        tabTwo.setText("Email Invite");
        tabTwo.setMinimumWidth(0);
        tabTwo.setTypeface(segoeui);
        tabTwo.setTextSize(15);
        tabTwo.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }
}
