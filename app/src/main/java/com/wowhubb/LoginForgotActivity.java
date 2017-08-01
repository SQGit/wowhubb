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

public class LoginForgotActivity extends Activity {

    String[] SPINNERLIST = {"Afghanistan","Australia","Bahrain","Canada","United States", "United Kingdom", "India"};
    String[] CODE_SPN = {"123", "423", "625", "435", "422"};
    Typeface latoheading,lato;
    TextView head_tv;
    TextInputLayout otp_til;
    ImageView submit,backiv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpwd);
/*
        FontsManager.initFormAssets(LoginForgotActivity.this, "fonts/lato.ttf");
        FontsManager.changeFonts(LoginForgotActivity.this);*/

        latoheading = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/latoheading.ttf");
        lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        head_tv=(TextView) findViewById(R.id.head_tv);
        otp_til=(TextInputLayout) findViewById(R.id.til_otp);
        submit=(ImageView)findViewById(R.id.submit_iv);
        backiv=(ImageView)findViewById(R.id.backiv);
        head_tv.setTypeface(latoheading);
        otp_til.setTypeface(lato);

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
                /*startActivity(new Intent(LoginForgotActivity.this, RegisterAllDetailsActivity.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);*/
            }
        });

    }
}
