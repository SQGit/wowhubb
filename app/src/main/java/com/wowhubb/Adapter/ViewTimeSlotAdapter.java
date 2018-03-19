package com.wowhubb.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.Fragment.DayFragment;
import com.wowhubb.R;
import com.wowhubb.Utils.HttpUtils;
import com.wowhubb.data.DayContent;
import com.wowhubb.data.FeedItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ViewTimeSlotAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<DayContent> timeslotItems;
    Typeface lato;
    String iti_date1,iti_date2,iti_merge;
    Animation anim;
    private int day;
    LinearLayout lnr_del_data;

    public ViewTimeSlotAdapter(Activity activity, ArrayList<DayContent> timeslotItems, int day) {
        this.activity = activity;
        this.timeslotItems = timeslotItems;
    }


    @Override
    public int getCount() {
        return timeslotItems.size();
    }

    @Override
    public Object getItem(int location) {
        return timeslotItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_time_slot, null);
            viewHolder = new ViewHolder();
            viewHolder.position = position;
            FontsOverride.overrideFonts(activity, convertView);
            lato = Typeface.createFromAsset(activity.getAssets(), "fonts/lato.ttf");
            viewHolder.txt_itinerary_time=convertView.findViewById(R.id.txt_itinerary_time);
            viewHolder.txt_itinerary_val=convertView.findViewById(R.id.txt_itinerary_val);
            viewHolder.txt_facilitator=convertView.findViewById(R.id.txt_facilitator);
            viewHolder.txt_facilitator_val=convertView.findViewById(R.id.txt_facilitator_val);
            viewHolder.txt_topic_agenda=convertView.findViewById(R.id.txt_topic_agenda);
            viewHolder.txt_agenda_val=convertView.findViewById(R.id.txt_agenda_val);
            viewHolder.txt_event_location=convertView.findViewById(R.id.txt_event_location);
            viewHolder.txt_location_val=convertView.findViewById(R.id.txt_location_val);
            viewHolder.img_delete_slot=convertView.findViewById(R.id.img_delete_slot);
            viewHolder.lnr_del_data = convertView.findViewById(R.id.lnr_del_data);

           /* viewHolder.img_delete_slot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   //Toast.makeText(activity, "item to delete at position "+position, Toast.LENGTH_SHORT).show();
                    timeslotItems.remove(position);
                    //DayFragment.notifyAdapter();
                    DayFragment.dialog.dismiss();

                }
            });*/



           viewHolder.img_delete_slot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StartSmartAnimation.startAnimation(viewHolder.lnr_del_data, AnimationType.SlideOutRight,1000,0,true);
                    timeslotItems.remove(position);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // listView.setAdapter(mAdapter);
                        }
                    },1000);
                }
            });




            viewHolder.txt_itinerary_time.setTypeface(lato);
            viewHolder.txt_itinerary_val.setTypeface(lato);
            viewHolder.txt_facilitator.setTypeface(lato);
            viewHolder.txt_facilitator_val.setTypeface(lato);
            viewHolder.txt_topic_agenda.setTypeface(lato);
            viewHolder.txt_agenda_val.setTypeface(lato);
            viewHolder.txt_event_location.setTypeface(lato);
            viewHolder.txt_location_val.setTypeface(lato);



            DayContent dayContent = timeslotItems.get(position);
            //viewHolder.txt_itinerary_val.setText(dayContent.getItinerary_start_time());
            viewHolder.txt_facilitator_val.setText(dayContent.getFacilitator());
            viewHolder.txt_agenda_val.setText(dayContent.getEvent_agenta());
            viewHolder.txt_location_val.setText(dayContent.getLocation_venue());



            iti_date1=dayContent.getItinerary_start_time();
            iti_date2=dayContent.getItinerary_end_time();
            iti_merge=iti_date1+" - "+iti_date2;
            viewHolder.txt_itinerary_val.setText(iti_merge);



        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }




        return convertView;
    }

    static class ViewHolder {
        int position;
        public TextView txt_day_count,txt_time_slot;
        public TextView txt_itinerary_time,txt_itinerary_val,txt_facilitator,txt_facilitator_val,txt_topic_agenda,txt_agenda_val,txt_event_location,txt_location_val;
        public LinearLayout lnr_del_data;
        public ImageView img_delete_slot;


    }

}
