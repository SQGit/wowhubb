package vineture.wowhubb.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import vineture.wowhubb.Adapter.CustomListAdapter;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.R;
import vineture.wowhubb.data.DayContent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Guna on 18-12-2017.
 */

public class DayFragment2 extends Fragment {
    public static TextView txt_start, txt_end, save_tv, viewallvenue_tv, addmoreevent_tv, dialog_head;
    public static EditText edt_event_agenta, edt_facilitator, start_timetv, end_timetv, txt_start_time, txt_end_time, edt_location;
    public static Dialog dialog, ctimedialog;
    public static ViewTimeSlotAdapter viewTimeSlotAdapter;
    public static String iti_start, iti_end, iti_merge;
    public static String str_day, str_event_start, str_event_end, str_itinery_start, str_itinery_end, str_agenda, str_facilitator, str_location, str_eventno;
    TextInputLayout lnr_eventlocation, lnr_edt_event_agenta, lnr_edt_facilitator, l1, l2;
    LinearLayout lnr_start_time, lnr_end_time, lnr_start, lnr_end;
    MaterialBetterSpinner spin_event_loc_venue;
    TextView timeslot_count;
    int slot_count = 1;
    TextView color;
    Typeface lato;
    ListView timeslot_listview;
    String str_event_location_venue, ss;
    CustomListAdapter arrayAdapter0;
    String event_date1, event_date2, event_merge;
    TextView itinerary_start, itinerary_end;
    // Store instance variables
    private String title;
    private int page;
    private List<DayContent> dayList = new ArrayList<>();

    // newInstance constructor for creating fragment with arguments
    public static DayFragment2 newInstance(int page, String title) {

        DayFragment2 fragmentFirst = new DayFragment2();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.day_fragment, container, false);
        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");
        FontsOverride.overrideFonts(getActivity(), view);

        txt_start = (TextView) view.findViewById(R.id.txt_start);
        txt_end = (TextView) view.findViewById(R.id.txt_end);
        save_tv = (TextView) view.findViewById(R.id.save_tv);
        viewallvenue_tv = (TextView) view.findViewById(R.id.viewallvenue_tv);
        // addmoreevent_tv=(TextView)view.findViewById(R.id.addmoreevent_tv);
        timeslot_count = (TextView) view.findViewById(R.id.timeslot_count);
        edt_event_agenta = (EditText) view.findViewById(R.id.edt_event_agenta);
        edt_facilitator = (EditText) view.findViewById(R.id.edt_facilitator);
        start_timetv = (EditText) view.findViewById(R.id.start_timetv);
        end_timetv = (EditText) view.findViewById(R.id.end_timetv);
        edt_location = (EditText) view.findViewById(R.id.edt_location);
        lnr_edt_event_agenta = (TextInputLayout) view.findViewById(R.id.lnr_edt_event_agenta);
        lnr_edt_facilitator = (TextInputLayout) view.findViewById(R.id.lnr_edt_facilitator);
        lnr_start_time = (LinearLayout) view.findViewById(R.id.lnr_start_time);
        lnr_end_time = (LinearLayout) view.findViewById(R.id.lnr_end_time);
        txt_start_time = (EditText) view.findViewById(R.id.txt_start_time);
        txt_end_time = (EditText) view.findViewById(R.id.txt_end_time);
        lnr_start = (LinearLayout) view.findViewById(R.id.lnr_start);
        lnr_end = (LinearLayout) view.findViewById(R.id.lnr_end);
        spin_event_loc_venue = (MaterialBetterSpinner) view.findViewById(R.id.spin_event_loc_venue);
        dialog_head = (TextView) view.findViewById(R.id.dialog_head);
        l1 = view.findViewById(R.id.l1);
        l2 = view.findViewById(R.id.l2);
        lnr_eventlocation = view.findViewById(R.id.lnr_eventlocation);

