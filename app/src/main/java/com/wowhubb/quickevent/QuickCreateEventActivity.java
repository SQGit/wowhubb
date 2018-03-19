package com.wowhubb.quickevent;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.badoualy.stepperindicator.StepperIndicator;
import com.wowhubb.Adapter.PagerAdapter;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.Fragment.EventContactFragment;
import com.wowhubb.Fragment.EventHighlightsFragment;
import com.wowhubb.Fragment.EventTypeFragment;
import com.wowhubb.Fragment.EventVenueFragment;
import com.wowhubb.Fragment.ProgramScheduleFragmentNew;
import com.wowhubb.Fragment.WowtagFragment;
import com.wowhubb.R;
import com.wowhubb.data.EventData;

/**
 * Created by Ramya on 25-07-2017.
 */

public class QuickCreateEventActivity extends AppCompatActivity {

    public static TextView skiptv;
    public static TextView backiv, publishtv;
    public static Snackbar snackbar;
    static ViewPager pager;
    public EventData eventData;
    TextView fab, previous_fab;
    Context context = this;
    String navdashboard;
    Bundle extras;
    Typeface lato;
    TextView tv_snack;
    int currentPage;
    LinearLayout nextlv, previouslv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        eventData = new EventData();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quickcreate_event);
        getSupportActionBar().hide();

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(QuickCreateEventActivity.this, v1);
        extras = getIntent().getExtras();
        lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        Log.e("tag", "navdashboard------------------->+" + navdashboard);

        pager = findViewById(R.id.pager);
        fab = findViewById(R.id.next_fb);
        previous_fab = findViewById(R.id.previous_fb);
        backiv = findViewById(R.id.backiv);
        skiptv = findViewById(R.id.skiptv);
        publishtv = findViewById(R.id.publishtv);
        nextlv = findViewById(R.id.nextlv);
        previouslv = findViewById(R.id.previouslv);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        pager.setOffscreenPageLimit(1);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager(), width, height));

        final StepperIndicator indicator = findViewById(R.id.stepper_indicator);
        indicator.setViewPager(pager);
        loadInitialFragment();

        indicator.addOnStepClickListener(new StepperIndicator.OnStepClickListener() {
            @Override
            public void onStepClicked(int step) {
                //  pager.setCurrentItem(step);
            }
        });

        //-----------------------------------------SNACKBAR----------------------------------------//

        snackbar = Snackbar.make(findViewById(R.id.top), R.string.networkError, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        tv_snack = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(lato);
        previous_fab.setVisibility(View.INVISIBLE);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPage = position;
                switch (position) {
                    case 0:
                        fab.setVisibility(View.VISIBLE);
                        skiptv.setVisibility(View.GONE);
                        publishtv.setVisibility(View.GONE);
                        break;
                    case 1:
                        fab.setVisibility(View.VISIBLE);
                        skiptv.setVisibility(View.GONE);
                        publishtv.setVisibility(View.GONE);
                        break;
                    case 2:
                        fab.setVisibility(View.VISIBLE);
                        publishtv.setVisibility(View.GONE);
                        skiptv.setVisibility(View.GONE);
                        break;
                    case 3:
                        fab.setVisibility(View.GONE);
                        skiptv.setVisibility(View.GONE);
                        publishtv.setVisibility(View.VISIBLE);
                        break;

                    default:
                        skiptv.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);

                        Log.e("tag", "11111111wwwwwww----");
                        break;

                }


            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

        backiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (extras != null) {
                    navdashboard = extras.getString("navdashboard");
                    if (navdashboard.equals("true")) {
                        finish();
                    }
                } else {
                    finish();
                }

            }
        });





        previous_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag", "fdshghfjhjfhj---------->>>>>>>" + currentPage);
                if (currentPage == 0) {
                    previous_fab.setVisibility(View.INVISIBLE);
                    previous_fab.setEnabled(false);
                    QuickCreateEventActivity.super.onBackPressed();
                    currentPage = currentPage + 1;
                    pager.setCurrentItem(currentPage, true);
                }
                if (currentPage == 1) {
                    previous_fab.setVisibility(View.INVISIBLE);
                    previous_fab.setEnabled(false);
                    QuickCreateEventActivity.super.onBackPressed();
                    currentPage = currentPage - 1;
                    pager.setCurrentItem(currentPage, true);
                } else {
                    previous_fab.setVisibility(View.VISIBLE);
                    previous_fab.setEnabled(true);
                    QuickCreateEventActivity.super.onBackPressed();
                    currentPage = currentPage - 1;
                    pager.setCurrentItem(currentPage, true);
                }


            }
        });
        previouslv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previous_fab.performClick();
            }
        });

        nextlv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.performClick();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("tag", "Current step------>");
                Log.e("tag", " step------>" + currentPage);
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String highlight1 = sharedPreferences.getString("highlight1", "");
                String highlight2 = sharedPreferences.getString("highlight2", "");
                String video1 = sharedPreferences.getString("video1", "");
                String coverphoto = sharedPreferences.getString("coverpage", "");


                if (currentPage == 0) {
                    fab.setVisibility(View.VISIBLE);
                    if (!EventTypeFragment.eventtopic_et.getText().toString().trim().equalsIgnoreCase("")) {
                        EventTypeFragment.eventtopic_til.setError(null);
                        if (EventTypeFragment.str_category != null) {

                            if (!EventTypeFragment.eventtimezone_et.getText().toString().trim().equalsIgnoreCase("")) {
                                EventTypeFragment.eventtimezone_til.setError(null);
                                if (!EventTypeFragment.eventdate_et.getText().toString().trim().equalsIgnoreCase("")) {
                                    EventTypeFragment.eventdate_til.setError(null);
                                   /* if (!EventTypeFragment.eventdateto_et.getText().toString().trim().equalsIgnoreCase("")) {
                                        EventTypeFragment.eventdateto_til.setError(null);
*/
                                    if (EventTypeFragment.str_eventday != null) {
                                        if (!EventTypeFragment.desc_et.getText().toString().trim().equalsIgnoreCase("")) {
                                            EventTypeFragment.desc_til.setError(null);

                                            if (EventTypeFragment.selectedCoverFilePath != null) {

                                                WowtagFragment WowtagFragment = new WowtagFragment();
                                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                                fragmentTransaction.setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left);
                                                fragmentTransaction.replace(R.id.frame_layout, WowtagFragment, "tag");
                                                fragmentTransaction.addToBackStack("tag");
                                                Log.e("tag", "jvkbjk" + currentPage);
                                                fragmentTransaction.commit();
                                                currentPage = currentPage + 1;
                                                pager.setCurrentItem(currentPage, true);
                                                previous_fab.setVisibility(View.VISIBLE);
                                                // pager.setCurrentItem(currentPage);
                                                Log.e("tag", "jvkbjk");
                                            } else {
                                                SpannableString s = new SpannableString("Please Capture any Cover Page Image for your Event");
                                                s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                                                EventTypeFragment.scrollView.scrollTo(0, EventTypeFragment.scrollView.getBottom());
                                            }

                                        } else {
                                            SpannableString s = new SpannableString("Enter Event Description");
                                            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            EventTypeFragment.desc_til.setError(s);
                                            EventTypeFragment.desc_et.requestFocus();
                                            EventTypeFragment.scrollView.scrollTo(0, EventTypeFragment.desc_til.getBottom());
                                        }
                                    } else {


                                        SpannableString s = new SpannableString("Select Event Days");
                                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        Toast.makeText(QuickCreateEventActivity.this, s, Toast.LENGTH_LONG).show();
                                        EventTypeFragment.eventDay_spn.setFocusable(true);

                                    }



                                } else {
                                    SpannableString s = new SpannableString("Select Event Start Date");
                                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    EventTypeFragment.eventdate_til.setError(s);


                                }

                            } else {

                                SpannableString s = new SpannableString("Enter Event City");
                                s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                EventTypeFragment.eventtimezone_til.setError(s);
                                EventTypeFragment.eventtimezone_et.requestFocus();
                            }
                        } else {
                            SpannableString s = new SpannableString("Select Event Category");
                            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        SpannableString s = new SpannableString("Enter Event Name");
                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        EventTypeFragment.eventtopic_til.setError(s);
                        EventTypeFragment.eventtopic_et.setFocusable(true);
                        EventTypeFragment.eventtopic_et.requestFocus();
                        EventTypeFragment.scrollView.scrollTo(0, EventTypeFragment.scrollView.getTop());
                    }


                } else if (currentPage == 1) {

                    Log.e("tag", "11111222");
                    fab.setVisibility(View.VISIBLE);
                    Log.e("tag", "3333333333");
                    if (!WowtagFragment.eventname_et.getText().toString().trim().equalsIgnoreCase("")) {
                        WowtagFragment.eventname_et.setError(null);

                        if (WowtagFragment.selectedVideoFilePath1 != null) {

                            if (!WowtagFragment.fromtime_tv.getText().toString().trim().equalsIgnoreCase("")) {
                                WowtagFragment.til_from.setError(null);
                                if (!WowtagFragment.totime_tv.getText().toString().trim().equalsIgnoreCase("")) {
                                    WowtagFragment.til_to.setError(null);
                                    Log.e("tag", "hgchgxc");
                                    setData();
                                    EventVenueFragment eventVenueFragment = new EventVenueFragment();
                                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left);
                                    fragmentTransaction.replace(R.id.frame_layout, eventVenueFragment, "tag");
                                    fragmentTransaction.addToBackStack("tag");
                                    fragmentTransaction.commit();
                                    currentPage = currentPage + 1;
                                    pager.setCurrentItem(currentPage, true);
                                    previous_fab.setVisibility(View.VISIBLE);

                                } else {
                                    SpannableString s = new SpannableString("Select End Date");
                                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    WowtagFragment.til_to.setError(s);
                                }

                            } else {
                                SpannableString s = new SpannableString("Select Start Date");
                                s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                WowtagFragment.til_from.setError(s);
                            }
                        } else {
                            SpannableString s = new SpannableString("Select Wowtag Video");
                            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        SpannableString s = new SpannableString("Enter Event Title");
                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        WowtagFragment.eventname_et.setError(s);
                        WowtagFragment.eventname_et.requestFocus();
                    }


                } else if (currentPage == 2) {
                    fab.setVisibility(View.VISIBLE);

                    // publishtv.setVisibility(View.VISIBLE);
                    if (!EventVenueFragment.venuename.getText().toString().trim().equalsIgnoreCase("")) {
                        Log.e("tag", "hsgdfd");
                        EventVenueFragment.postDataToSQLite();
                        // pager.setCurrentItem(indicator.getCurrentStep() + 1, true);
                        Log.e("tag", "ifff" + EventVenueFragment.listBeneficiary.size());
                        ProgramScheduleFragmentNew programScheduleFragmentNew = new ProgramScheduleFragmentNew();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left);
                        fragmentTransaction.replace(R.id.frame_layout, programScheduleFragmentNew, "tag");
                        fragmentTransaction.addToBackStack("tag");
                        fragmentTransaction.commit();
                        currentPage = currentPage + 1;
                        pager.setCurrentItem(currentPage, true);

                    } else {

                        // pager.setCurrentItem(indicator.getCurrentStep() + 1, true);
                        ProgramScheduleFragmentNew programScheduleFragmentNew = new ProgramScheduleFragmentNew();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout, programScheduleFragmentNew, "tag");
                        fragmentTransaction.addToBackStack(null);

                        fragmentTransaction.commitAllowingStateLoss();
                        currentPage = currentPage + 1;
                        pager.setCurrentItem(currentPage, true);

                    }

                }  else if (currentPage == 3) {
                    // pager.setCurrentItem(indicator.getCurrentStep() + 1, true);
                    fab.setVisibility(View.INVISIBLE);


                } else {
                    //  pager.setCurrentItem(indicator.getCurrentStep() + 1, true);
                    fab.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    private void setData() {
        eventData.eventttitle = WowtagFragment.eventname_et.getText().toString();

    }

    private void loadInitialFragment() {
        EventTypeFragment eventTypeFragment = new EventTypeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left);

        fragmentTransaction.replace(R.id.frame_layout, eventTypeFragment, "tag");
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commitAllowingStateLoss();
        currentPage = 0;
        previous_fab.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO);

        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED)) {
            Log.e("tag", "Permission is granted");
            return true;
        } else {
            Log.e("tag", "Permission is revoked");
            ActivityCompat.requestPermissions(QuickCreateEventActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }
    }

    @Override
    public void onBackPressed() {

        int counter = getSupportFragmentManager().getBackStackEntryCount();
        Log.e("tag", "counter----->" + counter);

        if (counter == 1) {
            finish();
            // super.onBackPressed();
        }
    }
}
