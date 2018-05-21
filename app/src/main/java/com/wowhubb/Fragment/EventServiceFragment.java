package com.wowhubb.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wowhubb.Activity.EventFeedDashboard;
import com.wowhubb.Activity.LandingPageActivity;
import com.wowhubb.EventServiceProvider.Activity.EventServiceProviderActivity;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.Nearbyeventsmodule.Activity.NearbyCategoryActivity;
import com.wowhubb.R;
import com.wowhubb.ShareBookEvents.Activity.SearchBookMainActivity;

import java.util.ArrayList;
import java.util.List;


public class EventServiceFragment extends Fragment {
    public EventServiceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("tag", "Ramya");
        View view = inflater.inflate(R.layout.fragment_eventservice,
                container, false);
        FontsOverride.overrideFonts(getActivity(), view);
        LinearLayout createevents_lv,searchproviders_lv,nearby_lv;


        createevents_lv=view.findViewById(R.id.createevents_lv);
        searchproviders_lv=view.findViewById(R.id.searchproviders_lv);
        nearby_lv=view.findViewById(R.id.nearby_lv);


        createevents_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EventServiceProviderActivity.class));
            }
        });


        searchproviders_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchBookMainActivity.class));
            }
        });


        nearby_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(getActivity(), NearbyCategoryActivity.class));


            }
        });

        return view;
    }



}