        txt_start.setTypeface(lato);
        txt_end.setTypeface(lato);
        save_tv.setTypeface(lato);
        viewallvenue_tv.setTypeface(lato);
        timeslot_count.setTypeface(lato);
        edt_event_agenta.setTypeface(lato);
        edt_facilitator.setTypeface(lato);
        lnr_edt_event_agenta.setTypeface(lato);
        lnr_edt_facilitator.setTypeface(lato);
        start_timetv.setTypeface(lato);
        end_timetv.setTypeface(lato);
        txt_start_time.setTypeface(lato);
        txt_end_time.setTypeface(lato);
        lnr_eventlocation.setTypeface(lato);
        l1.setTypeface(lato);
        l2.setTypeface(lato);


        timeslot_count.setText("Time Slot 1");
        start_timetv.setEnabled(true);
        if (EventTypeFragment.str_eventday.equals("2 Days Event")) {
            end_timetv.setText(EventTypeFragment.str_end);
            end_timetv.setEnabled(false);
        }


        str_event_start = start_timetv.getText().toString().trim();
        str_event_end = end_timetv.getText().toString().trim();
        iti_start = txt_start_time.getText().toString();
        iti_end = txt_end_time.getText().toString();
        Log.e("tag", "Zoot1" + txt_start_time.getText().toString());
        Log.e("tag", "Zoot2" + txt_end_time.getText().toString());
        iti_merge = iti_start + " - " + iti_end;
        Log.e("tag", "Zoot" + iti_merge);


        save_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Long> day_categories = new HashMap<>();
                str_agenda = edt_event_agenta.getText().toString().trim();
                str_facilitator = edt_facilitator.getText().toString().trim();
                str_eventno = String.valueOf(slot_count);
                ss = timeslot_count.getText().toString();

                Log.e("tag", "day " + String.valueOf(page + 1));
                Log.e("tag", "slot " + String.valueOf(slot_count));
                Log.e("tag", "event start time" + start_timetv.getText().toString());
                Log.e("tag", "event end time" + end_timetv.getText().toString());
                Log.e("tag", "itinerary start time" + txt_start_time.getText().toString());
                Log.e("tag", "itinerary end time" + txt_end_time.getText().toString());
                Log.e("tag", "event agenda" + edt_event_agenta.getText().toString());
                Log.e("tag", "facilitator" + edt_facilitator.getText().toString());
                Log.e("tag", "Location" + str_event_location_venue);


