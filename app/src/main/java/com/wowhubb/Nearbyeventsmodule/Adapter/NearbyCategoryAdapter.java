package com.wowhubb.Nearbyeventsmodule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wang.avi.AVLoadingIndicatorView;
import com.wowhubb.EventServiceProvider.Model.EventServiceProviderModel;
import com.wowhubb.Nearbyeventsmodule.Activity.AllNearbyEventActivity;
import com.wowhubb.Nearbyeventsmodule.Activity.GPSTracker;
import com.wowhubb.Nearbyeventsmodule.Database_Helper.DatabaseHandler;
import com.wowhubb.Nearbyeventsmodule.EmptyActivity;
import com.wowhubb.Nearbyeventsmodule.HighlightEventModelPojo;
import com.wowhubb.Nearbyeventsmodule.Model.InfoEventModelPojo;
import com.wowhubb.Nearbyeventsmodule.Model.NearByEventsModelPojo;
import com.wowhubb.Nearbyeventsmodule.Model.ScheduleEventModelPojo;
import com.wowhubb.R;
import com.wowhubb.Utils.Config;
import com.wowhubb.Utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ravi on 16/11/17.
 */

public class NearbyCategoryAdapter extends RecyclerView.Adapter<NearbyCategoryAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<EventServiceProviderModel> eventServiceProviderList;
    private List<EventServiceProviderModel> eventServiceProviderListFiltered;
    private NearByEventAdapter.NearByEventListener listener;
    ArrayList<String> category_list = new ArrayList<>();
    String getID,getCategory;
    Typeface lato;
    AVLoadingIndicatorView av_loader;
    String token,city;
    SharedPreferences.Editor editor;
    NearByEventsModelPojo nearByEventsModelPojo = new NearByEventsModelPojo();
    InfoEventModelPojo infoEventModelPojo=new InfoEventModelPojo();
    ScheduleEventModelPojo scheduleEventModelPojo=new ScheduleEventModelPojo();
    HighlightEventModelPojo highlightEventModelPojo=new HighlightEventModelPojo();
    public static int page_count;



    public NearbyCategoryAdapter(Context context, List<EventServiceProviderModel> eventServiceProviderList, NearByEventAdapter.NearByEventListener listener) {
        this.context=context;
        this.eventServiceProviderList=eventServiceProviderList;
        this.eventServiceProviderListFiltered=eventServiceProviderList;
        this.listener=listener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView provider_name;
        public ImageView img_provider_logo;

        public MyViewHolder(View view) {
            super(view);
            provider_name = view.findViewById(R.id.txt_provider);
            img_provider_logo = view.findViewById(R.id.img_provider_logo);

            //Change Typeface:
            lato = Typeface.createFromAsset(context.getAssets(), "fonts/segoeui.ttf");
            provider_name.setTypeface(lato);


            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            editor = sharedPreferences.edit();
            token = sharedPreferences.getString("token", "");



            GPSTracker gpsTracker = new GPSTracker(context);

            if (gpsTracker.getIsGPSTrackingEnabled())
            {
                String stringLatitude = String.valueOf(gpsTracker.getLatitude());


                String stringLongitude = String.valueOf(gpsTracker.getLongitude());


                String country = gpsTracker.getCountryName(context);


                city = gpsTracker.getLocality(context);
                Log.e("tag","printCity"+city);


                String postalCode = gpsTracker.getPostalCode(context);


                String addressLine = gpsTracker.getAddressLine(context);

            }
            else
            {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                //gpsTracker.showSettingsAlert();
            }




            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //get Recycler View Current Position:
                    EventServiceProviderModel current=eventServiceProviderListFiltered.get(getAdapterPosition());
                    getID = current.providerId;
                    getCategory=current.providerName;
                    Log.e("tag","working"+getID);
                    Log.e("tag","working"+getCategory);



                    //call NearbyEvents Activity Asynctask:
                    new nearbyeventsAsync().execute();



                }
            });
        }
    }

    /** Method to turn on GPS **/






    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_service_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final EventServiceProviderModel eventServiceProviderModel = eventServiceProviderListFiltered.get(position);
        holder.provider_name.setText(eventServiceProviderModel.getproviderName());
