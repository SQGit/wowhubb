package com.wowhubb.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
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

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.GetFilePathFromDevice;
import com.wowhubb.R;

import java.io.IOException;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class WowtagFragment extends Fragment {

    static TextView fromtime_tv, totime_tv;
    LinearLayout fromlv, tolv, layout_fromdate, layout_todate;
    TextInputLayout til_eventname, til_from, til_to;
    Typeface lato;
    FrameLayout framevideo1, framevideo2, framecoverpage, framehighlight1, fraehighlight2;
    ImageView video0_iv, video1_iv, cover_iv, highl1_iv, highl2_iv;
    ImageView video1plus, video2plus, coverplus, h1plus, h2plus, tickiv;
    EditText eventvalue_et;

    private static final int INTENT_REQUEST_GET_COVERIMAGES = 11;
    private static final int INTENT_REQUEST_GET_VIDEO1 = 12;
    private static final int INTENT_REQUEST_GET_VIDEO2 = 13;
    private static final int INTENT_REQUEST_GET_HIGHLIGHT1 = 14;
    private static final int INTENT_REQUEST_GET_HIGHLIGHT2 = 15;

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
        fromtime_tv = (TextView) view.findViewById(R.id.fromtv);
        totime_tv = (TextView) view.findViewById(R.id.totv);
        fromlv = (LinearLayout) view.findViewById(R.id.fromlv);
        tolv = (LinearLayout) view.findViewById(R.id.tolv);
        layout_fromdate = view.findViewById(R.id.layout_fromdate);
        layout_todate = view.findViewById(R.id.layout_todate);

        video1plus = view.findViewById(R.id.video1plus_iv);
        video2plus = view.findViewById(R.id.video2plus_iv);
        coverplus = view.findViewById(R.id.coverplusiv);
        h1plus = view.findViewById(R.id.highlight1plus_iv);
        h2plus = view.findViewById(R.id.highlight2plus_iv);
        eventvalue_et = view.findViewById(R.id.eventtitle_et);

        framevideo1 = view.findViewById(R.id.framevideo1);
        framevideo2 = view.findViewById(R.id.framevideo2);
        framecoverpage = view.findViewById(R.id.frame_cover);
        framehighlight1 = view.findViewById(R.id.frame_highlight1);
        fraehighlight2 = view.findViewById(R.id.frame_highlight2);


        video0_iv = (ImageView) view.findViewById(R.id.video0_iv);
        video1_iv = (ImageView) view.findViewById(R.id.video1_iv);
        cover_iv = (ImageView) view.findViewById(R.id.coverpage_iv);
        highl1_iv = (ImageView) view.findViewById(R.id.highlight1_iv);
        highl2_iv = (ImageView) view.findViewById(R.id.highlight2_iv);
        tickiv = view.findViewById(R.id.tickiv);


        til_from = (TextInputLayout) view.findViewById(R.id.til_from);
        til_to = (TextInputLayout) view.findViewById(R.id.til_to);
        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");

        til_from.setTypeface(lato);
        til_to.setTypeface(lato);

        eventvalue_et.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                tickiv.setVisibility(View.VISIBLE);

                return false;
            }


        });
        layout_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        layout_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new SelectDateFragmentTo();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        framecoverpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                startActivityForResult(pickIntent, INTENT_REQUEST_GET_COVERIMAGES);

            }
        });


        framevideo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), INTENT_REQUEST_GET_VIDEO1);

            }
        });


        framevideo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), INTENT_REQUEST_GET_VIDEO2);
            }
        });

        framehighlight1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // pickIntent.setType("image/*","video/*");
                pickIntent.setType("*/*");
                pickIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
                startActivityForResult(pickIntent, INTENT_REQUEST_GET_HIGHLIGHT1);

            }
        });
        fraehighlight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // pickIntent.setType("image/*","video/*");
                pickIntent.setType("*/*");
                pickIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
                startActivityForResult(pickIntent, INTENT_REQUEST_GET_HIGHLIGHT2);

            }
        });

        return view;
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

            //Disable the previous date in calend

            // DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 100000);
            //return datePickerDialog;

        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);

        }

        public void populateSetDate(int year, int month, int day) {
            //edittext_offdate.setText(month+"/"+day+"/"+year);
            fromtime_tv.setText(year + "/" + month + "/" + day);
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
            //Do something with the user chosen time
            //Get reference of host activity (XML Layout File) TextView widget
            // TextView tv = (TextView) getActivity().findViewById(R.id.tv);
            //Set a message for user
            //tv.setText("Your chosen time is...\n\n");
            //Display the user changed time on TextView
            fromtime_tv.setText(fromtime_tv.getText() + " " + String.valueOf(hourOfDay)
                    + ": " + String.valueOf(minute));
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
            //edittext_offdate.setText(month+"/"+day+"/"+year);
            totime_tv.setText(year + "/" + month + "/" + day);
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
            //Do something with the user chosen time
            //Get reference of host activity (XML Layout File) TextView widget
            // TextView tv = (TextView) getActivity().findViewById(R.id.tv);
            //Set a message for user
            //tv.setText("Your chosen time is...\n\n");
            //Display the user changed time on TextView
            totime_tv.setText(totime_tv.getText() + " " + String.valueOf(hourOfDay)
                    + ": " + String.valueOf(minute));
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            Log.e("tag", "code------------->" + resultCode);
            if (requestCode == INTENT_REQUEST_GET_COVERIMAGES) {
                Uri selectedMediaUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    // Log.d(TAG, String.valueOf(bitmap));
                    cover_iv.setImageBitmap(bitmap);
                    coverplus.setVisibility(View.GONE);
                    // video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (requestCode == INTENT_REQUEST_GET_VIDEO1) {
                Uri selectedMediaUri = data.getData();
                Log.d("tag", "567231546" + selectedMediaUri);
                //  Uri selectedVideo = data.getData();
                String selectedVideoFilePath = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);

                video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));
            } else if (requestCode == INTENT_REQUEST_GET_VIDEO2) {
                Uri selectedMediaUri = data.getData();
                Log.d("tag", "567231546" + selectedMediaUri);
                //  Uri selectedVideo = data.getData();
                String selectedVideoFilePath = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);

                video1_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                video2plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));
            } else if (requestCode == INTENT_REQUEST_GET_HIGHLIGHT1) {
                Uri selectedMediaUri = data.getData();

                if (selectedMediaUri.toString().contains("image")) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                        // Log.d(TAG, String.valueOf(bitmap));
                        highl1_iv.setImageBitmap(bitmap);
                        h1plus.setVisibility(View.GONE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else if (selectedMediaUri.toString().contains("video")) {
                    Log.d("tag", "567231546" + selectedMediaUri);
                    //  Uri selectedVideo = data.getData();
                    String selectedVideoFilePath = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);

                    highl1_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                    h1plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));

                } else {

                }

            } else {
                Uri selectedMediaUri = data.getData();

                if (selectedMediaUri.toString().contains("image")) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                        highl2_iv.setImageBitmap(bitmap);
                        h2plus.setVisibility(View.GONE);

                    } catch (IOException e) {
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


}