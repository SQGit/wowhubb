package vineture.wowhubb.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import vineture.wowhubb.Activity.CreateEventActivity;
import vineture.wowhubb.Adapter.ExpandableListAdapter;
import vineture.wowhubb.Adapter.ExpandableListDataPump;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class ProgramScheduleFragmentNew extends Fragment {

    public static int page_val;
    public static String str_month, str_day, str_date, str_oneday, str_twoday, str_threeday;
    public static Snackbar snackbar;
    Typeface lato;
    TextView tabTextView1, event_name;
    TextView tabTextView2;
    int selected_day_count = 3;
    TextView tv_snack;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List expandableListTitle;
    LinearLayout helpfultips_tv;
    Dialog dialog;
    HashMap<String, List<String>> expandableListDetail;

    public static ProgramScheduleFragmentNew newInstance(int page, boolean isLast) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        if (isLast)
            args.putBoolean("isLast", true);
        final ProgramScheduleFragmentNew fragment = new ProgramScheduleFragmentNew();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_programschedule_listnew, container, false);
        FontsOverride.overrideFonts(getActivity(), view);
        //Toast.makeText(getActivity(), "program", Toast.LENGTH_LONG).show();
        CreateEventActivity.skiptv.setVisibility(View.INVISIBLE);
        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        event_name = view.findViewById(R.id.event_name);
        helpfultips_tv = view.findViewById(R.id.helpfultips_tv);
        event_name.setText(EventTypeFragment.eventtopic_et.getText().toString().trim());
        page_val = sharedPreferences.getInt("str_eventday", 0);


        //-----------------------------------------SNACKBAR----------------------------------------//
        snackbar = Snackbar.make(getActivity().findViewById(R.id.top), R.string.timeslot, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(lato);


        // page_val = selected_day_count;
        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) view.findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);
        EventVenueFragment.listBeneficiary.clear();
        EventVenueFragment.listBeneficiary.addAll(EventVenueFragment.databaseHelper.getAllBeneficiary());
        Log.e("tag", "----7777777777777777-------->" + EventVenueFragment.listBeneficiary.size());

        helpfultips_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_helpfultips);
                View v1 = dialog.getWindow().getDecorView().getRootView();
                ImageView close = dialog.findViewById(R.id.closeiv);

                FontsOverride.overrideFonts(dialog.getContext(), v1);

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) dialog).getWindow();

                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                expandableListView = (ExpandableListView) dialog.findViewById(R.id.expandableListView);
                expandableListDetail = ExpandableListDataPump.getData();
                expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                expandableListAdapter = new ExpandableListAdapter(dialog.getContext(), expandableListTitle, expandableListDetail);

                expandableListView.setAdapter(expandableListAdapter);

                expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    public void onGroupExpand(int groupPosition) {
                    }
                });

                expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                    public void onGroupCollapse(int groupPosition) {
                    }
                });

                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v,
                                                int groupPosition, int childPosition, long id) {
                        return true;
                    }
                });

                dialog.show();
            }
        });


        for (int tabIndex = 0; tabIndex < tabs.getTabCount(); tabIndex++) {
            tabTextView1 = (TextView) (((LinearLayout) ((LinearLayout) tabs.getChildAt(0)).getChildAt(tabIndex)).getChildAt(1));
            tabTextView1.setAllCaps(false);
            tabTextView1.setTypeface(lato);
            tabTextView1.setTextSize(18);

        }
        return view;
    }


    //add fragment to tabs
    private void setupViewPager(ViewPager viewPager) {
        DayAdapter adapter = new DayAdapter(getChildFragmentManager());
        for (int start_day = 0; start_day < page_val; start_day++) {

            Log.e("tag", "----88888888888888-------->" + EventVenueFragment.listBeneficiary.size());
            Log.e("tag", "start day------------->" + start_day);
            if (EventTypeFragment.strDateFormat1 != null) {
                if (start_day == 0) {
                    if (EventTypeFragment.strDateFormat1 != null) {
                        try {
                            SimpleDateFormat spf1 = new SimpleDateFormat("MMM/dd/yyyy");
                            try {
                                Date newDate1 = null;
                                newDate1 = spf1.parse(EventTypeFragment.strDateFormat1);
                                spf1 = new SimpleDateFormat("MMM/dd/yyyy EEE");
                                EventTypeFragment.strDateFormat1 = spf1.format(newDate1);
                                Log.e("tag", "date-------->" + EventTypeFragment.strDateFormat1);
                                String str_fromdate12[] = EventTypeFragment.strDateFormat1.split("/");
                                str_month = str_fromdate12[0];
                                str_date = str_fromdate12[1];
                                String str_totime[] = EventTypeFragment.strDateFormat1.split(" ");
                                str_day = str_totime[1];

                                int kk = start_day + 1;
                                if (str_date.equals("01")) {
                                    str_date = str_date + "st";
                                } else if (str_date.equals("02")) {
                                    str_date = str_date + "nd";
                                } else if (str_date.equals("03")) {
                                    str_date = str_date + "rd";
                                } else {
                                    str_date = str_date + "th";
                                }
                                String tab_txt = "Day " + kk + "\n  " + str_day + " - " + str_date + " " + str_month;

                                str_oneday = "Day " + kk + " - " + str_day.toUpperCase() + " " + str_date + " " + str_month.toUpperCase();

                                adapter.addFragment(new DayFragment(), tab_txt);

                            } catch (NullPointerException e) {

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        } catch (ArrayIndexOutOfBoundsException e) {

                        }
                    }


                } else if (start_day == 1) {


                    try {
                        SimpleDateFormat spf1 = new SimpleDateFormat("MMM/dd/yyyy");
                        try {
                            Date newDate1 = null;
                            newDate1 = spf1.parse(EventTypeFragment.strDateFormat1);
                            spf1 = new SimpleDateFormat("MMM/dd/yyyy EEE");
                            Date myDate = DateUtil.addDays(newDate1, 1);

                            EventTypeFragment.strDateFormat1 = spf1.format(myDate);

                            String str_fromdate12[] = EventTypeFragment.strDateFormat1.split("/");
                            str_month = str_fromdate12[0];
                            str_date = str_fromdate12[1];


                            String str_totime[] = EventTypeFragment.strDateFormat1.split(" ");
                            str_day = str_totime[1];

                            int kk = start_day + 1;
                            if (str_date.equals("01")) {
                                str_date = str_date + "st";
                            } else if (str_date.equals("02")) {
                                str_date = str_date + "nd";
                            } else if (str_date.equals("03")) {
                                str_date = str_date + "rd";
                            } else {
                                str_date = str_date + "th";
                            }
                            String tab_txt = "Day " + kk + "\n  " + str_day + " - " + str_date + " " + str_month;
                            str_twoday = "Day " + kk + " - " + str_day.toUpperCase() + " - " + str_date + " " + str_month.toUpperCase();

                            adapter.addFragment(new DayFragment(), tab_txt);
                        } catch (NullPointerException e)

                        {

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    } catch (ArrayIndexOutOfBoundsException e) {

                    }


                } else if (start_day == 2) {

                    try {
                        SimpleDateFormat spf1 = new SimpleDateFormat("MMM/dd/yyyy");
                        try {
                            Date newDate1 = null;
                            newDate1 = spf1.parse(EventTypeFragment.strDateFormat1);
                            spf1 = new SimpleDateFormat("MMM/dd/yyyy EEE");
                            Date myDate = DateUtil.addDays(newDate1, 1);

                            EventTypeFragment.strDateFormat1 = spf1.format(myDate);

                            String str_fromdate12[] = EventTypeFragment.strDateFormat1.split("/");
                            str_month = str_fromdate12[0];
                            str_date = str_fromdate12[1];


                            String str_totime[] = EventTypeFragment.strDateFormat1.split(" ");
                            str_day = str_totime[1];

                            int kk = start_day + 1;
                            if (str_date.equals("01")) {
                                str_date = str_date + "st";
                            } else if (str_date.equals("02")) {
                                str_date = str_date + "nd";
                            } else if (str_date.equals("03")) {
                                str_date = str_date + "rd";
                            } else {
                                str_date = str_date + "th";
                            }
                            String tab_txt = "Day " + kk + "\n  " + str_day + " - " + str_date + " " + str_month;
                            str_threeday = "Day " + kk + " - " + str_day.toUpperCase() + " - " + str_date + " " + str_month.toUpperCase();

                            adapter.addFragment(new DayFragment(), tab_txt);
                        } catch (NullPointerException e)

                        {

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    } catch (ArrayIndexOutOfBoundsException e) {

                    }

                }
            } else {
                int kk = start_day + 1;
                String tab_txt = "Day " + kk;

                adapter.addFragment(new DayFragment(), tab_txt);
            }


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
                case 0:
                    // Fragment # 0 - This will show FirstFragment
                    return DayFragment.newInstance(0, "Day # 1 ");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return DayFragment2.newInstance(1, "Day # 2");
                case 2: // Fragment # 1 - This will show SecondFragment
                    return DayFragment3.newInstance(2, "Day # 3");

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

    public static class DateUtil {
        public static Date addDays(Date date, int days) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, days); //minus number would decrement the days
            return cal.getTime();
        }
    }
}
