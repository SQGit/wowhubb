package com.wowhubb.Adapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.wowhubb.R;
import com.wowhubb.data.FeedItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Salman on 24-10-2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    //private ArrayList<MediaStore.Video> mVideoList;
    private List<FeedItem> feedItems;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public String mBoundString;

        public final View mView;
        public final VideoView mVideoView;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mVideoView = (VideoView) view.findViewById(R.id.videoview);

        }


    }

    public RecyclerAdapter(Context context, List<FeedItem> feedItems) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        feedItems = feedItems;
        mBackground = mTypedValue.resourceId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       // feedItems=new ArrayList<>();

        final FeedItem item = feedItems.get(position);

       // holder.mBoundString = tempVideo.getGifTitle();
      //  holder.mTextView.setText(tempVideo.getGifTitle());

   /*     float count = tempVideo.getCount();
        String countText = "";

        if (count >= 1000)
            countText = String.format("%.1f", count / 1000) + "k";
        else
            countText = String.format("%d", (int)count);*/

       // holder.mCountTextView.setText(countText);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mVideoView.start();
            }
        });

        holder.mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // This is just to show image when loaded
                mp.start();
                mp.pause();
            }
        });

        holder.mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // setLooping(true) didn't work, thats why this workaround
                holder.mVideoView.setVideoPath(item.getWowtagvideo());
                holder.mVideoView.start();
            }
        });

        holder.mVideoView.setVideoPath(item.getWowtagvideo());
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }
}