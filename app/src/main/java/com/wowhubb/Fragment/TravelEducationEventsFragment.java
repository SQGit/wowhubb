package com.wowhubb.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.wowhubb.Activity.InterestActivity;
import com.wowhubb.Activity.ProfileActivity;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;
import com.wowhubb.Utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Ramya on 24-07-2017.
 */

public class TravelEducationEventsFragment extends Fragment {
    CheckBox cse_cb, hobbies_cb, parties_cb, autoboat_cb, holiday_cb, political_cb;
    ImageView cse_iv, hobbies_iv, parties_ev, autoboat_iv, holiday_iv, political_iv;
    String token;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_travelandeducationevents,
                container, false);
        FontsOverride.overrideFonts(getActivity(), view);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = sharedPreferences.getString("token", "");
        ImageView next = (ImageView) view.findViewById(R.id.submit_iv);

        cse_cb = (CheckBox) view.findViewById(R.id.comedy_cb);
        hobbies_cb = (CheckBox) view.findViewById(R.id.hobbies_cb);
        parties_cb = (CheckBox) view.findViewById(R.id.musicarts_cb);
        autoboat_cb = (CheckBox) view.findViewById(R.id.autoboat_cb);
        holiday_cb = (CheckBox) view.findViewById(R.id.holiday_cb);
        political_cb = (CheckBox) view.findViewById(R.id.political_cb);

        cse_iv = (ImageView) view.findViewById(R.id.comedy_iv);
        hobbies_iv = (ImageView) view.findViewById(R.id.hobbies_iv);
        parties_ev = (ImageView) view.findViewById(R.id.musicarts_iv);
        autoboat_iv = (ImageView) view.findViewById(R.id.autoboat_iv);
        holiday_iv = (ImageView) view.findViewById(R.id.holiday_iv);
        political_iv = (ImageView) view.findViewById(R.id.political_iv);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tag", "Interest------------>" + InterestActivity.list.size());
               /* startActivity(new Intent(getActivity(), ProfileActivity.class));
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);*/
                new updateInterestinfo().execute();
            }
        });


        cse_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    InterestActivity.list.add("comedy");
                    cse_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    cse_iv.setColorFilter(cse_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("comedy");
                    cse_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    cse_iv.setColorFilter(cse_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });


        hobbies_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    hobbies_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    hobbies_iv.setColorFilter(hobbies_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    hobbies_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    hobbies_iv.setColorFilter(hobbies_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });


        parties_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    parties_ev.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    parties_ev.setColorFilter(parties_ev.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    parties_ev.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    parties_ev.setColorFilter(parties_ev.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        autoboat_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    InterestActivity.list.add("auto");
                    autoboat_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    autoboat_iv.setColorFilter(autoboat_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("auto");
                    autoboat_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    autoboat_iv.setColorFilter(autoboat_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        political_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    InterestActivity.list.add("political");
                    political_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    political_iv.setColorFilter(political_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("political");
                    political_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    political_iv.setColorFilter(political_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
        holiday_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    InterestActivity.list.add("holiday");
                    holiday_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    holiday_iv.setColorFilter(holiday_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("holiday");
                    holiday_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    holiday_iv.setColorFilter(holiday_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        return view;
    }


    public class updateInterestinfo extends AsyncTask<String, Void, String> {

        public updateInterestinfo() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
             /*   "{
                ""interests"": [
                ""birthday"",""social"",""politics""
        ]
            }"*/
               /* jsonObject.accumulate("highlightdescription", EventTypeFragment.desc_et.getText().toString());
                jsonObject.accumulate("eventtime", EventTypeFragment.eventdate_et.getText().toString());
                jsonObject.accumulate("eventname", "!" + WowtagFragment.eventname_et.getText().toString());

                jsonObject.accumulate("eventvenuename", EventVenueFragment.venuename.getText().toString());
                jsonObject.accumulate("eventvenueaddress", EventVenueFragment.address.getText().toString());
                jsonObject.accumulate("eventvenuecity", EventVenueFragment.city.getText().toString());
                jsonObject.accumulate("eventvenuestate", EventVenueFragment.state.getText().toString());
                jsonObject.accumulate("eventvenuezipcode", EventVenueFragment.zipcode.getText().toString());*/

                JSONArray jsArray = new JSONArray(InterestActivity.list);
                //JSONArray jo=new JSONArray();
                jsonObject.put("interests", jsArray);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/getinterests", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);
            // av_loader.setVisibility(View.GONE);
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        startActivity(new Intent(getActivity(), ProfileActivity.class));
                        getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
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
