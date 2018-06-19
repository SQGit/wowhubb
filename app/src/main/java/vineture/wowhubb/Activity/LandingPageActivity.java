package vineture.wowhubb.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import vineture.wowhubb.EventServiceProvider.Activity.EventServiceProviderActivity;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.Nearbyeventsmodule.Activity.NearbyCategoryActivity;
import vineture.wowhubb.R;
import vineture.wowhubb.ShareBookEvents.Activity.SearchBookMainActivity;
import vineture.wowhubb.Utils.Config;
import vineture.wowhubb.Utils.HttpUtils;

import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static vineture.wowhubb.R.color.colorPrimary;


/**
 * Created by Salman on 05-10-2017.
 */

public class LandingPageActivity extends Activity {

    public static Dialog create_dialog;
    TextView viewfeed;
    LinearLayout createevents_lv, admin_lv, searchevents_lv, nearby_lv, searchproviders_lv;
    TextView tv_snack;
    Snackbar snackbar;
    Bundle extras;
    String fabstatus, token, selected_name, selected_id;
    TextInputLayout til_wowtag;
    SharedPreferences.Editor editor;
    Map<String, String> wowtag_hash = new HashMap<>();
    Set<String> keys;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter;
    TextView txt_search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Typeface segoeui = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/segoeui.ttf");
        final View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(LandingPageActivity.this, v1);

        viewfeed = findViewById(R.id.vieweventfeed);
        createevents_lv = findViewById(R.id.createevents_lv);
        searchevents_lv = findViewById(R.id.searchevents_lv);
        nearby_lv = findViewById(R.id.nearby_lv);
        searchproviders_lv = findViewById(R.id.searchproviders_lv);
        //til_wowtag = findViewById(R.id.til_wowtag);
        txt_search = findViewById(R.id.txt_search);

        til_wowtag = findViewById(R.id.til_wowtag);

        //til_wowtag.setTypeface(segoeui);

        create_dialog = new BottomSheetDialog(LandingPageActivity.this);
        create_dialog.setContentView(R.layout.dialog_createevent);
        create_dialog.setCancelable(true);
        View v = create_dialog.getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(LandingPageActivity.this, v);
        //til_wowtag.setTypeface(lato);


        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");

        new callWowtag().execute();


