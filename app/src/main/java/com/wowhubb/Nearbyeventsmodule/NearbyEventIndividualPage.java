package com.wowhubb.Nearbyeventsmodule;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.Nearbyeventsmodule.Activity.AllNearbyEventActivity;
import com.wowhubb.Nearbyeventsmodule.Activity.PlayVideoActivity;
import com.wowhubb.Nearbyeventsmodule.Fragment.InfoEventFragment;
import com.wowhubb.Nearbyeventsmodule.Fragment.ScheduleEventFragment;
import com.wowhubb.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guna on 09-02-2018.
 */

public class NearbyEventIndividualPage extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    Typeface segoeui;
    ImageView wowtag_play,img_back;
    LinearLayout lnr_wowtag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_event_individual_page);
        getSupportActionBar().hide();

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(NearbyEventIndividualPage.this, v1);
        segoeui = Typeface.createFromAsset(getAssets(), "fonts/segoeui.ttf");

        img_back=findViewById(R.id.img_back);
        lnr_wowtag=findViewById(R.id.lnr_wowtag);

        lnr_wowtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent video=new Intent(getApplicationContext(), PlayVideoActivity.class);
               startActivity(video);
               finish();
            }
        });


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(getApplicationContext(),AllNearbyEventActivity.class);
                startActivity(i);
                finish();
            }
        });


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new InfoEventFragment(), "Event Info");
        adapter.addFrag(new ScheduleEventFragment(), "Event Schedule");
        adapter.addFrag(new HighlightEventFragment(), "Event Highlights");
        adapter.addFrag(new EventDiscussionFragment(), "Event Discussions");
        viewPager.setAdapter(adapter);
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




  private void setupTabIcons() {
        View v2 = tabLayout.getRootView();
        FontsOverride.overrideFonts(NearbyEventIndividualPage.this, v2);
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabOne.setText("Event Info");
        tabOne.setMinimumWidth(0);
        tabOne.setTextSize(15);
        tabOne.setTypeface(segoeui);
        //tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.eventfeed_tab, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabTwo.setText(" Event Schedule");
        tabTwo.setMinimumWidth(0);
        tabTwo.setTypeface(segoeui);
        tabTwo.setTextSize(15);
        tabTwo.setGravity(View.TEXT_ALIGNMENT_CENTER);
        //tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.network_tab, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

         TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabThree.setText(" " +
                " Highlights");
        tabThree.setMinimumWidth(0);
        tabThree.setTypeface(segoeui);
        tabThree.setTextSize(15);
        tabThree.setGravity(View.TEXT_ALIGNMENT_CENTER);

        //tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.wowtag_tab, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabfour = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabfour.setText(" Event Discussions");
        tabfour.setMinimumWidth(0);
        tabfour.setSingleLine();
        tabfour.setTypeface(segoeui);
        tabfour.setTextSize(15);

        tabThree.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        //tabfour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.eventhubb_tab, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabfour);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(),AllNearbyEventActivity.class);
        startActivity(i);
        finish();
    }
}
