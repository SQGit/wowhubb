package com.wowhubb.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.wowhubb.Activity.EventInviteActivity;
import com.wowhubb.Activity.ViewMoreDetailspage;
import com.wowhubb.FeedsData.Doc;
import com.wowhubb.FeedsData.Eventvenue;
import com.wowhubb.FeedsData.Userid;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;
import com.wowhubb.Utils.HttpUtils;
import com.wowhubb.app.ImageLoader;
import com.wowhubb.data.FeedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * Created by Suleiman on 19/10/16.
 */

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int THOUGHT = 0;
    private static final int LOADING = 6;
    private static final int PRIVATE = 1;
    private static final int PROFESSIONAL = 2;
    private static final int BUSINESS = 3;
    private static final int SOCIAL = 4;
    private static final int QUICK = 5;
    public static MaterialBetterSpinner eventDay_spn;
    static Bitmap bitmap;
    public ImageLoader imageLoader1;
    public List<Doc> docs = new ArrayList<>();
    String str_eventday, str_frommonth, str_tomonth, str_fromdate, str_todate, str_fromtime, str_totime, str_timefrom, str_timeto;
    int from, to;
    String token, wowsomecount, username, userimage, thoughtid, eventId, userId;
    EditText comments_et;
    CommentsRecyclerAdapter recyclerAdapter;
    RecyclerView commentslistview;
    Dialog comments_dialog, menu_dialog;
    Userid userid;
    Activity activity;
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;
    String[] MEMBERLIST = {"1", "2", "3", "4", "5", "6", "7"};
    Dialog loader_dialog;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    String FromTimeExt;
    private ArrayList<FeedItem> feedList = new ArrayList<>();
    private Context context;
    private boolean isLoadingAdded = false;
    private ArrayList<FeedItem> feedItems = new ArrayList<>();
    private int i = 0;
    private List<Doc> filtercontactsList;

    public PaginationAdapter(Context context, List<Doc> docs) {
        this.context = context;
        this.docs = docs;
        //  filtercontactsList = new ArrayList<>();
        // filtercontactsList.addAll(docs);
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

            default:
                return new LoadHolder(inflater.inflate(R.layout.item_progress, parent, false));

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
        loader_dialog = new Dialog(context);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);
        Doc doc = docs.get(position);
        Log.e("tag", "pos" + position);
        Log.e("tag", "pos" + getItemCount());

        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }
        switch (getItemViewType(position)) {


            case THOUGHT:
                final ThoughtsVH thoughtsVH = (ThoughtsVH) holder;

                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    if (userid.getPersonalimage() != null) {
                        Glide.with(context).load("http://104.197.80.225:3010/wow/media/personal/" + userid.getPersonalimage()).into(thoughtsVH.profilePic);
                    }
                } else {
                    thoughtsVH.profilePic.setImageResource(R.drawable.profile_img);
                }

                //-------------------------EVENT USERNAME-----------------------------------------//
                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    if (userid.getFirstname() != null) {
                        thoughtsVH.name.setText(userid.getFirstname());
                    }

                }



                //-------------------------EVENT DESIGNATION------------------------------------//
                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    if (userid.getDesignation() != null) {
                        thoughtsVH.timestamp.setText(userid.getDesignation());
                    }
                }

                thoughtsVH.link_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(docs.get(position).getUrllink()));
                        context.startActivity(i);
                    }
                });


                //-------------------------COVER IMAGE---------------------------------------------//

                Log.e("tag", "---------THOUGHTS IMAGE---------->" + doc.getThoughtsimageurl());
                if (doc.getThoughtsimageurl() != null && !doc.getThoughtsimageurl().equals("null")) {
                    thoughtsVH.feedImageView.setVisibility(View.VISIBLE);
                    Glide
                            .with(context)
                            .load(doc.getThoughtsimageurl())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                            .centerCrop()
                            .crossFade()
                            .into(thoughtsVH.feedImageView);
                }


                if (doc.getThoughtsvideothumb() != null && !doc.getThoughtsvideothumb().equals("null")) {
                    thoughtsVH.frameLayout.setVisibility(View.VISIBLE);
                    Glide
                            .with(context)
                            .load(doc.getThoughtsvideothumb())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                            .centerCrop()
                            .crossFade()
                            .into(thoughtsVH.wowtagvideo);
                }

                thoughtsVH.frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Doc doc = docs.get(position);
                        final Dialog dialog = new Dialog(context, R.style.dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_videoview);
                        View v1 = dialog.getWindow().getDecorView().getRootView();
                        ImageView closeiv = dialog.findViewById(R.id.close_iv);
                        closeiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        VideoView videoView = dialog.findViewById(R.id.video_view);
                        if (doc.getWowtagvideourl() != null && doc.getWowtagvideourl() != null) {
                            dialog.show();
                            thoughtsVH.video3plus.setVisibility(View.VISIBLE);
                            videoView.setVideoURI(Uri.parse(doc.getThoughtsvideourl()));
                            videoView.start();
                        } else {
                            thoughtsVH.video3plus.setVisibility(View.INVISIBLE);
                        }
                        dialog.show();

                    }
                });
                //-------------------------EVENT NAME---------------------------------------------//
                if (doc.getEventname() != null) {
                    thoughtsVH.eventtopic_tv.setText(docs.get(position).getEventname());
                }


                //-------------------------EVENT CATAGORY-----------------------------------------//
                if (doc.getEventcategory() != null) {
                    thoughtsVH.eventcategory_tv.setText("- " + docs.get(position).getEventcategory());
                }

                //-------------------------DESCRIPTION---------------------------------------------//

                Log.e("tag", "111119999999999999999999----------------->" + doc.getThoughtstext());

                if (doc.getThoughtstext() != null) {
                    thoughtsVH.desc.setVisibility(View.VISIBLE);
                    thoughtsVH.desc.setText(docs.get(position).getThoughtstext());
                } else {
                    thoughtsVH.desc.setVisibility(View.GONE);
                }


                //-------------------------DESCRIPTION---------------------------------------------//

                Log.e("tag", "111119999999999999999999----------------->" + doc.getThoughtstext());

                if (doc.getUrllink() != null) {
                    thoughtsVH.url_lv.setVisibility(View.VISIBLE);
                    thoughtsVH.link_tv.setText(docs.get(position).getUrllink());
                } else {
                    thoughtsVH.url_lv.setVisibility(View.GONE);
                }


                //-------------------------EVENT ADDRESS-------------------------------------------//

                if (doc.getEventtimezone() != null) {
                    thoughtsVH.eventaddress_tv.setText(doc.getEventtimezone());
                }

                //-------------------------WOMSOMES COUNT------------------------------------------//

                if (doc.getWowsomecount() != null) {
                    thoughtsVH.viewwowsome.setVisibility(View.VISIBLE);
                    thoughtsVH.viewwowsome.setText(doc.getWowsomecount() + " Wowsomes");
                }

                //-------------------------COMMENTS COUNT------------------------------------------//
                if (doc.getCommentcount() != null) {
                    thoughtsVH.viewcomments.setText(doc.getCommentcount() + " Comments");
                    thoughtsVH.viewcomments.setVisibility(View.VISIBLE);
                }


                thoughtsVH.wowsome_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        String eventId = doc.getId();
                        Log.e("tag", "-----eventId---------->>>" + eventId);
                        new addThoughtsWowsome(eventId).execute();
                        thoughtsVH.viewwowsome.setVisibility(view.VISIBLE);
                        thoughtsVH.viewwowsome.setText(wowsomecount + " Wowsomes");
                    }
                });

                thoughtsVH.comments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        thoughtsVH.viewcomments.setVisibility(view.VISIBLE);
                        comments_dialog = new Dialog(context);
                        comments_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        comments_dialog.setContentView(R.layout.dialog_comments);
                        View v1 = comments_dialog.getWindow().getDecorView().getRootView();
                        FontsOverride.overrideFonts(context, v1);
                        ImageView close = comments_dialog.findViewById(R.id.closeiv);
                        comments_et = comments_dialog.findViewById(R.id.comments_et);
                        ImageView send_btn = comments_dialog.findViewById(R.id.btn_chat_send);
                        Doc doc = docs.get(position);
                        String eventId = doc.getId();
                        commentslistview = (RecyclerView) comments_dialog.findViewById(R.id.recyclerViewBeneficiary);

                        new getAllComments(eventId).execute();

                        send_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Doc doc = docs.get(position);
                                String eventId = doc.getId();
                                String str_comments = comments_et.getText().toString();
                                if (str_comments.length() > 0) {
                                    new postComments(str_comments, username, eventId).execute();
                                }
                            }
                        });

                        FontsOverride.overrideFonts(comments_dialog.getContext(), v1);

                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                comments_dialog.dismiss();
                            }
                        });

                        comments_dialog.show();
                    }
                });
                thoughtsVH.menu_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        menu_dialog = new BottomSheetDialog(context);
                        menu_dialog.setContentView(R.layout.dialog_menu_list);
                        menu_dialog.setCancelable(true);
                        View v1 = menu_dialog.getWindow().getDecorView().getRootView();
                        FontsOverride.overrideFonts(context, v1);
                        menu_dialog.show();
                        TextView deletetv = menu_dialog.findViewById(R.id.delete_tv);
                        final Doc doc = docs.get(position);

                        if (doc.getUserid() != null) {
                            userid = docs.get(position).getUserid();
                            Log.e("tag", "UID------>" + userId);
                            Log.e("tag", "userid------>" + userid.getId());

                            String Uid = userid.getId();
                            if (userId.equals(Uid)) {
                                deletetv.setVisibility(View.VISIBLE);
                            } else {
                                deletetv.setVisibility(View.GONE);
                            }
                        }


                        deletetv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                thoughtid = doc.getId();
                                new DeleteThoughts(thoughtid, position).execute();

                            }
                        });


                    }
                });
                thoughtsVH.frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Doc doc = docs.get(position);
                        final Dialog dialog = new Dialog(context, R.style.dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_videoview);
                        View v1 = dialog.getWindow().getDecorView().getRootView();
                        ImageView closeiv = dialog.findViewById(R.id.close_iv);
                        closeiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        VideoView videoView = dialog.findViewById(R.id.video_view);

                        if (doc.getThoughtsvideourl() != null && doc.getThoughtsvideourl() != null) {
                            dialog.show();
                            videoView.setVideoURI(Uri.parse(doc.getThoughtsvideourl()));
                            videoView.start();
                        } else {

                        }

                    }
                });


                //-------------------------FEEDIMAGE IMAGE CLICKING EVENT----------------------------------------//
                thoughtsVH.feedImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Doc doc = docs.get(position);
                        final Dialog dialog = new Dialog(context, R.style.dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_imageview);
                        View v1 = dialog.getWindow().getDecorView().getRootView();
                        ImageView closeiv = dialog.findViewById(R.id.close_iv);
                        closeiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        ImageView videoView = dialog.findViewById(R.id.video_view);
                        if (doc.getThoughtsimageurl() != null && !doc.getThoughtsimageurl().equals("null")) {
                            Glide.with(context).load(doc.getThoughtsimageurl())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                                    .centerCrop()
                                    .crossFade()
                                    .into(videoView);
                            dialog.show();
                        } else {

                        }


                    }
                });


                break;
            case QUICK:
                final QuickVH quickVH = (QuickVH) holder;

                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    if (userid.getPersonalimage() != null) {
                        Glide.with(context).load("http://104.197.80.225:3010/wow/media/personal/" + userid.getPersonalimage()).into(quickVH.profilePic);
                    }
                } else {
                    quickVH.profilePic.setImageResource(R.drawable.profile_img);
                }


                List<Eventvenue> eventvenues1 = docs.get(position).getEventvenue();

                Log.e("tag", "eventvenue---" + eventvenues1);

                if (eventvenues1 != null) {
                    if (eventvenues1.size() > 0) {
                        for (int i = 0; i < eventvenues1.size(); i++) {
                            quickVH.address_tv.setText(eventvenues1.get(0).getEventvenuename() + " , " + eventvenues1.get(0).getEventvenueaddress1() + ", " + eventvenues1.get(0).getEventvenuecity() + ", " +
                                    eventvenues1.get(0).getEventvenuezipcode());
                        }
                    }


                } else {
                    quickVH.address_tv.setText(docs.get(position).getEventtimezone());
                }


                //-------------------------EVENT USERNAME-----------------------------------------//
                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    if (userid.getFirstname() != null) {
                        quickVH.name.setText(userid.getFirstname());
                    }
                }


                //-------------------------EVENT NAME---------------------------------------------//
                if (doc.getEventtitle() != null) {
                    quickVH.eventname_tv.setText("! " + docs.get(position).getEventtitle());
                }
                //-------------------------EVENT NAME---------------------------------------------//
                if (doc.getEventname() != null) {
                    quickVH.eventtopic_tv.setText(docs.get(position).getEventname());
                }

                //-------------------------EVENT CATAGORY-----------------------------------------//
                if (doc.getEventcategory() != null) {
                    quickVH.eventcategory_tv.setText("- " + docs.get(position).getEventcategory());
                }


                //-------------------------EVENT DESIGNATION------------------------------------//
                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    if (userid.getDesignation() != null) {
                        quickVH.timestamp.setText(userid.getDesignation());
                    }
                }

                Log.e("tag", "12333" + doc.getWowtagvideothumb());

                if (doc.getWowtagvideothumb() != null && !doc.getWowtagvideothumb().equals("null")) {
                    Glide
                            .with(context)
                            .load(doc.getWowtagvideothumb())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                            .centerCrop()
                            .crossFade()
                            .into(quickVH.wowtagvideo);
                }

                if (doc.getWowtagvideourl() != null && !doc.getWowtagvideourl().equals("null")) {
                    quickVH.frameLayout.setVisibility(View.VISIBLE);
                }
                quickVH.frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Doc doc = docs.get(position);
                        final Dialog dialog = new Dialog(context, R.style.dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_videoview);
                        View v1 = dialog.getWindow().getDecorView().getRootView();
                        ImageView closeiv = dialog.findViewById(R.id.close_iv);
                        closeiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        VideoView videoView = dialog.findViewById(R.id.video_view);
                        if (doc.getWowtagvideourl() != null && doc.getWowtagvideourl() != null) {
                            dialog.show();
                            PrivateVH.video3plus.setVisibility(View.VISIBLE);
                            videoView.setVideoURI(Uri.parse(doc.getWowtagvideourl()));
                            videoView.start();
                        } else {
                            PrivateVH.video3plus.setVisibility(View.INVISIBLE);
                        }
                        dialog.show();

                    }
                });

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

                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    Log.e("tag", "UID------>" + userId);
                    Log.e("tag", "userid------>" + userid.getId());

                    String Uid = userid.getId();
                    if (userId.equals(Uid)) {
                        quickVH.sendinvite_tv.setVisibility(View.VISIBLE);
                    } else {
                        quickVH.sendinvite_tv.setVisibility(View.GONE);
                    }
                }
                quickVH.sendinvite_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        Intent intent = new Intent(context, EventInviteActivity.class);
                        Log.e("tag", "11111111111" + doc.getId());
                        Bundle bundle = new Bundle();
                        bundle.putString("eventId", doc.getId());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(context);
                        edit = sharedPrefces.edit();
                        edit.putString("eventId", doc.getId());
                        edit.putString("feedstatus", "allevents");
                        edit.commit();
                    }
                });


                quickVH.wowsome_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        String eventId = doc.getId();
                        Log.e("tag", "-----eventId---------->>>" + eventId);
                        new addWowsome(eventId).execute();
                        quickVH.viewwowsome.setVisibility(view.VISIBLE);
                        quickVH.viewwowsome.setText(wowsomecount + " Wowsomes");
                    }
                });

                quickVH.comments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        quickVH.viewcomments.setVisibility(view.VISIBLE);
                        comments_dialog = new Dialog(context);
                        comments_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        comments_dialog.setContentView(R.layout.dialog_comments);
                        View v1 = comments_dialog.getWindow().getDecorView().getRootView();
                        FontsOverride.overrideFonts(context, v1);
                        ImageView close = comments_dialog.findViewById(R.id.closeiv);
                        comments_et = comments_dialog.findViewById(R.id.comments_et);
                        ImageView send_btn = comments_dialog.findViewById(R.id.btn_chat_send);
                        Doc doc = docs.get(position);
                        String eventId = doc.getId();
                        commentslistview = (RecyclerView) comments_dialog.findViewById(R.id.recyclerViewBeneficiary);
                        new getAllComments(eventId).execute();
                        send_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Doc doc = docs.get(position);
                                String eventId = doc.getId();
                                String str_comments = comments_et.getText().toString();
                                if (str_comments.length() > 0) {
                                    new postComments(str_comments, username, eventId).execute();
                                }
                            }
                        });

                        FontsOverride.overrideFonts(comments_dialog.getContext(), v1);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                comments_dialog.dismiss();
                            }
                        });

                        comments_dialog.show();
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
                        final Doc doc = docs.get(position);
                        if (doc.getUserid() != null) {
                            userid = docs.get(position).getUserid();
                            Log.e("tag", "UID------>" + userId);
                            Log.e("tag", "userid------>" + userid.getId());

                            String Uid = userid.getId();
                            if (userId.equals(Uid)) {
                                deletetv.setVisibility(View.VISIBLE);
                            } else {
                                deletetv.setVisibility(View.GONE);
                            }
                        }
                        deletetv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                eventId = doc.getId();
                                new DeleteEvents(eventId, position).execute();

                            }
                        });


                    }
                });
                quickVH.frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        final Dialog dialog = new Dialog(context, R.style.dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_videoview);
                        View v1 = dialog.getWindow().getDecorView().getRootView();
                        ImageView closeiv = dialog.findViewById(R.id.close_iv);
                        closeiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        VideoView videoView = dialog.findViewById(R.id.video_view);
                        if (doc.getWowtagvideourl() != null && doc.getWowtagvideourl() != null) {
                            dialog.show();
                            quickVH.video3plus.setVisibility(View.VISIBLE);
                            videoView.setVideoURI(Uri.parse(doc.getWowtagvideourl()));
                            videoView.start();
                        } else {
                            quickVH.video3plus.setVisibility(View.INVISIBLE);
                        }


                    }
                });
                if ((!doc.getEventstartdate().equals("null")) && (!doc.getEventenddate().equals("null"))) {

                    String fromTime = doc.getEventstartdate();
                    String toTime = doc.getEventenddate();
                    Log.e("tag", "111111Dateeeee------->" + fromTime + toTime);
                    try {
                        String[] separated = fromTime.split(" ");


                        String runFromDate = separated[0];
                        String runFromTime = separated[1];
                        FromTimeExt = separated[2];
                        //  String strDateFormatTo = fromTime;
                        SimpleDateFormat spf = new SimpleDateFormat("yyyy/MM/dd");
                        Date newDate = null;
                        try {
                            newDate = spf.parse(runFromDate);
                            spf = new SimpleDateFormat("MMM/dd/yyyy EEE");
                            runFromDate = spf.format(newDate);
                            System.out.println(runFromDate);

                            String str_fromdate1[] = runFromDate.split("/");
                            str_frommonth = str_fromdate1[0];
                            str_fromdate = str_fromdate1[1];
                            String str_fromtime[] = runFromDate.split(" ");
                            str_timefrom = str_fromtime[1];
                            Log.e("tag", "date2222-------->" + str_timefrom + str_frommonth + str_fromdate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mm");
                        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");
                        String strDateFormat = runFromTime;
                        Log.e("tag", "date-------->" + runFromTime);
                        try {
                            Date dt = parseFormat.parse(strDateFormat);
                            System.out.println(outputFormat.format(dt));
                            str_fromtime = (outputFormat.format(dt));
                            Log.e("tag", "todate-------->" + outputFormat.format(dt));
                        } catch (ParseException exc) {
                            exc.printStackTrace();
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }

                    try {
                        String[] separated1 = toTime.split(" ");
                        String runFromDate1 = separated1[0];
                        String runFromTime1 = separated1[1];
                        SimpleDateFormat spf1 = new SimpleDateFormat("yyyy/MM/dd");
                        Date newDate1 = null;
                        try {
                            newDate1 = spf1.parse(runFromDate1);
                            spf1 = new SimpleDateFormat("MMM/dd/yyyy EEE");
                            runFromDate1 = spf1.format(newDate1);
                            Log.e("tag", "date-------->" + runFromDate1);
                            String str_fromdate12[] = runFromDate1.split("/");
                            str_tomonth = str_fromdate12[0];
                            str_todate = str_fromdate12[1];
                            String str_totime[] = runFromDate1.split(" ");
                            str_timeto = str_totime[1];
                            Log.e("tag", "date2222-------->" + str_timeto);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat outputFormat1 = new SimpleDateFormat("KK:mma");
                        SimpleDateFormat parseFormat1 = new SimpleDateFormat("hh:mm");
                        String strDateFormat1 = runFromTime1 + "a";
                        Log.e("tag", "date-------->" + runFromTime1);

                        try {
                            Date dt = parseFormat1.parse(strDateFormat1);
                            System.out.println(outputFormat1.format(dt));
                            str_totime = (outputFormat1.format(dt));
                            Log.e("tag", "todate-------->" + outputFormat1.format(dt));
                        } catch (ParseException exc) {
                            exc.printStackTrace();
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                    try {
                        if (str_frommonth.equals(str_tomonth)) {
                            quickVH.month_tv.setText(str_frommonth);
                        } else {
                            quickVH.month_tv.setText(str_frommonth + " to " + str_tomonth);
                        }
                    } catch (NullPointerException e) {

                    }

                    quickVH.time_tv.setText(str_fromtime +FromTimeExt+ "-" + str_totime);
                    try {
                        from = Integer.parseInt(str_fromdate);
                        to = Integer.parseInt(str_todate);
                    } catch (NumberFormatException e) {

                    }

                    if (from == 01) {
                        str_fromdate = str_fromdate + "st";
                    } else if (from == 02) {
                        str_fromdate = str_fromdate + "nd";
                    } else if (from == 03) {
                        str_fromdate = str_fromdate + "rd";
                    } else {
                        str_fromdate = str_fromdate + "th";
                    }
                    if (to == 01)
                    {
                        str_todate = str_todate + "st";
                        quickVH.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
                    }
                    else if (to == 02) {
                        str_todate = str_todate + "nd";
                        quickVH.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
                    }
                    else if (to == 03) {
                        str_todate = str_todate + "rd";
                        quickVH.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
                    }
                    else {
                        str_todate = str_todate + "th";
                        quickVH.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
                    }
                } else {
                    quickVH.runttime_lv.setVisibility(View.GONE);
                }

                break;

            case PRIVATE:

                final PrivateVH privateVH = (PrivateVH) holder;

                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    if (userid.getPersonalimage() != null) {
                        Glide.with(context).load("http://104.197.80.225:3010/wow/media/personal/" + userid.getPersonalimage()).into(privateVH.profilePic);
                    }
                } else {
                    privateVH.profilePic.setImageResource(R.drawable.profile_img);
                }

                //-------------------------EVENT USERNAME-----------------------------------------//
                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    if (userid.getFirstname() != null) {
                        privateVH.name.setText(userid.getFirstname());
                    }

                }
                Log.e("tag", "12333" + doc.getWowtagvideothumb());
                if (doc.getWowtagvideothumb() != null && !doc.getWowtagvideothumb().equals("null")) {
                    //    privateVH.wowtagvideo.setVisibility(View.VISIBLE);
                    Glide
                            .with(context)
                            .load(doc.getWowtagvideothumb())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                            .centerCrop()
                            .crossFade()
                            .into(privateVH.wowtagvideo);
                }


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


                //-------------------------EVENT DESIGNATION------------------------------------//
                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    if (userid.getDesignation() != null) {
                        privateVH.timestamp.setText(userid.getDesignation());
                    }
                }


                //-------------------------COVER IMAGE---------------------------------------------//

                Glide
                        .with(context)
                        .load(doc.getCoverpageurl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(privateVH.feedImageView);


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

                final CustomAdapter eventdayAdapter = new CustomAdapter(context, android.R.layout.simple_dropdown_item_1line, MEMBERLIST) {
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
                        //tv.setTypeface(lato);
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
                        // tv.setTypeface(lato);
                        if (position == 0) {
                            tv.setTextColor(Color.BLACK);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                privateVH.rsvptv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final Dialog rsvp_dialog = new Dialog(context);
                        rsvp_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        rsvp_dialog.setContentView(R.layout.dilaog_rsvpinvite);
                        View v1 = rsvp_dialog.getWindow().getDecorView().getRootView();
                        FontsOverride.overrideFonts(context, v1);
                        ImageView close = rsvp_dialog.findViewById(R.id.closeiv);
                        TextView rsvpinvite = (TextView) rsvp_dialog.findViewById(R.id.registertv);


                        eventDay_spn = (MaterialBetterSpinner) rsvp_dialog.findViewById(R.id.eventday_spn);
                        eventDay_spn.setAdapter(eventdayAdapter);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                rsvp_dialog.dismiss();
                            }
                        });
                        rsvpinvite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (str_eventday != null) {
                                    if (str_eventday.length() > 0) {
                                        //  Toast.makeText(context, "Succesfully Register", Toast.LENGTH_LONG).show();
                                        final Doc doc = docs.get(position);
                                        eventId = doc.getId();
                                        new postRSVP(eventId).execute();
                                        rsvp_dialog.dismiss();
                                    }
                                } else {
                                    Toast.makeText(context, "Please select the no of persons", Toast.LENGTH_LONG).show();

                                }
                            }
                        });

                        eventDay_spn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                str_eventday = adapterView.getItemAtPosition(i).toString();
                                Log.e("tag", "str_category------>" + str_eventday);

                            }
                        });

                        rsvp_dialog.show();
                    }
                });

                privateVH.menu_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        menu_dialog = new BottomSheetDialog(context);
                        menu_dialog.setContentView(R.layout.dialog_menu_list);
                        menu_dialog.setCancelable(true);
                        View v1 = menu_dialog.getWindow().getDecorView().getRootView();
                        FontsOverride.overrideFonts(context, v1);
                        menu_dialog.show();
                        TextView deletetv = menu_dialog.findViewById(R.id.delete_tv);

                        final Doc doc = docs.get(position);
                        if (doc.getUserid() != null) {
                            userid = docs.get(position).getUserid();
                            Log.e("tag", "UID------>" + userId);
                            Log.e("tag", "userid------>" + userid.getId());

                            String Uid = userid.getId();
                            if (userId.equals(Uid)) {
                                deletetv.setVisibility(View.VISIBLE);
                            } else {
                                deletetv.setVisibility(View.GONE);
                            }
                        }
                        deletetv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                eventId = doc.getId();
                                new DeleteEvents(eventId, position).execute();

                            }
                        });

                    }
                });
                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    Log.e("tag", "UID------>" + userId);
                    Log.e("tag", "userid------>" + userid.getId());

                    String Uid = userid.getId();
                    if (userId.equals(Uid)) {
                        privateVH.sendinvite_tv.setVisibility(View.VISIBLE);
                        privateVH.rsvptv.setVisibility(View.GONE);
                    } else {
                        privateVH.sendinvite_tv.setVisibility(View.GONE);
                        privateVH.rsvptv.setVisibility(View.VISIBLE);

                           /* if(doc.getRegisterrsvp().equals("on"))
                            {
                                privateVH.sendinvite_tv.setVisibility(View.GONE);
                                privateVH.rsvptv.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                privateVH.sendinvite_tv.setVisibility(View.GONE);
                                privateVH.rsvptv.setVisibility(View.GONE);
                            }*/

                    }
                }
                privateVH.sendinvite_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        Intent intent = new Intent(context, EventInviteActivity.class);
                        Log.e("tag", "11111111111" + doc.getId());
                        Bundle bundle = new Bundle();
                        bundle.putString("eventId", doc.getId());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(context);
                        edit = sharedPrefces.edit();
                        edit.putString("eventId", doc.getId());
                        edit.putString("feedstatus", "allevents");
                        edit.commit();
                    }
                });


                privateVH.wowsome_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        String eventId = doc.getId();
                        Log.e("tag", "-----eventId---------->>>" + eventId);
                        new addWowsome(eventId).execute();
                        privateVH.viewwowsome.setVisibility(view.VISIBLE);
                        privateVH.viewwowsome.setText(wowsomecount + " Wowsomes");
                        notifyDataSetChanged();
                    }
                });

                privateVH.comments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        privateVH.viewcomments.setVisibility(view.VISIBLE);
                        comments_dialog = new Dialog(context);
                        comments_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        comments_dialog.setContentView(R.layout.dialog_comments);
                        View v1 = comments_dialog.getWindow().getDecorView().getRootView();
                        FontsOverride.overrideFonts(context, v1);
                        ImageView close = comments_dialog.findViewById(R.id.closeiv);
                        comments_et = comments_dialog.findViewById(R.id.comments_et);
                        ImageView send_btn = comments_dialog.findViewById(R.id.btn_chat_send);
                        Doc doc = docs.get(position);
                        String eventId = doc.getId();
                        commentslistview = (RecyclerView) comments_dialog.findViewById(R.id.recyclerViewBeneficiary);
                        new getAllComments(eventId).execute();

                        send_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Doc doc = docs.get(position);
                                String eventId = doc.getId();
                                String str_comments = comments_et.getText().toString();
                                //String name = doc.get;
                                if (str_comments.length() > 0) {
                                    new postComments(str_comments, username, eventId).execute();
                                }

                            }
                        });


                        FontsOverride.overrideFonts(comments_dialog.getContext(), v1);


                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                comments_dialog.dismiss();
                            }
                        });
                        comments_dialog.show();
                        notifyDataSetChanged();
                    }
                });

                privateVH.frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Doc doc = docs.get(position);
                        final Dialog dialog = new Dialog(context, R.style.dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_videoview);
                        View v1 = dialog.getWindow().getDecorView().getRootView();
                        ImageView closeiv = dialog.findViewById(R.id.close_iv);
                        closeiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        VideoView videoView = dialog.findViewById(R.id.video_view);
                        if (doc.getWowtagvideourl() != null && doc.getWowtagvideourl() != null) {
                            dialog.show();
                            PrivateVH.video3plus.setVisibility(View.VISIBLE);
                            videoView.setVideoURI(Uri.parse(doc.getWowtagvideourl()));
                            videoView.start();
                        } else {
                            PrivateVH.video3plus.setVisibility(View.INVISIBLE);
                        }


                    }
                });

                //-------------------------HIGHLIGHT VIDEO CLICKING EVENT----------------------------------------//

                privateVH.highlight2_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        final Dialog dialog = new Dialog(context, R.style.dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_videoview);
                        View v1 = dialog.getWindow().getDecorView().getRootView();
                        ImageView closeiv = dialog.findViewById(R.id.close_iv);
                        closeiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        VideoView videoView = dialog.findViewById(R.id.video_view);
                        if (doc.getEventhighlights2url() != null && doc.getEventhighlights2url() != null) {
                            dialog.show();
                            PrivateVH.video3plus.setVisibility(View.VISIBLE);
                            videoView.setVideoURI(Uri.parse(doc.getEventhighlights2url()));
                            videoView.start();
                        } else {
                            PrivateVH.video3plus.setVisibility(View.INVISIBLE);
                        }
                    }
                });

                //-------------------------HIGHLIGHT IMAGE CLICKING EVENT----------------------------------------//

                privateVH.highlight1_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        final Dialog dialog = new Dialog(context, R.style.dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_imageview);
                        View v1 = dialog.getWindow().getDecorView().getRootView();
                        ImageView closeiv = dialog.findViewById(R.id.close_iv);
                        closeiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        ImageView videoView = dialog.findViewById(R.id.video_view);

                        if (doc.getEventhighlights1url() != null && !doc.getEventhighlights1url().equals("null")) {
                            Glide.with(context).load(doc.getEventhighlights1url())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                                    .centerCrop()
                                    .crossFade()
                                    .into(videoView);
                            //  imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/event/" + doc.getEventhighlights1(), videoView);
                            dialog.show();
                        } else {

                        }
                    }
                });
                //-------------------------FEEDIMAGE IMAGE CLICKING EVENT----------------------------------------//
                privateVH.feedImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Doc doc = docs.get(position);
                        final Dialog dialog = new Dialog(context, R.style.dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_imageview);
                        View v1 = dialog.getWindow().getDecorView().getRootView();
                        ImageView closeiv = dialog.findViewById(R.id.close_iv);
                        closeiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        ImageView videoView = dialog.findViewById(R.id.video_view);
                        if (doc.getCoverpageurl() != null && !doc.getCoverpageurl().equals("null")) {
                            Glide.with(context).load(doc.getCoverpageurl())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                                    .centerCrop()
                                    .crossFade()
                                    .into(videoView);
                            dialog.show();
                        } else {

                        }


                    }
                });

                privateVH.viewmore_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        List<Eventvenue> eventvenues = docs.get(position).getEventvenue();
                        Userid userid = docs.get(position).getUserid();
                        Log.e("tag", "eventvenue---" + eventvenues);
                        Intent intent = new Intent(context, ViewMoreDetailspage.class);

                        Bundle bundle = new Bundle();
                        if (doc.getEventvenue() != null) {
                            if (eventvenues.size() > 0) {
                                for (int i = 0; i < eventvenues.size(); i++) {
                                    String venueaddress = eventvenues.get(0).getEventvenuename() + " , " + eventvenues.get(0).getEventvenueaddress1() + ", " + eventvenues.get(0).getEventvenuecity() + ", " +
                                            eventvenues.get(0).getEventvenuezipcode();
                                    bundle.putString("eventvenueaddress", venueaddress);
                                }
                            }
                        }

                        if (doc.getWowtagvideourl() != null) {
                            bundle.putString("wowtagvideo", doc.getWowtagvideourl());
                        }

                        if (doc.getEventhighlights1() != null) {
                            bundle.putString("highlight1", doc.getEventhighlights1());
                        }

                        if (doc.getEventhighlightsvideo1() != null) {
                            bundle.putString("highlight2", doc.getEventhighlightsvideo1());
                        }

                        if (doc.getCoverpageurl() != null) {
                            bundle.putString("coverpage", doc.getCoverpageurl());
                        }

                        if (doc.getEventname() != null) {
                            bundle.putString("eventname", doc.getEventname());
                        }

                        if (doc.getEventdescription() != null) {
                            bundle.putString("description", doc.getEventdescription());
                        }
                        if (doc.getGiftregistryurl() != null) {

                            bundle.putString("gifturl", doc.getGiftregistryurl());
                        }
                        if (doc.getDonationsurl() != null) {
                            bundle.putString("donationurl", doc.getDonationsurl());

                        }
                        if (doc.getEventstartdate() != null) {
                            bundle.putString("eventstartdate", doc.getEventstartdate());
                        }
                        if (doc.getEventenddate() != null) {
                            bundle.putString("eventenddate", doc.getEventenddate());
                        }

                        bundle.putInt("eventdayscount", doc.getEventdayscount());
                        if (doc.getEventtype() != null) {
                            bundle.putString("eventtype", doc.getEventtype());
                        }

                        if (doc.getEventspeakername1() != null) {
                            bundle.putString("eventspeakername1", doc.getEventspeakername1());
                        }
                        if (doc.getEventspeakername2() != null) {
                            bundle.putString("eventspeakername2", doc.getEventspeakername1());
                        }
                        if (doc.getEventguesttype1() != null) {
                            bundle.putString("eventguesttype1", doc.getEventguesttype1());
                        }
                        if (doc.getEventguesttype2() != null) {
                            bundle.putString("eventguesttype2", doc.getEventguesttype2());
                        }
                        if (doc.getEventspeakeractivities1() != null) {
                            bundle.putString("eventspeakeractivities1", doc.getEventspeakeractivities1());
                        }
                        if (doc.getEventspeakeractivities2() != null) {
                            bundle.putString("eventspeakeractivities2", doc.getEventspeakeractivities2());
                        }
                        if (doc.getEventspeakerlink1() != null) {
                            bundle.putString("eventspeakerlink1", doc.getEventspeakerlink1());
                        }
                        if (doc.getEventspeakerlink2() != null) {
                            bundle.putString("eventspeakerlink2", doc.getEventspeakerlink2());
                        }


                        if (doc.getProgramschedule() != null) {
                            intent.putParcelableArrayListExtra("program", (ArrayList<? extends Parcelable>) docs.get(position).getProgramschedule());
                        }
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

                    }
                });


                if ((!doc.getEventstartdate().equals("null")) && (!doc.getEventenddate().equals("null"))) {

                    String fromTime = doc.getEventstartdate();
                    String toTime = doc.getEventenddate();
                    Log.e("tag", "111111Dateeeee------->" + fromTime + toTime);
                    try {
                        String[] separated = fromTime.split(" ");
                        String runFromDate = separated[0];
                        String runFromTime = separated[1];
                        //  String strDateFormatTo = fromTime;
                        SimpleDateFormat spf = new SimpleDateFormat("yyyy/MM/dd");
                        Date newDate = null;
                        try {
                            newDate = spf.parse(runFromDate);
                            spf = new SimpleDateFormat("MMM/dd/yyyy EEE");
                            runFromDate = spf.format(newDate);
                            System.out.println(runFromDate);

                            String str_fromdate1[] = runFromDate.split("/");
                            str_frommonth = str_fromdate1[0];
                            str_fromdate = str_fromdate1[1];
                            String str_fromtime[] = runFromDate.split(" ");
                            str_timefrom = str_fromtime[1];
                            Log.e("tag", "date2222-------->" + str_timefrom + str_frommonth + str_fromdate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mma");
                        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");
                        String strDateFormat = runFromTime + "a";
                        Log.e("tag", "date-------->" + runFromTime);
                        try {
                            Date dt = parseFormat.parse(strDateFormat);
                            System.out.println(outputFormat.format(dt));
                            str_fromtime = (outputFormat.format(dt));
                            Log.e("tag", "todate-------->" + outputFormat.format(dt));
                        } catch (ParseException exc) {
                            exc.printStackTrace();
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }

                    try {
                        String[] separated1 = toTime.split(" ");
                        String runFromDate1 = separated1[0];
                        String runFromTime1 = separated1[1];
                        SimpleDateFormat spf1 = new SimpleDateFormat("yyyy/MM/dd");
                        Date newDate1 = null;
                        try {
                            newDate1 = spf1.parse(runFromDate1);
                            spf1 = new SimpleDateFormat("MMM/dd/yyyy EEE");
                            runFromDate1 = spf1.format(newDate1);
                            Log.e("tag", "date-------->" + runFromDate1);
                            String str_fromdate12[] = runFromDate1.split("/");
                            str_tomonth = str_fromdate12[0];
                            str_todate = str_fromdate12[1];
                            String str_totime[] = runFromDate1.split(" ");
                            str_timeto = str_totime[1];
                            Log.e("tag", "date2222-------->" + str_timeto);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat outputFormat1 = new SimpleDateFormat("KK:mma");
                        SimpleDateFormat parseFormat1 = new SimpleDateFormat("hh:mm");
                        String strDateFormat1 = runFromTime1 + "a";
                        Log.e("tag", "date-------->" + runFromTime1);

                        try {
                            Date dt = parseFormat1.parse(strDateFormat1);
                            System.out.println(outputFormat1.format(dt));
                            str_totime = (outputFormat1.format(dt));
                            Log.e("tag", "todate-------->" + outputFormat1.format(dt));
                        } catch (ParseException exc) {
                            exc.printStackTrace();
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                    try {
                        if (str_frommonth.equals(str_tomonth)) {
                            privateVH.month_tv.setText(str_frommonth);
                        } else {
                            privateVH.month_tv.setText(str_frommonth + " to " + str_tomonth);
                        }
                    } catch (NullPointerException e) {

                    }

                    // date_tv.setText(str_fromdate + "-" + str_todate);
                    privateVH.time_tv.setText(str_fromtime + "-" + str_totime);
                    try {
                        from = Integer.parseInt(str_fromdate);
                        to = Integer.parseInt(str_todate);
                    } catch (NumberFormatException e) {

                    }

                    if (from == 01) {
                        str_fromdate = str_fromdate + "st";
                    } else if (from == 02) {
                        str_fromdate = str_fromdate + "nd";
                    } else if (from == 03) {
                        str_fromdate = str_fromdate + "rd";
                    } else {
                        str_fromdate = str_fromdate + "th";
                    }
                    if (to == 01) {
                        str_todate = str_todate + "st";
                        //date_tv.setText("");
                        privateVH.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
                    } else if (to == 02) {
                        str_todate = str_todate + "nd";
                        //  date_tv.setText("");
                        privateVH.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
                    } else if (to == 03) {
                        str_todate = str_todate + "rd";
                        //date_tv.setText("");
                        privateVH.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
                    } else {
                        str_todate = str_todate + "th";
                        //date_tv.setText("");
                        privateVH.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
                    }
                } else {
                    privateVH.runttime_lv.setVisibility(View.GONE);
                }

                if ((!doc.getEventstartdate().equals("null")) && (!doc.getEventenddate().equals("null"))) {

                    String fromTime = doc.getEventstartdate();
                    String toTime = doc.getEventenddate();
                    Log.e("tag", "111111Dateeeee------->" + fromTime + toTime);
                    try {
                        String[] separated = fromTime.split(" ");
                        String runFromDate = separated[0];
                        String runFromTime = separated[1];
                        //  String strDateFormatTo = fromTime;
                        SimpleDateFormat spf = new SimpleDateFormat("yyyy/MM/dd");
                        Date newDate = null;
                        try {
                            newDate = spf.parse(runFromDate);
                            spf = new SimpleDateFormat("MMM/dd/yyyy EEE");
                            runFromDate = spf.format(newDate);
                            System.out.println(runFromDate);

                            String str_fromdate1[] = runFromDate.split("/");
                            str_frommonth = str_fromdate1[0];
                            str_fromdate = str_fromdate1[1];
                            String str_fromtime[] = runFromDate.split(" ");
                            str_timefrom = str_fromtime[1];
                            Log.e("tag", "date2222-------->" + str_timefrom + str_frommonth + str_fromdate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mma");
                        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");
                        String strDateFormat = runFromTime + "a";
                        Log.e("tag", "date-------->" + runFromTime);
                        try {
                            Date dt = parseFormat.parse(strDateFormat);
                            System.out.println(outputFormat.format(dt));
                            str_fromtime = (outputFormat.format(dt));
                            Log.e("tag", "todate-------->" + outputFormat.format(dt));
                        } catch (ParseException exc) {
                            exc.printStackTrace();
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }

                    try {
                        String[] separated1 = toTime.split(" ");
                        String runFromDate1 = separated1[0];
                        String runFromTime1 = separated1[1];
                        SimpleDateFormat spf1 = new SimpleDateFormat("yyyy/MM/dd");
                        Date newDate1 = null;
                        try {
                            newDate1 = spf1.parse(runFromDate1);
                            spf1 = new SimpleDateFormat("MMM/dd/yyyy EEE");
                            runFromDate1 = spf1.format(newDate1);
                            Log.e("tag", "date-------->" + runFromDate1);
                            String str_fromdate12[] = runFromDate1.split("/");
                            str_tomonth = str_fromdate12[0];
                            str_todate = str_fromdate12[1];
                            String str_totime[] = runFromDate1.split(" ");
                            str_timeto = str_totime[1];
                            Log.e("tag", "date2222-------->" + str_timeto);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat outputFormat1 = new SimpleDateFormat("KK:mma");
                        SimpleDateFormat parseFormat1 = new SimpleDateFormat("hh:mm");
                        String strDateFormat1 = runFromTime1 + "a";
                        Log.e("tag", "date-------->" + runFromTime1);

                        try {
                            Date dt = parseFormat1.parse(strDateFormat1);
                            System.out.println(outputFormat1.format(dt));
                            str_totime = (outputFormat1.format(dt));
                            Log.e("tag", "todate-------->" + outputFormat1.format(dt));
                        } catch (ParseException exc) {
                            exc.printStackTrace();
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                    try {
                        if (str_frommonth.equals(str_tomonth)) {
                            privateVH.month_tv.setText(str_frommonth);
                        } else {
                            privateVH.month_tv.setText(str_frommonth + " to " + str_tomonth);
                        }
                    } catch (NullPointerException e) {

                    }

                    privateVH.time_tv.setText(str_fromtime + "-" + str_totime);

                    try {
                        from = Integer.parseInt(str_fromdate);
                        to = Integer.parseInt(str_todate);
                    } catch (NumberFormatException e) {

                    }

                    if (from == 01) {
                        str_fromdate = str_fromdate + "st";
                    } else if (from == 02) {
                        str_fromdate = str_fromdate + "nd";
                    } else if (from == 03) {
                        str_fromdate = str_fromdate + "rd";
                    } else {
                        str_fromdate = str_fromdate + "th";
                    }
                    if (to == 01) {
                        str_todate = str_todate + "st";
                        //date_tv.setText("");
                        privateVH.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
                    } else if (to == 02) {
                        str_todate = str_todate + "nd";
                        //  date_tv.setText("");
                        privateVH.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
                    } else if (to == 03) {
                        str_todate = str_todate + "rd";
                        //date_tv.setText("");
                        privateVH.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
                    } else {
                        str_todate = str_todate + "th";
                        //date_tv.setText("");
                        privateVH.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
                    }
                } else {
                    privateVH.runttime_lv.setVisibility(View.GONE);
                }

                break;

            case PROFESSIONAL:
                final ProfessionalVH prfessionalVH = (ProfessionalVH) holder;

                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    if (userid.getPersonalimage() != null) {
                        Glide.with(context).load("http://104.197.80.225:3010/wow/media/personal/" + userid.getPersonalimage()).into(prfessionalVH.profilePic);
                    }
                } else {
                    prfessionalVH.profilePic.setImageResource(R.drawable.profile_img);
                }

                //-------------------------EVENT USERNAME-----------------------------------------//
                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    if (userid.getFirstname() != null) {
                        prfessionalVH.name.setText(userid.getFirstname());
                    }
                }

                //-------------------------WOWSOMES COUNT------------------------------------------//

                if (doc.getWowsomecount() != null) {

                }
                if (doc.getWowtagvideothumb() != null && !doc.getWowtagvideothumb().equals("null")) {
                    Glide
                            .with(context)
                            .load(doc.getWowtagvideothumb())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                            .centerCrop()
                            .crossFade()
                            .into(prfessionalVH.wowtagvideo);
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


                //-------------------------EVENT DESIGNATION------------------------------------//
                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    if (userid.getDesignation() != null) {
                        prfessionalVH.timestamp.setText(userid.getDesignation());
                    }
                }

                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    Log.e("tag", "UID------>" + userId);
                    Log.e("tag", "userid------>" + userid.getId());

                    String Uid = userid.getId();
                    if (userId.equals(Uid)) {
                        prfessionalVH.sendinvite_tv.setVisibility(View.VISIBLE);
                    } else {
                        prfessionalVH.sendinvite_tv.setVisibility(View.GONE);
                    }
                }
                prfessionalVH.sendinvite_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        Intent intent = new Intent(context, EventInviteActivity.class);
                        Log.e("tag", "11111111111" + doc.getId());
                        Bundle bundle = new Bundle();
                        bundle.putString("eventId", doc.getId());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(context);
                        edit = sharedPrefces.edit();
                        edit.putString("eventId", doc.getId());
                        edit.putString("feedstatus", "allevents");
                        edit.commit();
                    }
                });

                //-------------------------COVER IMAGE---------------------------------------------//

                Glide
                        .with(context)
                        .load(doc.getCoverpageurl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(prfessionalVH.feedImageView);


                if (doc.getWowtagvideothumb() != null && !doc.getWowtagvideothumb().equals("null")) {
                    //    privateVH.wowtagvideo.setVisibility(View.VISIBLE);
                    Glide
                            .with(context)
                            .load(doc.getWowtagvideothumb())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                            .centerCrop()
                            .crossFade()
                            .into(prfessionalVH.wowtagvideo);
                }


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
                        final Doc doc = docs.get(position);
                        if (doc.getUserid() != null) {
                            userid = docs.get(position).getUserid();
                            Log.e("tag", "UID------>" + userId);
                            Log.e("tag", "userid------>" + userid.getId());

                            String Uid = userid.getId();
                            if (userId.equals(Uid)) {
                                deletetv.setVisibility(View.VISIBLE);
                            } else {
                                deletetv.setVisibility(View.GONE);
                            }
                        }
                        deletetv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                eventId = doc.getId();
                                new DeleteEvents(eventId, position).execute();

                            }
                        });

                    }
                });

                prfessionalVH.wowsome_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        String eventId = doc.getId();
                        Log.e("tag", "-----eventId---------->>>" + eventId);
                        new addWowsome(eventId).execute();
                        prfessionalVH.viewwowsome.setVisibility(view.VISIBLE);
                        prfessionalVH.viewwowsome.setText(wowsomecount + " Wowsomes");
                    }
                });

                prfessionalVH.comments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        prfessionalVH.viewcomments.setVisibility(view.VISIBLE);
                        comments_dialog = new Dialog(context);
                        comments_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        comments_dialog.setContentView(R.layout.dialog_comments);
                        View v1 = comments_dialog.getWindow().getDecorView().getRootView();
                        FontsOverride.overrideFonts(context, v1);
                        ImageView close = comments_dialog.findViewById(R.id.closeiv);
                        comments_et = comments_dialog.findViewById(R.id.comments_et);
                        ImageView send_btn = comments_dialog.findViewById(R.id.btn_chat_send);
                        Doc doc = docs.get(position);
                        String eventId = doc.getId();
                        commentslistview = (RecyclerView) comments_dialog.findViewById(R.id.recyclerViewBeneficiary);
                        //    Log.e("tag", "42545---------" + feedList.get(position).getComment());
                        new getAllComments(eventId).execute();

                        send_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Doc doc = docs.get(position);
                                String eventId = doc.getId();
                                String str_comments = comments_et.getText().toString();
                                //String name = doc.get;
                                if (str_comments.length() > 0) {
                                    new postComments(str_comments, username, eventId).execute();
                                }

                            }
                        });


                        FontsOverride.overrideFonts(comments_dialog.getContext(), v1);


                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                comments_dialog.dismiss();
                            }
                        });
                        comments_dialog.show();
                    }
                });

                prfessionalVH.frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Doc doc = docs.get(position);
                        final Dialog dialog = new Dialog(context, R.style.dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_videoview);
                        View v1 = dialog.getWindow().getDecorView().getRootView();
                        ImageView closeiv = dialog.findViewById(R.id.close_iv);
                        closeiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        VideoView videoView = dialog.findViewById(R.id.video_view);
                        if (doc.getWowtagvideourl() != null && doc.getWowtagvideourl() != null) {
                            dialog.show();
                            prfessionalVH.video3plus.setVisibility(View.VISIBLE);
                            videoView.setVideoURI(Uri.parse(doc.getWowtagvideourl()));
                            videoView.start();
                        } else {
                            prfessionalVH.video3plus.setVisibility(View.INVISIBLE);
                        }


                    }
                });

                //-------------------------HIGHLIGHT VIDEO CLICKING EVENT----------------------------------------//

                prfessionalVH.highlight2_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        final Dialog dialog = new Dialog(context, R.style.dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_videoview);
                        View v1 = dialog.getWindow().getDecorView().getRootView();
                        ImageView closeiv = dialog.findViewById(R.id.close_iv);
                        closeiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        VideoView videoView = dialog.findViewById(R.id.video_view);
                        if (doc.getEventhighlights2url() != null && doc.getEventhighlights2url() != null) {
                            dialog.show();
                            PrivateVH.video3plus.setVisibility(View.VISIBLE);
                            videoView.setVideoURI(Uri.parse(doc.getEventhighlights2url()));
                            videoView.start();
                        } else {
                            PrivateVH.video3plus.setVisibility(View.INVISIBLE);
                        }
                    }
                });

                //-------------------------HIGHLIGHT IMAGE CLICKING EVENT----------------------------------------//

                prfessionalVH.highlight1_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        final Dialog dialog = new Dialog(context, R.style.dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_imageview);
                        View v1 = dialog.getWindow().getDecorView().getRootView();
                        ImageView closeiv = dialog.findViewById(R.id.close_iv);
                        closeiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        ImageView videoView = dialog.findViewById(R.id.video_view);
                        if (doc.getEventhighlights1url() != null && !doc.getEventhighlights1url().equals("null")) {
                            Glide.with(context).load(doc.getEventhighlights1url())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                                    .centerCrop()
                                    .crossFade()
                                    .into(videoView);
                            //  imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/event/" + doc.getEventhighlights1(), videoView);
                            dialog.show();
                        } else {

                        }
                    }
                });
                //-------------------------FEEDIMAGE IMAGE CLICKING EVENT----------------------------------------//
                prfessionalVH.feedImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Doc doc = docs.get(position);
                        final Dialog dialog = new Dialog(context, R.style.dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_imageview);
                        View v1 = dialog.getWindow().getDecorView().getRootView();
                        ImageView closeiv = dialog.findViewById(R.id.close_iv);
                        closeiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        ImageView videoView = dialog.findViewById(R.id.video_view);
                        if (doc.getCoverpageurl() != null && !doc.getCoverpageurl().equals("null")) {

                            Glide.with(context).load(doc.getCoverpageurl())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                                    .centerCrop()
                                    .crossFade()
                                    .into(videoView);
                            dialog.show();
                        } else {

                        }


                    }
                });

                prfessionalVH.viewmore_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        List<Eventvenue> eventvenues = docs.get(position).getEventvenue();
                        Userid userid = docs.get(position).getUserid();
                        Log.e("tag", "eventvenue---" + eventvenues);
                        Intent intent = new Intent(context, ViewMoreDetailspage.class);

                        Bundle bundle = new Bundle();
                        if (doc.getEventvenue() != null) {
                            if (eventvenues.size() > 0) {
                                for (int i = 0; i < eventvenues.size(); i++) {
                                    String venueaddress = eventvenues.get(0).getEventvenuename() + " , " + eventvenues.get(0).getEventvenueaddress1() + ", " + eventvenues.get(0).getEventvenuecity() + ", " +
                                            eventvenues.get(0).getEventvenuezipcode();
                                    bundle.putString("eventvenueaddress", venueaddress);
                                }
                            }
                        }

                        if (doc.getWowtagvideourl() != null) {
                            bundle.putString("wowtagvideo", doc.getWowtagvideourl());
                        }

                        if (doc.getEventhighlights1() != null) {
                            bundle.putString("highlight1", doc.getEventhighlights1());
                        }

                        if (doc.getEventhighlightsvideo1() != null) {
                            bundle.putString("highlight2", doc.getEventhighlightsvideo1());
                        }

                        if (doc.getCoverpageurl() != null) {
                            bundle.putString("coverpage", doc.getCoverpageurl());
                        }

                        if (doc.getEventname() != null) {
                            bundle.putString("eventname", doc.getEventname());
                        }

                        if (doc.getEventdescription() != null) {
                            bundle.putString("description", doc.getEventdescription());
                        }
                        if (doc.getGiftregistryurl() != null) {

                            bundle.putString("gifturl", doc.getGiftregistryurl());
                        }
                        if (doc.getDonationsurl() != null) {
                            bundle.putString("donationurl", doc.getDonationsurl());

                        }
                        if (doc.getEventstartdate() != null) {
                            bundle.putString("eventstartdate", doc.getEventstartdate());
                        }
                        if (doc.getEventenddate() != null) {
                            bundle.putString("eventenddate", doc.getEventenddate());
                        }

                        bundle.putInt("eventdayscount", doc.getEventdayscount());
                        if (doc.getEventtype() != null) {
                            bundle.putString("eventtype", doc.getEventtype());
                        }

                        if (doc.getEventspeakername1() != null) {
                            bundle.putString("eventspeakername1", doc.getEventspeakername1());
                        }
                        if (doc.getEventspeakername2() != null) {
                            bundle.putString("eventspeakername2", doc.getEventspeakername1());
                        }
                        if (doc.getEventguesttype1() != null) {
                            bundle.putString("eventguesttype1", doc.getEventguesttype1());
                        }
                        if (doc.getEventguesttype2() != null) {
                            bundle.putString("eventguesttype2", doc.getEventguesttype2());
                        }
                        if (doc.getEventspeakeractivities1() != null) {
                            bundle.putString("eventspeakeractivities1", doc.getEventspeakeractivities1());
                        }
                        if (doc.getEventspeakeractivities2() != null) {
                            bundle.putString("eventspeakeractivities2", doc.getEventspeakeractivities2());
                        }
                        if (doc.getEventspeakerlink1() != null) {
                            bundle.putString("eventspeakerlink1", doc.getEventspeakerlink1());
                        }
                        if (doc.getEventspeakerlink2() != null) {
                            bundle.putString("eventspeakerlink2", doc.getEventspeakerlink2());
                        }


                        if (doc.getProgramschedule() != null) {
                            intent.putParcelableArrayListExtra("program", (ArrayList<? extends Parcelable>) docs.get(position).getProgramschedule());
                        }
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

                    }
                });


                if ((!doc.getEventstartdate().equals("null")) && (!doc.getEventenddate().equals("null"))) {

                    String fromTime = doc.getEventstartdate();
                    String toTime = doc.getEventenddate();
                    Log.e("tag", "111111Dateeeee------->" + fromTime + toTime);
                    try {
                        String[] separated = fromTime.split(" ");
                        String runFromDate = separated[0];
                        String runFromTime = separated[1];
                        //  String strDateFormatTo = fromTime;
                        SimpleDateFormat spf = new SimpleDateFormat("yyyy/MM/dd");
                        Date newDate = null;
                        try {
                            newDate = spf.parse(runFromDate);
                            spf = new SimpleDateFormat("MMM/dd/yyyy EEE");
                            runFromDate = spf.format(newDate);
                            System.out.println(runFromDate);

                            String str_fromdate1[] = runFromDate.split("/");
                            str_frommonth = str_fromdate1[0];
                            str_fromdate = str_fromdate1[1];
                            String str_fromtime[] = runFromDate.split(" ");
                            str_timefrom = str_fromtime[1];
                            Log.e("tag", "date2222-------->" + str_timefrom + str_frommonth + str_fromdate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mma");
                        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");
                        String strDateFormat = runFromTime + "a";
                        Log.e("tag", "date-------->" + runFromTime);
                        try {
                            Date dt = parseFormat.parse(strDateFormat);
                            System.out.println(outputFormat.format(dt));
                            str_fromtime = (outputFormat.format(dt));
                            Log.e("tag", "todate-------->" + outputFormat.format(dt));
                        } catch (ParseException exc) {
                            exc.printStackTrace();
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }

                    try {
                        String[] separated1 = toTime.split(" ");
                        String runFromDate1 = separated1[0];
                        String runFromTime1 = separated1[1];
                        SimpleDateFormat spf1 = new SimpleDateFormat("yyyy/MM/dd");
                        Date newDate1 = null;
                        try {
                            newDate1 = spf1.parse(runFromDate1);
                            spf1 = new SimpleDateFormat("MMM/dd/yyyy EEE");
                            runFromDate1 = spf1.format(newDate1);
                            Log.e("tag", "date-------->" + runFromDate1);
                            String str_fromdate12[] = runFromDate1.split("/");
                            str_tomonth = str_fromdate12[0];
                            str_todate = str_fromdate12[1];
                            String str_totime[] = runFromDate1.split(" ");
                            str_timeto = str_totime[1];
                            Log.e("tag", "date2222-------->" + str_timeto);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat outputFormat1 = new SimpleDateFormat("KK:mma");
                        SimpleDateFormat parseFormat1 = new SimpleDateFormat("hh:mm");
                        String strDateFormat1 = runFromTime1 + "a";
                        Log.e("tag", "date-------->" + runFromTime1);

                        try {
                            Date dt = parseFormat1.parse(strDateFormat1);
                            System.out.println(outputFormat1.format(dt));
                            str_totime = (outputFormat1.format(dt));
                            Log.e("tag", "todate-------->" + outputFormat1.format(dt));
                        } catch (ParseException exc) {
                            exc.printStackTrace();
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                    try {
                        if (str_frommonth.equals(str_tomonth)) {
                            prfessionalVH.month_tv.setText(str_frommonth);
                        } else {
                            prfessionalVH.month_tv.setText(str_frommonth + " to " + str_tomonth);
                        }
                    } catch (NullPointerException e) {

                    }

                    // date_tv.setText(str_fromdate + "-" + str_todate);
                    prfessionalVH.time_tv.setText(str_fromtime + "-" + str_totime);
                    try {
                        from = Integer.parseInt(str_fromdate);
                        to = Integer.parseInt(str_todate);
                    } catch (NumberFormatException e) {

                    }

                    if (from == 01) {
                        str_fromdate = str_fromdate + "st";
                    } else if (from == 02) {
                        str_fromdate = str_fromdate + "nd";
                    } else if (from == 03) {
                        str_fromdate = str_fromdate + "rd";
                    } else {
                        str_fromdate = str_fromdate + "th";
                    }
                    if (to == 01) {
                        str_todate = str_todate + "st";
                        //date_tv.setText("");
                        prfessionalVH.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
                    } else if (to == 02) {
                        str_todate = str_todate + "nd";
                        //  date_tv.setText("");
                        prfessionalVH.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
                    } else if (to == 03) {
                        str_todate = str_todate + "rd";
                        //date_tv.setText("");
                        prfessionalVH.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
                    } else {
                        str_todate = str_todate + "th";
                        //date_tv.setText("");
                        prfessionalVH.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
                    }
                } else {
                    prfessionalVH.runttime_lv.setVisibility(View.GONE);
                }


                break;


            case SOCIAL:
                final SocialVH socialVH = (SocialVH) holder;

                //  Userid userid;

                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    if (userid.getPersonalimage() != null) {
                        Glide.with(context).load("http://104.197.80.225:3010/wow/media/personal/" + userid.getPersonalimage()).into(socialVH.profilePic);
                    }
                } else {
                    socialVH.profilePic.setImageResource(R.drawable.profile_img);
                }

                //-------------------------EVENT USERNAME-----------------------------------------//
                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    if (userid.getFirstname() != null) {
                        socialVH.name.setText(userid.getFirstname());
                    }

                }


                if (doc.getWowtagvideothumb() != null && !doc.getWowtagvideothumb().equals("null")) {
                    Glide
                            .with(context)
                            .load(doc.getWowtagvideothumb())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                            .centerCrop()
                            .crossFade()
                            .into(socialVH.wowtagvideo);
                }

                //-------------------------WOWSOMES COUNT------------------------------------------//

                if (doc.getWowsomecount() != null) {

                    //  privateVH.wowsome_tv.


                }

                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    Log.e("tag", "UID------>" + userId);
                    Log.e("tag", "userid------>" + userid.getId());

                    String Uid = userid.getId();
                    if (userId.equals(Uid)) {
                        socialVH.sendinvite_tv.setVisibility(View.VISIBLE);
                    } else {
                        socialVH.sendinvite_tv.setVisibility(View.GONE);
                    }
                }
                socialVH.sendinvite_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        Intent intent = new Intent(context, EventInviteActivity.class);
                        Log.e("tag", "11111111111" + doc.getId());
                        Bundle bundle = new Bundle();
                        bundle.putString("eventId", doc.getId());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(context);
                        edit = sharedPrefces.edit();
                        edit.putString("feedstatus", "allevents");
                        edit.putString("eventId", doc.getId());
                        edit.commit();
                    }
                });

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


                //-------------------------EVENT DESIGNATION------------------------------------//
                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    if (userid.getDesignation() != null) {
                        socialVH.timestamp.setText(userid.getDesignation());
                    }
                }


                //-------------------------COVER IMAGE---------------------------------------------//

                Glide
                        .with(context)
                        .load(doc.getCoverpageurl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(socialVH.feedImageView);


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
                socialVH.menu_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        menu_dialog = new BottomSheetDialog(context);
                        menu_dialog.setContentView(R.layout.dialog_menu_list);
                        menu_dialog.setCancelable(true);
                        View v1 = menu_dialog.getWindow().getDecorView().getRootView();
                        FontsOverride.overrideFonts(context, v1);
                        menu_dialog.show();
                        TextView deletetv = menu_dialog.findViewById(R.id.delete_tv);
                        final Doc doc = docs.get(position);
                        if (doc.getUserid() != null) {
                            userid = docs.get(position).getUserid();
                            Log.e("tag", "UID------>" + userId);
                            Log.e("tag", "userid------>" + userid.getId());

                            String Uid = userid.getId();
                            if (userId.equals(Uid)) {
                                deletetv.setVisibility(View.VISIBLE);
                            } else {
                                deletetv.setVisibility(View.GONE);
                            }
                        }
                        deletetv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                eventId = doc.getId();
                                new DeleteEvents(eventId, position).execute();

                            }
                        });

                    }
                });

                socialVH.wowsome_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        String eventId = doc.getId();
                        Log.e("tag", "-----eventId---------->>>" + eventId);
                        new addWowsome(eventId).execute();
                        socialVH.viewwowsome.setVisibility(view.VISIBLE);
                        socialVH.viewwowsome.setText(wowsomecount + " Wowsomes");
                    }
                });

                socialVH.comments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        socialVH.viewcomments.setVisibility(view.VISIBLE);
                        comments_dialog = new Dialog(context);
                        comments_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        comments_dialog.setContentView(R.layout.dialog_comments);
                        View v1 = comments_dialog.getWindow().getDecorView().getRootView();
                        FontsOverride.overrideFonts(context, v1);
                        ImageView close = comments_dialog.findViewById(R.id.closeiv);
                        comments_et = comments_dialog.findViewById(R.id.comments_et);
                        ImageView send_btn = comments_dialog.findViewById(R.id.btn_chat_send);
                        Doc doc = docs.get(position);
                        String eventId = doc.getId();
                        commentslistview = (RecyclerView) comments_dialog.findViewById(R.id.recyclerViewBeneficiary);
                        //    Log.e("tag", "42545---------" + feedList.get(position).getComment());
                        new getAllComments(eventId).execute();

                        send_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Doc doc = docs.get(position);
                                String eventId = doc.getId();
                                String str_comments = comments_et.getText().toString();
                                //String name = doc.get;
                                if (str_comments.length() > 0) {
                                    new postComments(str_comments, username, eventId).execute();
                                }

                            }
                        });


                        FontsOverride.overrideFonts(comments_dialog.getContext(), v1);

                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                comments_dialog.dismiss();
                            }
                        });

                        comments_dialog.show();
                    }
                });

                socialVH.frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Doc doc = docs.get(position);
                        final Dialog dialog = new Dialog(context, R.style.dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_videoview);
                        View v1 = dialog.getWindow().getDecorView().getRootView();
                        ImageView closeiv = dialog.findViewById(R.id.close_iv);
                        closeiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        VideoView videoView = dialog.findViewById(R.id.video_view);
                        if (doc.getWowtagvideourl() != null && doc.getWowtagvideourl() != null) {
                            dialog.show();
                            socialVH.video3plus.setVisibility(View.VISIBLE);
                            videoView.setVideoURI(Uri.parse(doc.getWowtagvideourl()));
                            videoView.start();
                        } else {
                            socialVH.video3plus.setVisibility(View.INVISIBLE);
                        }


                    }
                });

                //-------------------------HIGHLIGHT VIDEO CLICKING EVENT-------------------------//

                socialVH.highlight2_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        final Dialog dialog = new Dialog(context, R.style.dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_videoview);
                        View v1 = dialog.getWindow().getDecorView().getRootView();
                        ImageView closeiv = dialog.findViewById(R.id.close_iv);
                        closeiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        VideoView videoView = dialog.findViewById(R.id.video_view);
                        if (doc.getEventhighlights2url() != null && doc.getEventhighlights2url() != null) {
                            dialog.show();
                            PrivateVH.video3plus.setVisibility(View.VISIBLE);
                            videoView.setVideoURI(Uri.parse(doc.getEventhighlights2url()));
                            videoView.start();
                        } else {
                            PrivateVH.video3plus.setVisibility(View.INVISIBLE);
                        }
                    }
                });

                //-------------------------HIGHLIGHT IMAGE CLICKING EVENT----------------------------------------//

                socialVH.highlight1_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        final Dialog dialog = new Dialog(context, R.style.dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_imageview);
                        View v1 = dialog.getWindow().getDecorView().getRootView();
                        ImageView closeiv = dialog.findViewById(R.id.close_iv);
                        closeiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        ImageView videoView = dialog.findViewById(R.id.video_view);
                        if (doc.getEventhighlights1url() != null && !doc.getEventhighlights1url().equals("null")) {
                            Glide.with(context).load(doc.getEventhighlights1url())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                                    .centerCrop()
                                    .crossFade()
                                    .into(videoView);
                            //  imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/event/" + doc.getEventhighlights1(), videoView);
                            dialog.show();
                        } else {

                        }
                    }
                });

                //-------------------------FEEDIMAGE IMAGE CLICKING EVENT--------------------------//


                socialVH.feedImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Doc doc = docs.get(position);
                        final Dialog dialog = new Dialog(context, R.style.dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_imageview);
                        View v1 = dialog.getWindow().getDecorView().getRootView();
                        ImageView closeiv = dialog.findViewById(R.id.close_iv);
                        closeiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        ImageView videoView = dialog.findViewById(R.id.video_view);
                        if (doc.getCoverpageurl() != null && doc.getCoverpageurl() != null) {
                            Glide.with(context).load(doc.getCoverpageurl())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                                    .centerCrop()
                                    .crossFade()
                                    .into(videoView);
                            dialog.show();
                        } else {
                        }


                    }
                });

                socialVH.viewmore_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        List<Eventvenue> eventvenues = docs.get(position).getEventvenue();
                        Userid userid = docs.get(position).getUserid();
                        Log.e("tag", "eventvenue---" + eventvenues);
                        Intent intent = new Intent(context, ViewMoreDetailspage.class);

                        Bundle bundle = new Bundle();
                        if (doc.getEventvenue() != null) {
                            if (eventvenues.size() > 0) {
                                for (int i = 0; i < eventvenues.size(); i++) {
                                    String venueaddress = eventvenues.get(0).getEventvenuename() + " , " + eventvenues.get(0).getEventvenueaddress1() + ", " + eventvenues.get(0).getEventvenuecity() + ", " +
                                            eventvenues.get(0).getEventvenuezipcode();
                                    bundle.putString("eventvenueaddress", venueaddress);
                                }
                            }
                        }

                        if (doc.getWowtagvideourl() != null) {
                            bundle.putString("wowtagvideo", doc.getWowtagvideourl());
                        }

                        if (doc.getEventhighlights1() != null) {
                            bundle.putString("highlight1", doc.getEventhighlights1());
                        }

                        if (doc.getEventhighlightsvideo1() != null) {
                            bundle.putString("highlight2", doc.getEventhighlightsvideo1());
                        }

                        if (doc.getCoverpageurl() != null) {
                            bundle.putString("coverpage", doc.getCoverpageurl());
                        }

                        if (doc.getEventname() != null) {
                            bundle.putString("eventname", doc.getEventname());
                        }

                        if (doc.getEventdescription() != null) {
                            bundle.putString("description", doc.getEventdescription());
                        }
                        if (doc.getGiftregistryurl() != null) {

                            bundle.putString("gifturl", doc.getGiftregistryurl());
                        }
                        if (doc.getDonationsurl() != null) {
                            bundle.putString("donationurl", doc.getDonationsurl());

                        }
                        if (doc.getEventstartdate() != null) {
                            bundle.putString("eventstartdate", doc.getEventstartdate());
                        }
                        if (doc.getEventenddate() != null) {
                            bundle.putString("eventenddate", doc.getEventenddate());
                        }

                        bundle.putInt("eventdayscount", doc.getEventdayscount());
                        if (doc.getEventtype() != null) {
                            bundle.putString("eventtype", doc.getEventtype());
                        }

                        if (doc.getEventspeakername1() != null) {
                            bundle.putString("eventspeakername1", doc.getEventspeakername1());
                        }
                        if (doc.getEventspeakername2() != null) {
                            bundle.putString("eventspeakername2", doc.getEventspeakername1());
                        }
                        if (doc.getEventguesttype1() != null) {
                            bundle.putString("eventguesttype1", doc.getEventguesttype1());
                        }
                        if (doc.getEventguesttype2() != null) {
                            bundle.putString("eventguesttype2", doc.getEventguesttype2());
                        }
                        if (doc.getEventspeakeractivities1() != null) {
                            bundle.putString("eventspeakeractivities1", doc.getEventspeakeractivities1());
                        }
                        if (doc.getEventspeakeractivities2() != null) {
                            bundle.putString("eventspeakeractivities2", doc.getEventspeakeractivities2());
                        }
                        if (doc.getEventspeakerlink1() != null) {
                            bundle.putString("eventspeakerlink1", doc.getEventspeakerlink1());
                        }
                        if (doc.getEventspeakerlink2() != null) {
                            bundle.putString("eventspeakerlink2", doc.getEventspeakerlink2());
                        }


                        if (doc.getProgramschedule() != null) {
                            intent.putParcelableArrayListExtra("program", (ArrayList<? extends Parcelable>) docs.get(position).getProgramschedule());
                        }
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

                    }
                });

                if ((!doc.getEventstartdate().equals("null")) && (!doc.getEventenddate().equals("null"))) {

                    String fromTime = doc.getEventstartdate();
                    String toTime = doc.getEventenddate();
                    Log.e("tag", "111111Dateeeee------->" + fromTime + toTime);
                    try {
                        String[] separated = fromTime.split(" ");
                        String runFromDate = separated[0];
                        String runFromTime = separated[1];
                        //  String strDateFormatTo = fromTime;
                        SimpleDateFormat spf = new SimpleDateFormat("yyyy/MM/dd");
                        Date newDate = null;
                        try {
                            newDate = spf.parse(runFromDate);
                            spf = new SimpleDateFormat("MMM/dd/yyyy EEE");
                            runFromDate = spf.format(newDate);
                            System.out.println(runFromDate);

                            String str_fromdate1[] = runFromDate.split("/");
                            str_frommonth = str_fromdate1[0];
                            str_fromdate = str_fromdate1[1];
                            String str_fromtime[] = runFromDate.split(" ");
                            str_timefrom = str_fromtime[1];
                            Log.e("tag", "date2222-------->" + str_timefrom + str_frommonth + str_fromdate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mma");
                        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");
                        String strDateFormat = runFromTime + "a";
                        Log.e("tag", "date-------->" + runFromTime);
                        try {
                            Date dt = parseFormat.parse(strDateFormat);
                            System.out.println(outputFormat.format(dt));
                            str_fromtime = (outputFormat.format(dt));
                            Log.e("tag", "todate-------->" + outputFormat.format(dt));
                        } catch (ParseException exc) {
                            exc.printStackTrace();
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }

                    try {
                        String[] separated1 = toTime.split(" ");
                        String runFromDate1 = separated1[0];
                        String runFromTime1 = separated1[1];
                        SimpleDateFormat spf1 = new SimpleDateFormat("yyyy/MM/dd");
                        Date newDate1 = null;
                        try {
                            newDate1 = spf1.parse(runFromDate1);
                            spf1 = new SimpleDateFormat("MMM/dd/yyyy EEE");
                            runFromDate1 = spf1.format(newDate1);
                            Log.e("tag", "date-------->" + runFromDate1);
                            String str_fromdate12[] = runFromDate1.split("/");
                            str_tomonth = str_fromdate12[0];
                            str_todate = str_fromdate12[1];
                            String str_totime[] = runFromDate1.split(" ");
                            str_timeto = str_totime[1];
                            Log.e("tag", "date2222-------->" + str_timeto);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat outputFormat1 = new SimpleDateFormat("KK:mma");
                        SimpleDateFormat parseFormat1 = new SimpleDateFormat("hh:mm");
                        String strDateFormat1 = runFromTime1 + "a";
                        Log.e("tag", "date-------->" + runFromTime1);

                        try {
                            Date dt = parseFormat1.parse(strDateFormat1);
                            System.out.println(outputFormat1.format(dt));
                            str_totime = (outputFormat1.format(dt));
                            Log.e("tag", "todate-------->" + outputFormat1.format(dt));
                        } catch (ParseException exc) {
                            exc.printStackTrace();
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                    try {
                        if (str_frommonth.equals(str_tomonth)) {
                            socialVH.month_tv.setText(str_frommonth);
                        } else {
                            socialVH.month_tv.setText(str_frommonth + " to " + str_tomonth);
                        }
                    } catch (NullPointerException e) {

                    }

                    // date_tv.setText(str_fromdate + "-" + str_todate);
                    socialVH.time_tv.setText(str_fromtime + "-" + str_totime);
                    try {
                        from = Integer.parseInt(str_fromdate);
                        to = Integer.parseInt(str_todate);
                    } catch (NumberFormatException e) {

                    }

                    if (from == 01) {
                        str_fromdate = str_fromdate + "st";
                    } else if (from == 02) {
                        str_fromdate = str_fromdate + "nd";
                    } else if (from == 03) {
                        str_fromdate = str_fromdate + "rd";
                    } else {
                        str_fromdate = str_fromdate + "th";
                    }
                    if (to == 01) {
                        str_todate = str_todate + "st";
                        //date_tv.setText("");
                        socialVH.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
                    } else if (to == 02) {
                        str_todate = str_todate + "nd";
                        //  date_tv.setText("");
                        socialVH.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
                    } else if (to == 03) {
                        str_todate = str_todate + "rd";
                        //date_tv.setText("");
                        socialVH.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
                    } else {
                        str_todate = str_todate + "th";
                        //date_tv.setText("");
                        socialVH.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
                    }
                } else {
                    socialVH.runttime_lv.setVisibility(View.GONE);
                }


                break;

            case BUSINESS:
                final BusinessVH businessVH = (BusinessVH) holder;
                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    if (userid.getPersonalimage() != null) {
                        Glide.with(context).load("http://104.197.80.225:3010/wow/media/personal/" + userid.getPersonalimage()).into(businessVH.profilePic);
                    }
                } else {
                    businessVH.profilePic.setImageResource(R.drawable.profile_img);
                }

                //-------------------------EVENT USERNAME-----------------------------------------//
                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    if (userid.getFirstname() != null) {
                        businessVH.name.setText(userid.getFirstname());
                    }

                }

                //-------------------------WOWSOMES COUNT------------------------------------------//

                if (doc.getWowsomecount() != null) {


                }
                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    Log.e("tag", "UID------>" + userId);
                    Log.e("tag", "userid------>" + userid.getId());

                    String Uid = userid.getId();
                    if (userId.equals(Uid)) {
                        businessVH.sendinvite_tv.setVisibility(View.VISIBLE);
                    } else {
                        businessVH.sendinvite_tv.setVisibility(View.GONE);
                    }
                }
                businessVH.sendinvite_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        Intent intent = new Intent(context, EventInviteActivity.class);
                        Log.e("tag", "11111111111" + doc.getId());
                        Bundle bundle = new Bundle();
                        bundle.putString("eventId", doc.getId());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(context);
                        edit = sharedPrefces.edit();
                        edit.putString("eventId", doc.getId());
                        edit.putString("feedstatus", "allevents");
                        edit.commit();
                    }
                });

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


                //-------------------------EVENT DESIGNATION------------------------------------//
                if (doc.getUserid() != null) {
                    userid = docs.get(position).getUserid();
                    if (userid.getDesignation() != null) {
                        businessVH.timestamp.setText(userid.getDesignation());
                    }
                }


                //-------------------------COVER IMAGE---------------------------------------------//

                Glide
                        .with(context)
                        .load(doc.getCoverpageurl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(businessVH.feedImageView);


                if (doc.getWowtagvideothumb() != null && !doc.getWowtagvideothumb().equals("null")) {
                    //    privateVH.wowtagvideo.setVisibility(View.VISIBLE);
                    Glide
                            .with(context)
                            .load(doc.getWowtagvideothumb())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                            .centerCrop()
                            .crossFade()
                            .into(businessVH.wowtagvideo);
                }


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

                List<Eventvenue> eventvenuesbus = docs.get(position).getEventvenue();

                Log.e("tag", "eventvenue---" + eventvenuesbus);

                if (eventvenuesbus != null) {
                    if (eventvenuesbus.size() > 0) {
                        for (int i = 0; i < eventvenuesbus.size(); i++) {
                            businessVH.eventaddress_tv.setText(eventvenuesbus.get(0).getEventvenuename() + " , " + eventvenuesbus.get(0).getEventvenueaddress1() + ", " + eventvenuesbus.get(0).getEventvenuecity() + ", " +
                                    eventvenuesbus.get(0).getEventvenuezipcode());
                        }
                    }
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
                        final Doc doc = docs.get(position);
                        if (doc.getUserid() != null) {
                            userid = docs.get(position).getUserid();
                            Log.e("tag", "UID------>" + userId);
                            Log.e("tag", "userid------>" + userid.getId());

                            String Uid = userid.getId();
                            if (userId.equals(Uid)) {
                                deletetv.setVisibility(View.VISIBLE);
                            } else {
                                deletetv.setVisibility(View.GONE);
                            }
                        }
                        deletetv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                eventId = doc.getId();
                                new DeleteEvents(eventId, position).execute();

                            }
                        });

                    }
                });
                businessVH.wowsome_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        String eventId = doc.getId();
                        Log.e("tag", "-----eventId---------->>>" + eventId);
                        new addWowsome(eventId).execute();
                        businessVH.viewwowsome.setVisibility(view.VISIBLE);
                        businessVH.viewwowsome.setText(wowsomecount + " Wowsomes");
                    }
                });

                businessVH.comments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        businessVH.viewcomments.setVisibility(view.VISIBLE);
                        comments_dialog = new Dialog(context);
                        comments_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        comments_dialog.setContentView(R.layout.dialog_comments);
                        View v1 = comments_dialog.getWindow().getDecorView().getRootView();
                        FontsOverride.overrideFonts(context, v1);
                        ImageView close = comments_dialog.findViewById(R.id.closeiv);
                        comments_et = comments_dialog.findViewById(R.id.comments_et);
                        ImageView send_btn = comments_dialog.findViewById(R.id.btn_chat_send);
                        Doc doc = docs.get(position);
                        String eventId = doc.getId();
                        commentslistview = (RecyclerView) comments_dialog.findViewById(R.id.recyclerViewBeneficiary);
                        //    Log.e("tag", "42545---------" + feedList.get(position).getComment());
                        new getAllComments(eventId).execute();

                        send_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Doc doc = docs.get(position);
                                String eventId = doc.getId();
                                String str_comments = comments_et.getText().toString();
                                //String name = doc.get;
                                if (str_comments.length() > 0) {
                                    new postComments(str_comments, username, eventId).execute();
                                }

                            }
                        });


                        FontsOverride.overrideFonts(comments_dialog.getContext(), v1);


                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                comments_dialog.dismiss();
                            }
                        });
                        comments_dialog.show();
                    }
                });

                businessVH.frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Doc doc = docs.get(position);
                        final Dialog dialog = new Dialog(context, R.style.dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_videoview);
                        View v1 = dialog.getWindow().getDecorView().getRootView();
                        ImageView closeiv = dialog.findViewById(R.id.close_iv);
                        VideoView videoView = dialog.findViewById(R.id.video_view);
                        closeiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        if (doc.getWowtagvideourl() != null && !doc.getWowtagvideourl().equals("null")) {
                            dialog.show();
                            businessVH.video3plus.setVisibility(View.VISIBLE);
                            videoView.setVideoURI(Uri.parse(doc.getWowtagvideourl()));
                            videoView.start();
                        } else {
                            businessVH.video3plus.setVisibility(View.INVISIBLE);
                        }


                    }
                });

                //-------------------------HIGHLIGHT VIDEO CLICKING EVENT----------------------------------------//

                businessVH.highlight2_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        final Dialog dialog = new Dialog(context, R.style.dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_videoview);
                        View v1 = dialog.getWindow().getDecorView().getRootView();
                        ImageView closeiv = dialog.findViewById(R.id.close_iv);
                        closeiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        VideoView videoView = dialog.findViewById(R.id.video_view);
                        if (doc.getEventhighlights2url() != null && doc.getEventhighlights2url() != null) {
                            dialog.show();
                            PrivateVH.video3plus.setVisibility(View.VISIBLE);
                            videoView.setVideoURI(Uri.parse(doc.getEventhighlights2url()));
                            videoView.start();
                        } else {
                            PrivateVH.video3plus.setVisibility(View.INVISIBLE);
                        }
                    }
                });

                //-------------------------HIGHLIGHT IMAGE CLICKING EVENT----------------------------------------//

                businessVH.highlight1_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        final Dialog dialog = new Dialog(context, R.style.dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_imageview);
                        View v1 = dialog.getWindow().getDecorView().getRootView();
                        ImageView closeiv = dialog.findViewById(R.id.close_iv);
                        closeiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        ImageView videoView = dialog.findViewById(R.id.video_view);
                        if (doc.getEventhighlights1url() != null && !doc.getEventhighlights1url().equals("null")) {
                            Glide.with(context).load(doc.getEventhighlights1url())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                                    .centerCrop()
                                    .crossFade()
                                    .into(videoView);
                            //  imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/event/" + doc.getEventhighlights1(), videoView);
                            dialog.show();
                        } else {

                        }
                    }
                });
                //-------------------------FEEDIMAGE IMAGE CLICKING EVENT----------------------------------------//
                businessVH.feedImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Doc doc = docs.get(position);
                        final Dialog dialog = new Dialog(context, R.style.dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_imageview);
                        View v1 = dialog.getWindow().getDecorView().getRootView();
                        ImageView closeiv = dialog.findViewById(R.id.close_iv);
                        closeiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        ImageView videoView = dialog.findViewById(R.id.video_view);
                        if (doc.getCoverpageurl() != null && !doc.getCoverpageurl().equals("null")) {

                            Glide.with(context).load(doc.getCoverpageurl())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                                    .centerCrop()
                                    .crossFade()
                                    .into(videoView);
                            dialog.show();
                        } else {

                        }


                    }
                });

                businessVH.viewmore_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Doc doc = docs.get(position);
                        List<Eventvenue> eventvenues = docs.get(position).getEventvenue();
                        Userid userid = docs.get(position).getUserid();
                        Log.e("tag", "eventvenue---" + eventvenues);
                        Intent intent = new Intent(context, ViewMoreDetailspage.class);

                        Bundle bundle = new Bundle();
                        if (doc.getEventvenue() != null) {
                            if (eventvenues.size() > 0) {
                                for (int i = 0; i < eventvenues.size(); i++) {
                                    String venueaddress = eventvenues.get(0).getEventvenuename() + " , " + eventvenues.get(0).getEventvenueaddress1() + ", " + eventvenues.get(0).getEventvenuecity() + ", " +
                                            eventvenues.get(0).getEventvenuezipcode();
                                    bundle.putString("eventvenueaddress", venueaddress);
                                }
                            }
                        }

                        if (doc.getWowtagvideourl() != null) {
                            bundle.putString("wowtagvideo", doc.getWowtagvideourl());
                        }

                        if (doc.getEventhighlights1() != null) {
                            bundle.putString("highlight1", doc.getEventhighlights1());
                        }

                        if (doc.getEventhighlightsvideo1() != null) {
                            bundle.putString("highlight2", doc.getEventhighlightsvideo1());
                        }

                        if (doc.getCoverpageurl() != null) {
                            bundle.putString("coverpage", doc.getCoverpageurl());
                        }

                        if (doc.getEventname() != null) {
                            bundle.putString("eventname", doc.getEventname());
                        }

                        if (doc.getEventdescription() != null) {
                            bundle.putString("description", doc.getEventdescription());
                        }
                        if (doc.getGiftregistryurl() != null) {

                            bundle.putString("gifturl", doc.getGiftregistryurl());
                        }
                        if (doc.getDonationsurl() != null) {
                            bundle.putString("donationurl", doc.getDonationsurl());

                        }
                        if (doc.getEventstartdate() != null) {
                            bundle.putString("eventstartdate", doc.getEventstartdate());
                        }
                        if (doc.getEventenddate() != null) {
                            bundle.putString("eventenddate", doc.getEventenddate());
                        }

                        bundle.putInt("eventdayscount", doc.getEventdayscount());
                        if (doc.getEventtype() != null) {
                            bundle.putString("eventtype", doc.getEventtype());
                        }

                        if (doc.getEventspeakername1() != null) {
                            bundle.putString("eventspeakername1", doc.getEventspeakername1());
                        }
                        if (doc.getEventspeakername2() != null) {
                            bundle.putString("eventspeakername2", doc.getEventspeakername1());
                        }
                        if (doc.getEventguesttype1() != null) {
                            bundle.putString("eventguesttype1", doc.getEventguesttype1());
                        }
                        if (doc.getEventguesttype2() != null) {
                            bundle.putString("eventguesttype2", doc.getEventguesttype2());
                        }
                        if (doc.getEventspeakeractivities1() != null) {
                            bundle.putString("eventspeakeractivities1", doc.getEventspeakeractivities1());
                        }
                        if (doc.getEventspeakeractivities2() != null) {
                            bundle.putString("eventspeakeractivities2", doc.getEventspeakeractivities2());
                        }
                        if (doc.getEventspeakerlink1() != null) {
                            bundle.putString("eventspeakerlink1", doc.getEventspeakerlink1());
                        }
                        if (doc.getEventspeakerlink2() != null) {
                            bundle.putString("eventspeakerlink2", doc.getEventspeakerlink2());
                        }


                        if (doc.getProgramschedule() != null) {
                            intent.putParcelableArrayListExtra("program", (ArrayList<? extends Parcelable>) docs.get(position).getProgramschedule());
                        }
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

                    }
                });


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

    public void add(Doc mc) {
        docs.add(mc);
        notifyItemInserted(docs.size() - 1);
    }

    public void addAll(List<Doc> mcList) {
        for (Doc mc : mcList) {
            add(mc);
        }
    }

    public void remove(Doc city) {
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

    public void AddThoughts(Doc doc) {
        docs.add(doc);
        notifyDataSetChanged();
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Doc());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;


        try {
            int position = docs.size() - 1;

            if (position > 0) {
                Doc item = getItem(position);
                if (item != null) {
                    docs.remove(position);
                    notifyItemRemoved(position);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {

        }


    }

    public Doc getItem(int position) {
        return docs.get(position);
    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        Log.e("tag", "FILTERRRRR--->>>>" + charText);
        for (int i = 0; i < filtercontactsList.size(); i++) {
            if (filtercontactsList.get(i).getEventname().contains(charText)) {
                Log.e("tag", "FILTERRRRR--->>>>" + charText);

                docs.clear();
                //  filtercontactsList.add(filtercontactsList.get(i).getEventname());

            }
        }
        //docs.clear();
       /* if (charText.length() == 0) {
            docs.addAll(docs);
        } else {
            for (Doc contact : filtercontactsList) {
                Log.e("tag", "FILTERRRRR111111--->>>>" + contact.getEventname());

                if (charText.length() != 0 && contact.getEventname().toLowerCase(Locale.getDefault()).contains(charText)) {
                    Log.e("tag", "FILTERRRRR111111" + contact.getEventname());
                    docs.add(contact);
                } else if (charText.length() != 0 && contact.getEventname().toLowerCase(Locale.getDefault()).contains(charText)) {
                    docs.add(contact);
                }
            }
        }*/
        notifyDataSetChanged();
    }

    public void setFilter(List<Doc> countryModels) {
        docs = new ArrayList<>();
        Log.e("tag", "ITEMMMMMMtdocsssssss" + countryModels);

        docs.addAll(countryModels);


        notifyDataSetChanged();
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    static class LoadHolder extends RecyclerView.ViewHolder {
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    protected static class PrivateVH extends RecyclerView.ViewHolder {

        private static ImageView video3plus;
        TextView viewmore_tv, viewcomments, viewshare, wowsome_tv, viewwowsome, eventcategory_tv;
        private TextView rsvptv, sendinvite_tv, timestamp, eventname_tv, name, desc, share, menu_tv, comments, eventtopic_tv, otherurl_tv, eventaddress_tv, month_tv, date_tv, time_tv;
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
            rsvptv = itemView.findViewById(R.id.rsvptv);
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
        private TextView sendinvite_tv, timestamp, eventname_tv, name, desc, share, comments, eventtopic_tv, menu_tv, otherurl_tv, eventaddress_tv, month_tv, date_tv, time_tv;
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

    protected class SocialVH extends RecyclerView.ViewHolder {

        TextView viewmore_tv, viewcomments, viewshare, wowsome_tv, viewwowsome, eventcategory_tv;
        private TextView sendinvite_tv, timestamp, eventname_tv, name, desc, share, comments, eventtopic_tv, menu_tv, otherurl_tv, eventaddress_tv, month_tv, date_tv, time_tv;
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

    protected class BusinessVH extends RecyclerView.ViewHolder {

        TextView viewmore_tv, viewcomments, viewshare, wowsome_tv, viewwowsome, eventcategory_tv;
        private TextView sendinvite_tv, timestamp, eventname_tv, name, desc, share, menu_tv, comments, eventtopic_tv, otherurl_tv, eventaddress_tv, month_tv, date_tv, time_tv;
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
            //date_tv = itemView.findViewById(R.id.date_tv);
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
    private class addThoughtsWowsome extends AsyncTask<String, Void, String> {
        String eventId;

        public addThoughtsWowsome(String eventId) {
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
                jsonObject.accumulate("thoughtid", eventId);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/postthoughtscomment", json, token);
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
                      //  String wowsomestr = message.getString("comments");
                        wowsomecount = jo.getString("commentcount");
                        Log.e("tag", "wowsomestr-------->" + wowsomecount);
                      //  notifyDataChanged();
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

    private class postRSVP extends AsyncTask<String, Void, String> {
        String eventId;

        public postRSVP(String eventId) {
            this.eventId = eventId;
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
                jsonObject.accumulate("eventid", eventId);
                jsonObject.accumulate("extra", str_eventday);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/postrsvp", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "RESSSSSSS-------->" + s.toString());
            loader_dialog.dismiss();
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        // JSONObject message = jo.getJSONObject("message");
                        Toast.makeText(context, "Succesfully Register", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {

                }

            } else {
            }

        }


    }
}
