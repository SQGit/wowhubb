package com.wowhubb.EventServiceProvider.Activity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.wowhubb.Adapter.CustomAdapter;
import com.wowhubb.EventServiceProvider.Adapter.EventListDetailsAdapter;
import com.wowhubb.EventServiceProvider.Model.EventListDetailsModel;
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
    String token;
    ArrayList<HashMap<String, String>> event_data;
    RecyclerView recycler_event_service;
    EventListDetailsAdapter eventListDetailsAdapter;
    private List<EventListDetailsModel> eventListDetailModel;
    ImageView img_back,filter_icon;
    LinearLayout lnr_filter_icon;
    public static String str_category,str_country;;
    Dialog dialog;

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
        Intent i=getIntent();
        String str_head=i.getStringExtra("provider");


        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(EventListDetailsNewActivity.this);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");
        str_head=sharedPreferences.getString("provider","");

        filter_icon=(ImageView)findViewById(R.id.filter_icon);
        txt_provider_head=(TextView)findViewById(R.id.txt_provider_head);
        av_loader = (AVLoadingIndicatorView) findViewById(R.id.avi);
        recycler_event_service = (RecyclerView)findViewById(R.id.recycler_view);
        img_back=(ImageView)findViewById(R.id.img_back);
        lnr_filter_icon=(LinearLayout)findViewById(R.id.lnr_filter_icon);

        eventListDetailModel = new ArrayList<>();

        if(str_head!=null)
        {
            txt_provider_head.setText(str_head);
        }


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
                Toast.makeText(getApplicationContext(),"Filtering!",Toast.LENGTH_LONG).show();

                Intent i=new Intent(getApplicationContext(),FilterEventProvider.class);
                startActivity(i);
                /*startActivity(new Intent(EventListDetailsNewActivity.this, FilterEventProvider.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                finish();*/

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
                                eventData.shopDiscount=jsonObject.getString("discount");
                                eventData.shopContact=jsonObject.getString("phone");
                                eventData.shopLogo=jsonObject.getString("shoplogo");
                                eventData.shopLink=jsonObject.getString("url");
                                eventData.shopCategory=jsonObject.getString("city");
                                eventData.shopLatitude=jsonObject.getDouble("latitude");
                                eventData.shopLongitude=jsonObject.getDouble("longitude");
                                eventData.shopCountry=jsonObject.getString("country");
                                eventData.shopOpenTime=jsonObject.getString("desciption");



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



