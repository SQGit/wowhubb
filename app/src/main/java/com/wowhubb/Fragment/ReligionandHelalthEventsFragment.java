package com.wowhubb.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
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
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;


/**
 * Created by Ramya on 24-07-2017.
 */

public class ReligionandHelalthEventsFragment extends Fragment {
    CheckBox cse_cb, hobbies_cb, parties_cb, charity_cb, family_cb, witness_cb;
    ImageView cse_iv, hobbies_iv, parties_ev, charity_iv, femily_iv, witness_iv;
    String religion, charity, family, food, wellness, sports;
    boolean flag1, flag2, flag3, flag4, flag5, flag6 = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_relegionandhealthevent,
                container, false);


        FontsOverride.overrideFonts(getActivity(), view);

        SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor edit = sharedPrefces.edit();

        cse_cb = (CheckBox) view.findViewById(R.id.religion_cb);
        hobbies_cb = (CheckBox) view.findViewById(R.id.food_cb);
        parties_cb = (CheckBox) view.findViewById(R.id.sports_cb);
        charity_cb = (CheckBox) view.findViewById(R.id.charity_cb);
        family_cb = (CheckBox) view.findViewById(R.id.family_cb);
        witness_cb = (CheckBox) view.findViewById(R.id.witness_cb);

        cse_iv = (ImageView) view.findViewById(R.id.comedy_iv);
        hobbies_iv = (ImageView) view.findViewById(R.id.hobbies_iv);
        parties_ev = (ImageView) view.findViewById(R.id.sports_iv);

        charity_iv = (ImageView) view.findViewById(R.id.charity_iv);
        femily_iv = (ImageView) view.findViewById(R.id.family_iv);
        witness_iv = (ImageView) view.findViewById(R.id.witness_iv);
        ImageView next = (ImageView) view.findViewById(R.id.submit_iv);


        religion = sharedPrefces.getString("religion", "");
        charity = sharedPrefces.getString("charity", "");
        family = sharedPrefces.getString("family", "");
        food = sharedPrefces.getString("food", "");
        wellness = sharedPrefces.getString("wellness", "");
        sports = sharedPrefces.getString("sports", "");
        if (!religion.equals("")) {
            if (religion.equals("10")) {
                cse_cb.setChecked(true);
                cse_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                cse_iv.setColorFilter(cse_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            }

        }
        if (!food.equals("")) {
            if (food.equals("13")) {
                hobbies_cb.setChecked(true);
                hobbies_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                hobbies_iv.setColorFilter(hobbies_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            }

        }

        if (!sports.equals("")) {
            if (sports.equals("15")) {
                parties_cb.setChecked(true);
                parties_ev.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                parties_ev.setColorFilter(parties_ev.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            }

        }
        if (!charity.equals("")) {
            if (charity.equals("11")) {
                charity_cb.setChecked(true);
                charity_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                charity_iv.setColorFilter(charity_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            }

        }


        if (!family.equals("")) {
            if (family.equals("12")) {
                family_cb.setChecked(true);
                femily_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                femily_iv.setColorFilter(femily_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            }

        }

        if (!wellness.equals("")) {
            if (wellness.equals("14")) {
                witness_cb.setChecked(true);
                witness_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                witness_iv.setColorFilter(witness_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            }

        }

        //------------------------TravelEducationEventsFragment-------------------------------//
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TravelEducationEventsFragment bef = new TravelEducationEventsFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, bef);
                ft.commit();
            }
        });
        cse_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag1 == true) {
                    flag1 = false;
                    cse_cb.setChecked(true);
                    Log.e("tag", "11111111111111111");
                    InterestActivity.list.add("religion");
                    edit.putString("religion", "10");
                    edit.commit();
                    cse_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    cse_iv.setColorFilter(cse_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    Log.e("tag", "22222222222");
                    cse_cb.setChecked(false);
                    flag1 = true;
                    InterestActivity.list.remove("religion");
                    edit.putString("religion", "0");
                    edit.commit();
                    cse_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    cse_iv.setColorFilter(cse_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }

            }
        });
        cse_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    InterestActivity.list.add("religion");
                    edit.putString("religion", "10");
                    edit.commit();
                    cse_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    cse_iv.setColorFilter(cse_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("religion");
                    edit.putString("religion", "0");
                    edit.commit();
                    cse_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    cse_iv.setColorFilter(cse_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
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
                    InterestActivity.list.add("food");
                    edit.putString("food", "13");
                    edit.commit();
                    hobbies_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    hobbies_iv.setColorFilter(hobbies_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    Log.e("tag", "22222222222");
                    hobbies_cb.setChecked(false);
                    flag2 = true;
                    InterestActivity.list.remove("food");
                    edit.putString("food", "13");
                    edit.commit();
                    hobbies_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    hobbies_iv.setColorFilter(hobbies_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }

            }
        });
        hobbies_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    InterestActivity.list.add("food");
                    edit.putString("food", "13");
                    edit.commit();
                    hobbies_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    hobbies_iv.setColorFilter(hobbies_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("food");
                    edit.putString("food", "13");
                    edit.commit();
                    hobbies_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    hobbies_iv.setColorFilter(hobbies_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        parties_ev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag3 == true) {
                    flag3 = false;
                    parties_cb.setChecked(true);
                    Log.e("tag", "11111111111111111");
                    InterestActivity.list.add("sports");
                    edit.putString("sports", "15");
                    edit.commit();
                    parties_ev.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    parties_ev.setColorFilter(parties_ev.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    Log.e("tag", "22222222222");
                    parties_cb.setChecked(false);
                    flag3 = true;
                    InterestActivity.list.remove("sports");
                    edit.putString("sports", "0");
                    edit.commit();
                    parties_ev.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    parties_ev.setColorFilter(parties_ev.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }

            }
        });
        parties_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    InterestActivity.list.add("sports");
                    edit.putString("sports", "15");
                    edit.commit();
                    parties_ev.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    parties_ev.setColorFilter(parties_ev.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("sports");
                    edit.putString("sports", "0");
                    edit.commit();
                    parties_ev.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    parties_ev.setColorFilter(parties_ev.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
        charity_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag4 == true) {
                    flag4 = false;
                    charity_cb.setChecked(true);
                    Log.e("tag", "11111111111111111");
                    InterestActivity.list.add("charity");
                    edit.putString("charity", "11");
                    edit.commit();
                    charity_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    charity_iv.setColorFilter(charity_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    Log.e("tag", "22222222222");
                    charity_cb.setChecked(false);
                    flag4 = true;
                    InterestActivity.list.remove("charity");
                    edit.putString("charity", "0");
                    edit.commit();
                    charity_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    charity_iv.setColorFilter(charity_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }

            }
        });
        charity_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    InterestActivity.list.add("charity");
                    edit.putString("charity", "11");
                    edit.commit();
                    charity_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    charity_iv.setColorFilter(charity_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("charity");
                    edit.putString("charity", "0");
                    edit.commit();
                    charity_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    charity_iv.setColorFilter(charity_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
        femily_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag5 == true) {
                    flag5 = false;
                    family_cb.setChecked(true);
                    Log.e("tag", "11111111111111111");
                    InterestActivity.list.add("family");
                    edit.putString("family", "12");
                    edit.commit();
                    femily_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    femily_iv.setColorFilter(femily_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    Log.e("tag", "22222222222");
                    family_cb.setChecked(false);
                    flag5 = true;
                    InterestActivity.list.remove("family");
                    edit.putString("family", "0");
                    edit.commit();
                    femily_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    femily_iv.setColorFilter(femily_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }

            }
        });
        family_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    InterestActivity.list.add("family");
                    edit.putString("family", "12");
                    edit.commit();
                    femily_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    femily_iv.setColorFilter(femily_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("family");
                    edit.putString("family", "0");
                    edit.commit();
                    femily_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    femily_iv.setColorFilter(femily_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
        witness_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag6 == true) {
                    flag6 = false;
                    witness_cb.setChecked(true);
                    Log.e("tag", "11111111111111111");
                    InterestActivity.list.add("wellness");
                    edit.putString("wellness", "14");
                    edit.commit();
                    witness_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    witness_iv.setColorFilter(witness_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    Log.e("tag", "22222222222");
                    witness_cb.setChecked(false);
                    flag6 = true;
                    InterestActivity.list.remove("wellness");
                    edit.putString("wellness", "0");
                    edit.commit();
                    witness_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    witness_iv.setColorFilter(witness_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }

            }
        });
        witness_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    InterestActivity.list.add("wellness");
                    edit.putString("wellness", "14");
                    edit.commit();
                    witness_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    witness_iv.setColorFilter(witness_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("wellness");
                    edit.putString("wellness", "0");
                    edit.commit();
                    witness_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    witness_iv.setColorFilter(witness_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });


        return view;
    }
}
