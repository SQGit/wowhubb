package com.wowhubb.Fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowhubb.Activity.ViewMoreDetailspage;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;


public class ViewHighlightFragment extends Fragment {
    String token;
    RecyclerView recyclerView;
    ImageView highiv;
    TextView eventname_tv, eventdesc_tv, guesttype_tv, speakername_tv;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_highlight, container, false);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        token = sharedPreferences.getString("token", "");
        FontsOverride.overrideFonts(getActivity(), view);

        highiv = view.findViewById(R.id.highlight_iv);
        eventdesc_tv = view.findViewById(R.id.desc_tv);
        eventname_tv = view.findViewById(R.id.eventname_tv);
        guesttype_tv = view.findViewById(R.id.guesttype_tv);
        speakername_tv = view.findViewById(R.id.speakername_tv);

        if (ViewMoreDetailspage.eventname != null && !ViewMoreDetailspage.eventname.equals("null")) {
            eventname_tv.setText(ViewMoreDetailspage.eventname);
        }
        if (ViewMoreDetailspage.eventspeakeractivities1 != null && !ViewMoreDetailspage.eventspeakeractivities1.equals("null")) {
            eventdesc_tv.setText(ViewMoreDetailspage.eventspeakeractivities1);
        }
        if (ViewMoreDetailspage.eventspeakername1 != null && !ViewMoreDetailspage.eventspeakername1.equals("null")) {
            speakername_tv.setText(ViewMoreDetailspage.eventspeakername1);
        }
        if (ViewMoreDetailspage.eventguesttype1 != null && !ViewMoreDetailspage.eventguesttype1.equals("null")) {
            guesttype_tv.setText(ViewMoreDetailspage.eventguesttype1);
        }

        if (ViewMoreDetailspage.highligh1 != null && !ViewMoreDetailspage.highligh1.equals("null")) {

            Glide.with(getActivity()).load("http://104.197.80.225:3010/wow/media/event/" + ViewMoreDetailspage.highligh1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .into(highiv);

        }

        return view;
    }


}
