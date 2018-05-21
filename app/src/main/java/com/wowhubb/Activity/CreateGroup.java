package com.wowhubb.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.Fragment.CreateGroupFragment;
import com.wowhubb.Fragment.DiscoverGroupFragment;
import com.wowhubb.Fragment.EventGroupFragment;
import com.wowhubb.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guna on 09-02-2018.
 */

public class CreateGroup extends AppCompatActivity {
    Typeface segoeui;
    ImageView wowtag_play, backiv;
    FloatingActionButton createfab;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.create_group);
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(CreateGroup.this, v1);
        segoeui = Typeface.createFromAsset(getAssets(), "fonts/segoeui.ttf");
        wowtag_play = findViewById(R.id.wowtag_play);
        backiv = findViewById(R.id.backtv);
        createfab = findViewById(R.id.createfab);
        createfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateGroup.this, CreateGroupActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

            }
        });
        backiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateGroup.this, EventFeedDashboard.class);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
        adapter.addFrag(new CreateGroupFragment(), "Groups");
        adapter.addFrag(new EventGroupFragment(), "Event Groups");
        adapter.addFrag(new DiscoverGroupFragment(), "Discover Groups");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        View v2 = tabLayout.getRootView();
        FontsOverride.overrideFonts(CreateGroup.this, v2);

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabgroup_title, null);
        tabOne.setText("Groups");
        tabOne.setMinimumWidth(0);
        tabOne.setTextSize(15);
        tabOne.setTypeface(segoeui);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabgroup_title, null);
        tabTwo.setText("Event Groups");
        tabTwo.setMinimumWidth(0);
        tabTwo.setTypeface(segoeui);
        tabTwo.setTextSize(15);
        tabTwo.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabgroup_title, null);
        tabThree.setText("Discover Groups");
        tabThree.setMinimumWidth(0);
        tabThree.setTypeface(segoeui);
        tabThree.setTextSize(15);
        tabThree.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tabLayout.getTabAt(2).setCustomView(tabThree);
        tabLayout.setTabTextColors(
                getResources().getColor(R.color.textcolr),
                getResources().getColor(R.color.white)
        );

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
