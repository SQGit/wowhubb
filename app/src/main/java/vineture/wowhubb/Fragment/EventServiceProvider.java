package vineture.wowhubb.Fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sa90.materialarcmenu.ArcMenu;
import vineture.wowhubb.Fonts.FontsOverride;


public class EventServiceProvider extends Fragment {
    FloatingActionButton createevent_fab;
    public static ArcMenu arcMenu;

    public EventServiceProvider() {
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
        View view = inflater.inflate(vineture.wowhubb.R.layout.fragment_eventserviceprovider,
                container, false);
        FontsOverride.overrideFonts(getActivity(), view);
        Log.e("tag","123332provider");

        return view;

    }

}
