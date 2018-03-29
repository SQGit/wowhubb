package com.wowhubb.EventServiceProvider.Adapter;

/**
 * Created by Guna on 20-03-2018.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wowhubb.R;


public class CustomGridPhotoGallery extends BaseAdapter{
    private Context mContext;
    private final String[] web;
    public final String[] price;
    private final int[] Imageid;
    Typeface segoeui;

    public CustomGridPhotoGallery(Context c, String[] web, int[] Imageid, String[] price) {
        mContext = c;
        this.Imageid = Imageid;
        this.web = web;
        this.price=price;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return web.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_photo_single, null);
            TextView item_name = (TextView) grid.findViewById(R.id.grid_text);
            TextView item_price = (TextView) grid.findViewById(R.id.grid_price);
            TextView eventorg = (TextView) grid.findViewById(R.id.eventorg);

            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);

            segoeui = Typeface.createFromAsset(mContext.getAssets(), "fonts/segoeui.ttf");
            item_name.setTypeface(segoeui);
            item_price.setTypeface(segoeui);
            eventorg.setTypeface(segoeui);
            item_name.setText(web[position]);
            item_price.setText(price[position]);
            imageView.setImageResource(Imageid[position]);

        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}