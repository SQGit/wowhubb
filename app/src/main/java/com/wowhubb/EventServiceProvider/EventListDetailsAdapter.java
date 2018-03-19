package com.wowhubb.EventServiceProvider;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wowhubb.R;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by Guna on 29-11-2017.
 */

public class EventListDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public LayoutInflater inflater;
    private List<EventListDetailsModel> eventListDetailsModels;
    SharedPreferences.Editor editor;
    String token;
    Typeface lato;
    Dialog dialog;



    public EventListDetailsAdapter(Context context, List<EventListDetailsModel> eventListDetailsModels) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.eventListDetailsModels = eventListDetailsModels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.event_list_details_adapter, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final EventListDetailsModel current=eventListDetailsModels.get(position);
        myHolder.txt_shop_name.setText(current.shopName);
        myHolder.txt_shop_address.setText(current.getshopAddress());
        myHolder.txt_shop_category.setText(current.getshopCategory());
        myHolder.txt_shop_mobile.setText(current.shopContact);
        myHolder.txt_shop_link.setText(current.shopLink);
        myHolder.txt_open_time.setText("Opens@ "+current.shopOpenTime);
        myHolder.txt_shop_rating.setText(current.shopRating);
        myHolder.txt_shop_category.setText(current.shopCategory);
        myHolder.txt_shop_country.setText("Country: "+current.shopCountry);

        lato = Typeface.createFromAsset(context.getAssets(), "fonts/segoeui.ttf");

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");


        myHolder.show_ratingbar.setRating(Float.parseFloat(current.shopRating));

        // load image into imageview using glide:
        Glide.with(context).load("http://104.197.80.225:3010/wow/media/provider/" + current.shopLogo)
                .into(myHolder.img_provider_logo);


        myHolder.txt_shop_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://"+current.shopLink;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });


        //Underline in particular textView:
        String sourceString = "<u>" + current.shopLink + "</u> ";
        myHolder.txt_shop_link.setText(Html.fromHtml(sourceString));




        myHolder.txt_call.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        myHolder.txt_call.setTextColor(Color.parseColor("#FFFFFF"));
                        myHolder.txt_call.setBackgroundResource(R.drawable.fill_square);
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+current.shopContact));
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.

                        }
                        context.startActivity(callIntent);
                        return true;
                    case MotionEvent.ACTION_UP:
                        myHolder.txt_call.setTextColor(Color.parseColor("#000000"));
                        myHolder.txt_call.setBackgroundResource(R.drawable.normalsquare);
                        return true;
                }
                return false;
            }
        });






        myHolder.txt_wowsome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        myHolder.txt_wowsome.setTextColor(Color.parseColor("#FFFFFF"));
                        myHolder.txt_wowsome.setBackgroundResource(R.drawable.fill_square);
                        Toast.makeText(context, "Under Development", Toast.LENGTH_SHORT).show();
                        return true;
                    case MotionEvent.ACTION_UP:
                        myHolder.txt_wowsome.setTextColor(Color.parseColor("#000000"));
                        myHolder.txt_wowsome.setBackgroundResource(R.drawable.normalsquare);
                        return true;
                }
                return false;
            }
        });



        myHolder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               Toast.makeText(context,"Under Development",Toast.LENGTH_LONG).show();



            /*  MapDialogFragment mapDialogFragment = new MapDialogFragment();
                mapDialogFragment.show(, "sds");*/

                FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
                MapDialogFragment pcf = new MapDialogFragment();
                pcf.setvalues(current);

                pcf.show(fm, "");

            }
        });



    }



    @Override
    public int getItemCount() {
        return eventListDetailsModels.size();
    }





    @Override
    public int getItemViewType(int position) {
        return position;

    }



    class MyHolder extends RecyclerView.ViewHolder {

        public  TextView txt_shop_country,txt_shop_name,txt_shop_category,txt_shop_rating,txt_shop_address,txt_shop_mobile,txt_shop_link,txt_open_time,txt_wowsome,txt_call,wowsomes;
        ImageView img_provider_logo,location;
        MaterialRatingBar show_ratingbar;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/segoeui.ttf");
            txt_shop_name = (TextView) itemView.findViewById(R.id.txt_shop_name);
            txt_shop_country=itemView.findViewById(R.id.txt_shop_country);
            txt_shop_category = (TextView) itemView.findViewById(R.id.txt_shop_category);
            txt_shop_rating = (TextView) itemView.findViewById(R.id.txt_shop_rating);
            txt_shop_address = (TextView) itemView.findViewById(R.id.txt_shop_address);
            txt_shop_mobile = (TextView) itemView.findViewById(R.id.txt_shop_mobile);
            txt_shop_link = (TextView) itemView.findViewById(R.id.txt_shop_link);
            img_provider_logo=(ImageView)itemView.findViewById(R.id.img_provider_logo);
            location=(ImageView)itemView.findViewById(R.id.location);
            txt_open_time=(TextView)itemView.findViewById(R.id.txt_open_time);
            txt_wowsome=(TextView)itemView.findViewById(R.id.txt_wowsome);
            txt_call=(TextView)itemView.findViewById(R.id.txt_call);
            wowsomes=itemView.findViewById(R.id.wowsomes);
            show_ratingbar=itemView.findViewById(R.id.show_ratingbar);


            txt_shop_name.setTypeface(tf);
            txt_shop_category.setTypeface(tf);
            txt_shop_country.setTypeface(tf);
            txt_shop_rating.setTypeface(tf);
            txt_shop_address.setTypeface(tf);
            txt_shop_mobile.setTypeface(tf);
            txt_shop_link.setTypeface(tf);
            txt_open_time.setTypeface(tf);
            txt_call.setTypeface(tf);
            txt_wowsome.setTypeface(tf);
            wowsomes.setTypeface(tf);
        }
    }
}
