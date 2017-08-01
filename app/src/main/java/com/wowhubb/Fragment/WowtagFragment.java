package com.wowhubb.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

import java.util.Calendar;

public class WowtagFragment extends Fragment {

    static TextView fromtime_tv, totime_tv;
    LinearLayout fromlv, tolv;

    public static WowtagFragment newInstance(int page, boolean isLast) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        if (isLast)
            args.putBoolean("isLast", true);
        final WowtagFragment fragment = new WowtagFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_wowtag, container, false);
        FontsOverride.setDefaultFont(getActivity(), "LATO", "fonts/lato.ttf");
        fromtime_tv = (TextView) view.findViewById(R.id.fromtv);
        totime_tv = (TextView) view.findViewById(R.id.totv);
        fromlv = (LinearLayout) view.findViewById(R.id.fromlv);
        tolv = (LinearLayout) view.findViewById(R.id.tolv);

        fromlv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        tolv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });
        return view;
    }


    @SuppressLint("ValidFragment")
    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);

            //Disable the previous date in calend

            // DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 100000);
            //return datePickerDialog;

        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);

        }

        public void populateSetDate(int year, int month, int day) {
            //edittext_offdate.setText(month+"/"+day+"/"+year);
            fromtime_tv.setText(year + "/" + month + "/" + day);
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getFragmentManager(), "TimePicker");

        }
    }


    @SuppressLint("ValidFragment")
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //Use the current time as the default values for the time picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);


            //Create and return a new instance of TimePickerDialog
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        //onTimeSet() callback method
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            //Do something with the user chosen time
            //Get reference of host activity (XML Layout File) TextView widget
            // TextView tv = (TextView) getActivity().findViewById(R.id.tv);
            //Set a message for user
            //tv.setText("Your chosen time is...\n\n");
            //Display the user changed time on TextView
            fromtime_tv.setText(fromtime_tv.getText() + " " + String.valueOf(hourOfDay)
                    + ": " + String.valueOf(minute));
        }

    }
}