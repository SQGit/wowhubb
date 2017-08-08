package com.wowhubb.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.sa90.materialarcmenu.ArcMenu;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.Fragment.EventFragment;
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
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static ArcMenu arcMenu;
    FloatingActionButton createevent_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventfeeddashboard);
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(EventFeedDashboard.this, v1);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        arcMenu = (ArcMenu) findViewById(R.id.arcMenu);
        arcMenu.setRadius(getResources().getDimension(R.dimen.radius));

        EventFeedDashboard.arcMenu.setVisibility(View.VISIBLE);

        createevent_fab = findViewById(R.id.fab1);

        createevent_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "create event", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new EventFragment(), "Event Feed".toLowerCase());
        adapter.addFrag(new MyNetworkFragment(), "My Network".toLowerCase());
        adapter.addFrag(new WowFragment(), "Wowtag".toLowerCase());
        adapter.addFrag(new EventHubbFragment(), "My Event Hubb".toLowerCase());
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
