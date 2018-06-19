package vineture.wowhubb.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.R;


public class EventServiceNearbyEnents extends Fragment {

    public EventServiceNearbyEnents()
    {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_eventfeed, container, false);
        View view = inflater.inflate(R.layout.fragment_eventservicenearbyevents,
                container, false);
        FontsOverride.overrideFonts(getActivity(), view);


        return view;

    }

}
