package vineture.wowhubb.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import vineture.wowhubb.EventServiceProvider.Activity.EventServiceProviderActivity;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.Nearbyeventsmodule.Activity.NearbyCategoryActivity;
import vineture.wowhubb.ShareBookEvents.Activity.SearchBookMainActivity;


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
        View view = inflater.inflate(vineture.wowhubb.R.layout.fragment_eventservice,
                container, false);
        FontsOverride.overrideFonts(getActivity(), view);
        LinearLayout createevents_lv,searchproviders_lv,nearby_lv;


        createevents_lv=view.findViewById(vineture.wowhubb.R.id.createevents_lv);
        searchproviders_lv=view.findViewById(vineture.wowhubb.R.id.searchproviders_lv);
        nearby_lv=view.findViewById(vineture.wowhubb.R.id.nearby_lv);


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
