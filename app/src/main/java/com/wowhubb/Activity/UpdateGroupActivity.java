package com.wowhubb.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pchmn.materialchips.ChipsInput;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.wowhubb.Adapter.CustomAdapter;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.Fragment.ContactChip;
import com.wowhubb.R;
import com.wowhubb.Utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Salman on 21-02-2018.
 */

public class UpdateGroupActivity extends Activity {
    public static MaterialBetterSpinner privacy_spn;
    TextView backtv;
    String[] PRIVACYLIST = {"Public Group", "Closed Group", "Secret Group"};
    Typeface lato;
    SharedPreferences.Editor editor;
    String token, privacy_str,groupid,groupprivacy;
    TextInputLayout groupname_til, people_til, privacy_til;
    List<ContactChip> contactList;
    ChipsInput chipsInput;
    JSONArray jsonArray;
    TextView createtv;
    EditText groupname_et;
    Dialog loader_dialog;
    CustomAdapter eventdayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creategroupfragment);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(UpdateGroupActivity.this, v1);
        lato = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        backtv = findViewById(R.id.backtv);

        Bundle b = new Bundle();
        b = getIntent().getExtras();
        String name = b.getString("groupname");
        groupid = b.getString("groupid");
        groupprivacy=b.getString("groupprivacy");

        backtv.setText("Edit Group");
        backtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(UpdateGroupActivity.this);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");
        contactList = new ArrayList<>();
        Log.e("tag", token);

        privacy_spn = (MaterialBetterSpinner) findViewById(R.id.privacy_spn);
        groupname_til = findViewById(R.id.til_groupname);
        people_til = findViewById(R.id.til_people);
     //   privacy_til = findViewById(R.id.til_selectprivacy);
        createtv = findViewById(R.id.createtv);
        groupname_et = findViewById(R.id.groupname_et);
        groupname_et.setText(name);
        createtv.setText("Update");
        groupname_et.requestFocus();

        View view1 = privacy_spn.getRootView();


     /*   eventdayAdapter = new CustomAdapter(UpdateGroupActivity.this, android.R.layout.simple_dropdown_item_1line, PRIVACYLIST) {
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
        privacy_spn.setAdapter(eventdayAdapter);*/
        privacy_spn.setTypeface(lato);
        groupname_til.setTypeface(lato);
        people_til.setTypeface(lato);
     //   privacy_til.setTypeface(lato);


        loader_dialog = new Dialog(UpdateGroupActivity.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);

        new getNetworks().execute();
        if(groupprivacy!=null)

        {
            if (groupprivacy.equals("Public Group")) {
                String[] PRIVACYLIST = {"Public Group", "Closed Group", "Secret Group"};
                eventdayAdapter = new CustomAdapter(UpdateGroupActivity.this, android.R.layout.simple_dropdown_item_1line, PRIVACYLIST) {
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
                privacy_spn.setAdapter(eventdayAdapter);

            } else if (groupprivacy.equals("Closed Group")) {
                String[] PRIVACYLIST = {"Closed Group", "Public Group", "Secret Group"};
                eventdayAdapter = new CustomAdapter(UpdateGroupActivity.this, android.R.layout.simple_dropdown_item_1line, PRIVACYLIST) {
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
                privacy_spn.setAdapter(eventdayAdapter);
                privacy_spn.setSelected(true);
                eventdayAdapter.notifyDataSetChanged();

            } else if (groupprivacy.equals("Secret Group")) {
                String[] PRIVACYLIST = {"Secret Group", "Closed Group", "Public Group"};
                eventdayAdapter = new CustomAdapter(UpdateGroupActivity.this, android.R.layout.simple_dropdown_item_1line, PRIVACYLIST) {
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
                privacy_spn.setAdapter(eventdayAdapter);
            }
        }
        else
        {
            String[] PRIVACYLIST = {"Public Group","Closed Group","Secret Group"};
            eventdayAdapter = new CustomAdapter(UpdateGroupActivity.this, android.R.layout.simple_dropdown_item_1line, PRIVACYLIST) {
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
            privacy_spn.setAdapter(eventdayAdapter);
        }

        Log.e("tag", "tagqqqqqqqqqq");
        privacy_spn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FontsOverride.overrideFonts(UpdateGroupActivity.this, view);
                privacy_str = adapterView.getItemAtPosition(i).toString();
                Log.e("tag", "str_category------>" + privacy_str);

            }
        });

        createtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactChip item = new ContactChip();
                List<ContactChip> contactsSelected = (List<ContactChip>) chipsInput.getSelectedChipList();
                Log.e("tag", "stridddd------>" + contactsSelected.size());
                jsonArray = new JSONArray();
                for (int i = 0; i < contactsSelected.size(); i++) {
                    Log.e("tag", "stridddd12222------>" + contactsSelected.get(i).getId());
                    jsonArray.put(contactsSelected.get(i).getId());
                }

                Log.e("tag", "stridddd12222------>" + jsonArray.toString());

                if (!groupname_et.getText().toString().trim().equalsIgnoreCase("")) {
                    groupname_til.setError(null);
                    if (contactsSelected.size() > 0) {
                        new updateGroup().execute();
                    } else {
                        //chipsInput.setFocusable(true);
                        SpannableString s = new SpannableString("Atleat Choose One More Peoples");
                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        Toast.makeText(UpdateGroupActivity.this, s, Toast.LENGTH_LONG).show();
                    }
                } else {
                    SpannableString s = new SpannableString("Enter Group Name");
                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    groupname_til.setError(s);
                    groupname_et.setFocusable(true);
                    groupname_et.requestFocus();

                }


            }
        });


        chipsInput = (ChipsInput) findViewById(R.id.chips_input);
        chipsInput.setFocusable(false);

        chipsInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class getNetworks extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("search", "");

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/network/friendsuggestion", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tagqqqqqqqqqq" + s);


            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    try {

                        JSONArray feedArray = jo.getJSONArray("message");

                        for (int i = 0; i < feedArray.length(); i++) {
                            ContactChip item = new ContactChip();
                            JSONObject feedObj = (JSONObject) feedArray.get(i);


                            String friendid = feedObj.getString("_id");
                            String friendname = feedObj.getString("firstname");
                            String wowtagid = feedObj.getString("wowtagid");
                            String friendstatus = feedObj.getString("status");
                            String mutualfriends = feedObj.getString("mutualfriendscount");
                            item.setId(friendid);
                            item.setName(friendname);
                            item.setPhoneNumber(wowtagid);

                            contactList.add(item);
                            chipsInput.setFilterableList(contactList);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                }
            } else {

            }

        }

    }

    public class updateGroup extends AsyncTask<String, Void, String> {
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
              //  groupid, groupname, privacy
                jsonObject.accumulate("groupid", groupid);
                jsonObject.accumulate("groupname", groupname_et.getText().toString());
                //jsonObject.accumulate("users", jsonArray);
                Log.e("tag", "tagqqqqqqqqqq" + jsonObject.toString());
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/group/editgroup", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loader_dialog.dismiss();

            if (s != null) {
                startActivity(new Intent(UpdateGroupActivity.this, CreateGroup.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

            } else {


            }

        }

    }


}
