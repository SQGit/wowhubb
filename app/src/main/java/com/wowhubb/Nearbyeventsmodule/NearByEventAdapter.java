package com.wowhubb.Nearbyeventsmodule;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wowhubb.Activity.LandingPageActivity;
import com.wowhubb.R;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by Guna on 29-11-2017.
 */

public class NearByEventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    private List<NearByEventsModelPojo> nearByEventsModelPojos;
    SharedPreferences.Editor editor;
    String token;
    Typeface lato;
    Dialog dialog;




    public NearByEventAdapter(Context context, List<NearByEventsModelPojo> nearByEventsModelPojos) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.nearByEventsModelPojos = nearByEventsModelPojos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nearbyevent_adapter, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final NearByEventsModelPojo current=nearByEventsModelPojos.get(position);
        myHolder.event_name.setText(current.geteventName());
        myHolder.event_category.setText(current.geteventCategory());
        myHolder.event_address.setText(current.geteventAddress());
        myHolder.event_opentime.setText(current.geteventTime());
        myHolder.event_attendsize.setText(current.geteventAttending());




        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");



        //load events In ImageView:
        final int[] imageArray = { R.drawable.event1, R.drawable.event2,
                R.drawable.event3, R.drawable.event4,
                R.drawable.event5};




        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i = 0;

            public void run() {
                myHolder.img_show_events.setImageResource(imageArray[i]);
                i++;
                if (i > imageArray.length - 1) {
                    i = 0;
                }
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(runnable, 2000);



        myHolder.view_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(context, PlayVideoActivity.class);
                context.startActivity(i2);
                ((Activity) context).overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);


            }
        });

        myHolder.img_share_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, R.drawable.event1);
                shareIntent.setType("image/jpeg");
                context.startActivity(Intent.createChooser(shareIntent, context.getResources().getText(R.string.app_name)));
            }
        });


        myHolder.lnr_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,NearbyEventIndividualPage.class);
                context.startActivity(i);



            }
        });


    }






    @Override
    public int getItemCount() {
        return nearByEventsModelPojos.size();
    }





    @Override
    public int getItemViewType(int position) {
        return position;

    }

    public interface NearByEventListener {
    }


    class MyHolder extends RecyclerView.ViewHolder {
       public  TextView event_name,event_category,event_address,event_opentime,event_contact,event_attendsize,visit_link;
       public ImageView img_show_events,view_video,img_share_icon;
       public LinearLayout lnr_item;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/segoeui.ttf");
            event_name = (TextView) itemView.findViewById(R.id.event_name);
            event_category=itemView.findViewById(R.id.event_category);
            event_address = (TextView) itemView.findViewById(R.id.event_address);
            event_opentime = (TextView) itemView.findViewById(R.id.event_opentime);
            event_contact = (TextView) itemView.findViewById(R.id.event_contact);
            event_attendsize = (TextView) itemView.findViewById(R.id.event_attendsize);
            visit_link = (TextView) itemView.findViewById(R.id.visit_link);
            img_show_events=itemView.findViewById(R.id.img_show_events);
            view_video=itemView.findViewById(R.id.view_video);
            lnr_item=itemView.findViewById(R.id.lnr_item);
            img_share_icon=itemView.findViewById(R.id.img_share_icon);



            event_name.setTypeface(tf);
            event_category.setTypeface(tf);
            event_address.setTypeface(tf);
            event_opentime.setTypeface(tf);
            event_contact.setTypeface(tf);
            event_attendsize.setTypeface(tf);
            visit_link.setTypeface(tf);

        }
    }
}
