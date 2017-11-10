package com.wowhubb.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.wowhubb.Activity.EventFeedDashboard;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;


public class MyNetworkAddContact extends Fragment {

    public static MyNetworkAddContact newInstance() {
        MyNetworkAddContact fragment = new MyNetworkAddContact();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.mynetwork_addcontact, container, false);
        FontsOverride.overrideFonts(getActivity(), view);
        return view;
    }

}
