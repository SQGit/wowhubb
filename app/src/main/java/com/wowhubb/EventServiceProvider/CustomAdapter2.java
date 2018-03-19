package com.wowhubb.EventServiceProvider;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wowhubb.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guna on 03-02-2018.
 */

class CustomAdapter2 extends ArrayAdapter<String> {

    private Context context;
    private int layout;
    List<String> source;

    public CustomAdapter2(EventProviderCategotyActivity eventProviderCategoty, int simple_dropdown_item_1line, ArrayList<String> category_list) {
        super(eventProviderCategoty, R.layout.spinner_item_list,category_list);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = super.getView(position, convertView, parent);

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/opensans.ttf");

        TextView suggestion = (TextView) view.findViewById(R.id.text);

        return view;
    }


}
