package com.wowhubb.Fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;


public class EventServiceVenueFragment extends Fragment {

    public EventServiceVenueFragment()
    {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("tag", "zz1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("tag", "zz2");

        View view = inflater.inflate(R.layout.fragment_eventhub,
                container, false);
        Log.e("tag", "zz13");


        //FontsOverride.overrideFonts(getActivity(), view);
        Log.e("tag","123332222222");

        return view;

    }

}
