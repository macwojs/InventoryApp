package com.example.macwojs.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.macwojs.inventoryapp.data.InventorContract;
import com.example.macwojs.inventoryapp.data.InventorDbHelper;
import com.example.macwojs.inventoryapp.data.InventorContract.InventorEntry;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private TextView mNameTextView;
    private TextView mPriceTextView;
    private TextView mQuantityTextView;
    private TextView mSupplierNameTextView;
    private TextView mSupplierPhoneTextView;

    private static final Integer EXISTING_PET_LOADER = 0;

    private InventorDbHelper mDbHelper;

    private Uri currentPetUri = null;

    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        currentPetUri = intent.getData();

        getLoaderManager().initLoader(EXISTING_PET_LOADER, null, this);

        // Find all relevant views that we will need to read user input from
        mNameTextView = (TextView) findViewById(R.id.nameData);
        mPriceTextView = (TextView) findViewById(R.id.priceData);
        mQuantityTextView = (TextView) findViewById(R.id.quantityData);
        mSupplierNameTextView = (TextView) findViewById(R.id.supplierData);
        mSupplierPhoneTextView = (TextView) findViewById(R.id.supplierPhone);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mDbHelper = new InventorDbHelper(this);
    }


    //Methods for Loader
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InventorContract.InventorEntry._ID,
                InventorContract.InventorEntry.COLUMN_INVENTOR_NAME,
                InventorContract.InventorEntry.COLUMN_INVENTOR_PRICE,
                InventorContract.InventorEntry.COLUMN_INVENTOR_QUANTITY,
        };

        return new CursorLoader(this,
                InventorContract.InventorEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(InventorEntry.COLUMN_INVENTOR_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventorEntry.COLUMN_INVENTOR_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventorEntry.COLUMN_INVENTOR_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(InventorEntry.COLUMN_INVENTOR_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InventorEntry.COLUMN_INVENTOR_SUPPLIER_PHONE);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            Double price = cursor.getDouble(priceColumnIndex);
            Integer quantity = cursor.getInt(quantityColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            String supplierPhone = cursor.getString(supplierPhoneColumnIndex);

            // Update the views on the screen with the values from the database
            mNameTextView.setText(name);
            mPriceTextView.setText(Double.toString(price));
            mQuantityTextView.setText(Integer.toString(quantity));
            mSupplierNameTextView.setText(supplierName);
            mSupplierPhoneTextView.setText(supplierPhone);
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
