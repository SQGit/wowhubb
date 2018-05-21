package com.wowhubb.Activity;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.wowhubb.Adapter.CustomAdapter;
import com.wowhubb.Adapter.ExpandableListAdapter;
import com.wowhubb.Adapter.ExpandableListDataPump;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.GetFilePathFromDevice;
import com.wowhubb.R;
import com.wowhubb.Utils.Config;
import com.wowhubb.Utils.Utils;
import com.wowhubb.data.EventData;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * Created by Salman on 01-03-2018.
 */

public class QuickCreateEvent extends Activity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int INTENT_REQUEST_GET_VIDEO1 = 12;
    private static final int INTENT_REQUEST_GET_VIDEO11 = 13;
    private static final int INTENT_REQUEST_GET_GALERYVIDEO1 = 14;
    public static MaterialBetterSpinner event_spn;
    public static EditText eventname_et, eventdate_et, eventdateto_et, eventtopic_et, eventaddcategory_et, eventdesc_et;
    public static String date, str_category, str_eventday, strDateFormat1, strDateFormat2;
    public static TextInputLayout til_eventtimeto, til_eventtime, til_event_spn, til_desc, til_spincategory, til_spindays, eventdate_til, eventtopic_til, eventaddcategory_til, eventtimezone_til, eventdateto_til;
    public static String selectedVideoFilePath1;
     static AutoCompleteTextView eventtimezone_et;
    public static Calendar selectedCal, selectedCalto, wowtagfrom, wowtagto;
    public static FrameLayout framestart, frameend;
    public static ScrollView scrollView;
    public static EditText runfromtime_tv, runtotime_tv, telepasscode_et, webinarlink_et, telephone_et;
    public static TextInputLayout til_from, til_to, til_eventtile;
    public EventData eventData;
    String token;
    ImageView tickiv, video0_iv, video1plus;
    LinearLayout layout_eventdate, layout_eventdateto, nextlv;
    LinearLayout fromlv, tolv, layout_fromtime, layout_totime, layout_runfromdate, layout_runtodate, backiv;
    TextView txt_fromgallery, txt_takevideo;
    EditText eventstarttime, eventendtime;
    Typeface lato;
    TextView helpfultips_tv, publishtv;
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;
    Dialog dialog, cdialog, ctimedialog, loader_dialog, comments_dialog;
    String[] SPINNERLIST = {"Anniversary", "Baby Shower", "Birthday Party", "Holiday Party", "Wedding", "Baby Naming", "Graduation Party", "Team Outing", "Photo/File Shoot Event", "Bridal Shower Event", "Meeting", "Others", "Group Meetings"};
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    FrameLayout framevideo1;
    DatePicker datePicker, dp;
    CheckBox onlineevent_cb;
    String[] EVENTLIST = {"Webinar", "Teleconference", "Podcast"};
    String eventstartdate, eventenddate, eventstart, eventend;
    int hour, min, hoursto, minto;
    private Uri fileUri, videoUri;

    public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            String path = context.getFilesDir().getAbsolutePath();
            File copyFile = new File(path + File.separator + fileName);
            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * returning image / video
     */
    private File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("tag", "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");

            //Make sure you close all streams.

        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");

        } else {
            return null;
        }

        return mediaFile;
    }

    /**
     * returning image / video
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        eventData = new EventData();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_quickevent);

        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(QuickCreateEvent.this);
        edit = sharedPrefces.edit();
        token = sharedPrefces.getString("token", "");
        edit.putString("onlinestatus", "false");
        edit.apply();

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(QuickCreateEvent.this, v1);

        lato = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");

        eventdate_et = findViewById(R.id.eventdate);
        eventdesc_et = findViewById(R.id.eventdesc_et);
        eventdateto_et = findViewById(R.id.eventdateto_et);
        eventtopic_til = findViewById(R.id.til_eventtopic);
        eventdate_til = findViewById(R.id.til_eventdate);
        eventdateto_til = findViewById(R.id.til_eventdateto);
        eventaddcategory_til = findViewById(R.id.til_eventaddcategory);
        eventtimezone_til = findViewById(R.id.til_eventtimezone);
        helpfultips_tv = findViewById(R.id.helpfultips_tv);
        til_spincategory = findViewById(R.id.til_spincategory);

        til_desc = findViewById(R.id.til_eventdesc);
        scrollView = findViewById(R.id.scrollView);

        fromlv =  findViewById(R.id.fromlv);
        tolv = findViewById(R.id.tolv);
        eventstarttime = findViewById(R.id.eventstarttime);
        eventendtime = findViewById(R.id.eventendtime);
        runfromtime_tv = findViewById(R.id.runfromtv);
        runtotime_tv = findViewById(R.id.runtotv);
        til_from = findViewById(R.id.til_from);
        til_to = findViewById(R.id.til_to);
        til_eventtime = findViewById(R.id.til_eventtime);
        til_eventtimeto = findViewById(R.id.til_eventtimeto);
        layout_runfromdate = findViewById(R.id.layout_runfromdate);
        layout_runtodate = findViewById(R.id.runlayout_todate);
        framevideo1 = findViewById(R.id.framevideo1);
        tickiv = findViewById(R.id.tickiv);
        eventname_et = findViewById(R.id.eventtitle_et);
        video0_iv = findViewById(R.id.video0_iv);
        video1plus = findViewById(R.id.video1plus_iv);
        publishtv = findViewById(R.id.publishtv);
        wowtagfrom = Calendar.getInstance();
        onlineevent_cb = findViewById(R.id.onlineevent_cb);
        eventtopic_et = findViewById(R.id.eventtopic_et);
        layout_eventdate = findViewById(R.id.layout_eventdate);
        layout_eventdateto = findViewById(R.id.layout_todate);
        layout_totime = findViewById(R.id.layout_totime);
        layout_fromtime = findViewById(R.id.layout_eventtime);

        eventaddcategory_et = findViewById(R.id.eventaddcategory_et);
        eventtimezone_et = findViewById(R.id.eventtimezone_et);
        nextlv = findViewById(R.id.nextlv);
        framestart = findViewById(R.id.frame_startdate);
        backiv = findViewById(R.id.back_lv);

        eventdate_til.setTypeface(lato);
        eventdateto_til.setTypeface(lato);
        eventtopic_til.setTypeface(lato);
        til_desc.setTypeface(lato);
        til_eventtime.setTypeface(lato);
        til_eventtimeto.setTypeface(lato);
        til_from.setTypeface(lato);
        til_to.setTypeface(lato);
        eventaddcategory_til.setTypeface(lato);
        eventtimezone_til.setTypeface(lato);
        til_spincategory.setTypeface(lato);
        selectedCal = Calendar.getInstance();

        selectedCalto = Calendar.getInstance();
        wowtagto = Calendar.getInstance();


        loader_dialog = new Dialog(QuickCreateEvent.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);


        backiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        eventtimezone_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    hideKeyboard();
                    eventdate_til.setError("");
                    layout_eventdate.setFocusable(true);
                }
                return true;
            }
        });


        onlineevent_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("tag", "11111-------" + isChecked);
                if (isChecked) {

                    SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor edit = sharedPrefces.edit();
                    edit.putString("onlinestatus", "true");
                    edit.apply();
                    comments_dialog = new Dialog(QuickCreateEvent.this);
                    comments_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    comments_dialog.setContentView(R.layout.dialog_onlineevent);
                    View v2 = comments_dialog.getWindow().getDecorView().getRootView();
                    FontsOverride.overrideFonts(QuickCreateEvent.this, v2);
                    ImageView close = comments_dialog.findViewById(R.id.closeiv);
                    final LinearLayout webinarlv = comments_dialog.findViewById(R.id.webinar_lv);
                    final LinearLayout telelv = comments_dialog.findViewById(R.id.tele_lv);
                    TextView submittv = comments_dialog.findViewById(R.id.submittv);
                    webinarlink_et = comments_dialog.findViewById(R.id.webinarlink_et);
                    telephone_et = comments_dialog.findViewById(R.id.teleconphone_et);
                    telepasscode_et = comments_dialog.findViewById(R.id.passcode_et);

                    submittv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            comments_dialog.dismiss();
                            SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor edit = sharedPrefces.edit();
                            edit.putString("str_eventtype", str_category);
                            edit.putString("webinar_link", webinarlink_et.getText().toString().trim());
                            edit.putString("str_tele_phone", telephone_et.getText().toString().trim());
                            edit.putString("str_tele_passcode", telepasscode_et.getText().toString().trim());
                            edit.apply();
                        }
                    });

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            comments_dialog.dismiss();
                        }
                    });

                    til_event_spn = comments_dialog.findViewById(R.id.til_spnevents);
                    TextInputLayout til_webinar_link = comments_dialog.findViewById(R.id.til_webinar_link);
                    TextInputLayout til_telecon_phone = comments_dialog.findViewById(R.id.til_telecon_phone);
                    TextInputLayout til_passcode = comments_dialog.findViewById(R.id.til_passcode);
                    event_spn = comments_dialog.findViewById(R.id.category_spn);

                    FontsOverride.overrideFonts(comments_dialog.getContext(), v2);

                    til_event_spn.setTypeface(lato);
                    til_webinar_link.setTypeface(lato);
                    til_telecon_phone.setTypeface(lato);
                    til_passcode.setTypeface(lato);

                    final CustomAdapter eventdayAdapter = new CustomAdapter(comments_dialog.getContext(), android.R.layout.simple_dropdown_item_1line, EVENTLIST) {
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
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
                            tv.setTextSize(9);
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
                            tv.setTextSize(15);
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
                    event_spn.setAdapter(eventdayAdapter);
                    comments_dialog.show();

                    event_spn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            FontsOverride.overrideFonts(comments_dialog.getContext(), view);
                            str_category = adapterView.getItemAtPosition(i).toString();
                            Log.e("tag", "str_category------>" + str_category);
                            event_spn.requestFocus();
                            event_spn.performClick();
                            if (str_category.equals("Webinar")) {
                                webinarlv.setVisibility(View.VISIBLE);
                                telelv.setVisibility(View.GONE);
                            } else if (str_category.equals("Teleconference")) {
                                telelv.setVisibility(View.VISIBLE);
                                webinarlv.setVisibility(View.GONE);
                            } else if (str_category.equals("Podcast")) {
                                telelv.setVisibility(View.VISIBLE);
                                webinarlv.setVisibility(View.GONE);
                            } else {
                                telelv.setVisibility(View.GONE);
                                webinarlv.setVisibility(View.GONE);
                            }

                        }
                    });


                } else {
                    comments_dialog.dismiss();
                    SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor edit = sharedPrefces.edit();
                    edit.putString("onlinestatus", "false");
                    edit.apply();
                }

            }
        });

        nextlv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!eventtopic_et.getText().toString().trim().equalsIgnoreCase("")) {
                    eventtopic_til.setError(null);
                    if (str_category != null) {

                        if (!eventdesc_et.getText().toString().trim().equalsIgnoreCase("")) {
                            til_desc.setError(null);
                            if (!eventtimezone_et.getText().toString().trim().equalsIgnoreCase("")) {
                                eventtimezone_til.setError(null);
                                if (!eventdate_et.getText().toString().trim().equalsIgnoreCase("")) {
                                    eventdate_til.setError(null);
                                    if (!eventdateto_et.getText().toString().trim().equalsIgnoreCase("")) {
                                        if (!eventstarttime.getText().toString().trim().equalsIgnoreCase("")) {

                                            if (!eventendtime.getText().toString().trim().equalsIgnoreCase("")) {

                                                if (!eventname_et.getText().toString().trim().equalsIgnoreCase("")) {
                                                eventname_et.setError(null);
                                                if (selectedVideoFilePath1 != null) {
                                                    if (!runfromtime_tv.getText().toString().trim().equalsIgnoreCase("")) {
                                                        til_from.setError(null);
                                                        if (!runtotime_tv.getText().toString().trim().equalsIgnoreCase("")) {
                                                            til_to.setError(null);
                                                            Log.e("tag", "hgchgxc");
                                                            SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                                            SharedPreferences.Editor edit = sharedPrefces.edit();
                                                            edit.putString("str_eventtopic", eventtopic_et.getText().toString().trim());
                                                            edit.putString("str_category", str_category);
                                                            edit.putString("str_eventday", str_eventday);
                                                            edit.putString("str_desc", eventdesc_et.getText().toString().trim());
                                                            edit.putString("str_city", eventtimezone_et.getText().toString().trim());
                                                            edit.putString("str_eventname", eventname_et.getText().toString().trim());
                                                            edit.putString("str_wowtag", selectedVideoFilePath1);
                                                            edit.putString("str_startdate", eventstartdate + " " + eventstart);
                                                            edit.putString("str_enddate", eventenddate + " " + eventend);
                                                            edit.putString("str_runfrom", "");
                                                            edit.putString("str_runto", "");
                                                            edit.apply();
                                                            startActivity(new Intent(QuickCreateEvent.this, QuickCreateEventVenues.class));
                                                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                                                        } else {
                                                            SpannableString s = new SpannableString("Select Run To Date");
                                                            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            Toast.makeText(QuickCreateEvent.this, s, Toast.LENGTH_LONG).show();
                                                        }

                                                    } else {
                                                        SpannableString s = new SpannableString("Select Run From Date");
                                                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                        Toast.makeText(QuickCreateEvent.this, s, Toast.LENGTH_LONG).show();
                                                    }
                                                } else {
                                                    SpannableString s = new SpannableString("Select Wowtag Video");
                                                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                                                }


                                            } else {
                                                SpannableString s = new SpannableString("Enter Event Title");
                                                s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                eventname_et.setError(s);
                                                eventname_et.requestFocus();
                                            }
                                            } else {
                                                SpannableString s = new SpannableString("Select Event End Time");
                                                s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                                            }
                                        } else {
                                            SpannableString s = new SpannableString("Select Event Start Time");
                                            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                                        }
                                    } else {
                                        SpannableString s = new SpannableString("Select Event End Date");
                                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                                    }
                                } else {
                                    SpannableString s = new SpannableString("Select Event Start Date");
                                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();


                                }



                            } else {

                                SpannableString s = new SpannableString("Enter Event City");
                                s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                eventtimezone_til.setError(s);
                                eventtimezone_et.requestFocus();
                            }
                        } else {

                            SpannableString s = new SpannableString("Enter Event Description");
                            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            til_desc.setError(s);
                            eventdesc_et.requestFocus();
                        }

                    } else {
                        SpannableString s = new SpannableString("Select Event Category");
                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    SpannableString s = new SpannableString("Enter Event Name");
                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    eventtopic_til.setError(s);
                    eventtopic_et.setFocusable(true);
                    eventtopic_et.requestFocus();
                    scrollView.scrollTo(0, scrollView.getTop());
                }


            }


        });

        layout_fromtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!eventdate_et.getText().toString().trim().equalsIgnoreCase("")) {
                    if (!eventdateto_et.getText().toString().trim().equalsIgnoreCase("")) {
                ctimedialog = new Dialog(QuickCreateEvent.this);
                ctimedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                ctimedialog.setContentView(R.layout.dialog_customtime);
                View v1 = ctimedialog.getWindow().getDecorView().getRootView();

                final TimePicker dp = ctimedialog.findViewById(R.id.timePicker);

                //dp.setIs24HourView(true);
                TextView ok = ctimedialog.findViewById(R.id.oktv);
                TextView cancel = ctimedialog.findViewById(R.id.canceltv);

                FontsOverride.overrideFonts(ctimedialog.getContext(), v1);


                ctimedialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ctimedialog.getWindow();

                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        ctimedialog.dismiss();
                    }
                });


                ok.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {

                        hour = 0;
                        min = 0;

                        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                        if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                            hour = dp.getHour();
                            min = dp.getMinute();
                        } else {
                            hour = dp.getCurrentHour();
                            min = dp.getCurrentMinute();
                        }

                        ctimedialog.dismiss();
                        populateSetTime(hour, min);
                    }
                });
                ctimedialog.show();
                    } else {
                        SpannableString s = new SpannableString("Select Event End Date");
                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                    }
                } else {
                    SpannableString s = new SpannableString("Select Event Start Date");
                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();


                }

            }
        });
// function
        // purpose
        // creaqted date:
        // altered date;  pupose:
        layout_totime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!eventdate_et.getText().toString().trim().equalsIgnoreCase("")) {
                    if (!eventdateto_et.getText().toString().trim().equalsIgnoreCase("")) {
                        if (!eventstarttime.getText().toString().trim().equalsIgnoreCase("")) {
                            ctimedialog = new Dialog(QuickCreateEvent.this);
                            ctimedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            ctimedialog.setContentView(R.layout.dialog_customtime);
                            View v1 = ctimedialog.getWindow().getDecorView().getRootView();

                            final TimePicker dp = ctimedialog.findViewById(R.id.timePicker);
                            TextView ok = ctimedialog.findViewById(R.id.oktv);
                            TextView cancel = ctimedialog.findViewById(R.id.canceltv);
                            dp.setIs24HourView(false);

                            FontsOverride.overrideFonts(ctimedialog.getContext(), v1);

                            try {
                                if (eventstartdate.equals(eventenddate)) {
                                    int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                                    if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                                        dp.setHour(hour);
                                        dp.setMinute(min);

                                    } else {
                                        dp.setCurrentHour(hour);
                                        dp.setCurrentMinute(min);

                                    }
                                }
                            }
                            catch (NullPointerException e)
                            {
                                Log.e("tag","exc"+e.toString());

                            }


                            ctimedialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialogInterface) {
                                    Window view1 = ((Dialog) ctimedialog).getWindow();

                                }
                            });

                            cancel.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View view) {
                                    ctimedialog.dismiss();
                                }
                            });


                            ok.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View view) {

                                    hoursto = 0;
                                    minto = 0;
                                    eventendtime.setText("");
                                    int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                                    try {
                                    if (eventstartdate.equals(eventenddate)) {
                                        if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                                            hoursto = dp.getHour();
                                            minto = dp.getMinute();
                                            double s = hoursto + (0.0 + minto);
                                            double s1 = hour + (0.0 + min);
                                            if (s > s1) {
                                                populateSetTimeTo(hoursto, minto);
                                            }


                                        } else {
                                            hoursto = dp.getCurrentHour();
                                            minto = dp.getCurrentMinute();
                                            double s = hoursto + (0.0 + minto);
                                            double s1 = hour + (0.0 + min);
                                            if (s > s1) {
                                                populateSetTimeTo(hoursto, minto);
                                            }
                                        }
                                    } else {
                                        if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                                            hoursto = dp.getHour();
                                            minto = dp.getMinute();
                                            populateSetTimeTo(hoursto, minto);
                                        } else {
                                            hoursto = dp.getCurrentHour();
                                            minto = dp.getCurrentMinute();
                                            populateSetTimeTo(hoursto, minto);
                                        }
                                    }

                                    }
                                    catch (NullPointerException e)
                                    {
                                        Log.e("tag","exc"+e.toString());
                                    }
                                    ctimedialog.dismiss();


                                }
                            });
                            ctimedialog.show();
                        } else {
                            SpannableString s = new SpannableString("Select Event Start Time");
                            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                        }
                    } else {
                        SpannableString s = new SpannableString("Select Event End Date");
                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                    }
                } else {
                    SpannableString s = new SpannableString("Select Event Start Date");
                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();


                }

            }
        });

        layout_eventdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                eventdate_til.setError("");
                cdialog = new Dialog(QuickCreateEvent.this);
                cdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                cdialog.setContentView(R.layout.dialog_customcalendar);
                View v1 = cdialog.getWindow().getDecorView().getRootView();
                dp = cdialog.findViewById(R.id.datePicker);
                TextView ok = cdialog.findViewById(R.id.oktv);
                TextView cancel = cdialog.findViewById(R.id.canceltv);
                FontsOverride.overrideFonts(cdialog.getContext(), v1);
                dp.setMinDate(System.currentTimeMillis() - 1000);
                cdialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = cdialog.getWindow();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cdialog.dismiss();
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        int day = dp.getDayOfMonth();
                        int month = dp.getMonth();
                        int year = dp.getYear();
                        wowtagfrom = Calendar.getInstance();
                        selectedCal.set(year, month, day);
                        wowtagfrom.set(year, month, day);
                        Log.e("tag", "values---------------------->>>>>>>" + day + month + year);
                        populateSetDate(year, month + 1, day);
                        cdialog.dismiss();
                    }
                });
                cdialog.show();

            }
        });


        layout_eventdateto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!eventdate_et.getText().toString().trim().equalsIgnoreCase("")) {
                    eventdate_til.setError(null);
                    eventdateto_til.setError(null);
                    cdialog = new Dialog(QuickCreateEvent.this);
                    cdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    cdialog.setContentView(R.layout.dialog_customcalendar);
                    View v1 = cdialog.getWindow().getDecorView().getRootView();
                    dp = cdialog.findViewById(R.id.datePicker);
                    TextView ok = cdialog.findViewById(R.id.oktv);
                    TextView cancel = cdialog.findViewById(R.id.canceltv);
                    FontsOverride.overrideFonts(cdialog.getContext(), v1);
                    try {
                        //long selectedMilli = wowtagfrom.getTimeInMillis();
                        //    dp.setMinDate(selectedMilli - 1000);
                        //  dp.setMinDate(System.currentTimeMillis() - 1000);
                        long selectedMillisec = wowtagfrom.getTimeInMillis();
                        dp.setMinDate(selectedMillisec - 1000);
                    } catch (NullPointerException e) {
                        Log.e("tag","exc"+e.toString());
                    }

                    cdialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            Window view1 = cdialog.getWindow();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cdialog.dismiss();
                        }
                    });

                    ok.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            int day = dp.getDayOfMonth();
                            int month = dp.getMonth();
                            int year = dp.getYear();
                            wowtagto = Calendar.getInstance();
                            selectedCal.set(year, month, day);
                            wowtagto.set(year, month, day);
                            Log.e("tag", "values---------------------->>>>>>>" + day + month + year);
                            populateSetDateTo(year, month + 1, day);
                            cdialog.dismiss();
                        }
                    });
                    cdialog.show();


                } else {
                    SpannableString s = new SpannableString("Select Event Start Date");
                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                }
            }
        });

        helpfultips_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new Dialog(QuickCreateEvent.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_helpfultips);
                View v1 = dialog.getWindow().getDecorView().getRootView();
                ImageView close = dialog.findViewById(R.id.closeiv);

                FontsOverride.overrideFonts(dialog.getContext(), v1);

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = dialog.getWindow();

                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                expandableListView = dialog.findViewById(R.id.expandableListView);
                expandableListDetail = ExpandableListDataPump.getData();
                expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                expandableListAdapter = new ExpandableListAdapter(dialog.getContext(), expandableListTitle, expandableListDetail);

                expandableListView.setAdapter(expandableListAdapter);

                expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    public void onGroupExpand(int groupPosition) {
                    }
                });

                expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                    public void onGroupCollapse(int groupPosition) {
                    }
                });

                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v,
                                                int groupPosition, int childPosition, long id) {
                        return true;
                    }
                });

                dialog.show();
            }
        });


        framevideo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new Dialog(QuickCreateEvent.this);
                dialog.setContentView(R.layout.dialog_takevideo);
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = dialog.getWindow();
                        view1.setBackgroundDrawableResource(R.drawable.border_bg);
                    }
                });
                txt_fromgallery =  dialog.findViewById(R.id.txt_fromgallery);
                //img_take_gallery = (ImageView) dialog.findViewById(R.id.img_take_gallery);
                txt_takevideo =  dialog.findViewById(R.id.txt_takevideo);
                //img_take_video = (ImageView) dialog.findViewById(R.id.img_take_video);
                ImageView image =  dialog.findViewById(R.id.imageDialog);
                final LinearLayout lnr_video = dialog.findViewById(R.id.lnr_video);
                final LinearLayout lnr_gallery =  dialog.findViewById(R.id.lnr_gallery);


                txt_fromgallery.setTextColor(Color.parseColor("#3c3c3c"));
                txt_takevideo.setTextColor(Color.parseColor("#3c3c3c"));
                txt_fromgallery.setText("From Gallery");
                txt_takevideo.setText("Take Video   ");

                lnr_gallery.setBackgroundResource(R.drawable.btn_bg_white);
                lnr_video.setBackgroundResource(R.drawable.btn_bg_white);
                txt_takevideo.setTypeface(lato);
                txt_fromgallery.setTypeface(lato);

                lnr_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                        lnr_gallery.setBackgroundResource(R.drawable.btnbg);
                        txt_fromgallery.setTextColor(Color.parseColor("#FFFFFF"));

                        lnr_video.setBackgroundResource(R.drawable.btn_bg_white);
                        txt_takevideo.setTextColor(Color.parseColor("#3c3c3c"));

                        if (checkPermission()) {
                            Intent intent = new Intent();
                            intent.setType("video/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Video"), INTENT_REQUEST_GET_GALERYVIDEO1);
                            //Toast.makeText(QuickCreateEvent.this, "Please choose less than 2 mb video", Toast.LENGTH_LONG).show();
                        }


                    }
                });


                lnr_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Log.e("tag", "111111------->>>>>>>>>NOUGAT");
                            if (checkPermission()) {
                                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                videoUri = getOutputMediaFileUri2(MEDIA_TYPE_VIDEO);
                                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri); // set the image file
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.putExtra("android.intent.extra.durationLimit", 120);
                                startActivityForResult(intent, INTENT_REQUEST_GET_VIDEO11);
                                dialog.dismiss();
                            }

                        } else {
                            Log.e("tag", "111111------->>>>>>>>>BELOWWWW");
                            lnr_video.setBackgroundResource(R.drawable.btnbg);
                            txt_takevideo.setTextColor(Color.parseColor("#FFFFFF"));
                            lnr_gallery.setBackgroundResource(R.drawable.btn_bg_white);
                            txt_fromgallery.setTextColor(Color.parseColor("#3c3c3c"));

                            if (checkPermission()) {
                                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.putExtra("android.intent.extra.durationLimit", 120);
                                startActivityForResult(intent, INTENT_REQUEST_GET_VIDEO1);
                                dialog.dismiss();
                            }
                        }

                    }
                });


                dialog.show();


            }
        });
        layout_runfromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!eventdate_et.getText().toString().trim().equalsIgnoreCase("")) {
                    runfromtime_tv.setError(null);
                    cdialog = new Dialog(QuickCreateEvent.this);
                    cdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    cdialog.setContentView(R.layout.dialog_customcalendar);
                    View v1 = cdialog.getWindow().getDecorView().getRootView();

                    final DatePicker dp = cdialog.findViewById(R.id.datePicker);
                    TextView ok = cdialog.findViewById(R.id.oktv);
                    TextView cancel = cdialog.findViewById(R.id.canceltv);
                    //  dp.setMinDate(System.currentTimeMillis() - 1000);
                    //   dp.setOnDateChangedListener(dateChangedListener);
                    FontsOverride.overrideFonts(cdialog.getContext(), v1);
                    Log.e("tag", "111----------" + wowtagto);
                    try {
                        long selectedMilli = wowtagfrom.getTimeInMillis();
                        //    dp.setMinDate(selectedMilli - 1000);
                        dp.setMinDate(System.currentTimeMillis() - 1000);
                        long selectedMillisec = wowtagto.getTimeInMillis();
                        dp.setMaxDate(selectedMillisec - 1000);
                    } catch (NullPointerException e) {
                        Log.e("tag","exc"+e.toString());
                    }


                    cdialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            Window view1 = cdialog.getWindow();

                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cdialog.dismiss();
                        }
                    });


                    ok.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            ///cdialog.dismiss();
                            int day = dp.getDayOfMonth();
                            int month = dp.getMonth();
                            int year = dp.getYear();
                            wowtagfrom.set(year, month, day);
                            runpopulateSetDate(year, month + 1, day);
                            cdialog.dismiss();
                        }
                    });
                    cdialog.show();

                } else {
                    SpannableString s = new SpannableString("Select Event Start Date");
                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                }
            }
        });

        layout_runtodate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!eventdate_et.getText().toString().trim().equalsIgnoreCase("")) {
                    if (!runfromtime_tv.getText().toString().trim().equalsIgnoreCase("")) {
                        runfromtime_tv.setError(null);
                        runtotime_tv.setError(null);
                        cdialog = new Dialog(QuickCreateEvent.this);
                        cdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        cdialog.setContentView(R.layout.dialog_customcalendar);
                        View v1 = cdialog.getWindow().getDecorView().getRootView();

                        final DatePicker dp = cdialog.findViewById(R.id.datePicker);
                        TextView ok = cdialog.findViewById(R.id.oktv);
                        TextView cancel = cdialog.findViewById(R.id.canceltv);

                        FontsOverride.overrideFonts(cdialog.getContext(), v1);
                        try {
                            long selectedMilli = wowtagfrom.getTimeInMillis();
                            dp.setMinDate(selectedMilli - 1000);

                            long selectedMillisec = wowtagto.getTimeInMillis();
                            dp.setMaxDate(selectedMillisec - 1000);
                        } catch (NullPointerException e) {

                            Log.e("tag","exc"+e.toString());
                        }
                        cdialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                Window view1 = cdialog.getWindow();

                            }
                        });

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cdialog.dismiss();
                            }
                        });


                        ok.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                ///cdialog.dismiss();
                                int day = dp.getDayOfMonth();
                                int month = dp.getMonth();
                                int year = dp.getYear();
                                runpopulateSetDateTo(year, month + 1, day);
                                cdialog.dismiss();
                            }
                        });
                        cdialog.show();
                    } else {
                        SpannableString s = new SpannableString("Select From Run Date");
                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                    }
                } else {
                    SpannableString s = new SpannableString("Select Event Start Date");
                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                }
            }
        });
        MaterialBetterSpinner materialDesignSpinner =findViewById(R.id.category_spn);
        View view1 = materialDesignSpinner.getRootView();
        FontsOverride.overrideFonts(QuickCreateEvent.this, view1);


        final CustomAdapter arrayAdapter = new CustomAdapter(QuickCreateEvent.this, android.R.layout.simple_dropdown_item_1line, SPINNERLIST) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
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
                tv.setTextSize(9);
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
                tv.setTextSize(15);
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
        materialDesignSpinner.setTypeface(lato);

        materialDesignSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                FontsOverride.overrideFonts(QuickCreateEvent.this, view);
                str_category = adapterView.getItemAtPosition(i).toString();
                if (str_category.equals("Others")) {
                    eventaddcategory_til.setVisibility(View.VISIBLE);
                    edit.putString("str_category", "others");
                    edit.commit();
                } else {
                    eventaddcategory_til.setVisibility(View.GONE);
                    edit.putString("str_category", str_category);
                    edit.commit();
                }
            }
        });

    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) QuickCreateEvent.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(QuickCreateEvent.this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(QuickCreateEvent.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(QuickCreateEvent.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(QuickCreateEvent.this, Manifest.permission.RECORD_AUDIO);

        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED)) {
            Log.e("tag", "Permission is granted");
            return true;
        } else {
            Log.e("tag", "Permission is revoked");
            ActivityCompat.requestPermissions(QuickCreateEvent.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }
    }

    private void populateSetDate(int year, int month, int day) {
        String strDateFormatTo = month + "/" + day + "/" + year;
        SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
        Date newDate = null;

        try {
            newDate = spf.parse(strDateFormatTo);

            spf = new SimpleDateFormat("MMM/dd/yyyy");

            strDateFormatTo = spf.format(newDate);
            eventdate_et.setText(strDateFormatTo);
            eventdateto_et.setText("");
            SimpleDateFormat spf1 = new SimpleDateFormat("yyyy/MM/dd");
            eventstartdate = spf1.format(newDate);
            Log.e("tag", "new Date------>" + eventstartdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void populateSetDateTo(int year, int month, int day) {
        String strDateFormatTo = month + "/" + day + "/" + year;
        SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
        Date newDate = null;

        try {
            newDate = spf.parse(strDateFormatTo);
            spf = new SimpleDateFormat("MMM/dd/yyyy");
            strDateFormatTo = spf.format(newDate);
            eventdateto_et.setText(strDateFormatTo);
            Log.e("tag", "date------------>" + strDateFormatTo);
            SimpleDateFormat spf1 = new SimpleDateFormat("yyyy/MM/dd");
            eventenddate = spf1.format(newDate);
            Log.e("tag", "new Date------>" + eventenddate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void populateSetTime(int hourOfDay, int minute) {
        //  strDateFormat1 = eventdate_et.getText().toString();
        SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
        Date newDate = null;

        String strDateFormat = String.format("%02d:%02d", hourOfDay, minute);
        strDateFormat = strDateFormat + " a";

        SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mm a");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");

        try {
            Date dt = parseFormat.parse(strDateFormat);
            System.out.println(outputFormat.format(dt));
            eventstarttime.setText("");
            eventstarttime.setText(outputFormat.format(dt));
            eventstart = (outputFormat.format(dt));


        } catch (ParseException exc) {
            exc.printStackTrace();
        }


    }

    private void populateSetTimeTo(int hourOfDay, int minute) {

        SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
        Date newDate = null;

        String strDateFormat = String.format("%02d:%02d", hourOfDay, minute);
        strDateFormat = strDateFormat + " a";

        SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mm a");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");

        try {
            Date dt = parseFormat.parse(strDateFormat);
            System.out.println(outputFormat.format(dt));

            eventendtime.setText("");
            eventendtime.setText(outputFormat.format(dt));
            eventend = (outputFormat.format(dt));
        } catch (ParseException exc) {
            exc.printStackTrace();
        }


    }

    private void runpopulateSetDate(int year, int month, int day) {
        String strDateFormatTo = month + "/" + day + "/" + year;
        SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
        Date newDate = null;

        try {
            newDate = spf.parse(strDateFormatTo);
            spf = new SimpleDateFormat("MMM/dd/yyyy");
            strDateFormatTo = spf.format(newDate);
            runfromtime_tv.setText(strDateFormatTo);
            Log.e("tag", "date------------>" + strDateFormatTo);


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void runpopulateSetDateTo(int year, int month, int day) {
        String strDateFormatTo = month + "/" + day + "/" + year;
        SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
        Date newDate = null;

        try {
            newDate = spf.parse(strDateFormatTo);
            spf = new SimpleDateFormat("MMM/dd/yyyy");
            strDateFormatTo = spf.format(newDate);
            runtotime_tv.setText(strDateFormatTo);
            Log.e("tag", "dvhdsvjvj" + strDateFormatTo);


            ctimedialog = new Dialog(QuickCreateEvent.this);
            ctimedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            ctimedialog.setContentView(R.layout.dialog_customtime);
            View v1 = ctimedialog.getWindow().getDecorView().getRootView();

            final TimePicker dp = ctimedialog.findViewById(R.id.timePicker);
            TextView ok = ctimedialog.findViewById(R.id.oktv);
            TextView cancel = ctimedialog.findViewById(R.id.canceltv);

            //   dp.setOnDateChangedListener(dateChangedListener);
            FontsOverride.overrideFonts(ctimedialog.getContext(), v1);

            ctimedialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    Window view1 = ctimedialog.getWindow();

                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ctimedialog.dismiss();
                }
            });


            ok.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                     hour = 0;
                     min = 0;
                    int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                    if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                        hour = dp.getHour();
                        min = dp.getMinute();
                    } else {
                        hour = dp.getCurrentHour();
                        min = dp.getCurrentMinute();
                    }

                    ctimedialog.dismiss();
                    // populateSetTimeTo(hour, min);
                }
            });
            // ctimedialog.show();
            //getActivity().showDialog(Time_id);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == INTENT_REQUEST_GET_VIDEO1) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    Uri selectedMediaUri = data.getData();
                    selectedVideoFilePath1 = GetFilePathFromDevice.getPath(QuickCreateEvent.this, selectedMediaUri);
                    Log.e("tag", "qqqqqqqqqqqqqqqqqq------------" + selectedVideoFilePath1);

                    MediaPlayer mp = new MediaPlayer();
                    int duration = 0;
                    try {
                        mp.setDataSource(selectedVideoFilePath1);
                        mp.prepare();
                        duration = mp.getDuration();
                    } catch (IOException e) {
                        Log.e(Utils.class.getName(), e.getMessage());
                    } finally {
                        mp.release();
                    }
                    Log.e("tag", "duration------------->" + duration);
                    Log.e("tag", "duration------------->" + duration / 1000);

                    if ((duration / 1000) > 30) {
                        video0_iv.setImageBitmap(null);
                        video0_iv.setImageDrawable(QuickCreateEvent.this.getDrawable(R.drawable.dotted_square));
                        video1plus.setImageDrawable(QuickCreateEvent.this.getDrawable(R.drawable.plus_icon));
                        SpannableString s = new SpannableString("Upload minimum 2 min Video");
                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        // til_eventdays.setError(s);
                        Toast.makeText(QuickCreateEvent.this, s, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                            video1plus.setImageDrawable(QuickCreateEvent.this.getDrawable(R.drawable.video_icon));
                            edit.putString("video1", selectedVideoFilePath1);
                            edit.commit();


                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (NullPointerException e) {
                    Log.e("tag","exc"+e.toString());
                }
            } else {

                selectedVideoFilePath1 = fileUri.getPath();
                Log.e("tag", "qqqqqqqqqqqqqqqqqq------------" + selectedVideoFilePath1);

                MediaPlayer mp = new MediaPlayer();
                int duration = 0;
                try {
                    mp.setDataSource(selectedVideoFilePath1);
                    mp.prepare();
                    duration = mp.getDuration();
                } catch (IOException e) {
                    Log.e(Utils.class.getName(), e.getMessage());
                } finally {
                    mp.release();
                }
                Log.e("tag", "duration------------->" + duration);
                Log.e("tag", "duration------------->" + duration / 1000);
                if ((duration / 1000) > 120) {
                    video0_iv.setImageBitmap(null);
                    video0_iv.setImageDrawable(QuickCreateEvent.this.getDrawable(R.drawable.dotted_square));
                    video1plus.setImageDrawable(QuickCreateEvent.this.getDrawable(R.drawable.plus_icon));
                    SpannableString s = new SpannableString("Upload minimum 2 min Video");
                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    // til_eventdays.setError(s);
                    Toast.makeText(QuickCreateEvent.this, s, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                        video1plus.setImageDrawable(QuickCreateEvent.this.getDrawable(R.drawable.video_icon));
                        edit.putString("video1", selectedVideoFilePath1);
                        edit.commit();


                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }


            }

        } else if (requestCode == INTENT_REQUEST_GET_VIDEO11) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    Uri selectedMediaUri = videoUri;
                    selectedVideoFilePath1 = getFilePathFromURI(QuickCreateEvent.this, selectedMediaUri);
                    Log.e("tag", "qqqqqqqqqqqqqqqqqq------------" + selectedVideoFilePath1);

                    MediaPlayer mp = new MediaPlayer();
                    int duration = 0;
                    try {
                        mp.setDataSource(selectedVideoFilePath1);
                        mp.prepare();
                        duration = mp.getDuration();
                    } catch (IOException e) {
                        Log.e(Utils.class.getName(), e.getMessage());
                    } finally {
                        mp.release();
                    }
                    Log.e("tag", "duration------------->" + duration);
                    Log.e("tag", "duration------------->" + duration / 1000);
                    if ((duration / 1000) > 120) {
                        video0_iv.setImageBitmap(null);
                        video0_iv.setImageDrawable(QuickCreateEvent.this.getDrawable(R.drawable.dotted_square));
                        video1plus.setImageDrawable(QuickCreateEvent.this.getDrawable(R.drawable.plus_icon));
                        SpannableString s = new SpannableString("Upload minimum 2 min Video");
                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        // til_eventdays.setError(s);
                        Toast.makeText(QuickCreateEvent.this, s, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                            video1plus.setImageDrawable(QuickCreateEvent.this.getDrawable(R.drawable.video_icon));
                            edit.putString("video1", selectedVideoFilePath1);
                            edit.commit();

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (NullPointerException e) {
                    Log.e("tag","exc"+e.toString());
                }
            } else {
                try {
                    Uri selectedMediaUri = data.getData();
                    selectedVideoFilePath1 = GetFilePathFromDevice.getPath(QuickCreateEvent.this, selectedMediaUri);
                    Log.e("tag", "qqqqqqqqqqqqqqqqqq------------" + selectedVideoFilePath1);
                    MediaPlayer mp = new MediaPlayer();
                    int duration = 0;
                    try {
                        mp.setDataSource(selectedVideoFilePath1);
                        mp.prepare();
                        duration = mp.getDuration();
                    } catch (IOException e) {
                        Log.e(Utils.class.getName(), e.getMessage());
                    } finally {
                        mp.release();
                    }
                    Log.e("tag", "duration------------->" + duration);
                    Log.e("tag", "duration------------->" + duration / 1000);

                    if ((duration / 1000) > 120) {
                        video0_iv.setImageBitmap(null);
                        video0_iv.setImageDrawable(QuickCreateEvent.this.getDrawable(R.drawable.dotted_square));
                        video1plus.setImageDrawable(QuickCreateEvent.this.getDrawable(R.drawable.plus_icon));
                        SpannableString s = new SpannableString("Upload minimum 2 min Video");
                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        // til_eventdays.setError(s);
                        Toast.makeText(QuickCreateEvent.this, s, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                            video1plus.setImageDrawable(QuickCreateEvent.this.getDrawable(R.drawable.video_icon));
                            edit.putString("video1", selectedVideoFilePath1);
                            edit.commit();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (NullPointerException e) {

                    Log.e("tag","exc"+e.toString());
                }
            }
        } else if (requestCode == INTENT_REQUEST_GET_GALERYVIDEO1) {
            try {
                Uri selectedMediaUri = data.getData();
                selectedVideoFilePath1 = GetFilePathFromDevice.getPath(QuickCreateEvent.this, selectedMediaUri);
                Log.e("tag", "qqqqqqqqqqqqqqqqqq------------" + selectedVideoFilePath1);

                MediaPlayer mp = new MediaPlayer();
                int duration = 0;
                try {
                    mp.setDataSource(selectedVideoFilePath1);
                    mp.prepare();
                    duration = mp.getDuration();
                } catch (IOException e) {
                    Log.e(Utils.class.getName(), e.getMessage());
                } finally {
                    mp.release();
                }
                Log.e("tag", "duration------------->" + duration);
                Log.e("tag", "duration------------->" + duration / 1000);

                if ((duration / 1000) > 120) {
                    video0_iv.setImageBitmap(null);
                    video0_iv.setImageDrawable(QuickCreateEvent.this.getDrawable(R.drawable.dotted_square));
                    video1plus.setImageDrawable(QuickCreateEvent.this.getDrawable(R.drawable.plus_icon));

                    SpannableString s = new SpannableString("Upload minimum 2 min Video");
                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    // til_eventdays.setError(s);
                    Toast.makeText(QuickCreateEvent.this, s, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                        video1plus.setImageDrawable(QuickCreateEvent.this.getDrawable(R.drawable.video_icon));
                        edit.putString("video1", selectedVideoFilePath1);
                        edit.commit();

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            } catch (NullPointerException e) {
                Log.e("tag","exc"+e.toString());
            }
        } else {
            Log.e("tag","exc");
        }

    }

    public Uri getOutputMediaFileUri(int type) {
        Uri selectedMediaUri = Uri.fromFile(getOutputMediaFile(type));
        // Uri selectedMediaUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider",getOutputMediaFile(type));
        Log.e("tag", "r7e7t8e7t---------->>" + selectedMediaUri);

        selectedVideoFilePath1 = GetFilePathFromDevice.getPath(QuickCreateEvent.this, selectedMediaUri);
        Log.e("tag", "11111111111--------->>" + selectedVideoFilePath1);
        video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
        video1plus.setImageDrawable(QuickCreateEvent.this.getDrawable(R.drawable.video_icon));
        edit.putString("video1", selectedVideoFilePath1);
        edit.commit();
        return selectedMediaUri;
    }

    public Uri getOutputMediaFileUri2(int type) {
        File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myvideo.mp4");
        //videoUri = Uri.fromFile(mediaFile);
        Log.e("tag", "gsdhdgshghdsgf--------" + QuickCreateEvent.this.getPackageName());
        String authorities = QuickCreateEvent.this.getPackageName() + ".fileprovider";
        videoUri = FileProvider.getUriForFile(QuickCreateEvent.this, authorities, getOutputMediaFile(type));
        Log.e("tag", "11111111111--------->>" + videoUri);

        return videoUri;
    }


}


