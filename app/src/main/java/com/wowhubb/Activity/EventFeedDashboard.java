package com.wowhubb.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.Fragment.EventFeedFragment;
import com.wowhubb.Fragment.EventHubbFragment;
import com.wowhubb.Fragment.MyNetworkFragment;
import com.wowhubb.Fragment.WowFragment;
import com.wowhubb.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ramya on 07-08-2017.
 */

public class EventFeedDashboard extends AppCompatActivity {
    Typeface lato;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageView backiv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventfeeddashboard);
        getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        lato = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(EventFeedDashboard.this, v1);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        backiv=findViewById(R.id.backiv);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();
        backiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventFeedDashboard.this, LandingPageActivity.class));
            }
        });

    }

    private void setupTabIcons() {
        View v2 = tabLayout.getRootView();
        FontsOverride.overrideFonts(EventFeedDashboard.this, v2);
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabOne.setText("Event Feed");
        tabOne.setTextSize(14);
        tabOne.setTypeface(lato);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.eventfeed_tab,0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabTwo.setText(" Network");
        tabTwo.setTypeface(lato);
        tabTwo.setTextSize(14);
        tabTwo.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.network_tab, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabThree.setText(" Wowtag");
        tabThree.setTypeface(lato);
        tabThree.setTextSize(14);
        tabThree.setGravity(View.TEXT_ALIGNMENT_CENTER);

        tabThree.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.wowtag_tab,0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabfour = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabfour.setText(" Event Hubb");
        tabfour.setSingleLine();
        tabfour.setTypeface(lato);
        tabfour.setTextSize(14);

        tabThree.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        tabfour.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.eventhubb_tab , 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabfour);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new EventFeedFragment(), "Event Feed");
        adapter.addFrag(new MyNetworkFragment(), "My Network");
        adapter.addFrag(new WowFragment(), "Wowtag");
        adapter.addFrag(new EventHubbFragment(), "Event Hubb");
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

}
