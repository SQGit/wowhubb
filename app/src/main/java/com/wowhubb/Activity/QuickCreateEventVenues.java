package com.wowhubb.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suke.widget.SwitchButton;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.wowhubb.Adapter.CustomAdapter;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;
import com.wowhubb.Utils.HttpUtils;
import com.wowhubb.Utils.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by Salman on 01-03-2018.
 */

public class QuickCreateEventVenues extends Activity {

    public static TextInputLayout till_address, till_city, till_state, til_zipcode;
    public static TextInputLayout til_eventname, til_name, til_phone, til_email, til_msg;
    public static String str_start, str_end, selectedVideoFilePath1;
    public static String str_eventtopic, str_category, str_eventday, str_desc, str_city, str_coverpage, str_eventname, str_wowtag, str_startdate, str_enddate, str_runfrom, str_runto;
    LinearLayout previouslv;
    CheckBox onlineevent_cb, selectedgroup_cb, invitedguest_cb, inviteonly_cb;
    Typeface lato;
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;
    String token, eventId;
    Dialog loader_dialog;
    EditText telepasscode_et, webinarlink_et, telephone_et, eventvenue_et, address_et, city_et, state_et, zipcode_et, phone_et, email_et;
    TextView publishtv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_quickvenue);
        final View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(QuickCreateEventVenues.this, v1);
        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(QuickCreateEventVenues.this);
        edit = sharedPrefces.edit();
        token = sharedPrefces.getString("token", "");
        str_eventtopic = sharedPrefces.getString("str_eventtopic", "");
        str_category = sharedPrefces.getString("str_category", "");
        str_eventday = sharedPrefces.getString("str_eventday", "");
        str_desc = sharedPrefces.getString("str_desc", "");
        str_city = sharedPrefces.getString("str_city", "");
        str_coverpage = sharedPrefces.getString("coverpage", "");
        str_eventname = sharedPrefces.getString("str_eventname", "");
        str_wowtag = sharedPrefces.getString("str_wowtag", "");

        lato = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        phone_et = findViewById(R.id.phone_et);
        email_et = findViewById(R.id.email_et);
        previouslv = findViewById(R.id.previouslv);
        lato = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");

        til_eventname = (TextInputLayout) findViewById(R.id.til_eventvenue);
        till_address = (TextInputLayout) findViewById(R.id.til_address);
        till_city = (TextInputLayout) findViewById(R.id.til_city);
        till_state = (TextInputLayout) findViewById(R.id.til_state);
        til_zipcode = (TextInputLayout) findViewById(R.id.til_zipcode);
        til_name = (TextInputLayout) findViewById(R.id.til_name);
        til_phone = (TextInputLayout) findViewById(R.id.til_phone);
        til_email = (TextInputLayout) findViewById(R.id.til_email);
        til_msg = (TextInputLayout) findViewById(R.id.til_msg);

        publishtv = findViewById(R.id.publishtv);
        eventvenue_et = findViewById(R.id.eventvenue_et);
        address_et = findViewById(R.id.address_et);
        city_et = findViewById(R.id.city_et);
        state_et = findViewById(R.id.state_et);
        zipcode_et = findViewById(R.id.zipcode_et);
        final com.suke.widget.SwitchButton switchButton = (com.suke.widget.SwitchButton)
                findViewById(R.id.switch_button);
        com.suke.widget.SwitchButton switchButton1 = (com.suke.widget.SwitchButton)
                findViewById(R.id.switch_button1);
        phone_et.setEnabled(false);
        email_et.setEnabled(false);
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
                if (isChecked) {
                    phone_et.setEnabled(true);
                    // email.setEnabled(true);
                } else {
                    phone_et.setEnabled(false);
                    // email.setEnabled(false);
                }


            }
        });
        switchButton1.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
                if (isChecked) {
                    email_et.setEnabled(true);
                    // email.setEnabled(true);
                } else {
                    email_et.setEnabled(false);
                    // email.setEnabled(false);
                }


            }
        });
        til_eventname.setTypeface(lato);
        till_address.setTypeface(lato);
        till_city.setTypeface(lato);
        till_state.setTypeface(lato);
        til_zipcode.setTypeface(lato);
        til_name.setTypeface(lato);
        til_phone.setTypeface(lato);
        til_email.setTypeface(lato);
        til_msg.setTypeface(lato);

        loader_dialog = new Dialog(QuickCreateEventVenues.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);

        previouslv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        publishtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (!eventvenue_et.getText().toString().trim().equalsIgnoreCase(""))
                {
                    til_eventname.setError(null);
                    if (!eventvenue_et.getText().toString().trim().equalsIgnoreCase(""))
                    {
                        til_eventname.setError(null);

                        if (!eventvenue_et.getText().toString().trim().equalsIgnoreCase(""))
                        {
                            til_eventname.setError(null);



                        }

                        else
                        {
                            SpannableString s = new SpannableString("Enter Venue Name");
                            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            til_eventname.setError(s);
                            eventvenue_et.setFocusable(true);
                            eventvenue_et.requestFocus();

                        }


                    }

                    else
                    {
                        SpannableString s = new SpannableString("Enter Venue Name");
                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        til_eventname.setError(s);
                        eventvenue_et.setFocusable(true);
                        eventvenue_et.requestFocus();

                    }



                }

                else
                {
                    SpannableString s = new SpannableString("Enter Venue Name");
                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    til_eventname.setError(s);
                    eventvenue_et.setFocusable(true);
                    eventvenue_et.requestFocus();

                }

                new quickDetails().execute();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class quickDetails extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                String responseString = null;
                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost("http://104.197.80.225:3010/wow/event/androideventdetails");
                httppost.setHeader("token", token);
                Log.e("tag", "ds0000000000----------------------->:" + token);
                httppost.setHeader("eventtype", "quick_event");
                httppost.setHeader("eventtypeint", "" + 5);
                httppost.setHeader("eventtitle", str_eventtopic);
                httppost.setHeader("eventname", str_eventname);
                httppost.setHeader("eventcategory", str_category);
                httppost.setHeader("eventstartdate", "");
                Log.e("tag", "ds33---------------------->:" + str_wowtag);
                httppost.setHeader("eventenddate", "");
                Log.e("tag", "ds444----------------------->:" + selectedVideoFilePath1);
                httppost.setHeader("eventtimezone", str_city);
                Log.e("tag", "ds55555555----------------------->:" + selectedVideoFilePath1);
                httppost.setHeader("runtimefrom", "");
                httppost.setHeader("eventdescription", str_desc);
                httppost.setHeader("runtimeto", "");

                HttpResponse response = null;
                HttpEntity r_entity = null;

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                if (!str_wowtag.equals("")) {

                    entity.addPart("wowtagvideo", new FileBody(new File(str_wowtag), "video/mp4"));
                }
                if (!str_coverpage.equals("")) {
                    entity.addPart("coverpage", new FileBody(new File(str_coverpage), "image/jpeg"));
                }

                httppost.setEntity(entity);


                try {
                    try {
                        response = httpclient.execute(httppost);
                    } catch (Exception e) {
                        Log.e("tag", "ds:" + e.toString());
                    }

                    try {
                        r_entity = response.getEntity();
                    } catch (Exception e) {
                        Log.e("tag", "dsa:" + e.toString());
                    }

                    int statusCode = response.getStatusLine().getStatusCode();
                    Log.e("tag", response.getStatusLine().toString());
                    if (statusCode == 200) {
                        responseString = EntityUtils.toString(r_entity);
                        Log.e("tag", "rssss" + responseString);
                        // return success;

                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                        Log.e("tag3", responseString);
                    }
                } catch (ClientProtocolException e) {
                    responseString = e.toString();
                    Log.e("tag44", responseString);
                } catch (IOException e) {
                    responseString = e.toString();
                    Log.e("tag45", responseString);
                }
                return responseString;
            } catch (Exception e) {
                Log.e("tag_InputStream0", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loader_dialog.dismiss();
            Log.e("tag", "-------------------------->>>>" + s);

            if (s != null) {
                if (s.length() > 0) {
                    try {
                        JSONObject jo = new JSONObject(s.toString());
                        String success = jo.getString("success");
                        if (success.equals("true")) {

                            JSONObject msg = jo.getJSONObject("message");
                            eventId = msg.getString("_id");
                            Log.e("tag", "eventIdddd----------------->>>>>>>" + eventId);

                            if (Utils.Operations.isOnline(QuickCreateEventVenues.this)) {
                                new eventvenue().execute();
                            } else {
                            }


                        } else {


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } else {

            }
        }

    }

    public class eventvenue extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("eventid", eventId);
                jsonObject.accumulate("inviteonlyevent", "true");
                jsonObject.accumulate("onlineevent", "true");
                //   jsonObject.accumulate("eventvenueguestshare", "fggf");
                //   jsonObject.accumulate("eventvenueaddressvisibility", "fgg");


                JSONArray jArray = new JSONArray();// /ItemDetail jsonArray

                if (eventvenue_et.getText().toString().length() > 0) {


                    JSONObject jGroup = new JSONObject();// /sub Object

                    try {
                        // jGroup.put("eventvenuenumber", EventVenueFragment.listBeneficiary.get(i).getId());
                        jGroup.put("eventvenuename", eventvenue_et.getText().toString());
                        jGroup.put("eventvenueaddress1", address_et.getText().toString());
                        jGroup.put("eventvenuecity", city_et.getText().toString());
                        jGroup.put("eventvenuezipcode", zipcode_et.getText().toString());

                        jArray.put(jGroup);

                        // /itemDetail Name is JsonArray Name
                        jsonObject.put("eventvenue", jArray);
                        //return jResult;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                }


                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/androideventvenue", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);
            loader_dialog.dismiss();
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        Toast.makeText(QuickCreateEventVenues.this, "Quick Event Created Successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(QuickCreateEventVenues.this, EventFeedDashboard.class));
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        finish();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                }
            } else {

            }

        }

    }

    public class onlineevent extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("eventid", eventId);
               /* if (!gift_url.matches("")) {
                    Log.e("tag", "gift urllllll");
                    jsonObject.accumulate("onlineeventname", "true");
                }*/
                if (!webinarlink_et.getText().toString().trim().matches("")) {
                    Log.e("tag", "gift urllllll");
                    jsonObject.accumulate("webinarlink", webinarlink_et.getText().toString().trim());
                }
                if (!telephone_et.getText().toString().trim().matches("")) {
                    Log.e("tag", "gift urllllll");
                    jsonObject.accumulate("teleconferencephone", telephone_et.getText().toString().trim());
                }
                if (!telepasscode_et.getText().toString().trim().matches("")) {
                    Log.e("tag", "gift urllllll");
                    jsonObject.accumulate("teleconferencepassword", telepasscode_et.getText().toString().trim());
                }


                // eventid, onlineeventname, webinarlink, teleconferencephone,teleconferencepassword

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/androidonlineevent", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);
            loader_dialog.dismiss();
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        if (Utils.Operations.isOnline(QuickCreateEventVenues.this)) {
                            // new EventContactFragment.eventhighlight().execute();
                        } else {
                            CreateEventActivity.snackbar.show();
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                }
            } else {

            }

        }

    }


}


