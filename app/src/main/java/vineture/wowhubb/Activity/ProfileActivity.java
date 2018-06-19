package vineture.wowhubb.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gun0912.tedpicker.ImagePickerActivity;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.GetFilePathFromDevice;
import vineture.wowhubb.R;
import vineture.wowhubb.Utils.Config;
import vineture.wowhubb.Utils.HttpUtils;

/**
 * Created by Salman on 05-10-2017.
 */

public class ProfileActivity extends Activity {
    public final static int REQUEST_PROFILE = 1;
    public final static int REQUEST_COVERPAGE = 2;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int INTENT_REQUEST_GET_VIDEO1 = 12;
    private static final int INTENT_REQUEST_GET_VIDEO11 = 13;
    static Bitmap bitmap;
    public com.gun0912.tedpicker.Config img_config;
    TextView personal_tv, skiptv, wowtag2, save_tv, editiv;
    ImageView backiv, coverpageiv, introeditiv, video0_iv, video1plus;
    ArrayList<Uri> image_uris;
    String navdashboard, str_profile_img, str_profile_cover, token, name, lname, str_wowtag, profilepath, personalimage, personalcover, selectedVideoFilePath1, personalself;
    ImageView profile_iv;
    FrameLayout framevideo1;
    TextView username, lastname, wowtag, professionaltv;

    LinearLayout interest_lvl;
    SharedPreferences.Editor editor;
    Dialog dialog, loader_dialog;
    SharedPreferences sharedPrefces;
    TextView txt_fromgallery, txt_takevideo;
    ImageView img_take_gallery, img_take_video;
    File videoMediaFile;
    Typeface lato;
    Bundle extras;
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

    //--------------------------------------checkPermission---------------------------------------//

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

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();

        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    //--------------------------------ASYN TASK FOR PROFILE----------------------------------------//

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        extras = getIntent().getExtras();

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");
        name = sharedPreferences.getString("str_name", "");
        lname = sharedPreferences.getString("str_lname", "");
        str_wowtag = sharedPreferences.getString("wowtagid", "");
        profilepath = sharedPreferences.getString("profilepath", "");
        personalself = sharedPreferences.getString("personalself", "");
        img_config = new com.gun0912.tedpicker.Config();
        img_config.setCameraHeight(R.dimen.app_camera_height);
        img_config.setSelectedBottomHeight(R.dimen.bottom_height);
        img_config.setCameraBtnBackground(R.drawable.round_rd);

        loader_dialog = new Dialog(ProfileActivity.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(vineture.wowhubb.R.layout.test_loader);

        new getpersonalprofile().execute();

        personal_tv = findViewById(R.id.personal_tv);
        professionaltv = findViewById(R.id.professionaltv);
        editiv = (TextView) findViewById(R.id.coveredittv);
        backiv = findViewById(R.id.backiv);
        skiptv = findViewById(R.id.skiptv);
        introeditiv = findViewById(R.id.editiv);
        // save_tv = findViewById(R.id.save_tv);

        username = findViewById(R.id.nametv);
        lastname = findViewById(R.id.subname);
        wowtag = findViewById(R.id.wowtagid);

        profile_iv = findViewById(R.id.imageview_profile);
        coverpageiv = findViewById(R.id.coverpage_iv);
        //  coverpageeditiv = findViewById(R.id.editiv);
        framevideo1 = findViewById(R.id.framespeaker);
        video0_iv = (ImageView) findViewById(R.id.speaker_iv);
        video1plus = findViewById(R.id.speakerplus);
        interest_lvl = findViewById(R.id.interest_lv);


        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ProfileActivity.this, v1);
        lato = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");

        if (name != null) {
            username.setText(name);
        }

        if (lname != null) {
            lastname.setText(lname);
        }

