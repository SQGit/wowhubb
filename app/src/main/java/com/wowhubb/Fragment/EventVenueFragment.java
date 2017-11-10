package com.wowhubb.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.wowhubb.Activity.CreateEventActivity;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

public class EventVenueFragment extends Fragment {


    public static TextInputLayout till_address, till_city, till_state, til_zipcode;
    public static TextInputLayout til_eventname;
    public static CheckBox oneday, twoday, threeday;
    public static String checkboxstatus;
    public static EditText venuename, address, city, state, zipcode;
    Typeface lato;

    public static EventVenueFragment newInstance(int page, boolean isLast) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        if (isLast)
            args.putBoolean("isLast", true);
        final EventVenueFragment fragment = new EventVenueFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_eventvenue, container, false);
        FontsOverride.overrideFonts(getActivity(), view);
        CreateEventActivity.skiptv.setVisibility(View.INVISIBLE);
        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");
        til_eventname = (TextInputLayout) view.findViewById(R.id.til_eventvenue);
        till_address = (TextInputLayout) view.findViewById(R.id.til_address);
        till_city = (TextInputLayout) view.findViewById(R.id.til_city);
        till_state = (TextInputLayout) view.findViewById(R.id.til_state);
        til_zipcode = (TextInputLayout) view.findViewById(R.id.til_zipcode);

        venuename = view.findViewById(R.id.eventvenue_et);
        address = view.findViewById(R.id.adddress_et);
        city = view.findViewById(R.id.city_et);
        state = view.findViewById(R.id.state_et);
        zipcode = view.findViewById(R.id.zipcode_et);


        oneday = (CheckBox) view.findViewById(R.id.oneday_cb);
        twoday = view.findViewById(R.id.twoday_cb);
        threeday = view.findViewById(R.id.threeday_cb);

        til_eventname.setTypeface(lato);
        till_address.setTypeface(lato);
        till_city.setTypeface(lato);
        till_state.setTypeface(lato);
        til_zipcode.setTypeface(lato);


        return view;
    }


}