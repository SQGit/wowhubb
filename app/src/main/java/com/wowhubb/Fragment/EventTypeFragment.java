package com.wowhubb.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.wowhubb.Activity.CreateEventActivity;
import com.wowhubb.Adapter.CustomAdapter;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EventTypeFragment extends Fragment {

    public static EditText eventdate_et, desc_et,eventtopic_et;
    LinearLayout layout_eventdate;
    Typeface lato;
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;
    String[] SPINNERLIST = {"Event Type", "Anniversary", "Baby Shower", "Birthday Party", "Holiday Party", "Wedding", "Baby Naming", "Graduation Party", "Team Outing", "Photo/File Shoot Event", "Bridal Shower Event", "Meeting", "Others", "Group Meetings"};
    TextInputLayout desc_til, eventdate_til,eventtopic_til;

    public static EventTypeFragment newInstance(int page, boolean isLast) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        if (isLast)
            args.putBoolean("isLast", true);
        final EventTypeFragment fragment = new EventTypeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_event_type, container, false);
        FontsOverride.overrideFonts(getActivity(), view);
        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getActivity());
        edit = sharedPrefces.edit();
        CreateEventActivity.skiptv.setVisibility(View.INVISIBLE);
        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");

        eventdate_et = view.findViewById(R.id.eventdate);
        desc_til = view.findViewById(R.id.til_description);
        eventtopic_til = view.findViewById(R.id.til_eventtopic);
        eventdate_til = view.findViewById(R.id.til_eventdate);

        desc_et = view.findViewById(R.id.description_et);
        eventtopic_et = view.findViewById(R.id.eventtopic_et);
        layout_eventdate = view.findViewById(R.id.layout_eventdate);

        desc_til.setTypeface(lato);
        eventdate_til.setTypeface(lato);
        eventtopic_til.setTypeface(lato);

        layout_eventdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new SelectEventDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner) view.findViewById(R.id.gender_spn);
        View view1=materialDesignSpinner.getRootView();
        FontsOverride.overrideFonts(getActivity(), view1);


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
        materialDesignSpinner.setAdapter(arrayAdapter);
        materialDesignSpinner.setTypeface(lato);

        return view;
    }


    @SuppressLint("ValidFragment")
    public static class SelectEventDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
           // return new DatePickerDialog(getActivity(), this, yy, mm, dd);

            DatePickerDialog da=new DatePickerDialog(getActivity(), this, yy, mm, dd);

            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, 1);
            Date newDate = c.getTime();
            da.getDatePicker().setMinDate(newDate.getTime()-(newDate.getTime()%(24*60*60*1000)));

            return da;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }

        public void populateSetDate(int year, int month, int day)
        {
            //edittext_offdate.setText(month+"/"+day+"/"+year);
            String strDateFormatTo=month + "/" + day + "/" + year;
            SimpleDateFormat spf=new SimpleDateFormat("MM/dd/yyyy");
            Date newDate= null;

            try
            {
                newDate = spf.parse(strDateFormatTo);
                spf= new SimpleDateFormat("MMM/dd/yyyy");
                strDateFormatTo = spf.format(newDate);
                eventdate_et.setText(strDateFormatTo);
                Log.e("tag","dvhdsvjvj"+strDateFormatTo);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }


}