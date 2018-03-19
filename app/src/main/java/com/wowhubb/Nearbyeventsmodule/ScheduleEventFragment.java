package com.wowhubb.Nearbyeventsmodule;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ScheduleEventFragment extends Fragment {

    public static int page_val;
    Typeface lato;
    TextView tabTextView1;
    TextView tabTextView2;
    int selected_day_count = 3;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.event_schedule_fragment, container, false);
        FontsOverride.overrideFonts(getActivity(), view);

        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //page_val = sharedPreferences.getInt("str_eventday", 0);

        page_val=3;
       // page_val = selected_day_count;
        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) view.findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);



        for (int tabIndex = 0; tabIndex < tabs.getTabCount(); tabIndex++) {
            tabTextView1 = (TextView) (((LinearLayout) ((LinearLayout) tabs.getChildAt(0)).getChildAt(tabIndex)).getChildAt(1));
            tabTextView1.setAllCaps(false);
            tabTextView1.setTypeface(lato);
            tabTextView1.setTextSize(18);
            tabTextView1.setGravity(Gravity.LEFT);

        }
        return view;
    }


    //add fragment to tabs
    private void setupViewPager(ViewPager viewPager) {


        String input_date = "01/08/2017";
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        Date dt1 = null;
        try {
            dt1 = format1.parse(input_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat format2 = new SimpleDateFormat("EEE");
        String finalDay = format2.format(dt1);
        Log.e("tag", "calculate_date" + finalDay);





        DayAdapter adapter = new DayAdapter(getChildFragmentManager());
        for (int start_day = 0; start_day < page_val; start_day++) {
            int kk = start_day + 1;
            String tab_txt = "| Day " + kk;
            Log.e("tag", "terrrrr" + tab_txt);
            adapter.addFragment(new ScheduleInnerEventFragment(), tab_txt);
        }
        viewPager.setAdapter(adapter);


    }

    static class DayAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public DayAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {


            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return ScheduleInnerEventFragment.newInstance(0, "Day # 1");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return ScheduleInnerEventFragment.newInstance(1, "Day # 2");
                case 2: // Fragment # 1 - This will show SecondFragment
                    return ScheduleInnerEventFragment.newInstance(2, "Day # 3");

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return page_val;
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
