package com.wowhubb.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.view.Window;
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
import com.wowhubb.Utils.Config;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class WowtagFragment extends Fragment {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int INTENT_REQUEST_GET_COVERIMAGES = 11;
    private static final int INTENT_REQUEST_GET_VIDEO1 = 12;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static TextView fromtime_tv, totime_tv;
    public static TextInputLayout til_from, til_to;
    public static String selectedVideoFilePath, selectedCoverFilePath, temp;
    public static EditText eventname_et;
    public static SharedPreferences.Editor edit;
    LinearLayout fromlv, tolv, layout_fromdate, layout_todate;
    Typeface lato;
    ImageView tickiv;
    FrameLayout framevideo1, framecoverpage, framehighlight1, fraehighlight2;
    ImageView video0_iv, cover_iv, highl1_iv, highl2_iv;
    ImageView video1plus, coverplus, h1plus, h2plus, wowtagiv;
    Dialog dialog;
    SharedPreferences sharedPrefces;
    static String selectedVideoFilePath1;
    TextView txt_fromgallery, txt_takevideo;
    ImageView img_take_gallery, img_take_video;
    private Uri fileUri;


    public static WowtagFragment newInstance(int page, boolean isLast) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        if (isLast)
            args.putBoolean("isLast", true);
        final WowtagFragment fragment = new WowtagFragment();
        fragment.setArguments(args);
        return fragment;
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

        /*    FileInputStream fin = null;
            try {
                fin = new FileInputStream(mediaFile);

                selectedVideoFilePath1 = convertStreamToString(fin);
                edit.putString("video1", selectedVideoFilePath1);
                edit.commit();
                dialog.dismiss();
                video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));
                fin.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        } else {
            return null;
        }

        return mediaFile;
    }
    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
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

                dialog = new Dialog(getActivity());
                // Include dialog.xml file
                dialog.setContentView(R.layout.dialog_takevideo);
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) dialog).getWindow();
                        view1.setBackgroundDrawableResource(R.drawable.border_bg);
                    }
                });
                txt_fromgallery = (TextView) dialog.findViewById(R.id.txt_fromgallery);
                img_take_gallery = (ImageView) dialog.findViewById(R.id.img_take_gallery);
                txt_takevideo = (TextView) dialog.findViewById(R.id.txt_takevideo);
                img_take_video = (ImageView) dialog.findViewById(R.id.img_take_video);


                ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
                final LinearLayout lnr_video = (LinearLayout) dialog.findViewById(R.id.lnr_video);
                final LinearLayout lnr_gallery = (LinearLayout) dialog.findViewById(R.id.lnr_gallery);


                txt_fromgallery.setTextColor(Color.parseColor("#3c3c3c"));
                txt_takevideo.setTextColor(Color.parseColor("#3c3c3c"));
                img_take_gallery.setImageResource(R.drawable.gallery_black);
                img_take_video.setImageResource(R.drawable.video_black);

                txt_fromgallery.setText("From Gallery");
                txt_takevideo.setText(" Take Video");

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
                        img_take_gallery.setImageResource(R.drawable.gallery_white);

                        lnr_video.setBackgroundResource(R.drawable.btn_bg_white);
                        txt_takevideo.setTextColor(Color.parseColor("#3c3c3c"));
                        img_take_video.setImageResource(R.drawable.video_black);


                        if (checkPermission()) {
                            Intent intent = new Intent();
                            intent.setType("video/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Video"), INTENT_REQUEST_GET_VIDEO1);
                            Toast.makeText(getActivity(), "Please choose less than 2 mb video", Toast.LENGTH_LONG).show();
                        }


                    }
                });


                lnr_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        lnr_video.setBackgroundResource(R.drawable.btnbg);
                        txt_takevideo.setTextColor(Color.parseColor("#FFFFFF"));
                        img_take_video.setImageResource(R.drawable.video_white);

                        lnr_gallery.setBackgroundResource(R.drawable.btn_bg_white);
                        txt_fromgallery.setTextColor(Color.parseColor("#3c3c3c"));
                        img_take_gallery.setImageResource(R.drawable.gallery_black);


                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

                        // set video quality
                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
                        // name

                        // start the video capture Intent
                        startActivityForResult(intent, INTENT_REQUEST_GET_VIDEO1);
                        dialog.dismiss();

                    }
                });


               dialog.show();


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
                 selectedVideoFilePath1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                Log.e("tag", "qqqqqqqqqqqqqqqqqq------------"+selectedVideoFilePath1);
                video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));
                edit.putString("video1", selectedVideoFilePath1);
                edit.commit();

            } else {
                Uri selectedMediaUri = data.getData();

                if (selectedMediaUri.toString().contains("image")) {
                    try
                    {
                        String selectedVideoFilePath1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                        highl2_iv.setImageBitmap(bitmap);
                        h2plus.setVisibility(View.GONE);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else if (selectedMediaUri.toString().contains("video")) {
                    Log.d("tag", "567231546" + selectedMediaUri);
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

    public Uri getOutputMediaFileUri(int type) {
        Uri selectedMediaUri =Uri.fromFile(getOutputMediaFile(type));
        Log.e("tag","r7e7t8e7t---------->>"+selectedMediaUri);
        selectedVideoFilePath1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
        Log.e("tag","11111111111--------->>"+selectedVideoFilePath1);
        video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
        video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));
        edit.putString("video1", selectedVideoFilePath1);
        edit.commit();
        return selectedMediaUri;
    }

    @SuppressLint("ValidFragment")
    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog da = new DatePickerDialog(getActivity(), this, yy, mm, dd);

            /*Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, 1);
            Date newDate = c.getTime();
            da.getDatePicker().setMinDate(newDate.getTime()-(newDate.getTime()%(24*60*60*1000)));*/

            return da;

        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);

        }

        public void populateSetDate(int year, int month, int day) {
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
            String strDateFormat1 = fromtime_tv.getText().toString();
            SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
            Date newDate = null;

            String strDateFormat = String.format("%02d:%02d", hourOfDay, minute);


            String runtimeFrom = strDateFormat1 + " " + strDateFormat;
            Log.e("tag", "RUNTIME---------->" + runtimeFrom);
            edit.putString("runtimeFrom", runtimeFrom);
            edit.commit();
            strDateFormat = strDateFormat + " a";
            try {
                newDate = spf.parse(strDateFormat1);
                spf = new SimpleDateFormat("MMM/dd/yyyy");
                strDateFormat1 = spf.format(newDate);
                System.out.println(strDateFormat1);
                Log.e("tag", "dvhdsvjvj" + strDateFormat1);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mm a");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");


            try {
                Date dt = parseFormat.parse(strDateFormat);
                System.out.println(outputFormat.format(dt));
                fromtime_tv.setText(strDateFormat1 + " " + outputFormat.format(dt));
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

            String strDateFormatTo = totime_tv.getText().toString();
            SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
            Date newDate = null;
            String strDateFormat = String.format("%02d:%02d", hourOfDay, minute);
            String runtimeTo = strDateFormatTo + " " + strDateFormat;
            edit.putString("runtimeTo", runtimeTo);
            edit.commit();
            Log.e("tag", "RUNTIME TO-------->" + runtimeTo);
            strDateFormat = strDateFormat + " a";
            try {
                newDate = spf.parse(strDateFormatTo);
                spf = new SimpleDateFormat("MMM/dd/yyyy");
                strDateFormatTo = spf.format(newDate);
                System.out.println(strDateFormatTo);
                Log.e("tag", "dvhdsvjvj" + strDateFormatTo);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mm a");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");


            try {
                Date dt = parseFormat.parse(strDateFormat);
                System.out.println(outputFormat.format(dt));
                totime_tv.setText(strDateFormatTo + " " + outputFormat.format(dt));
            } catch (ParseException exc) {
                exc.printStackTrace();
            }


        }

    }
}