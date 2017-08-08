package com.wowhubb.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

public class ProgramScheduleFragment extends Fragment {

    Typeface lato;
    TextInputLayout til_starttime, til_endtime, til_food, til_organiser;
    TextInputLayout til_starttime0, til_endtime0, til_food0, til_organiser0;
    TextInputLayout til_starttime1, til_endtime1, til_food1, til_organiser1;
    TextView day1, day2, day3, day4;
    LinearLayout direction1, direction2, direction3;

    public static ProgramScheduleFragment newInstance(int page, boolean isLast) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        if (isLast)
            args.putBoolean("isLast", true);
        final ProgramScheduleFragment fragment = new ProgramScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_programschedule, container, false);
        FontsOverride.overrideFonts(getActivity(), view);
        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");
        til_starttime = (TextInputLayout) view.findViewById(R.id.til_starttime);
        til_endtime = (TextInputLayout) view.findViewById(R.id.til_endtime);
        til_food = (TextInputLayout) view.findViewById(R.id.til_food);
        til_organiser = (TextInputLayout) view.findViewById(R.id.til_organiser);

        til_starttime0 = (TextInputLayout) view.findViewById(R.id.til_starttime0);
        til_endtime0 = (TextInputLayout) view.findViewById(R.id.til_endtime0);
        til_food0 = (TextInputLayout) view.findViewById(R.id.til_food0);
        til_organiser0 = (TextInputLayout) view.findViewById(R.id.til_organiser0);

        til_starttime1 = (TextInputLayout) view.findViewById(R.id.til_starttime1);
        til_endtime1 = (TextInputLayout) view.findViewById(R.id.til_endtime1);
        til_food1 = (TextInputLayout) view.findViewById(R.id.til_food1);
        til_organiser1 = (TextInputLayout) view.findViewById(R.id.til_organiser1);

        day1 = (TextView) view.findViewById(R.id.day1_tv);
        day2 = (TextView) view.findViewById(R.id.day2_tv);
        day3 = (TextView) view.findViewById(R.id.day3_tv);
        day4 = (TextView) view.findViewById(R.id.day4_tv);

        direction1 = view.findViewById(R.id.direc_lv1);
        direction2 = view.findViewById(R.id.direc_lv2);
        direction3 = view.findViewById(R.id.direc_lv3);

        day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                day1.setTextColor(Color.parseColor("#3c3c3c"));
                day2.setTextColor(Color.parseColor("#e91e63"));

            }
        });

        til_starttime.setTypeface(lato);
        til_endtime.setTypeface(lato);
        til_food.setTypeface(lato);
        til_organiser.setTypeface(lato);

        til_starttime0.setTypeface(lato);
        til_endtime0.setTypeface(lato);
        til_food0.setTypeface(lato);
        til_organiser0.setTypeface(lato);

        til_starttime1.setTypeface(lato);
        til_endtime1.setTypeface(lato);
        til_food1.setTypeface(lato);
        til_organiser1.setTypeface(lato);

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_getdirection);
        final LinearLayout newvenue = dialog.findViewById(R.id.newvenue_lv);
        final CheckBox newvenue_cb = dialog.findViewById(R.id.newvenue_cb);
        final ImageView sub = dialog.findViewById(R.id.submit_iv);

        final ImageView close = dialog.findViewById(R.id.closeiv);
        View view1 = dialog.getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(dialog.getContext(), view1);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        newvenue_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    newvenue.setVisibility(View.VISIBLE);
                } else {
                    newvenue.setVisibility(View.GONE);
                }

            }
        });
        direction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();

            }
        });

        direction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();

            }
        });

        direction3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();

            }
        });
        return view;
    }


}