package vineture.wowhubb.Nearbyeventsmodule;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.R;


public class EventDiscussionFragment extends Fragment {
    String token;
    RecyclerView recyclerView;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventdiscussion, container, false);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        token = sharedPreferences.getString("token", "");
        FontsOverride.overrideFonts(getActivity(), view);

        recyclerView = (RecyclerView) view.findViewById(R.id.listview);

        return view;
    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     */






}
