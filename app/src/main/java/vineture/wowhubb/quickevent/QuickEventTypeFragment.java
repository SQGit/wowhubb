package vineture.wowhubb.quickevent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import vineture.wowhubb.Activity.CreateEventActivity;
import vineture.wowhubb.Adapter.CustomAdapter;
import vineture.wowhubb.Adapter.ExpandableListAdapter;
import vineture.wowhubb.Adapter.ExpandableListDataPump;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.Fragment.WowtagFragment;
import vineture.wowhubb.GetFilePathFromDevice;
import vineture.wowhubb.data.EventData;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class QuickEventTypeFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static final int INTENT_REQUEST_GET_COVERIMAGES = 11;
    private static final int Time_id = 1;
    public static EditText eventdate_et, eventdateto_et, desc_et, eventtopic_et, eventaddcategory_et, eventdescr_et;
    public static String str_category, str_eventday, selectedCoverFilePath, strDateFormat1,strDateFormat2;
    public static TextInputLayout til_eventdays, til_spincategory, til_spindays, desc_til, eventdate_til, eventtopic_til, eventaddcategory_til, eventtimezone_til, eventdateto_til;
    public static String str_start;
    public static String str_end;
    public static AutoCompleteTextView eventtimezone_et;
    public static MaterialBetterSpinner eventDay_spn;
    public static Calendar selectedCal, selectedCalto, wowtagfrom, wowtagto;
    public static ImageView cover_iv, coverplus;
    public static FrameLayout framecoverpage, framestart, frameend;
    public static ScrollView scrollView;
    public EventData eventData;
    LinearLayout layout_eventdate, layout_eventdateto;
    Typeface lato;
    TextView helpfultips_tv;
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;
    Dialog dialog, cdialog, ctimedialog;
    String[] SPINNERLIST = {"Anniversary", "Baby Shower", "Birthday Party", "Holiday Party", "Wedding", "Baby Naming", "Graduation Party", "Team Outing", "Photo/File Shoot Event", "Bridal Shower Event", "Meeting", "Others", "Group Meetings"};
    String[] EVENTDAYLIST = {"1 Day Event", "2 Days Event", "3 Days Event"};
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    View view;
    DatePicker datePicker, dp;
    SharedPreferences.Editor editor;
    int strdayscounrt;
    TimePickerDialog.OnTimeSetListener time_listener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            // store the data in one string and set it to text
            String time1 = String.valueOf(hour) + ":" + String.valueOf(minute);
            //set_time.setText(time1);
        }
    };
    DatePicker.OnDateChangedListener dateChangedListener;

    public static QuickEventTypeFragment newInstance(int page, boolean isLast) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        if (isLast)
            args.putBoolean("isLast", true);
        final QuickEventTypeFragment fragment = new QuickEventTypeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        eventData = ((CreateEventActivity) getActivity()).eventData;


        if (view == null) {
            view = inflater.inflate(vineture.wowhubb.R.layout.activity_event_type, container, false);
            FontsOverride.overrideFonts(getActivity(), view);
            sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getActivity());
            edit = sharedPrefces.edit();
            CreateEventActivity.skiptv.setVisibility(View.INVISIBLE);
            lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");

            eventdate_et = view.findViewById(vineture.wowhubb.R.id.eventdate);
            eventdescr_et = view.findViewById(vineture.wowhubb.R.id.description_et);
            eventdateto_et = view.findViewById(vineture.wowhubb.R.id.eventdateto_et);
            desc_til = view.findViewById(vineture.wowhubb.R.id.til_description);
            eventtopic_til = view.findViewById(vineture.wowhubb.R.id.til_eventtopic);
            eventdate_til = view.findViewById(vineture.wowhubb.R.id.til_eventdate);
            eventdateto_til = view.findViewById(vineture.wowhubb.R.id.til_eventdateto);
            eventaddcategory_til = view.findViewById(vineture.wowhubb.R.id.til_eventaddcategory);
            eventtimezone_til = view.findViewById(vineture.wowhubb.R.id.til_eventtimezone);
            helpfultips_tv = view.findViewById(vineture.wowhubb.R.id.helpfultips_tv);
            til_spincategory = view.findViewById(vineture.wowhubb.R.id.til_spincategory);
            til_eventdays = view.findViewById(vineture.wowhubb.R.id.til_spindays);
            scrollView = view.findViewById(vineture.wowhubb.R.id.scrollView);


            desc_et = view.findViewById(vineture.wowhubb.R.id.description_et);
            eventtopic_et = view.findViewById(vineture.wowhubb.R.id.eventtopic_et);
            layout_eventdate = view.findViewById(vineture.wowhubb.R.id.layout_eventdate);
            layout_eventdateto = view.findViewById(vineture.wowhubb.R.id.layout_todate);
            eventaddcategory_et = view.findViewById(vineture.wowhubb.R.id.eventaddcategory_et);
            eventtimezone_et = view.findViewById(vineture.wowhubb.R.id.eventtimezone_et);
            til_spindays = view.findViewById(vineture.wowhubb.R.id.til_spindays);
            cover_iv = (ImageView) view.findViewById(vineture.wowhubb.R.id.coverpage_iv);
            coverplus = view.findViewById(vineture.wowhubb.R.id.coverplusiv);
            framecoverpage = view.findViewById(vineture.wowhubb.R.id.frame_cover);
            framestart = view.findViewById(vineture.wowhubb.R.id.frame_startdate);

            desc_til.setTypeface(lato);
            eventdate_til.setTypeface(lato);
            eventdateto_til.setTypeface(lato);
            eventtopic_til.setTypeface(lato);
            eventaddcategory_til.setTypeface(lato);
            eventtimezone_til.setTypeface(lato);
            til_spindays.setTypeface(lato);
            til_spincategory.setTypeface(lato);
            selectedCal = Calendar.getInstance();

            selectedCalto = Calendar.getInstance();
            wowtagto = Calendar.getInstance();
            eventtimezone_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        hideKeyboard();
                        eventdate_til.setError("");
                        layout_eventdate.setFocusable(true);
                    }
                    return true;
                }
            });
            layout_eventdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (str_eventday != null) {
                        eventdate_til.setError("");
                        cdialog = new Dialog(getActivity());
                        cdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        cdialog.setContentView(vineture.wowhubb.R.layout.dialog_customcalendar);
                        View v1 = cdialog.getWindow().getDecorView().getRootView();

                        dp = cdialog.findViewById(vineture.wowhubb.R.id.datePicker);
                        TextView ok = cdialog.findViewById(vineture.wowhubb.R.id.oktv);
                        TextView cancel = cdialog.findViewById(vineture.wowhubb.R.id.canceltv);

                        //   dp.setOnDateChangedListener(dateChangedListener);
                        FontsOverride.overrideFonts(cdialog.getContext(), v1);
                        dp.setMinDate(System.currentTimeMillis() - 1000);
                        cdialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                Window view1 = ((Dialog) cdialog).getWindow();

                            }
                        });

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cdialog.dismiss();
                            }
                        });


                        ok.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {

                                int day = dp.getDayOfMonth();
                                int month = dp.getMonth();
                                int year = dp.getYear();

                                wowtagfrom = Calendar.getInstance();
                                selectedCal.set(year, month, day);
                                wowtagfrom.set(year, month, day);

                                Log.e("tag", "values---------------------->>>>>>>" + day + month + year);

                                populateSetDate(year, month + 1, day);
                                cdialog.dismiss();
                            }
                        });
                        cdialog.show();
                    } else {
                        SpannableString s = new SpannableString("Select Event Days");
                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        // til_eventdays.setError(s);
                        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                        eventDay_spn.setFocusable(true);
                    }
                }
            });


            layout_eventdateto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (str_eventday != null) {
                        eventdate_til.setError("");
                    cdialog = new Dialog(getActivity());
                    cdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    cdialog.setContentView(vineture.wowhubb.R.layout.dialog_customcalendar);
                    View v1 = cdialog.getWindow().getDecorView().getRootView();

                    datePicker = cdialog.findViewById(vineture.wowhubb.R.id.datePicker);
                    TextView ok = cdialog.findViewById(vineture.wowhubb.R.id.oktv);
                    TextView cancel = cdialog.findViewById(vineture.wowhubb.R.id.canceltv);

                    FontsOverride.overrideFonts(cdialog.getContext(), v1);
                    //datePicker.setMinDate(System.currentTimeMillis() - 1000);
                    Log.e("tag", "wowtagdfg---------------->>>>>>" + wowtagto);

                    try {
                        long selectedMilli = wowtagfrom.getTimeInMillis();
                        datePicker.setMinDate(selectedMilli - 1000);

                        long selectedMillisec = wowtagto.getTimeInMillis();
                        datePicker.setMaxDate(selectedMillisec - 1000);

                    } catch (NullPointerException e) {

                    }

                    cdialog.setOnShowListener(new DialogInterface.OnShowListener() {

                        public void onShow(DialogInterface dialogInterface) {
                            Window view1 = ((Dialog) cdialog).getWindow();

                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            cdialog.dismiss();
                        }
                    });


                    ok.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            ///cdialog.dismiss();
                            int day = datePicker.getDayOfMonth();
                            int month = datePicker.getMonth();
                            int year = datePicker.getYear();

                            selectedCalto.set(year, month, day);
                            wowtagto.set(year, month, day);
                            Log.e("tag", "date-------------->" + selectedCalto + 1);

                            populateSetDateTo(year, month + 1, day);
                            cdialog.dismiss();
                        }
                    });
                    cdialog.show();
                    } else {
                        SpannableString s = new SpannableString("Select Event Days");
                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        // til_eventdays.setError(s);
                        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                        eventDay_spn.setFocusable(true);
                    }
                }
            });
            framecoverpage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkPermission()) {
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        pickIntent.setType("image/*");
                        startActivityForResult(pickIntent, INTENT_REQUEST_GET_COVERIMAGES);
                    }
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
                            Window view1 = ((Dialog) dialog).getWindow();

                        }
                    });

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    expandableListView = (ExpandableListView) dialog.findViewById(vineture.wowhubb.R.id.expandableListView);
                    expandableListDetail = ExpandableListDataPump.getData();
                    expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                    expandableListAdapter = new ExpandableListAdapter(dialog.getContext(), expandableListTitle, expandableListDetail);

                    expandableListView.setAdapter(expandableListAdapter);

                    expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {
                        public void onGroupExpand(int groupPosition) {
                        }
                    });

                    expandableListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
                        public void onGroupCollapse(int groupPosition) {
                        }
                    });

                    expandableListView.setOnChildClickListener(new OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v,
                                                    int groupPosition, int childPosition, long id) {
                            return true;
                        }
                    });

                    dialog.show();
                }
            });

            MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner) view.findViewById(vineture.wowhubb.R.id.category_spn);
            View view1 = materialDesignSpinner.getRootView();
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
            materialDesignSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    FontsOverride.overrideFonts(getContext(), view);
                    str_category = adapterView.getItemAtPosition(i).toString();

                    Log.e("tag", "str_category------>" + str_category);
                    eventDay_spn.requestFocus();
                    eventDay_spn.performClick();
                    if (str_category.equals("Others")) {
                        eventaddcategory_til.setVisibility(View.VISIBLE);
                        edit.putString("str_category", "others");
                        edit.commit();
                    } else {
                        eventaddcategory_til.setVisibility(View.GONE);
                        edit.putString("str_category", str_category);
                        edit.commit();
                    }

                }
            });
            eventDay_spn = (MaterialBetterSpinner) view.findViewById(vineture.wowhubb.R.id.eventday_spn);
            FontsOverride.overrideFonts(getActivity(), view1);


            final CustomAdapter eventdayAdapter = new CustomAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, EVENTDAYLIST) {
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
            eventDay_spn.setAdapter(eventdayAdapter);
            eventDay_spn.setTypeface(lato);

            eventDay_spn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    FontsOverride.overrideFonts(getContext(), view);
                    str_eventday = adapterView.getItemAtPosition(i).toString();
                    Log.e("tag", "str_category------>" + str_eventday);
                    if (str_eventday.equals("1 Day Event")) {
                        til_eventdays.setError("");

                        edit.putInt("str_eventday", 1);
                        edit.commit();
                        eventdate_et.setText("");
                        eventdateto_et.setText("");

                    } else if (str_eventday.equals("2 Days Event")) {
                        edit.putInt("str_eventday", 2);
                        edit.commit();

                        til_eventdays.setError("");
                        eventdate_et.setText("");
                        eventdateto_et.setText("");
                    } else if (str_eventday.equals("3 Days Event")) {
                        edit.putInt("str_eventday", 3);
                        edit.commit();
                        til_eventdays.setError("");

                        eventdate_et.setText("");
                        eventdateto_et.setText("");
                    } else {
                        edit.putInt("str_eventday", 1);
                        edit.commit();
                        til_eventdays.setError("");

                        eventdate_et.setText("");
                        eventdateto_et.setText("");
                    }
                }
            });
        } else {

        }
        return view;
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }


    private void populateSetDate(int year, int month, int day) {
        String strDateFormatTo = month + "/" + day + "/" + year;
        SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
        Date newDate = null;

        try {
            newDate = spf.parse(strDateFormatTo);
            spf = new SimpleDateFormat("MMM/dd/yyyy");
            strDateFormatTo = spf.format(newDate);
            eventdate_et.setText(strDateFormatTo);
            Log.e("tag", "date------------>" + strDateFormatTo);

            ctimedialog = new Dialog(getActivity());
            ctimedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            ctimedialog.setContentView(vineture.wowhubb.R.layout.dialog_customtime);
            View v1 = ctimedialog.getWindow().getDecorView().getRootView();

            final TimePicker dp = ctimedialog.findViewById(vineture.wowhubb.R.id.timePicker);
            TextView ok = ctimedialog.findViewById(vineture.wowhubb.R.id.oktv);
            TextView cancel = ctimedialog.findViewById(vineture.wowhubb.R.id.canceltv);

            FontsOverride.overrideFonts(ctimedialog.getContext(), v1);


            ctimedialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    Window view1 = ((Dialog) ctimedialog).getWindow();

                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
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
                    populateSetTime(hour, min);
                }
            });
            ctimedialog.show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void populateSetTime(int hourOfDay, int minute) {
        strDateFormat1 = eventdate_et.getText().toString();
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
            eventdate_et.setText(strDateFormat1 + " " + outputFormat.format(dt));
            str_start = outputFormat.format(dt);
            str_end = outputFormat.format(dt);
            strdayscounrt = sharedPrefces.getInt("str_eventday", 0);
            if (strdayscounrt == 1) {
                selectedCal.add(Calendar.DATE, 1);
                SimpleDateFormat spfdate = new SimpleDateFormat("MMM/dd/yyyy");
                String date = spfdate.format(selectedCal.getTime());
                Log.e("tag", "vvvvvvvvvvvvvvvvv-------" + selectedCal.get(Calendar.MONTH) + selectedCal.get(Calendar.DATE) + selectedCal.get(Calendar.YEAR));
                Log.e("tag", "date-------" + date);

                eventdateto_et.setText("");
                eventdateto_et.setText(date + " "+str_start);
                wowtagto = Calendar.getInstance();
                int month = selectedCal.get(Calendar.MONTH);
                int day = selectedCal.get(Calendar.DATE);
                int year = selectedCal.get(Calendar.YEAR);
                wowtagto.set(year, month, day);
            } else if (strdayscounrt == 2) {
                selectedCal.add(Calendar.DATE, 2);
                SimpleDateFormat spfdate = new SimpleDateFormat("MMM/dd/yyyy");
                String date = spfdate.format(selectedCal.getTime());
                Log.e("tag", "vvvvvvvvvvvvvvvvv-------" + selectedCal);
                eventdateto_et.setText("");
                eventdateto_et.setText(date + " "+str_start);
                wowtagto = Calendar.getInstance();
                int month = selectedCal.get(Calendar.MONTH);
                int day = selectedCal.get(Calendar.DATE);
                int year = selectedCal.get(Calendar.YEAR);
                wowtagto.set(year, month, day);
            } else if (strdayscounrt == 3) {
                selectedCal.add(Calendar.DATE, 3);
                SimpleDateFormat spfdate = new SimpleDateFormat("MMM/dd/yyyy");
                String date = spfdate.format(selectedCal.getTime());

                eventdateto_et.setText("");
                eventdateto_et.setText(date + " "+str_start);
                wowtagto = Calendar.getInstance();
                int month = selectedCal.get(Calendar.MONTH);
                int day = selectedCal.get(Calendar.DATE);
                int year = selectedCal.get(Calendar.YEAR);
                wowtagto.set(year, month, day);
            } else {
                eventdateto_et.setText("");
            }

        } catch (ParseException exc) {
            exc.printStackTrace();
        }


    }

    private void populateSetDateTo(int year, int month, int day) {
        String strDateFormatTo = month + "/" + day + "/" + year;
        SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
        Date newDate = null;

        try {
            newDate = spf.parse(strDateFormatTo);
            spf = new SimpleDateFormat("MMM/dd/yyyy");
            strDateFormatTo = spf.format(newDate);
            eventdateto_et.setText(strDateFormatTo);
            Log.e("tag", "dvhdsvjvj" + strDateFormatTo);
            eventData.eventstarttime = "";
            eventData.eventendtime = "";
            try {
                WowtagFragment.fromtime_tv.setText("");
                WowtagFragment.totime_tv.setText("");
            } catch (NullPointerException e) {

            }

            ctimedialog = new Dialog(getActivity());
            ctimedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            ctimedialog.setContentView(vineture.wowhubb.R.layout.dialog_customtime);
            View v1 = ctimedialog.getWindow().getDecorView().getRootView();

            final TimePicker dp = ctimedialog.findViewById(vineture.wowhubb.R.id.timePicker);
            TextView ok = ctimedialog.findViewById(vineture.wowhubb.R.id.oktv);
            TextView cancel = ctimedialog.findViewById(vineture.wowhubb.R.id.canceltv);

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
                    populateSetTimeTo(hour, min);
                }
            });
            ctimedialog.show();
            //getActivity().showDialog(Time_id);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void populateSetTimeTo(int hourOfDay, int minute) {
        strDateFormat2 = eventdateto_et.getText().toString();
        SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
        Date newDate = null;

        String strDateFormat = String.format("%02d:%02d", hourOfDay, minute);
        String runtimeFrom = strDateFormat2 + " " + strDateFormat;
        Log.e("tag", "RUNTIME---------->" + runtimeFrom);
        strDateFormat = strDateFormat + " a";

        try {
            newDate = spf.parse(strDateFormat2);
            spf = new SimpleDateFormat("MMM/dd/yyyy");
            strDateFormat2 = spf.format(newDate);
            System.out.println(strDateFormat2);
            Log.e("tag", "dvhdsvjvj" + strDateFormat2);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mm a");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");

        try {
            Date dt = parseFormat.parse(strDateFormat);
            System.out.println(outputFormat.format(dt));
            eventdateto_et.setText(strDateFormat2 + " " + outputFormat.format(dt));
            str_end = outputFormat.format(dt);
        } catch (ParseException exc) {
            exc.printStackTrace();
        }


    }

    @Override
    public void onDestroyView() {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        super.onDestroyView();
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED) && (result2 == PackageManager.PERMISSION_GRANTED)) {
            return true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            return false;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Log.e("tag", "code------------->" + resultCode);
            if (requestCode == INTENT_REQUEST_GET_COVERIMAGES) {
                Uri selectedMediaUri = data.getData();
                selectedCoverFilePath = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    // Log.d(TAG, String.valueOf(bitmap));
                    cover_iv.setImageBitmap(bitmap);
                    coverplus.setVisibility(View.GONE);
                    Log.e("tag", "code------------->" + selectedCoverFilePath);
                    edit.putString("coverpage", selectedCoverFilePath);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        //setDate(cal);
    }

    @Override
    public void onPause() {
        super.onPause();
        setData();
    }

    private void setData() {
        eventData.eventname = eventtopic_et.getText().toString();
        eventData.eventcityzone = eventtimezone_et.getText().toString().trim();
        eventData.eventdays = eventdate_et.getText().toString();
        eventData.eventdesc = desc_et.getText().toString();
        eventData.eventstartdate = eventdate_et.getText().toString();
        eventData.eventenddate = eventdateto_et.getText().toString();
        eventData.eventcover = selectedCoverFilePath;


    }

    @SuppressLint("ValidFragment")
    public static class SelectEventDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog da = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
            //Set a title for DatePickerDialog
            da.setTitle("Set a Date");
            // da.getDatePicker().setDrawingCacheBackgroundColor(getContext().getColor(R.color.textcolr));
            //DatePickerDialog da = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            //Calendar cal = new GregorianCalendar(year, month, day);
            DatePicker dp = da.getDatePicker();
            //Set the DatePicker minimum date selection to current date
            dp.setMinDate(c.getTimeInMillis());

            return da;

        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }

        public void populateSetDate(int year, int month, int day) {
            String strDateFormatTo = month + "/" + day + "/" + year;
            SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
            Date newDate = null;

            try {
                newDate = spf.parse(strDateFormatTo);
                spf = new SimpleDateFormat("MMM/dd/yyyy");
                strDateFormatTo = spf.format(newDate);
                eventdate_et.setText(strDateFormatTo);
                Log.e("tag", "dvhdsvjvj" + strDateFormatTo);
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "TimePicker");
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
           /* return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));*/
            // return new TimePickerDialog(getActivity(), this, hour, minute,false);

            return new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT
                    , this, hour, minute, false);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            strDateFormat1 = eventdate_et.getText().toString();
            SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
            Date newDate = null;

            String strDateFormat = String.format("%02d:%02d", hourOfDay, minute);


            String runtimeFrom = strDateFormat1 + " " + strDateFormat;
            Log.e("tag", "RUNTIME---------->" + runtimeFrom);
            //  edit.putString("runtimeFrom", runtimeFrom);
            // edit.commit();
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
                eventdate_et.setText(strDateFormat1 + " " + outputFormat.format(dt));
            } catch (ParseException exc) {
                exc.printStackTrace();
            }


        }

    }

    @SuppressLint("ValidFragment")
    public static class SelectEventDateToFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog da = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
            //Set a title for DatePickerDialog
            da.setTitle("Set a Date");
            //  da.getDatePicker().setDrawingCacheBackgroundColor(getContext().getColor(R.color.textcolr));

            //DatePickerDialog da = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            //Calendar cal = new GregorianCalendar(year, month, day);
            DatePicker dp = da.getDatePicker();
            //Set the DatePicker minimum date selection to current date
            dp.setMinDate(c.getTimeInMillis());

            return da;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
            // populateSetDate
        }


        public void populateSetDate(int year, int month, int day) {
            String strDateFormatTo = month + "/" + day + "/" + year;
            SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
            Date newDate = null;

            try {
                newDate = spf.parse(strDateFormatTo);
                spf = new SimpleDateFormat("MMM/dd/yyyy");
                strDateFormatTo = spf.format(newDate);
                eventdateto_et.setText(strDateFormatTo);
                Log.e("tag", "dvhdsvjvj" + strDateFormatTo);

                DialogFragment newFragment = new TimePickerToFragment();
                newFragment.show(getFragmentManager(), "timePicker");
                //DialogFragment newFragment = new TimePickerToFragment();
                // newFragment.show(getFragmentManager(), "TimePicker");
                //  getActivity().showDialog(Time_id);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    @SuppressLint("ValidFragment")
    public static class TimePickerToFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //Use the current time as the default values for the time picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            // DatePickerDialog da = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);

            //Create and return a new instance of TimePickerDialog
            return new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, hour, minute, false);
        }

        //onTimeSet() callback method
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            strDateFormat1 = eventdateto_et.getText().toString();
            SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
            Date newDate = null;

            String strDateFormat = String.format("%02d:%02d", hourOfDay, minute);


            String runtimeFrom = strDateFormat1 + " " + strDateFormat;
            Log.e("tag", "RUNTIME---------->" + runtimeFrom);
            //  edit.putString("runtimeFrom", runtimeFrom);
            // edit.commit();
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
                eventdateto_et.setText(strDateFormat1 + " " + outputFormat.format(dt));
            } catch (ParseException exc) {
                exc.printStackTrace();
            }


        }

    }
}