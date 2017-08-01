package com.wowhubb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.sloop.fonts.FontsManager;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

/**
 * Created by Ramya on 18-07-2017.
 */

public class RegisterCountryActivity extends Activity {

    String[] SPINNERLIST = {"Afghanistan","Australia","Bahrain","Canada","United States", "United Kingdom", "India"};
    String[] CODE_SPN = {"123", "423", "625", "435", "422"};
    Typeface latoheading,lato;
    TextView head_tv;
    TextInputLayout phone_til;
    ImageView submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_country);


        latoheading = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/latoheading.ttf");
        lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        head_tv=(TextView) findViewById(R.id.head_tv);
        submit=(ImageView)findViewById(R.id.submit_iv);

        phone_til=(TextInputLayout) findViewById(R.id.til_phone);
        head_tv.setTypeface(latoheading);
        phone_til.setTypeface(lato);




        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
                findViewById(R.id.android_material_design_spinner);
      /*  ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);

        materialDesignSpinner.setAdapter(arrayAdapter);*/

        MaterialBetterSpinner code_spn = (MaterialBetterSpinner)
                findViewById(R.id.code_spn);
        ArrayAdapter<String> codearrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, CODE_SPN);
        code_spn.setAdapter(codearrayAdapter);


        final CustomAdapter arrayAdapter = new CustomAdapter(RegisterCountryActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNERLIST) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
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
                tv.setPadding(10, 15, 10, 15);
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
                tv.setPadding(5, 0, 0, 0);
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

       // materialDesignSpinner.setPaddingSafe(0, 0, 0, 0);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(RegisterCountryActivity.this, LoginOtpActivity.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

    }
}
