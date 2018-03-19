package com.wowhubb.Nearbyeventsmodule;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wang.avi.AVLoadingIndicatorView;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.Fragment.DayFragment;
import com.wowhubb.Fragment.ProgramScheduleFragmentNew;
import com.wowhubb.R;

import java.util.ArrayList;


public class ScheduleInnerEventFragment extends Fragment {
    String token;
    RecyclerView recycler_eventhighlight;
    AVLoadingIndicatorView av_loader;
    private ArrayList<ScheduleEventModelPojo> staticArrayListforschedule=new ArrayList<>();
    ScheduleInnerEventAdapter scheduleInnerEventAdapter;



    // newInstance constructor for creating fragment with arguments
    public static ScheduleInnerEventFragment newInstance(int page, String title) {

        ScheduleInnerEventFragment fragmentFirst = new ScheduleInnerEventFragment();
        Bundle args = new Bundle();
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }


    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_inner, container, false);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        token = sharedPreferences.getString("token", "");
        FontsOverride.overrideFonts(getActivity(), view);
        recycler_eventhighlight = (RecyclerView) view.findViewById(R.id.recycler_eventhighlight);
        av_loader = (AVLoadingIndicatorView)view. findViewById(R.id.avi);

        //load static data to method:
        loadStaticEventsHighlights();



        //call NearbyEvents Separete Activity Asynctask:
        new callEventHighlights().execute();


        return view;
    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     */


    private void loadStaticEventsHighlights() {
        ScheduleEventModelPojo item1, item2, item3;

        staticArrayListforschedule.clear();

        item1 = new ScheduleEventModelPojo("7:00AM- 8:00AM", "Welcome Registration", "Floyer Counter","");
        staticArrayListforschedule.add(item1);


        item2 = new ScheduleEventModelPojo("8:00AM- 9:00AM", "BBQ- Breakfast", "Elex Young","");
        staticArrayListforschedule.add(item2);

        item3 = new ScheduleEventModelPojo("10:00AM- 11:00AM", "Fashion Catwalk1", "Organizer","");
        staticArrayListforschedule.add(item3);

    }



    private class callEventHighlights extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            av_loader.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(String... strings) {
            Log.e("tag","DO IN BACKGROUND");
            return null;
        }


        @Override
        protected void onPostExecute(String jsonstr) {
            av_loader.setVisibility(View.GONE);
            super.onPostExecute(jsonstr);
            Log.e("tag","POST EXECUTE"+staticArrayListforschedule.size());



            for (int i1 = 0; i1 < staticArrayListforschedule.size(); i1++) {


                scheduleInnerEventAdapter = new ScheduleInnerEventAdapter(getActivity(), staticArrayListforschedule);
                recycler_eventhighlight.setAdapter(scheduleInnerEventAdapter);
                scheduleInnerEventAdapter.notifyDataSetChanged();
                recycler_eventhighlight.setLayoutManager(new LinearLayoutManager(getActivity()));

            }

        }

    }

}
