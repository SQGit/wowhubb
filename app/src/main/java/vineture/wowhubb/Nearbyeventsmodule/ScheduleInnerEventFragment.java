package vineture.wowhubb.Nearbyeventsmodule;

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
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.Nearbyeventsmodule.Database_Helper.DatabaseHandler;
import vineture.wowhubb.Nearbyeventsmodule.Model.ScheduleEventModelPojo;

import java.util.ArrayList;
import java.util.List;


public class ScheduleInnerEventFragment extends Fragment {
    String token;
    RecyclerView recycler_eventhighlight;
    AVLoadingIndicatorView av_loader;
    private ArrayList<ScheduleEventModelPojo> staticArrayListforschedule=new ArrayList<>();
    ScheduleInnerEventAdapter scheduleInnerEventAdapter;
    ScheduleEventModelPojo dbscheduleEventModelPojo;


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
        View view = inflater.inflate(vineture.wowhubb.R.layout.fragment_schedule_inner, container, false);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        token = sharedPreferences.getString("token", "");
        FontsOverride.overrideFonts(getActivity(), view);
        recycler_eventhighlight = (RecyclerView) view.findViewById(vineture.wowhubb.R.id.recycler_eventhighlight);
        av_loader = (AVLoadingIndicatorView)view. findViewById(vineture.wowhubb.R.id.avi);

        //call NearbyEvents Separete Activity Asynctask:
        new callEventHighlights().execute();
        return view;
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

            DatabaseHandler db = new DatabaseHandler(getActivity());
            staticArrayListforschedule.clear();
            List<ScheduleEventModelPojo> list = db.getNearbyEventSchedule();

            Log.e("tag","View Details"+list.size());

            for (int i = 0; i < list.size(); i++) {
                dbscheduleEventModelPojo = list.get(i);
                Log.e("tag", " ADAAV1" + dbscheduleEventModelPojo.eventInnerTime);
                Log.e("tag", " ADAAV2" + dbscheduleEventModelPojo.eventInnerWho);
                Log.e("tag", " ADAAV3" + dbscheduleEventModelPojo.eventInnerEvent);
                Log.e("tag", " ADAAV4" + dbscheduleEventModelPojo.eventInnerWhere);

                staticArrayListforschedule.add(dbscheduleEventModelPojo);
            }

                scheduleInnerEventAdapter = new ScheduleInnerEventAdapter(getActivity(), staticArrayListforschedule);
                recycler_eventhighlight.setAdapter(scheduleInnerEventAdapter);
                scheduleInnerEventAdapter.notifyDataSetChanged();
                recycler_eventhighlight.setLayoutManager(new LinearLayoutManager(getActivity()));
            }

        }

    }


