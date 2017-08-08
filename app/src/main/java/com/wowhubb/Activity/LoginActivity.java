package com.wowhubb.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;


/**
 * Created by Ramya on 18-07-2017.
 */

public class LoginActivity extends Activity {


    Typeface latoheading,lato;
    TextView head_tv,forgotpwd_tv;
    TextInputLayout otp_til;
    ImageView submit,backiv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(LoginActivity.this,v1);

        latoheading = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/latoheading.ttf");
        lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        head_tv=(TextView) findViewById(R.id.head_tv);
        forgotpwd_tv=(TextView) findViewById(R.id.forgot_pwd_tv);
        backiv=(ImageView)findViewById(R.id.backiv);
        otp_til=(TextInputLayout) findViewById(R.id.til_otp);
        submit=(ImageView)findViewById(R.id.submit_iv);
        head_tv.setTypeface(latoheading);
        otp_til.setTypeface(lato);
        forgotpwd_tv.setTypeface(lato);

        forgotpwd_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, LoginForgotActivity.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

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
             startActivity(new Intent(LoginActivity.this, InterestActivity.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

            }


        });

    }
}
