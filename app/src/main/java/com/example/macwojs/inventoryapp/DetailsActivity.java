package com.example.macwojs.inventoryapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macwojs.inventoryapp.data.InventorContract;
import com.example.macwojs.inventoryapp.data.InventorDbHelper;
import com.example.macwojs.inventoryapp.data.InventorContract.InventorEntry;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView mNameTextView;
    private TextView mPriceTextView;
    private TextView mQuantityTextView;
    private TextView mSupplierNameTextView;
    private TextView mSupplierPhoneTextView;

    private static final Integer EXISTING_PET_LOADER = 0;

    private InventorDbHelper mDbHelper;

    private Uri currentInventUri = null;

    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        currentInventUri = intent.getData();

        getLoaderManager().initLoader(EXISTING_PET_LOADER, null, this);

        // Find all relevant views that we will need to read user input from
        mNameTextView = (TextView) findViewById(R.id.nameData);
        mPriceTextView = (TextView) findViewById(R.id.priceData);
        mQuantityTextView = (TextView) findViewById(R.id.quantityData);
        mSupplierNameTextView = (TextView) findViewById(R.id.supplierData);
        mSupplierPhoneTextView = (TextView) findViewById(R.id.supplierPhone);

        Button mButtonUpdate = (Button) findViewById(R.id.editButton);
        Button mButtonDelete = (Button) findViewById(R.id.delButton);

        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, EditorActivity.class);
                intent.setData(currentInventUri);
                startActivity(intent);
            }
        });

        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mDbHelper = new InventorDbHelper(this);

        Button decreaseButton = (Button) findViewById(R.id.decrease_button);
        Button increaseButton = (Button) findViewById(R.id.increase_button);
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer quantity = Integer.parseInt(mQuantityTextView.getText().toString());
                if (quantity > 0) {
                    quantity = quantity - 1;
                }
                // Content Values to update quantity
                ContentValues values = new ContentValues();
                values.put(InventorEntry.COLUMN_INVENTOR_QUANTITY, quantity);

                // update the database
                getContentResolver().update(currentInventUri, values, null, null);
            }
        });

        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer quantity = Integer.parseInt(mQuantityTextView.getText().toString());

                quantity = quantity + 1;

                // Content Values to update quantity
                ContentValues values = new ContentValues();
                values.put(InventorEntry.COLUMN_INVENTOR_QUANTITY, quantity);

                // update the database
                getContentResolver().update(currentInventUri, values, null, null);
            }
        });

        Button callButton = (Button) findViewById(R.id.call_button);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = mSupplierPhoneTextView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                // Send phone number to intent as data
                intent.setData(Uri.parse("tel:" + phone));
                // Start the dialer app activity with number
                startActivity(intent);
            }
        });
    }


    //Methods for Loader
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InventorContract.InventorEntry._ID,
                InventorContract.InventorEntry.COLUMN_INVENTOR_NAME,
                InventorContract.InventorEntry.COLUMN_INVENTOR_PRICE,
                InventorContract.InventorEntry.COLUMN_INVENTOR_QUANTITY,
                InventorEntry.COLUMN_INVENTOR_SUPPLIER_NAME,
                InventorEntry.COLUMN_INVENTOR_SUPPLIER_PHONE
        };

        return new CursorLoader(this,
                currentInventUri,
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

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deleteProduct() {
        // Only perform the delete if this is an existing pet.

        // Call the ContentResolver to delete the pet at the given content URI.
        // Pass in null for the selection and selection args because the mCurrentPetUri
        // content URI already identifies the pet that we want.
        int rowsDeleted = getContentResolver().delete(currentInventUri, null, null);

        // Show a toast message depending on whether or not the delete was successful.
        if (rowsDeleted == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, getString(R.string.datails_delete_product_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.datails_delete_product_succesfull),
                    Toast.LENGTH_SHORT).show();
        }


        // Close the activity
        finish();
    }
}
