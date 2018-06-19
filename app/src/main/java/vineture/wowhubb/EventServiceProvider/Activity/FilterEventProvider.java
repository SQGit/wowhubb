package vineture.wowhubb.EventServiceProvider.Activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import vineture.wowhubb.Adapter.CustomAdapter;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.R;

import lib.kingja.switchbutton.SwitchMultiButton;

/**
 * Created by Guna on 22-03-2018.
 */

public class FilterEventProvider extends AppCompatActivity {
    LinearLayout close,lnr_clear_filter,lnr_miles,lnr_kilometer;
    TextInputLayout til_eventcity,til_spincountry,til_event_service_vendors;
    MaterialBetterSpinner materialDesignSpinner;
    TextView txt_head;
    EditText eventtopic_et;
    Button filter_btn;
    String str_country,str_vendor,token;
    Typeface segoeui;
    CustomAdapter arrayAdapter;
    SharedPreferences.Editor editor;
    SwitchMultiButton switchm;

    String[] SPINNERLIST = {"US","Nigeria","Brazil"};


    String[] VENDORS = {"Catering Services", "Cakes", "Wines", "BBQ Indoor & outdoor", "Music & DJ Services", "Event Organizer", "Beauty Services", "Photography & Video Services", "Decor & Party Rentals", "Floral",
            "Party Cleaning Services", "Limo & Car Rental Services", "Dress & Jewellery", "MC's & Comedians", "Event Guest Speakers", "Event Technology Support", "Event Gifting Souveniers",
            "France", "French Guiana", "Gabon", "Gambia", "Greece", "Greenland", "Hong Kong", "Hungary", "Iceland", "India", "Iraq"};




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventschedule_filter);
        getSupportActionBar().hide();
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(FilterEventProvider.this, v1);

        segoeui = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/segoeui.ttf");
        close = findViewById(R.id.closelnr);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        lnr_clear_filter=findViewById(R.id.lnr_clear_filter);
        til_eventcity=findViewById(R.id.til_eventcity);
        eventtopic_et=findViewById(R.id.eventtopic_et);
        lnr_miles=findViewById(R.id.lnr_miles);
        lnr_kilometer=findViewById(R.id.lnr_kms);
        filter_btn=findViewById(R.id.filter_btn);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(FilterEventProvider.this);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");

        lnr_miles.setVisibility(View.VISIBLE);
        lnr_kilometer.setVisibility(View.GONE);


        //Event Country Spinner:
        materialDesignSpinner = (MaterialBetterSpinner) findViewById(R.id.country_spn);
        til_spincountry=findViewById(R.id.til_spincountry);
        til_spincountry.setTypeface(segoeui);
        View view1 = materialDesignSpinner.getRootView();
        FontsOverride.overrideFonts(FilterEventProvider.this, view1);
        switchm=findViewById(R.id.switchm);

        til_eventcity.setTypeface(segoeui);
        eventtopic_et.setTypeface(segoeui);



        switchm.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
               String str_switch=tabText;
               if(str_switch.equals("Miles"))
               {
                   lnr_miles.setVisibility(View.VISIBLE);
                   lnr_kilometer.setVisibility(View.GONE);
               }
               else
               {
                   lnr_miles.setVisibility(View.GONE);
                   lnr_kilometer.setVisibility(View.VISIBLE);
               }
            }
        });


        arrayAdapter = new CustomAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, SPINNERLIST) {
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
        materialDesignSpinner.setAdapter(arrayAdapter);
        materialDesignSpinner.setTypeface(segoeui);


        materialDesignSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FontsOverride.overrideFonts(getApplicationContext(), view);
                str_country = adapterView.getItemAtPosition(i).toString();
            }
        });


        //Event Vendor Spinner:
        MaterialBetterSpinner materialDesignSpinner1 = (MaterialBetterSpinner) findViewById(R.id.event_service_vendors_spn);
        til_event_service_vendors=findViewById(R.id.til_event_service_vendors);
        til_event_service_vendors.setTypeface(segoeui);
        View view2 = materialDesignSpinner1.getRootView();
        FontsOverride.overrideFonts(FilterEventProvider.this, view2);


        final CustomAdapter arrayAdapter1 = new CustomAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, VENDORS) {
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
        materialDesignSpinner1.setAdapter(arrayAdapter1);
        materialDesignSpinner1.setTypeface(segoeui);


        materialDesignSpinner1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FontsOverride.overrideFonts(getApplicationContext(), view);
                str_vendor = adapterView.getItemAtPosition(i).toString();
            }
        });
    }
}
