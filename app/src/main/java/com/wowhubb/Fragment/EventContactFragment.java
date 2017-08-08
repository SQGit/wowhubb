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

public class EventContactFragment extends Fragment {

    private TextView callToAction;
    TextInputLayout til_evntinfo, til_name, til_phone, til_email, til_msg;
    Typeface lato;

    public static EventContactFragment newInstance(int page, boolean isLast) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        if (isLast)
            args.putBoolean("isLast", true);
        final EventContactFragment fragment = new EventContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_eventcontact, container, false);
        FontsOverride.overrideFonts(getActivity(), view);

        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");

        til_evntinfo = (TextInputLayout) view.findViewById(R.id.til_eventinfo);
        til_name = (TextInputLayout) view.findViewById(R.id.til_name);
        til_phone = (TextInputLayout) view.findViewById(R.id.til_phone);
        til_email = (TextInputLayout) view.findViewById(R.id.til_email);
        til_msg = (TextInputLayout) view.findViewById(R.id.til_msg);

        til_evntinfo.setTypeface(lato);
        til_name.setTypeface(lato);
        til_phone.setTypeface(lato);
        til_email.setTypeface(lato);
        til_msg.setTypeface(lato);


        return view;
    }


}