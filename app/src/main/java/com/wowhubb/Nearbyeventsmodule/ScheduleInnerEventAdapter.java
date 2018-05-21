package com.wowhubb.Nearbyeventsmodule;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wowhubb.Nearbyeventsmodule.Model.ScheduleEventModelPojo;
import com.wowhubb.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guna on 29-11-2017.
 */

public class ScheduleInnerEventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    private List<ScheduleEventModelPojo> scheduleEventModelPojos;
    SharedPreferences.Editor editor;
    String token;
    Typeface lato;
    Dialog dialog;




    public ScheduleInnerEventAdapter(Context context, ArrayList<ScheduleEventModelPojo> scheduleEventModelPojos) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.scheduleEventModelPojos = scheduleEventModelPojos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_schedule_adapter, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final ScheduleEventModelPojo current=scheduleEventModelPojos.get(position);


        myHolder.event_time.setText(current.eventInnerTime);
        myHolder.event_name.setText(current.eventInnerEvent);
        myHolder.event_who.setText(current.eventInnerWho);


        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");



        if(position %2 == 1)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#F1F1F1"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
    }






    @Override
    public int getItemCount() {
        return scheduleEventModelPojos.size();
    }





    @Override
    public int getItemViewType(int position) {
        return position;

    }

    public interface NearByEventListener {
    }


    class MyHolder extends RecyclerView.ViewHolder {
       public  TextView event_time,event_name,event_who;
       public ImageView event_where;



        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/segoeui.ttf");
            event_time = (TextView) itemView.findViewById(R.id.event_time);
            event_name=(TextView)itemView.findViewById(R.id.event_name);
            event_who = (TextView) itemView.findViewById(R.id.event_who);
            event_where=(ImageView) itemView.findViewById(R.id.event_where);



            event_time.setTypeface(tf);
            event_name.setTypeface(tf);
            event_who.setTypeface(tf);


        }
    }
}
