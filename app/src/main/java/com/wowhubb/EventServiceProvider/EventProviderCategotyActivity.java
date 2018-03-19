package com.wowhubb.EventServiceProvider;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

import java.util.ArrayList;


/**
 * Created by Guna on 29-11-2017.
 */

public class EventProviderCategotyActivity extends AppCompatActivity {
    MaterialBetterSpinner category_spn;
    Typeface lato;
    public static TextView btn_next,btn_cancel,txt_provider_head;
    Intent i = getIntent();
    ArrayList<String> category_list = new ArrayList<>();
    String str_provider,str_category;
    ImageView img_back;
    TextInputLayout til_spincategory;
    Snackbar snackbar;
    TextView tv_snack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_category_sublist);
        lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/segoeui.ttf");
        getSupportActionBar().hide();

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(EventProviderCategotyActivity.this, v1);


        Intent i = getIntent();
        category_list = i.getStringArrayListExtra("category_list");
        str_provider = i.getStringExtra("provider");
        Log.e("tag","66666666"+str_provider);


        til_spincategory=findViewById(R.id.til_spincategory);
        category_spn = (MaterialBetterSpinner) findViewById(R.id.category_spn);
        txt_provider_head=(TextView)findViewById(R.id.txt_provider_head);
        btn_cancel=(TextView)findViewById(R.id.btn_cancel);
        btn_next=(TextView)findViewById(R.id.btn_next);
        img_back=(ImageView)findViewById(R.id.img_back);


        til_spincategory.setTypeface(lato);

        //--------------------------SNACKBAR-------------------------------------------------------//
        snackbar = Snackbar.make(findViewById(R.id.top), R.string.categoryError, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(lato);


      //declare spinner:
        final MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner) findViewById(R.id.category_spn);
        materialDesignSpinner.setTypeface(lato);


        //load item to spinner:
        final CustomAdapter2 arrayAdapter = new CustomAdapter2(EventProviderCategotyActivity.this, android.R.layout.simple_dropdown_item_1line, category_list) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    SpannableString s = new SpannableString(str_provider);
                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    materialDesignSpinner.setHint(s);


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
                tv.setTypeface(lato);
                tv.setTextSize(14);
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
                tv.setPadding(10, 20, 0, 20);
                tv.setTypeface(lato);
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        materialDesignSpinner.setAdapter(arrayAdapter);

        if(str_provider.equals("Catering Services"))
        {
            txt_provider_head.setText("Catering Services");
            materialDesignSpinner.setHint("Catering Services Category");
        }
        else if(str_provider.equals("Cakes"))
        {
            txt_provider_head.setText("Cakes");
            materialDesignSpinner.setHint("Cakes Category");
        }
        else if(str_provider.equals("Wines"))
        {
            txt_provider_head.setText("Wines");
            materialDesignSpinner.setHint("Wines Category");
        }
        else if(str_provider.equals("BBQ Indoor & Outoor"))
        {
            txt_provider_head.setText("BBQ Indoor & Outoor");
            materialDesignSpinner.setHint("BBQ Indoor & Outoor Category");
        }
        else if(str_provider.equals("Music & DJ Services"))
        {
            txt_provider_head.setText("Music & DJ Services");
            materialDesignSpinner.setHint("Music & DJ Services Category");
        }
        else if(str_provider.equals("Event Organizer"))
        {
            txt_provider_head.setText("Event Organizer");
            materialDesignSpinner.setHint("Event Organizer Category");

        }
        else if(str_provider.equals("Beauty Services"))
        {
            txt_provider_head.setText("Beauty Services");
            materialDesignSpinner.setHint("Beauty Services Category");
        }
        else if(str_provider.equals("Photography & Video Services"))
        {
            txt_provider_head.setText("Photography & Video Services");
            materialDesignSpinner.setHint("Photography & Video Services Category");
        }
        else if(str_provider.equals("Decor & Party Rentals"))
        {
            txt_provider_head.setText("Decor & Party Rentals");
            materialDesignSpinner.setHint("Decor & Party Rentals Category");
        }
        else if(str_provider.equals("Floral"))
        {
            txt_provider_head.setText("Floral");
            materialDesignSpinner.setHint("Floral Category");
        }
        else if(str_provider.equals("Party Cleaning Services"))
        {
            txt_provider_head.setText("Party Cleaning Services");
            materialDesignSpinner.setHint("Party Cleaning Services Category");
        }

        else if(str_provider.equals("Limo & Car Rental Services"))
        {
            txt_provider_head.setText("Limo & Car Rental Services");
            materialDesignSpinner.setHint("Limo & Car Rental Services Category");
        }
        else if(str_provider.equals("Dress & Jewellery"))
        {
            txt_provider_head.setText("Dress & Jewellery");
            materialDesignSpinner.setHint("Dress & Jewellery Category");
        }
        else if(str_provider.equals("MC's & Comedians"))
        {
            txt_provider_head.setText("MC's & Comedians");
            materialDesignSpinner.setHint("MC's & Comedians Category");
        }
        else if(str_provider.equals("Event Guest Speakers"))
        {
            txt_provider_head.setText("Event Guest Speakers");
            materialDesignSpinner.setHint("Event Guest Speakers Category");
        }
        else if(str_provider.equals("Event Technology Support"))
        {
            txt_provider_head.setText("Event Technology Support");
            materialDesignSpinner.setHint("Event Technology Support Category");
        }
        else if(str_provider.equals("Event Gifting Souveniers"))
        {
            txt_provider_head.setText("Event Gifting Souveniers");
            materialDesignSpinner.setHint("Event Gifting Souveniers Category");
        }


//check spinner condition:
        materialDesignSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                materialDesignSpinner.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
                FontsOverride.overrideFonts(EventProviderCategotyActivity.this, view);
                str_category = adapterView.getItemAtPosition(i).toString();



            }
        });






        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(EventProviderCategotyActivity.this, EventServiceProviderActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                finish();

            }
        });



        btn_next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_next.setTextColor(Color.parseColor("#ffffff"));
                        btn_next.setBackgroundResource(R.drawable.fill_square);
                        if (str_category != null) {

                            Intent intent = new Intent(EventProviderCategotyActivity.this, EventListDetailsNewActivity.class);
                            intent.putExtra("provider", str_provider);
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {

                            snackbar.show();
                        }

                        return true;
                    case MotionEvent.ACTION_UP:
                        btn_next.setTextColor(Color.parseColor("#000000"));
                        btn_next.setBackgroundResource(R.drawable.normalsquare);
                        return true;
                }
                return false;
            }
        });




        btn_cancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_cancel.setTextColor(Color.parseColor("#ffffff"));
                        btn_cancel.setBackgroundResource(R.drawable.fill_square);


                        startActivity(new Intent(EventProviderCategotyActivity.this, EventServiceProviderActivity.class));
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        finish();
                        return true;
                    case MotionEvent.ACTION_UP:
                        btn_cancel.setTextColor(Color.parseColor("#000000"));
                        btn_cancel.setBackgroundResource(R.drawable.normalsquare);
                        return true;
                }
                return false;
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EventProviderCategotyActivity.this, EventServiceProviderActivity.class));
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        finish();

    }
}
