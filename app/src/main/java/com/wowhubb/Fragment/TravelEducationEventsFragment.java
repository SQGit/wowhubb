package com.wowhubb.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.wowhubb.Activity.CreateEventActivity;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;


/**
 * Created by Ramya on 24-07-2017.
 */

public class TravelEducationEventsFragment extends Fragment {
    CheckBox cse_cb, hobbies_cb, parties_cb;
    ImageView cse_iv,hobbies_iv,parties_ev;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_travelandeducationevents,
                container, false);
        FontsOverride.overrideFonts(getActivity(),view);

        ImageView next = (ImageView) view.findViewById(R.id.submit_iv);

        cse_cb = (CheckBox)view.findViewById(R.id.comedy_cb);
        hobbies_cb = (CheckBox)view.findViewById(R.id.hobbies_cb);
        parties_cb = (CheckBox)view.findViewById(R.id.musicarts_cb);

        cse_iv = (ImageView)view.findViewById(R.id.comedy_iv);
        hobbies_iv = (ImageView)view.findViewById(R.id.hobbies_iv);
        parties_ev = (ImageView)view.findViewById(R.id.musicarts_iv);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateEventActivity.class));
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });



        cse_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    cse_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    cse_iv.setColorFilter(cse_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
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

        return view;
    }
}
