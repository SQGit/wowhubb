package vineture.wowhubb.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import vineture.wowhubb.R;
import vineture.wowhubb.data.AddressVenue;

import java.util.List;

/**
 * Created by Salman on 23-12-2017.
 */

public class ViewVenuesAdapter extends ArrayAdapter<AddressVenue> {


        Context mCtx;
        int listLayoutRes;
        List<AddressVenue> employeeList;
        SQLiteDatabase mDatabase;

        public ViewVenuesAdapter(Context mCtx, int listLayoutRes, List<AddressVenue> employeeList, SQLiteDatabase mDatabase) {
            super(mCtx, listLayoutRes, employeeList);

            this.mCtx = mCtx;
            this.listLayoutRes = listLayoutRes;
            this.employeeList = employeeList;
            this.mDatabase = mDatabase;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(listLayoutRes, null);

            //getting employee of the specified position
            AddressVenue addressVenue = employeeList.get(position);


            //getting views
            TextView textViewName = view.findViewById(R.id.addresstv);
            TextView textViewDept = view.findViewById(R.id.citytv);
            TextView textViewSalary = view.findViewById(R.id.statetv);
            TextView textViewJoiningDate = view.findViewById(R.id.zipcodetv);

            //adding data to views
            textViewName.setText(addressVenue.getAddress());
            textViewDept.setText(addressVenue.getCity());
            textViewSalary.setText(String.valueOf(addressVenue.getState()));
            textViewJoiningDate.setText(addressVenue.getZipcode());

            //we will use these buttons later for update and delete operation
            Button buttonDelete = view.findViewById(R.id.buttonDelete);
            Button buttonEdit = view.findViewById(R.id.buttonEdit);

            return view;
        }
    }
