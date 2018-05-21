package com.wowhubb.Nearbyeventsmodule;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.wowhubb.R;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Guna on 29-11-2017.
 */

public class HighlightEventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    private List<HighlightEventModelPojo> eventHighlightsModelPojos;
    SharedPreferences.Editor editor;
    String token;


    public HighlightEventAdapter(Context context, ArrayList<HighlightEventModelPojo> eventHighlightsModelPojos) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.eventHighlightsModelPojos = eventHighlightsModelPojos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_adapter_event_hifhlights, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final HighlightEventModelPojo current=eventHighlightsModelPojos.get(position);
        myHolder.guest_wowtagid.setText(current.getguestWowTagId());
        String video1=current.getguestVideo1();
        Log.e("tag","Print Video"+video1);


        Glide.with(context).load(R.drawable.speaker1)
                .into(myHolder.img_guestprofile);

        myHolder.lnr_video_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, PlayMultipleActivity.class);
                context.startActivity(intent);
            }
        });

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");
    }


    @Override
    public int getItemCount() {
        return eventHighlightsModelPojos.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    class MyHolder extends RecyclerView.ViewHolder {
       public  TextView guest_wowtagid,guest_name,guest_info,guest_website,txt_speaker;
       public ImageView img_guestprofile;
       public LinearLayout lnr_video_link;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/segoeui.ttf");
            guest_wowtagid = (TextView) itemView.findViewById(R.id.guest_wowtagid);
            guest_name=itemView.findViewById(R.id.guest_name);
            guest_info = (TextView) itemView.findViewById(R.id.guest_info);
            img_guestprofile = (ImageView) itemView.findViewById(R.id.img_guestprofile);
            guest_website=(TextView) itemView.findViewById(R.id.guest_website);
            txt_speaker=itemView.findViewById(R.id.txt_speaker);
            lnr_video_link=itemView.findViewById(R.id.lnr_video_link);

            guest_wowtagid.setTypeface(tf);
            guest_name.setTypeface(tf);
            guest_info.setTypeface(tf);
            guest_website.setTypeface(tf);
            txt_speaker.setTypeface(tf);
        }
    }
}
