package com.example.macwojs.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.macwojs.inventoryapp.data.InventorContract.InventorEntry;

public class InventorDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shelter.db";

    private static final Integer DATABASE_VERSION = 1;

    public InventorDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the Inventors table
        String SQL_CREATE_InventorS_TABLE = "CREATE TABLE " + InventorEntry.TABLE_NAME + " ("
                + InventorEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventorEntry.COLUMN_INVENTOR_NAME + " TEXT NOT NULL, "
                + InventorEntry.COLUMN_INVENTOR_PRICE + " REAL, "
                + InventorEntry.COLUMN_INVENTOR_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + InventorEntry.COLUMN_INVENTOR_SUPPLIER_NAME + " TEXT, "
                + InventorEntry.COLUMN_INVENTOR_SUPPLIER_PHONE + " TEXT);";
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_InventorS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
