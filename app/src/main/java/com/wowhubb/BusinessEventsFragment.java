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

public class BusinessEventsFragment extends Fragment {

    ImageView bne_iv, ste_iv, pte_iv;
    CheckBox bne_cb, ste_cb, pte_cb;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.activity_interest,
                container, false);
        ImageView next = (ImageView) view.findViewById(R.id.submit_iv);
        //FontsManager.initFormAssets(getActivity(), "fonts/lato.ttf");
      //  FontsManager.changeFonts(getActivity());
        bne_cb = (CheckBox)view.findViewById(R.id.bne_cb);
        ste_cb = (CheckBox)view.findViewById(R.id.ste_cb);
        pte_cb = (CheckBox)view.findViewById(R.id.pte_cb);

        bne_iv = (ImageView)view.findViewById(R.id.bne_iv);
        ste_iv = (ImageView)view.findViewById(R.id.ste_iv);
        pte_iv = (ImageView)view.findViewById(R.id.pte_iv);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SocialEventsFragment bef = new SocialEventsFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, bef);
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                ft.commit();
            }
        });



        bne_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    bne_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    bne_iv.setColorFilter(bne_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    bne_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    bne_iv.setColorFilter(bne_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
        ste_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    ste_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    ste_iv.setColorFilter(bne_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    ste_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    ste_iv.setColorFilter(bne_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        pte_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    pte_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    pte_iv.setColorFilter(bne_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    pte_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    pte_iv.setColorFilter(bne_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });






        return view;
    }
}