        if (str_wowtag != null) {

            if (str_wowtag.length() > 0) {
                String sa = str_wowtag.substring(0, 1);
                if (sa.equals("!")) {
                    wowtag.setText(str_wowtag);
                    //wowtag2.setText(str_wowtag);
                } else {
                    wowtag.setText("!" + str_wowtag);
                    //  wowtag2.setText("!" + str_wowtag);
                }
            }
        }


        personal_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag", "11111111111----------" + profilepath);
                editor.putString("profilepath", profilepath);
                editor.commit();
                startActivity(new Intent(ProfileActivity.this, ProfilePersonalActivity.class));
            }
        });

        professionaltv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, ProfileProfessionalActivity.class));

            }
        });

        backiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        skiptv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, LandingPageActivity.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }
        });

        interest_lvl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, InterestActivity.class);
                intent.putExtra("navdashboard", "true");
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                startActivity(intent);
                finish();
            }
        });

      /*  edittv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    img_config.setSelectionMin(1);
                    img_config.setSelectionLimit(1);
                    img_config.setToolbarTitleRes(R.string.img_profile);
                    ImagePickerActivity.setConfig(img_config);
                    Intent intent = new Intent(ProfileActivity.this, ImagePickerActivity.class);
                    startActivityForResult(intent, REQUEST_PROFILE);
                }
            }
        });*/

        editiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    img_config.setSelectionMin(1);
                    img_config.setSelectionLimit(1);
                    img_config.setToolbarTitleRes(R.string.img_coverprofile);
                    ImagePickerActivity.setConfig(img_config);
                    Intent intent = new Intent(ProfileActivity.this, ImagePickerActivity.class);
                    startActivityForResult(intent, REQUEST_COVERPAGE);
                }
            }
        });


        introeditiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new Dialog(ProfileActivity.this);
                dialog.setContentView(R.layout.dialog_takevideo);
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) dialog).getWindow();
                        //view1.setBackgroundDrawableResource(R.drawable.border_bg);
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
                //img_take_gallery.setImageResource(R.drawable.gallery_black);
                //img_take_video.setImageResource(R.drawable.video_black);

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
                        //img_take_gallery.setImageResource(R.drawable.gallery_white);

                        lnr_video.setBackgroundResource(R.drawable.btn_bg_white);
                        txt_takevideo.setTextColor(Color.parseColor("#3c3c3c"));
                        //img_take_video.setImageResource(R.drawable.video_black);


                        if (checkPermission()) {
                            Intent intent = new Intent();
                            intent.setType("video/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Video"), INTENT_REQUEST_GET_VIDEO1);
                            Toast.makeText(ProfileActivity.this, "Please choose less than 2 mb video", Toast.LENGTH_LONG).show();
                        }


                    }
                });


                lnr_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            // Do something for lollipop and above versions
                            Log.e("tag", "111111------->>>>>>>>>NOUGAT");
                            // takePicture();
                            if (checkPermission()) {
                                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                videoUri = getOutputMediaFileUri2(MEDIA_TYPE_VIDEO);
                                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri); // set the image file
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                // start the video capture Intent
                                startActivityForResult(intent, INTENT_REQUEST_GET_VIDEO11);
                                dialog.dismiss();
                            }
                        } else {
                            // Do something for lollipop and above versions
                            Log.e("tag", "111111------->>>>>>>>>BELOWWWW");


                            lnr_video.setBackgroundResource(R.drawable.btnbg);
                            txt_takevideo.setTextColor(Color.parseColor("#FFFFFF"));
                            //img_take_video.setImageResource(R.drawable.video_white);

                            lnr_gallery.setBackgroundResource(R.drawable.btn_bg_white);
                            txt_fromgallery.setTextColor(Color.parseColor("#3c3c3c"));
                            //img_take_gallery.setImageResource(R.drawable.gallery_black);
                            if (checkPermission()) {
                                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
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


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<String> photos = null;
        if (requestCode == REQUEST_PROFILE && resultCode == Activity.RESULT_OK) {
            try {
                image_uris = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
                Log.e("tag", "12345----------->>>>>>" + image_uris.get(0).toString());
            } catch (IndexOutOfBoundsException e) {
            }
            if (image_uris != null) {
                str_profile_img = image_uris.get(0).toString();
                Glide.with(ProfileActivity.this).load(new File(str_profile_img)).into(profile_iv);
                new profileupload_task().execute();
            }
        } else if (requestCode == REQUEST_COVERPAGE && resultCode == Activity.RESULT_OK) {
            try {
                image_uris = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
                Log.e("tag", "12345" + image_uris.get(0).toString());
            } catch (IndexOutOfBoundsException e) {
            }

            if (image_uris != null) {
                str_profile_cover = image_uris.get(0).toString();
                Glide.with(ProfileActivity.this).load(new File(str_profile_cover)).into(coverpageiv);
                new profilecoverupload_task().execute();
            }
        } else if (requestCode == INTENT_REQUEST_GET_VIDEO1) {

            try {
                Uri selectedMediaUri = data.getData();
                Log.d("tag", "567231546" + selectedMediaUri);
                String selectedVideoFilePath1 = GetFilePathFromDevice.getPath(ProfileActivity.this, selectedMediaUri);
                new profileselfintro_task(selectedVideoFilePath1).execute();

                video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                video1plus.setImageDrawable(ProfileActivity.this.getDrawable(R.drawable.video_icon));

            } catch (NullPointerException e) {

            }


        } else if (requestCode == INTENT_REQUEST_GET_VIDEO1) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Uri selectedMediaUri = data.getData();
                selectedVideoFilePath1 = GetFilePathFromDevice.getPath(this, selectedMediaUri);
                Log.e("tag", "qqqqqqqqqqqqqqqqqq------------" + selectedVideoFilePath1);
                new profileselfintro_task(selectedVideoFilePath1).execute();
                video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                video1plus.setImageDrawable(this.getDrawable(R.drawable.video_icon));

            } else {
                Uri selectedMediaUri = data.getData();
                selectedVideoFilePath1 = GetFilePathFromDevice.getPath(this, selectedMediaUri);
                Log.e("tag", "qqqqqqqqqqqqqqqqqq------------" + selectedVideoFilePath1);
                new profileselfintro_task(selectedVideoFilePath1).execute();
                video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                video1plus.setImageDrawable(this.getDrawable(R.drawable.video_icon));

            }
        } else if (requestCode == INTENT_REQUEST_GET_VIDEO11) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //  final Uri dataa = data.getData();
                //  final File file = new File(dataa.getPath());
                // now you can upload your image file

                Uri selectedMediaUri = videoUri;
                selectedVideoFilePath1 = getFilePathFromURI(this, selectedMediaUri);
                Log.e("tag", "qqqqqqqqqqqqqqqqqq------------" + selectedVideoFilePath1);
                new profileselfintro_task(selectedVideoFilePath1).execute();
                video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                video1plus.setImageDrawable(this.getDrawable(R.drawable.video_icon));

            } else {
                Uri selectedMediaUri = data.getData();
                selectedVideoFilePath1 = GetFilePathFromDevice.getPath(this, selectedMediaUri);
                Log.e("tag", "qqqqqqqqqqqqqqqqqq------------" + selectedVideoFilePath1);
                new profileselfintro_task(selectedVideoFilePath1).execute();
                video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                video1plus.setImageDrawable(this.getDrawable(R.drawable.video_icon));

            }
        }


    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA);
        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED) && (result2 == PackageManager.PERMISSION_GRANTED)) {
            return true;
        } else {
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            return false;

        }
    }

    //-------------------------------ASYN TASK FOR PROFILE COVER PIC-------------------------------//

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    //------------------------------ASYN TASK FOR PROFILE GET PROFILE------------------------------//

    public Uri getOutputMediaFileUri(int type) {
        Uri selectedMediaUri = Uri.fromFile(getOutputMediaFile(type));
        // Uri selectedMediaUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider",getOutputMediaFile(type));

        Log.e("tag", "r7e7t8e7t---------->>" + selectedMediaUri);
        selectedVideoFilePath1 = GetFilePathFromDevice.getPath(this, selectedMediaUri);
        Log.e("tag", "11111111111--------->>" + selectedVideoFilePath1);

        return selectedMediaUri;
    }

    public Uri getOutputMediaFileUri2(int type) {
        File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myvideo.mp4");
        //videoUri = Uri.fromFile(mediaFile);
        String authorities = this.getPackageName() + ".fileprovider";
        videoUri = FileProvider.getUriForFile(this, authorities, getOutputMediaFile(type));
        Log.e("tag", "11111111111--------->>" + videoUri);

        return videoUri;
    }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class profileupload_task extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                String responseString = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.WEB_URL_PROFILE_IMG);
                httppost.setHeader("token", token);
                HttpResponse response = null;
                HttpEntity r_entity = null;

                if (str_profile_img != null) {
                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    entity.addPart("personalimage", new FileBody(new File(str_profile_img), "image/jpeg"));
                    httppost.setEntity(entity);
                }

                try {
                    try {
                        response = httpclient.execute(httppost);
                    } catch (Exception e) {
                        Log.e("tag", "ds:" + e.toString());
                    }

                    try {
                        r_entity = response.getEntity();
                    } catch (Exception e) {
                        Log.e("tag", "dsa:" + e.toString());
                    }

                    int statusCode = response.getStatusLine().getStatusCode();
                    Log.e("tag", response.getStatusLine().toString());
                    if (statusCode == 200) {
                        responseString = EntityUtils.toString(r_entity);
                        JSONObject jo = new JSONObject(responseString);
                        String success = jo.getString("success");

                        return success;


                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                        Log.e("tag3", responseString);
                    }
                } catch (ClientProtocolException e) {
                    responseString = e.toString();
                    Log.e("tag44", responseString);
                } catch (IOException e) {
                    responseString = e.toString();
                    Log.e("tag45", responseString);
                }
                return responseString;
            } catch (Exception e) {
                Log.e("tag_InputStream0", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loader_dialog.dismiss();

        }

    }

    public class profilecoverupload_task extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                String responseString = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.WEB_URL_COVER_IMG);
                httppost.setHeader("token", token);
                HttpResponse response = null;
                HttpEntity r_entity = null;

                if (str_profile_cover != null) {
                    Log.e("tag", "strt111" + str_profile_cover);
                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    entity.addPart("personalcover", new FileBody(new File(str_profile_cover), "image/jpeg"));
                    httppost.setEntity(entity);
                }

                try {

                    try {
                        response = httpclient.execute(httppost);
                    } catch (Exception e) {
                        Log.e("tag", "ds:" + e.toString());
                    }

                    try {
                        r_entity = response.getEntity();
                    } catch (Exception e) {
                        Log.e("tag", "dsa:" + e.toString());
                    }

                    int statusCode = response.getStatusLine().getStatusCode();
                    Log.e("tag", response.getStatusLine().toString());
                    if (statusCode == 200) {
                        responseString = EntityUtils.toString(r_entity);
                        Log.e("tag", "rssss" + responseString);
                        JSONObject jo = new JSONObject(responseString);
                        String success = jo.getString("success");

                        return success;


                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                        Log.e("tag3", responseString);
                    }
                } catch (ClientProtocolException e) {
                    responseString = e.toString();
                    Log.e("tag44", responseString);
                } catch (IOException e) {
                    responseString = e.toString();
                    Log.e("tag45", responseString);
                }
                return responseString;
            } catch (Exception e) {
                Log.e("tag_InputStream0", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loader_dialog.dismiss();
            //Toast.makeText(getActivity(),"Event Created Successfully",Toast.LENGTH_LONG).show();

        }

    }

    public class getpersonalprofile extends AsyncTask<String, Void, String> {
        String phone_str, str_pwd;

        public getpersonalprofile() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_GETPERSONAL, json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "getprofile------->" + s);
            loader_dialog.dismiss();
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        JSONObject message = jo.getJSONObject("message");
                        if (message.has("personalimage")) {
                            personalimage = message.getString("personalimage");
                            if (!personalimage.equals("")) {
                                Log.e("tag", "getprofileelseee------->");
                                try {
                                    Glide.with(ProfileActivity.this).load("http://104.197.80.225:3010/wow/media/personal/" + personalimage).into(profile_iv);
                                    editor.putString("profilepath", personalimage);
                                    editor.commit();
                                } catch (IllegalArgumentException e) {

                                }
                            }
                        }
                        if (message.has("personalcover")) {
                            personalcover = message.getString("personalcover");
                            Log.e("tag", "getprofileelseee------->" + personalimage);
                            if (!personalcover.equals("")) {

                                try {
                                    Glide.with(ProfileActivity.this).load("http://104.197.80.225:3010/wow/media/personal/" + personalcover).into(coverpageiv);

                                } catch (IllegalArgumentException e) {

                                }

                            }
                        }
                        if (message.has("personalself")) {
                            personalself = message.getString("personalself");
                            Log.e("tag", "personalself333333------->" + personalself);
                            if (!personalself.equals("")) {

                                try {
                                    String selectedVideoFilePath1 = "http://104.197.80.225:3010/wow/media/personal/" + personalself;
                                    Log.d("tag", "567231546" + selectedVideoFilePath1);
                                    try {
                                        retriveVideoFrameFromVideo(selectedVideoFilePath1);
                                        video0_iv.setImageBitmap(bitmap);
                                        video1plus.setImageDrawable(ProfileActivity.this.getDrawable(R.drawable.video_icon));
                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }

                                } catch (IllegalArgumentException e) {

                                }

                            }
                        }


                    }

                } catch (JSONException e) {

                }

            } else {

            }

        }

    }

    public class profileselfintro_task extends AsyncTask<String, Void, String> {
        String intropath;

        profileselfintro_task(String path) {
            this.intropath = path;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                String responseString = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.WEB_URL_SELFINTRO);
                httppost.setHeader("token", token);
                HttpResponse response = null;
                HttpEntity r_entity = null;

                if (intropath != null) {
                    Log.e("tag", "strt111" + intropath);
                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    entity.addPart("personalself", new FileBody(new File(intropath), "video/mp4"));
                    httppost.setEntity(entity);
                }

                try {

                    try {
                        response = httpclient.execute(httppost);
                    } catch (Exception e) {
                        Log.e("tag", "ds:" + e.toString());
                    }

                    try {
                        r_entity = response.getEntity();
                    } catch (Exception e) {
                        Log.e("tag", "dsa:" + e.toString());
                    }

                    int statusCode = response.getStatusLine().getStatusCode();
                    Log.e("tag", response.getStatusLine().toString());
                    if (statusCode == 200) {
                        responseString = EntityUtils.toString(r_entity);
                        Log.e("tag", "rssss" + responseString);
                        JSONObject jo = new JSONObject(responseString);
                        String success = jo.getString("success");

                        return success;


                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                        Log.e("tag3", responseString);
                    }
                } catch (ClientProtocolException e) {
                    responseString = e.toString();
                    Log.e("tag44", responseString);
                } catch (IOException e) {
                    responseString = e.toString();
                    Log.e("tag45", responseString);
                }
                return responseString;
            } catch (Exception e) {
                Log.e("tag_InputStream0", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loader_dialog.dismiss();
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        String message = jo.getString("message");
                        editor.putString("personalself", message);
                        editor.commit();

                    }

                } catch (JSONException e) {

                }

            } else {

            }

        }


    }


}