//        holder.phone.setText(contact.getPhone());

        Glide.with(context).load("http://104.197.80.225:3010/wow/media/nearby/" + eventServiceProviderModel.providerLogo)
                .into(holder.img_provider_logo);
    }

    @Override
    public int getItemCount() {
        return eventServiceProviderListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    eventServiceProviderListFiltered = eventServiceProviderList;
                } else {
                    List<EventServiceProviderModel> filteredList = new ArrayList<>();
                    for (EventServiceProviderModel row : eventServiceProviderList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getproviderName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    eventServiceProviderListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = eventServiceProviderListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                eventServiceProviderListFiltered = (ArrayList<EventServiceProviderModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }






    //Doing NearbyEvents Activity Asynctask:
    class nearbyeventsAsync extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("location",city);
                jsonObject.accumulate("eventcategory",getCategory);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_GET_NEAR_BY_EVENTS, json,token);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonstr) {
            Log.e("tag", "<-----0000000--------->" + jsonstr);
            //av_loader.setVisibility(View.GONE);
            super.onPostExecute(jsonstr);
            List<EventServiceProviderModel> data=new ArrayList<>();
            if (jsonstr.equals("")) {
                Toast.makeText(context,"Check Network Connection",Toast.LENGTH_LONG).show();
            } else {

                try {

                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("success");
                    if(status.equals("true")) {

                        JSONArray event_service = jo.getJSONArray("message");
                        DatabaseHandler db = new DatabaseHandler(context);

                            if (event_service.length() > 0) {
                                for (int i1 = 0; i1 < event_service.length(); i1++) {


                                    JSONObject jsonObject = event_service.getJSONObject(i1);


                                    db.deleteTable();


                                    nearByEventsModelPojo.eventCategory=jsonObject.getString("eventcategory");  //1st table
                                    nearByEventsModelPojo.eventName=jsonObject.getString("eventname");  //1st table
                                    nearByEventsModelPojo.eventTicketType=jsonObject.getString("tickettype");  //1st table
                                    nearByEventsModelPojo.eventPrice=jsonObject.getString("ticketprice");  //1st table
                                    nearByEventsModelPojo.eventDateTime=jsonObject.getString("eventstartdate");  //1st Table

                                    infoEventModelPojo.eventDescription=jsonObject.getString("eventdescription");  //2nd table
                                    infoEventModelPojo.eventLink=jsonObject.getString("eventticketurl");

                                    String ss=jsonObject.getString("runtimefrom");
                                    Log.e("tag","chejjjjj"+ss);
                                    infoEventModelPojo.eventDate=jsonObject.getString("runtimefrom");  //2nd table

                                    nearByEventsModelPojo.eventCoverpage=jsonObject.getString("coverpage");  //1st table
                                    String wowtag_video=jsonObject.getString("wowtagvideo");
                                    if(wowtag_video!=null)
                                    {
                                        nearByEventsModelPojo.eventWowtagVideo=jsonObject.getString("wowtagvideo");  //1st table
                                    }



                                    nearByEventsModelPojo.wowtagName=jsonObject.getString("eventtitle");  //1st Table
                                    highlightEventModelPojo.guestVideo2=jsonObject.getString("eventhighlightsvideo2");
                                    highlightEventModelPojo.guestVideo1=jsonObject.getString("eventhighlightsvideo1");
                                    highlightEventModelPojo.guestProfile2=jsonObject.getString("eventhighlights2");
                                    highlightEventModelPojo.guestProfile1=jsonObject.getString("eventhighlights1");
                                    highlightEventModelPojo.guestInfo2=jsonObject.getString("eventspeakeractivities2");
                                    highlightEventModelPojo.guestInfo1=jsonObject.getString("eventspeakeractivities1");
                                    highlightEventModelPojo.guestSite2=jsonObject.getString("eventspeakerlink2");
                                    highlightEventModelPojo.guestSite1=jsonObject.getString("eventspeakerlink1");
                                    highlightEventModelPojo.guestName2=jsonObject.getString("eventspeakername2");
                                    highlightEventModelPojo.guestName1=jsonObject.getString("eventspeakername1");
                                    highlightEventModelPojo.guestType2=jsonObject.getString("eventguesttype2");
                                    highlightEventModelPojo.guestType1=jsonObject.getString("eventguesttype1");




                                    JSONArray program_schedule=jsonObject.getJSONArray("programschedule");
                                    Log.e("tag","PROGRAM SCHEDULE-->"+program_schedule);
                                    page_count=program_schedule.length();
                                    for(int pg_count=0;pg_count<program_schedule.length();pg_count++)
                                    {
                                        Log.e("tag","surf");
                                        JSONObject program_data=program_schedule.getJSONObject(pg_count);
                                        scheduleEventModelPojo.eventInnerEvent=program_data.getString("agenda");  //3rd Table
                                        scheduleEventModelPojo.eventInnerWhere=program_data.getString("location");  //3rd Table
                                        scheduleEventModelPojo.eventInnerWho=program_data.getString("facilitator");  //3rd table
                                        String ed_time=program_data.getString("endtime");
                                        String st_time=program_data.getString("starttime");
                                        String merge_time=st_time+" - "+ed_time;
                                        scheduleEventModelPojo.eventInnerTime=merge_time;


                                        db.addNewNearByScheduleEntry(scheduleEventModelPojo);
                                    }


                                    JSONArray event_venue=jsonObject.getJSONArray("eventvenue");
                                    Log.e("tag","EVENT VENUE-->"+event_venue);
                                    for(int ev_count=0;ev_count<event_venue.length();ev_count++)
                                    {
                                        Log.e("tag","Excel");
                                        JSONObject eventvenue_data=event_venue.getJSONObject(ev_count);
                                        String zipcode=eventvenue_data.getString("eventvenuezipcode");
                                        String city=eventvenue_data.getString("eventvenuecity");
                                        String address2=eventvenue_data.getString("eventvenueaddress2");
                                        String address1=eventvenue_data.getString("eventvenueaddress1");
                                        String venue_name=eventvenue_data.getString("eventvenuename");
                                        String address=venue_name+" "+address1+" "+city+" "+zipcode;
                                        Log.e("tag","PRINT VENUE DATA"+address);
                                        nearByEventsModelPojo.eventLoaction=address;  //1st Table
                                    }
                                    //add values to db:
                                    db.addNewNearByEventEntry(nearByEventsModelPojo);
                                    db.addNewNearByInfoEntry(infoEventModelPojo);
                                    db.addNewNearByHighlightsEntry(highlightEventModelPojo);
                                }


                                Intent intent=new Intent(context, AllNearbyEventActivity.class);
                                intent.putExtra("category_name1",getCategory);
                                Log.e("tag","gethere"+getCategory);
                                context.startActivity(intent);
                            }

                            else {
                                Intent empty_acty=new Intent(context, EmptyActivity.class);
                                empty_acty.putExtra("category_name",getCategory);
                                context.startActivity(empty_acty);
                           // Toast.makeText(context,"data empty",Toast.LENGTH_LONG).show();
                        }

                    }
                    else
                    {

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }




}
