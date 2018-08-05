package com.example.macwojs.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macwojs.inventoryapp.data.InventorContract;
import com.example.macwojs.inventoryapp.data.InventorContract.InventorEntry;
import com.example.macwojs.inventoryapp.data.InventorDbHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    static private String LOG_TAG_INSERT = "Insert data";
    static private String LOG_TAG_QUERY = "Query data";

    private static final Integer INVENTORY_LOADER = 0;

    InventorCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView inventoryListView = (ListView) findViewById(R.id.list_view_inventory);

        View emptyView = findViewById(R.id.empty_view);
        inventoryListView.setEmptyView(emptyView);

        mCursorAdapter = new InventorCursorAdapter(this, null);
        inventoryListView.setAdapter(mCursorAdapter);

        getLoaderManager().initLoader(INVENTORY_LOADER, null, this);

        inventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                Uri currentPetUri = ContentUris.withAppendedId(InventorEntry.CONTENT_URI ,id);
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });
    }

    //Options for create menu on the bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_add_product:
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertData(){
        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(InventorEntry.COLUMN_INVENTOR_NAME, "Harry Potter");
        values.put(InventorEntry.COLUMN_INVENTOR_PRICE, 40.50);
        values.put(InventorEntry.COLUMN_INVENTOR_QUANTITY, 3);
        values.put(InventorEntry.COLUMN_INVENTOR_SUPPLIER_NAME, "Media Rodzina");
        values.put(InventorEntry.COLUMN_INVENTOR_SUPPLIER_PHONE, "609948883");

        getContentResolver().insert(InventorEntry.CONTENT_URI, values);
    }

    //Methods for Loader
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InventorEntry._ID,
                InventorEntry.COLUMN_INVENTOR_NAME,
                InventorEntry.COLUMN_INVENTOR_PRICE,
                InventorEntry.COLUMN_INVENTOR_QUANTITY,
        };

        return new CursorLoader(this,
                InventorEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