        Log.e("tag", "FABBBB STATUSS1111111----" + extras);
        extras = getIntent().getExtras();
        if (extras != null) {
            fabstatus = extras.getString("fabstatus");
            Log.e("tag", "FABBBB STATUSS----" + fabstatus);
            if (fabstatus.equals("true")) {
                create_dialog.show();
            }
        }
        searchevents_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LandingPageActivity.this, SearchBookMainActivity.class));
            }
        });


        //-----------------------------------------SNACKBAR----------------------------------------//
        snackbar = Snackbar.make(findViewById(R.id.top), R.string.underdev, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(RED);
        View sbView = snackbar.getView();
        tv_snack = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(WHITE);
        tv_snack.setTypeface(segoeui);
        viewfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    Intent intent = new Intent(LandingPageActivity.this, EventFeedDashboard.class);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });

        final LinearLayout ll1 = create_dialog.findViewById(R.id.ll1);
        final LinearLayout ll2 = create_dialog.findViewById(R.id.ll2);
        final TextView quickeventtv = create_dialog.findViewById(R.id.quicktv);
        final TextView generaleventtv = create_dialog.findViewById(R.id.generaleventtv);
        final ImageView quickiv = create_dialog.findViewById(R.id.quickiv);
        final ImageView generaliv = create_dialog.findViewById(R.id.generaleventiv);
        final ImageView quickhintiv = create_dialog.findViewById(R.id.quickhint_icon);
        final ImageView generalhintiv = create_dialog.findViewById(R.id.generalhint_icon);


        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent wow_front = new Intent(getApplicationContext(), LandingWowSearch.class);
                wow_front.putExtra("list_value", list);
                wow_front.putExtra("hashmap", (Serializable) wowtag_hash);
                startActivity(wow_front);
                finish();
            }
        });


        quickhintiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_dialog = new Dialog(LandingPageActivity.this);
                create_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                create_dialog.setContentView(R.layout.dialog_quickflashhint);
                create_dialog.setCanceledOnTouchOutside(true);

                //Max Screen width
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(create_dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                create_dialog.getWindow().setAttributes(lp);
                create_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) create_dialog).getWindow();
                        view1.setBackgroundDrawableResource(R.drawable.border_graybg);
                    }
                });
                View v1 = create_dialog.getWindow().getDecorView().getRootView();
                FontsOverride.overrideFonts(LandingPageActivity.this, v1);
                create_dialog.show();

                TextView oktv = create_dialog.findViewById(R.id.oktv);
                TextView canceltv = create_dialog.findViewById(R.id.canceltv);

                oktv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        create_dialog.dismiss();
                    }
                });
                canceltv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        create_dialog.dismiss();
                    }
                });
            }
        });


        generalhintiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog d = new Dialog(LandingPageActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.dialog_generalflashhint);
                d.setCanceledOnTouchOutside(true);

                //Max Screen width
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(d.getWindow().getAttributes());
               lp.width = WindowManager.LayoutParams.MATCH_PARENT;
               lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                d.getWindow().setAttributes(lp);
                d.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) d).getWindow();
                        view1.setBackgroundDrawableResource(R.drawable.border_graybg);
                    }
                });
                View v1 = d.getWindow().getDecorView().getRootView();
                FontsOverride.overrideFonts(LandingPageActivity.this, v1);
                d.show();

                TextView oktv = d.findViewById(R.id.oktv);
                TextView canceltv = d.findViewById(R.id.canceltv);

                oktv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
                canceltv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
            }
        });
        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    ll1.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinktriangle));
                    ll2.setBackgroundDrawable(getResources().getDrawable(R.color.white));
                    generaleventtv.setTextColor(getResources().getColor(R.color.dialoggrey));
                    quickeventtv.setTextColor(getResources().getColor(R.color.white));

                    quickhintiv.setColorFilter(quickhintiv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                    generalhintiv.setColorFilter(generalhintiv.getContext().getResources().getColor(R.color.dialoggrey), PorterDuff.Mode.SRC_ATOP);

                    generaliv.setColorFilter(generaliv.getContext().getResources().getColor(R.color.dialoggrey), PorterDuff.Mode.SRC_ATOP);
                    quickiv.setColorFilter(quickiv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                    Toast.makeText(LandingPageActivity.this, "Under Development", Toast.LENGTH_LONG).show();

                    startActivity(new Intent(LandingPageActivity.this, QuickCreateEvent.class));
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    create_dialog.dismiss();

                } else {
                    ll2.setBackground(getResources().getDrawable(R.color.white));
                    ll1.setBackground(getResources().getDrawable(R.drawable.pinktriangle));

                    quickhintiv.setColorFilter(quickhintiv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                    generalhintiv.setColorFilter(generalhintiv.getContext().getResources().getColor(R.color.dialoggrey), PorterDuff.Mode.SRC_ATOP);

                    quickeventtv.setTextColor(getResources().getColor(R.color.white));
                    generaliv.setColorFilter(generaliv.getContext().getResources().getColor(R.color.dialoggrey), PorterDuff.Mode.SRC_ATOP);

                    generaleventtv.setTextColor(getResources().getColor(R.color.dialoggrey));
                    quickiv.setColorFilter(quickiv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


                    startActivity(new Intent(LandingPageActivity.this, QuickCreateEvent.class));
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    create_dialog.dismiss();
                }

            }
        });


        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    ll1.setBackgroundDrawable(getResources().getDrawable(R.drawable.whitetriangle));
                    ll2.setBackgroundDrawable(getResources().getDrawable(colorPrimary));
                    generaliv.setColorFilter(generaliv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                    generaleventtv.setTextColor(getResources().getColor(R.color.white));
                    quickeventtv.setTextColor(getResources().getColor(R.color.dialoggrey));
                    quickiv.setColorFilter(quickiv.getContext().getResources().getColor(R.color.dialoggrey), PorterDuff.Mode.SRC_ATOP);
                    generalhintiv.setColorFilter(generalhintiv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                    quickhintiv.setColorFilter(quickhintiv.getContext().getResources().getColor(R.color.dialoggrey), PorterDuff.Mode.SRC_ATOP);

                    startActivity(new Intent(LandingPageActivity.this, CreateEventActivity.class));
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor edit = sharedPrefces.edit();
                    edit.remove("addmorestatus");
                    edit.commit();
                    create_dialog.dismiss();

                } else {
                    ll2.setBackground(getResources().getDrawable(colorPrimary));
                    ll1.setBackground(getResources().getDrawable(R.drawable.whitetriangle));
                    generaliv.setColorFilter(generaliv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                    generaleventtv.setTextColor(getResources().getColor(R.color.white));
                    quickeventtv.setTextColor(getResources().getColor(R.color.dialoggrey));
                    quickiv.setColorFilter(quickiv.getContext().getResources().getColor(R.color.dialoggrey), PorterDuff.Mode.SRC_ATOP);
                    generalhintiv.setColorFilter(generalhintiv.getContext().getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                    quickhintiv.setColorFilter(quickhintiv.getContext().getResources().getColor(R.color.dialoggrey), PorterDuff.Mode.SRC_ATOP);
                    create_dialog.dismiss();
                    startActivity(new Intent(LandingPageActivity.this, CreateEventActivity.class));
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor edit = sharedPrefces.edit();
                    edit.remove("addmorestatus");
                    edit.commit();
                    create_dialog.dismiss();
                }

            }
        });
        createevents_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_dialog.show();

            }
        });

        searchproviders_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LandingPageActivity.this, EventServiceProviderActivity.class));
            }
        });


        nearby_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LandingPageActivity.this, NearbyCategoryActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logout")
                .setMessage("Do You want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent setIntent = new Intent(Intent.ACTION_MAIN);
                        setIntent.addCategory(Intent.CATEGORY_HOME);
                        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(setIntent);


                        SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor edit = sharedPrefces.edit();
                        edit.remove("status");
                        edit.clear();
                        edit.commit();
                        finish();

                    }

                })
                .setNegativeButton("No", null)
                .show();

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(LandingPageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(LandingPageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(LandingPageActivity.this, Manifest.permission.CAMERA);
        int result3 = ContextCompat.checkSelfPermission(LandingPageActivity.this, Manifest.permission.READ_CONTACTS);
        //int result4= ContextCompat.checkSelfPermission(LandingPageActivity.this, Manifest.permission.RECORD_AUDIO);

        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED) && (result2 == PackageManager.PERMISSION_GRANTED) && (result3 == PackageManager.PERMISSION_GRANTED)) {


            return true;
        } else {
            ActivityCompat.requestPermissions(LandingPageActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS}, 1);

            return false;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1:

                if (grantResults.length > 0) {

                    boolean ReadStoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean CameraPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadPhoneStatePermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    if (ReadStoragePermission && CameraPermission && WriteStoragePermission && ReadPhoneStatePermission) {
                        Intent intent = new Intent(LandingPageActivity.this, EventFeedDashboard.class);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LandingPageActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    //describe wowtag names:
    private class callWowtag extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_GET_WOWTAGLIST, json, token);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonstr) {
            super.onPostExecute(jsonstr);

            if (jsonstr.equals("")) {
                Toast.makeText(getApplicationContext(), "Check Network Connection", Toast.LENGTH_LONG).show();
            } else {

                try {

                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("success");

                    if (status.equals("true")) {
                        JSONArray event_service = jo.getJSONArray("message");
                        if (event_service != null) {
                            wowtag_hash.clear();
                            for (int i1 = 0; i1 < event_service.length(); i1++) {
                                Log.e("tag", "LENGTH" + event_service.length());
                                JSONObject jsonObject = event_service.getJSONObject(i1);
                                if (jsonObject.has("_id")) {
                                    if (!jsonObject.getString("_id").equals("null")) {

                                        if (jsonObject.has("userid")) {

                                            if (!jsonObject.getString("userid").equals("null")) {
                                                if (jsonObject.has("eventtitle")) ;
                                                {
                                                    if (!jsonObject.getString("eventtitle").equals("null")) {
                                                        wowtag_hash.put(jsonObject.getString("eventtitle"), jsonObject.getString("_id"));
                                                    }
                                                }
                                            }

                                        }

                                    }

                                }

                            }

                            keys = wowtag_hash.keySet();
                            Log.e("tag", "print_Keys" + keys);
                            list = new ArrayList<>();
                            list.addAll(keys);
                            Log.e("tag", "print_List" + list);


                        } else {


                            //Toast.makeText(getActivity(),"No Wowtag Id Found..",Toast.LENGTH_LONG).show();
                        }

                    } else {

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
