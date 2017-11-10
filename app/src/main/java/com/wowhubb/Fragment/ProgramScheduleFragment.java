package com.wowhubb.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.wowhubb.Activity.CreateEventActivity;
import com.wowhubb.Adapter.Schedule;

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ProgramScheduleFragment extends Fragment {

    Typeface lato;
    TextView day1, day2, day3, day4;
    static ArrayList<Schedule> scheduleList;
    private ListView recyclerView;
    private ScheduleAdapter mAdapter;
    String status;
    LinearLayout lv1, lv2, lv3, lv4;
    private static String str_frm, str_to;
    static EditText from_et, to_et;
    ImageView addmore;


    public static ProgramScheduleFragment newInstance(int page, boolean isLast) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        if (isLast)
            args.putBoolean("isLast", true);
        final ProgramScheduleFragment fragment = new ProgramScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_programschedule_list, container, false);
        FontsOverride.overrideFonts(getActivity(), view);
        CreateEventActivity.skiptv.setVisibility(View.INVISIBLE);
        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");

        recyclerView = (ListView) view.findViewById(R.id.listview);
        addmore = (ImageView) view.findViewById(R.id.addmore_tv);
        lv1 = view.findViewById(R.id.lv1);
        lv2 = view.findViewById(R.id.lv2);
        lv3 = view.findViewById(R.id.lv3);
        lv4 = view.findViewById(R.id.lv4);
        day1 = view.findViewById(R.id.day1_tv);
        day2 = view.findViewById(R.id.day2_tv);
        day3 = view.findViewById(R.id.day3_tv);
        day4 = view.findViewById(R.id.day4_tv);

        scheduleList = new ArrayList<>();
        scheduleList.add(new Schedule());

        scheduleList.add(new Schedule());
        scheduleList.add(new Schedule());

        mAdapter = new ScheduleAdapter(getActivity(), scheduleList);
        Log.e("tag", "STATUS-----" + EventVenueFragment.checkboxstatus);

        recyclerView.setAdapter(mAdapter);
        lv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleList.clear();
                scheduleList.add(new Schedule());
                scheduleList.add(new Schedule());
                scheduleList.add(new Schedule());
                mAdapter.notifyDataSetChanged();
                day2.setTextColor(Color.parseColor("#000000"));
                day3.setTextColor(Color.parseColor("#000000"));
                day4.setTextColor(Color.parseColor("#000000"));
                day1.setTextColor(Color.parseColor("#e91e63"));

            }
        });
        lv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleList.clear();
                scheduleList.add(new Schedule());
                scheduleList.add(new Schedule());
                scheduleList.add(new Schedule());
                mAdapter.notifyDataSetChanged();
                day2.setTextColor(Color.parseColor("#e91e63"));
                day1.setTextColor(Color.parseColor("#000000"));
                day3.setTextColor(Color.parseColor("#000000"));
                day4.setTextColor(Color.parseColor("#000000"));
            }
        });
        lv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleList.clear();
                scheduleList.add(new Schedule());
                scheduleList.add(new Schedule());
                scheduleList.add(new Schedule());
                mAdapter.notifyDataSetChanged();
                day3.setTextColor(Color.parseColor("#e91e63"));
                day1.setTextColor(Color.parseColor("#000000"));
                day2.setTextColor(Color.parseColor("#000000"));
                day4.setTextColor(Color.parseColor("#000000"));


            }
        });
        lv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleList.clear();
                scheduleList.add(new Schedule());
                scheduleList.add(new Schedule());
                scheduleList.add(new Schedule());
                mAdapter.notifyDataSetChanged();
                day4.setTextColor(Color.parseColor("#e91e63"));
                day1.setTextColor(Color.parseColor("#000000"));
                day2.setTextColor(Color.parseColor("#000000"));
                day3.setTextColor(Color.parseColor("#000000"));

            }
        });

        addmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleList.add(new Schedule());
                scheduleList.add(new Schedule());
                scheduleList.add(new Schedule());

                Toast.makeText(getActivity(), "1 Row Added", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();


            }
        });
        EventVenueFragment.oneday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                    EventVenueFragment.twoday.setChecked(false);
                EventVenueFragment.threeday.setChecked(false);
                EventVenueFragment.checkboxstatus = "oneday";
                Log.e("tag", "STATUSqwqww-----" + EventVenueFragment.checkboxstatus);
                lv2.setVisibility(View.GONE);
                lv3.setVisibility(View.GONE);
                lv4.setVisibility(View.GONE);

            }
        });
        EventVenueFragment.twoday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                    EventVenueFragment.oneday.setChecked(false);
                EventVenueFragment.threeday.setChecked(false);
                EventVenueFragment.checkboxstatus = "twoday";
                Log.e("tag", "STATUSqwqww-----" + EventVenueFragment.checkboxstatus);
                lv1.setVisibility(View.VISIBLE);
                lv2.setVisibility(View.VISIBLE);
                lv3.setVisibility(View.GONE);
                lv4.setVisibility(View.GONE);
            }
        });


        EventVenueFragment.threeday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                    EventVenueFragment.twoday.setChecked(false);
                EventVenueFragment.oneday.setChecked(false);
                EventVenueFragment.checkboxstatus = "threeday";
                Log.e("tag", "STATUSqwqww-----" + EventVenueFragment.checkboxstatus);
                lv1.setVisibility(View.VISIBLE);
                lv2.setVisibility(View.VISIBLE);
                lv3.setVisibility(View.VISIBLE);
                lv4.setVisibility(View.VISIBLE);
            }
        });


        return view;
    }

    public class ScheduleAdapter extends BaseAdapter {
        ArrayList<Schedule> scheduleList;
        LayoutInflater inflater;
        Context context;
        Activity activity;

        public ScheduleAdapter(Context context, ArrayList<Schedule>
                scheduleList) {
            this.context = context;
            this.scheduleList = scheduleList;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return scheduleList.size();
        }


        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.fragment_programschedule, parent, false);
                FontsOverride.overrideFonts(getActivity(), convertView);

                holder = new ViewHolder();
                holder.mWatcher = new MutableWatcher();
                holder.fromtime_tv = (EditText) convertView.findViewById(R.id.fromtv);
                holder.totime_tv = (EditText) convertView.findViewById(R.id.totv);
                holder.fromlv = (LinearLayout) convertView.findViewById(R.id.fromlv);
                holder.tolv = (LinearLayout) convertView.findViewById(R.id.tolv);
                holder.layout_fromdate = convertView.findViewById(R.id.layout_fromdate);
                holder.layout_todate = convertView.findViewById(R.id.layout_todate);

                holder.fromtime_tv.setTag(position);
                holder.totime_tv.setTag(position);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.fromtime_tv.setText(scheduleList.get(position).getStarttime());
            holder.totime_tv.setText(scheduleList.get(position).getEndtime());

            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_getdirection);
            final LinearLayout newvenue = dialog.findViewById(R.id.newvenue_lv);
            final CheckBox newvenue_cb = dialog.findViewById(R.id.newvenue_cb);
            final CheckBox samevenue_cb = dialog.findViewById(R.id.samevenue_id);
            final ImageView sub = dialog.findViewById(R.id.submit_iv);

            final ImageView close = dialog.findViewById(R.id.closeiv);
            View view1 = dialog.getWindow().getDecorView().getRootView();
            FontsOverride.overrideFonts(dialog.getContext(), view1);
            holder.mWatcher.setActive(false);
            holder.mWatcher.setPosition(position);
            holder.mWatcher.setActive(true);
            // holder.fromtime_tv.setText(str_frm+str_to);
             /*  holder.direction1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.show();

                }
            });*/
            sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            newvenue_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        newvenue.setVisibility(View.VISIBLE);
                        samevenue_cb.setChecked(true);
                    } else {
                        newvenue.setVisibility(View.GONE);
                    }
                }
            });
            samevenue_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        newvenue.setVisibility(View.GONE);
                        newvenue_cb.setChecked(false);
                    } else {
                    }
                }
            });
            holder.fromtime_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    from_et = (EditText) view1;
                    OpenDialog(view1);

                }
            });

            holder.totime_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    to_et = (EditText) view2;
                    OpenDialogTo(view2);

                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            return convertView;
        }

        private void OpenDialog(final View view1) {
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getFragmentManager(), "TimePicker");
            Log.e("tag", "FROM----------->" + str_frm);

        }

        private void OpenDialogTo(final View view2) {
            DialogFragment newFragment = new TimePickerFragmentTo();
            newFragment.show(getFragmentManager(), "TimePicker");

        }

        public class ViewHolder {
            public MutableWatcher mWatcher;
            public EditText fromtime_tv, totime_tv;
            LinearLayout fromlv, tolv, layout_fromdate, layout_todate;
        }

        class MutableWatcher implements TextWatcher {

            private int mPosition;
            private boolean mActive;

            void setPosition(int position) {
                mPosition = position;
            }

            void setActive(boolean active) {
                mActive = active;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mActive)
                {

                }
            }
        }
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


        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }

        public void populateSetDate(int year, int month, int day) {
            from_et.setText(year + "/" + month + "/" + day);
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getFragmentManager(), "TimePicker");

        }
    }


    @SuppressLint("ValidFragment")
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            //Use the current time as the default values for the time picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {
            String strDateFormat=String.format("%02d:%02d", hourOfDay, minute) ;
            strDateFormat=strDateFormat+" a";
            SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mm a");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");

            try
            {
                Date dt = parseFormat.parse(strDateFormat);
                System.out.println(outputFormat.format(dt));
                from_et.setText(outputFormat.format(dt));
            } catch(ParseException exc)
            {
                exc.printStackTrace();
            }


            int pos = (int) from_et.getTag();

            try {
                scheduleList.get(pos).setStarttime(from_et.getText().toString());
            } catch (IndexOutOfBoundsException e) {

            }
        }

    }

    @SuppressLint("ValidFragment")
    public static class SelectDateFragmentTo extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);

        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);

        }

        public void populateSetDate(int year, int month, int day) {
            //edittext_offdate.setText(month+"/"+day+"/"+year);
            to_et.setText(year + "/" + month + "/" + day);
            DialogFragment newFragment = new TimePickerFragmentTo();
            newFragment.show(getFragmentManager(), "TimePicker");

        }
    }


    @SuppressLint("ValidFragment")
    public static class TimePickerFragmentTo extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

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


            String strDateFormat=String.format("%02d:%02d", hourOfDay, minute) ;
            strDateFormat=strDateFormat+" a";


            SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mm a");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");


            try {
                Date dt = parseFormat.parse(strDateFormat);
                System.out.println(outputFormat.format(dt));
                to_et.setText(outputFormat.format(dt));
            } catch(ParseException exc) {
                exc.printStackTrace();
            }
            int pos1 = (int) to_et.getTag();

            try {
                scheduleList.get(pos1).setEndtime(to_et.getText().toString());
            } catch (IndexOutOfBoundsException e) {

            }
        }

    }


}