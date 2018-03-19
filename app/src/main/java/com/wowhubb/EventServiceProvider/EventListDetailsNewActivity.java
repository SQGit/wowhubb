package com.wowhubb.EventServiceProvider;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.whygraphics.multilineradiogroup.MultiLineRadioGroup;
import com.wowhubb.Adapter.CustomAdapter;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;
import com.wowhubb.Utils.Config;
import com.wowhubb.Utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lib.kingja.switchbutton.SwitchMultiButton;

/**
 * Created by Guna on 27-11-2017.
 */

public class EventListDetailsNewActivity extends AppCompatActivity {
    TextView backiv_event,txt_provider_head;
    AVLoadingIndicatorView av_loader;
    SharedPreferences.Editor editor;
    String token,evalue;
    ArrayList<HashMap<String, String>> event_data;
    RecyclerView recycler_event_service;
    EventListDetailsAdapter eventListDetailsAdapter;
    private List<EventListDetailsModel> eventListDetailModel;
    ImageView img_back,filter_icon;
    LinearLayout lnr_filter_icon,lnr_clear_filter;
    public static String str_provider_name,str_category,str_address,str_time,str_contact,str_link,str_logo,str_rating,str_country,str_miles,str_vendor;
    public static Double str_provider_latitude,str_provider_longitude;
    Dialog dialog;
    Typeface lato;
    CustomAdapter arrayAdapter;

    MaterialBetterSpinner materialDesignSpinner;

    String[] SPINNERLIST = {"US","Nigeria","Brazil"};


