package com.wowhubb;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.sloop.fonts.FontsManager;

/**
 * Created by Ramya on 24-07-2017.
 */

public class ReligionandHelalthEventsFragment extends Fragment {
    CheckBox cse_cb, hobbies_cb, parties_cb;
    ImageView cse_iv,hobbies_iv,parties_ev;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_relegionandhealthevent,
                container, false);
       /* FontsManager.initFormAssets(getActivity(), "fonts/lato.ttf");
        FontsManager.changeFonts(getActivity());*/
        ImageView next = (ImageView) view.findViewById(R.id.submit_iv);
        cse_cb = (CheckBox)view.findViewById(R.id.religion_cb);
        hobbies_cb = (CheckBox)view.findViewById(R.id.food_cb);
        parties_cb = (CheckBox)view.findViewById(R.id.sports_cb);

        cse_iv = (ImageView)view.findViewById(R.id.comedy_iv);
        hobbies_iv = (ImageView)view.findViewById(R.id.hobbies_iv);
        parties_ev = (ImageView)view.findViewById(R.id.sports_iv);


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
