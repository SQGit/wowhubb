package com.wowhubb.Nearbyeventsmodule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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


import com.wowhubb.EventServiceProvider.Model.EventServiceProviderModel;
import com.wowhubb.R;
import com.wowhubb.Utils.Config;
import com.wowhubb.Utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;

/**
 * Created by ravi on 16/11/17.
 */

public class NearbyEventsCategoryAdapter extends RecyclerView.Adapter<NearbyEventsCategoryAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<EventServiceProviderModel> eventServiceProviderList;
    private List<EventServiceProviderModel> eventServiceProviderListFiltered;
    private NearByEventAdapter.NearByEventListener listener;
    ArrayList<String> category_list = new ArrayList<>();
    String getID;
    String token;
    SharedPreferences.Editor editor;
    Typeface lato;

    public NearbyEventsCategoryAdapter(Context context, List<EventServiceProviderModel> eventServiceProviderList, NearByEventAdapter.NearByEventListener listener) {
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
            lato = Typeface.createFromAsset(context.getAssets(), "fonts/segoeui.ttf");

            provider_name.setTypeface(lato);


            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            editor = sharedPreferences.edit();
            token = sharedPreferences.getString("token", "");

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
//                    listener.onContactSelected(eventServiceProviderListFiltered.get(getAdapterPosition()));

//                    listener.onEventServiceProviderSelected(eventServiceProviderListFiltered.get(getAdapterPosition()));

                    EventServiceProviderModel current=eventServiceProviderListFiltered.get(getAdapterPosition());
                    getID = current.providerId;

                    Intent intent=new Intent(context, AllNearbyEventActivity.class);
                    context.startActivity(intent);



                }
            });
        }
    }


    /*public NearbyEventsCategoryAdapter(Context context, List<EventServiceProviderModel> contactList, EventServiceAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.eventServiceProviderList = contactList;
        this.eventServiceProviderListFiltered = contactList;
    }*/

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

    public interface EventServiceAdapterListener {
        void onEventServiceProviderSelected(EventServiceProviderModel eventServiceProviderModel);
    }

    private class callProviderItem extends AsyncTask<String,Void,String> {

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
                jsonObject.accumulate("providerid",getID);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_EVENT_SERVICE_CATEGORY, json,token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }


        @Override
        protected void onPostExecute(String jsonstr) {
            super.onPostExecute(jsonstr);
            List<EventServiceProviderModel> data=new ArrayList<>();
            if (jsonstr.equals("")) {
                Toast.makeText(context,"Check Network Connection",Toast.LENGTH_LONG).show();
            } else {

                try {

                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("success");
                    if(status.equals("true")) {

                        JSONObject event_category = jo.getJSONObject("message");
                        String id=event_category.getString("_id");
                        String cat_name=event_category.getString("provider");
                        Log.e("tag","game"+cat_name);
                        JSONArray cat_val=event_category.getJSONArray("category");

                        if (cat_val.length() > 0) {
                            category_list.clear();

                            for(int g=0;g<cat_val.length();g++)
                            {

                                category_list.add(cat_val.getString(g));
                            }


                          /*  Intent intent=new Intent(context, EventProviderCategotyActivity.class);
                            intent.putStringArrayListExtra("category_list",category_list);
                            intent.putExtra("provider",cat_name);
                            context.startActivity(intent);
*/


                        } else {
                            Toast.makeText(context,"This provider not added category still",Toast.LENGTH_LONG).show();
                        }

                    }
                    else
                    {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
