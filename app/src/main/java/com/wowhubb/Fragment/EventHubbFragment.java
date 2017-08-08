package com.wowhubb.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wowhubb.Activity.EventFeedDashboard;
import com.wowhubb.R;


public class EventHubbFragment extends Fragment {

    public EventHubbFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_eventfeed, container, false);


        View view = inflater.inflate(R.layout.fragment_eventfeed,
                container, false);

       // EventFeedDashboard.arcMenu.setVisibility(View.VISIBLE);
        return view;

    }

}
