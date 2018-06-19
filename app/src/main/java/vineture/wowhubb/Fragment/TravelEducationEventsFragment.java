package vineture.wowhubb.Fragment;

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

import vineture.wowhubb.Activity.InterestActivity;
import vineture.wowhubb.Activity.ProfileActivity;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.Utils.HttpUtils;

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
    boolean flag1, flag2, flag3, flag4, flag5, flag6 = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(vineture.wowhubb.R.layout.activity_travelandeducationevents,
                container, false);
        FontsOverride.overrideFonts(getActivity(), view);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = sharedPreferences.getString("token", "");
        ImageView next = (ImageView) view.findViewById(vineture.wowhubb.R.id.submit_iv);
        SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor edit = sharedPrefces.edit();
        cse_cb = (CheckBox) view.findViewById(vineture.wowhubb.R.id.comedy_cb);
        hobbies_cb = (CheckBox) view.findViewById(vineture.wowhubb.R.id.hobbies_cb);
        parties_cb = (CheckBox) view.findViewById(vineture.wowhubb.R.id.musicarts_cb);
        autoboat_cb = (CheckBox) view.findViewById(vineture.wowhubb.R.id.autoboat_cb);
        holiday_cb = (CheckBox) view.findViewById(vineture.wowhubb.R.id.holiday_cb);
        political_cb = (CheckBox) view.findViewById(vineture.wowhubb.R.id.political_cb);

        cse_iv = (ImageView) view.findViewById(vineture.wowhubb.R.id.comedy_iv);
        hobbies_iv = (ImageView) view.findViewById(vineture.wowhubb.R.id.hobbies_iv);
        parties_ev = (ImageView) view.findViewById(vineture.wowhubb.R.id.musicarts_iv);
        autoboat_iv = (ImageView) view.findViewById(vineture.wowhubb.R.id.autoboat_iv);
        holiday_iv = (ImageView) view.findViewById(vineture.wowhubb.R.id.holiday_iv);
        political_iv = (ImageView) view.findViewById(vineture.wowhubb.R.id.political_iv);



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tag", "Interest------------>" + InterestActivity.list.size());
                new updateInterestinfo().execute();
            }
        });

        cse_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag1 == true) {
                    flag1 = false;
                    cse_cb.setChecked(true);
                    Log.e("tag", "11111111111111111");

                    edit.putString("comedy", "15");
                    edit.commit();
                    InterestActivity.list.add("comedy");
                    cse_iv.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest_color));
                    cse_iv.setColorFilter(cse_iv.getContext().getResources().getColor(vineture.wowhubb.R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    Log.e("tag", "22222222222");
                    cse_cb.setChecked(false);
                    flag1 = true;
                    InterestActivity.list.remove("comedy");
                    edit.putString("comedy", "0");
                    edit.commit();
                    cse_iv.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest));
                    cse_iv.setColorFilter(cse_iv.getContext().getResources().getColor(vineture.wowhubb.R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }

            }
        });
        cse_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    InterestActivity.list.add("comedy");
                    cse_iv.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest_color));
                    cse_iv.setColorFilter(cse_iv.getContext().getResources().getColor(vineture.wowhubb.R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("comedy");
                    cse_iv.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest));
                    cse_iv.setColorFilter(cse_iv.getContext().getResources().getColor(vineture.wowhubb.R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
        hobbies_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag2 == true) {
                    flag2 = false;
                    hobbies_cb.setChecked(true);
                    Log.e("tag", "11111111111111111");
                    InterestActivity.list.add("religion");
                    edit.putString("religion", "16");
                    edit.commit();
                    hobbies_iv.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest_color));
                    hobbies_iv.setColorFilter(hobbies_iv.getContext().getResources().getColor(vineture.wowhubb.R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    Log.e("tag", "22222222222");
                    hobbies_cb.setChecked(false);
                    flag2 = true;
                    InterestActivity.list.remove("religion");
                    edit.putString("religion", "0");
                    edit.commit();
                    hobbies_iv.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest));
                    hobbies_iv.setColorFilter(hobbies_iv.getContext().getResources().getColor(vineture.wowhubb.R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }

            }
        });

        hobbies_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    hobbies_iv.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest_color));
                    hobbies_iv.setColorFilter(hobbies_iv.getContext().getResources().getColor(vineture.wowhubb.R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    hobbies_iv.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest));
                    hobbies_iv.setColorFilter(hobbies_iv.getContext().getResources().getColor(vineture.wowhubb.R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        parties_ev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag3== true) {
                    flag3 = false;
                    parties_cb.setChecked(true);
                    Log.e("tag", "11111111111111111");
                    InterestActivity.list.add("religion");
                    edit.putString("religion", "10");
                    edit.commit();
                    parties_ev.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest_color));
                    parties_ev.setColorFilter(parties_ev.getContext().getResources().getColor(vineture.wowhubb.R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    Log.e("tag", "22222222222");
                    parties_cb.setChecked(false);
                    flag3 = true;
                    InterestActivity.list.remove("religion");
                    edit.putString("religion", "0");
                    edit.commit();
                    parties_ev.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest));
                    parties_ev.setColorFilter(parties_ev.getContext().getResources().getColor(vineture.wowhubb.R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }

            }
        });
        parties_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    parties_ev.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest_color));
                    parties_ev.setColorFilter(parties_ev.getContext().getResources().getColor(vineture.wowhubb.R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    parties_ev.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest));
                    parties_ev.setColorFilter(parties_ev.getContext().getResources().getColor(vineture.wowhubb.R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
        autoboat_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag4 == true) {
                    flag4 = false;
                    autoboat_cb.setChecked(true);
                    Log.e("tag", "11111111111111111");
                   // InterestActivity.list.add("religion");
                    edit.putString("auto", "10");
                    edit.commit();
                    InterestActivity.list.add("auto");
                    autoboat_iv.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest_color));
                    autoboat_iv.setColorFilter(autoboat_iv.getContext().getResources().getColor(vineture.wowhubb.R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    Log.e("tag", "22222222222");
                    autoboat_cb.setChecked(false);
                    flag4 = true;
                   // InterestActivity.list.remove("religion");
                    edit.putString("auto", "0");
                    edit.commit();
                    InterestActivity.list.remove("auto");
                    autoboat_iv.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest));
                    autoboat_iv.setColorFilter(autoboat_iv.getContext().getResources().getColor(vineture.wowhubb.R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }

            }
        });
        autoboat_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    InterestActivity.list.add("auto");
                    autoboat_iv.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest_color));
                    autoboat_iv.setColorFilter(autoboat_iv.getContext().getResources().getColor(vineture.wowhubb.R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("auto");
                    autoboat_iv.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest));
                    autoboat_iv.setColorFilter(autoboat_iv.getContext().getResources().getColor(vineture.wowhubb.R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
        political_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag5 == true) {
                    flag5 = false;
                    political_cb.setChecked(true);
                    Log.e("tag", "11111111111111111");
                    InterestActivity.list.add("political");
                    political_iv.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest_color));
                    political_iv.setColorFilter(political_iv.getContext().getResources().getColor(vineture.wowhubb.R.color.white), PorterDuff.Mode.SRC_ATOP);
                    edit.putString("political", "10");
                    edit.commit();

                } else {
                    Log.e("tag", "22222222222");
                    political_cb.setChecked(false);
                    flag5 = true;
                    edit.putString("political", "0");
                    edit.commit();
                    InterestActivity.list.remove("political");
                    political_iv.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest));
                    political_iv.setColorFilter(political_iv.getContext().getResources().getColor(vineture.wowhubb.R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }

            }
        });
        political_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    InterestActivity.list.add("political");
                    political_iv.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest_color));
                    political_iv.setColorFilter(political_iv.getContext().getResources().getColor(vineture.wowhubb.R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("political");
                    political_iv.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest));
                    political_iv.setColorFilter(political_iv.getContext().getResources().getColor(vineture.wowhubb.R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
        holiday_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag6 == true) {
                    flag6 = false;
                    holiday_cb.setChecked(true);
                    Log.e("tag", "11111111111111111");
                    edit.putString("holiday", "10");
                    edit.commit();
                    InterestActivity.list.add("holiday");
                    holiday_iv.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest_color));
                    holiday_iv.setColorFilter(holiday_iv.getContext().getResources().getColor(vineture.wowhubb.R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    Log.e("tag", "22222222222");
                    holiday_cb.setChecked(false);
                    flag6 = true;
                    edit.putString("holiday", "0");
                    edit.commit();
                    InterestActivity.list.remove("holiday");
                    holiday_iv.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest));
                    holiday_iv.setColorFilter(holiday_iv.getContext().getResources().getColor(vineture.wowhubb.R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }

            }
        });
        holiday_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    InterestActivity.list.add("holiday");
                    holiday_iv.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest_color));
                    holiday_iv.setColorFilter(holiday_iv.getContext().getResources().getColor(vineture.wowhubb.R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("holiday");
                    holiday_iv.setBackground(getResources().getDrawable(vineture.wowhubb.R.drawable.selector_interest));
                    holiday_iv.setColorFilter(holiday_iv.getContext().getResources().getColor(vineture.wowhubb.R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        return view;
    }

    //--------------------------------ASYN TASK FOR UPDATE INTEREST-------------------------------//

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
                JSONArray jsArray = new JSONArray(InterestActivity.list);
                jsonObject.put("interests", jsArray);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/updateinterests", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);
            if (s != null)
            {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        startActivity(new Intent(getActivity(), ProfileActivity.class));
                        getActivity().overridePendingTransition(vineture.wowhubb.R.anim.left_to_right, vineture.wowhubb.R.anim.right_to_left);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

            }

        }

    }
}
