package com.wowhubb.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wowhubb.R;
import com.wowhubb.Utils.Config;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

/**
 * Created by Salman on 01-12-2017.
 */

public class TestVideo extends Activity
{
    Button btnvideo;
    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Uri fileUri; // file url to store image/video

    private Button btnCapturePicture, btnRecordVideo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testvideo);
        btnvideo=findViewById(R.id.button);

        btnvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

                // set video quality
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
                // name

                // start the video capture Intent
                startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
            }
        });
    }



    public Uri getOutputMediaFileUri(int type) {

        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

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
            Log.e("tag","1------------>>>>>>"+mediaFile);

        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
            Log.e("tag","222------------>>>>>>"+mediaFile);
        } else {
            return null;
        }

        return mediaFile;
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(TestVideo.this, Manifest.permission.CAMERA);
        int result1 = ContextCompat.checkSelfPermission(TestVideo.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(TestVideo.this, Manifest.permission.RECORD_AUDIO);

        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED)&& (result2 == PackageManager.PERMISSION_GRANTED)) {
            Log.e("tag", "Permission is granted");
            return true;


        } else {
            Log.e("tag", "Permission is revoked");
            ActivityCompat.requestPermissions(TestVideo.this, new String[]{Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO}, 1);
            return false;

        }
    }
}