    String[] VENDORS = {"Catering Services", "Cakes", "Wines", "BBQ Indoor & outdoor", "Music & DJ Services", "Event Organizer", "Beauty Services", "Photography & Video Services", "Decor & Party Rentals", "Floral",
            "Party Cleaning Services", "Limo & Car Rental Services", "Dress & Jewellery", "MC's & Comedians", "Event Guest Speakers", "Event Technology Support", "Event Gifting Souveniers",
            "France", "French Guiana", "Gabon", "Gambia", "Greece", "Greenland", "Hong Kong", "Hungary", "Iceland", "India", "Iraq"};


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_details_new);
        getSupportActionBar().hide();
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(EventListDetailsNewActivity.this, v1);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(EventListDetailsNewActivity.this);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");

        filter_icon=(ImageView)findViewById(R.id.filter_icon);
        txt_provider_head=(TextView)findViewById(R.id.txt_provider_head);
        av_loader = (AVLoadingIndicatorView) findViewById(R.id.avi);
        recycler_event_service = (RecyclerView)findViewById(R.id.recycler_view);
        img_back=(ImageView)findViewById(R.id.img_back);
        lnr_filter_icon=(LinearLayout)findViewById(R.id.lnr_filter_icon);

        eventListDetailModel = new ArrayList<>();

        txt_provider_head.setText(EventProviderCategotyActivity.txt_provider_head.getText().toString());

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(EventListDetailsNewActivity.this, EventServiceProviderActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                finish();

            }
        });



        lnr_filter_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new Dialog(EventListDetailsNewActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_eventschedule_filter);
                View v1 = dialog.getWindow().getDecorView().getRootView();
                LinearLayout close = dialog.findViewById(R.id.closeiv);
                TextView txt_head = dialog.findViewById(R.id.txt_head);
                FontsOverride.overrideFonts(dialog.getContext(), v1);
                lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/segoeui.ttf");

                final TextInputLayout til_spincountry,til_eventcity,til_event_service_vendors;
                final EditText eventtopic_et;
                Button filter_btn;
                final LinearLayout lnr_miles,lnr_kilometer;


                lnr_clear_filter=dialog.findViewById(R.id.lnr_clear_filter);
                til_eventcity=dialog.findViewById(R.id.til_eventcity);
                eventtopic_et=dialog.findViewById(R.id.eventtopic_et);
                lnr_miles=dialog.findViewById(R.id.lnr_miles);
                lnr_kilometer=dialog.findViewById(R.id.lnr_kilometer);
                filter_btn=dialog.findViewById(R.id.filter_btn);
                til_eventcity.setTypeface(lato);
                eventtopic_et.setTypeface(lato);
                filter_btn.setTypeface(lato);
                txt_head.setTypeface(lato);
                lnr_miles.setVisibility(View.VISIBLE);
                lnr_kilometer.setVisibility(View.GONE);
                dialog.show();

                lnr_clear_filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        eventtopic_et.setText("");
                        //materialDesignSpinner.setSelection(0);

                    }
                });

                filter_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(str_country !=null) {
                                new filterAsync().execute();
                                dialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Please Select Country",Toast.LENGTH_LONG).show();
                        }
                    }
                });


                //call vendors name service
                //  new eventServiceProviderName().execute();

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


               SwitchMultiButton mSwitchMultiButton = (SwitchMultiButton) dialog.findViewById(R.id.switchmultibutton);
                mSwitchMultiButton.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
                    @Override
                    public void onSwitch(int position, String tabText) {
                        String switch_case=tabText;
                        if(switch_case.equals("Miles"))
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


                MultiLineRadioGroup mMultiLineRadioGroup = (MultiLineRadioGroup)dialog. findViewById(R.id.main_activity_multi_line_radio_group);
                mMultiLineRadioGroup.setOnCheckedChangeListener(new MultiLineRadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(ViewGroup group, RadioButton button) {
                        Toast.makeText(EventListDetailsNewActivity.this,
                                button.getText() + " was clicked",
                                Toast.LENGTH_SHORT).show();
                        str_miles=button.getText().toString();


                    }
                });



                //Event Country Spinner:
                materialDesignSpinner = (MaterialBetterSpinner) dialog.findViewById(R.id.country_spn);
                til_spincountry=dialog.findViewById(R.id.til_spincountry);
                til_spincountry.setTypeface(lato);
                View view1 = materialDesignSpinner.getRootView();
                FontsOverride.overrideFonts(EventListDetailsNewActivity.this, view1);


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
                        tv.setTypeface(lato);
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
                        tv.setTypeface(lato);
                        if (position == 0) {
                            tv.setTextColor(Color.BLACK);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                materialDesignSpinner.setAdapter(arrayAdapter);
                materialDesignSpinner.setTypeface(lato);


                materialDesignSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        FontsOverride.overrideFonts(getApplicationContext(), view);
                        str_country = adapterView.getItemAtPosition(i).toString();
                    }
                });


                //Event Vendor Spinner:
                MaterialBetterSpinner materialDesignSpinner1 = (MaterialBetterSpinner) dialog.findViewById(R.id.event_service_vendors_spn);
                til_event_service_vendors=dialog.findViewById(R.id.til_event_service_vendors);
                til_event_service_vendors.setTypeface(lato);
                View view2 = materialDesignSpinner1.getRootView();
                FontsOverride.overrideFonts(EventListDetailsNewActivity.this, view2);


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
                        tv.setTypeface(lato);
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
                        tv.setTypeface(lato);
                        if (position == 0) {
                            tv.setTextColor(Color.BLACK);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                materialDesignSpinner1.setAdapter(arrayAdapter1);
                materialDesignSpinner1.setTypeface(lato);


                materialDesignSpinner1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        FontsOverride.overrideFonts(getApplicationContext(), view);
                        str_vendor = adapterView.getItemAtPosition(i).toString();
                    }
                });
            }
        });

        new eventServiceProviderAsync().execute();
    }

