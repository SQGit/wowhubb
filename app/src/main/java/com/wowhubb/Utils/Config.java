package com.wowhubb.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


/**
 * Created by Admin on 22-03-2017.
 */
public class Config {

    public static final String WEB_URL_LOGIN = "http://104.197.80.225:3010/wow/login";
    public static final String WEB_URL_EMAILLOGIN = "http://104.197.80.225:3010/wow/emaillogin";
    public static final String WEB_URL_OTP= "http://104.197.80.225:3010/wow/otp";
    public static final String WEB_URL_MAILOTP= "http://104.197.80.225:3010/wow/mailotp";
    public static final String WEB_URL_SIGNUP = "http://104.197.80.225:3010/wow/signup";
    public static final String WEB_URL_PROFILE_IMG="http://104.197.80.225:3010/wow/user/personalimage";
    public static final String WEB_URL_COVER_IMG="http://104.197.80.225:3010/wow/user/personalcover";
    public static final String WEB_URL_UPDATE_PERSONAL="http://104.197.80.225:3010/wow/user/updatepersonalprofile";
    public static final String WEB_URL_GETPERSONAL="http://104.197.80.225:3010/wow/user/getpersonalprofile";
    public static final String WEB_URL_GETCHECKTAG="http://104.197.80.225:3010/wow/checktagid";
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
    public static boolean isStringNullOrWhiteSpace(String value) {
        if (value == null) {
            return true;
        }

        for (int i = 0; i < value.length(); i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }

        return true;
    }






}
