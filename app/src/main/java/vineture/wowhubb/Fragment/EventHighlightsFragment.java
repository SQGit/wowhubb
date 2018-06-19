package vineture.wowhubb.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import vineture.wowhubb.Activity.CreateEventActivity;
import vineture.wowhubb.Adapter.CustomAdapter;
import vineture.wowhubb.Adapter.ExpandableListAdapter;
import vineture.wowhubb.Adapter.ExpandableListDataPump;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.GetFilePathFromDevice;
import vineture.wowhubb.Utils.HttpUtils;
import vineture.wowhubb.data.EventData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

public class EventHighlightsFragment extends Fragment {

    private static final int INTENT_REQUEST_GET_HIGHLIGHT1 = 14;
    private static final int INTENT_REQUEST_GET_HIGHLIGHT2 = 15;
    private static final int INTENT_REQUEST_GET_HIGHLIGHT11 = 16;
    private static final int INTENT_REQUEST_GET_HIGHLIGHT12 = 17;
    public static TextInputLayout til_speaker, til_intro, til_url, til_guesttype;
    public static EditText speaker_et, url_et, intro_et;
    public static String eventhighlightsvideo1, eventhighlights1;
    public static TextInputLayout til_speaker1, til_intro1, til_url1, til_guesttype1;
    public static EditText speaker1_et, url1_et, intro1_et;
    public static String eventhighlightsvideo2, eventhighlights2;
    public EventData eventData;
    Typeface lato;
    FrameLayout framehighlight1;
    ImageView highl1_iv, high13_iv;
    ImageView h1plus, h2plus, wowtagiv;
    FrameLayout framehighlight11;
    //ImageView h11plus, h12plus;
    Dialog dialog;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    SharedPreferences.Editor edit;
    SharedPreferences sharedPrefces;
    ArrayList<String> ar_sectionno = new ArrayList<>();
    AutoCompleteTextView wowtag;
    TextView addmore_iv, helpfultips_tv;
    String[] SPINNERLIST = {"Guest Speaker", "Event Host", "Guest Artist", "Comedian", "Music Minister", "Music Artist", "Pre-Wedding Clips", "Pre-Birthday Clips", "Past Event Clips", "Others"};
    Set<String> map1;
    String token;
    MaterialBetterSpinner materialDesignSpinner, materialBetterSpinner1;
    String str_type;
    LinearLayout highlight_lv1, highlight_lv2;
    ScrollView scrollView;
    String str_addmore;

    public static EventHighlightsFragment newInstance(int page, boolean isLast) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        if (isLast)
            args.putBoolean("isLast", true);
        final EventHighlightsFragment fragment = new EventHighlightsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        eventData = ((CreateEventActivity) getActivity()).eventData;
        final View view = inflater.inflate(vineture.wowhubb.R.layout.fragment_eventspeaker, container, false);
        FontsOverride.overrideFonts(getActivity(), view);
        CreateEventActivity.skiptv.setVisibility(View.VISIBLE);

