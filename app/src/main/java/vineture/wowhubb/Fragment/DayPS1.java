package vineture.wowhubb.Fragment;

/**
 * Created by Salman on 13-02-2018.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import vineture.wowhubb.FeedsData.Programschedule;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.R;

import java.util.ArrayList;

public class DayPS1 extends Fragment {
    public static ArrayList<Programschedule> ps = new ArrayList<>();
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.days_ps, container, false);
        FontsOverride.overrideFonts(getActivity(), view);

        listView = view.findViewById(R.id.scheduleview);

        Bundle b = getArguments();
        ps = b.getParcelableArrayList("program");

        Log.e("tag", "--------ggggggggggggg111111------->" + ps);
        //   Log.e("tag", "--------ggggggggggggg111111------->" + ViewProgramSchedule.ps);
//        Log.e("tag", "--------ggggggggggggg111111------->" + ViewProgramSchedule.ps.get(0).get(0).getLocation());

        ScheduleListAdapter adapter = new ScheduleListAdapter(getActivity(), ps);
        listView.setAdapter(adapter);

        return view;
    }

    class ScheduleListAdapter extends BaseAdapter {


        public ArrayList<Programschedule> pss = new ArrayList<>();
        SharedPreferences.Editor editor;
        String token, userId;
        Dialog dialog;
        private Activity activity;
        private LayoutInflater inflater;


        public ScheduleListAdapter(Activity activity, ArrayList<Programschedule> pss) {
            this.activity = activity;
            this.pss = pss;
        }


        @Override
        public int getCount() {
            return pss.size();
        }

        @Override
        public Object getItem(int location) {
            return pss.get(location);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.view_programschedule, null);
                viewHolder = new ViewHolder();
                viewHolder.position = position;
                  FontsOverride.overrideFonts(activity, convertView);
                Log.e("tag", "--------valllllaaaaaaaa------->");

                //  Log.e("tag", "--------valllll------->" + pss.get(0).getLocation());
                viewHolder.locationtv = convertView.findViewById(R.id.location_tv);
                viewHolder.topictv = convertView.findViewById(R.id.topic_tv);
                viewHolder.timetv = convertView.findViewById(R.id.time_tv);
                viewHolder.facilitatortv = convertView.findViewById(R.id.faclilitator_tv);

                viewHolder.locationtv.setText(pss.get(position).getLocation());
                viewHolder.topictv.setText(pss.get(position).getAgenda());
                viewHolder.timetv.setText(pss.get(position).getItystarttime() + " - " + pss.get(position).getItyendtime());
                viewHolder.facilitatortv.setText(pss.get(position).getFacilitator());

                convertView.setTag(viewHolder);


            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            return convertView;
        }

        class ViewHolder {
            TextView locationtv, topictv, timetv, facilitatortv;
            int position;
            ImageView profilePic;


        }

    }

}