                if (!start_timetv.getText().toString().trim().equalsIgnoreCase("")) {
                    if (!end_timetv.getText().toString().trim().equalsIgnoreCase("")) {
                        if (!txt_start_time.getText().toString().trim().equalsIgnoreCase("")) {
                            if (!txt_end_time.getText().toString().trim().equalsIgnoreCase("")) {
                                if (!edt_event_agenta.getText().toString().trim().equalsIgnoreCase("")) {
                                    edt_event_agenta.setError(null);

                                    if (!edt_facilitator.getText().toString().trim().equalsIgnoreCase("")) {
                                        edt_facilitator.setError(null);
                                        if (str_event_location_venue != null) {

                                            final ProgressDialog progress = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
                                            progress.setMessage(ss + " Saving...");
                                            progress.show();

                                            Runnable progressRunnable = new Runnable() {

                                                @Override
                                                public void run() {
                                                    progress.cancel();
                                                    Toast.makeText(getActivity(), ss + " added Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            };

                                            Handler pdCanceller = new Handler();
                                            pdCanceller.postDelayed(progressRunnable, 3000);
                                            DayContent dayContent = new DayContent(String.valueOf(page + 1), String.valueOf(slot_count), start_timetv.getText().toString(), end_timetv.getText().toString(), txt_start_time.getText().toString(), txt_end_time.getText().toString(), edt_event_agenta.getText().toString(), edt_facilitator.getText().toString(), str_event_location_venue);

                                            dayList.add(dayContent);

                                            slot_count = slot_count + 1;

                                            timeslot_count.setText("Time Slot " + slot_count);

                                            clearValues();
                                        } else {
                                            Toast.makeText(getActivity(), "Please Select Event Location Venue for" + ss, Toast.LENGTH_LONG).show();
                                        }

                                    } else {
                                        edt_facilitator.setError("Please enter Event Facilitator");
                                    }
                                } else {
                                    edt_event_agenta.setError("Please enter Event Agenda");
                                }
                            } else {
                                Toast.makeText(getActivity(), "Please Select Itinerary End Time for " + ss, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Please Select Itinerary Start Time for " + ss, Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Please Select Event End Time for " + ss, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Select Event Start Time for " + ss, Toast.LENGTH_LONG).show();
                }


            }
        });


        viewallvenue_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (timeslot_count.getText().toString().equals("Time Slot 1")) {
                    ProgramScheduleFragmentNew.snackbar.show();

                } else {
                    dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_showvenue);
                    View v1 = dialog.getWindow().getDecorView().getRootView();
                    ImageView close = dialog.findViewById(R.id.closeiv);
                    TextView time = dialog.findViewById(R.id.time);
                    FontsOverride.overrideFonts(dialog.getContext(), v1);
                    dialog_head = dialog.findViewById(R.id.dialog_head);
                    timeslot_listview = dialog.findViewById(R.id.timeslot_listview);
                    dialog_head.setText(ProgramScheduleFragmentNew.str_twoday);
                    TextView event_name = dialog.findViewById(R.id.event_name);

                    event_date1 = start_timetv.getText().toString();
                    Log.e("tag", "time1" + event_date1);
                    event_date2 = end_timetv.getText().toString();
                    Log.e("tag", "time2" + event_date2);
                    time.setText(event_date1 + " - " + event_date2);


                    event_name.setText(EventTypeFragment.eventtopic_et.getText().toString().trim());


               /* event_merge=start_timetv+" - "+end_timetv;
                time.setText(event_merge);*/

                    viewTimeSlotAdapter = new ViewTimeSlotAdapter(getActivity(), (ArrayList<DayContent>) dayList, slot_count);
                    timeslot_listview.setAdapter(viewTimeSlotAdapter);


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


                    dialog.show();

                }


            }
        });
        start_timetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_timetv.setText("");
                // DialogFragment newFragment = new TimePickerFragment();
                // newFragment.show(getFragmentManager(), "TimePicker");

