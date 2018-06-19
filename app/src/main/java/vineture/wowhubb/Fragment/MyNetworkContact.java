package vineture.wowhubb.Fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vineture.wowhubb.Activity.AddContactsInvite;
import vineture.wowhubb.Adapter.InviteContactsRecyclerAdapter;
import vineture.wowhubb.Fonts.FontsOverride;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import Contacts.Contact;
import Contacts.ContactFetcher;
import Contacts.ContactsAdapter;
import vineture.wowhubb.Utils.HttpUtils;


public class MyNetworkContact extends Fragment implements SearchView.OnQueryTextListener{
    ArrayList<Contact> listContacts;
    private List<Contact> mCountryModel;
    RecyclerView lvContacts;
    ContactsAdapter adapterContacts;
    public static MyNetworkContact newInstance() {
        MyNetworkContact fragment = new MyNetworkContact();
        return fragment;
    }
    SharedPreferences.Editor editor;
    String token;
    ArrayList<Contact> newlistContacts;
    ArrayList<Contact> filtercontactsList;
    List<Contact> resultContacts;
    ArrayList<String> alContacts;
    HashMap<String, Contact> hashMap;
    Contact contact;
    String locale, CountryZipCode, eventMessage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(vineture.wowhubb.R.layout.addcontactinvite_listview, container, false);
        FontsOverride.overrideFonts(getActivity(), view);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");
        filtercontactsList = new ArrayList<>();
        listContacts = new ArrayList<>();
        newlistContacts = new ArrayList<>();
        resultContacts = new ArrayList<>();

        alContacts=new ArrayList<>();
        TelephonyManager tm = (TelephonyManager)getActivity(). getSystemService(Context.TELEPHONY_SERVICE);
        locale = tm.getSimCountryIso().toUpperCase();
        GetCountryZipCode();

      //  listContacts = new ContactFetcher(getActivity()).fetchAll();
        mCountryModel = new ArrayList<>();
        lvContacts = (RecyclerView)view .findViewById(vineture.wowhubb.R.id.lvContacts);
        //   adapterContacts = new ContactsAdapter(getActivity(), listContacts);
       // lvContacts.setAdapter(adapterContacts);
        Collections.sort(newlistContacts, new Comparator() {
            public int compare(Object o1, Object o2) {
                Contact p1 = (Contact) o1;
                Contact p2 = (Contact) o2;
                return p1.getName().compareToIgnoreCase(p2.getName());
            }
        });

        adapterContacts = new ContactsAdapter(newlistContacts, filtercontactsList, getActivity());
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        lvContacts.setLayoutManager(linearLayoutManager);
        lvContacts.setItemAnimator(new DefaultItemAnimator());
        lvContacts.setAdapter(adapterContacts);
        ContentResolver cr =getActivity(). getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            alContacts = new ArrayList<String>();
            do {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String contactId = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).trim();
                        contactNumber = contactNumber.replaceAll(" ", "");
                        Log.e("tag", "CONTACTTTNOOOO-----" + contactNumber);
                        if (contactNumber.startsWith("+")) {
                            contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            contactNumber = contactNumber.replaceAll(" ", "");
                        } else {
                            contactNumber = "+" + CountryZipCode + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            contactNumber = contactNumber.replaceAll(" ", "");
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
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(vineture.wowhubb.R.menu.menu_home, menu);

        final MenuItem item = menu.findItem(vineture.wowhubb.R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                       // adapterContacts.setFilter(listContacts);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Contact> filteredModelList = filter(listContacts, newText);
      //  adapterContacts.setFilter(filteredModelList);
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
                        jsonObject.put("phone", listContacts.get(i).getPhoneno().trim());
                        jsonObject.put("id", listContacts.get(i).getId());

                        jsonArray.put(jsonObject);
                        //  Log.e("tag", "LISTCONTACTSSSSS---------->" + listContacts.get(1).getPhoneno());
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
                                adapterContacts.notifyDataSetChanged();
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

    private void GetCountryZipCode() {
        String CountryID = "";
        CountryZipCode = "";
        /* TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID= manager.getSimCountryIso().toUpperCase();
        Log.e("tag","aadhav1"+CountryID);*/
        String[] rl = this.getResources().getStringArray(vineture.wowhubb.R.array.CountryCodes);
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
