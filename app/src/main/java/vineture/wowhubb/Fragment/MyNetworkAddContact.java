package vineture.wowhubb.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vineture.wowhubb.Fonts.FontsOverride;


public class MyNetworkAddContact extends Fragment {
    TextView addcontact_tv;

    public static MyNetworkAddContact newInstance() {
        MyNetworkAddContact fragment = new MyNetworkAddContact();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(vineture.wowhubb.R.layout.mynetwork_addcontact, container, false);
        FontsOverride.overrideFonts(getActivity(), view);
        addcontact_tv=view.findViewById(vineture.wowhubb.R.id.addcontact_tv);
        addcontact_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
                startActivity(intent);

            }
        });
        return view;
    }

}
