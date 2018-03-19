package com.wowhubb.wowtag.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.wowhubb.R;
import com.wowhubb.wowtag.Model.WowtagModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guna on 29-11-2017.
 */

public class WowtagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    private List<WowtagModel> wowtagModels;
    SharedPreferences.Editor editor;
    String token;
    Typeface lato;
    Dialog dialog;




    public WowtagAdapter(Context context, ArrayList<WowtagModel> wowtagModels) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.wowtagModels = wowtagModels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.wowtag_adapter, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final WowtagModel current=wowtagModels.get(position);

        myHolder.wowtag_name.setText(current.wowtagName);
        myHolder.wowtag_description_cont.setText(current.wowtagDescription);
        String wow_video="http://104.197.80.225:3010/wow/media/event/"+current.wowtagVideo;




      /*  myHolder.event_time.setText(current.eventInnerTime);
        myHolder.event_name.setText(current.eventInnerEvent);
        myHolder.event_who.setText(current.eventInnerWho);
*/


        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");

    }






    @Override
    public int getItemCount() {
        return wowtagModels.size();
    }





    @Override
    public int getItemViewType(int position) {
        return position;

    }

    public interface NearByEventListener {
    }


    class MyHolder extends RecyclerView.ViewHolder {
       public  TextView wowtagid,wowtag_name,wowtag_venue,wowtag_address,wowtag_eventdatetime,wowtag_time,wowtag_description,wowtag_description_cont,wowtag_runtime
               ,wow_shares,wow_attending,wow_views;
       public Button wowtag_register;
       public VideoView wowtag_video;



        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/segoeui.ttf");
            wowtagid = (TextView) itemView.findViewById(R.id.wowtagid);
            wowtag_name=(TextView)itemView.findViewById(R.id.wowtag_name);
            wowtag_venue = (TextView) itemView.findViewById(R.id.wowtag_venue);
            wowtag_address=(TextView) itemView.findViewById(R.id.wowtag_address);


            wowtag_eventdatetime = (TextView) itemView.findViewById(R.id.wowtag_eventdatetime);
            wowtag_time=(TextView)itemView.findViewById(R.id.wowtag_time);
            wowtag_description = (TextView) itemView.findViewById(R.id.wowtag_description);
            wowtag_description_cont=(TextView) itemView.findViewById(R.id.wowtag_description_cont);
            wowtag_runtime=(TextView) itemView.findViewById(R.id.wowtag_runtime);
            wowtag_register=(Button)itemView.findViewById(R.id.wowtag_register);
            wow_shares=(TextView) itemView.findViewById(R.id.wow_shares);
            wow_attending=(TextView) itemView.findViewById(R.id.wow_attending);
            wow_views=(TextView) itemView.findViewById(R.id.wow_views);
            wowtag_video=(VideoView) itemView.findViewById(R.id.wowtag_video);

            wowtagid.setTypeface(tf);
            wowtag_name.setTypeface(tf);
            wowtag_address.setTypeface(tf);
            wowtag_eventdatetime.setTypeface(tf);
            wowtag_time.setTypeface(tf);
            wowtag_description.setTypeface(tf);
            wowtag_description_cont.setTypeface(tf);
            wowtag_runtime.setTypeface(tf);
            wowtag_register.setTypeface(tf);
            wow_shares.setTypeface(tf);
            wow_attending.setTypeface(tf);
            wow_views.setTypeface(tf);


        }
    }
}
