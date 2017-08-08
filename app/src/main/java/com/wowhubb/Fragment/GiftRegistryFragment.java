package com.wowhubb.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.TextView;

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

public class GiftRegistryFragment extends Fragment {

    private TextView callToAction, subaction;
    Dialog dialog;
    CheckBox cb1, cb2, cb3, cb4;
    TextInputLayout til_giftregistry, til_donation;
    Typeface lato;

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
        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");
        til_giftregistry = (TextInputLayout) view.findViewById(R.id.til_giftregistry);
        til_donation = (TextInputLayout) view.findViewById(R.id.til_donation);
        callToAction = (TextView) view.findViewById(R.id.tv_calltoaction);
        callToAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        til_donation.setTypeface(lato);
        til_giftregistry.setTypeface(lato);


        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_call_to_action);
        View view1 = dialog.getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(dialog.getContext(), view1);
        cb1 = (CheckBox) dialog.findViewById(R.id.cb1);
        cb2 = (CheckBox) dialog.findViewById(R.id.cb2);
        cb3 = (CheckBox) dialog.findViewById(R.id.cb3);
        cb4 = (CheckBox) dialog.findViewById(R.id.cb4);
        //  subaction=(TextView) dialog.findViewById(R.id.tv_calltoaction);
       /* subaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        })*/
        ;

        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                    cb2.setChecked(false);
                cb3.setChecked(false);
                cb4.setChecked(false);
            }
        });


        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    cb1.setChecked(false);
                cb3.setChecked(false);
                cb4.setChecked(false);
            }
        });
        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    cb1.setChecked(false);
                cb2.setChecked(false);
                cb4.setChecked(false);
            }
        });

        cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    cb1.setChecked(false);
                cb3.setChecked(false);
                cb2.setChecked(false);
            }
        });

        return view;
    }


}