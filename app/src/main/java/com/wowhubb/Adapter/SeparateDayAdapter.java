package com.wowhubb.Adapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suke.widget.SwitchButton;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import com.wowhubb.R;
import com.wowhubb.data.DayContent;

import java.util.List;

/**
 * Created by Guna on 19-12-2017.
 */

public class SeparateDayAdapter extends RecyclerView.Adapter<SeparateDayAdapter.MyViewHolder>{
    private List<DayContent> daycontent;
    TextView itinerary_start,itinerary_end,itinery_txt,txt_hours,txt_mins,time_hour,time_min,txt_start_time,txt_end_time;
    public static EditText edt_event_agenta,edt_facilitator,edt_location,start_timetv,end_timetv;
    LinearLayout layout_fromdate,lnr_start,lnr_end;
    MaterialBetterSpinner spin_event_loc_venue,spin_start_hour,spin_start_minutes,spin_end_hour,spin_end_minutes,spin_end_time,spin_start_zone,spin_end_zone;

    String[] LOCATION = {"Location 1","Location 2","Location 3"};
     private Activity activity;
    Typeface lato;
    public static SharedPreferences.Editor edit;
    TextInputLayout lnr_edt_event_agenta,lnr_edt_facilitator;
    ImageView img_itinerary_start,img_itinerary_end;
    SwitchButton switch_am,switch_pm;
    ImageView img_hours_sub,img_hours_add,img_mins_sub,img_mins_add,img_close;
    int numtest_hour=0;
    int numtest_mins=0;
    EditText txt_am,txt_pm;
    String hours_count,mins_count,str_am_pm;
    Button btn_ok;
    public static String str_hrs,str_mins;
    AlertDialog b;





    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View view) {
            super(view);

        }
    }

    public SeparateDayAdapter(Activity activity, List<DayContent> daycontent) {

        this.daycontent = daycontent;
        this.activity=activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_list_row, parent, false);


        itinerary_start=(TextView)itemView.findViewById(R.id.itinerary_start);
        itinerary_end=(TextView)itemView.findViewById(R.id.itinerary_end);
        lnr_start=(LinearLayout)itemView.findViewById(R.id.lnr_start);
        lnr_end=(LinearLayout)itemView.findViewById(R.id.lnr_end);
        edt_event_agenta=(EditText) itemView.findViewById(R.id.edt_event_agenta);
        edt_facilitator=(EditText) itemView.findViewById(R.id.edt_facilitator);

        start_timetv=(EditText) itemView.findViewById(R.id.start_timetv);
        end_timetv=(EditText) itemView.findViewById(R.id.end_timetv);
        txt_start_time=(TextView)itemView.findViewById(R.id.txt_start_time);
        txt_end_time=(TextView)itemView.findViewById(R.id.txt_end_time);

        layout_fromdate = itemView.findViewById(R.id.layout_fromdate1);
        spin_event_loc_venue=(MaterialBetterSpinner)itemView.findViewById(R.id.spin_event_loc_venue);
        lnr_edt_event_agenta=(TextInputLayout) itemView.findViewById(R.id.lnr_edt_event_agenta);
        lnr_edt_facilitator=(TextInputLayout) itemView.findViewById(R.id.lnr_edt_facilitator);
        img_itinerary_start=(ImageView) itemView.findViewById(R.id.img_itinerary_start);
        img_itinerary_end=(ImageView) itemView.findViewById(R.id.img_itinerary_end);
        lato = Typeface.createFromAsset(activity.getAssets(), "fonts/lato.ttf");



        itinerary_start.setTypeface(lato);
        itinerary_end.setTypeface(lato);
        edt_event_agenta.setTypeface(lato);
        edt_facilitator.setTypeface(lato);
        lnr_edt_event_agenta.setTypeface(lato);
        lnr_edt_facilitator.setTypeface(lato);










        lnr_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                LayoutInflater inflater = activity.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_dialog_select_time, null);
                dialogBuilder.setView(dialogView);
                lato = Typeface.createFromAsset(activity.getAssets(), "fonts/lato.ttf");
                itinery_txt = (TextView) dialogView.findViewById(R.id.itinery_txt);
                txt_hours=(TextView) dialogView.findViewById(R.id.txt_hours);
                txt_mins=(TextView)dialogView.findViewById(R.id.txt_mins);
                txt_am=(EditText) dialogView.findViewById(R.id.txt_am);
                txt_pm=(EditText) dialogView.findViewById(R.id.txt_pm);
                time_hour=(TextView) dialogView.findViewById(R.id.time_hour);
                time_min=(TextView)dialogView.findViewById(R.id.time_min);
                switch_am=(SwitchButton)dialogView.findViewById(R.id.switch_am) ;
                switch_pm=(SwitchButton)dialogView.findViewById(R.id.switch_pm) ;
                img_hours_sub=(ImageView)dialogView.findViewById(R.id.img_hours_sub) ;
                img_hours_add=(ImageView)dialogView.findViewById(R.id.img_hours_add) ;
                img_mins_sub=(ImageView)dialogView.findViewById(R.id.img_mins_sub) ;
                img_mins_add=(ImageView)dialogView.findViewById(R.id.img_mins_add) ;
                btn_ok=(Button)dialogView.findViewById(R.id.btn_ok);
                img_close=(ImageView) dialogView.findViewById(R.id.img_close);

                itinery_txt.setTypeface(lato);
                txt_hours.setTypeface(lato);
                txt_mins.setTypeface(lato);
                txt_am.setTypeface(lato);
                txt_pm.setTypeface(lato);
                time_hour.setTypeface(lato);
                time_min.setTypeface(lato);
                btn_ok.setTypeface(lato);
                txt_start_time.setTypeface(lato);
                txt_end_time.setTypeface(lato);


                img_hours_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        img_hours_sub.setClickable(true);
                        numtest_hour=numtest_hour+1;
                        hours_count = Integer.toString(numtest_hour);
                        txt_hours.setText(hours_count+" Hours");
                        time_hour.setText(hours_count);
                            Log.e("tag","increment  "+hours_count);

                            if(hours_count.equals("12"))
                            {
                                Log.e("tag","over  "+numtest_hour);
                                img_hours_add.setClickable(false);

                            }


                    }
                });


                img_hours_sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        img_hours_add.setClickable(true);
                        numtest_hour = numtest_hour - 1;
                        hours_count = Integer.toString(numtest_hour);
                        txt_hours.setText(hours_count+" Hours");
                        time_hour.setText(hours_count);
                        Log.e("tag","decrement  "+hours_count);

                        if(hours_count.equals("1"))
                        {
                            Log.e("tag","over  "+numtest_hour);
                            img_hours_sub.setClickable(false);

                        }
                    }
                });



                img_mins_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        img_mins_sub.setClickable(true);
                        numtest_mins=numtest_mins+1;
                        mins_count = Integer.toString(numtest_mins);
                        txt_mins.setText(mins_count+" Minutes");
                        time_min.setText(mins_count);
                        Log.e("tag","increment  "+hours_count);

                        if(mins_count.equals("60"))
                        {
                            Log.e("tag","over  "+numtest_mins);
                            img_mins_add.setClickable(false);

                        }


                    }
                });


                img_mins_sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        img_mins_add.setClickable(true);
                        numtest_mins = numtest_mins - 1;
                        mins_count = Integer.toString(numtest_mins);
                        txt_mins.setText(mins_count+" Minutes");
                        time_min.setText(mins_count);
                        Log.e("tag","decrement  "+hours_count);

                        if(mins_count.equals("1"))
                        {
                            Log.e("tag","over  "+numtest_mins);
                            img_mins_sub.setClickable(false);

                        }
                    }
                });



                switch_am.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                        if(isChecked)
                        {
                            switch_pm.setChecked(false);
                            txt_am.setTextColor(Color.parseColor("#3c3c3c"));
                            txt_pm.setTextColor(Color.parseColor("#828689"));
                            str_am_pm="AM";
                            txt_am.setText(str_am_pm);
                        }
                        else
                        {
                            str_am_pm="";
                            txt_am.setTextColor(Color.parseColor("#828689"));
                        }

                    }
                });




                switch_pm.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                   @Override
                   public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                       if(isChecked)
                       {
                           switch_am.setChecked(false);
                           txt_pm.setTextColor(Color.parseColor("#3c3c3c"));
                           txt_am.setTextColor(Color.parseColor("#828689"));
                           str_am_pm="PM";
                           txt_pm.setText(str_am_pm);
                       }
                       else
                       {
                           str_am_pm="";
                           txt_pm.setTextColor(Color.parseColor("#828689"));
                       }

                   }
               });

                img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        b.dismiss();

                    }
                });


                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        str_hrs=time_hour.getText().toString();
                        str_mins=time_min.getText().toString();
                        img_itinerary_start.setVisibility(View.GONE);
                        Log.e("tag","checking"+str_am_pm);


                        if(!str_hrs.equals("00")){
                            if(!str_mins.equals("00")){
                                if(str_am_pm!=null){
                                    txt_start_time.setText(str_hrs+":"+str_mins+str_am_pm);
                                    b.dismiss();
                                }
                                else
                                {

                                    Toast.makeText(activity,"Please Select AM/PM",Toast.LENGTH_LONG).show();
                                }



                            }
                            else
                            {
                                Toast.makeText(activity,"Please Select Minutes",Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(activity,"Please Select Hours",Toast.LENGTH_LONG).show();
                        }



                    }
                });



                b = dialogBuilder.create();
                b.show();
            }
        });


        lnr_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                LayoutInflater inflater = activity.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_dialog_select_time, null);
                dialogBuilder.setView(dialogView);
                lato = Typeface.createFromAsset(activity.getAssets(), "fonts/lato.ttf");
                itinery_txt = (TextView) dialogView.findViewById(R.id.itinery_txt);
                txt_hours=(TextView) dialogView.findViewById(R.id.txt_hours);
                txt_mins=(TextView)dialogView.findViewById(R.id.txt_mins);
                txt_am=(EditText) dialogView.findViewById(R.id.txt_am);
                txt_pm=(EditText) dialogView.findViewById(R.id.txt_pm);
                time_hour=(TextView) dialogView.findViewById(R.id.time_hour);
                time_min=(TextView)dialogView.findViewById(R.id.time_min);
                switch_am=(SwitchButton)dialogView.findViewById(R.id.switch_am) ;
                switch_pm=(SwitchButton)dialogView.findViewById(R.id.switch_pm) ;
                img_hours_sub=(ImageView)dialogView.findViewById(R.id.img_hours_sub) ;
                img_hours_add=(ImageView)dialogView.findViewById(R.id.img_hours_add) ;
                img_mins_sub=(ImageView)dialogView.findViewById(R.id.img_mins_sub) ;
                img_mins_add=(ImageView)dialogView.findViewById(R.id.img_mins_add) ;
                btn_ok=(Button)dialogView.findViewById(R.id.btn_ok);
                img_close=(ImageView) dialogView.findViewById(R.id.img_close);

                itinery_txt.setTypeface(lato);
                txt_hours.setTypeface(lato);
                txt_mins.setTypeface(lato);
                txt_am.setTypeface(lato);
                txt_pm.setTypeface(lato);
                time_hour.setTypeface(lato);
                time_min.setTypeface(lato);
                btn_ok.setTypeface(lato);
                txt_end_time.setTypeface(lato);


                img_hours_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        img_hours_sub.setClickable(true);
                        numtest_hour=numtest_hour+1;
                        hours_count = Integer.toString(numtest_hour);
                        txt_hours.setText(hours_count+" Hours");
                        time_hour.setText(hours_count);
                        Log.e("tag","increment  "+hours_count);

                        if(hours_count.equals("12"))
                        {
                            Log.e("tag","over  "+numtest_hour);
                            img_hours_add.setClickable(false);

                        }


                    }
                });


                img_hours_sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        img_hours_add.setClickable(true);
                        numtest_hour = numtest_hour - 1;
                        hours_count = Integer.toString(numtest_hour);
                        txt_hours.setText(hours_count+" Hours");
                        time_hour.setText(hours_count);
                        Log.e("tag","decrement  "+hours_count);

                        if(hours_count.equals("1"))
                        {
                            Log.e("tag","over  "+numtest_hour);
                            img_hours_sub.setClickable(false);

                        }
                    }
                });



                img_mins_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        img_mins_sub.setClickable(true);
                        numtest_mins=numtest_mins+1;
                        mins_count = Integer.toString(numtest_mins);
                        txt_mins.setText(mins_count+" Minutes");
                        time_min.setText(mins_count);
                        Log.e("tag","increment  "+hours_count);

                        if(mins_count.equals("60"))
                        {
                            Log.e("tag","over  "+numtest_mins);
                            img_mins_add.setClickable(false);

                        }


                    }
                });


                img_mins_sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        img_mins_add.setClickable(true);
                        numtest_mins = numtest_mins - 1;
                        mins_count = Integer.toString(numtest_mins);
                        txt_mins.setText(mins_count+" Minutes");
                        time_min.setText(mins_count);
                        Log.e("tag","decrement  "+hours_count);

                        if(mins_count.equals("1"))
                        {
                            Log.e("tag","over  "+numtest_mins);
                            img_mins_sub.setClickable(false);

                        }
                    }
                });



                switch_am.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                        if(isChecked)
                        {
                            switch_pm.setChecked(false);
                            txt_am.setTextColor(Color.parseColor("#3c3c3c"));
                            txt_pm.setTextColor(Color.parseColor("#828689"));
                            str_am_pm="AM";
                            txt_am.setText(str_am_pm);
                        }
                        else
                        {
                            str_am_pm="";
                            txt_am.setTextColor(Color.parseColor("#828689"));
                        }

                    }
                });




                switch_pm.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                        if(isChecked)
                        {
                            switch_am.setChecked(false);
                            txt_pm.setTextColor(Color.parseColor("#3c3c3c"));
                            txt_am.setTextColor(Color.parseColor("#828689"));
                            str_am_pm="PM";
                            txt_pm.setText(str_am_pm);
                        }
                        else
                        {
                            str_am_pm="";
                            txt_pm.setTextColor(Color.parseColor("#828689"));
                        }

                    }
                });

                img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        b.dismiss();

                    }
                });


                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        str_hrs=time_hour.getText().toString();
                        str_mins=time_min.getText().toString();
                        img_itinerary_end.setVisibility(View.GONE);
                        Log.e("tag","checking"+str_am_pm);


                        if(!str_hrs.equals("00")){
                            if(!str_mins.equals("00")){
                                if(str_am_pm!=null){
                                    txt_end_time.setText(str_hrs+":"+str_mins+str_am_pm);
                                    b.dismiss();
                                }
                                else
                                {

                                    Toast.makeText(activity,"Please Select AM/PM",Toast.LENGTH_LONG).show();
                                }



                            }
                            else
                            {
                                Toast.makeText(activity,"Please Select Minutes",Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(activity,"Please Select Hours",Toast.LENGTH_LONG).show();
                        }

                    }
                });



                b = dialogBuilder.create();
                b.show();
            }
        });



        //START LOCATION  SET SPINNER
        final CustomAdapter arrayAdapter0 = new CustomAdapter(activity, android.R.layout.simple_dropdown_item_1line, LOCATION) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return true;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(14);
                tv.setTypeface(lato);
                tv.setPadding(30, 55, 10, 25);
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(14);
                tv.setTypeface(lato);
                tv.setPadding(10, 20, 0, 20);

                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spin_event_loc_venue.setAdapter(arrayAdapter0);
        spin_event_loc_venue.setTypeface(lato);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        animate(holder);
    }

    @Override
    public int getItemCount() {
        return daycontent.size();
    }


    // Insert a new item to the RecyclerView
    public void insert(int position, DayContent dayContent) {
        daycontent.add(position, dayContent);
        notifyItemInserted(position);

    }

    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(activity, R.anim.bounce_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }


}
