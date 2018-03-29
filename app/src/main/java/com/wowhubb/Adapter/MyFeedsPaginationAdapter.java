package com.wowhubb.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.wowhubb.Activity.EventInviteActivity;
import com.wowhubb.Activity.ViewFeedActivityDetails;
import com.wowhubb.Activity.ViewMoreDetailspage;




import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.MyFeedsData.Eventvenue;
import com.wowhubb.MyFeedsData.Message;
import com.wowhubb.R;
import com.wowhubb.Utils.HttpUtils;
import com.wowhubb.app.ImageLoader;
import com.wowhubb.data.FeedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Suleiman on 19/10/16.
 */

public class MyFeedsPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int THOUGHT = 0;
    private static final int LOADING = 6;
    private static final int PRIVATE = 1;
    private static final int PROFESSIONAL = 2;
    private static final int BUSINESS = 3;
    private static final int SOCIAL = 4;
    private static final int QUICK = 5;

    static Bitmap bitmap;
    public ImageLoader imageLoader1;
    String token, wowsomecount, username, userimage, thoughtid, eventId, userId;
    EditText comments_et;
    CommentsRecyclerAdapter recyclerAdapter;
    RecyclerView commentslistview;
    Dialog comments_dialog, menu_dialog;

    private ArrayList<FeedItem> feedList = new ArrayList<>();
    private List<Message> docs;
    private Context context;
    private boolean isLoadingAdded = false;
    private ArrayList<FeedItem> feedItems = new ArrayList<>();
    private int i = 0;
