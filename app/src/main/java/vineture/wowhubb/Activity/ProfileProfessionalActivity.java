package vineture.wowhubb.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vineture.wowhubb.Adapter.CustomListAdapter;
import vineture.wowhubb.Adapter.ProfileCertificationsAdapter;
import vineture.wowhubb.Adapter.ProfileCollegeAdapter;
import vineture.wowhubb.Adapter.ProfileCustomAdapter;
import vineture.wowhubb.Adapter.ProfileExperienceAdapter;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.Profile.Certification;
import vineture.wowhubb.Profile.College;
import vineture.wowhubb.Profile.Message;
import vineture.wowhubb.Profile.Profile;
import vineture.wowhubb.Profile.Workexperience;
import vineture.wowhubb.R;
import vineture.wowhubb.Utils.ApiClient;
import vineture.wowhubb.Utils.ApiInterface;
import vineture.wowhubb.Utils.HttpUtils;
import vineture.wowhubb.app.ProfileModel;

/**
 * Created by Salman on 05-10-2017.
 */

public class ProfileProfessionalActivity extends Activity {
    private static Retrofit retrofit = null;
    ApiInterface apiInterface;
    //MaterialBetterSpinner year_spn;
    Spinner year_spn, college_fromspn, college_tospn;
    SharedPreferences.Editor editor;
    String token, skills_str, year_str, cerifcation_str, college_str, fromyear_str, toyear_str, title_str, company_str, location_str, description_str;
    EditText certificate_et, skills_et;
    LinearLayout addintro_lv, addeducation_lv, addworkexp_lv, addprofessionalskills_lv, addCertifications_lv;
    LinearLayout education_lv, workexp_lv, professionalskills_lv, Certifications_lv;
    LinearLayout college_edit, work_edit, skills_edit, cert_edit;
    Dialog d, dialog, loader_dialog;
    TextView backtv, wowtagtv, yeartv;
    ArrayList<ProfileModel> modelList;

    ArrayList<String> skillList;
    ProfileCustomAdapter adapter;
    ProfileCertificationsAdapter profileCertificationsAdapter;
    ProfileCollegeAdapter profileCollegeAdapter;
    ProfileExperienceAdapter profileExperienceAdapter;

    String[] YEAR = {"Year", "2018", "2017", "2016", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005", "2004", "2003", "2002", "2001", "2000"};
    CustomListAdapter arrayAdapter;
    ArrayList<String> yearList;
    Typeface lato;
    Message profileList;
    List<College> collegeList;
    List<Certification> certificationList;
    List<Workexperience> workexperienceList;
    List<String> professionalSkills;
    ArrayList<String> professionalSkillsList;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professional_profile);

        lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ProfileProfessionalActivity.this, v1);

        backtv = findViewById(R.id.backtv);
        addintro_lv = findViewById(R.id.addintro_lv);
        addeducation_lv = findViewById(R.id.addeducation_lv);
        addworkexp_lv = findViewById(R.id.addworkexp_lv);
        addprofessionalskills_lv = findViewById(R.id.addprofessionalskills_lv);
        addCertifications_lv = findViewById(R.id.addCertifications_lv);
        education_lv = findViewById(R.id.education_lv);
        workexp_lv = findViewById(R.id.workexp_lv);
        professionalskills_lv = findViewById(R.id.professionalskills_lv);
        Certifications_lv = findViewById(R.id.Certifications_lv);
        college_edit = findViewById(R.id.college_edit);
        cert_edit = findViewById(R.id.cert_edit);
        skills_edit = findViewById(R.id.skills_edit);
        work_edit = findViewById(R.id.work_edit);
        wowtagtv=findViewById(R.id.wowtagid);

        skillList = new ArrayList<>();
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ProfileProfessionalActivity.this);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");

        loader_dialog = new Dialog(ProfileProfessionalActivity.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        connectAndGetApiData();
        loader_dialog.setContentView(vineture.wowhubb.R.layout.test_loader);
        yearList = new ArrayList<>(Arrays.asList(YEAR));
        arrayAdapter = new CustomListAdapter(ProfileProfessionalActivity.this, android.R.layout.simple_dropdown_item_1line, yearList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
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
                tv.setTextSize(15);
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


        //-----------------------------------BACK NAVIGATION--------------------------------------//

        backtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //-----------------------------ADD EDUCATION ---------------------------------------//

        addeducation_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d = new Dialog(ProfileProfessionalActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                d.setContentView(R.layout.dialog_professionalcollege);
                d.setCanceledOnTouchOutside(false);
                ImageView close = d.findViewById(R.id.closeiv);
                ListView lv = d.findViewById(R.id.listview);
                TextView save = d.findViewById(R.id.save_tv);
                TextView addmore = d.findViewById(R.id.addmore_tv);
                final EditText collegename_et = d.findViewById(R.id.certificate_et);
                college_fromspn = d.findViewById(vineture.wowhubb.R.id.from_year);
                college_tospn = d.findViewById(vineture.wowhubb.R.id.to_year);
                college_fromspn.setAdapter(arrayAdapter);
                college_tospn.setAdapter(arrayAdapter);
                modelList = new ArrayList<ProfileModel>();
                profileCollegeAdapter = new ProfileCollegeAdapter(getApplicationContext(), modelList, skillList);
                lv.setAdapter(profileCollegeAdapter);

                if (collegeList.size() > 0) {

                    for (int i = 0; i < collegeList.size(); i++) {
                        ProfileModel md = new ProfileModel(collegeList, 1);
                        md.setCollege(collegeList.get(i).getCollege());
                        md.setFrom(collegeList.get(i).getFrom());
                        md.setTo(collegeList.get(i).getTo());
                        modelList.add(md);
                    }

                }


                college_fromspn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        FontsOverride.overrideFonts(ProfileProfessionalActivity.this, view);
                        fromyear_str = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                college_tospn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        FontsOverride.overrideFonts(ProfileProfessionalActivity.this, view);
                        toyear_str = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });

                d.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) d).getWindow();
                        view1.setBackgroundDrawableResource(R.drawable.border_graybg);
                    }
                });

                final View v2 = d.getWindow().getDecorView().getRootView();
                FontsOverride.overrideFonts(ProfileProfessionalActivity.this, v2);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        college_str = collegename_et.getText().toString().trim();
                        if (!collegename_et.getText().toString().trim().equalsIgnoreCase("")) {
                            if (fromyear_str != null && !fromyear_str.equals("Year")) {
                                if (toyear_str != null && !toyear_str.equals("Year")) {
                                    ProfileModel md = new ProfileModel(college_str, fromyear_str, toyear_str);
                                    md.setCollege(college_str);
                                    md.setFrom(fromyear_str);
                                    md.setTo(toyear_str);
                                    modelList.add(md);
                                    hideKeyboard(v2);
                                    new updateColleges().execute();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Select To Year",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Select From Year",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            if (modelList.size() > 0) {
                                hideKeyboard(v2);
                                Log.e("tag", "jsArray--------------->>>" + modelList);
                                new updateColleges().execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Enter College Name",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                });

                addmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        college_str = collegename_et.getText().toString().trim();
                        if (college_str.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Enter Certification",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            ProfileModel md = new ProfileModel(college_str, fromyear_str, toyear_str);
                            md.setCollege(college_str);
                            md.setFrom(fromyear_str);
                            md.setTo(toyear_str);
                            modelList.add(md);
                            collegename_et.setText("");
                            updateSpinner(college_fromspn, yearList);
                            updateSpinner(college_tospn, yearList);

                            hideKeyboard(v2);

                        }
                    }
                });


                d.show();
            }
        });



        college_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d = new Dialog(ProfileProfessionalActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                d.setContentView(R.layout.dialog_professionalcollege);
                d.setCanceledOnTouchOutside(false);
                ImageView close = d.findViewById(R.id.closeiv);
                ListView lv = d.findViewById(R.id.listview);
                TextView save = d.findViewById(R.id.save_tv);
                TextView addmore = d.findViewById(R.id.addmore_tv);
                final EditText collegename_et = d.findViewById(R.id.certificate_et);
                college_fromspn = d.findViewById(vineture.wowhubb.R.id.from_year);
                college_tospn = d.findViewById(vineture.wowhubb.R.id.to_year);
                college_fromspn.setAdapter(arrayAdapter);
                college_tospn.setAdapter(arrayAdapter);
                modelList = new ArrayList<ProfileModel>();
                profileCollegeAdapter = new ProfileCollegeAdapter(getApplicationContext(), modelList, skillList);
                lv.setAdapter(profileCollegeAdapter);

                if (collegeList.size() > 0) {

                    for (int i = 0; i < collegeList.size(); i++) {
                        ProfileModel md = new ProfileModel(collegeList, 1);
                        md.setCollege(collegeList.get(i).getCollege());
                        md.setFrom(collegeList.get(i).getFrom());
                        md.setTo(collegeList.get(i).getTo());
                        modelList.add(md);
                    }

                }


                college_fromspn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        FontsOverride.overrideFonts(ProfileProfessionalActivity.this, view);
                        fromyear_str = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                college_tospn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        FontsOverride.overrideFonts(ProfileProfessionalActivity.this, view);
                        toyear_str = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });

                d.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) d).getWindow();
                        view1.setBackgroundDrawableResource(R.drawable.border_graybg);
                    }
                });

                final View v2 = d.getWindow().getDecorView().getRootView();
                FontsOverride.overrideFonts(ProfileProfessionalActivity.this, v2);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        college_str = collegename_et.getText().toString().trim();
                        if (!collegename_et.getText().toString().trim().equalsIgnoreCase("")) {
                            if (fromyear_str != null && !fromyear_str.equals("Year")) {
                                if (toyear_str != null && !toyear_str.equals("Year")) {
                                    ProfileModel md = new ProfileModel(college_str, fromyear_str, toyear_str);
                                    md.setCollege(college_str);
                                    md.setFrom(fromyear_str);
                                    md.setTo(toyear_str);
                                    modelList.add(md);
                                    hideKeyboard(v2);
                                    new updateColleges().execute();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Select To Year",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Select From Year",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            if (modelList.size() > 0) {
                                hideKeyboard(v2);
                                Log.e("tag", "jsArray--------------->>>" + modelList);
                                new updateColleges().execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Enter College Name",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                });

                addmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        college_str = collegename_et.getText().toString().trim();
                        if (college_str.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Enter Certification",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            ProfileModel md = new ProfileModel(college_str, fromyear_str, toyear_str);
                            md.setCollege(college_str);
                            md.setFrom(fromyear_str);
                            md.setTo(toyear_str);
                            modelList.add(md);
                            collegename_et.setText("");
                            updateSpinner(college_fromspn, yearList);
                            updateSpinner(college_tospn, yearList);

                            hideKeyboard(v2);

                        }
                    }
                });


                d.show();
            }
        });


        //-----------------------------ADD WORK EXPERIENCE---------------------------------------//



        addworkexp_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d = new Dialog(ProfileProfessionalActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                d.setContentView(R.layout.dialog_professionalexperience);
                d.setCanceledOnTouchOutside(false);
                ImageView close = d.findViewById(R.id.closeiv);
                final EditText title_et = d.findViewById(R.id.title_et);
                final EditText company_et = d.findViewById(R.id.company_et);
                final EditText location_et = d.findViewById(R.id.location_et);
                final EditText description_et = d.findViewById(R.id.description_et);
                ListView lv = d.findViewById(R.id.listview);
                TextView save = d.findViewById(R.id.save_tv);
                TextView addmore = d.findViewById(R.id.addmore_tv);
                college_fromspn = d.findViewById(vineture.wowhubb.R.id.from_year);
                college_tospn = d.findViewById(vineture.wowhubb.R.id.to_year);
                college_fromspn.setAdapter(arrayAdapter);
                college_tospn.setAdapter(arrayAdapter);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });

                d.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) d).getWindow();
                        view1.setBackgroundDrawableResource(R.drawable.border_graybg);
                    }
                });

                final View v2 = d.getWindow().getDecorView().getRootView();
                FontsOverride.overrideFonts(ProfileProfessionalActivity.this, v2);
                d.show();
                modelList = new ArrayList<ProfileModel>();
                profileExperienceAdapter = new ProfileExperienceAdapter(getApplicationContext(), modelList, skillList);
                lv.setAdapter(profileExperienceAdapter);

                if (workexperienceList.size() > 0) {

                    for (int i = 0; i < workexperienceList.size(); i++) {
                        ProfileModel md = new ProfileModel(workexperienceList, "s");
                        md.setTitle(workexperienceList.get(i).getTitle());
                        md.setFrom(workexperienceList.get(i).getFromyear());
                        md.setTo(workexperienceList.get(i).getToyear());
                        md.setCompany(workexperienceList.get(i).getCompany());
                        md.setLocation(workexperienceList.get(i).getLocation());
                        md.setDescription(workexperienceList.get(i).getDescription());
                        modelList.add(md);
                    }

                }


                college_fromspn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        FontsOverride.overrideFonts(ProfileProfessionalActivity.this, view);
                        fromyear_str = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                college_tospn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        FontsOverride.overrideFonts(ProfileProfessionalActivity.this, view);
                        toyear_str = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        title_str = title_et.getText().toString();
                        company_str = company_et.getText().toString();
                        location_str = location_et.getText().toString();
                        description_str = description_et.getText().toString();


                        if (!title_et.getText().toString().trim().equalsIgnoreCase("")) {
                            if (!company_et.getText().toString().trim().equalsIgnoreCase("")) {
                                if (!location_et.getText().toString().trim().equalsIgnoreCase("")) {
                                    if (fromyear_str != null && !fromyear_str.equals("Year")) {
                                        if (toyear_str != null && !toyear_str.equals("Year")) {
                                            if (!description_et.getText().toString().trim().equalsIgnoreCase("")) {
                                                ProfileModel md = new ProfileModel(title_str, company_str, location_str, fromyear_str, toyear_str, description_str);
                                                md.setTitle(title_str);
                                                md.setCompany(company_str);
                                                md.setLocation(location_str);
                                                md.setFrom(fromyear_str);
                                                md.setTo(toyear_str);
                                                md.setDescription(description_str);
                                                modelList.add(md);
                                                skillList.add(skills_str);

                                                hideKeyboard(v2);
                                                Log.e("tag", "jsArray--------------->>>" + modelList);
                                                new updateWorkExperience().execute();

                                            } else {
                                                Toast.makeText(getApplicationContext(), "Enter Description",
                                                        Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Select To Year",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Select From Year",
                                                Toast.LENGTH_SHORT).show();
                                    }


                                } else {
                                    Toast.makeText(getApplicationContext(), "Enter Location",
                                            Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Enter Company",
                                        Toast.LENGTH_SHORT).show();
                            }


                            if (year_str != null && !year_str.equals("Year")) {


                            } else {
                                Toast.makeText(getApplicationContext(), "Select From Year",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            if (modelList.size() > 0) {
                                hideKeyboard(v2);
                                Log.e("tag", "jsArray--------------->>>" + modelList);
                                new updateCerification().execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Enter Title",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                });

                addmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        title_str = title_et.getText().toString();
                        company_str = company_et.getText().toString();
                        location_str = location_et.getText().toString();
                        description_str = description_et.getText().toString();

                        if (skills_str.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Fields Invalid",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            ProfileModel md = new ProfileModel(title_str, company_str, location_str, fromyear_str, toyear_str, description_str);
                            md.setTitle(title_str);
                            md.setCompany(company_str);
                            md.setLocation(location_str);
                            md.setFrom(fromyear_str);
                            md.setTo(toyear_str);
                            md.setDescription(description_str);
                            modelList.add(md);
                            hideKeyboard(v2);
                            title_et.setText("");
                            company_et.setText("");
                            location_et.setText("");
                            description_et.setText("");
                            updateSpinner(college_fromspn, yearList);
                            updateSpinner(college_tospn, yearList);


                        }
                    }
                });

            }
        });




        work_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d = new Dialog(ProfileProfessionalActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                d.setContentView(R.layout.dialog_professionalexperience);
                d.setCanceledOnTouchOutside(false);
                ImageView close = d.findViewById(R.id.closeiv);
                final EditText title_et = d.findViewById(R.id.title_et);
                final EditText company_et = d.findViewById(R.id.company_et);
                final EditText location_et = d.findViewById(R.id.location_et);
                final EditText description_et = d.findViewById(R.id.description_et);
                ListView lv = d.findViewById(R.id.listview);
                TextView save = d.findViewById(R.id.save_tv);
                TextView addmore = d.findViewById(R.id.addmore_tv);
                college_fromspn = d.findViewById(vineture.wowhubb.R.id.from_year);
                college_tospn = d.findViewById(vineture.wowhubb.R.id.to_year);
                college_fromspn.setAdapter(arrayAdapter);
                college_tospn.setAdapter(arrayAdapter);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });

                d.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) d).getWindow();
                        view1.setBackgroundDrawableResource(R.drawable.border_graybg);
                    }
                });

                final View v2 = d.getWindow().getDecorView().getRootView();
                FontsOverride.overrideFonts(ProfileProfessionalActivity.this, v2);
                d.show();
                modelList = new ArrayList<ProfileModel>();
                profileExperienceAdapter = new ProfileExperienceAdapter(getApplicationContext(), modelList, skillList);
                lv.setAdapter(profileExperienceAdapter);

                if (workexperienceList.size() > 0) {

                    for (int i = 0; i < workexperienceList.size(); i++) {
                        ProfileModel md = new ProfileModel(workexperienceList, "s");
                        md.setTitle(workexperienceList.get(i).getTitle());
                        md.setFrom(workexperienceList.get(i).getFromyear());
                        md.setTo(workexperienceList.get(i).getToyear());
                        md.setCompany(workexperienceList.get(i).getCompany());
                        md.setLocation(workexperienceList.get(i).getLocation());
                        md.setDescription(workexperienceList.get(i).getDescription());
                        modelList.add(md);
                    }

                }


                college_fromspn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        FontsOverride.overrideFonts(ProfileProfessionalActivity.this, view);
                        fromyear_str = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                college_tospn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        FontsOverride.overrideFonts(ProfileProfessionalActivity.this, view);
                        toyear_str = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        title_str = title_et.getText().toString();
                        company_str = company_et.getText().toString();
                        location_str = location_et.getText().toString();
                        description_str = description_et.getText().toString();


                        if (!title_et.getText().toString().trim().equalsIgnoreCase("")) {
                            if (!company_et.getText().toString().trim().equalsIgnoreCase("")) {
                                if (!location_et.getText().toString().trim().equalsIgnoreCase("")) {
                                    if (fromyear_str != null && !fromyear_str.equals("Year")) {
                                        if (toyear_str != null && !toyear_str.equals("Year")) {
                                            if (!description_et.getText().toString().trim().equalsIgnoreCase("")) {
                                                ProfileModel md = new ProfileModel(title_str, company_str, location_str, fromyear_str, toyear_str, description_str);
                                                md.setTitle(title_str);
                                                md.setCompany(company_str);
                                                md.setLocation(location_str);
                                                md.setFrom(fromyear_str);
                                                md.setTo(toyear_str);
                                                md.setDescription(description_str);
                                                modelList.add(md);
                                                skillList.add(skills_str);

                                                hideKeyboard(v2);
                                                Log.e("tag", "jsArray--------------->>>" + modelList);
                                                new updateWorkExperience().execute();

                                            } else {
                                                Toast.makeText(getApplicationContext(), "Enter Description",
                                                        Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Select To Year",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Select From Year",
                                                Toast.LENGTH_SHORT).show();
                                    }


                                } else {
                                    Toast.makeText(getApplicationContext(), "Enter Location",
                                            Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Enter Company",
                                        Toast.LENGTH_SHORT).show();
                            }


                            if (year_str != null && !year_str.equals("Year")) {


                            } else {
                                Toast.makeText(getApplicationContext(), "Select From Year",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            if (modelList.size() > 0) {
                                hideKeyboard(v2);
                                Log.e("tag", "jsArray--------------->>>" + modelList);
                                new updateCerification().execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Enter Title",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                });

                addmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        title_str = title_et.getText().toString();
                        company_str = company_et.getText().toString();
                        location_str = location_et.getText().toString();
                        description_str = description_et.getText().toString();

                        if (skills_str.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Enter Professional Skills",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            ProfileModel md = new ProfileModel(title_str, company_str, location_str, fromyear_str, toyear_str, description_str);
                            md.setTitle(title_str);
                            md.setCompany(company_str);
                            md.setLocation(location_str);
                            md.setFrom(fromyear_str);
                            md.setTo(toyear_str);
                            md.setDescription(description_str);
                            modelList.add(md);
                            hideKeyboard(v2);
                            title_et.setText("");
                            company_et.setText("");
                            location_et.setText("");
                            description_et.setText("");
                            updateSpinner(college_fromspn, yearList);
                            updateSpinner(college_tospn, yearList);


                        }
                    }
                });

            }
        });

        //-----------------------------ADD SKILLS---------------------------------------//


        addprofessionalskills_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d = new Dialog(ProfileProfessionalActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                d.setContentView(R.layout.dialog_professionalskills);
                d.setCanceledOnTouchOutside(false);
                ImageView close = d.findViewById(R.id.closeiv);
                ListView lv = d.findViewById(R.id.listview);
                TextView save = d.findViewById(R.id.save_tv);
                TextView addmore = d.findViewById(R.id.addmore_tv);

                skills_et = d.findViewById(R.id.skills_et);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
                modelList = new ArrayList<ProfileModel>();
                adapter = new ProfileCustomAdapter(getApplicationContext(), modelList, skillList);
                lv.setAdapter(adapter);
                if (professionalSkills.size() > 0) {

                    for (int i = 0; i < professionalSkills.size(); i++) {
                        ProfileModel md = new ProfileModel(professionalSkills, 1);
                        md.setSkills(professionalSkills.get(i));
                        modelList.add(md);
                        skillList.add(professionalSkills.get(i));
                    }

                }
                d.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) d).getWindow();
                        view1.setBackgroundDrawableResource(R.drawable.border_graybg);
                    }
                });
                final View v2 = d.getWindow().getDecorView().getRootView();
                FontsOverride.overrideFonts(ProfileProfessionalActivity.this, v2);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (!skills_et.getText().toString().trim().equalsIgnoreCase("")) {
                            skills_str = skills_et.getText().toString();
                            ProfileModel md = new ProfileModel(skills_str);
                            modelList.add(md);
                            skillList.add(skills_str);
                            adapter.notifyDataSetChanged();
                            skills_et.setText("");
                            hideKeyboard(v2);
                            Log.e("tag", "jsArray111--------------->>>" + modelList);
                            new updateProfession().execute();
                        } else {
                            if (modelList.size() > 0) {
                                hideKeyboard(v2);
                                Log.e("tag", "jsArray--------------->>>" + modelList);
                                new updateProfession().execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Enter Professional Skills",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                });

                addmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        skills_str = skills_et.getText().toString();

                        if (skills_str.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Enter Professional Skills",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            ProfileModel md = new ProfileModel(skills_str);
                            modelList.add(md);
                            skillList.add(skills_str);
                            adapter.notifyDataSetChanged();
                            skills_et.setText("");
                            hideKeyboard(v2);

                        }
                    }
                });


                d.show();
            }
        });





        skills_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d = new Dialog(ProfileProfessionalActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                d.setContentView(R.layout.dialog_professionalskills);
                d.setCanceledOnTouchOutside(false);
                ImageView close = d.findViewById(R.id.closeiv);
                ListView lv = d.findViewById(R.id.listview);
                TextView save = d.findViewById(R.id.save_tv);
                TextView addmore = d.findViewById(R.id.addmore_tv);

                skills_et = d.findViewById(R.id.skills_et);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
                modelList = new ArrayList<ProfileModel>();
                adapter = new ProfileCustomAdapter(getApplicationContext(), modelList, skillList);
                lv.setAdapter(adapter);
                if (professionalSkills.size() > 0) {

                    for (int i = 0; i < professionalSkills.size(); i++) {
                        ProfileModel md = new ProfileModel(professionalSkills, 1);
                        md.setSkills(professionalSkills.get(i));
                        modelList.add(md);
                        skillList.add(professionalSkills.get(i));
                    }

                }
                d.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) d).getWindow();
                        view1.setBackgroundDrawableResource(R.drawable.border_graybg);
                    }
                });
                final View v2 = d.getWindow().getDecorView().getRootView();
                FontsOverride.overrideFonts(ProfileProfessionalActivity.this, v2);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (!skills_et.getText().toString().trim().equalsIgnoreCase("")) {
                            skills_str = skills_et.getText().toString();
                            ProfileModel md = new ProfileModel(skills_str);
                            modelList.add(md);
                            skillList.add(skills_str);
                            adapter.notifyDataSetChanged();
                            skills_et.setText("");
                            hideKeyboard(v2);
                            Log.e("tag", "jsArray111--------------->>>" + modelList);
                            new updateProfession().execute();
                        } else {
                            if (modelList.size() > 0) {
                                hideKeyboard(v2);
                                Log.e("tag", "jsArray--------------->>>" + modelList);
                                new updateProfession().execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Enter Professional Skills",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                });

                addmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        skills_str = skills_et.getText().toString();

                        if (skills_str.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Enter Professional Skills",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            ProfileModel md = new ProfileModel(skills_str);
                            modelList.add(md);
                            skillList.add(skills_str);
                            adapter.notifyDataSetChanged();
                            skills_et.setText("");
                            hideKeyboard(v2);

                        }
                    }
                });


                d.show();
            }
        });
        //-----------------------------ADD CERTIFICATIONS---------------------------------------//

        addCertifications_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d = new Dialog(ProfileProfessionalActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                d.setContentView(R.layout.dialog_professionalcertification);
                d.setCanceledOnTouchOutside(false);
                ImageView close = d.findViewById(R.id.closeiv);
                ListView lv = d.findViewById(R.id.listview);
                TextView save = d.findViewById(R.id.save_tv);
                TextView addmore = d.findViewById(R.id.addmore_tv);
                year_spn = d.findViewById(vineture.wowhubb.R.id.year_spn);
                year_spn.setAdapter(arrayAdapter);


                year_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        FontsOverride.overrideFonts(ProfileProfessionalActivity.this, view);
                        year_str = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                certificate_et = d.findViewById(R.id.certificate_et);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
                modelList = new ArrayList<ProfileModel>();
                profileCertificationsAdapter = new ProfileCertificationsAdapter(getApplicationContext(), modelList, skillList);
                lv.setAdapter(profileCertificationsAdapter);

                if (certificationList.size() > 0) {

                    for (int i = 0; i < certificationList.size(); i++) {
                        ProfileModel md = new ProfileModel(certificationList);
                        md.setCertication(certificationList.get(i).getCertification());
                        md.setCertificationYear(certificationList.get(i).getYear());
                        modelList.add(md);
                    }

                }

                d.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) d).getWindow();
                        view1.setBackgroundDrawableResource(R.drawable.border_graybg);
                    }
                });
                final View v2 = d.getWindow().getDecorView().getRootView();
                FontsOverride.overrideFonts(ProfileProfessionalActivity.this, v2);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        cerifcation_str = certificate_et.getText().toString();

                        if (!certificate_et.getText().toString().trim().equalsIgnoreCase("")) {
                            if (year_str != null && !year_str.equals("Year")) {
                                ProfileModel md = new ProfileModel(cerifcation_str, year_str);
                                md.setCertication(cerifcation_str);
                                md.setCertificationYear(year_str);
                                modelList.add(md);
                                yearList.clear();
                                yearList.addAll(Arrays.asList(YEAR));

                                certificate_et.setText("");
                                updateSpinner(year_spn, yearList);
                                hideKeyboard(v2);
                                Log.e("tag", "jsArray--------------->>>" + modelList);
                                new updateCerification().execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Select From Year",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            if (modelList.size() > 0) {
                                hideKeyboard(v2);
                                Log.e("tag", "jsArray--------------->>>" + modelList);
                                new updateCerification().execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Enter Certification",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

                addmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cerifcation_str = certificate_et.getText().toString();
                        if (cerifcation_str.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Enter Certification",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            ProfileModel md = new ProfileModel(cerifcation_str);
                            md.setCertication(cerifcation_str);
                            md.setCertificationYear(year_str);
                            modelList.add(md);
                            yearList.clear();
                            yearList.addAll(Arrays.asList(YEAR));

                            certificate_et.setText("");
                            updateSpinner(year_spn, yearList);


                            hideKeyboard(v2);

                        }
                    }
                });


                d.show();
            }
        });
        cert_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d = new Dialog(ProfileProfessionalActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                d.setContentView(R.layout.dialog_professionalcertification);
                d.setCanceledOnTouchOutside(false);
                ImageView close = d.findViewById(R.id.closeiv);
                ListView lv = d.findViewById(R.id.listview);
                TextView save = d.findViewById(R.id.save_tv);
                TextView addmore = d.findViewById(R.id.addmore_tv);
                year_spn = d.findViewById(vineture.wowhubb.R.id.year_spn);
                year_spn.setAdapter(arrayAdapter);


                year_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        FontsOverride.overrideFonts(ProfileProfessionalActivity.this, view);
                        year_str = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                certificate_et = d.findViewById(R.id.certificate_et);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
                modelList = new ArrayList<ProfileModel>();
                profileCertificationsAdapter = new ProfileCertificationsAdapter(getApplicationContext(), modelList, skillList);
                lv.setAdapter(profileCertificationsAdapter);

                if (certificationList.size() > 0) {

                    for (int i = 0; i < certificationList.size(); i++) {
                        ProfileModel md = new ProfileModel(certificationList);
                        md.setCertication(certificationList.get(i).getCertification());
                        md.setCertificationYear(certificationList.get(i).getYear());
                        modelList.add(md);
                    }

                }

                d.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) d).getWindow();
                        view1.setBackgroundDrawableResource(R.drawable.border_graybg);
                    }
                });
                final View v2 = d.getWindow().getDecorView().getRootView();
                FontsOverride.overrideFonts(ProfileProfessionalActivity.this, v2);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        cerifcation_str = certificate_et.getText().toString();


                        if (!certificate_et.getText().toString().trim().equalsIgnoreCase("")) {
                            if (year_str != null && !year_str.equals("Year")) {
                                ProfileModel md = new ProfileModel(cerifcation_str, year_str);
                                md.setCertication(cerifcation_str);
                                md.setCertificationYear(year_str);
                                modelList.add(md);
                                yearList.clear();
                                yearList.addAll(Arrays.asList(YEAR));

                                certificate_et.setText("");
                                updateSpinner(year_spn, yearList);
                                hideKeyboard(v2);
                                Log.e("tag", "jsArray--------------->>>" + modelList);
                                new updateCerification().execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Select From Year",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            if (modelList.size() > 0) {
                                hideKeyboard(v2);
                                Log.e("tag", "jsArray--------------->>>" + modelList);
                                new updateCerification().execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Enter Certification",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

                addmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cerifcation_str = certificate_et.getText().toString();
                        if (cerifcation_str.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Enter Certification",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            ProfileModel md = new ProfileModel(cerifcation_str);
                            md.setCertication(cerifcation_str);
                            md.setCertificationYear(year_str);
                            modelList.add(md);
                            yearList.clear();
                            yearList.addAll(Arrays.asList(YEAR));

                            certificate_et.setText("");
                            updateSpinner(year_spn, yearList);


                            hideKeyboard(v2);

                        }
                    }
                });


                d.show();
            }
        });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(ProfileProfessionalActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(ProfileProfessionalActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED)) {
            Log.e("tag", "Permission is granted");
            return true;
        } else {
            Log.e("tag", "Permission is revoked");
            ActivityCompat.requestPermissions(ProfileProfessionalActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }
    }


    public void hideKeyboard(View view) {

        //View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void connectAndGetApiData() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://104.197.80.225:3010/wow/user/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        ApiInterface movieApiService = retrofit.create(ApiInterface.class);

        Call<Profile> call = movieApiService.getProfile("application/x-www-form-urlencoded", token);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response)
            {
                Log.e("tag", "888888888 " + response.body());
                profileList = response.body().getMessage();
                collegeList = profileList.getCollege();
                certificationList = profileList.getCertification();
                workexperienceList = profileList.getWorkexperience();
                professionalSkills = profileList.getProfessionalskills();

                Log.e("tag", "profile " + collegeList.size());
                if (profileList.getWowtagid() != null)
                {
                    wowtagtv.setText(profileList.getWowtagid());
                }

                if (profileList.getCollege() != null) {
                    if (profileList.getCollege().size() > 0) {
                        education_lv.setVisibility(View.VISIBLE);
                        addeducation_lv.setVisibility(View.GONE);
                        ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        for (int i = 0; i < collegeList.size(); i++) {
                            college_edit.setVisibility(View.VISIBLE);
                            TextView tv = new TextView(getApplicationContext());
                            tv.setLayoutParams(lparams);
                            tv.setTextColor(Color.parseColor("#3c3c3c"));
                            TextView tv1 = new TextView(getApplicationContext());
                            tv1.setLayoutParams(lparams);
                            tv1.setTextColor(Color.parseColor("#3c3c3c"));
                            tv.setText(collegeList.get(i).getCollege());
                            tv1.setText(collegeList.get(i).getFrom() + " - " + collegeList.get(i).getTo());
                            View view = new TextView(getApplicationContext());
                            view.setLayoutParams(lparams);
                            education_lv.addView(tv);
                            education_lv.addView(tv1);
                            education_lv.addView(view);
                        }
                    }

                }


                if (profileList.getCertification() != null) {
                    if (profileList.getCertification().size() > 0) {
                        Certifications_lv.setVisibility(View.VISIBLE);
                        addCertifications_lv.setVisibility(View.GONE);
                        ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        for (int i = 0; i < certificationList.size(); i++) {
                            cert_edit.setVisibility(View.VISIBLE);
                            TextView tv = new TextView(getApplicationContext());
                            tv.setLayoutParams(lparams);
                            tv.setTextColor(Color.parseColor("#3c3c3c"));
                            View view = new TextView(getApplicationContext());
                            view.setLayoutParams(lparams);


                            tv.setText(certificationList.get(i).getCertification() + " - " + certificationList.get(i).getYear());
                            Certifications_lv.addView(tv);
                            Certifications_lv.addView(view);

                        }
                    }

                }

                if (profileList.getWorkexperience() != null) {
                    if (profileList.getWorkexperience().size() > 0) {
                        workexp_lv.setVisibility(View.VISIBLE);
                        addworkexp_lv.setVisibility(View.GONE);
                        ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        for (int i = 0; i < workexperienceList.size(); i++) {
                            work_edit.setVisibility(View.VISIBLE);
                            TextView tv = new TextView(getApplicationContext());
                            tv.setLayoutParams(lparams);
                            tv.setTextColor(Color.parseColor("#3c3c3c"));
                            TextView tv1 = new TextView(getApplicationContext());
                            tv1.setLayoutParams(lparams);
                            tv1.setTextColor(Color.parseColor("#3c3c3c"));
                            TextView tv2 = new TextView(getApplicationContext());
                            tv2.setLayoutParams(lparams);
                            tv2.setTextColor(Color.parseColor("#3c3c3c"));
                            TextView tv3 = new TextView(getApplicationContext());
                            tv3.setLayoutParams(lparams);
                            tv3.setTextColor(Color.parseColor("#3c3c3c"));
                            View view = new TextView(getApplicationContext());
                            view.setLayoutParams(lparams);

                            tv.setText(workexperienceList.get(i).getTitle());
                            tv2.setText(workexperienceList.get(i).getCompany() + " , " + workexperienceList.get(i).getLocation());
                            tv1.setText(workexperienceList.get(i).getFromyear() + " - " + workexperienceList.get(i).getToyear());
                            tv3.setText(workexperienceList.get(i).getDescription().trim());

                            workexp_lv.addView(tv);
                            workexp_lv.addView(tv2);
                            workexp_lv.addView(tv1);
                            workexp_lv.addView(tv3);
                            workexp_lv.addView(view);

                        }

                    }
                }
                if (profileList.getProfessionalskills() != null) {
                    if (profileList.getProfessionalskills().size() > 0) {
                        professionalskills_lv.setVisibility(View.VISIBLE);
                        addprofessionalskills_lv.setVisibility(View.GONE);
                        ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        for (int i = 0; i < professionalSkills.size(); i++) {
                            skills_edit.setVisibility(View.VISIBLE);
                            TextView tv = new TextView(getApplicationContext());
                            tv.setLayoutParams(lparams);
                            tv.setTextColor(Color.parseColor("#3c3c3c"));
                            View view = new TextView(getApplicationContext());
                            view.setLayoutParams(lparams);
                            tv.setText(professionalSkills.get(i));
                            professionalskills_lv.addView(tv);
                            professionalskills_lv.addView(view);

                        }

                    }
                }

            }

            @Override
            public void onFailure(Call<Profile> call, Throwable throwable) {
                Log.e("tag", throwable.toString());
            }
        });
    }

    private void updateSpinner(final Spinner materialBetterSpinner, final ArrayList<String> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                arrayAdapter = new CustomListAdapter(ProfileProfessionalActivity.this, android.R.layout.simple_dropdown_item_1line, list) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {
                            return false;
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
                        tv.setTextSize(15);
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
                materialBetterSpinner.setAdapter(arrayAdapter);
            }
        });
    }

    public class updateProfession extends AsyncTask<String, Void, String> {

        public updateProfession() {
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
                Log.e("tag", "modelList--------------->>>" + modelList);
                skillList.clear();
                for (int i = 0; i < modelList.size(); i++) {

                    skillList.add(modelList.get(i).getSkills());
                }
                Log.e("tag", "modelList111--------------->>>" + modelList);
                JSONArray jsArray = new JSONArray(skillList);
                jsonObject.put("professionalskills", jsArray);

                Log.e("tag", "jsArray--------------->>>" + jsArray);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/user/updateprofessionalskills", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);
            loader_dialog.dismiss();
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        d.dismiss();
                        startActivity(new Intent(ProfileProfessionalActivity.this, ProfileProfessionalActivity.class));
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

            }

        }

    }

    public class updateCerification extends AsyncTask<String, Void, String> {

        public updateCerification() {
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

                JSONArray jsArray = new JSONArray();
                Log.e("tag", "modelList--------------->>>" + modelList.size());

                for (int i = 0; i < modelList.size(); i++) {
                    JSONObject jo = new JSONObject();
                    jo.put("certification", modelList.get(i).getCertication());
                    jo.put("year", modelList.get(i).getCertificationYear());
                    jsArray.put(jo);
                }


                jsonObject.put("certification", jsArray);
                Log.e("tag", "jsArray--------------->>>" + jsonObject);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/user/updateprofessionalprofile", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);
            loader_dialog.dismiss();
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        d.dismiss();
                        startActivity(new Intent(ProfileProfessionalActivity.this, ProfileProfessionalActivity.class));
                        finish();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

            }

        }

    }

    public class updateWorkExperience extends AsyncTask<String, Void, String> {

        public updateWorkExperience() {
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

                JSONArray jsArray = new JSONArray();
                Log.e("tag", "modelList--------------->>>" + modelList.size());
                for (int i = 0; i < modelList.size(); i++) {
                    JSONObject jo = new JSONObject();
                    jo.put("title", modelList.get(i).getTitle());
                    jo.put("company", modelList.get(i).getCompany());
                    jo.put("location", modelList.get(i).getLocation());
                    jo.put("fromyear", modelList.get(i).getFrom());
                    jo.put("toyear", modelList.get(i).getTo());
                    jo.put("description", modelList.get(i).getDescription());
                    jsArray.put(jo);
                }


                jsonObject.put("workexperience", jsArray);
                Log.e("tag", "jsArray--------------->>>" + jsonObject);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/user/updateworkexperience", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);
            loader_dialog.dismiss();
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        d.dismiss();
                        startActivity(new Intent(ProfileProfessionalActivity.this, ProfileProfessionalActivity.class));
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

            }

        }

    }

    public class updateColleges extends AsyncTask<String, Void, String> {

        public updateColleges() {
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

                JSONArray jsArray = new JSONArray();
                Log.e("tag", "modelList--------------->>>" + modelList.size());

                for (int i = 0; i < modelList.size(); i++) {
                    JSONObject jo = new JSONObject();
                    jo.put("college", modelList.get(i).getCollege());
                    jo.put("from", modelList.get(i).getFrom());
                    jo.put("to", modelList.get(i).getTo());
                    jsArray.put(jo);
                }


                jsonObject.put("college", jsArray);
                Log.e("tag", "jsArray--------------->>>" + jsonObject);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/user/updatecollegedetails", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);
            loader_dialog.dismiss();
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        d.dismiss();
                        startActivity(new Intent(ProfileProfessionalActivity.this, ProfileProfessionalActivity.class));
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

            }

        }

    }

}
