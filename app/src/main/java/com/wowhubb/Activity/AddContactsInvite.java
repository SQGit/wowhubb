package com.wowhubb.Activity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.wowhubb.Adapter.InviteContactsRecyclerAdapter;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;
import com.wowhubb.Utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import Contacts.Contact;
import Contacts.InviteContactsAdapter;

import static android.view.View.OnClickListener;
import static android.view.View.OnFocusChangeListener;

/**
 * Created by Salman on 30-03-2018.
 */

public class AddContactsInvite extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ArrayList<Contact> listContacts;
    List<Contact> resultContacts;
    RecyclerView lvContacts;
    InviteContactsAdapter adapterContacts;
    TextView backtv, donetv;
    ArrayList<String> alContacts;
    SharedPreferences.Editor editor;
    Dialog dialog, loader_dialog;
    SharedPreferences sharedPrefces;
    String token, eventId, feedstatus;
    ArrayList<Contact> newlistContacts;
    ArrayList<Contact> filtercontactsList;
    HashMap<String, Contact> hashMap;
    Contact contact;
    InviteContactsRecyclerAdapter inviteContactsRecyclerAdapter;
   public static MenuItem doneItem;
   String locale,CountryZipCode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontactinvite_listview);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        locale = tm.getSimCountryIso().toUpperCase();


        GetCountryZipCode();



        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(AddContactsInvite.this);
        editor = sharedPrefces.edit();
        token = sharedPrefces.getString("token", "");
        eventId = sharedPrefces.getString("eventId", "");
        feedstatus = sharedPrefces.getString("feedstatus", "");

        View view = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(AddContactsInvite.this, view);
        filtercontactsList = new ArrayList<>();
        listContacts = new ArrayList<>();
        newlistContacts = new ArrayList<>();
        resultContacts = new ArrayList<>();

        lvContacts = (RecyclerView) findViewById(R.id.lvContacts);
        loader_dialog = new Dialog(AddContactsInvite.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);

        Collections.sort(newlistContacts, new Comparator()
        {
            public int compare(Object o1, Object o2) {
                Contact p1 = (Contact) o1;
                Contact p2 = (Contact) o2;
                return p1.getName().compareToIgnoreCase(p2.getName());
            }
        });


        inviteContactsRecyclerAdapter = new InviteContactsRecyclerAdapter(newlistContacts, filtercontactsList, AddContactsInvite.this);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(AddContactsInvite.this, LinearLayoutManager.VERTICAL, false);
        lvContacts.setLayoutManager(linearLayoutManager);
        lvContacts.setItemAnimator(new DefaultItemAnimator());
        lvContacts.setAdapter(inviteContactsRecyclerAdapter);


        ContentResolver cr = getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            alContacts = new ArrayList<String>();

            do {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String contactId = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (contactNumber.startsWith("+")) {
                            contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        } else {
                            contactNumber = "+"+CountryZipCode + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        String contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                        if (contactNumber.length() > 9) {
                            Contact contact = new Contact(contactId, contactName, contactNumber);
                            alContacts.add(contactNumber);
                            listContacts.add(contact);
                        }
                        //   newlistContacts.add(contact);
                        hashMap = new HashMap<>();
                        for (int i = 0; i < listContacts.size(); i++) {
                            contact = listContacts.get(i);
                            hashMap.put(contact.getId(), contact);
                        }

                        break;
                    }
                    pCur.close();
                }

            } while (cursor.moveToNext());
        }

        Log.e("tag", "!23------>>>" + alContacts);

        new checkingcontactswithphone().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home1, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        doneItem = menu.findItem(R.id.action_done);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                inviteContactsRecyclerAdapter.filter(searchQuery.toString().trim());
                lvContacts.invalidate();
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                Intent intent = new Intent(AddContactsInvite.this, EventInviteActivity.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.action_search:
                return true;

            case R.id.action_done:
                Log.e("tag", "eventId------------------>>>>" + eventId);
                new sms_invite(eventId).execute();

                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Contact> filteredModelList = filter(listContacts, newText);
        adapterContacts.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    private List<Contact> filter(List<Contact> models, String query) {
        query = query.toLowerCase();

        final List<Contact> filteredModelList = new ArrayList<>();
        for (Contact model : models) {
            final String text = model.name;
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    public class checkingcontactswithphone extends AsyncTask<String, Void, String> {
        String eventid;

        public checkingcontactswithphone() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   loader_dialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {

                JSONObject jo = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                Log.e("tag", "LISTCONTACTSSSSS---------->" + listContacts.size());
                if (listContacts.size() > 0) {
                    //   Log.e("tag", "LISTCONTACTSSSSS---------->" + listContacts);
                    for (int i = 0; i < listContacts.size(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("contactname", listContacts.get(i).getName());
                        jsonObject.put("phone", listContacts.get(i).getPhoneno());
                        jsonObject.put("id", listContacts.get(i).getId());

                        jsonArray.put(jsonObject);
                        Log.e("tag", "LISTCONTACTSSSSS---------->" + listContacts.get(1).getPhoneno());
                    }
                    jo.put("phonewithid", jsonArray);

                    Log.e("tag", "LISTCONTACTSSSSS---------->" + jo.toString());
                    json = jo.toString();
                    return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/group/fetchphonewithwowtag", json, token);
                }
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //  loader_dialog.dismiss();
            Log.e("tag", "RESULTTTTTTTTT------------" + s);

            if (s != null) {
                try {

                    JSONObject jo = new JSONObject(s);
                    if (jo.has("success")) {
                        String status = jo.getString("success");
                        if (status.equals("true")) {
                            JSONArray feedArray = jo.getJSONArray("message");
                            Log.e("tag", "feedArray------------------>>>>" + feedArray);
                            ///   listContacts.clear();
                            for (int i = 0; i < feedArray.length(); i++) {
                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                //  Contact contact = new Contact();
                                //String phone = feedObj.getString("phone");
                                String phone = feedObj.getString("phone");
                                String id = feedObj.getString("id");
                                String wowtagid = feedObj.getString("wowtagid");
                                String name = feedObj.getString("contactname");
                                Log.e("tag", "PHONE------------------>>>>" + id + phone);
                                Contact contact = new Contact(id, name, phone, wowtagid);
                                newlistContacts.add(contact);

                                filtercontactsList.add(contact);

                                Collections.sort(newlistContacts, new Comparator() {

                                    public int compare(Object o1, Object o2) {
                                        Contact p1 = (Contact) o1;
                                        Contact p2 = (Contact) o2;
                                        return p1.getName().compareToIgnoreCase(p2.getName());
                                    }

                                });
                                Collections.sort(filtercontactsList, new Comparator() {

                                    public int compare(Object o1, Object o2) {
                                        Contact p1 = (Contact) o1;
                                        Contact p2 = (Contact) o2;
                                        return p1.getName().compareToIgnoreCase(p2.getName());
                                    }

                                });
                                inviteContactsRecyclerAdapter.notifyDataSetChanged();
                            }

                        }
                    } else {
                        String msg = jo.getString("message");
                        //Toast.makeText(AddContactsInvite.this, msg, Toast.LENGTH_LONG).show();
                    }

                   /* Log.e("tag", "111111------------------>>>>" + listContacts.size());
                    for (int i = 0; i < resultContacts.size(); i++) {
                        //Contact c=listContacts.get(i);
                        // hashMap.put(c.getId(),c);
                        newlistContacts.add(hashMap.get(resultContacts.get(i).getId()));
                      //  Contact contact = new Contact(resultContacts.get(i).getId(), resultContacts.get(i).getName(), resultContacts.get(i).getPhoneno());
                      //  listContacts.add(contact);
                    }*/


                    //  adapterContacts = new InviteContactsAdapter(AddContactsInvite.this, listContacts);
                    //    lvContacts.setAdapter(adapterContacts);

                  /*  InviteContactsRecyclerAdapter inviteContactsRecyclerAdapter = new InviteContactsRecyclerAdapter(listContacts,AddContactsInvite.this);
                    RecyclerView.LayoutManager  linearLayoutManager = new LinearLayoutManager(AddContactsInvite.this, LinearLayoutManager.VERTICAL, false);
                    lvContacts.setLayoutManager(linearLayoutManager);
                    lvContacts.setItemAnimator(new DefaultItemAnimator());
                    lvContacts.setAdapter(inviteContactsRecyclerAdapter);*/
                    //inviteContactsRecyclerAdapter.notifyDataSetChanged();
                    Log.e("tag", "contact list------------------>>>>" + newlistContacts.size());
                } catch (JSONException e) {

                } catch (NullPointerException e) {

                }

            } else {

            }

        }

    }

    public class sms_invite extends AsyncTask<String, Void, String> {
        String eventid, code;

        public sms_invite(String eventid) {
            this.eventid = eventid;
            this.code = code;

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

                Log.e("tag", "111111111SSSIZEEEEEE------->>" + InviteContactsRecyclerAdapter.selectedcontacts.size());


                if (InviteContactsRecyclerAdapter.selectedcontacts.size() > 0) {
                    for (int i = 0; i < InviteContactsRecyclerAdapter.selectedcontacts.size(); i++) {
                        jsonObject.accumulate("phone", InviteContactsRecyclerAdapter.selectedcontacts.get(i));
                    }
                }
                jsonObject.accumulate("eventid", eventid);


                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/smsinvite", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loader_dialog.dismiss();

            Log.e("tag", "aaaaaaaaashjdh------------" + s);

            if (s != null) {
                try {

                    JSONObject jo = new JSONObject(s);
                    if (jo.has("success")) {
                        String status = jo.getString("success");
                        if (status.equals("true")) {
                            String msg = jo.getString("message");
                            Toast.makeText(AddContactsInvite.this, msg, Toast.LENGTH_LONG).show();

                            if (feedstatus.equals("myevents")) {
                                Intent intent = new Intent(AddContactsInvite.this, MyEventFeedsActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                finish();
                            } else {
                                Intent intent = new Intent(AddContactsInvite.this, EventFeedDashboard.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                finish();
                            }


                        }
                    } else {
                        String msg = jo.getString("message");
                        Toast.makeText(AddContactsInvite.this, msg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {

                } catch (NullPointerException e) {

                }

            } else {

            }

        }


    }
    private void GetCountryZipCode() {


        String CountryID = "";
         CountryZipCode = "";

       /* TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID= manager.getSimCountryIso().toUpperCase();
        Log.e("tag","aadhav1"+CountryID);*/

        String[] rl = this.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(locale.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        Log.e("tag", "aadhav2" + CountryZipCode);
    }

}
