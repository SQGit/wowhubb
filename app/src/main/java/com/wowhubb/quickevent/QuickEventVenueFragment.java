package com.wowhubb.quickevent;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;
import com.wowhubb.Activity.CreateEventActivity;
import com.wowhubb.Adapter.ExpandableListAdapter;
import com.wowhubb.Adapter.ExpandableListDataPump;
import com.wowhubb.Adapter.ViewVenuesRecyclerAdapter;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.Fragment.EventTypeFragment;
import com.wowhubb.R;
import com.wowhubb.data.AddressVenue;
import com.wowhubb.data.EventData;
import com.wowhubb.data.Faq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sql.DatabaseHelper;

public class QuickEventVenueFragment extends Fragment {


    public static final String DATABASE_NAME = "venuedatabase";
    public static TextInputLayout till_address, till_city, till_state, til_zipcode;
    public static TextInputLayout til_eventname;
    public static String checkboxstatus;
    public static EditText venuename, address, city, state, zipcode;
    public static Dialog dialog;
    public static ArrayList<AddressVenue> listBeneficiary;
    public static ArrayList<String> straddress_list;
    public static DatabaseHelper databaseHelper;
    public static Snackbar snackbar;
    private static AddressVenue beneficiary;
    public EventData eventData;
    TextView helpfultips_tv;
    Typeface lato;
    TextView addeventfaq_tv, viewallvenue, addmoreevent_tv;
    LinearLayout lnr_faq;
    EditText faq_question, faq_answer;
    TextView save_tv_faq, addmore_tv;
    FAQAdapter faqAdapter;
    ListView faq_listview;
    ImageView del_faq;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    TextView tv_snack;
    private RecyclerView recyclerViewBeneficiary;
    private ViewVenuesRecyclerAdapter viewVenuesRecyclerAdapter;
    private List<Faq> faqList = new ArrayList<Faq>();

    public static QuickEventVenueFragment newInstance(int page, boolean isLast) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        if (isLast)
            args.putBoolean("isLast", true);
        final QuickEventVenueFragment fragment = new QuickEventVenueFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static void postDataToSQLite() {
        beneficiary.setVenuname(venuename.getText().toString().trim());
        beneficiary.setCity(city.getText().toString().trim());
        beneficiary.setState(state.getText().toString().trim());
        beneficiary.setAddress(address.getText().toString().trim());
        beneficiary.setZipcode(zipcode.getText().toString().trim());
        databaseHelper.addBeneficiary(beneficiary);
        straddress_list.add(venuename.getText().toString().trim());
        QuickEventVenueFragment.listBeneficiary.clear();
        QuickEventVenueFragment.listBeneficiary.addAll(QuickEventVenueFragment.databaseHelper.getAllBeneficiary());
        //  Log.e("tag", "----7777777777777777-------->"+EventVenueFragment.listBeneficiary.size());
        Log.e("tag", "LISTTTTTT------------");

    }

