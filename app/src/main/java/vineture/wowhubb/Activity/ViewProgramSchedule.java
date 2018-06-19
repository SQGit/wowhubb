package vineture.wowhubb.Activity;

/**
 * Created by Salman on 13-02-2018.
 */

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import vineture.wowhubb.Adapter.SchedulePagerAdapter;
import vineture.wowhubb.FeedsData.Programschedule;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.R;

import java.util.ArrayList;

public class ViewProgramSchedule extends Fragment {

    public static ArrayList<ArrayList<Programschedule>> ps = new ArrayList<>();


    int daycount = 3;

  //  ImageView closeiv;
    Typeface lato;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_program_schedule, container, false);

       // closeiv = view.findViewById(R.id.closeiv);
        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");
        View v1 = getActivity().getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(getActivity(), v1);
        TabLayout tabLayout = (TabLayout)view. findViewById(R.id.tab_layout);



        if (ViewMoreDetailspage.programschedules != null && ViewMoreDetailspage.programschedules.size() > 0) {
          ps.clear();
            sortData();
        }


        switch (ViewMoreDetailspage.eventdayscount) {
            case 1:
                tabLayout.addTab(tabLayout.newTab().setText("Day 1"));
                break;
            case 2:
                tabLayout.addTab(tabLayout.newTab().setText("Day 1"));
                tabLayout.addTab(tabLayout.newTab().setText("Day 2"));
                break;
            case 3:
                tabLayout.addTab(tabLayout.newTab().setText("Day 1"));
                tabLayout.addTab(tabLayout.newTab().setText("Day 2"));
                tabLayout.addTab(tabLayout.newTab().setText("Day 3"));
                break;

            case 4:
                tabLayout.addTab(tabLayout.newTab().setText("Day 1"));
                tabLayout.addTab(tabLayout.newTab().setText("Day 2"));
                tabLayout.addTab(tabLayout.newTab().setText("Day 3"));
                tabLayout.addTab(tabLayout.newTab().setText("Day 4"));
                break;

            case 5:
                tabLayout.addTab(tabLayout.newTab().setText("Day 1"));
                tabLayout.addTab(tabLayout.newTab().setText("Day 2"));
                tabLayout.addTab(tabLayout.newTab().setText("Day 3"));
                tabLayout.addTab(tabLayout.newTab().setText("Day 4"));
                tabLayout.addTab(tabLayout.newTab().setText("Day 5"));
                break;


            case 6:
                tabLayout.addTab(tabLayout.newTab().setText("Day 1"));
                tabLayout.addTab(tabLayout.newTab().setText("Day 2"));
                tabLayout.addTab(tabLayout.newTab().setText("Day 3"));
                tabLayout.addTab(tabLayout.newTab().setText("Day 4"));
                tabLayout.addTab(tabLayout.newTab().setText("Day 5"));
                tabLayout.addTab(tabLayout.newTab().setText("Day 6"));
                break;
            case 7:
                tabLayout.addTab(tabLayout.newTab().setText("Day 1"));
                tabLayout.addTab(tabLayout.newTab().setText("Day 2"));
                tabLayout.addTab(tabLayout.newTab().setText("Day 3"));
                tabLayout.addTab(tabLayout.newTab().setText("Day 4"));
                tabLayout.addTab(tabLayout.newTab().setText("Day 5"));
                tabLayout.addTab(tabLayout.newTab().setText("Day 6"));
                tabLayout.addTab(tabLayout.newTab().setText("Day 7"));
                break;

            default:
                tabLayout.addTab(tabLayout.newTab().setText("Day 1"));
                break;

        }


        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        final SchedulePagerAdapter adapter = new SchedulePagerAdapter
                (getFragmentManager(), tabLayout.getTabCount(), ps);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for (int tabIndex = 0; tabIndex < tabLayout.getTabCount(); tabIndex++) {
            TextView tabTextView1 = (TextView) (((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(tabIndex)).getChildAt(1));
            tabTextView1.setAllCaps(false);
            tabTextView1.setTypeface(lato);
            tabTextView1.setTextSize(18);

        }


        return view;
    }

    private void addDay(ArrayList<Programschedule> programschedules, String s) {
        //load the data
    }


    private void sortData() {
        for (int j = 0; j < 7; j++) {
            ps.add(new ArrayList<Programschedule>());
        }
        Log.e("tag", "day 00000------------" + ViewMoreDetailspage.programschedules);
        Log.e("tag", "day 000001111111111------------" + ViewMoreDetailspage.programschedules);
        Log.e("tag", "day 1------------" + ViewMoreDetailspage.programschedules.size());
        for (int i = 0; i < ViewMoreDetailspage.programschedules.size(); i++) {
            switch (ViewMoreDetailspage.programschedules.get(i).getDay()) {
                case "1":
                    if (ViewMoreDetailspage.programschedules.get(i) != null && ViewMoreDetailspage.programschedules.size() > 0)
                    {
                        ps.get(0).add(ViewMoreDetailspage.programschedules.get(i));
                        Log.e("tag", "day 1------------" + ps.get(0));
                        //Log.e("tag", "day 1------------" + ps.get(0).get(0).getLocation());
                    }

                    break;
                case "2":
                    if (ViewMoreDetailspage.programschedules.get(i) != null && ViewMoreDetailspage.programschedules.size() > 0) {
                    // ps.get(1).clear();
                        ps.get(1).add(ViewMoreDetailspage.programschedules.get(i));
                        Log.e("tag", "day 1------------" + ps.get(1));
                        // Log.e("tag", "day 1------------" + ps.get(0).get(0).getLocation());
                    }
                    //   ps.get(1).add(programschedules.get(i));
                    break;
                case "3":
                    //ps.clear();
                    if (ps.get(2) != null && ps.get(2).size() > 0) {
                    //    ps.get(1).clear();
                        ps.get(2).add(ViewMoreDetailspage.programschedules.get(i));
                    } else {

                    }

                    break;
                case "4":
                    if (ps.get(3) != null && ps.get(3).size() > 0) {
                        //ps.get(3).clear();
                        ps.get(3).add(ViewMoreDetailspage.programschedules.get(i));
                        Log.e("tag", "day 1------------" + ps.get(0));
                        Log.e("tag", "day 1------------" + ps.get(0).get(0).getLocation());
                    }
                    // ps.get(3).add(programschedules.get(i));
                    break;
                case "5":

                    if (ps.get(4) != null && ps.get(4).size() > 0) {
                        //ps.get(4).clear();
                        ps.get(4).add(ViewMoreDetailspage.programschedules.get(i));
                        Log.e("tag", "day 1------------" + ps.get(0));
                        Log.e("tag", "day 1------------" + ps.get(0).get(0).getLocation());
                    }
                    // ps.get(4).add(programschedules.get(i));
                    break;
                case "6":

                    if (ps.get(5) != null && ps.get(5).size() > 0) {
                       // ps.get(5).clear();
                        ps.get(5).add(ViewMoreDetailspage.programschedules.get(i));
                        Log.e("tag", "day 1------------" + ps.get(0));
                        Log.e("tag", "day 1------------" + ps.get(0).get(0).getLocation());
                    }
                    // ps.get(5).add(programschedules.get(i));
                    break;
                case "7":

                    if (ps.get(6) != null && ps.get(6).size() > 0) {
                        //ps.get(6).clear();
                        ps.get(6).add(ViewMoreDetailspage.programschedules.get(i));
                        Log.e("tag", "day 1------------" + ps.get(0));
                        Log.e("tag", "day 1------------" + ps.get(0).get(0).getLocation());
                    }
                    // ps.get(6).add(programschedules.get(i));
                    break;


            }

        }

    }
}
