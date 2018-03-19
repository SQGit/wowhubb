package com.wowhubb.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suke.widget.SwitchButton;
import com.wowhubb.Activity.CreateEventActivity;
import com.wowhubb.Activity.EventFeedDashboard;
import com.wowhubb.Adapter.ExpandableListAdapter;
import com.wowhubb.Adapter.ExpandableListDataPump;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;
import com.wowhubb.Utils.HttpUtils;
import com.wowhubb.Utils.Utils;
import com.wowhubb.data.EventData;

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
import java.util.HashMap;
import java.util.List;

public class EventContactFragment extends Fragment {

    public EventData eventData;
    TextInputLayout til_name, til_phone, til_email, til_msg, til_gift, til_donation, til_others;
    Typeface lato;
    EditText event_info, name, phone, email, message;
    CheckBox sharenw_cb, specific_group, eventlink_cb;
    Dialog dialog, loader_dialog;
    String str_phone, str_email;
    TextView helpfultips_tv;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    String runtimeFrom, runtimeTo, str_category, eventhighlights2, eventhighlightsvideo2, eventhighlightsvideo1, eventhighlights1, speakername2, speakerlink2, speakeractivities2;
    EditText eventregistryurl_et, donationurl_et, othersurl_et;
    LinearLayout ll;
    String profilepath, video1, video2, coverpage, token, eventId, highlights1, highlights2, highlight1_status, highlight2_status, highlightsvideo1, highlightsvideo2;
    //eventhighlights1,eventhighlights2,eventhighlightsvideo1,eventhighlightsvideo2
    SharedPreferences.Editor edit;
    String eventguesttype2;
    int eventcount;

    public static EventContactFragment newInstance(int page, boolean isLast) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        if (isLast)
            args.putBoolean("isLast", true);
        final EventContactFragment fragment = new EventContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        eventData = ((CreateEventActivity) getActivity()).eventData;
        final View view = inflater.inflate(R.layout.fragment_eventcontact, container, false);

        FontsOverride.overrideFonts(getActivity(), view);
        CreateEventActivity.skiptv.setVisibility(View.VISIBLE);
        Log.e("tag", "listBeneficiary>>>>>>>>>>>>>>------" + EventVenueFragment.listBeneficiary.size());
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        edit = sharedPreferences.edit();
        video1 = sharedPreferences.getString("video1", "");
        token = sharedPreferences.getString("token", "");
        runtimeFrom = sharedPreferences.getString("runtimeFrom", "");
        runtimeTo = sharedPreferences.getString("runtimeTo", "");
        str_category = sharedPreferences.getString("str_category", "");
        coverpage = sharedPreferences.getString("coverpage", "");
        // eventhighlights2 = sharedPreferences.getString("eventhighlights2", "");
        eventhighlights1 = sharedPreferences.getString("eventhighlights1", "");
        // eventhighlightsvideo2 = sharedPreferences.getString("eventhighlightsvideo2", "");
        eventhighlightsvideo1 = sharedPreferences.getString("eventhighlightsvideo1", "");
        // speakername2 = sharedPreferences.getString("speakername2", "");
        // speakerlink2 = sharedPreferences.getString("speakerlink2", "");
        //  speakeractivities2 = sharedPreferences.getString("speakeractivities2", "");

        helpfultips_tv = view.findViewById(R.id.helpfultips_tv);

        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");
        final com.suke.widget.SwitchButton switchButton = (com.suke.widget.SwitchButton)
                view.findViewById(R.id.switch_button);
        com.suke.widget.SwitchButton switchButton1 = (com.suke.widget.SwitchButton)
                view.findViewById(R.id.switch_button1);


