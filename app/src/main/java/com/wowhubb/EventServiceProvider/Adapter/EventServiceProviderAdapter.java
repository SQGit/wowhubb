package com.wowhubb.EventServiceProvider.Adapter;

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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wowhubb.EventServiceProvider.Activity.EventListDetailsNewActivity;
import com.wowhubb.EventServiceProvider.Model.EventServiceProviderModel;
import com.wowhubb.R;

import java.util.ArrayList;
import java.util.List;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;

/**
 * Created by ravi on 16/11/17.
 */

public class EventServiceProviderAdapter extends RecyclerView.Adapter<EventServiceProviderAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<EventServiceProviderModel> eventServiceProviderList;
    private List<EventServiceProviderModel> eventServiceProviderListFiltered;
    private EventServiceAdapterListener listener;
    String getID;
    String token;
    SharedPreferences.Editor editor;
    Typeface lato;

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
                    EventServiceProviderModel current=eventServiceProviderListFiltered.get(getAdapterPosition());
                    getID = current.providerName;

                    Intent intent=new Intent(context, EventListDetailsNewActivity.class);
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("provider",getID);
                    editor.commit();
                    context.startActivity(intent);
                }
            });
        }
    }


    public EventServiceProviderAdapter(Context context, List<EventServiceProviderModel> contactList, EventServiceAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.eventServiceProviderList = contactList;
        this.eventServiceProviderListFiltered = contactList;
    }

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


        Glide.with(context).load("http://104.197.80.225:3010/wow/media/provider/" + eventServiceProviderModel.providerLogo)
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


}
