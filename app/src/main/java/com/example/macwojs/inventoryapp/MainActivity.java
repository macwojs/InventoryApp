package com.example.macwojs.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macwojs.inventoryapp.data.InventorContract.InventorEntry;
import com.example.macwojs.inventoryapp.data.InventorDbHelper;

public class MainActivity extends AppCompatActivity {

    static private String LOG_TAG_INSERT = "Insert data";
    static private String LOG_TAG_QUERY = "query data";

    private InventorDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new InventorDbHelper(this);

        insertData();
        queryData();
    }

    private void queryData(){
        /**
         * Query the database.
         * Always close the cursor when you're done reading from it.
         * This releases all its resources and makes it invalid.
         */

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.

        String[] projection = {
                InventorEntry._ID,
                InventorEntry.COLUMN_INVENTOR_NAME,
                InventorEntry.COLUMN_INVENTOR_PRICE,
                InventorEntry.COLUMN_INVENTOR_QUANTITY,
                InventorEntry.COLUMN_INVENTOR_SUPPLIER_NAME,
                InventorEntry.COLUMN_INVENTOR_SUPPLIER_PHONE
        };

        Cursor cursor = db.query(
                InventorEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        try {
            Log.e(LOG_TAG_QUERY,
                    InventorEntry._ID +  " - " +
                    InventorEntry.COLUMN_INVENTOR_NAME + " - " +
                    InventorEntry.COLUMN_INVENTOR_PRICE + " - " +
                    InventorEntry.COLUMN_INVENTOR_QUANTITY + " - " +
                    InventorEntry.COLUMN_INVENTOR_SUPPLIER_NAME + " - " +
                    InventorEntry.COLUMN_INVENTOR_SUPPLIER_PHONE);

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(InventorEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(InventorEntry.COLUMN_INVENTOR_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventorEntry.COLUMN_INVENTOR_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventorEntry.COLUMN_INVENTOR_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(InventorEntry.COLUMN_INVENTOR_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InventorEntry.COLUMN_INVENTOR_SUPPLIER_PHONE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentRow = cursor.getPosition();
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                Double currentPrice = cursor.getDouble(priceColumnIndex);
                Integer currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierPhone = cursor.getString(supplierPhoneColumnIndex);

                Log.e(LOG_TAG_QUERY, "\nCurrent row:" + currentRow + ". Data from it: \n" +
                        currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplierName + " - " +
                        currentSupplierPhone);
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }


    }

    private void insertData(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventorEntry.COLUMN_INVENTOR_NAME, "Harry Potter");
        values.put(InventorEntry.COLUMN_INVENTOR_PRICE, 40.50);
        values.put(InventorEntry.COLUMN_INVENTOR_QUANTITY, 3);
        values.put(InventorEntry.COLUMN_INVENTOR_SUPPLIER_NAME, "Media Rodzina");
        values.put(InventorEntry.COLUMN_INVENTOR_SUPPLIER_PHONE, "609948883");


        long newRowId = db.insert(InventorEntry.TABLE_NAME, null, values);
        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Log.e(LOG_TAG_INSERT, "Error with saving product");
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Log.e(LOG_TAG_INSERT, "Product saved with row id:" + newRowId);
        }
    }
}
