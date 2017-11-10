package com.wowhubb.Fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wowhubb.Activity.EventFeedDashboard;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

import java.util.ArrayList;
import java.util.List;


public class EventServiceFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static TextView titletv;

    public EventServiceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("tag", "Ramya");
        View view = inflater.inflate(R.layout.fragment_eventservice,
                container, false);
        FontsOverride.overrideFonts(getActivity(), view);

        //getSupportActionBar().hide();

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager1(viewPager);
        Log.e("tag", "Ramya11");
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        Log.e("tag", "Ramya1221");
        return view;
    }

    private void setupViewPager1(ViewPager viewPager) {
        Log.e("tag", "Ramyasetupp");
        ViewPagerAdapter1 adapter1 = new ViewPagerAdapter1(getActivity().getSupportFragmentManager());
        Log.e("tag", "sdadssd");
        adapter1.addFrag(new EventServiceVenueFragment(), "Event Venue");
        adapter1.addFrag(new EventServiceProvider(), "Event Service Providers".toLowerCase());
        adapter1.addFrag(new EventServiceNearbyEnents(), "Nearby Events".toLowerCase());
        viewPager.setAdapter(adapter1);
    }

    class ViewPagerAdapter1 extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList1 = new ArrayList<>();
        private final List<String> mFragmentTitleList1 = new ArrayList<>();

        public ViewPagerAdapter1(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList1.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList1.size();
        }

        public void addFrag(Fragment fragment, String title) {
            Log.e("tag", "55555");
            mFragmentList1.add(fragment);
            mFragmentTitleList1.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList1.get(position);
        }


    }

}
