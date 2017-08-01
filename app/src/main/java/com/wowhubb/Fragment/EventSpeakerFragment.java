package com.wowhubb.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

public class EventSpeakerFragment extends Fragment {

    private TextView lblPage;

    public static EventSpeakerFragment newInstance(int page, boolean isLast) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        if (isLast)
            args.putBoolean("isLast", true);
        final EventSpeakerFragment fragment = new EventSpeakerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_eventspeaker, container, false);
        FontsOverride.setDefaultFont(getContext(), "DEFAULT", "fonts/lato.ttf");
        return view;
    }


}