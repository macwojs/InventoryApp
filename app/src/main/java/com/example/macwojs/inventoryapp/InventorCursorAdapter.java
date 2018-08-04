package com.example.macwojs.inventoryapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.macwojs.inventoryapp.data.InventorContract.InventorEntry;

/**
 * {@link InventorCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class InventorCursorAdapter extends CursorAdapter {

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
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow(InventorEntry.COLUMN_INVENTOR_NAME));
        Double price = cursor.getDouble(cursor.getColumnIndexOrThrow(InventorEntry.COLUMN_INVENTOR_PRICE));
        Integer quantity = cursor.getInt(cursor.getColumnIndexOrThrow(InventorEntry.COLUMN_INVENTOR_QUANTITY));

        // Populate fields with extracted properties
        nameTextView.setText(name);
        priceTextView.setText(Double.toString(price));
        quantityTextView.setText(Integer.toString(quantity));
    }
}
