package com.wowhubb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sloop.fonts.FontsManager;

/**
 * Created by Ramya on 18-07-2017.
 */

public class RegisterAllDetailsActivity extends Activity {

    Typeface latoheading,lato;
    TextView head_tv;
    TextInputLayout fname_til,lname_til,pwd_til,repwd_til,email_til;
    ImageView submit,backiv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_alldetails);

      /*  FontsManager.initFormAssets(RegisterAllDetailsActivity.this, "fonts/lato.ttf");
        FontsManager.changeFonts(RegisterAllDetailsActivity.this);*/

        latoheading = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/latoheading.ttf");
        lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        head_tv=(TextView) findViewById(R.id.head_tv);
        fname_til=(TextInputLayout) findViewById(R.id.til_fname);
        lname_til=(TextInputLayout) findViewById(R.id.til_lastname);
        pwd_til=(TextInputLayout) findViewById(R.id.til_pwd);
        repwd_til=(TextInputLayout) findViewById(R.id.til_repwd);
        email_til=(TextInputLayout) findViewById(R.id.til_email);
        submit=(ImageView)findViewById(R.id.submit_iv);
        backiv=(ImageView)findViewById(R.id.backiv);
        head_tv.setTypeface(latoheading);
        fname_til.setTypeface(lato);
        lname_til.setTypeface(lato);
        pwd_til.setTypeface(lato);
        repwd_til.setTypeface(lato);
        email_til.setTypeface(lato);

        backiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(RegisterAllDetailsActivity.this, RegisterBdayActivity.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

    }
}