        loader_dialog = new Dialog(getActivity());
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);
        til_name = (TextInputLayout) view.findViewById(R.id.til_name);
        til_phone = (TextInputLayout) view.findViewById(R.id.til_phone);
        til_email = (TextInputLayout) view.findViewById(R.id.til_email);
        til_msg = (TextInputLayout) view.findViewById(R.id.til_msg);
        til_donation = view.findViewById(R.id.til_donation);
        til_gift = view.findViewById(R.id.til_gift);
        til_others = view.findViewById(R.id.til_otherurl);
        sharenw_cb = view.findViewById(R.id.share_cb);
        specific_group = view.findViewById(R.id.specific_group_cb);
        eventlink_cb = view.findViewById(R.id.eventlink_cb);
        name = view.findViewById(R.id.name_et);
        phone = view.findViewById(R.id.phone_et);
        email = view.findViewById(R.id.email_et);
        message = view.findViewById(R.id.msg_et);
        eventregistryurl_et = view.findViewById(R.id.eventreg_et);
        donationurl_et = view.findViewById(R.id.donationurl_et);
        othersurl_et = view.findViewById(R.id.otherurl_et);
        ll = view.findViewById(R.id.ll);
        til_name.setTypeface(lato);
        til_phone.setTypeface(lato);
        til_email.setTypeface(lato);
        til_msg.setTypeface(lato);
        til_gift.setTypeface(lato);
        til_donation.setTypeface(lato);
        til_others.setTypeface(lato);

        str_phone = sharedPreferences.getString("str_phone", "");
        str_email = sharedPreferences.getString("str_email", "");
        if (str_phone != null) {
            email.setText(str_email);
            phone.setText(str_phone);
            email.setEnabled(false);
            phone.setEnabled(false);
        }
        eventregistryurl_et.setText(eventData.gifturl);
        othersurl_et.setText(eventData.gifturl);
        donationurl_et.setText(eventData.donurl);

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

        // new eventvenue().execute();

        CreateEventActivity.publishtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateEventActivity.snackbar.dismiss();
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
                eventcount = sharedPreferences.getInt("str_eventday", 0);
                Log.e("tag", "video1" + highlights1 + highlight1_status);
                if (Utils.Operations.isOnline(getActivity())) {
                    new eventDetails().execute();
                } else {
                    CreateEventActivity.snackbar.show();
                }

            }
        });

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
                //  new videoupload_task().execute();

            }
        });

        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
                if (isChecked) {
                    phone.setEnabled(true);
                    email.setEnabled(true);
                } else {
                    phone.setEnabled(false);
                    email.setEnabled(false);
                }


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
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    eventregistryurl_et.setVisibility(View.GONE);
                    donationurl_et.setVisibility(View.GONE);
                    othersurl_et.setVisibility(View.GONE);
                    ll.setVisibility(View.GONE);
                } else {
                    eventregistryurl_et.setVisibility(View.VISIBLE);
                    donationurl_et.setVisibility(View.VISIBLE);
                    othersurl_et.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.VISIBLE);
                }
            }
        });
        switchButton1.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
                if (isChecked) {
                    phone.setEnabled(true);
                    email.setEnabled(true);
                } else {
                    phone.setEnabled(false);
                    email.setEnabled(false);
                }

            }
        });


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        setData();
    }

    private void setData() {
        eventData.gifturl = eventregistryurl_et.getText().toString().trim();
        eventData.otherurl = othersurl_et.getText().toString().trim();
        eventData.donurl = donationurl_et.getText().toString().trim();
        eventData.message = message.getText().toString().trim();

    }

    public class eventDetails extends AsyncTask<String, Void, String> {
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
                HttpPost httppost = new HttpPost("http://104.197.80.225:3010/wow/event/details");
                httppost.setHeader("token", token);

                httppost.setHeader("eventcategory", str_category);
                httppost.setHeader("eventtypeint", "" + 1);
                Log.e("tag", "typpppppppppppppppppppppp");
                httppost.setHeader("eventdayscount", String.valueOf(eventcount));
                Log.e("tag", "typpppppppppppppppppppp111111111111" + String.valueOf(eventcount));
                httppost.setHeader("eventtype", "personal_event");
                httppost.setHeader("eventname", EventTypeFragment.eventtopic_et.getText().toString());
                httppost.setHeader("eventtimezone", EventTypeFragment.eventtimezone_et.getText().toString());
                httppost.setHeader("eventstartdate", EventTypeFragment.eventdate_et.getText().toString());
                httppost.setHeader("eventenddate", EventTypeFragment.eventdateto_et.getText().toString());
                httppost.setHeader("eventdescription", EventTypeFragment.eventdescr_et.getText().toString());
                HttpResponse response = null;
                HttpEntity r_entity = null;


                Log.e("tag", "strt111" + coverpage);
                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                if (!coverpage.equals("")) {
                    entity.addPart("coverpage", new FileBody(new File(coverpage), "image/jpeg"));
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
            Log.e("tag", "eventIdddd----------------->>>>>>>" + s);
            if (s.length() > 0) {
                try {
                    JSONObject jo = new JSONObject(s.toString());
                    String success = jo.getString("success");
                    if (success.equals("true")) {
                        edit.remove("coverpage");
                        edit.commit();
                        JSONObject msg = jo.getJSONObject("message");
                        eventId = msg.getString("_id");
                        Log.e("tag", "eventIdddd----------------->>>>>>>" + eventId);

                        if (Utils.Operations.isOnline(getActivity())) {
                            new wowtagDetails().execute();
                        } else {
                            CreateEventActivity.snackbar.show();
                        }


                    } else {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }

    }

    public class wowtagDetails extends AsyncTask<String, Void, String> {
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
                HttpPost httppost = new HttpPost("http://104.197.80.225:3010/wow/event/eventwowtag");
                httppost.setHeader("token", token);

                httppost.setHeader("eventid", eventId);
                httppost.setHeader("eventtitle", WowtagFragment.eventname_et.getText().toString());
                httppost.setHeader("runtimefrom", WowtagFragment.fromtime_tv.getText().toString());
                httppost.setHeader("runtimeto", WowtagFragment.totime_tv.getText().toString());

                HttpResponse response = null;
                HttpEntity r_entity = null;

                Log.e("tag", "strt111" + coverpage);

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                if (!video1.equals("")) {
                    entity.addPart("wowtagvideo", new FileBody(new File(video1), "video/mp4"));
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
            Log.e("tag", "wwwwwwwwwwwwwww----------------->>>>>>>" + s);
            if (s.length() > 0) {
                try {
                    JSONObject jo = new JSONObject(s.toString());
                    String success = jo.getString("success");
                    if (success.equals("true")) {
                        edit.remove("video1");
                        edit.commit();
                        if (Utils.Operations.isOnline(getActivity())) {
                            new eventvenue().execute();
                        } else {
                            CreateEventActivity.snackbar.show();
                        }


                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }

    }

    public class eventhighlight extends AsyncTask<String, Void, String> {
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

                Log.e("tag", "IDDDDDDDD------------------->>>>>>>>>>" + token + eventId);
                HttpPost httppost = new HttpPost("http://104.197.80.225:3010/wow/event/highlights");
                httppost.setHeader("token", token);
                httppost.setHeader("eventid", eventId);
                httppost.setHeader("eventguesttype1", "dfdf");
                httppost.setHeader("eventspeakername1", EventHighlightsFragment.speaker_et.getText().toString());
                httppost.setHeader("eventspeakerlink1", EventHighlightsFragment.url_et.getText().toString());
                httppost.setHeader("eventspeakeractivities1", EventHighlightsFragment.intro_et.getText().toString());


                HttpResponse response = null;
                HttpEntity r_entity = null;


                Log.e("tag", "h12" + eventhighlights1);
                Log.e("tag", "h111" + eventhighlightsvideo1);

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                if (EventHighlightsFragment.eventhighlights1 != null) {
                    Log.e("tag", "h1" + eventhighlights1);
                    entity.addPart("eventhighlights1", new FileBody(new File(EventHighlightsFragment.eventhighlights1), "image/jpeg"));
                }

                if (eventhighlights1 != null) {
                    Log.e("tag", "haaaa1" + eventhighlights1);
                }
               /* if (eventhighlights2 != null) {
                    Log.e("tag", "h2" + eventhighlights2);
                    entity.addPart("eventhighlights2", new FileBody(new File(eventhighlights2), "image/jpeg"));

                }*/
                if (EventHighlightsFragment.eventhighlightsvideo1 != null) {
                    Log.e("tag", "h11" + eventhighlightsvideo1);
                    entity.addPart("eventhighlightsvideo1", new FileBody(new File(EventHighlightsFragment.eventhighlightsvideo1), "video/mp4"));
                }
                /*if (eventhighlightsvideo2 != null) {
                    Log.e("tag", "h12" + eventhighlightsvideo1);
                    entity.addPart("eventhighlightsvideo2", new FileBody(new File(eventhighlightsvideo2), "video/mp4"));

                }*/
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
            Log.e("tag", "highlights----------------->>>>>>>" + s);
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s.toString());
                    String success = jo.getString("success");
                    if (success.equals("true")) {

                        JSONObject msg = jo.getJSONObject("message");
                        eventId = msg.getString("_id");
                        Log.e("tag", "eventIdddd----------------->>>>>>>" + eventId);
                        if (Utils.Operations.isOnline(getActivity())) {
                            new eventcontact().execute();
                        } else {
                            CreateEventActivity.snackbar.show();
                        }

                    } else {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                jsonObject.accumulate("inviteonlyevent", "fdg");
                jsonObject.accumulate("onlineevent", "fgf");
                jsonObject.accumulate("eventvenueguestshare", "fggf");
                jsonObject.accumulate("eventvenueaddressvisibility", "fgg");

                JSONArray jArray = new JSONArray();// /ItemDetail jsonArray

                if (EventVenueFragment.listBeneficiary.size() > 0) {


                    for (int i = 0; i < EventVenueFragment.listBeneficiary.size(); i++) {
                        JSONObject jGroup = new JSONObject();// /sub Object

                        try {
                            jGroup.put("eventvenuenumber", EventVenueFragment.listBeneficiary.get(i).getId());
                            jGroup.put("eventvenuename", EventVenueFragment.listBeneficiary.get(i).getVenuname());
                            jGroup.put("eventvenueaddress1", EventVenueFragment.listBeneficiary.get(i).getAddress());
                            jGroup.put("eventvenuecity", EventVenueFragment.listBeneficiary.get(i).getCity());
                            jGroup.put("eventvenuezipcode", EventVenueFragment.listBeneficiary.get(i).getZipcode());

                            jArray.put(jGroup);

                            // /itemDetail Name is JsonArray Name
                            jsonObject.put("eventvenue", jArray);
                            //return jResult;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {

                }
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/venue", json, token);
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
                        if (Utils.Operations.isOnline(getActivity())) {
                            new eventhighlight().execute();
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

    public class eventcontact extends AsyncTask<String, Void, String> {
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
                jsonObject.accumulate("eventcontactname", name.getText().toString());
                jsonObject.accumulate("eventcontactphone", phone.getText().toString());
                jsonObject.accumulate("eventcontactemail", email.getText().toString());
                jsonObject.accumulate("eventcontactmessage", message.getText().toString());

                jsonObject.accumulate("privateevent", "pv");
                jsonObject.accumulate("registerrsvp", "regrsvp");
                jsonObject.accumulate("eventnolinks", "no lilnks");


                String gift_url = eventregistryurl_et.getText().toString();
                String donation_url = donationurl_et.getText().toString();
                String other_url = othersurl_et.getText().toString();

                if (!gift_url.matches("")) {
                    Log.e("tag", "gift urllllll");
                    jsonObject.accumulate("giftregistryurl", eventregistryurl_et.getText().toString());
                }
                if (!donation_url.matches("")) {
                    jsonObject.accumulate("donationsurl", donationurl_et.getText().toString());
                }
                if (!other_url.matches("")) {
                    jsonObject.accumulate("otherurl", othersurl_et.getText().toString());
                }
               /* if (str_category.equals("others"))
                {
                    jsonObject.accumulate("eventtype", EventTypeFragment.eventaddcategory_et.getText().toString());
                }
                else
                {
                    jsonObject.accumulate("eventtype", str_category);
                }*/

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/contact", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "FINALLLLLLLLLLLLLLLLLLL" + s);
            loader_dialog.dismiss();
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        Toast.makeText(getActivity(), "Event Created Successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getActivity(), EventFeedDashboard.class));
                        getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        getActivity().finish();
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