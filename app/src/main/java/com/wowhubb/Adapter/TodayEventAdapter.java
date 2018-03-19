package com.wowhubb.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;
import com.wowhubb.data.TodayEvents;

import java.util.List;


public class TodayEventAdapter extends BaseAdapter {


    SharedPreferences.Editor editor;
    String token, userId;
    Dialog dialog;
    private Activity activity;
    private LayoutInflater inflater;
    private List<TodayEvents> feedItems;

    public TodayEventAdapter(Activity activity, List<TodayEvents> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
    }


    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
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
            convertView = inflater.inflate(R.layout.dialog_list_todayevents, null);
            viewHolder = new ViewHolder();
            viewHolder.position = position;
            FontsOverride.overrideFonts(activity, convertView);
            viewHolder.eventname = (TextView) convertView.findViewById(R.id.eventtitle_tv);
            viewHolder.time = (TextView) convertView.findViewById(R.id.runttime_tv);
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TodayEvents item = feedItems.get(position);

        viewHolder.position = position;
        viewHolder.eventname.setText("!"+item.getEventtitle());

        return convertView;
    }

    static class ViewHolder {
        TextView eventname, time;
        int position;

    }


}
