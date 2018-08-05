package com.example.macwojs.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.macwojs.inventoryapp.data.InventorContract.InventorEntry;

/**
 * {@link InventorCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class InventorCursorAdapter extends CursorAdapter{
    Context context2;

    /**
     * Constructs a new {@link InventorCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public InventorCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        context2=context;
        // Find fields to populate in inflated template
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow(InventorEntry.COLUMN_INVENTOR_NAME));
        Double price = cursor.getDouble(cursor.getColumnIndexOrThrow(InventorEntry.COLUMN_INVENTOR_PRICE));
        final Integer quantity = cursor.getInt(cursor.getColumnIndexOrThrow(InventorEntry.COLUMN_INVENTOR_QUANTITY));

        // Populate fields with extracted properties
        nameTextView.setText(name);
        priceTextView.setText(Double.toString(price));
        quantityTextView.setText(Integer.toString(quantity));

        // Quantity Button
        Button button = (Button) view.findViewById(R.id.sale_button);
        // Get the current items ID
        int currentId = cursor.getInt(cursor.getColumnIndex(InventorEntry._ID));
        // Make the content uri for the current Id
        final Uri contentUri = Uri.withAppendedPath(InventorEntry.CONTENT_URI, Integer.toString(currentId));

        // Change the quantity when you click the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer quantity2 = quantity;
                if (quantity2 > 0) {
                    quantity2 = quantity2 - 1;
                }
                // Content Values to update quantity
                ContentValues values = new ContentValues();
                values.put(InventorEntry.COLUMN_INVENTOR_QUANTITY, quantity2);

                // update the database
                context2.getContentResolver().update(contentUri, values, null, null);
            }
        });
    }
}
