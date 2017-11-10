package com.wowhubb.Fragment;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.suke.widget.SwitchButton;
import com.wowhubb.Activity.CreateEventActivity;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

public class EventContactFragment extends Fragment {

    TextInputLayout til_name, til_phone, til_email, til_msg;
    Typeface lato;
    EditText event_info, name, phone, email, message;
    private TextView callToAction;
String str_phone,str_email;
ImageView person;
    public static EventContactFragment newInstance(int page, boolean isLast) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        if (isLast)
            args.putBoolean("isLast", true);
        final EventContactFragment fragment = new EventContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_eventcontact, container, false);
        FontsOverride.overrideFonts(getActivity(), view);
        CreateEventActivity.skiptv.setVisibility(View.VISIBLE);
        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final com.suke.widget.SwitchButton switchButton = (com.suke.widget.SwitchButton)
                view.findViewById(R.id.switch_button);
        com.suke.widget.SwitchButton switchButton1 = (com.suke.widget.SwitchButton)
                view.findViewById(R.id.switch_button1);

        til_name = (TextInputLayout) view.findViewById(R.id.til_name);
        til_phone = (TextInputLayout) view.findViewById(R.id.til_phone);
        til_email = (TextInputLayout) view.findViewById(R.id.til_email);
        til_msg = (TextInputLayout) view.findViewById(R.id.til_msg);

        name = view.findViewById(R.id.name_et);
        phone=view.findViewById(R.id.phone_et);
        email=view.findViewById(R.id.email_et);
        message=view.findViewById(R.id.msg_et);
        person=view.findViewById(R.id.person);

        //til_evntinfo.setTypeface(lato);
        til_name.setTypeface(lato);
        til_phone.setTypeface(lato);
        til_email.setTypeface(lato);
        til_msg.setTypeface(lato);


        str_phone = sharedPreferences.getString("str_phone", "");
        str_email = sharedPreferences.getString("str_email", "");
        if (str_phone != null) {
            email.setText(str_email);
            phone.setText(str_phone);
            email.setEnabled(false);
            phone.setEnabled(false);
        }


        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email.setText("");
                phone.setText("");
                email.setText("");
                name.setText("");
                message.setText("");
                email.setHint("Email");
                phone.setHint("Phone");
                email.setEnabled(true);
                phone.setEnabled(true);
                switchButton.setChecked(false);
                switchButton.setChecked(false);
            }
        });


        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked)
            {
                //TODO do your job
                if(isChecked) {
                    phone.setEnabled(true);
                    email.setEnabled(true);
                }
                else
                {
                    phone.setEnabled(false);
                    email.setEnabled(false);
                }


            }
        });


        switchButton1.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked)
            {
                //TODO do your job
                if(isChecked) {
                    phone.setEnabled(true);
                    email.setEnabled(true);
                }
                else
                {
                    phone.setEnabled(false);
                    email.setEnabled(false);
                }

            }
        });



        return view;
    }


}