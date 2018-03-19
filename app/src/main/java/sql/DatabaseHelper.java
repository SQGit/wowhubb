package sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wowhubb.data.AddressVenue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by delaroy on 5/10/17.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "BeneficiaryManager.db";
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    //drop beneficiary table
    private String DROP_BENEFICIARY_TABLE = "DROP TABLE IF EXISTS " + BeneficiaryContract.BeneficiaryEntry.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + BeneficiaryContract.BeneficiaryEntry.TABLE_NAME + " (" +

                BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_ID+ " integer primary key," +
                BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_VENUENAME + " TEXT NOT NULL, " +
                BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_CITY + " TEXT NOT NULL, " +
                BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_STATE + " TEXT NOT NULL, " +
                BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_ADDRESS + " TEXT NOT NULL, " +
                BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_ZIPCODE + " TEXT NOT NULL " +

                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    //---opens the database---
    public DatabaseHelper open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }


    //---closes the database---
    public void close() {
        DBHelper.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db1, int oldVersion, int newVersion) {

        //Drop User Table if exist

        db1.execSQL(DROP_BENEFICIARY_TABLE);

        // Create tables again
        onCreate(db1);

    }


    //Method to create beneficiary records

    public void addBeneficiary(AddressVenue beneficiary) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_ADDRESS, beneficiary.getAddress());
        values.put(BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_VENUENAME, beneficiary.getVenuname());
        values.put(BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_STATE, beneficiary.getState());
        values.put(BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_ZIPCODE, beneficiary.getZipcode());
        values.put(BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_CITY, beneficiary.getCity());
        db.insert(BeneficiaryContract.BeneficiaryEntry.TABLE_NAME, null, values);
        db.close();
        Log.e("tag", "Inserted");
    }

    public SQLiteDatabase deleteContact(Integer id) {


        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "id=?";
        String whereArgs[] = {id.toString()};
        db.delete(BeneficiaryContract.BeneficiaryEntry.TABLE_NAME, whereClause, whereArgs);
        return db;
      /*  return db.delete(BeneficiaryContract.BeneficiaryEntry.TABLE_NAME,
                "id = ?",
                new String[]{id.toString()});*/
    }

    public boolean deleteProduct(String productname) {

        boolean result = false;

        String query = "Select * FROM " + BeneficiaryContract.BeneficiaryEntry.TABLE_NAME + " WHERE " + BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_VENUENAME + " =  \"" + productname + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        AddressVenue product = new AddressVenue();

        if (cursor.moveToFirst()) {
            product.setId(Integer.parseInt(cursor.getString(0)));
            db.delete(BeneficiaryContract.BeneficiaryEntry.TABLE_NAME, BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_ID + " = ?",
                    new String[] { String.valueOf(product.getId()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public List<AddressVenue> getAllBeneficiary() {
        // array of columns to fetch
        String[] columns = {


                BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_ADDRESS,
                BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_STATE,
                BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_VENUENAME,
                BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_ZIPCODE,
                BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_CITY

        };
        // sorting orders
        String sortOrder =
                BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_VENUENAME + " ASC";
        List<AddressVenue> beneficiaryList = new ArrayList<AddressVenue>();

        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(BeneficiaryContract.BeneficiaryEntry.TABLE_NAME, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AddressVenue beneficiary = new AddressVenue();
                //beneficiary.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));

                beneficiary.setCity(cursor.getString(cursor.getColumnIndex(BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_CITY)));
                beneficiary.setAddress(cursor.getString(cursor.getColumnIndex(BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_ADDRESS)));
                beneficiary.setVenuname(cursor.getString(cursor.getColumnIndex(BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_VENUENAME)));
                beneficiary.setState(cursor.getString(cursor.getColumnIndex(BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_STATE)));
                beneficiary.setZipcode(cursor.getString(cursor.getColumnIndex(BeneficiaryContract.BeneficiaryEntry.COLUMN_BENEFICIARY_ZIPCODE)));

                // Adding user record to list

                beneficiaryList.add(beneficiary);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return beneficiaryList;
    }

}
