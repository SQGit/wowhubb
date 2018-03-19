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
import android.widget.HorizontalScrollView;

import com.wang.avi.AVLoadingIndicatorView;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

import java.util.ArrayList;


public class HighlightEventFragment extends Fragment {
    String token;
    RecyclerView recycler_eventhighlight;
    AVLoadingIndicatorView av_loader;
    private ArrayList<HighlightEventModelPojo> staticArrayListforHighlights=new ArrayList<>();
    HighlightEventAdapter nearByEventHighlightsAdapter;
    HorizontalScrollView imaqe_scroll;


    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventhighlights, container, false);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        token = sharedPreferences.getString("token", "");
        FontsOverride.overrideFonts(getActivity(), view);
        recycler_eventhighlight = (RecyclerView) view.findViewById(R.id.recycler_eventhighlight);
        av_loader = (AVLoadingIndicatorView)view. findViewById(R.id.avi);
        imaqe_scroll=view.findViewById(R.id.imaqe_scroll);


        imaqe_scroll.postDelayed(new Runnable() {
            @Override
            public void run() {
                imaqe_scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 100L);

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
        HighlightEventModelPojo item1, item2, item3;

        String s1=getResources().getDrawable(R.drawable.speaker1).toString();
        String s2=getResources().getDrawable(R.drawable.speaker2).toString();
        String s3=getResources().getDrawable(R.drawable.speaker3).toString();



        item1 = new HighlightEventModelPojo("12 Dr. TD Jake", "Dr. TD Jake", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.","www.wowhubb.com/drtdjake",s1);
        staticArrayListforHighlights.add(item1);


        item2 = new HighlightEventModelPojo("042 Dr. Segun", "Dr. Segun", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.","www.wowhubb.com/drtdsegun",s2);
        staticArrayListforHighlights.add(item2);

        item3 = new HighlightEventModelPojo("56 Dr. TD Jake", "Dr. TD Jake", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.","www.wowhubb.com/drtdjake",s3);
        staticArrayListforHighlights.add(item3);

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
            Log.e("tag","POST EXECUTE"+staticArrayListforHighlights.size());



            for (int i1 = 0; i1 < staticArrayListforHighlights.size(); i1++) {


                nearByEventHighlightsAdapter = new HighlightEventAdapter(getActivity(), staticArrayListforHighlights);
                recycler_eventhighlight.setAdapter(nearByEventHighlightsAdapter);
                nearByEventHighlightsAdapter.notifyDataSetChanged();
                recycler_eventhighlight.setLayoutManager(new LinearLayoutManager(getActivity()));

            }

        }

    }

}
