package Contacts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends ArrayAdapter<Contact> {
    private List<Contact> contacts;

    public ContactsAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
        this.contacts = contacts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item
        Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.adapter_contact_item, parent, false);
        }
        // Populate the data into the template view using the data object
        FontsOverride.overrideFonts(getContext(), convertView);
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        TextView tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        TextView tvinvite = (TextView) view.findViewById(R.id.invite_tv);
        tvName.setText(contact.name);

        tvinvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTest();
            }
        });

        String s = null;
        try {
            s = String.valueOf(contact.name.charAt(0)).toUpperCase();
        } catch (NullPointerException e) {

        } catch (StringIndexOutOfBoundsException e) {
            s = "*";
        }
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT

        int color1 = generator.getRandomColor();
        int color2 = generator.getColor("user@gmail.com");

        // declare the builder object once.
        TextDrawable drawable1 = TextDrawable.builder()
                .buildRound(s, color1);


        ImageView image = (ImageView) view.findViewById(R.id.image_view);
        image.setImageDrawable(drawable1);
        tvEmail.setText("");
        tvPhone.setText("");
        if (contact.emails.size() > 0 && contact.emails.get(0) != null) {
            tvEmail.setText(contact.emails.get(0).address);
        }
        if (contact.numbers.size() > 0 && contact.numbers.get(0) != null) {
            tvPhone.setText(contact.numbers.get(0).number);
        }
        return view;
    }

    private void startTest() {
        // specify our test image location
        Uri url = Uri.parse("android.resource://"
                + getContext().getPackageName() + "/" + R.drawable.ic_action_done);

        // set up an intent to share the image
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);
        share_intent.setType("text/plain");
        //    share_intent.putExtra(Intent.EXTRA_STREAM,
        //   Uri.fromFile(new File(url.toString())));
        share_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       /* share_intent.putExtra(Intent.EXTRA_SUBJECT,
                "share an image");*/
        share_intent.putExtra(Intent.EXTRA_TEXT, "wowhubb play store link ");

        // start the intent
        try {
            getContext().startActivity(Intent.createChooser(share_intent,
                    "Wowhubb Invite"));
        } catch (android.content.ActivityNotFoundException ex) {
            (new AlertDialog.Builder(getContext())
                    .setMessage("Share failed")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                }
                            }).create()).show();
        }
    }


    public void setFilter(List<Contact> countryModels) {
        contacts = new ArrayList<>();
        contacts.addAll(countryModels);
        notifyDataSetChanged();
    }


}
