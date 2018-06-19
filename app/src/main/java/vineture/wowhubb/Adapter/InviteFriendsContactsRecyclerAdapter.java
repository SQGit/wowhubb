package vineture.wowhubb.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import vineture.wowhubb.Activity.AddContactsInvite;
import vineture.wowhubb.Activity.InviteFriendsActivity;
import vineture.wowhubb.Fonts.FontsOverride;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Contacts.Contact;

/**
 * Created by delaroy on 5/10/17.
 */
public class InviteFriendsContactsRecyclerAdapter extends RecyclerView.Adapter<InviteFriendsContactsRecyclerAdapter.BeneficiaryViewHolder>
{
    static public List<String> selectedcontacts;
    View itemView;
    private List<Contact> contactsList;
    private Context mContext;
    private ArrayList<Contact> filtercontactsList;

    public InviteFriendsContactsRecyclerAdapter(List<Contact> list, ArrayList<Contact> filterlist, Context context)
    {
        this.contactsList = list;
        this.filtercontactsList = filterlist;
        this.mContext = context;
        filtercontactsList.addAll(filterlist);
    }

    @Override
    public BeneficiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        itemView = LayoutInflater.from(parent.getContext()).inflate(vineture.wowhubb.R.layout.adapter_contact_itemlinvite, parent, false);
        FontsOverride.overrideFonts(mContext, itemView);
        selectedcontacts = new ArrayList<>();
        return new BeneficiaryViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final BeneficiaryViewHolder holder, final int position) {

        final int pos = position;
        final Contact contact = contactsList.get(position);
        holder.tvName.setText(contact.getName());
        holder.tvPhone.setText(contact.getPhoneno());
        //    Log.e("tag", "CONTACT ID-----" + contact.getWowtagid());

      holder.chkContact.setChecked(contactsList.get(position).isSelected());

        holder.chkContact.setTag(contactsList.get(position));
        holder.chkContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // contactsList.get(holder.getAdapterPosition()).setSelected(isChecked);
               InviteFriendsActivity.doneItem.setVisible(true);
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
        });
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

    public class BeneficiaryViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName, tvEmail, tvPhone;
        int position;
        ImageView profilePic, inviteiv;
        CheckBox chkContact;

        public BeneficiaryViewHolder(View view) {
            super(view);

            tvName = (TextView) view.findViewById(vineture.wowhubb.R.id.tvName);
            tvEmail = (TextView) view.findViewById(vineture.wowhubb.R.id.tvEmail);
            tvPhone = (TextView) view.findViewById(vineture.wowhubb.R.id.tvPhone);
            chkContact = (CheckBox) view.findViewById(vineture.wowhubb.R.id.chkcb);
            profilePic = (ImageView) itemView.findViewById(vineture.wowhubb.R.id.image_view);
            inviteiv = itemView.findViewById(vineture.wowhubb.R.id.invite_iv);
        }


    }
}
