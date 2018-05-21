package com.wowhubb.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.suke.widget.SwitchButton;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.Groups.Group;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Salman on 01-03-2018.
 */

public class QuickCreateEventVenues extends Activity {

    public static TextInputLayout till_address, till_city, till_state, til_zipcode;
    public static TextInputLayout til_eventname, til_name, til_phone, til_email, til_msg;
    public static String str_start, str_end, selectedVideoFilePath1;
    public static String str_eventtopic, str_category, str_eventday, str_desc, str_city, str_coverpage, str_eventname, str_wowtag, str_startdate, str_enddate, str_runfrom, str_runto;
    public RadioGroup publishgroup_gp;
    LinearLayout previouslv, eventvenue_lv;
    CheckBox registerrsvp_cb, inviteonlyevent_cb;
    RadioButton sharenetwork_rb, specificgroup_rb, privateevent_rb;
    Typeface lato;
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;
    String token, eventId, str_email, str_phone, onlinestatus, str_registerrsvp;
    Dialog loader_dialog, dialog, askinvite_dialog;
    EditText telepasscode_et, webinarlink_et, telephone_et, eventvenue_et, address_et, city_et, state_et, zipcode_et, phone_et, email_et;
    TextView publishtv, grouppublishtv;
    ListView listview;
    SparseBooleanArray sparseBooleanArray;
    private List<Group> groups;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_quickvenue);
        groups = new ArrayList<>();

        final View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(QuickCreateEventVenues.this, v1);
        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(QuickCreateEventVenues.this);
        edit = sharedPrefces.edit();
        token = sharedPrefces.getString("token", "");
        str_email = sharedPrefces.getString("str_email", "");
        str_phone = sharedPrefces.getString("str_phone", "");
        onlinestatus = sharedPrefces.getString("onlinestatus", "");


        str_eventtopic = sharedPrefces.getString("str_eventtopic", "");
        str_category = sharedPrefces.getString("str_category", "");
        str_eventday = sharedPrefces.getString("str_eventday", "");
        str_desc = sharedPrefces.getString("str_desc", "");
        str_city = sharedPrefces.getString("str_city", "");
        str_coverpage = sharedPrefces.getString("coverpage", "");
        str_eventname = sharedPrefces.getString("str_eventname", "");
        str_wowtag = sharedPrefces.getString("str_wowtag", "");
        str_enddate = sharedPrefces.getString("str_enddate", "");
        str_startdate = sharedPrefces.getString("str_startdate", "");

        lato = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        phone_et = findViewById(R.id.phone_et);
        email_et = findViewById(R.id.email_et);
        previouslv = findViewById(R.id.previouslv);
        lato = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        registerrsvp_cb = findViewById(R.id.registerrsvp_cb);
        inviteonlyevent_cb = findViewById(R.id.invitedonly_event);
        til_eventname = (TextInputLayout) findViewById(R.id.til_eventvenue);
        till_address = (TextInputLayout) findViewById(R.id.til_address);
        till_city = (TextInputLayout) findViewById(R.id.til_city);
        till_state = (TextInputLayout) findViewById(R.id.til_state);
        til_zipcode = (TextInputLayout) findViewById(R.id.til_zipcode);
        til_name = (TextInputLayout) findViewById(R.id.til_name);
        til_phone = (TextInputLayout) findViewById(R.id.til_phone);
        til_email = (TextInputLayout) findViewById(R.id.til_email);
        til_msg = (TextInputLayout) findViewById(R.id.til_msg);
        eventvenue_lv = findViewById(R.id.eventvenue_lv);
        publishgroup_gp = findViewById(R.id.group_rb);


        publishtv = findViewById(R.id.publishtv);
        eventvenue_et = findViewById(R.id.eventvenue_et);
        address_et = findViewById(R.id.address_et);
        city_et = findViewById(R.id.city_et);
        state_et = findViewById(R.id.state_et);
        zipcode_et = findViewById(R.id.zipcode_et);
        final com.suke.widget.SwitchButton phone_sb = (com.suke.widget.SwitchButton)
                findViewById(R.id.switch_button);
        com.suke.widget.SwitchButton mail_sb = (com.suke.widget.SwitchButton)
                findViewById(R.id.switch_button1);

        if (str_email != null) {
            email_et.setText(str_email);
        }
        if (str_phone != null) {
            phone_et.setText(str_phone);
        }
        if (onlinestatus != null) {
            if (onlinestatus.equals("true")) {
                eventvenue_lv.setVisibility(View.GONE);
            } else {
                eventvenue_lv.setVisibility(View.VISIBLE);

            }
        }

        phone_et.setEnabled(false);
        email_et.setEnabled(false);


        registerrsvp_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    str_registerrsvp = "on";
                } else {
                    str_registerrsvp = "off";
                }
            }
        });

        inviteonlyevent_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                  //  str_registerrsvp = "on";
                } else {
                  //  str_registerrsvp = "off";
                }
            }
        });

        phone_sb.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
                if (isChecked) {
                    phone_et.setEnabled(true);
                } else {
                    phone_et.setEnabled(false);

                }
            }
        });

        mail_sb.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
                if (isChecked) {
                    email_et.setEnabled(true);
                } else {
                    email_et.setEnabled(false);
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

        //-------------------Loader Dialog---------------------------//

        loader_dialog = new Dialog(QuickCreateEventVenues.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);

        dialog = new Dialog(QuickCreateEventVenues.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_wowhubb_networklist);


        ImageView closeiv = dialog.findViewById(R.id.closeiv);
        grouppublishtv = (TextView) dialog.findViewById(R.id.publish_tv);
        closeiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        View view1 = dialog.getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(dialog.getContext(), view1);

        listview = (ListView) dialog.findViewById(R.id.listView);
        new fetchGroup().execute();


        previouslv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        grouppublishtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();


            }
        });
        publishtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("tag", "onlinestatus------" + onlinestatus);

                if (!onlinestatus.equals("true")) {

                    if (!eventvenue_et.getText().toString().trim().equalsIgnoreCase("")) {
                        til_eventname.setError(null);

                        if (!address_et.getText().toString().trim().equalsIgnoreCase("")) {
                            till_address.setError(null);
                            if (!city_et.getText().toString().trim().equalsIgnoreCase("")) {
                                till_city.setError(null);
                                if (!state_et.getText().toString().trim().equalsIgnoreCase("")) {
                                    till_state.setError(null);
                                    if (!zipcode_et.getText().toString().trim().equalsIgnoreCase("")) {
                                        til_zipcode.setError(null);
                                        if(phone_et.getText().toString().trim().length()>12)
                                        {
                                            til_phone.setError(null);
                                            if (!(!android.util.Patterns.EMAIL_ADDRESS.matcher(email_et.getText().toString()).matches())) {
                                                til_email.setError(null);
                                                new quickDetails().execute();
                                            }
                                            else {
                                                SpannableString s = new SpannableString("Invalid Email");
                                                s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                til_email.setError(s);
                                            }


                                        }
                                        else {
                                            SpannableString s = new SpannableString("Invalid Phone No");
                                            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            til_phone.setError(s);
                                            phone_et.setFocusable(true);
                                            phone_et.requestFocus();

                                        }


                                    } else {
                                        SpannableString s = new SpannableString("Enter Zipcode");
                                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        til_zipcode.setError(s);
                                        zipcode_et.setFocusable(true);
                                        zipcode_et.requestFocus();

                                    }


                                } else {
                                    SpannableString s = new SpannableString("Enter State");
                                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    till_state.setError(s);
                                    state_et.setFocusable(true);
                                    state_et.requestFocus();

                                }


                            } else {
                                SpannableString s = new SpannableString("Enter City");
                                s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                till_city.setError(s);
                                city_et.setFocusable(true);
                                city_et.requestFocus();

                            }


                        } else {
                            SpannableString s = new SpannableString("Enter Address");
                            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            till_address.setError(s);
                            address_et.setFocusable(true);
                            address_et.requestFocus();

                        }


                    } else {
                        SpannableString s = new SpannableString("Enter Venue Name");
                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        til_eventname.setError(s);
                        eventvenue_et.setFocusable(true);
                        eventvenue_et.requestFocus();

                    }
                } else {
                    new quickDetails().execute();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onRadioButtonClicked(View v) {
        specificgroup_rb = findViewById(R.id.specificgroup_rb);
        sharenetwork_rb = findViewById(R.id.invitedguest_cb);

        boolean checked = ((RadioButton) v).isChecked();

        //now check which radio button is selected
        //android switch statement
        switch (v.getId()) {

            case R.id.specificgroup_rb:
                if (checked)
                    dialog.show();
                else
                    dialog.dismiss();
                break;

            case R.id.invitedguest_cb:

                    break;


        }
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
                httppost.setHeader("eventstartdate", str_startdate);
                Log.e("tag", "ds33---------------------->:" + str_startdate);
                httppost.setHeader("eventenddate", str_enddate);
                httppost.setHeader("eventtimezone", str_city);
                httppost.setHeader("runtimefrom", "");
                httppost.setHeader("eventdescription", str_desc);
                httppost.setHeader("runtimeto", "");

                HttpResponse response = null;
                HttpEntity r_entity = null;

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                if (!str_wowtag.equals("")) {
                    entity.addPart("wowtagvideo", new FileBody(new File(str_wowtag), "video/mp4"));
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
                jsonObject.accumulate("registerrsvp", str_registerrsvp);
                //   jsonObject.accumulate("eventvenueaddressvisibility", "fgg");


                JSONArray jArray = new JSONArray();// /ItemDetail jsonArray

                if (eventvenue_et.getText().toString().length() > 0) {


                    JSONObject jGroup = new JSONObject();// /sub Object

                    try {
                        jGroup.put("eventvenuenumber", "1");
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
                        edit.remove("video1");
                        edit.commit();

                        JSONObject jsonObject = jo.getJSONObject("message");
                        final String eventId = jsonObject.getString("_id");
                        Log.e("tag", "111-1-------->" + eventId);

                        askinvite_dialog = new Dialog(QuickCreateEventVenues.this);
                        askinvite_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        askinvite_dialog.setContentView(R.layout.dilaog_sendinvites);
                        View v1 = askinvite_dialog.getWindow().getDecorView().getRootView();
                        FontsOverride.overrideFonts(QuickCreateEventVenues.this, v1);
                        ImageView close = askinvite_dialog.findViewById(R.id.closeiv);
                        TextView tvinvite = (TextView) askinvite_dialog.findViewById(R.id.invitetv);
                        TextView tvskip = (TextView) askinvite_dialog.findViewById(R.id.tvskip);
                        tvinvite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(QuickCreateEventVenues.this, EventInviteActivity.class);
                                Log.e("tag", "11111111111" + eventId);
                                Bundle bundle = new Bundle();
                                bundle.putString("eventId", eventId);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                sharedPrefces = PreferenceManager.getDefaultSharedPreferences(QuickCreateEventVenues.this);
                                edit = sharedPrefces.edit();
                                edit.putString("eventId", eventId);
                                edit.putString("feedstatus", "allevents");
                                edit.commit();
                            }
                        });
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                askinvite_dialog.dismiss();
                            }
                        });


                        askinvite_dialog.show();

                        tvskip.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                askinvite_dialog.dismiss();
                                // Toast.makeText(QuickCreateEventVenues.this, "Quick Event Created Successfully", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(QuickCreateEventVenues.this, EventFeedDashboard.class));
                                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                                finish();
                            }
                        });


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

                //  jsonObject.accumulate("onlineeventname", onlinestatus);
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

    public class fetchGroup extends AsyncTask<String, Void, String> {
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
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/group/fetchgroups", json, token);
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
                        try {
                            // av_loader.setVisibility(View.GONE);
                            JSONArray feedArray = jo.getJSONArray("groups");

                            for (int i = 0; i < feedArray.length(); i++) {
                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                Group item = new Group();
                                item.setId(feedObj.getString("_id"));
                                item.setGroupname(feedObj.getString("groupname"));
                                JSONArray users = feedObj.getJSONArray("users");
                                item.setGroupcount("" + users.length());

                                JSONObject adminobj = feedObj.getJSONObject("adminid");
                                {
                                    item.setFirstname(adminobj.getString("firstname"));
                                }


                                groups.add(item);

                                Log.e("tag", "basda-----" + groups.toString());
                            }

                            groupAdapter adapter = new groupAdapter(QuickCreateEventVenues.this, groups);
                            listview.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    class groupAdapter extends BaseAdapter {
        SharedPreferences.Editor editor;
        String token, userId;
        Dialog dialog;
        private Activity activity;
        private LayoutInflater inflater;
        private List<Group> feedItems;

        public groupAdapter(Activity activity, List<Group> feedItems) {
            this.activity = activity;
            this.feedItems = feedItems;
        }

        @Override
        public int getCount() {
            return feedItems.size();
        }

        @Override
        public Object getItem(int location) {
            return feedItems.get(location);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.dialog_viewgroup, null);
                viewHolder = new ViewHolder();
                viewHolder.position = position;
                FontsOverride.overrideFonts(activity, convertView);
                viewHolder.name = (CheckBox) convertView.findViewById(R.id.text1);
                viewHolder.memberscount = (TextView) convertView.findViewById(R.id.members_tv);
                viewHolder.createdname_tv = convertView.findViewById(R.id.createdname_tv);
                convertView.setTag(viewHolder);


            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Group item = groups.get(position);
            viewHolder.name.setText(item.getGroupname());
            Log.e("tag", "Gropupname" + item.getGroupname());
            viewHolder.memberscount.setText(item.getGroupcount() + " Members");

            viewHolder.createdname_tv.setText("Created by " + item.getFirstname());
            viewHolder.name.setChecked(false);

            return convertView;
        }

        class ViewHolder {
            TextView memberscount, createdname_tv;
            int position;
            CheckBox name;


        }


    }
}


