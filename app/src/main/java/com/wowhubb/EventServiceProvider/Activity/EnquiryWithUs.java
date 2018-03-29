package com.wowhubb.EventServiceProvider.Activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.wowhubb.Adapter.CustomAdapter;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

/**
 * Created by Guna on 22-03-2018.
 */

public class EnquiryWithUs extends AppCompatActivity {
    MaterialBetterSpinner event_spn,spn_eventdays;
    TextInputLayout til_spineventdays,til_spincategory,til_eventtimezone,til_attendees,til_comments,til_starttime,til_fname,til_endtime
            ,til_lname,til_email,til_phone,til_addproduct;
    EditText edt_attendees,edt_comments,edt_starttime,edt_fname,edt_endtime,edt_lname,edt_email,edt_phone,edt_product;
    LinearLayout closelr;
    TextView meetingtime,enquiry,submittv;
    String str_days,str_category;
    String[] EVENTLIST = {"Anniversary", "Baby Shower", "Birthday Party", "Holiday Party", "Wedding", "Baby Naming", "Graduation Party", "Team Outing", "Photo/File Shoot Event", "Bridal Shower Event", "Meeting", "Others", "Group Meetings"};
    String[] DAYSLIST = {"Day 1","Day 2","Day 3"};
    Typeface segoeui;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_enquiry);
        getSupportActionBar().hide();
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(EnquiryWithUs.this, v1);

        segoeui = Typeface.createFromAsset(getAssets(), "fonts/segoeui.ttf");

        event_spn = (MaterialBetterSpinner)findViewById(R.id.event_spn);
        spn_eventdays = (MaterialBetterSpinner)findViewById(R.id.spn_eventdays);
        til_spineventdays=findViewById(R.id.til_spineventdays);
        til_spincategory=findViewById(R.id.til_spincategory);
        til_eventtimezone=findViewById(R.id.til_eventtimezone);
        til_attendees=findViewById(R.id.til_attendees);
        til_comments=findViewById(R.id.til_comments);
        edt_attendees=findViewById(R.id.edt_attendees);
        edt_comments=findViewById(R.id.edt_comments);
        closelr=findViewById(R.id.closelr);
        meetingtime=findViewById(R.id.meetingtime);
        til_starttime=findViewById(R.id.til_starttime);
        edt_starttime=findViewById(R.id.edt_starttime);
        til_fname=findViewById(R.id.til_fname);
        edt_fname=findViewById(R.id.edt_fname);
        til_endtime=findViewById(R.id.til_endtime);
        edt_endtime=findViewById(R.id.edt_endtime);
        til_lname=findViewById(R.id.til_lname);
        edt_lname=findViewById(R.id.edt_lname);
        til_email=findViewById(R.id.til_email);
        edt_email=findViewById(R.id.edt_email);
        til_phone=findViewById(R.id.til_phone);
        edt_phone=findViewById(R.id.edt_phone);
        til_addproduct=findViewById(R.id.til_addproduct);
        edt_product=findViewById(R.id.edt_product);
        enquiry=findViewById(R.id.enquiry);
        submittv= findViewById(R.id.submittv);


        til_spineventdays.setTypeface(segoeui);
        til_spincategory.setTypeface(segoeui);
        til_eventtimezone.setTypeface(segoeui);
        til_attendees.setTypeface(segoeui);
        til_comments.setTypeface(segoeui);
        til_starttime.setTypeface(segoeui);
        til_fname.setTypeface(segoeui);
        til_endtime.setTypeface(segoeui);
        til_lname.setTypeface(segoeui);
        til_email.setTypeface(segoeui);
        til_phone.setTypeface(segoeui);
        til_addproduct.setTypeface(segoeui);



        final CustomAdapter eventdayAdapter = new CustomAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, EVENTLIST) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return true;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTypeface(segoeui);
                tv.setTextSize(9);
                tv.setPadding(30, 55, 10, 25);
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(15);
                tv.setPadding(10, 20, 0, 20);
                tv.setTypeface(segoeui);
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        event_spn.setAdapter(eventdayAdapter);

        event_spn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FontsOverride.overrideFonts(getApplicationContext(), view);
                str_category = adapterView.getItemAtPosition(i).toString();
                event_spn.requestFocus();
            }
        });


        final CustomAdapter eventdayAdapter1 = new CustomAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, DAYSLIST) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return true;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTypeface(segoeui);
                tv.setTextSize(9);
                tv.setPadding(30, 55, 10, 25);
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(15);
                tv.setPadding(10, 20, 0, 20);
                tv.setTypeface(segoeui);
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spn_eventdays.setAdapter(eventdayAdapter1);

        spn_eventdays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FontsOverride.overrideFonts(getApplicationContext(), view);
                str_days = adapterView.getItemAtPosition(i).toString();
            }
        });


        closelr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        submittv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Under Development",Toast.LENGTH_LONG).show();
            }
        });
    }
}