Activity activity;
    public MyFeedsPaginationAdapter(Context context) {
        this.context = context;
        this.docs = docs;

        docs = new ArrayList<>();
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        imageLoader1 = new ImageLoader(context);
        switch (viewType) {

            case PRIVATE:
                View viewPrivate1 = inflater.inflate(R.layout.feed_items_private, parent, false);
                FontsOverride.overrideFonts(context, viewPrivate1);
                viewHolder = new PrivateVH(viewPrivate1);
                break;

            case PROFESSIONAL:
                View viewPrivate = inflater.inflate(R.layout.feed_items_professional, parent, false);
                FontsOverride.overrideFonts(context, viewPrivate);
                viewHolder = new ProfessionalVH(viewPrivate);
                break;
            case BUSINESS:
                View viewbusiness = inflater.inflate(R.layout.feed_items_business, parent, false);
                FontsOverride.overrideFonts(context, viewbusiness);
                viewHolder = new BusinessVH(viewbusiness);
                break;
            case SOCIAL:
                View viewsocial = inflater.inflate(R.layout.feed_items_social, parent, false);
                FontsOverride.overrideFonts(context, viewsocial);
                viewHolder = new SocialVH(viewsocial);
                break;
            case THOUGHT:
                View viewthoughts = inflater.inflate(R.layout.feed_items_thoughts, parent, false);
                FontsOverride.overrideFonts(context, viewthoughts);
                viewHolder = new ThoughtsVH(viewthoughts);
                break;

            case QUICK:
                View quickthoughts = inflater.inflate(R.layout.feed_items_quickevent, parent, false);
                FontsOverride.overrideFonts(context, quickthoughts);
                viewHolder = new QuickVH(quickthoughts);
                break;
            case LOADING:
                //   View v2 = inflater.inflate(R.layout.test_loader, parent, false);
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.feed_items_private, parent, false);
        FontsOverride.overrideFonts(context, v1);
        viewHolder = new PrivateVH(v1);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        token = sharedPreferences.getString("token", "");
        userimage = sharedPreferences.getString("profilepath", "");
        username = sharedPreferences.getString("str_name", "");
        userId = sharedPreferences.getString("userid", "");

        Message doc = docs.get(position);

        switch (getItemViewType(position)) {


            case THOUGHT:


                break;
            case QUICK:
                final QuickVH quickVH = (QuickVH) holder;



                if (doc.getEventtimezone() != null) {
                    quickVH.address_tv.setText(doc.getEventtimezone());
                }
                //-------------------------EVENT NAME---------------------------------------------//
                if (doc.getEventname() != null) {
                    quickVH.eventname_tv.setText(docs.get(position).getEventtitle());
                }
                //-------------------------EVENT NAME---------------------------------------------//
                if (doc.getEventname() != null) {
                    quickVH.eventtopic_tv.setText("! " + docs.get(position).getEventname());
                }

                //-------------------------EVENT CATAGORY-----------------------------------------//
                if (doc.getEventcategory() != null) {
                    quickVH.eventcategory_tv.setText("- " + docs.get(position).getEventcategory());
                }






                if (doc.getWowtagvideo() != null && !doc.getWowtagvideo().equals("null")) {
                    quickVH.frameLayout.setVisibility(View.VISIBLE);
                }


                //-------------------------WOMSOMES COUNT------------------------------------------//

                if (doc.getWowsomecount() != null) {
                    quickVH.viewwowsome.setVisibility(View.VISIBLE);
                    quickVH.viewwowsome.setText(doc.getWowsomecount() + " Wowsomes");
                }

                //-------------------------COMMENTS COUNT------------------------------------------//
                if (doc.getCommentcount() != null) {
                    quickVH.viewcomments.setText(doc.getCommentcount() + " Comments");
                    quickVH.viewcomments.setVisibility(View.VISIBLE);
                }


                quickVH.sendinvite_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        Log.e("tag","11111111111");
                        final Message msg = docs.get(position);
                        Intent intent = new Intent(context, EventInviteActivity.class);
                        Log.e("tag","11111111111"+msg.getId());
                        Bundle bundle = new Bundle();
                        bundle.putString("eventId",msg.getId());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });


                quickVH.menu_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        menu_dialog = new BottomSheetDialog(context);
                        menu_dialog.setContentView(R.layout.dialog_menu_list);
                        menu_dialog.setCancelable(true);
                        View v1 = menu_dialog.getWindow().getDecorView().getRootView();
                        FontsOverride.overrideFonts(context, v1);
                        menu_dialog.show();
                        TextView deletetv = menu_dialog.findViewById(R.id.delete_tv);
                        final Message doc = docs.get(position);

                        deletetv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                eventId = doc.getId();
                                new DeleteEvents(eventId, position).execute();

                            }
                        });


                    }
                });


                break;

            case PRIVATE:

                final PrivateVH privateVH = (PrivateVH) holder;


                //-------------------------WOWSOMES COUNT------------------------------------------//

                if (doc.getWowsomecount() != null) {


                }

                //-------------------------DESCRIPTION---------------------------------------------//

                if (doc.getEventdescription() != null) {
                    privateVH.desc.setText(docs.get(position).getEventdescription());
                }

                //-------------------------EVENT NAME---------------------------------------------//
                if (doc.getEventtitle() != null) {
                    privateVH.eventname_tv.setText("! " + docs.get(position).getEventtitle());
                    // privateVH.eventaddress_tv.setText(docs.get(position).getEventvenue().toString());
                }


                //-------------------------COVER IMAGE---------------------------------------------//

                Glide
                        .with(context)
                        .load("http://104.197.80.225:3010/wow/media/event/" + doc.getCoverpage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(privateVH.feedImageView);


                Glide
                        .with(context)
                        .load("http://104.197.80.225:3010/wow/media/event/" + doc.getEventhighlights1())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(privateVH.highlight1_iv);


                //-------------------------EVENT NAME---------------------------------------------//
                if (doc.getEventname() != null) {
                    privateVH.eventtopic_tv.setText(docs.get(position).getEventname());
                }


                //-------------------------EVENT CATAGORY-----------------------------------------//
                if (doc.getEventcategory() != null) {
                    privateVH.eventcategory_tv.setText("- " + docs.get(position).getEventcategory());
                }

              /*  //-------------------------EVENT TIMEE ZONE----------------------------------------//
                if (doc.getEventcategory() != null) {
                    privateVH.eventcategory_tv.setText("- " + docs.get(position).getEventcategory());
                }*/
                //-------------------------EVENT ADDRESS-------------------------------------------//
                if (doc.getEventtimezone() != null) {
                    privateVH.eventaddress_tv.setText(doc.getEventtimezone());
                }
                //-------------------------WOMSOMES COUNT------------------------------------------//
                if (doc.getWowsomecount() != null) {
                    privateVH.viewwowsome.setVisibility(View.VISIBLE);
                    privateVH.viewwowsome.setText(doc.getWowsomecount() + " Wowsomes");
                }
                //-------------------------COMMENTS COUNT------------------------------------------//
                if (doc.getCommentcount() != null) {
                    privateVH.viewcomments.setText(doc.getCommentcount() + " Comments");
                    privateVH.viewcomments.setVisibility(View.VISIBLE);
                }
                List<Eventvenue> eventvenues = docs.get(position).getEventvenue();

                Log.e("tag", "eventvenue---" + eventvenues);

                if (eventvenues != null) {
                    if (eventvenues.size() > 0) {
                        for (int i = 0; i < eventvenues.size(); i++) {
                            privateVH.eventaddress_tv.setText(eventvenues.get(0).getEventvenuename() + " , " + eventvenues.get(0).getEventvenueaddress1() + ", " + eventvenues.get(0).getEventvenuecity() + ", " +
                                    eventvenues.get(0).getEventvenuezipcode());
                        }
                    }
                }

                privateVH.sendinvite_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                        final Message msg = docs.get(position);
                        Intent intent = new Intent(context, EventInviteActivity.class);
                        Log.e("tag","11111111111"+msg.getId());
                        Bundle bundle = new Bundle();
                        bundle.putString("eventId",msg.getId());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
                privateVH.viewmore_tv.setVisibility(View.GONE);


                break;

            case PROFESSIONAL:
                final ProfessionalVH prfessionalVH = (ProfessionalVH) holder;


                //-------------------------WOWSOMES COUNT------------------------------------------//

                if (doc.getWowsomecount() != null) {

                }

                //-------------------------DESCRIPTION---------------------------------------------//

                if (doc.getEventdescription() != null) {
                    prfessionalVH.desc.setText(docs.get(position).getEventdescription());
                    // privateVH.eventaddress_tv.setText(docs.get(position).getEventvenue().toString());
                }

                //-------------------------EVENT NAME---------------------------------------------//
                if (doc.getEventtitle() != null) {
                    prfessionalVH.eventname_tv.setText("! " + docs.get(position).getEventtitle());
                    // privateVH.eventaddress_tv.setText(docs.get(position).getEventvenue().toString());
                }
                prfessionalVH.sendinvite_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                        final Message msg = docs.get(position);
                        Intent intent = new Intent(context, EventInviteActivity.class);
                        Log.e("tag","11111111111"+msg.getId());
                        Bundle bundle = new Bundle();
                        bundle.putString("eventId",msg.getId());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });


                //-------------------------COVER IMAGE---------------------------------------------//

                Glide
                        .with(context)
                        .load("http://104.197.80.225:3010/wow/media/event/" + doc.getCoverpage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(prfessionalVH.feedImageView);


                Glide
                        .with(context)
                        .load("http://104.197.80.225:3010/wow/media/event/" + doc.getEventhighlights1())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(prfessionalVH.highlight1_iv);


                //-------------------------EVENT NAME---------------------------------------------//
                if (doc.getEventname() != null) {
                    prfessionalVH.eventtopic_tv.setText(docs.get(position).getEventname());
                }


                //-------------------------EVENT CATAGORY-----------------------------------------//
                if (doc.getEventcategory() != null) {
                    prfessionalVH.eventcategory_tv.setText("- " + docs.get(position).getEventcategory());
                }

               /* //-------------------------EVENT TIMEE ZONE----------------------------------------//
                if (doc.getEventcategory() != null) {
                    prfessionalVH.eventcategory_tv.setText("- " + docs.get(position).getEventcategory());
                }*/
                //-------------------------EVENT ADDRESS-------------------------------------------//
                if (doc.getEventtimezone() != null) {
                    prfessionalVH.eventaddress_tv.setText(doc.getEventtimezone());
                }
                //-------------------------WOMSOMES COUNT------------------------------------------//
                if (doc.getWowsomecount() != null) {
                    prfessionalVH.viewwowsome.setVisibility(View.VISIBLE);
                    prfessionalVH.viewwowsome.setText(doc.getWowsomecount() + " Wowsomes");
                }
                //-------------------------COMMENTS COUNT------------------------------------------//
                if (doc.getCommentcount() != null) {
                    prfessionalVH.viewcomments.setText(doc.getCommentcount() + " Comments");
                    prfessionalVH.viewcomments.setVisibility(View.VISIBLE);
                }

                List<Eventvenue> eventvenuespro = docs.get(position).getEventvenue();

                Log.e("tag", "eventvenue---" + eventvenuespro);

                if (eventvenuespro != null) {
                    if (eventvenuespro.size() > 0) {
                        for (int i = 0; i < eventvenuespro.size(); i++) {
                            prfessionalVH.eventaddress_tv.setText(eventvenuespro.get(0).getEventvenuename() + " , " + eventvenuespro.get(0).getEventvenueaddress1() + ", " + eventvenuespro.get(0).getEventvenuecity() + ", " +
                                    eventvenuespro.get(0).getEventvenuezipcode());
                        }
                    }
                }
                prfessionalVH.menu_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        menu_dialog = new BottomSheetDialog(context);
                        menu_dialog.setContentView(R.layout.dialog_menu_list);
                        menu_dialog.setCancelable(true);
                        View v1 = menu_dialog.getWindow().getDecorView().getRootView();
                        FontsOverride.overrideFonts(context, v1);
                        menu_dialog.show();
                        TextView deletetv = menu_dialog.findViewById(R.id.delete_tv);
                        final Message doc = docs.get(position);

                        deletetv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                eventId = doc.getId();
                                new DeleteEvents(eventId, position).execute();

                            }
                        });

                    }
                });


                prfessionalVH.viewmore_tv.setVisibility(View.GONE);

                break;


            case SOCIAL:
                final SocialVH socialVH = (SocialVH) holder;


                //-------------------------WOWSOMES COUNT------------------------------------------//

                if (doc.getWowsomecount() != null) {

                    //  privateVH.wowsome_tv.


                }


                //-------------------------DESCRIPTION---------------------------------------------//

                if (doc.getEventdescription() != null) {
                    socialVH.desc.setText(docs.get(position).getEventdescription());
                    // privateVH.eventaddress_tv.setText(docs.get(position).getEventvenue().toString());
                }

                //-------------------------EVENT NAME---------------------------------------------//
                if (doc.getEventname() != null) {
                    socialVH.eventname_tv.setText("! " + docs.get(position).getEventname());
                    // privateVH.eventaddress_tv.setText(docs.get(position).getEventvenue().toString());
                }
                socialVH.sendinvite_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                        final Message msg = docs.get(position);
                        Intent intent = new Intent(context, EventInviteActivity.class);
                        Log.e("tag","11111111111"+msg.getId());
                        Bundle bundle = new Bundle();
                        bundle.putString("eventId",msg.getId());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });





                //-------------------------COVER IMAGE---------------------------------------------//

                Glide
                        .with(context)
                        .load("http://104.197.80.225:3010/wow/media/event/" + doc.getCoverpage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(socialVH.feedImageView);


                Glide
                        .with(context)
                        .load("http://104.197.80.225:3010/wow/media/event/" + doc.getEventhighlights1())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(socialVH.highlight1_iv);


                //-------------------------EVENT NAME---------------------------------------------//
                if (doc.getEventtitle() != null) {
                    socialVH.eventtopic_tv.setText(docs.get(position).getEventtitle());
                }


                //-------------------------EVENT CATAGORY-----------------------------------------//
                if (doc.getEventcategory() != null) {
                    socialVH.eventcategory_tv.setText("- " + docs.get(position).getEventcategory());
                }

               /* //-------------------------EVENT TIMEE ZONE----------------------------------------//
                if (doc.getEventcategory() != null) {
                    socialVH.eventcategory_tv.setText("- " + docs.get(position).getEventcategory());
                }*/
                //-------------------------EVENT ADDRESS-------------------------------------------//
                if (doc.getEventtimezone() != null) {
                    socialVH.eventaddress_tv.setText(doc.getEventtimezone());
                }
                //-------------------------WOMSOMES COUNT------------------------------------------//
                if (doc.getWowsomecount() != null) {
                    socialVH.viewwowsome.setVisibility(View.VISIBLE);
                    socialVH.viewwowsome.setText(doc.getWowsomecount() + " Wowsomes");
                }
                //-------------------------COMMENTS COUNT------------------------------------------//
                if (doc.getCommentcount() != null) {
                    socialVH.viewcomments.setText(doc.getCommentcount() + " Comments");
                    socialVH.viewcomments.setVisibility(View.VISIBLE);
                }

                List<Eventvenue> eventvenuessocial = docs.get(position).getEventvenue();

                Log.e("tag", "eventvenue---" + eventvenuessocial);

                if (eventvenuessocial != null) {
                    if (eventvenuessocial.size() > 0) {
                        for (int i = 0; i < eventvenuessocial.size(); i++) {
                            socialVH.eventaddress_tv.setText(eventvenuessocial.get(0).getEventvenuename() + " , " + eventvenuessocial.get(0).getEventvenueaddress1() + ", " + eventvenuessocial.get(0).getEventvenuecity() + ", " +
                                    eventvenuessocial.get(0).getEventvenuezipcode());
                        }
                    }
                }



                socialVH.viewmore_tv.setVisibility(View.GONE);



                break;

            case BUSINESS:
                final BusinessVH businessVH = (BusinessVH) holder;

                //-------------------------WOWSOMES COUNT------------------------------------------//

                if (doc.getWowsomecount() != null) {


                }

                //-------------------------DESCRIPTION---------------------------------------------//

                if (doc.getEventdescription() != null) {
                    businessVH.desc.setText(docs.get(position).getEventdescription());
                    // privateVH.eventaddress_tv.setText(docs.get(position).getEventvenue().toString());
                }

                //-------------------------EVENT NAME---------------------------------------------//
                if (doc.getEventtitle() != null) {
                    businessVH.eventname_tv.setText("! " + docs.get(position).getEventtitle());
                    // privateVH.eventaddress_tv.setText(docs.get(position).getEventvenue().toString());
                }

                businessVH.sendinvite_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                        final Message msg = docs.get(position);
                        Intent intent = new Intent(context, EventInviteActivity.class);
                        Log.e("tag","11111111111"+msg.getId());
                        Bundle bundle = new Bundle();
                        bundle.putString("eventId",msg.getId());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });


                //-------------------------COVER IMAGE---------------------------------------------//

                Glide
                        .with(context)
                        .load("http://104.197.80.225:3010/wow/media/event/" + doc.getCoverpage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(businessVH.feedImageView);


                Glide
                        .with(context)
                        .load("http://104.197.80.225:3010/wow/media/event/" + doc.getEventhighlights1())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(businessVH.highlight1_iv);


                //-------------------------EVENT NAME---------------------------------------------//
                if (doc.getEventname() != null) {
                    businessVH.eventtopic_tv.setText("! " + docs.get(position).getEventname());
                }


                //-------------------------EVENT CATAGORY-----------------------------------------//
                if (doc.getEventcategory() != null) {
                    businessVH.eventcategory_tv.setText("- " + docs.get(position).getEventcategory());
                }

               /* //-------------------------EVENT TIMEE ZONE----------------------------------------//
                if (doc.getEventcategory() != null) {
                    businessVH.eventcategory_tv.setText("- " + docs.get(position).getEventcategory());
                }*/
                //-------------------------EVENT ADDRESS-------------------------------------------//
                if (doc.getEventtimezone() != null) {
                    businessVH.eventaddress_tv.setText(doc.getEventtimezone());
                }
                //-------------------------WOMSOMES COUNT------------------------------------------//
                if (doc.getWowsomecount() != null) {
                    businessVH.viewwowsome.setVisibility(View.VISIBLE);
                    businessVH.viewwowsome.setText(doc.getWowsomecount() + " Wowsomes");
                }
                //-------------------------COMMENTS COUNT------------------------------------------//
                if (doc.getCommentcount() != null) {
                    businessVH.viewcomments.setText(doc.getCommentcount() + " Comments");
                    businessVH.viewcomments.setVisibility(View.VISIBLE);
                }


                businessVH.menu_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        menu_dialog = new BottomSheetDialog(context);
                        menu_dialog.setContentView(R.layout.dialog_menu_list);
                        menu_dialog.setCancelable(true);
                        View v1 = menu_dialog.getWindow().getDecorView().getRootView();
                        FontsOverride.overrideFonts(context, v1);
                        menu_dialog.show();
                        TextView deletetv = menu_dialog.findViewById(R.id.delete_tv);
                        final Message doc = docs.get(position);

                    }
                });
                businessVH.wowsome_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Message doc = docs.get(position);
                        String eventId = doc.getId();
                        Log.e("tag", "-----eventId---------->>>" + eventId);
                        new addWowsome(eventId).execute();
                        businessVH.viewwowsome.setVisibility(view.VISIBLE);
                        businessVH.viewwowsome.setText(wowsomecount + " Wowsomes");
                    }
                });

