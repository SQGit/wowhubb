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

public class SocialEventsFragment extends Fragment {

    CheckBox cse_cb, hobbies_cb, parties_cb, media_cb, homelife_cb, musicarts_cb;
    ImageView cse_iv, hobbies_iv, parties_ev, media_iv, homelife_iv, musicarts_iv;
    String cse,hobbies,parties,media,homelife,musicarts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_interest_socialevents,
                container, false);
        /*FontsManager.initFormAssets(getActivity(), "fonts/lato.ttf");
        FontsManager.changeFonts(getActivity());*/
        FontsOverride.overrideFonts(getActivity(), view);
        SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor edit = sharedPrefces.edit();
        cse_cb = (CheckBox) view.findViewById(R.id.comedy_cb);
        hobbies_cb = (CheckBox) view.findViewById(R.id.hobbies_cb);
        parties_cb = (CheckBox) view.findViewById(R.id.parties_cb);
        media_cb = (CheckBox) view.findViewById(R.id.mediafilm_cb);
        homelife_cb = (CheckBox) view.findViewById(R.id.homelife_cb);
        musicarts_cb = (CheckBox) view.findViewById(R.id.musicarts_cb);


        cse_iv = (ImageView) view.findViewById(R.id.comedy_iv);
        hobbies_iv = (ImageView) view.findViewById(R.id.hobbies_iv);
        parties_ev = (ImageView) view.findViewById(R.id.parties_iv);
        media_iv = (ImageView) view.findViewById(R.id.mediafilm_iv);
        homelife_iv = (ImageView) view.findViewById(R.id.homelife_iv);
        musicarts_iv = (ImageView) view.findViewById(R.id.musicarts_iv);

        cse = sharedPrefces.getString("cse", "");
        hobbies = sharedPrefces.getString("hobbies", "");
        parties = sharedPrefces.getString("parties", "");
        media = sharedPrefces.getString("media", "");
        homelife = sharedPrefces.getString("homelife", "");
        musicarts = sharedPrefces.getString("musicarts", "");

        if (!cse.equals("")) {
            if (cse.equals("4")) {
                cse_cb.setChecked(true);
                cse_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                cse_iv.setColorFilter(cse_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            }

        }
        if (!hobbies.equals("")) {
            if (hobbies.equals("7")) {
                hobbies_cb.setChecked(true);

                hobbies_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                hobbies_iv.setColorFilter(hobbies_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            }

        }
        if (!parties.equals("")) {
            if (parties.equals("9")) {
                parties_cb.setChecked(true);

                parties_ev.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                parties_ev.setColorFilter(parties_ev.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            }

        }
        if (!media.equals("")) {
            if (media.equals("5")) {
                media_cb.setChecked(true);

                media_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                media_iv.setColorFilter(media_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            }

        }

        if (!homelife.equals("")) {
            if (homelife.equals("6")) {
                homelife_cb.setChecked(true);

                homelife_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                homelife_iv.setColorFilter(homelife_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            }

        }
        if (!musicarts.equals("")) {
            if (musicarts.equals("8")) {
                musicarts_cb.setChecked(true);

                musicarts_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                musicarts_iv.setColorFilter(parties_ev.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            }

        }
        cse_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    edit.putString("cse", "4");
                    edit.commit();
                    InterestActivity.list.add("comedy");
                    cse_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    cse_iv.setColorFilter(cse_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("comedy");
                    edit.putString("cse", "0");
                    edit.commit();
                    cse_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    cse_iv.setColorFilter(cse_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });


        hobbies_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    InterestActivity.list.add("hobbies");
                    edit.putString("hobbies", "7");
                    edit.commit();
                    hobbies_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    hobbies_iv.setColorFilter(hobbies_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("hobbies");
                    edit.putString("hobbies", "0");
                    edit.commit();
                    hobbies_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    hobbies_iv.setColorFilter(hobbies_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });


        parties_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    InterestActivity.list.add("parties");
                    edit.putString("parties", "9");
                    edit.commit();
                    parties_ev.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    parties_ev.setColorFilter(parties_ev.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("parties");
                    edit.putString("parties", "0");
                    edit.commit();
                    parties_ev.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    parties_ev.setColorFilter(parties_ev.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        musicarts_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    InterestActivity.list.add("music");
                    edit.putString("musicarts", "8");
                    edit.commit();
                    musicarts_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    musicarts_iv.setColorFilter(musicarts_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("music");
                    edit.putString("musicarts", "0");
                    edit.commit();
                    musicarts_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    musicarts_iv.setColorFilter(musicarts_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
        media_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    InterestActivity.list.add("media");
                    edit.putString("media", "5");
                    edit.commit();
                    media_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    media_iv.setColorFilter(media_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("media");
                    edit.putString("media", "0");
                    edit.commit();
                    media_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    media_iv.setColorFilter(media_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
        homelife_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    InterestActivity.list.add("homelife");
                    edit.putString("homelife", "6");
                    edit.commit();
                    homelife_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    homelife_iv.setColorFilter(homelife_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("homelife");
                    edit.putString("homelife", "0");
                    edit.commit();
                    homelife_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    homelife_iv.setColorFilter(homelife_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });


        ImageView next = (ImageView) view.findViewById(R.id.submit_iv);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReligionandHelalthEventsFragment bef = new ReligionandHelalthEventsFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, bef);
                ft.commit();
            }
        });
        return view;
    }
}