                startTime();
            }
        });


        end_timetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end_timetv.setText("");
                endTime();
                // DialogFragment newFragment = new TimePickerFragment1();
                // newFragment.show(getFragmentManager(), "TimePicker");
            }
        });


        lnr_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_start_time.setText("");
                txt_start_time.setVisibility(View.VISIBLE);
                itenarystartTime();
                // DialogFragment newFragment = new TimePickerFragment2();
                //newFragment.show(getFragmentManager(), "TimePicker");
            }
        });


        lnr_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_end_time.setText("");
                txt_end_time.setVisibility(View.VISIBLE);
                itenaryendTime();
                // DialogFragment newFragment = new TimePickerFragment3();
                // newFragment.show(getFragmentManager(), "TimePicker");
            }
        });



        if (EventVenueFragment.listBeneficiary.size() > 0) {
            Log.e("tag", "1111111111111---------------->" + EventVenueFragment.listBeneficiary.size());
            EventVenueFragment.straddress_list.clear();
            for (int i = 0; i < EventVenueFragment.listBeneficiary.size(); i++) {
                Log.e("tag", "2222222222---------------->" + EventVenueFragment.straddress_list.size());
                EventVenueFragment.straddress_list.add(EventVenueFragment.listBeneficiary.get(i).getVenuname());
                Log.e("tag", "2222222222---------------->" + EventVenueFragment.straddress_list.size());
            }
            spin_event_loc_venue.setVisibility(View.VISIBLE);
            edt_location.setVisibility(View.GONE);
            //START LOCATION  SET SPINNER
            arrayAdapter0 = new CustomListAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, EventVenueFragment.straddress_list) {
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
                    tv.setTextSize(14);
                    tv.setTypeface(lato);
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
                    tv.setTextSize(14);
                    tv.setTypeface(lato);
                    tv.setPadding(10, 20, 0, 20);

                    if (position == 0) {
                        tv.setTextColor(Color.BLACK);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            spin_event_loc_venue.setAdapter(arrayAdapter0);
            spin_event_loc_venue.setTypeface(lato);
        } else {
            edt_location.setVisibility(View.VISIBLE);
            spin_event_loc_venue.setVisibility(View.GONE);
        }


        spin_event_loc_venue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // materialDesignSpinner.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
                FontsOverride.overrideFonts(getContext(), view);
                str_event_location_venue = adapterView.getItemAtPosition(i).toString();
                Log.e("tag", "str_category------>" + str_event_location_venue);

            }
        });

        return view;
    }

    private void startTime() {
        ctimedialog = new Dialog(getActivity());
        ctimedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ctimedialog.setContentView(R.layout.dialog_customtime);
        View v1 = ctimedialog.getWindow().getDecorView().getRootView();

        final TimePicker dp = ctimedialog.findViewById(R.id.timePicker);
        TextView ok = ctimedialog.findViewById(R.id.oktv);
        TextView cancel = ctimedialog.findViewById(R.id.canceltv);

        //   dp.setOnDateChangedListener(dateChangedListener);
        FontsOverride.overrideFonts(ctimedialog.getContext(), v1);

        ctimedialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Window view1 = ((Dialog) ctimedialog).getWindow();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctimedialog.dismiss();
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                int hour = 0;
                int min = 0;

                int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    hour = dp.getHour();
                    min = dp.getMinute();
                } else {
                    hour = dp.getCurrentHour();
                    min = dp.getCurrentMinute();
                }

                ctimedialog.dismiss();
                populatestartSetTimeTo(hour, min);
            }
        });
        ctimedialog.show();
        //getActivity().showDialog(Time_id);


    }

    private void endTime() {
        ctimedialog = new Dialog(getActivity());
        ctimedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ctimedialog.setContentView(R.layout.dialog_customtime);
        View v1 = ctimedialog.getWindow().getDecorView().getRootView();

        final TimePicker dp = ctimedialog.findViewById(R.id.timePicker);
        TextView ok = ctimedialog.findViewById(R.id.oktv);
        TextView cancel = ctimedialog.findViewById(R.id.canceltv);

        //   dp.setOnDateChangedListener(dateChangedListener);
        FontsOverride.overrideFonts(ctimedialog.getContext(), v1);

        ctimedialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Window view1 = ((Dialog) ctimedialog).getWindow();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctimedialog.dismiss();
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                int hour = 0;
                int min = 0;

                int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    hour = dp.getHour();
                    min = dp.getMinute();
                } else {
                    hour = dp.getCurrentHour();
                    min = dp.getCurrentMinute();
                }

                ctimedialog.dismiss();
                populateendSetTimeTo(hour, min);
            }
        });
        ctimedialog.show();
        //getActivity().showDialog(Time_id);


    }

    private void itenarystartTime() {
        ctimedialog = new Dialog(getActivity());
        ctimedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ctimedialog.setContentView(R.layout.dialog_customtime);
        View v1 = ctimedialog.getWindow().getDecorView().getRootView();

        final TimePicker dp = ctimedialog.findViewById(R.id.timePicker);
        TextView ok = ctimedialog.findViewById(R.id.oktv);
        TextView cancel = ctimedialog.findViewById(R.id.canceltv);

        //   dp.setOnDateChangedListener(dateChangedListener);
        FontsOverride.overrideFonts(ctimedialog.getContext(), v1);

        ctimedialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Window view1 = ((Dialog) ctimedialog).getWindow();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctimedialog.dismiss();
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                int hour = 0;
                int min = 0;

                int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    hour = dp.getHour();
                    min = dp.getMinute();
                } else {
                    hour = dp.getCurrentHour();
                    min = dp.getCurrentMinute();
                }

                ctimedialog.dismiss();
                populateitenirarystartSetTimeTo(hour, min);
            }
        });
        ctimedialog.show();


    }

    private void itenaryendTime() {
        ctimedialog = new Dialog(getActivity());
        ctimedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ctimedialog.setContentView(R.layout.dialog_customtime);
        View v1 = ctimedialog.getWindow().getDecorView().getRootView();

        final TimePicker dp = ctimedialog.findViewById(R.id.timePicker);
        TextView ok = ctimedialog.findViewById(R.id.oktv);
        TextView cancel = ctimedialog.findViewById(R.id.canceltv);

        FontsOverride.overrideFonts(ctimedialog.getContext(), v1);

        ctimedialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Window view1 = ((Dialog) ctimedialog).getWindow();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctimedialog.dismiss();
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                int hour = 0;
                int min = 0;

                int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    hour = dp.getHour();
                    min = dp.getMinute();
                } else {
                    hour = dp.getCurrentHour();
                    min = dp.getCurrentMinute();
                }

                ctimedialog.dismiss();
                populateiteniraryendSetTimeTo(hour, min);
            }
        });
        ctimedialog.show();


    }


    private void populatestartSetTimeTo(int hourOfDay, int minute) {
        String strDateFormat = String.format("%02d:%02d", hourOfDay, minute);


        SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mm a");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");


        try {
            Date dt = parseFormat.parse(strDateFormat);
            System.out.println(outputFormat.format(dt));
            start_timetv.setText(outputFormat.format(dt));
            Log.e("tag", "12345" + start_timetv.getText().toString());
        } catch (ParseException exc) {
            exc.printStackTrace();
        }


    }

    private void populateendSetTimeTo(int hourOfDay, int minute) {
        String strDateFormat = String.format("%02d:%02d", hourOfDay, minute);

        SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mm a");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");

        try {
            Date dt = parseFormat.parse(strDateFormat);
            System.out.println(outputFormat.format(dt));
            end_timetv.setText(outputFormat.format(dt));
        } catch (ParseException exc) {
            exc.printStackTrace();
        }


    }

    private void populateitenirarystartSetTimeTo(int hourOfDay, int minute) {
        String strDateFormat = String.format("%02d:%02d", hourOfDay, minute);

        SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mm a");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");


        try {
            Date dt = parseFormat.parse(strDateFormat);
            System.out.println(outputFormat.format(dt));
            txt_start_time.setText(outputFormat.format(dt));
        } catch (ParseException exc) {
            exc.printStackTrace();
        }


    }


    private void populateiteniraryendSetTimeTo(int hourOfDay, int minute) {
        // String strDateFormat1 = t.getText().toString();


        String strDateFormat = String.format("%02d:%02d", hourOfDay, minute);


        SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mm a");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");


        try {
            Date dt = parseFormat.parse(strDateFormat);
            System.out.println(outputFormat.format(dt));
            txt_end_time.setText(outputFormat.format(dt));
        } catch (ParseException exc) {
            exc.printStackTrace();
        }


    }

    private void clearValues() {
        txt_start_time.setVisibility(View.GONE);
        txt_end_time.setVisibility(View.GONE);
       /* txt_start_time.setText("");
        txt_end_time.setText("");*/
        edt_event_agenta.setText("");
        edt_facilitator.setText("");

    }

    private List<DayContent> prepareMovieData() {

        List<DayContent> daylist = new ArrayList<>();
        DayContent day = new DayContent("", "", "", "", "", "", "", "", "");
        daylist.add(0, day);

//        separateAdapter.notifyDataSetChanged();

        return daylist;

    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //Use the current time as the default values for the time picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            //Create and return a new instance of TimePickerDialog
            return new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT
                    , this, hour, minute, false);
        }

        //onTimeSet() callback method
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            //String strDateFormat1 = start_timetv.getText().toString();


            String strDateFormat = String.format("%02d:%02d", hourOfDay, minute);


            SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mm a");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");


            try {
                Date dt = parseFormat.parse(strDateFormat);
                System.out.println(outputFormat.format(dt));
                start_timetv.setText(outputFormat.format(dt));
                Log.e("tag", "12345" + start_timetv.getText().toString());
            } catch (ParseException exc) {
                exc.printStackTrace();
            }


        }

    }

    public static class TimePickerFragment1 extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //Use the current time as the default values for the time picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            //Create and return a new instance of TimePickerDialog
            return new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT
                    , this, hour, minute, false);
        }

        //onTimeSet() callback method
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            // SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
            Date newDate = null;
            String strDateFormat = String.format("%02d:%02d", hourOfDay, minute);


          /*  String runtimeFrom =  strDateFormat;
            Log.e("tag", "RUNTIME---------->" + runtimeFrom);*/


          /*  strDateFormat = strDateFormat + " a";
            try {
                //newDate = spf.parse(strDateFormat1);
                //spf = new SimpleDateFormat("MMM/dd/yyyy");
                //strDateFormat1 = spf.format(newDate);
                System.out.println(strDateFormat1);
                //Log.e("tag", "dvhdsvjvj" + strDateFormat1);
            } catch (ParseException e) {
                e.printStackTrace();
            }*/


            SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mm a");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");


            try {
                Date dt = parseFormat.parse(strDateFormat);
                System.out.println(outputFormat.format(dt));
                end_timetv.setText(outputFormat.format(dt));
            } catch (ParseException exc) {
                exc.printStackTrace();
            }


        }

    }

    public static class TimePickerFragment2 extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //Use the current time as the default values for the time picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            //Create and return a new instance of TimePickerDialog
            return new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT
                    , this, hour, minute, false);
        }

        //onTimeSet() callback method
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String strDateFormat1 = txt_start_time.getText().toString();
            SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
            Date newDate = null;

            String strDateFormat = String.format("%02d:%02d", hourOfDay, minute);


            String runtimeFrom = strDateFormat1 + " " + strDateFormat;
            Log.e("tag", "RUNTIME---------->" + runtimeFrom);

            strDateFormat = strDateFormat + " a";
            try {
                newDate = spf.parse(strDateFormat1);
                spf = new SimpleDateFormat("MMM/dd/yyyy");
                strDateFormat1 = spf.format(newDate);
                System.out.println(strDateFormat1);
                Log.e("tag", "dvhdsvjvj" + strDateFormat1);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mm a");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");


            try {
                Date dt = parseFormat.parse(strDateFormat);
                System.out.println(outputFormat.format(dt));
                txt_start_time.setText(strDateFormat1 + " " + outputFormat.format(dt));
            } catch (ParseException exc) {
                exc.printStackTrace();
            }


        }

    }

    public static class TimePickerFragment3 extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //Use the current time as the default values for the time picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            //Create and return a new instance of TimePickerDialog
            return new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT
                    , this, hour, minute, false);
        }

        //onTimeSet() callback method
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String strDateFormat1 = txt_end_time.getText().toString();
            SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
            Date newDate = null;

            String strDateFormat = String.format("%02d:%02d", hourOfDay, minute);


            String runtimeFrom = strDateFormat1 + " " + strDateFormat;
            Log.e("tag", "RUNTIME---------->" + runtimeFrom);

            strDateFormat = strDateFormat + " a";
            try {
                newDate = spf.parse(strDateFormat1);
                spf = new SimpleDateFormat("MMM/dd/yyyy");
                strDateFormat1 = spf.format(newDate);
                System.out.println(strDateFormat1);
                Log.e("tag", "dvhdsvjvj" + strDateFormat1);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mm a");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");


            try {
                Date dt = parseFormat.parse(strDateFormat);
                System.out.println(outputFormat.format(dt));
                txt_end_time.setText(strDateFormat1 + " " + outputFormat.format(dt));
            } catch (ParseException exc) {
                exc.printStackTrace();
            }


        }

    }

    class ViewTimeSlotAdapter extends BaseAdapter {
        Typeface lato;
        String iti_date1, iti_date2, iti_merge;
        Animation anim;
        LinearLayout lnr_del_data;
        private Activity activity;
        private LayoutInflater inflater;
        private ArrayList<DayContent> timeslotItems;
        private int day;

        public ViewTimeSlotAdapter(Activity activity, ArrayList<DayContent> timeslotItems, int day) {
            this.activity = activity;
            this.timeslotItems = timeslotItems;
        }


        @Override
        public int getCount() {
            return timeslotItems.size();
        }

        @Override
        public Object getItem(int location) {
            return timeslotItems.get(location);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.view_time_slot, null);
                viewHolder = new ViewHolder();
                viewHolder.position = position;
                FontsOverride.overrideFonts(activity, convertView);
                lato = Typeface.createFromAsset(activity.getAssets(), "fonts/lato.ttf");
                viewHolder.txt_itinerary_time = convertView.findViewById(R.id.txt_itinerary_time);
                viewHolder.txt_itinerary_val = convertView.findViewById(R.id.txt_itinerary_val);
                viewHolder.txt_facilitator = convertView.findViewById(R.id.txt_facilitator);
                viewHolder.txt_facilitator_val = convertView.findViewById(R.id.txt_facilitator_val);
                viewHolder.txt_topic_agenda = convertView.findViewById(R.id.txt_topic_agenda);
                viewHolder.txt_agenda_val = convertView.findViewById(R.id.txt_agenda_val);
                viewHolder.txt_event_location = convertView.findViewById(R.id.txt_event_location);
                viewHolder.txt_location_val = convertView.findViewById(R.id.txt_location_val);
                viewHolder.img_delete_slot = convertView.findViewById(R.id.img_delete_slot);
                viewHolder.lnr_del_data = convertView.findViewById(R.id.lnr_del_data);

           /* viewHolder.img_delete_slot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   //Toast.makeText(activity, "item to delete at position "+position, Toast.LENGTH_SHORT).show();
                    timeslotItems.remove(position);
                    //DayFragment.notifyAdapter();
                    DayFragment.dialog.dismiss();

                }
            });*/


                viewHolder.img_delete_slot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StartSmartAnimation.startAnimation(viewHolder.lnr_del_data, AnimationType.SlideOutRight, 1000, 0, true);
                        timeslotItems.remove(position);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                timeslot_listview.setAdapter(viewTimeSlotAdapter);
                            }
                        }, 1000);
                    }
                });


                viewHolder.txt_itinerary_time.setTypeface(lato);
                viewHolder.txt_itinerary_val.setTypeface(lato);
                viewHolder.txt_facilitator.setTypeface(lato);
                viewHolder.txt_facilitator_val.setTypeface(lato);
                viewHolder.txt_topic_agenda.setTypeface(lato);
                viewHolder.txt_agenda_val.setTypeface(lato);
                viewHolder.txt_event_location.setTypeface(lato);
                viewHolder.txt_location_val.setTypeface(lato);


                DayContent dayContent = timeslotItems.get(position);
                //viewHolder.txt_itinerary_val.setText(dayContent.getItinerary_start_time());
                viewHolder.txt_facilitator_val.setText(dayContent.getFacilitator());
                viewHolder.txt_agenda_val.setText(dayContent.getEvent_agenta());
                viewHolder.txt_location_val.setText(dayContent.getLocation_venue());


                iti_date1 = dayContent.getItinerary_start_time();
                iti_date2 = dayContent.getItinerary_end_time();
                iti_merge = iti_date1 + " - " + iti_date2;
                viewHolder.txt_itinerary_val.setText(iti_merge);


            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            return convertView;
        }

        class ViewHolder {
            public TextView txt_day_count, txt_time_slot;
            public TextView txt_itinerary_time, txt_itinerary_val, txt_facilitator, txt_facilitator_val, txt_topic_agenda, txt_agenda_val, txt_event_location, txt_location_val;
            public LinearLayout lnr_del_data;
            public ImageView img_delete_slot;
            int position;


        }

    }
}