package vineture.wowhubb.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import vineture.wowhubb.Activity.CreateEventActivity;
import vineture.wowhubb.Adapter.ExpandableListAdapter;
import vineture.wowhubb.Adapter.ExpandableListDataPump;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.GetFilePathFromDevice;
import vineture.wowhubb.R;
import vineture.wowhubb.Utils.Config;
import vineture.wowhubb.Utils.Utils;
import vineture.wowhubb.data.EventData;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class WowtagFragment extends Fragment {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int INTENT_REQUEST_GET_COVERIMAGES = 11;
    private static final int INTENT_REQUEST_GET_VIDEO1 = 12;
    private static final int INTENT_REQUEST_GET_VIDEO11 = 13;
    public static TextView fromtime_tv, totime_tv, helpfultips_tv;
    public static TextInputLayout til_from, til_to, til_eventtile;
    public static String selectedVideoFilePath, selectedCoverFilePath, temp;
    public static EditText eventname_et;
    public static SharedPreferences.Editor edit;
    public static String selectedVideoFilePath1;
    public EventData eventData;
    LinearLayout fromlv, tolv, layout_fromdate, layout_todate;
    Typeface lato;
    ImageView tickiv;
    FrameLayout framevideo1, framecoverpage, framehighlight1, fraehighlight2;
    ImageView video0_iv, video1plus, highl1_iv, highl2_iv;
    ImageView coverplus, h1plus, h2plus, wowtagiv;
    Dialog dialog, cdialog, ctimedialog;
    SharedPreferences sharedPrefces;
    TextView txt_fromgallery, txt_takevideo;
    String strDateFormat1,runtimefrom,runtimeto;
    File videoMediaFile;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    Calendar wowtagfrom;
    private Uri fileUri, videoUri;

    public static WowtagFragment newInstance(int page, boolean isLast) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        if (isLast)
            args.putBoolean("isLast", true);
        final WowtagFragment fragment = new WowtagFragment();
        fragment.setArguments(args);
        return fragment;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        eventData = ((CreateEventActivity) getActivity()).eventData;
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
        eventname_et.setText(eventData.eventttitle);
        video0_iv = (ImageView) view.findViewById(R.id.video0_iv);
        video1plus = view.findViewById(R.id.video1plus_iv);
        wowtagfrom = Calendar.getInstance();
        Log.e("tag", "11111-------" + eventData.eventwowvideo);
        try {
            if (eventData.eventwowvideo != "") {
                Log.e("tag", "11111-dddd------" + eventData.eventwowvideo);
                video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(eventData.eventwowvideo, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));
            } else if (eventData.eventwowvideo == null) {
                Log.e("tag", "22222222222-dddd------" + eventData.eventwowvideo);
                video0_iv.setImageResource(R.drawable.dotted_square);
                video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));
            }
        } catch (NullPointerException e) {

        }

        fromtime_tv.setText(eventData.eventstarttime);
        totime_tv.setText(eventData.eventendtime);

        til_from = (TextInputLayout) view.findViewById(R.id.til_from);
        til_to = (TextInputLayout) view.findViewById(R.id.til_to);


        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");

        coverplus = view.findViewById(R.id.coverplusiv);
        framevideo1 = view.findViewById(R.id.framevideo1);
        helpfultips_tv = view.findViewById(R.id.helpfultips_tv);

        til_from.setTypeface(lato);
        til_to.setTypeface(lato);

        helpfultips_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getActivity());
                // Include dialog.xml file
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_helpfultips);

                View v1 = dialog.getWindow().getDecorView().getRootView();

                ImageView close = dialog.findViewById(R.id.closeiv);
                FontsOverride.overrideFonts(dialog.getContext(), v1);
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) dialog).getWindow();
                        // view1.setBackgroundDrawableResource(R.drawable.border_graybg);

                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                expandableListView = (ExpandableListView) dialog.findViewById(R.id.expandableListView);
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
                //img_take_gallery = (ImageView) dialog.findViewById(R.id.img_take_gallery);
                txt_takevideo = (TextView) dialog.findViewById(R.id.txt_takevideo);
                //img_take_video = (ImageView) dialog.findViewById(R.id.img_take_video);


                ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
                final LinearLayout lnr_video = (LinearLayout) dialog.findViewById(R.id.lnr_video);
                final LinearLayout lnr_gallery = (LinearLayout) dialog.findViewById(R.id.lnr_gallery);


                txt_fromgallery.setTextColor(Color.parseColor("#3c3c3c"));
                txt_takevideo.setTextColor(Color.parseColor("#3c3c3c"));


                txt_fromgallery.setText("From Gallery");
                txt_takevideo.setText("Take Video ");

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
                            startActivityForResult(Intent.createChooser(intent, "Select Video"), INTENT_REQUEST_GET_VIDEO1);
                          //  Toast.makeText(getActivity(), "Please choose less than 2 mb video", Toast.LENGTH_LONG).show();
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
                                intent.putExtra("android.intent.extra.durationLimit", 120);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                // start the video capture Intent
                                startActivityForResult(intent, INTENT_REQUEST_GET_VIDEO1);
                                dialog.dismiss();
                            }
                        }

                    }
                });


                dialog.show();


            }
        });
        layout_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromtime_tv.setError(null);
                cdialog = new Dialog(getActivity());
                cdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                cdialog.setContentView(R.layout.dialog_customcalendar);
                View v1 = cdialog.getWindow().getDecorView().getRootView();

                final DatePicker dp = cdialog.findViewById(R.id.datePicker);
                TextView ok = cdialog.findViewById(R.id.oktv);
                TextView cancel = cdialog.findViewById(R.id.canceltv);
                //  dp.setMinDate(System.currentTimeMillis() - 1000);
                //   dp.setOnDateChangedListener(dateChangedListener);
                FontsOverride.overrideFonts(cdialog.getContext(), v1);
                Log.e("tag", "111----------" + EventTypeFragment.wowtagto);
                try {
                    long selectedMilli = EventTypeFragment.wowtagfrom.getTimeInMillis();
                    //    dp.setMinDate(selectedMilli - 1000);
                    dp.setMinDate(System.currentTimeMillis() - 1000);
                    long selectedMillisec = EventTypeFragment.wowtagto.getTimeInMillis();
                    dp.setMaxDate(selectedMillisec - 1000);
                } catch (NullPointerException e) {

                }


                cdialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) cdialog).getWindow();

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
                        populateSetDate(year, month + 1, day);
                        cdialog.dismiss();
                    }
                });
                cdialog.show();

            }
        });

        layout_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totime_tv.setError(null);
                cdialog = new Dialog(getActivity());
                cdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                cdialog.setContentView(R.layout.dialog_customcalendar);
                View v1 = cdialog.getWindow().getDecorView().getRootView();

                final DatePicker dp = cdialog.findViewById(R.id.datePicker);
                TextView ok = cdialog.findViewById(R.id.oktv);
                TextView cancel = cdialog.findViewById(R.id.canceltv);
                // dp.setMinDate(System.currentTimeMillis() - 1000);
                //   dp.setOnDateChangedListener(dateChangedListener);

                Log.e("tag", "111----------" + EventTypeFragment.wowtagfrom);

                FontsOverride.overrideFonts(cdialog.getContext(), v1);
                try {
                    long selectedMilli = wowtagfrom.getTimeInMillis();
                    dp.setMinDate(selectedMilli - 1000);
                    // dp.setMinDate(selectedMilli - 1000);
                    //dp.setMinDate(System.currentTimeMillis() - 1000);

                    long selectedMillisec = EventTypeFragment.wowtagto.getTimeInMillis();
                    dp.setMaxDate(selectedMillisec - 1000);

                } catch (NullPointerException e) {

                }
                cdialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) cdialog).getWindow();

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
                        populateSetDateTo(year, month + 1, day);
                        cdialog.dismiss();
                    }
                });
                cdialog.show();

            }
        });


        return view;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED) && (result2 == PackageManager.PERMISSION_GRANTED)) {
            return true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            return false;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Log.e("tag", "code------------->" + resultCode);
            if (requestCode == INTENT_REQUEST_GET_COVERIMAGES) {
                try {
                    Uri selectedMediaUri = data.getData();
                    selectedCoverFilePath = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                        // Log.d(TAG, String.valueOf(bitmap));
                        // cover_iv.setImageBitmap(bitmap);
                        //  coverplus.setVisibility(View.GONE);
                        edit.putString("coverpage", selectedCoverFilePath);
                        edit.commit();
                        // video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (NullPointerException e) {

                }

            } else if (requestCode == INTENT_REQUEST_GET_VIDEO1) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        Uri selectedMediaUri = data.getData();
                        selectedVideoFilePath1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                        Log.e("tag", "qqqqqqqqqqqqqqqqqq------------" + selectedVideoFilePath1);
                       /// video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                       // video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));

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
                        Log.e("tag", "duration------------->" + duration/1000);

                        if((duration/1000) > 30){
                            video0_iv.setImageBitmap(null);
                            video0_iv.setImageDrawable(getActivity().getDrawable(R.drawable.dotted_square));
                            video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.plus_icon));
                            SpannableString s = new SpannableString("Upload minimum 2 min Video");
                            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            // til_eventdays.setError(s);
                            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();


                        }else{
                            try {
                                video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                                video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));
                                edit.putString("video1", selectedVideoFilePath1);
                                edit.commit();


                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }













                    } catch (NullPointerException e) {

                    }
                } else {
                    try {
                        Uri selectedMediaUri = data.getData();
                        selectedVideoFilePath1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
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
                        Log.e("tag", "duration------------->" + duration/1000);

                        if((duration/1000) > 30){
                            video0_iv.setImageBitmap(null);
                            video0_iv.setImageDrawable(getActivity().getDrawable(R.drawable.dotted_square));
                            video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.plus_icon));
                            SpannableString s = new SpannableString("Upload minimum 2 min Video");
                            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            // til_eventdays.setError(s);
                            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();


                        }else{
                            try {
                                video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                                video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));
                                edit.putString("video1", selectedVideoFilePath1);
                                edit.commit();


                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (NullPointerException e) {

                    }
                }

            } else if (requestCode == INTENT_REQUEST_GET_VIDEO11) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        Uri selectedMediaUri = videoUri;
                        selectedVideoFilePath1 = getFilePathFromURI(getActivity(), selectedMediaUri);
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
                        Log.e("tag", "duration------------->" + duration/1000);

                        if((duration/1000) > 30){
                            video0_iv.setImageBitmap(null);
                            video0_iv.setImageDrawable(getActivity().getDrawable(R.drawable.dotted_square));
                            video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.plus_icon));
                            SpannableString s = new SpannableString("Upload minimum 2 min Video");
                            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            // til_eventdays.setError(s);
                            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();


                        }else{
                            try {
                                video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                                video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));
                                edit.putString("video1", selectedVideoFilePath1);
                                edit.apply();


                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (NullPointerException e) {

                    }
                } else {
                    try {
                        Uri selectedMediaUri = data.getData();
                        selectedVideoFilePath1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
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
                        Log.e("tag", "duration------------->" + duration/1000);

                        if((duration/1000) > 30){
                            video0_iv.setImageBitmap(null);
                            video0_iv.setImageDrawable(getActivity().getDrawable(R.drawable.dotted_square));
                            video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.plus_icon));
                            SpannableString s = new SpannableString("Upload minimum 2 min Video");
                            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            // til_eventdays.setError(s);
                            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();


                        }else{
                            try {
                                video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                                video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));
                                edit.putString("video1", selectedVideoFilePath1);
                                edit.commit();


                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (NullPointerException e) {

                    }
                }
            } else {

                try {
                    Uri selectedMediaUri = data.getData();
                    if (selectedMediaUri.toString().contains("image")) {
                        try {
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
                } catch (NullPointerException e) {

                }

            }

        }

    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        // get the file url
//        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    public Uri getOutputMediaFileUri(int type) {
        Uri selectedMediaUri = Uri.fromFile(getOutputMediaFile(type));
        // Uri selectedMediaUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider",getOutputMediaFile(type));

        Log.e("tag", "r7e7t8e7t---------->>" + selectedMediaUri);
        selectedVideoFilePath1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
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
        Log.e("tag", "duration------------->" + duration/1000);

        if((duration/1000) > 120){
            video0_iv.setImageBitmap(null);
            video0_iv.setImageDrawable(getActivity().getDrawable(R.drawable.dotted_square));
            video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.plus_icon));

            SpannableString s = new SpannableString("Upload minimum 2 min Video");
            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // til_eventdays.setError(s);
            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
        }else{
            try {
                video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));
                edit.putString("video1", selectedVideoFilePath1);
                edit.commit();

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return selectedMediaUri;
    }

    public Uri getOutputMediaFileUri2(int type) {
        File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myvideo.mp4");
        //videoUri = Uri.fromFile(mediaFile);
        Log.e("tag", "gsdhdgshghdsgf--------" + getActivity().getPackageName());
        String authorities = getActivity().getPackageName() + ".fileprovider";
        videoUri = FileProvider.getUriForFile(getActivity(), authorities, getOutputMediaFile(type));
        Log.e("tag", "11111111111--------->>" + videoUri);

        return videoUri;
    }

    private void takePicture() {
        if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myvideo.mp4");
            //videoUri = Uri.fromFile(mediaFile);
            String authorities = getActivity().getPackageName() + ".fileprovider";
            videoUri = FileProvider.getUriForFile(getActivity(), authorities, mediaFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivityForResult(intent, INTENT_REQUEST_GET_VIDEO11);

        } else {
            Toast.makeText(getActivity(), "No camera on device", Toast.LENGTH_LONG).show();
        }

    }

    private File createImageFileWith() throws IOException {
        // Create an image file name
        File Video_folder = new File(Environment.getExternalStorageDirectory(),
                "Sample/Videos");
        videoMediaFile = null;

        try {
            videoMediaFile = File.createTempFile(
                    "VID_" + System.currentTimeMillis(),
                    ".mp4",
                    Video_folder
            );
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoMediaFile));
            startActivityForResult(takeVideoIntent, INTENT_REQUEST_GET_VIDEO11);
        } catch (Exception e) {
            Log.e("Image Capturing", e.toString());
        }
        return videoMediaFile;

    }

    @Override
    public void onPause() {
        super.onPause();
        setData();
    }

    private void setData() {
        eventData.eventttitle = eventname_et.getText().toString();
        eventData.eventwowvideo = selectedVideoFilePath1;
        eventData.eventstarttime = fromtime_tv.getText().toString().trim();
        eventData.eventendtime = totime_tv.getText().toString().trim();


    }

    private void populateSetDate(int year, int month, int day) {
        String strDateFormatTo = month + "/" + day + "/" + year;
        SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
        Date newDate = null;

        try {
            newDate = spf.parse(strDateFormatTo);
            spf = new SimpleDateFormat("MMM/dd/yyyy");
            strDateFormatTo = spf.format(newDate);
            totime_tv.setText("");
            fromtime_tv.setText(strDateFormatTo);
            Log.e("tag", "dvhdsvjvj" + strDateFormatTo);
            SimpleDateFormat spf1 = new SimpleDateFormat("yyyy/MM/dd");
            newDate = spf.parse(strDateFormatTo);
            runtimefrom = spf1.format(newDate);
            Log.e("tag", "new run frommmm------>" + runtimefrom);
            edit.putString("runtimefrom", runtimefrom);
            edit.commit();
           /* ctimedialog = new Dialog(getActivity());
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
                    Window view1 = ((Dialog) ctimedialog).getWindow();

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

                    int hour = 0;
                    int min = 0;

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
            ctimedialog.show();*/
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void populateSetTime(int hourOfDay, int minute) {
        strDateFormat1 = fromtime_tv.getText().toString();
        SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
        Date newDate = null;

        String strDateFormat = String.format("%02d:%02d", hourOfDay, minute);


        String runtimeFrom = strDateFormat1 + " " + strDateFormat;
        Log.e("tag", "RUNTIME---------->" + runtimeFrom);
        //  edit.putString("runtimeFrom", runtimeFrom);
        // edit.commit();
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


            int strdayscounrt = sharedPrefces.getInt("str_eventday", 0);
            if (strdayscounrt == 1) {


                totime_tv.setText("");
                totime_tv.setText(strDateFormat1 + " " + outputFormat.format(dt));


            }


        } catch (ParseException exc) {
            exc.printStackTrace();
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
            totime_tv.setText(strDateFormatTo);
            Log.e("tag", "dvhdsvjvj" + strDateFormatTo);
            SimpleDateFormat spf1 = new SimpleDateFormat("yyyy/MM/dd");
            newDate = spf.parse(strDateFormatTo);
            runtimeto = spf1.format(newDate);
            Log.e("tag", "new runnn to------>" + runtimeto);
            edit.putString("runtimeto", runtimeto);
            edit.commit();
          /*  ctimedialog = new Dialog(getActivity());
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
                    Window view1 = ((Dialog) ctimedialog).getWindow();

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


                    int hour = 0;
                    int min = 0;

                    int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                    if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                        hour = dp.getHour();
                        min = dp.getMinute();
                    } else {
                        hour = dp.getCurrentHour();
                        min = dp.getCurrentMinute();
                    }

                    ctimedialog.dismiss();
                    populateSetTimeTo(hour, min);
                }
            });
            ctimedialog.show();*/

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void populateSetTimeTo(int hourOfDay, int minute) {
        strDateFormat1 = totime_tv.getText().toString();
        SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
        Date newDate = null;

        String strDateFormat = String.format("%02d:%02d", hourOfDay, minute);


        String runtimeFrom = strDateFormat1 + " " + strDateFormat;
        Log.e("tag", "RUNTIME---------->" + runtimeFrom);
        //  edit.putString("runtimeFrom", runtimeFrom);
        // edit.commit();
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
            totime_tv.setText(strDateFormat1 + " " + outputFormat.format(dt));
        } catch (ParseException exc) {
            exc.printStackTrace();
        }


    }

    @SuppressLint("ValidFragment")
    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog da = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
            //Set a title for DatePickerDialog
            da.setTitle("Set a Date");
            //  da.getDatePicker().setDrawingCacheBackgroundColor(getContext().getColor(R.color.textcolr));

            //DatePickerDialog da = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            //Calendar cal = new GregorianCalendar(year, month, day);
            DatePicker dp = da.getDatePicker();
            //Set the DatePicker minimum date selection to current date
            dp.setMinDate(c.getTimeInMillis());

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
            return new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT
                    , this, hour, minute, false);
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
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog da = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
            //Set a title for DatePickerDialog
            da.setTitle("Set a Date");
            //  da.getDatePicker().setDrawingCacheBackgroundColor(getContext().getColor(R.color.textcolr));

            //DatePickerDialog da = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            //Calendar cal = new GregorianCalendar(year, month, day);
            DatePicker dp = da.getDatePicker();
            //Set the DatePicker minimum date selection to current date
            dp.setMinDate(c.getTimeInMillis());

            return da;
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
            return new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT
                    , this, hour, minute, false);
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