//Async Task All List:
    class eventServiceProviderAsync extends AsyncTask<String,Void,String>{

           @Override
    protected void onPreExecute() {
        super.onPreExecute();
        av_loader.setVisibility(View.VISIBLE);
    }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_EVENT_SERVICE_LIST, json,token);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonstr) {
            av_loader.setVisibility(View.GONE);
            super.onPostExecute(jsonstr);

            if (jsonstr.equals("")) {
                Toast.makeText(getApplicationContext(),"Check Network Connection",Toast.LENGTH_LONG).show();
            } else {

                try {
                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("success");
                    if(status.equals("true")) {

                        JSONArray event_service = jo.getJSONArray("message");

                        eventListDetailModel.clear();
                        if (event_service.length() > 0) {
                            for (int i1 = 0; i1 < event_service.length(); i1++) {

                                HashMap<String, String> map = new HashMap<String, String>();
                                EventListDetailsModel eventData = new EventListDetailsModel();
                                JSONObject jsonObject = event_service.getJSONObject(i1);
                                eventData.shopName=jsonObject.getString("shopname");
                                eventData.shopRating=jsonObject.getString("rating");
                                eventData.shopAddress=jsonObject.getString("address");
                                eventData.shopOpenTime=jsonObject.getString("openingtime");
                                eventData.shopContact=jsonObject.getString("phone");
                                eventData.shopLogo=jsonObject.getString("shoplogo");
                                eventData.shopLink=jsonObject.getString("url");
                                eventData.shopCategory=jsonObject.getString("category");
                                eventData.shopLatitude=jsonObject.getDouble("latitude");
                                eventData.shopLongitude=jsonObject.getDouble("longitude");
                                eventData.shopCountry=jsonObject.getString("country");
                                eventListDetailModel.add(eventData);
                            }

                            // Setup and Handover data to recyclerview
                            eventListDetailsAdapter = new EventListDetailsAdapter(EventListDetailsNewActivity.this, eventListDetailModel);
                            recycler_event_service.setAdapter(eventListDetailsAdapter);
                            eventListDetailsAdapter.notifyDataSetChanged();
                            recycler_event_service.setLayoutManager(new LinearLayoutManager(EventListDetailsNewActivity.this));

                        } else {
                            //data empty
                        }

                    }
                    else
                    {

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }




//AsyncTask filter
private class filterAsync extends AsyncTask<String,Void,String>{

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //av_loader.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... strings) {
        String json = "", jsonStr = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("country",str_country);
            json = jsonObject.toString();
            return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_EVENT_FILTER_LIST, json,token);
        } catch (Exception e) {
            Log.e("InputStream", e.getLocalizedMessage());
        }
        return null;
    }


    @Override
    protected void onPostExecute(String jsonstr) {
        av_loader.setVisibility(View.GONE);
        super.onPostExecute(jsonstr);
        dialog.dismiss();

        if (jsonstr.equals("")) {
            Toast.makeText(getApplicationContext(),"Check Network Connection",Toast.LENGTH_LONG).show();
        } else {

            try {

                JSONObject jo = new JSONObject(jsonstr);
                String status = jo.getString("success");
                if(status.equals("true")) {

                    JSONArray event_service = jo.getJSONArray("message");

                    eventListDetailModel.clear();
                    if (event_service.length() > 0) {
                        for (int i1 = 0; i1 < event_service.length(); i1++) {

                            HashMap<String, String> map = new HashMap<String, String>();
                            EventListDetailsModel eventData = new EventListDetailsModel();
                            JSONObject jsonObject = event_service.getJSONObject(i1);
                            eventData.shopName=jsonObject.getString("shopname");
                            eventData.shopRating=jsonObject.getString("rating");
                            eventData.shopAddress=jsonObject.getString("address");
                            eventData.shopOpenTime=jsonObject.getString("openingtime");
                            eventData.shopContact=jsonObject.getString("phone");
                            eventData.shopLogo=jsonObject.getString("shoplogo");
                            eventData.shopLink=jsonObject.getString("url");
                            eventData.shopCategory=jsonObject.getString("category");
                            eventData.shopLatitude=jsonObject.getDouble("latitude");
                            eventData.shopLongitude=jsonObject.getDouble("longitude");
                            eventData.shopCountry=jsonObject.getString("country");
                            eventListDetailModel.add(eventData);

                        }

                        // Setup and Handover data to recyclerview
                        eventListDetailsAdapter = new EventListDetailsAdapter(EventListDetailsNewActivity.this, eventListDetailModel);
                        recycler_event_service.setAdapter(eventListDetailsAdapter);
                        eventListDetailsAdapter.notifyDataSetChanged();
                        recycler_event_service.setLayoutManager(new LinearLayoutManager(EventListDetailsNewActivity.this));



                    } else {
                        //data empty
                    }

                }
                else
                {

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}


    @Override
    public void onBackPressed() {
        startActivity(new Intent(EventListDetailsNewActivity.this, EventServiceProviderActivity.class));
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        finish();
    }
}