businessVH.viewmore_tv.setVisibility(View.GONE);


                break;


            case LOADING:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return docs == null ? 0 : docs.size();
    }

    @Override
    public int getItemViewType(int position) {
        //    return (position == docs.size() - 1 && isLoadingAdded) ? LOADING : ITEM;

        int s = docs.get(position).getEventtypeint();

        switch (s) {
            case 0:
                return THOUGHT;

            case 1:
                return PRIVATE;
            case 2:
                return PROFESSIONAL;

            case 4:
                return SOCIAL;

            case 3:
                return BUSINESS;
            case 5:
                return QUICK;

            default:
                return (position == docs.size() - 1 && isLoadingAdded) ?
                        LOADING : PRIVATE;


        }


    }

    public void add(Message mc) {
        docs.add(mc);
        notifyItemInserted(docs.size() - 1);
    }

    public void addAll(List<Message> mcList) {
        for (Message mc : mcList) {
            add(mc);
        }
    }

    public void remove(Message city) {
        int position = docs.indexOf(city);
        if (position > -1) {
            docs.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeThoughts(int pos) {
        if (pos > -1) {
            docs.remove(pos);
            notifyItemRemoved(pos);
        }
    }






    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Message());
    }



    public Message getItem(int position) {
        return docs.get(position);
    }

    protected static class PrivateVH extends RecyclerView.ViewHolder {

        private static ImageView video3plus;
        TextView viewmore_tv, viewcomments, viewshare, wowsome_tv, viewwowsome, eventcategory_tv;
        private TextView sendinvite_tv,timestamp, eventname_tv, name, desc, share, menu_tv, comments, eventtopic_tv, otherurl_tv, eventaddress_tv, month_tv, date_tv, time_tv;
        private ImageView profilePic;
        private ImageView feedImageView, wowtagvideo, highlight1_iv, highlight2_iv;
        private ImageView video1plus;
        private FrameLayout frameLayout;
        private LinearLayout runttime_lv;


        public PrivateVH(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.hoster_name);
            eventname_tv = (TextView) itemView.findViewById(R.id.eventname_tv);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            desc = (TextView) itemView.findViewById(R.id.desc_tv);
            profilePic = (ImageView) itemView.findViewById(R.id.imageview_profile);
            feedImageView = (ImageView) itemView.findViewById(R.id.feedImage1);

            wowsome_tv = itemView.findViewById(R.id.wowsome_tv);
            menu_tv = itemView.findViewById(R.id.menu_tv);
            eventcategory_tv = itemView.findViewById(R.id.eventcategory_tv);
            comments = itemView.findViewById(R.id.comments_tv);
            share = itemView.findViewById(R.id.share_tv);
            viewmore_tv = itemView.findViewById(R.id.viewmore_tv);
            viewwowsome = itemView.findViewById(R.id.viewwow_tv);
            viewcomments = itemView.findViewById(R.id.viewcomments_tv);
            viewshare = itemView.findViewById(R.id.viewshare_tv);
            sendinvite_tv = itemView.findViewById(R.id.sendinvite_tv);

            month_tv = itemView.findViewById(R.id.month_tv);
            date_tv = itemView.findViewById(R.id.date_tv);
            time_tv = itemView.findViewById(R.id.time_tv);
            runttime_lv = itemView.findViewById(R.id.runtimelv);

            eventtopic_tv = itemView.findViewById(R.id.eventtopic_tv);
            otherurl_tv = itemView.findViewById(R.id.otherurl_tv);
            eventaddress_tv = itemView.findViewById(R.id.address_tv);
            frameLayout = itemView.findViewById(R.id.framevideo1);

            highlight1_iv = itemView.findViewById(R.id.highlight1);
            highlight2_iv = itemView.findViewById(R.id.highlight2);
            video1plus = itemView.findViewById(R.id.video1plus_iv);
            video3plus = itemView.findViewById(R.id.video3plus_iv);
            wowtagvideo = itemView.findViewById(R.id.video0_iv);


        }
    }

    protected static class ThoughtsVH extends RecyclerView.ViewHolder {

        private static ImageView video3plus;
        TextView viewmore_tv, viewcomments, viewshare, wowsome_tv, viewwowsome, eventcategory_tv;
        private TextView link_tv, timestamp, eventname_tv, name, desc, share, comments, menu_tv, eventtopic_tv, otherurl_tv, eventaddress_tv, month_tv, date_tv, time_tv;
        private ImageView profilePic;
        private ImageView feedImageView, wowtagvideo, highlight1_iv, highlight2_iv;
        private ImageView video1plus;
        private FrameLayout frameLayout;
        private LinearLayout runttime_lv, url_lv;


        public ThoughtsVH(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.hoster_name);
            eventname_tv = (TextView) itemView.findViewById(R.id.eventname_tv);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            desc = (TextView) itemView.findViewById(R.id.desc_tv);
            profilePic = (ImageView) itemView.findViewById(R.id.imageview_profile);
            feedImageView = (ImageView) itemView.findViewById(R.id.feedImage1);
            menu_tv = itemView.findViewById(R.id.menu_tv);
            wowsome_tv = itemView.findViewById(R.id.wowsome_tv);
            eventcategory_tv = itemView.findViewById(R.id.eventcategory_tv);
            comments = itemView.findViewById(R.id.comments_tv);
            share = itemView.findViewById(R.id.share_tv);
            viewmore_tv = itemView.findViewById(R.id.viewmore_tv);
            viewwowsome = itemView.findViewById(R.id.viewwow_tv);
            viewcomments = itemView.findViewById(R.id.viewcomments_tv);
            viewshare = itemView.findViewById(R.id.viewshare_tv);
            url_lv = itemView.findViewById(R.id.link_lv);
            link_tv = itemView.findViewById(R.id.link_tv);
            month_tv = itemView.findViewById(R.id.month_tv);
            date_tv = itemView.findViewById(R.id.date_tv);
            time_tv = itemView.findViewById(R.id.time_tv);
            runttime_lv = itemView.findViewById(R.id.runtimelv);
            eventtopic_tv = itemView.findViewById(R.id.eventtopic_tv);
            otherurl_tv = itemView.findViewById(R.id.otherurl_tv);
            eventaddress_tv = itemView.findViewById(R.id.address_tv);
            frameLayout = itemView.findViewById(R.id.framevideo1);

            wowtagvideo = itemView.findViewById(R.id.video0_iv);


        }
    }

    protected static class QuickVH extends RecyclerView.ViewHolder {

        private static ImageView video3plus;
        TextView viewmore_tv, viewcomments, viewshare, wowsome_tv, viewwowsome, eventcategory_tv;
        private TextView sendinvite_tv, eventtopic_tv, timestamp, eventname_tv, name, desc, share, comments, menu_tv, month_tv, date_tv, time_tv, link_tv, address_tv;
        private ImageView profilePic;
        private ImageView wowtagvideo;
        private ImageView video1plus;
        private FrameLayout frameLayout;
        private LinearLayout runttime_lv;


        public QuickVH(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.hoster_name);

            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            desc = (TextView) itemView.findViewById(R.id.desc_tv);
            profilePic = (ImageView) itemView.findViewById(R.id.imageview_profile);
            eventname_tv = (TextView) itemView.findViewById(R.id.eventname_tv);
            menu_tv = itemView.findViewById(R.id.menu_tv);
            wowsome_tv = itemView.findViewById(R.id.wowsome_tv);
            eventcategory_tv = itemView.findViewById(R.id.eventcategory_tv);
            comments = itemView.findViewById(R.id.comments_tv);
            share = itemView.findViewById(R.id.share_tv);
            viewmore_tv = itemView.findViewById(R.id.viewmore_tv);
            viewwowsome = itemView.findViewById(R.id.viewwow_tv);
            viewcomments = itemView.findViewById(R.id.viewcomments_tv);
            viewshare = itemView.findViewById(R.id.viewshare_tv);
            address_tv = itemView.findViewById(R.id.address_tv);
            eventtopic_tv = itemView.findViewById(R.id.eventtopic_tv);
            sendinvite_tv = itemView.findViewById(R.id.sendinvite_tv);
            month_tv = itemView.findViewById(R.id.month_tv);
            date_tv = itemView.findViewById(R.id.date_tv);
            time_tv = itemView.findViewById(R.id.time_tv);
            runttime_lv = itemView.findViewById(R.id.runtimelv);
            video3plus = itemView.findViewById(R.id.video1plus_iv);
            frameLayout = itemView.findViewById(R.id.framevideo1);
            wowtagvideo = itemView.findViewById(R.id.video0_iv);


        }
    }

    protected class ProfessionalVH extends RecyclerView.ViewHolder {

        TextView viewmore_tv, viewcomments, viewshare, wowsome_tv, viewwowsome, eventcategory_tv;
        private TextView sendinvite_tv,timestamp, eventname_tv, name, desc, share, comments, eventtopic_tv, menu_tv, otherurl_tv, eventaddress_tv, month_tv, date_tv, time_tv;
        private ImageView profilePic;
        private ImageView feedImageView, wowtagvideo, highlight1_iv, highlight2_iv;
        private ImageView video1plus, video3plus;
        private FrameLayout frameLayout;
        private LinearLayout runttime_lv;


        public ProfessionalVH(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.hoster_name);
            eventname_tv = (TextView) itemView.findViewById(R.id.eventname_tv);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            desc = (TextView) itemView.findViewById(R.id.desc_tv);
            profilePic = (ImageView) itemView.findViewById(R.id.imageview_profile);
            feedImageView = (ImageView) itemView.findViewById(R.id.feedImage1);

            wowsome_tv = itemView.findViewById(R.id.wowsome_tv);
            eventcategory_tv = itemView.findViewById(R.id.eventcategory_tv);
            comments = itemView.findViewById(R.id.comments_tv);
            share = itemView.findViewById(R.id.share_tv);
            viewmore_tv = itemView.findViewById(R.id.viewmore_tv);
            viewwowsome = itemView.findViewById(R.id.viewwow_tv);
            viewcomments = itemView.findViewById(R.id.viewcomments_tv);
            viewshare = itemView.findViewById(R.id.viewshare_tv);
            menu_tv = itemView.findViewById(R.id.menu_tv);
            sendinvite_tv = itemView.findViewById(R.id.sendinvite_tv);
            month_tv = itemView.findViewById(R.id.month_tv);
            date_tv = itemView.findViewById(R.id.date_tv);
            time_tv = itemView.findViewById(R.id.time_tv);
            runttime_lv = itemView.findViewById(R.id.runtimelv);

            eventtopic_tv = itemView.findViewById(R.id.eventtopic_tv);
            otherurl_tv = itemView.findViewById(R.id.otherurl_tv);
            eventaddress_tv = itemView.findViewById(R.id.address_tv);
            frameLayout = itemView.findViewById(R.id.framevideo1);

            highlight1_iv = itemView.findViewById(R.id.highlight1);
            highlight2_iv = itemView.findViewById(R.id.highlight2);
            video1plus = itemView.findViewById(R.id.video1plus_iv);
            video3plus = itemView.findViewById(R.id.video3plus_iv);
            wowtagvideo = itemView.findViewById(R.id.video0_iv);


        }
    }


    protected class SocialVH extends RecyclerView.ViewHolder {

        TextView viewmore_tv, viewcomments, viewshare, wowsome_tv, viewwowsome, eventcategory_tv;
        private TextView sendinvite_tv,timestamp, eventname_tv, name, desc, share, comments, eventtopic_tv, menu_tv, otherurl_tv, eventaddress_tv, month_tv, date_tv, time_tv;
        private ImageView profilePic;
        private ImageView feedImageView, wowtagvideo, highlight1_iv, highlight2_iv;
        private ImageView video1plus, video3plus;
        private FrameLayout frameLayout;
        private LinearLayout runttime_lv;


        public SocialVH(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.hoster_name);
            eventname_tv = (TextView) itemView.findViewById(R.id.eventname_tv);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            desc = (TextView) itemView.findViewById(R.id.desc_tv);
            profilePic = (ImageView) itemView.findViewById(R.id.imageview_profile);
            feedImageView = (ImageView) itemView.findViewById(R.id.feedImage1);

            wowsome_tv = itemView.findViewById(R.id.wowsome_tv);
            eventcategory_tv = itemView.findViewById(R.id.eventcategory_tv);
            comments = itemView.findViewById(R.id.comments_tv);
            share = itemView.findViewById(R.id.share_tv);
            viewmore_tv = itemView.findViewById(R.id.viewmore_tv);
            viewwowsome = itemView.findViewById(R.id.viewwow_tv);
            viewcomments = itemView.findViewById(R.id.viewcomments_tv);
            viewshare = itemView.findViewById(R.id.viewshare_tv);
            menu_tv = itemView.findViewById(R.id.menu_tv);
            sendinvite_tv = itemView.findViewById(R.id.sendinvite_tv);
            month_tv = itemView.findViewById(R.id.month_tv);
            date_tv = itemView.findViewById(R.id.date_tv);
            time_tv = itemView.findViewById(R.id.time_tv);
            runttime_lv = itemView.findViewById(R.id.runtimelv);

            eventtopic_tv = itemView.findViewById(R.id.eventtopic_tv);
            otherurl_tv = itemView.findViewById(R.id.otherurl_tv);
            eventaddress_tv = itemView.findViewById(R.id.address_tv);
            frameLayout = itemView.findViewById(R.id.framevideo1);

            highlight1_iv = itemView.findViewById(R.id.highlight1);
            highlight2_iv = itemView.findViewById(R.id.highlight2);
            video1plus = itemView.findViewById(R.id.video1plus_iv);
            video3plus = itemView.findViewById(R.id.video3plus_iv);
            wowtagvideo = itemView.findViewById(R.id.video0_iv);


        }
    }


    protected class BusinessVH extends RecyclerView.ViewHolder {

        TextView viewmore_tv, viewcomments, viewshare, wowsome_tv, viewwowsome, eventcategory_tv;
        private TextView sendinvite_tv,timestamp, eventname_tv, name, desc, share, menu_tv, comments, eventtopic_tv, otherurl_tv, eventaddress_tv, month_tv, date_tv, time_tv;
        private ImageView profilePic;
        private ImageView feedImageView, wowtagvideo, highlight1_iv, highlight2_iv;
        private ImageView video1plus, video3plus;
        private FrameLayout frameLayout;
        private LinearLayout runttime_lv;


        public BusinessVH(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.hoster_name);
            eventname_tv = (TextView) itemView.findViewById(R.id.eventname_tv);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            desc = (TextView) itemView.findViewById(R.id.desc_tv);
            profilePic = (ImageView) itemView.findViewById(R.id.imageview_profile);
            feedImageView = (ImageView) itemView.findViewById(R.id.feedImage1);
            sendinvite_tv = itemView.findViewById(R.id.sendinvite_tv);
            wowsome_tv = itemView.findViewById(R.id.wowsome_tv);
            eventcategory_tv = itemView.findViewById(R.id.eventcategory_tv);
            comments = itemView.findViewById(R.id.comments_tv);
            share = itemView.findViewById(R.id.share_tv);
            viewmore_tv = itemView.findViewById(R.id.viewmore_tv);
            viewwowsome = itemView.findViewById(R.id.viewwow_tv);
            viewcomments = itemView.findViewById(R.id.viewcomments_tv);
            viewshare = itemView.findViewById(R.id.viewshare_tv);
            menu_tv = itemView.findViewById(R.id.menu_tv);

            month_tv = itemView.findViewById(R.id.month_tv);
            date_tv = itemView.findViewById(R.id.date_tv);
            time_tv = itemView.findViewById(R.id.time_tv);
            runttime_lv = itemView.findViewById(R.id.runtimelv);

            eventtopic_tv = itemView.findViewById(R.id.eventtopic_tv);
            otherurl_tv = itemView.findViewById(R.id.otherurl_tv);
            eventaddress_tv = itemView.findViewById(R.id.address_tv);
            frameLayout = itemView.findViewById(R.id.framevideo1);

            highlight1_iv = itemView.findViewById(R.id.highlight1);
            highlight2_iv = itemView.findViewById(R.id.highlight2);
            video1plus = itemView.findViewById(R.id.video1plus_iv);
            video3plus = itemView.findViewById(R.id.video3plus_iv);
            wowtagvideo = itemView.findViewById(R.id.video0_iv);


        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    private class addWowsome extends AsyncTask<String, Void, String> {
        String eventId;

        public addWowsome(String eventId) {
            this.eventId = eventId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("eventid", eventId);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/wowsome", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "RESSSSSSS-------->" + s.toString());

            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        JSONObject message = jo.getJSONObject("message");
                        String wowsomestr = message.getString("wowsome");
                        wowsomecount = jo.getString("wowsomes");
                        Log.e("tag", "wowsomestr-------->" + wowsomecount);
                    }

                } catch (JSONException e) {

                }

            } else {
            }

        }


    }

    private class postComments extends AsyncTask<String, Void, String> {
        String eventId, comments, name;

        public postComments(String comments, String name, String eventId) {
            this.name = name;
            this.eventId = eventId;
            this.comments = comments;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loaderdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("eventid", eventId);
                jsonObject.accumulate("comment", comments);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/postcomment", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "RECYCLERRRRRRRRRR-------->" + s.toString());

            //   loaderdialog.dismiss();
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    Log.e("tag", "FEEDITEMSSSSSSSS-------->");
                    if (status.equals("true")) {
                        //Doc doc = docs.get(position);
                        FeedItem item = new FeedItem();

                        //    Doc doc=new Doc();


                        item.setComment(comments_et.getText().toString());
                        item.setUserfname(name);
                        item.setUserimage(userimage);
                        feedItems.add(item);
                        i++;
                        Log.e("tag", "FEEDITEMSSSSSSSS-------->" + feedItems);

                        recyclerAdapter = new CommentsRecyclerAdapter(feedItems, context);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                        commentslistview.setLayoutManager(mLayoutManager);
                        commentslistview.setItemAnimator(new DefaultItemAnimator());
                        commentslistview.setHasFixedSize(true);
                        commentslistview.setAdapter(recyclerAdapter);
                        recyclerAdapter.notifyDataSetChanged();
                        comments_et.setText("");
                    }

                } catch (JSONException e) {

                }

            } else {
            }

        }


    }

    private class getAllComments extends AsyncTask<String, Void, String> {
        String eventId, comments;

        public getAllComments(String eventId) {
            this.eventId = eventId;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("eventid", eventId);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/getcomment", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "getAllComments-------->" + s.toString());

            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        JSONObject message = jo.getJSONObject("message");
                        JSONArray jsonArray = message.getJSONArray("comments");
                        feedItems.clear();
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                FeedItem item = new FeedItem();
                                JSONObject feedObj = (JSONObject) jsonArray.get(i);

                                String commentedAt = feedObj.getString("commentedAt");
                                String comments = feedObj.getString("comment");
                                item.setComment(comments);
                                JSONObject user = feedObj.getJSONObject("userid");
                                String fname = user.getString("firstname");
                                item.setUserfname(fname);
                                String lname = user.getString("lastname");
                                String wowtagid = user.getString("wowtagid");
                                String personalimage = user.getString("personalimage");
                                item.setUserimage(personalimage);
                                feedItems.add(item);
                            }


                            Log.e("tag", "list----------" + feedItems.size());
                            recyclerAdapter = new CommentsRecyclerAdapter(feedItems, context);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                            commentslistview.setLayoutManager(mLayoutManager);
                            commentslistview.setItemAnimator(new DefaultItemAnimator());
                            commentslistview.setHasFixedSize(true);
                            commentslistview.setAdapter(recyclerAdapter);

                        }


                    }

                } catch (JSONException e) {

                }

            } else {
            }

        }


    }

    private class DeleteThoughts extends AsyncTask<String, Void, String> {
        String thoughtid;
        int pos;

        public DeleteThoughts(String eventId, int pos) {
            this.thoughtid = eventId;
            this.pos = pos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("thoughtid", thoughtid);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/deletethought", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "RESSSSSSS-------->" + s.toString());

            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        String msg = jo.getString("message");
                        menu_dialog.dismiss();
                        removeThoughts(pos);

                    }
                } catch (JSONException e) {

                }

            } else {

            }

        }


    }

    private class DeleteEvents extends AsyncTask<String, Void, String> {
        String eventId;
        int pos;

        public DeleteEvents(String eventId, int pos) {
            this.eventId = eventId;
            this.pos = pos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("eventid", eventId);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/deleteevent", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "RESSSSSSS-------->" + s.toString());

            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        String msg = jo.getString("message");
                        menu_dialog.dismiss();
                        removeThoughts(pos);

                    }
                } catch (JSONException e) {

                }

            } else {

            }

        }


    }


}