    private static void emptyInputEditText() {
        venuename.requestFocus();
        venuename.setText(null);
        city.setText(null);
        state.setText(null);
        address.setText(null);
        zipcode.setText(null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        eventData = ((CreateEventActivity) getActivity()).eventData;
        final View view = inflater.inflate(R.layout.fragment_eventvenue, container, false);
        FontsOverride.overrideFonts(getActivity(), view);

        CreateEventActivity.skiptv.setVisibility(View.INVISIBLE);
        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");

        databaseHelper = new DatabaseHelper(getActivity());
        beneficiary = new AddressVenue();

        listBeneficiary = new ArrayList<>();
        straddress_list = new ArrayList<>();
        listBeneficiary.clear();
        listBeneficiary.addAll(databaseHelper.getAllBeneficiary());

        til_eventname = (TextInputLayout) view.findViewById(R.id.til_eventvenue);
        till_address = (TextInputLayout) view.findViewById(R.id.til_address);
        till_city = (TextInputLayout) view.findViewById(R.id.til_city);
        till_state = (TextInputLayout) view.findViewById(R.id.til_state);
        til_zipcode = (TextInputLayout) view.findViewById(R.id.til_zipcode);
        addeventfaq_tv = view.findViewById(R.id.addeventfaq_tv);
        addmoreevent_tv = view.findViewById(R.id.addmoreevent_tv);
        venuename = view.findViewById(R.id.eventvenue_et);
        address = view.findViewById(R.id.adddress_et);
        city = view.findViewById(R.id.city_et);
        state = view.findViewById(R.id.state_et);
        zipcode = view.findViewById(R.id.zipcode_et);
        viewallvenue = view.findViewById(R.id.viewallvenue_tv);
        helpfultips_tv = view.findViewById(R.id.helpfultips_tv);
        til_eventname.setTypeface(lato);
        till_address.setTypeface(lato);
        till_city.setTypeface(lato);
        till_state.setTypeface(lato);
        til_zipcode.setTypeface(lato);
        venuename.setText(eventData.venuename);
        address.setText(eventData.venueaddress);
        city.setText(eventData.venuecity);
        state.setText(eventData.venuestate);
        zipcode.setText(eventData.venuezipcode);

        //-----------------------------------------SNACKBAR----------------------------------------//
        snackbar = Snackbar.make(getActivity().findViewById(R.id.top), R.string.venueempty, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        tv_snack = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(lato);

        //------------------------------------------------------------------------------------------//

        if (EventTypeFragment.str_eventday != null) {

            if (EventTypeFragment.str_eventday.equals("1 Day Event")) {
                addmoreevent_tv.setVisibility(View.INVISIBLE);
                viewallvenue.setVisibility(View.INVISIBLE);
                city.setText(eventData.eventcityzone);
                city.setEnabled(false);
            } else {
                addmoreevent_tv.setVisibility(View.VISIBLE);
                viewallvenue.setVisibility(View.VISIBLE);
                city.setText(eventData.eventcityzone);
                city.setEnabled(false);
            }
        }

        helpfultips_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_helpfultips);
                View v1 = dialog.getWindow().getDecorView().getRootView();
                ImageView close = dialog.findViewById(R.id.closeiv);

                FontsOverride.overrideFonts(dialog.getContext(), v1);

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) dialog).getWindow();

                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                expandableListView = (ExpandableListView) dialog.findViewById(R.id.expandableListView);
                expandableListDetail = ExpandableListDataPump.getData();
                expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                expandableListAdapter = new ExpandableListAdapter(dialog.getContext(), expandableListTitle, expandableListDetail);

                expandableListView.setAdapter(expandableListAdapter);

                expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    public void onGroupExpand(int groupPosition) {
                    }
                });

                expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                    public void onGroupCollapse(int groupPosition) {
                    }
                });

                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v,
                                                int groupPosition, int childPosition, long id) {
                        return true;
                    }
                });

                dialog.show();
            }
        });


        addmoreevent_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                city.setEnabled(true);
                postDataToSQLite();
                emptyInputEditText();
            }
        });


        addeventfaq_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_faq);
                View v1 = dialog.getWindow().getDecorView().getRootView();
                ImageView close = dialog.findViewById(R.id.closeiv);
                faq_question = dialog.findViewById(R.id.faq_question);
                faq_answer = dialog.findViewById(R.id.faq_answer);
                save_tv_faq = dialog.findViewById(R.id.save_tv_faq);
                addmore_tv = dialog.findViewById(R.id.addmore_tv);
                faq_listview = dialog.findViewById(R.id.faq_listview);
                lnr_faq = (LinearLayout) dialog.findViewById(R.id.lnr_faq);
                //lnr_faq.setVisibility(View.GONE);
                FontsOverride.overrideFonts(dialog.getContext(), v1);
                faqAdapter = new FAQAdapter(getActivity(), (ArrayList<Faq>) faqList);
                faq_listview.setAdapter(faqAdapter);


                save_tv_faq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        if (!faq_question.getText().toString().equalsIgnoreCase("")) {
                            if (!faq_answer.getText().toString().equalsIgnoreCase("")) {


                                final ProgressDialog progress = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
                                progress.setMessage(" Saving  FAQ...");
                                progress.show();

                                Runnable progressRunnable = new Runnable() {

                                    @Override
                                    public void run() {
                                        progress.cancel();
                                        Faq faq = new Faq(faq_question.getText().toString(), faq_answer.getText().toString());
                                        faqList.add(faq);
                                        faqAdapter = new FAQAdapter(getActivity(), (ArrayList<Faq>) faqList);
                                        faq_listview.setAdapter(faqAdapter);
                                        Toast.makeText(getActivity(), "FAQ added Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                };

                                Handler pdCanceller = new Handler();
                                pdCanceller.postDelayed(progressRunnable, 3000);


                            } else {
                                Toast.makeText(getActivity(), "Please Enter Answer", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(getActivity(), "Please Enter Question", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                addmore_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        lnr_faq.setVisibility(View.VISIBLE);
                        if (!faq_question.getText().toString().equalsIgnoreCase("")) {
                            if (!faq_answer.getText().toString().equalsIgnoreCase("")) {


                                StartSmartAnimation.startAnimation(lnr_faq, AnimationType.SlideOutRight, 1000, 0, true);
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        StartSmartAnimation.startAnimation(lnr_faq, AnimationType.SlideInLeft, 1000, 0, true);
                                        faq_question.setText("");
                                        faq_answer.setText("");
                                    }

                                }, 1000);
                            } else {
                                Toast.makeText(getActivity(), "Please Enter Answer", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(getActivity(), "Please Enter Question", Toast.LENGTH_LONG).show();
                        }


                    }

                });


                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) dialog).getWindow();
                        // view1.setBackgroundDrawableResource(R.drawable.border_graybg);
                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        viewallvenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_viewvenue);
                View v1 = dialog.getWindow().getDecorView().getRootView();
                ImageView close = dialog.findViewById(R.id.closeiv);
                FontsOverride.overrideFonts(dialog.getContext(), v1);
                initViews();
                initObjects();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) dialog).getWindow();
                        // view1.setBackgroundDrawableResource(R.drawable.border_graybg);
                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                dialog.show();
            }
        });


        return view;
    }

    private void initViews() {
        recyclerViewBeneficiary = (RecyclerView) dialog.findViewById(R.id.recyclerViewBeneficiary);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        listBeneficiary = new ArrayList<>();
        viewVenuesRecyclerAdapter = new ViewVenuesRecyclerAdapter(listBeneficiary, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewBeneficiary.setLayoutManager(mLayoutManager);
        recyclerViewBeneficiary.setItemAnimator(new DefaultItemAnimator());
        recyclerViewBeneficiary.setHasFixedSize(true);
        recyclerViewBeneficiary.setAdapter(viewVenuesRecyclerAdapter);
        databaseHelper = new DatabaseHelper(getActivity());

        getDataFromSQLite();


    }


    /**
     * This method is to fetch all user records from SQLite
     */
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listBeneficiary.clear();
                listBeneficiary.addAll(databaseHelper.getAllBeneficiary());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                viewVenuesRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        setData();
    }

    private void setData() {
        eventData.venuename = venuename.getText().toString();
        eventData.venueaddress = address.getText().toString();
        eventData.venuecity = city.getText().toString().trim();
        eventData.venuestate = state.getText().toString().trim();
        eventData.venuezipcode = zipcode.getText().toString().trim();

    }

    //FAQ ADAPTERE
    class FAQAdapter extends BaseAdapter {
        Typeface lato;
        private Activity activity;
        private LayoutInflater inflater;
        private ArrayList<Faq> faqItems = new ArrayList<>();

        public FAQAdapter(FragmentActivity activity, ArrayList<Faq> faqItems) {

            this.activity = activity;
            this.faqItems = faqItems;
        }


        @Override
        public int getCount() {
            return faqItems.size();
        }

        @Override
        public Object getItem(int i) {
            return faqItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            final ViewHolder viewHolder;

            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (view == null) {
                view = inflater.inflate(R.layout.faq_listview, null);
                viewHolder = new ViewHolder();
                viewHolder.position = position;
                FontsOverride.overrideFonts(activity, view);
                viewHolder.txt_question = view.findViewById(R.id.txt_question);
                viewHolder.txt_answer = view.findViewById(R.id.txt_answer);
                viewHolder.del_faq = view.findViewById(R.id.del_faq);
                viewHolder.lnr_faq_val = view.findViewById(R.id.lnr_faq_val);


                lato = Typeface.createFromAsset(activity.getAssets(), "fonts/lato.ttf");
                viewHolder.txt_question.setTypeface(lato);
                viewHolder.txt_answer.setTypeface(lato);


                viewHolder.del_faq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StartSmartAnimation.startAnimation(viewHolder.lnr_faq_val, AnimationType.SlideOutRight, 1000, 0, true);
                        faqItems.remove(position);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                faq_listview.setAdapter(faqAdapter);
                                notifyDataSetChanged();
                            }
                        }, 1000);
                    }
                });


                Faq faq = faqItems.get(position);
                //viewHolder.txt_itinerary_val.setText(dayContent.getItinerary_start_time());
                viewHolder.txt_question.setText(faq.getQuestion());
                viewHolder.txt_answer.setText(faq.getAnswer());


            }
            return view;
        }

        class ViewHolder {
            public TextView txt_question, txt_answer;
            public ImageView del_faq;
            int position;
            LinearLayout lnr_faq_val;


        }

    }

}
