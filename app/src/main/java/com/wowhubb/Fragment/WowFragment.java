package com.wowhubb.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wowhubb.Activity.EventFeedDashboard;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;


public class WowFragment extends Fragment {

    public WowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wowtag,
                container, false);

        FontsOverride.overrideFonts(getActivity(), view);

        return view;
    }


}
