package com.wowhubb.Activity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.badoualy.stepperindicator.StepperIndicator;
import com.wowhubb.Adapter.PagerAdapter;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.Fragment.EventHighlightsFragment;
import com.wowhubb.Fragment.EventTypeFragment;
import com.wowhubb.Fragment.EventVenueFragment;
import com.wowhubb.Fragment.WowtagFragment;
import com.wowhubb.R;

/**
 * Created by Ramya on 25-07-2017.
 */

public class CreateEventActivity extends AppCompatActivity {

    public static TextView skiptv;
    static ViewPager pager;
    FloatingActionButton fab;
    Context context = this;
    TextView backiv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        getSupportActionBar().hide();
        Log.e("tag", "dbcreated11");
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(CreateEventActivity.this, v1);

        pager = findViewById(R.id.pager);
        fab = findViewById(R.id.next_fb);
        backiv = findViewById(R.id.backiv);
        skiptv = findViewById(R.id.skiptv);

        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

        final StepperIndicator indicator = findViewById(R.id.stepper_indicator);

        View v2 = indicator.getRootView();
        FontsOverride.overrideFonts(CreateEventActivity.this, v2);
        indicator.setViewPager(pager);


        indicator.addOnStepClickListener(new StepperIndicator.OnStepClickListener() {
            @Override
            public void onStepClicked(int step) {
            }
        });
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("tag", "podsition---------->" + position);
                switch (position) {
                    case 0:
                        fab.setVisibility(View.VISIBLE);
                        skiptv.setVisibility(View.GONE);
                        break;
                    case 1:
                        fab.setVisibility(View.VISIBLE);
                        skiptv.setVisibility(View.GONE);
                        break;
                    case 2:
                        fab.setVisibility(View.VISIBLE);
                        skiptv.setVisibility(View.GONE);
                        break;
                    case 3:
                        fab.setVisibility(View.VISIBLE);
                        skiptv.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        fab.setVisibility(View.VISIBLE);
                        skiptv.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        fab.setVisibility(View.VISIBLE);
                        skiptv.setVisibility(View.GONE);
                        break;
                    case 6:
                        skiptv.setVisibility(View.GONE);
                        fab.setVisibility(View.GONE);
                        break;
                    default:
                        skiptv.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);
                        break;

                }


            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        backiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //startActivity(new Intent(CreateEventActivity.this, LandingPageActivity.class));
                //overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        skiptv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(indicator.getCurrentStep() + 1, true);
                //skiptv.setVisibility(View.INVISIBLE);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indicator.getCurrentStep();
                indicator.setLabelColor(R.color.colorPrimary);
                Log.d("tag", "Current step------>" + indicator.getCurrentStep() + indicator.getStepCount());

                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String highlight1 = sharedPreferences.getString("highlight1", "");
                String highlight2 = sharedPreferences.getString("highlight2", "");
                String video1 = sharedPreferences.getString("video1", "");
                String coverphoto = sharedPreferences.getString("coverpage", "");
                Log.e("tag", "video------>" + video1);

                if (indicator.getCurrentStep() == 0) {
                    fab.setVisibility(View.VISIBLE);
                    if (!EventTypeFragment.eventdate_et.getText().toString().trim().equalsIgnoreCase("")) {
                        EventTypeFragment.eventdate_et.setError(null);
                        if (!EventTypeFragment.desc_et.getText().toString().trim().equalsIgnoreCase("")) {
                            EventTypeFragment.desc_et.setError(null);
                            pager.setCurrentItem(indicator.getCurrentStep() + 1, true);
                        } else {
                            EventTypeFragment.desc_et.setError("Enter Description");
                        }

                    } else {
                        EventTypeFragment.eventdate_et.setError("Select Event Date");
                    }

                } else if (indicator.getCurrentStep() == 1) {
                    fab.setVisibility(View.VISIBLE);
                    if (!WowtagFragment.fromtime_tv.getText().toString().trim().equalsIgnoreCase("")) {
                        WowtagFragment.fromtime_tv.setError(null);
                        if (!WowtagFragment.totime_tv.getText().toString().trim().equalsIgnoreCase("")) {
                            WowtagFragment.totime_tv.setError(null);
                            pager.setCurrentItem(indicator.getCurrentStep() + 1, true);

                        } else {
                            Toast.makeText(CreateEventActivity.this, "Select To Date", Toast.LENGTH_LONG).show();
                            WowtagFragment.totime_tv.setError("Select To Date");
                        }


                    } else {
                        Toast.makeText(CreateEventActivity.this, "Select From Date", Toast.LENGTH_LONG).show();
                        WowtagFragment.fromtime_tv.setError("Select From Date");
                    }


                } else if (indicator.getCurrentStep() == 2) {
                    fab.setVisibility(View.VISIBLE);
                    if (!EventVenueFragment.venuename.getText().toString().trim().equalsIgnoreCase("")) {
                        EventVenueFragment.venuename.setError(null);
                        if (!EventVenueFragment.address.getText().toString().trim().equalsIgnoreCase("")) {
                            EventVenueFragment.address.setError(null);
                            if (!EventVenueFragment.city.getText().toString().trim().equalsIgnoreCase("")) {
                                EventVenueFragment.city.setError(null);

                                if (!EventVenueFragment.state.getText().toString().trim().equalsIgnoreCase("")) {
                                    EventVenueFragment.state.setError(null);
                                    if (!EventVenueFragment.zipcode.getText().toString().trim().equalsIgnoreCase("")) {
                                        EventVenueFragment.zipcode.setError(null);
                                        pager.setCurrentItem(indicator.getCurrentStep() + 1, true);
                                    } else {
                                        EventVenueFragment.zipcode.setError("Enter Zipcode");
                                        EventVenueFragment.zipcode.requestFocus();
                                    }
                                } else {
                                    EventVenueFragment.state.setError("Enter State");
                                    EventVenueFragment.state.requestFocus();
                                }
                            } else {
                                EventVenueFragment.city.setError("Enter City");
                                EventVenueFragment.city.requestFocus();
                            }

                        } else {
                            EventVenueFragment.address.setError("Enter Address");
                            EventVenueFragment.address.requestFocus();
                        }
                    } else {
                        EventVenueFragment.venuename.setError("Enter Venue Name");
                        EventVenueFragment.venuename.requestFocus();
                    }
                } else if (indicator.getCurrentStep() == 3) {
                    fab.setVisibility(View.VISIBLE);
                    pager.setCurrentItem(indicator.getCurrentStep() + 1, true);
                } else if (indicator.getCurrentStep() == 4) {
                    fab.setVisibility(View.VISIBLE);
                    if (!EventHighlightsFragment.speaker_et.getText().toString().trim().equalsIgnoreCase("")) {
                        EventHighlightsFragment.til_speaker.setError(null);
                        pager.setCurrentItem(indicator.getCurrentStep() + 1, true);
                    } else {
                        EventHighlightsFragment.til_speaker.setError("Enter the Speaker Name");
                    }
                } else if (indicator.getCurrentStep() == 5) {
                    pager.setCurrentItem(indicator.getCurrentStep() + 1, true);
                    fab.setVisibility(View.INVISIBLE);
                } else if (indicator.getCurrentStep() == 6) {
                    pager.setCurrentItem(indicator.getCurrentStep() + 1, true);
                    // fab.setVisibility(View.INVISIBLE);

                } else {
                    pager.setCurrentItem(indicator.getCurrentStep() + 1, true);
                    fab.setVisibility(View.VISIBLE);
                }

            }
        });

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
            ActivityCompat.requestPermissions(CreateEventActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;

        }
    }

}
