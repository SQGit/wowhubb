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
import android.widget.TextView;

import com.wowhubb.Activity.InterestActivity;
import com.wowhubb.Activity.SplashActivity;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;


/**
 * Created by Ramya on 24-07-2017.
 */

public class BusinessEventsFragment extends Fragment {

    ImageView bne_iv, ste_iv, pte_iv;
    CheckBox bne_cb, ste_cb, pte_cb;
    String bne, ste,pte;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_interest, container, false);
        SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor edit = sharedPrefces.edit();
        ImageView next = (ImageView) view.findViewById(R.id.submit_iv);
        FontsOverride.overrideFonts(getActivity(), view);
        bne_cb = (CheckBox) view.findViewById(R.id.bne_cb);
        ste_cb = (CheckBox) view.findViewById(R.id.ste_cb);
        pte_cb = (CheckBox) view.findViewById(R.id.pte_cb);

        bne_iv = (ImageView) view.findViewById(R.id.bne_iv);
        ste_iv = (ImageView) view.findViewById(R.id.ste_iv);
        pte_iv = (ImageView) view.findViewById(R.id.pte_iv);
        bne = sharedPrefces.getString("bne", "");
        ste = sharedPrefces.getString("ste", "");
        pte = sharedPrefces.getString("pte", "");
        Log.e("tag", "111111111--------" + ste);

        if (!bne.equals("")) {
            if (bne.equals("1"))
            {
                bne_cb.setChecked(true);
                bne_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                bne_iv.setColorFilter(bne_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


            }

        }
        if (!ste.equals("")) {
            if (ste.equals("2")) {
                ste_cb.setChecked(true);

                ste_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                ste_iv.setColorFilter(bne_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

            }

        }
        if (!pte.equals("")) {
            if (pte.equals("3")) {
                pte_cb.setChecked(true);

                pte_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                pte_iv.setColorFilter(bne_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            }

        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tag","LIst----"+InterestActivity.list);
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
                    edit.putString("bne", "1");
                    edit.commit();
                    InterestActivity.list.add("business");
                    bne_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    bne_iv.setColorFilter(bne_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("business");
                    edit.putString("bne", "0");
                    edit.commit();
                    bne_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    bne_iv.setColorFilter(bne_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
        ste_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    edit.putString("ste", "2");
                    edit.commit();
                    InterestActivity.list.add("startup");
                    ste_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    ste_iv.setColorFilter(ste_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("startup");
                    edit.putString("ste", "0");
                    edit.commit();
                    ste_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    ste_iv.setColorFilter(ste_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        pte_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    InterestActivity.list.add("professional");
                    edit.putString("pte", "3");
                    edit.commit();
                    pte_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest_color));
                    pte_iv.setColorFilter(pte_iv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    InterestActivity.list.remove("professional");
                    edit.putString("pte", "0");
                    edit.commit();
                    pte_iv.setBackground(getResources().getDrawable(R.drawable.selector_interest));
                    pte_iv.setColorFilter(pte_iv.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });


        return view;
    }
}
