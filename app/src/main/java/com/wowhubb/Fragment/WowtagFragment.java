package com.wowhubb.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.wowhubb.Activity.CreateEventActivity;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.GetFilePathFromDevice;
import com.wowhubb.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class WowtagFragment extends Fragment {

    private static final int INTENT_REQUEST_GET_COVERIMAGES = 11;
    private static final int INTENT_REQUEST_GET_VIDEO1 = 12;
    public static TextView fromtime_tv, totime_tv;
    public static TextInputLayout til_from, til_to;
    public static String selectedVideoFilePath, selectedCoverFilePath, temp;
    public static EditText eventname_et;
    LinearLayout fromlv, tolv, layout_fromdate, layout_todate;
    Typeface lato;
    ImageView tickiv;
    FrameLayout framevideo1, framecoverpage, framehighlight1, fraehighlight2;
    ImageView video0_iv, cover_iv, highl1_iv, highl2_iv;
    ImageView video1plus, coverplus, h1plus, h2plus, wowtagiv;
    Dialog dialog;
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;
    String calString;

    public static WowtagFragment newInstance(int page, boolean isLast) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        if (isLast)
            args.putBoolean("isLast", true);
        final WowtagFragment fragment = new WowtagFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_wowtag, container, false);
        FontsOverride.overrideFonts(getActivity(), view);
        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getActivity());
        edit = sharedPrefces.edit();
        CreateEventActivity.skiptv.setVisibility(View.INVISIBLE);
        fromtime_tv = (TextView) view.findViewById(R.id.fromtv);
        totime_tv = (TextView) view.findViewById(R.id.totv);
        fromlv = (LinearLayout) view.findViewById(R.id.fromlv);
        tolv = (LinearLayout) view.findViewById(R.id.tolv);
        layout_fromdate = view.findViewById(R.id.layout_fromdate);
        layout_todate = view.findViewById(R.id.layout_todate);
        tickiv = view.findViewById(R.id.tickiv);
        eventname_et = view.findViewById(R.id.eventtitle_et);

        til_from = (TextInputLayout) view.findViewById(R.id.til_from);
        til_to = (TextInputLayout) view.findViewById(R.id.til_to);
        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");
        video1plus = view.findViewById(R.id.video1plus_iv);
        coverplus = view.findViewById(R.id.coverplusiv);
        framevideo1 = view.findViewById(R.id.framevideo1);
        framecoverpage = view.findViewById(R.id.frame_cover);

        video0_iv = (ImageView) view.findViewById(R.id.video0_iv);
        cover_iv = (ImageView) view.findViewById(R.id.coverpage_iv);


        til_from.setTypeface(lato);
        til_to.setTypeface(lato);

        framecoverpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");
                    startActivityForResult(pickIntent, INTENT_REQUEST_GET_COVERIMAGES);
                }
            }
        });


        framevideo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    Intent intent = new Intent();
                    intent.setType("video/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Video"), INTENT_REQUEST_GET_VIDEO1);
                    Toast.makeText(getActivity(), "Please choose less than 2 mb video", Toast.LENGTH_LONG).show();
                }
            }
        });
        layout_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromtime_tv.setError(null);
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        layout_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totime_tv.setError(null);
                DialogFragment newFragment = new SelectDateFragmentTo();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });


        return view;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED)) {
            Log.e("tag", "Permission is granted");
            return true;
        } else {
            Log.e("tag", "Permission is revoked");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Log.e("tag", "code------------->" + resultCode);
            if (requestCode == INTENT_REQUEST_GET_COVERIMAGES) {
                Uri selectedMediaUri = data.getData();
                selectedCoverFilePath = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);


                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    // Log.d(TAG, String.valueOf(bitmap));
                    cover_iv.setImageBitmap(bitmap);
                    coverplus.setVisibility(View.GONE);
                    edit.putString("coverpage", selectedCoverFilePath);
                    edit.commit();
                    // video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (requestCode == INTENT_REQUEST_GET_VIDEO1) {

                Uri selectedMediaUri = data.getData();

                String selectedVideoFilePath1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);

                video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));
                edit.putString("video1", selectedVideoFilePath1);
                edit.commit();

            } else {
                Uri selectedMediaUri = data.getData();

                if (selectedMediaUri.toString().contains("image")) {
                    try {
                        String selectedVideoFilePath1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                       /* Bitmap bitmapImage = BitmapFactory.decodeFile(selectedVideoFilePath1);
                        int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
                        Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
                        highl2_iv.setImageBitmap(scaled);*/

                        // BitMapToString(scaled);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                        highl2_iv.setImageBitmap(bitmap);
                        h2plus.setVisibility(View.GONE);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else if (selectedMediaUri.toString().contains("video")) {
                    Log.d("tag", "567231546" + selectedMediaUri);
                    //  Uri selectedVideo = data.getData();
                    String selectedVideoFilePath = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);

                    highl2_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                    h2plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));

                }

            }

        }

    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    @SuppressLint("ValidFragment")
    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);

        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);

        }

        public void populateSetDate(int year, int month, int day) {
            //edittext_offdate.setText(month+"/"+day+"/"+year);
            fromtime_tv.setText(month + "/" + day + "/" + year);
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getFragmentManager(), "TimePicker");

        }
    }

    @SuppressLint("ValidFragment")
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //Use the current time as the default values for the time picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            //Create and return a new instance of TimePickerDialog
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        //onTimeSet() callback method
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            String strDateFormat1=fromtime_tv.getText().toString();
            //String date="Mar 10, 2016 6:30:00 PM";
            SimpleDateFormat spf=new SimpleDateFormat("MM/dd/yyyy");
            Date newDate= null;
            try {
                newDate = spf.parse(strDateFormat1);
                spf= new SimpleDateFormat("MMM/dd/yyyy");
                strDateFormat1 = spf.format(newDate);
                System.out.println(strDateFormat1);
                Log.e("tag","dvhdsvjvj"+strDateFormat1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String strDateFormat = String.format("%02d:%02d", hourOfDay, minute);
            strDateFormat = strDateFormat + " a";


            SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mm a");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");


            try {
                Date dt = parseFormat.parse(strDateFormat);
                System.out.println(outputFormat.format(dt));
                fromtime_tv.setText(strDateFormat1+" " + outputFormat.format(dt));
            } catch (ParseException exc) {
                exc.printStackTrace();
            }


        }

    }

    @SuppressLint("ValidFragment")
    public static class SelectDateFragmentTo extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();

            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }

        public void populateSetDate(int year, int month, int day) {
           totime_tv.setText(month + "/" + day + "/" + year);
            DialogFragment newFragment = new TimePickerFragmentTo();
            newFragment.show(getFragmentManager(), "TimePicker");
        }
    }

    @SuppressLint("ValidFragment")
    public static class TimePickerFragmentTo extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //Use the current time as the default values for the time picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            //Create and return a new instance of TimePickerDialog
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        //onTimeSet() callback method
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String strDateFormatTo=totime_tv.getText().toString();
            SimpleDateFormat spf=new SimpleDateFormat("MM/dd/yyyy");
            Date newDate= null;
            try {
                newDate = spf.parse(strDateFormatTo);
                spf= new SimpleDateFormat("MMM/dd/yyyy");
                strDateFormatTo = spf.format(newDate);
                System.out.println(strDateFormatTo);
                Log.e("tag","dvhdsvjvj"+strDateFormatTo);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String strDateFormat = String.format("%02d:%02d", hourOfDay, minute);
            strDateFormat = strDateFormat + " a";

            SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mm a");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");


            try {
                Date dt = parseFormat.parse(strDateFormat);
                System.out.println(outputFormat.format(dt));
                totime_tv.setText(strDateFormatTo+" " + outputFormat.format(dt));
            } catch (ParseException exc) {
                exc.printStackTrace();
            }


        }

    }


}