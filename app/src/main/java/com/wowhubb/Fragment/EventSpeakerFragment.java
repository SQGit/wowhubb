package com.wowhubb.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.GetFilePathFromDevice;
import com.wowhubb.R;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class EventSpeakerFragment extends Fragment {

    private TextView lblPage;
    Typeface lato;
    TextInputLayout til_desc, til_url;
    FrameLayout speaker, highlight;
    ImageView speakeriv, highlight_iv, speakerplus_iv, highlightplus_iv;

    private static final int INTENT_REQUEST_GET_SPEAKER = 11;
    private static final int INTENT_REQUEST_GET_HIGHLIGHTS = 12;

    public static EventSpeakerFragment newInstance(int page, boolean isLast) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        if (isLast)
            args.putBoolean("isLast", true);
        final EventSpeakerFragment fragment = new EventSpeakerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_eventspeaker, container, false);
        FontsOverride.overrideFonts(getActivity(), view);
        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");

        til_desc = (TextInputLayout) view.findViewById(R.id.til_description);
        til_url = (TextInputLayout) view.findViewById(R.id.til_url);
        speaker = (FrameLayout) view.findViewById(R.id.framespeaker);
        highlight = (FrameLayout) view.findViewById(R.id.framehighlights);

        speakeriv = (ImageView) view.findViewById(R.id.speaker_iv);
        highlight_iv = (ImageView) view.findViewById(R.id.highlightiv);

        speakerplus_iv = (ImageView) view.findViewById(R.id.speakerplus);
        highlightplus_iv = (ImageView) view.findViewById(R.id.highlightplus);


        til_url.setTypeface(lato);
        til_desc.setTypeface(lato);

        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                startActivityForResult(pickIntent, INTENT_REQUEST_GET_SPEAKER);


            }
        });


        highlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // pickIntent.setType("image/*","video/*");
                pickIntent.setType("*/*");
                pickIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
                startActivityForResult(pickIntent, INTENT_REQUEST_GET_HIGHLIGHTS);

            }
        });







        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            Log.e("tag", "code------------->" + resultCode);
            if (requestCode == INTENT_REQUEST_GET_SPEAKER) {
                Uri selectedMediaUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    // Log.d(TAG, String.valueOf(bitmap));
                    speakeriv.setImageBitmap(bitmap);
                    speakerplus_iv.setVisibility(View.GONE);
                    // video1plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }  else {
                Uri selectedMediaUri = data.getData();

                if (selectedMediaUri.toString().contains("image")) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                        highlight_iv.setImageBitmap(bitmap);
                        highlightplus_iv.setVisibility(View.GONE);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else if (selectedMediaUri.toString().contains("video")) {
                    Log.d("tag", "567231546" + selectedMediaUri);
                    //  Uri selectedVideo = data.getData();
                    String selectedVideoFilePath = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);

                    highlight_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                    highlightplus_iv.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));

                }

            }


        }
    }

}