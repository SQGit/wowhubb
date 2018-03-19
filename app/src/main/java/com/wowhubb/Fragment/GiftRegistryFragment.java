package com.wowhubb.Fragment;

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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;
import com.wowhubb.Activity.CreateEventActivity;
import com.wowhubb.Activity.LandingPageActivity;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;
import com.wowhubb.Utils.HttpUtils;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class GiftRegistryFragment extends Fragment {
    Dialog dialog, loader_dialog;
    CheckBox sharenw_cb, specific_group,eventlink_cb;
    TextInputLayout til_ticketurl, til_otherurl, til_donation;
    Typeface lato;
    String profilepath, video1, video2, coverpage, token, eventId, highlights1, highlights2, highlight1_status, highlight2_status, highlightsvideo1, highlightsvideo2;
    AVLoadingIndicatorView av_loader;
    String runtimeFrom, runtimeTo,str_category;
    EditText eventregistryurl_et, donationurl_et, othersurl_et;
    private TextView callToAction, subaction;
    LinearLayout ll;

    public static GiftRegistryFragment newInstance(int page, boolean isLast) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        if (isLast)
            args.putBoolean("isLast", true);
        final GiftRegistryFragment fragment = new GiftRegistryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_giftregistry, container, false);
        FontsOverride.overrideFonts(getActivity(), view);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        video1 = sharedPreferences.getString("video1", "");
        token = sharedPreferences.getString("token", "");
        runtimeFrom = sharedPreferences.getString("runtimeFrom", "");
        runtimeTo = sharedPreferences.getString("runtimeTo", "");
        str_category = sharedPreferences.getString("str_category", "");

        CreateEventActivity.skiptv.setVisibility(View.INVISIBLE);

        av_loader = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");
        til_ticketurl = (TextInputLayout) view.findViewById(R.id.til_ticketurl);
        til_donation = (TextInputLayout) view.findViewById(R.id.til_donation);
        til_otherurl = (TextInputLayout) view.findViewById(R.id.til_otherurl);
        callToAction = (TextView) view.findViewById(R.id.tv_calltoaction);

        eventregistryurl_et = view.findViewById(R.id.eventreg_et);
        donationurl_et = view.findViewById(R.id.donationurl_et);
        othersurl_et = view.findViewById(R.id.otherurl_et);
        ll=view.findViewById(R.id.ll);

        callToAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                video1 = sharedPreferences.getString("video1", "");
                video2 = sharedPreferences.getString("video2", "");
                coverpage = sharedPreferences.getString("coverpage", "");
                highlights1 = sharedPreferences.getString("highlight1", "");
                highlights2 = sharedPreferences.getString("highlight2", "");
                highlight1_status = sharedPreferences.getString("highlight1_status", "");
                highlight2_status = sharedPreferences.getString("highlight2_status", "");
                highlightsvideo1 = sharedPreferences.getString("highlightsvideo1", "");
                highlightsvideo2 = sharedPreferences.getString("highlightsvideo2", "");
                Log.e("tag", "video1" + highlights1 + highlight1_status);
                new videoupload_task().execute();

            }
        });

        til_donation.setTypeface(lato);
        til_otherurl.setTypeface(lato);
        til_ticketurl.setTypeface(lato);

        loader_dialog = new Dialog(getActivity());
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);
        sharenw_cb = view.findViewById(R.id.share_cb);
        specific_group = view.findViewById(R.id.specific_group_cb);
        eventlink_cb = view.findViewById(R.id.eventlink_cb);
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_wowhubb_network);
        View view1 = dialog.getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(dialog.getContext(), view1);
        ImageView share_iv = dialog.findViewById(R.id.closeiv);
        share_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        sharenw_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                video1 = sharedPreferences.getString("video1", "");
                video2 = sharedPreferences.getString("video2", "");
                coverpage = sharedPreferences.getString("coverpage", "");
                highlights1 = sharedPreferences.getString("highlight1", "");
                highlights2 = sharedPreferences.getString("highlight2", "");
                highlight1_status = sharedPreferences.getString("highlight1_status", "");
                highlight2_status = sharedPreferences.getString("highlight2_status", "");
                highlightsvideo1 = sharedPreferences.getString("highlightsvideo1", "");
                highlightsvideo2 = sharedPreferences.getString("highlightsvideo2", "");
                Log.e("tag", "video1" + highlights1 + highlight1_status);
                new videoupload_task().execute();

            }
        });


        specific_group.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    dialog.show();
                } else {
                    dialog.dismiss();
                }
            }
        });
        eventlink_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    eventregistryurl_et.setVisibility(View.GONE);
                    donationurl_et.setVisibility(View.GONE);
                    othersurl_et.setVisibility(View.GONE);
                    ll.setVisibility(View.GONE);
                }
                else
                {
                    eventregistryurl_et.setVisibility(View.VISIBLE);
                    donationurl_et.setVisibility(View.VISIBLE);
                    othersurl_et.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

    public class videoupload_task extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
            //av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                String responseString = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://104.197.80.225:3010/wow/event/create");
                httppost.setHeader("token", token);
                HttpResponse response = null;
                HttpEntity r_entity = null;

                if (video1 != null) {
                    Log.e("tag", "strt111" + profilepath);
                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    if (!coverpage.equals("")) {
                        entity.addPart("coverpage1", new FileBody(new File(coverpage), "image/jpeg"));
                    }
                    if (!video1.equals("")) {
                        entity.addPart("wowtagvideo1", new FileBody(new File(video1), "video/mp4"));
                    }
                    if (!highlights1.equals("")) {
                        if (highlight1_status.equals("image")) {
                            entity.addPart("eventhighlights1", new FileBody(new File(highlights1), "image/jpeg"));
                        }
                    }
                    if (!highlightsvideo1.equals("")) {
                        if (highlight1_status.equals("video")) {
                            entity.addPart("eventhighlightsvideo1", new FileBody(new File(highlightsvideo1), "video/mp4"));
                        }
                    }


                    if (!highlights2.equals("")) {
                        if (highlight2_status.equals("image")) {
                            entity.addPart("eventhighlights2", new FileBody(new File(highlights2), "image/jpeg"));
                        }
                    }
                    if (!highlightsvideo2.equals("")) {
                        if (highlight2_status.equals("video")) {
                            entity.addPart("eventhighlightsvideo2", new FileBody(new File(highlightsvideo2), "video/mp4"));
                        }
                    }
                    httppost.setEntity(entity);
                }

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
            try {
                JSONObject jo = new JSONObject(s.toString());
                String success = jo.getString("success");
                if (success.equals("true")) {
                    JSONObject msg = jo.getJSONObject("message");
                    eventId = msg.getString("_id");
                    new updateeventinfo().execute();
                } else {


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    public class updateeventinfo extends AsyncTask<String, Void, String> {

        public updateeventinfo() {

        }

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
                jsonObject.accumulate("eventtopic", EventTypeFragment.eventtopic_et.getText().toString());
                jsonObject.accumulate("eventdescription", EventTypeFragment.desc_et.getText().toString());
                jsonObject.accumulate("eventdate", EventTypeFragment.eventdate_et.getText().toString());
                jsonObject.accumulate("eventname", "!" + WowtagFragment.eventname_et.getText().toString());

                jsonObject.accumulate("eventvenuename", EventVenueFragment.venuename.getText().toString());
                jsonObject.accumulate("eventvenueaddress", EventVenueFragment.address.getText().toString());
                jsonObject.accumulate("eventvenuecity", EventVenueFragment.city.getText().toString());
                jsonObject.accumulate("eventvenuestate", EventVenueFragment.state.getText().toString());
                jsonObject.accumulate("eventvenuezipcode", EventVenueFragment.zipcode.getText().toString());
                jsonObject.accumulate("runtimefrom", runtimeFrom);
                jsonObject.accumulate("runtimeto", runtimeTo);

                String gift_url=eventregistryurl_et.getText().toString();
                String donation_url=donationurl_et.getText().toString();
                String other_url=othersurl_et.getText().toString();

                if(!gift_url.matches(""))
                {
                    Log.e("tag","gift urllllll");
                    jsonObject.accumulate("giftregistryurl", eventregistryurl_et.getText().toString());
                }
                if(!donation_url.matches(""))
                {
                    jsonObject.accumulate("donationsurl", donationurl_et.getText().toString());
                }
                if(!other_url.matches(""))
                {
                    jsonObject.accumulate("otherurl", othersurl_et.getText().toString());
                }
                if(str_category.equals("others"))
                {
                    jsonObject.accumulate("eventtype", EventTypeFragment.eventaddcategory_et.getText().toString());
                }
                else
                {
                    jsonObject.accumulate("eventtype", str_category);
                }

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/info", json, token);
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
                        Toast.makeText(getActivity(), "Event Created Successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getActivity(), LandingPageActivity.class));
                        getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
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