        final Animation slide_down = AnimationUtils.loadAnimation(getActivity(), vineture.wowhubb.R.anim.top);
        final Animation slide_up = AnimationUtils.loadAnimation(getActivity(), vineture.wowhubb.R.anim.bottom);

        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");
        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getActivity());
        edit = sharedPrefces.edit();
        token = sharedPrefces.getString("token", "");
        str_addmore = sharedPrefces.getString("addmorestatus", "");

        Log.e("tag", "111111111" + str_addmore);
        til_speaker = view.findViewById(vineture.wowhubb.R.id.til_nameofspeaker);
        til_url =  view.findViewById(vineture.wowhubb.R.id.til_url);
        til_intro = view.findViewById(vineture.wowhubb.R.id.til_intro);
        til_guesttype = view.findViewById(vineture.wowhubb.R.id.til_guesttype);

        til_speaker1 =  view.findViewById(vineture.wowhubb.R.id.til_nameofspeaker1);
        til_url1 =view.findViewById(vineture.wowhubb.R.id.til_url1);
        til_intro1 =  view.findViewById(vineture.wowhubb.R.id.til_intro1);
        til_guesttype1 = view.findViewById(vineture.wowhubb.R.id.til_guesttype1);

        helpfultips_tv = view.findViewById(vineture.wowhubb.R.id.helpfultips_tv);

        highlight_lv1 = view.findViewById(vineture.wowhubb.R.id.highlight1_lv);
        highlight_lv2 = view.findViewById(vineture.wowhubb.R.id.highlight2_lv);


        scrollView = view.findViewById(vineture.wowhubb.R.id.scrollview);
        til_intro.setTypeface(lato);
        til_url.setTypeface(lato);
        til_speaker.setTypeface(lato);
        til_intro1.setTypeface(lato);
        til_url1.setTypeface(lato);
        til_speaker1.setTypeface(lato);
        til_guesttype.setTypeface(lato);
        til_guesttype1.setTypeface(lato);

        h1plus = view.findViewById(vineture.wowhubb.R.id.highlight1plus_iv);
        //h2plus = view.findViewById(R.id.highlight2plus_iv);

        framehighlight1 = view.findViewById(vineture.wowhubb.R.id.frame_highlight1);
     //   fraehighlight2 = view.findViewById(R.id.frame_highlight2);
        addmore_iv = view.findViewById(vineture.wowhubb.R.id.addmore_iv);

        highl1_iv = (ImageView) view.findViewById(vineture.wowhubb.R.id.highlight1_iv);
     //   highl2_iv = (ImageView) view.findViewById(R.id.highlight2_iv);
        speaker_et = view.findViewById(vineture.wowhubb.R.id.speaker_et);
        url_et = view.findViewById(vineture.wowhubb.R.id.url_et);
        intro_et = view.findViewById(vineture.wowhubb.R.id.intro_et);


        til_speaker.setHint("Name of Event Guest type");
        til_url.setHint("Event Guest URL/Link");
        til_intro.setHint("Event Guest Introduction");
        wowtagiv = view.findViewById(vineture.wowhubb.R.id.wowtag_add);
        speaker_et.setText(eventData.gusetname);
        url_et.setText(eventData.guesturl);
        intro_et.setText(eventData.guestintro);

     /*   h11plus = view.findViewById(R.id.highlight1plus_iv1);
        h12plus = view.findViewById(R.id.highlight2plus_iv1);*/

        framehighlight11 = view.findViewById(vineture.wowhubb.R.id.frame_highlight11);
        //fraehighlight12 = view.findViewById(R.id.frame_highlight21);

        high13_iv = (ImageView) view.findViewById(vineture.wowhubb.R.id.highlight1_iv1);
       // high14_iv = (ImageView) view.findViewById(R.id.highlight2_iv1);
        speaker1_et = view.findViewById(vineture.wowhubb.R.id.speaker_et1);
        url1_et = view.findViewById(vineture.wowhubb.R.id.url_et1);
        intro1_et = view.findViewById(vineture.wowhubb.R.id.intro_et1);


        til_speaker1.setHint("Name of Event Guest type");
        til_url1.setHint("Event Guest URL/Link");
        til_intro1.setHint("Event Guest Introduction");

        speaker1_et.setText(eventData.gusetname);
        url1_et.setText(eventData.guesturl);
        intro1_et.setText(eventData.guestintro);

        if (str_addmore != null) {
            if (str_addmore.equals("trueh1")) {
                scrollView.scrollTo(0, intro1_et.getTop());
                highlight_lv1.setVisibility(View.GONE);
                highlight_lv2.setVisibility(View.VISIBLE);
                highlight_lv2.startAnimation(slide_up);
                addmore_iv.setText("View Highlights Clips");

                edit.putString("addmorestatus", "trueh2");
                edit.apply();
            } else if (str_addmore.equals("trueh2")) {
                scrollView.scrollTo(0, intro1_et.getTop());
                highlight_lv2.setVisibility(View.GONE);
                highlight_lv1.setVisibility(View.VISIBLE);
                highlight_lv1.startAnimation(slide_up);
                addmore_iv.setText("View Highlights Clips");
                edit.putString("addmorestatus", "trueh1");
                edit.commit();
            } else {
                scrollView.scrollTo(0, intro1_et.getTop());
                highlight_lv2.setVisibility(View.GONE);
                highlight_lv1.setVisibility(View.VISIBLE);
                highlight_lv1.startAnimation(slide_up);
                addmore_iv.setText("Add more Event Highlights Clips");
                //edit.putString("addmorestatus", "tr");
                // edit.commit();
            }

        } else {
            scrollView.scrollTo(0, intro1_et.getTop());
            highlight_lv2.setVisibility(View.GONE);
            highlight_lv1.setVisibility(View.VISIBLE);
            highlight_lv1.startAnimation(slide_up);
            addmore_iv.setText("Add more Event Highlights Clips");
            // edit.putString("addmorestatus", "trueh2");
            //edit.commit();
        }


        try {
            if (eventData.eventhighlight2 != "") {
                Log.e("tag", "111111111111111111-dddd------" + eventData.eventhighlight2);
                if (eventData.eventhighlight2 != null) {
                  //  highl2_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(eventData.eventhighlight2, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                    h2plus.setImageDrawable(getActivity().getDrawable(vineture.wowhubb.R.drawable.video_icon));
                    //highl2_iv.setImageDrawable(getActivity().getDrawable(R.drawable.dotted_square));
                }
            } else if (eventData.eventhighlight2.equals("null")) {
                Log.e("tag", "22222222222-dddd------" + eventData.eventhighlight2);
               // highl2_iv.setImageDrawable(getActivity().getDrawable(R.drawable.dotted_square));
                h2plus.setVisibility(View.VISIBLE);
                //highl2_iv.setImageDrawable(getActivity().getDrawable(R.drawable.dotted_square));
            } else {
                Log.e("tag", "3333333333333-dddd------" + eventData.eventhighlight2);
               // highl2_iv.setImageDrawable(getActivity().getDrawable(R.drawable.dotted_square));
                h2plus.setVisibility(View.VISIBLE);
            }


        } catch (NullPointerException e) {
            Log.e("tag","exc"+e.toString());
        }
        Log.e("tag", "ev1111-dddd------" + eventData.eventhighlight1);
        try {
            if (eventData.eventhighlight1 != "") {

                File imgFile = new File(eventData.eventhighlight1);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //ImageView imageView=(ImageView)findViewById(R.id.imageView);
                    highl1_iv.setImageBitmap(myBitmap);
                    //    highl1_iv.setImageDrawable(getActivity().getDrawable(R.drawable.dotted_square));
                    h1plus.setVisibility(View.GONE);
                }
                Log.e("tag", "11111-dddd------" + eventData.eventhighlight1);

            } else if (eventData.eventhighlight1 == null) {
                Log.e("tag", "22222222222-dddd------" + eventData.eventwowvideo);
                File imgFile = new File(eventData.eventhighlight1);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //ImageView imageView=(ImageView)findViewById(R.id.imageView);
                    highl1_iv.setImageBitmap(myBitmap);
                    // highl1_iv.setImageDrawable(getActivity().getDrawable(R.drawable.dotted_square));
                    h1plus.setVisibility(View.GONE);
                } else {
                    highl1_iv.setImageDrawable(getActivity().getDrawable(vineture.wowhubb.R.drawable.dotted_square));
                    h1plus.setVisibility(View.VISIBLE);
                }
                Log.e("tag", "11111-dddd------" + eventData.eventhighlight1);
            }
        }
        catch (NullPointerException e)
        {
            Log.e("tag","exc"+e.toString());
        }


        //------------------------------------------------------------------------------------------//


        Log.e("tag", "ev1111-dddd------" + eventData.eventhighlight1);
        try {
            if (eventData.eventhighlight11 != "") {

                File imgFile = new File(eventData.eventhighlight11);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //ImageView imageView=(ImageView)findViewById(R.id.imageView);
                    high13_iv.setImageBitmap(myBitmap);
                    //    highl1_iv.setImageDrawable(getActivity().getDrawable(R.drawable.dotted_square));
                  //..  h11plus.setVisibility(View.GONE);
                }
                Log.e("tag", "11111-dddd------>" + eventData.eventhighlight11);

            } else if (eventData.eventhighlight11 == null) {
                Log.e("tag", "22222222222-dddd------>" + eventData.eventwowvideo);
                File imgFile = new File(eventData.eventhighlight11);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //ImageView imageView=(ImageView)findViewById(R.id.imageView);
                    highl1_iv.setImageBitmap(myBitmap);
                    // highl1_iv.setImageDrawable(getActivity().getDrawable(R.drawable.dotted_square));
                    h1plus.setVisibility(View.GONE);
                }
                else
                {
                    highl1_iv.setImageDrawable(getActivity().getDrawable(vineture.wowhubb.R.drawable.dotted_square));
                    h1plus.setVisibility(View.VISIBLE);
                }
                Log.e("tag", "11111------->" + eventData.eventhighlight11);
            }
        } catch (NullPointerException e) {
            Log.e("tag","exc"+e.toString());
        }


        wowtagiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                new getWowtag().execute();
            }
        });


        helpfultips_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(vineture.wowhubb.R.layout.dialog_helpfultips);
                View v1 = dialog.getWindow().getDecorView().getRootView();
                ImageView close = dialog.findViewById(vineture.wowhubb.R.id.closeiv);
                FontsOverride.overrideFonts(dialog.getContext(), v1);

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = dialog.getWindow();
                    }
                });


                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                expandableListView =  dialog.findViewById(vineture.wowhubb.R.id.expandableListView);
                expandableListDetail = ExpandableListDataPump.getData();
                expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                expandableListAdapter = new ExpandableListAdapter(dialog.getContext(), expandableListTitle, expandableListDetail);

                expandableListView.setAdapter(expandableListAdapter);

                expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    public void onGroupExpand(int groupPosition) {}
                });

                expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                    public void onGroupCollapse(int groupPosition) { }
                });

                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id)
                    {
                        return true;
                    }
                });

                dialog.show();
            }
        });

        addmore_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_addmore = sharedPrefces.getString("addmorestatus", "");
                Log.e("tag", "adddmoew------------->>>" + str_addmore);
                if (str_addmore != null) {
                    if (str_addmore.equals("trueh2")) {
                        scrollView.scrollTo(0, intro1_et.getTop());
                        highlight_lv2.setVisibility(View.GONE);
                        highlight_lv1.setVisibility(View.VISIBLE);
                        highlight_lv2.startAnimation(slide_up);
                        addmore_iv.setText("View Highlights Clips");
                        edit.putString("addmorestatus", "trueh1");
                        edit.commit();
                    } else if (str_addmore.equals("trueh1")) {
                        scrollView.scrollTo(0, intro1_et.getTop());
                        highlight_lv1.setVisibility(View.GONE);
                        highlight_lv2.setVisibility(View.VISIBLE);
                        highlight_lv2.startAnimation(slide_up);
                        addmore_iv.setText("View Highlights Clips");
                        edit.putString("addmorestatus", "trueh2");
                        edit.commit();
                    } else {
                        scrollView.scrollTo(0, intro1_et.getTop());
                        highlight_lv1.setVisibility(View.GONE);
                        highlight_lv2.setVisibility(View.VISIBLE);
                        highlight_lv2.startAnimation(slide_up);
                        addmore_iv.setText("View Highlights Clips");
                        edit.putString("addmorestatus", "trueh2");
                        edit.commit();
                    }

                } else {
                    scrollView.scrollTo(0, intro1_et.getTop());
                    highlight_lv1.setVisibility(View.GONE);
                    highlight_lv2.setVisibility(View.VISIBLE);
                    highlight_lv2.startAnimation(slide_up);
                    addmore_iv.setText("View Highlights Clips");
                    edit.putString("addmorestatus", "trueh2");
                    edit.commit();
                }


            }
        });


        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(vineture.wowhubb.R.layout.dialog_addwowtag);
        wowtag = (AutoCompleteTextView) dialog.findViewById(vineture.wowhubb.R.id.wowtag_ac);

        //Max Screen width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        ImageView iv = dialog.findViewById(vineture.wowhubb.R.id.closeiv);
        final TextView ok_tv = dialog.findViewById(vineture.wowhubb.R.id.ok_tv);
        View view1 = dialog.getWindow().getDecorView().getRootView();

        FontsOverride.overrideFonts(dialog.getContext(), view1);

        wowtag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ok_tv.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                String Name = wowtag.getText().toString();
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        til_url.setTypeface(lato);
        til_speaker.setTypeface(lato);
        til_intro.setTypeface(lato);

        materialDesignSpinner = view.findViewById(vineture.wowhubb.R.id.sp);
        materialBetterSpinner1 =  view.findViewById(vineture.wowhubb.R.id.sp1);
        final CustomAdapter arrayAdapter = new CustomAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNERLIST) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return true;
                } else {
                    return true;
                }
            }

            @Override
            @NonNull
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTypeface(lato);
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
            @NonNull
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(15);
                tv.setPadding(10, 20, 0, 20);
                tv.setTypeface(lato);
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        materialDesignSpinner.setAdapter(arrayAdapter);

        final CustomAdapter arrayAdapter1 = new CustomAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNERLIST) {
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
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTypeface(lato);
                tv.setTextSize(9);
                tv.setPadding(30, 55, 10, 25);
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }


            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(15);
                tv.setPadding(10, 20, 0, 20);
                tv.setTypeface(lato);
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        materialBetterSpinner1.setAdapter(arrayAdapter1);

        materialDesignSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FontsOverride.overrideFonts(getContext(), view);
                str_type = adapterView.getItemAtPosition(i).toString();


                if (str_type.equals("Guest Speaker")) {
                    speaker_et.setText("");
                    url_et.setText("");
                    intro_et.setText("");
                    til_speaker.setHint("");
                    til_url.setHint("");
                    til_intro.setHint("");
                    til_speaker.setHint("Name of Speaker");
                    til_url.setHint("Speaker URL/Link");
                    til_intro.setHint("Speaker Introduction");

                } else if (str_type.equals("Guest Artist")) {
                    speaker_et.setText("");
                    url_et.setText("");
                    intro_et.setText("");
                    til_speaker.setHint("");
                    til_url.setHint("");
                    til_intro.setHint("");
                    til_speaker.setHint("Name of Guest Artist");
                    til_url.setHint("Guest Artist URL/Link");
                    til_intro.setHint("Guest Artist Introduction");

                } else if (str_type.equals("Comedian")) {
                    speaker_et.setText("");
                    url_et.setText("");
                    intro_et.setText("");
                    til_speaker.setHint("");
                    til_url.setHint("");
                    til_intro.setHint("");
                    til_speaker.setHint("Name of Comedian");
                    til_url.setHint("Comedian URL/Link");
                    til_intro.setHint("Comedian Introduction");

                } else if (str_type.equals("Event Host")) {
                    speaker_et.setText("");
                    url_et.setText("");
                    intro_et.setText("");
                    til_speaker.setHint("");
                    til_url.setHint("");
                    til_intro.setHint("");
                    til_speaker.setHint("Name of Event Host");
                    til_url.setHint("Event Host URL/Link");
                    til_intro.setHint("Event Host Introduction");

                } else if (str_type.equals("Music Minister")) {
                    speaker_et.setText("");
                    url_et.setText("");
                    intro_et.setText("");
                    til_speaker.setHint("");
                    til_url.setHint("");
                    til_intro.setHint("");
                    til_speaker.setHint("Name of Music Minister");
                    til_url.setHint("Music Minister Host URL/Link");
                    til_intro.setHint("Music Minister Introduction");

                } else if (str_type.equals("Music Artist")) {
                    speaker_et.setText("");
                    url_et.setText("");
                    intro_et.setText("");
                    til_speaker.setHint("");
                    til_url.setHint("");
                    til_intro.setHint("");
                    til_speaker.setHint("Name of Music Artist");
                    til_url.setHint("Music Artist Host URL/Link");
                    til_intro.setHint("Music Artist Introduction");

                } else if (str_type.equals("Pre-Wedding Clips")) {
                    til_speaker.setHint("");
                    speaker_et.setText("");
                    url_et.setText("");
                    intro_et.setText("");
                    til_url.setHint("");
                    til_intro.setHint("");
                    til_speaker.setHint("Name of Pre-Wedding Clips");
                    til_url.setHint("Pre-Wedding Clips Host URL/Link");
                    til_intro.setHint("Pre-Wedding Clips Introduction");
                } else if (str_type.equals("Pre-Birthday Clips")) {
                    speaker_et.setText("");
                    url_et.setText("");
                    intro_et.setText("");
                    til_speaker.setHint("");
                    til_url.setHint("");
                    til_intro.setHint("");
                    til_speaker.setHint("Name of Pre-Birthday Clips");
                    til_url.setHint("Pre-Birthday Clips Host URL/Link");
                    til_intro.setHint("Pre-Birthday Clips Introduction");
                } else if (str_type.equals("Past Event Clips")) {
                    speaker_et.setText("");
                    url_et.setText("");
                    intro_et.setText("");
                    til_speaker.setHint("");
                    til_url.setHint("");
                    til_intro.setHint("");
                    til_speaker.setHint("Name of Past Event Clips");
                    til_url.setHint("Past Event Clips Host URL/Link");
                    til_intro.setHint("Past Event Clips Introduction");
                } else if (str_type.equals("Pre-Birthday Clips")) {
                    speaker_et.setText("");
                    url_et.setText("");
                    intro_et.setText("");
                    til_speaker.setHint("");
                    til_url.setHint("");
                    til_intro.setHint("");
                    til_speaker.setHint("Name of Pre-Birthday Clips");
                    til_url.setHint("Pre-Birthday Clips Host URL/Link");
                    til_intro.setHint("Pre-Birthday Clips Introduction");
                } else {
                    speaker_et.setText("");
                    url_et.setText("");
                    intro_et.setText("");
                    til_speaker.setHint("");
                    til_url.setHint("");
                    til_intro.setHint("");
                    til_speaker.setHint("Name of Event Guest type");
                    til_url.setHint("Event Guest URL/Link");
                    til_intro.setHint("Event Guest Introduction");

                }

            }
        });
        materialBetterSpinner1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FontsOverride.overrideFonts(getContext(), view);
                str_type = adapterView.getItemAtPosition(i).toString();

                Log.e("tag", "111111111-------------->" + str_type);

                if (str_type.equals("Guest Speaker")) {
                    speaker1_et.setText("");
                    url1_et.setText("");
                    intro1_et.setText("");
                    til_speaker1.setHint("");
                    til_url.setHint("");
                    til_intro1.setHint("");
                    til_speaker1.setHint("Name of Speaker");
                    til_url1.setHint("Speaker URL/Link");
                    til_intro1.setHint("Speaker Introduction");

                } else if (str_type.equals("Guest Artist")) {
                    speaker1_et.setText("");
                    url1_et.setText("");
                    intro1_et.setText("");
                    til_speaker1.setHint("");
                    til_url.setHint("");
                    til_intro1.setHint("");
                    til_speaker1.setHint("Name of Guest Artist");
                    til_url1.setHint("Guest Artist URL/Link");
                    til_intro1.setHint("Guest Artist Introduction");

                } else if (str_type.equals("Comedian")) {
                    speaker1_et.setText("");
                    url1_et.setText("");
                    intro1_et.setText("");
                    til_speaker1.setHint("");
                    til_url.setHint("");
                    til_intro1.setHint("");
                    til_speaker1.setHint("Name of Comedian");
                    til_url1.setHint("Comedian URL/Link");
                    til_intro1.setHint("Comedian Introduction");

                } else if (str_type.equals("Event Host")) {
                    speaker1_et.setText("");
                    url1_et.setText("");
                    intro1_et.setText("");
                    til_speaker1.setHint("");
                    til_url.setHint("");
                    til_intro1.setHint("");
                    til_speaker1.setHint("Name of Event Host");
                    til_url1.setHint("Event Host URL/Link");
                    til_intro1.setHint("Event Host Introduction");

                } else if (str_type.equals("Music Minister")) {
                    speaker1_et.setText("");
                    url1_et.setText("");
                    intro1_et.setText("");
                    til_speaker1.setHint("");
                    til_url.setHint("");
                    til_intro1.setHint("");
                    til_speaker1.setHint("Name of Music Minister");
                    til_url1.setHint("Music Minister Host URL/Link");
                    til_intro1.setHint("Music Minister Introduction");

                } else if (str_type.equals("Music Artist")) {
                    speaker1_et.setText("");
                    url1_et.setText("");
                    intro1_et.setText("");
                    til_speaker1.setHint("");
                    til_url.setHint("");
                    til_intro1.setHint("");
                    til_speaker1.setHint("Name of Music Artist");
                    til_url1.setHint("Music Artist Host URL/Link");
                    til_intro1.setHint("Music Artist Introduction");

                } else if (str_type.equals("Pre-Wedding Clips")) {
                    speaker1_et.setText("");
                    url1_et.setText("");
                    intro1_et.setText("");
                    til_speaker1.setHint("");
                    til_url.setHint("");
                    til_intro1.setHint("");
                    til_speaker1.setHint("Name of Pre-Wedding Clips");
                    til_url1.setHint("Pre-Wedding Clips Host URL/Link");
                    til_intro1.setHint("Pre-Wedding Clips Introduction");
                } else if (str_type.equals("Pre-Birthday Clips")) {
                    speaker1_et.setText("");
                    url1_et.setText("");
                    intro1_et.setText("");
                    til_speaker1.setHint("");
                    til_url.setHint("");
                    til_intro1.setHint("");
                    til_speaker1.setHint("Name of Pre-Birthday Clips");
                    til_url1.setHint("Pre-Birthday Clips Host URL/Link");
                    til_intro1.setHint("Pre-Birthday Clips Introduction");
                } else if (str_type.equals("Past Event Clips")) {
                    speaker1_et.setText("");
                    url1_et.setText("");
                    intro1_et.setText("");
                    til_speaker1.setHint("");
                    til_url.setHint("");
                    til_intro1.setHint("");
                    til_speaker1.setHint("Name of Past Event Clips");
                    til_url1.setHint("Past Event Clips Host URL/Link");
                    til_intro1.setHint("Past Event Clips Introduction");
                } else if (str_type.equals("Pre-Birthday Clips")) {
                    speaker1_et.setText("");
                    url1_et.setText("");
                    intro1_et.setText("");
                    til_speaker1.setHint("");
                    til_url.setHint("");
                    til_intro1.setHint("");
                    til_speaker1.setHint("Name of Pre-Birthday Clips");
                    til_url1.setHint("Pre-Birthday Clips Host URL/Link");
                    til_intro1.setHint("Pre-Birthday Clips Introduction");
                } else {
                    speaker1_et.setText("");
                    url1_et.setText("");
                    intro1_et.setText("");
                    til_speaker1.setHint("");
                    til_url.setHint("");
                    til_intro1.setHint("");
                    til_speaker1.setHint("Name of Event Guest type");
                    til_url1.setHint("Event Guest URL/Link");
                    til_intro1.setHint("Event Guest Introduction");

                }

            }
        });
        framehighlight1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //pickIntent.setType("*/*");
                    pickIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*"});
                    startActivityForResult(pickIntent, INTENT_REQUEST_GET_HIGHLIGHT1);
                }

            }
        });



        ///----------------------------------------------------------------//

        framehighlight11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //pickIntent.setType("*/*");
                    pickIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*"});
                    startActivityForResult(pickIntent, INTENT_REQUEST_GET_HIGHLIGHT11);
                }

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
            if (requestCode == INTENT_REQUEST_GET_HIGHLIGHT1) {
                Uri selectedMediaUri = data.getData();

                eventhighlights1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    // Log.d(TAG, String.valueOf(bitmap));
                    highl1_iv.setImageBitmap(bitmap);
                    h1plus.setVisibility(View.GONE);
                    Log.e("tag", "code------------->" + eventhighlights1);
                    edit.putString("eventhighlights1", eventhighlights1);
                    edit.commit();
                    eventData.eventhighlight1 = eventhighlights1;
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (requestCode == INTENT_REQUEST_GET_HIGHLIGHT11) {
                Uri selectedMediaUri = data.getData();

                eventhighlights2 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    // Log.d(TAG, String.valueOf(bitmap));
                    high13_iv.setImageBitmap(bitmap);
                    //..h11plus.setVisibility(View.GONE);
                    Log.e("tag", "code------------->" + eventhighlights2);
                    edit.putString("eventhighlights2", eventhighlights2);
                    edit.commit();
                    eventData.eventhighlight11 = eventhighlights2;
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (requestCode == INTENT_REQUEST_GET_HIGHLIGHT12) {

                Uri selectedMediaUri = data.getData();
                Log.d("tag", "567231546" + selectedMediaUri);
                eventhighlightsvideo2 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
               //.. high14_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(eventhighlightsvideo2, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                //..h12plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));
                edit.putString("highlight2_status", "video");
                edit.putString("eventhighlightsvideo2", eventhighlightsvideo2);
                edit.commit();
                eventData.eventhighlight12 = eventhighlightsvideo2;

            } else {
                Uri selectedMediaUri = data.getData();
                Log.d("tag", "567231546" + selectedMediaUri);
                eventhighlightsvideo1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
               //.. highl2_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(eventhighlightsvideo1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                h2plus.setImageDrawable(getActivity().getDrawable(vineture.wowhubb.R.drawable.video_icon));
                edit.putString("highlight2_status", "video");
                edit.putString("eventhighlightsvideo1", eventhighlightsvideo1);
                edit.commit();
                eventData.eventhighlight2 = eventhighlightsvideo1;
            }


        }

    }

    @Override
    public void onPause() {
        super.onPause();
        setData();
    }

    private void setData() {
        eventData.gusetname = speaker_et.getText().toString();
        eventData.guesturl = url_et.getText().toString().trim();
        eventData.guestintro = intro_et.getText().toString().trim();
        eventData.eventhighlight1 = eventhighlights1;
        eventData.eventhighlight2 = eventhighlightsvideo1;
        eventData.gusetname = speaker1_et.getText().toString();
        eventData.guesturl = url1_et.getText().toString().trim();
        eventData.guestintro = intro1_et.getText().toString().trim();
        eventData.eventhighlight11 = eventhighlights2;
        eventData.eventhighlight12 = eventhighlightsvideo2;
    }

    @SuppressLint("StaticFieldLeak")
    public class getWowtag extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json, jsonStr = "";
            try {
                // Log.e("tag", "section_str" + section_str);
                JSONObject jsonObject = new JSONObject();
                // jsonObject.accumulate("regulation", str_regulation);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/getwowtag", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "placeorderrr" + s);
            //  dialog.dismiss();


            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        JSONArray message = jo.getJSONArray("message");

                        if (message.length() > 0) {

                            map1 = new HashSet<>();

                            for (int i = 0; i < message.length(); i++) {
                                try {
                                    JSONObject jsonObject = message.getJSONObject(i);
                                    String wowtag = jsonObject.getString("wowtagid");
                                    map1.add(wowtag);
                                    ar_sectionno.add(wowtag);
                                } catch (JSONException e) {
                                    Log.e("tag","exc"+e.toString());
                                }

                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                    getActivity(), android.R.layout.simple_dropdown_item_1line, ar_sectionno);
                            wowtag.setAdapter(adapter);
                            wowtag.setThreshold(0);
                            // offence_spn.setVisibility(View.VISIBLE);


                        } else

                        {
                            Log.e("tag", "12132123");
                            String msg = jo.getString("message");
                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                }
            }


        }

    }
}