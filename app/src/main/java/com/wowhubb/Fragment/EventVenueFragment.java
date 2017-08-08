package com.wowhubb.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

public class EventVenueFragment extends Fragment {

    private TextView lblPage;
    TextInputLayout til_eventname,till_address,till_city,till_state,til_zipcode,til_from,til_to;
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
        FontsOverride.overrideFonts(getActivity(),view);
        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");
        til_eventname = (TextInputLayout) view.findViewById(R.id.til_eventvenue);
        till_address = (TextInputLayout) view.findViewById(R.id.til_address);
        till_city = (TextInputLayout) view.findViewById(R.id.til_city);
        till_state = (TextInputLayout) view.findViewById(R.id.til_state);
        til_zipcode = (TextInputLayout) view.findViewById(R.id.til_zipcode);
        til_from = (TextInputLayout) view.findViewById(R.id.til_from);
        til_to = (TextInputLayout) view.findViewById(R.id.til_to);

        til_eventname.setTypeface(lato);
        till_address.setTypeface(lato);
        till_city.setTypeface(lato);
        till_state.setTypeface(lato);
        til_zipcode.setTypeface(lato);
        til_to.setTypeface(lato);
        til_from.setTypeface(lato);
        return view;
    }

  /* @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final int page = getArguments().getInt("page", 0);
        if (getArguments().containsKey("isLast"))
            lblPage.setText("You're done!");
        else
            lblPage.setText(Integer.toString(page));
    }*/
}