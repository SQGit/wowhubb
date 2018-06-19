package Contacts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.R;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.BeneficiaryViewHolder> {
    static public List<String> selectedcontacts;
    View itemView;
    String playlink;
    private List<Contact> contactsList;
    private Context mContext;
    private ArrayList<Contact> filtercontactsList;

    public ContactsAdapter(List<Contact> list, ArrayList<Contact> filterlist, Context context) {
        this.contactsList = list;
        this.filtercontactsList = filterlist;
        this.mContext = context;
        filtercontactsList.addAll(filterlist);
    }

    @Override
    public ContactsAdapter.BeneficiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contact_item, parent, false);
        FontsOverride.overrideFonts(mContext, itemView);
        selectedcontacts = new ArrayList<>();
        return new ContactsAdapter.BeneficiaryViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ContactsAdapter.BeneficiaryViewHolder holder, final int position) {

        final int pos = position;
        final Contact contact = contactsList.get(position);
        holder.tvName.setText(contact.getName());
        holder.tvPhone.setText(contact.getPhoneno());
        //    Log.e("tag", "CONTACT ID-----" + contact.getWowtagid());
        if (!contact.getWowtagid().equals("null")) {
            holder.tvEmail.setText(contact.getWowtagid());
            holder.inviteiv.setVisibility(View.INVISIBLE);
        } else {
            holder.tvEmail.setText("");
            holder.inviteiv.setVisibility(View.VISIBLE);
        }
       /* holder.chkContact.setChecked(contactsList.get(position).isSelected());

        holder.chkContact.setTag(contactsList.get(position));
        holder.chkContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //    contactsList.get(holder.getAdapterPosition()).setSelected(isChecked);
                AddContactsInvite.doneItem.setVisible(true);
                if (isChecked) {
                    contactsList.get(holder.getAdapterPosition()).setSelected(isChecked);
                    selectedcontacts.add(contact.getPhoneno());
                    Log.e("tag", "sizeeeee-----" + selectedcontacts);
                }
                else {
                    // selectedcontacts.remove(contact.getPhoneno());
                    Log.e("tag", "sizeeeee-----" + selectedcontacts);
                }
            }
        });*/
       /* holder.chkContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Contact contact = (Contact) cb.getTag();
                contact.setSelected(cb.isChecked());
                contactsList.get(pos).setSelected(cb.isChecked());
                Log.e("tag", "222222222-----" + contact.getPhoneno());
                if ( holder.chkContact.isChecked()) {
                    Log.e("tag", "11111-----" + contact.getPhoneno());
                    AddContactsInvite.doneItem.setVisible(true);
                    selectedcontacts.add(contact.getPhoneno());

                } else {
                    // Log.e("tag","222222222-----"+cb.getTag());
                    //  AddContactsInvite.doneItem.setVisible(false);
                }

            }
        });*/
      /*  holder.chkContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final Contact contact = contactsList.get(position);
                //   int adapterPosition = getAdapterPosition();
                if (contact.getChecked()) {
                    selectedcontacts.add(contact.getPhoneno());
                   // holder.chkContact.setChecked(true);
                    contact.setChecked(true);
                }
                else {
                    holder.chkContact.setChecked(false);
                   contact.setChecked(false);
                }


            }
        });*/

        holder.inviteiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteWowhubb();
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

        holder.profilePic.setImageDrawable(drawable1);
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        contactsList.clear();
        if (charText.length() == 0) {
            contactsList.addAll(filtercontactsList);
        } else {
            for (Contact contact : filtercontactsList) {
                Log.e("tag", "11111-www----->>>>" + contact.getName());
                if (charText.length() != 0 && contact.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    Log.e("tag", "11111------>>>>" + contact.getName());
                    contactsList.add(contact);
                } else if (charText.length() != 0 && contact.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    contactsList.add(contact);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void inviteWowhubb() {
        // specify our test image location
        Uri url = Uri.parse("android.resource://"
                + mContext.getPackageName() + "/" + R.drawable.ic_action_done);

        // set up an intent to share the image
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);
        share_intent.setType("text/plain");
        //    share_intent.putExtra(Intent.EXTRA_STREAM,
        //   Uri.fromFile(new File(url.toString())));
        share_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       /* share_intent.putExtra(Intent.EXTRA_SUBJECT,
                "share an image");*/
        final String appPackageName = mContext.getPackageName(); // getPackageName() from Context or Activity object


        String link_val = "https://play.google.com/store/apps/details?id=" + appPackageName;
        String body = "Hi There!\n Please download the wowhubb app from <a href=\"" + link_val + "\">" + link_val+ "</a>\n\n\nThank You";
        share_intent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(body));

       /* try {
            Uri playlinkuri = Uri.parse("market://details?id=" + appPackageName);
            share_intent.putExtra(Intent.EXTRA_TEXT, playlinkuri);
            // mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            Uri playlinkuri = Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName);
            share_intent.putExtra(Intent.EXTRA_TEXT, playlinkuri);
            // mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
*/
        // start the intent
        try {
            mContext.startActivity(Intent.createChooser(share_intent,
                    "Wowhubb Invite"));
        } catch (android.content.ActivityNotFoundException ex) {
            (new AlertDialog.Builder(mContext)
                    .setMessage("Share failed")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                }
                            }).create()).show();
        }
    }

    public class BeneficiaryViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName, tvEmail, tvPhone, inviteiv;
        int position;
        ImageView profilePic;
        // CheckBox chkContact;

        public BeneficiaryViewHolder(View view) {
            super(view);

            tvName = (TextView) view.findViewById(R.id.tvName);
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);
            tvPhone = (TextView) view.findViewById(R.id.tvPhone);
            //chkContact = (CheckBox) view.findViewById(R.id.chkcb);
            profilePic = (ImageView) itemView.findViewById(R.id.image_view);
            inviteiv = itemView.findViewById(R.id.invite_iv);
        }


    }

}
