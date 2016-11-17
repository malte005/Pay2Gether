package com.maltedammann.pay2gether.pay2gether.friends;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.maltedammann.pay2gether.pay2gether.R;

public class AddFriendActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = AddFriendActivity.class.getSimpleName();
    private Toolbar toolbar;
    Intent returnIntent;

    /**
     * An arbitrary ID to use for the Loader Manager
     */
    public static final int LOADER_MANAGER_ID = 0;

    /**
     * Content URI used to access contacts
     */
    public static final Uri CONTACTS_URI = ContactsContract.Contacts.CONTENT_URI;

    /**
     * List of columns to include when querying for contacts
     */
    public static final String[] CONTACTS_PROJECTION
            = new String[]{ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.PHOTO_URI};

    /**
     * Adapter used to display contacts
     */
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        toolbar = (Toolbar) findViewById(R.id.toolbar_addUser);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize the adapter
        adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, null,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                new int[]{android.R.id.text1}, 0);

       /* adapter = new SimpleCursorAdapter(this,
                R.layout.contacts_list_row, null,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Email.ADDRESS},
                new int[]{R.id.contact_name, R.id.contact_mail});*/

        final ListView lvContacts = (ListView) findViewById(R.id.lvContacts);
        lvContacts.setAdapter(adapter);

        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = adapter.getCursor().getString(1);
                //String mail = adapter.getCursor().getString(2);
                returnIntent = new Intent();
                returnIntent.putExtra(FriendsActivity.INTENT_DISPLAY_NAME, name);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        getLoaderManager().initLoader(LOADER_MANAGER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_MANAGER_ID:
                return new CursorLoader(this, CONTACTS_URI, CONTACTS_PROJECTION,
                        null, null, null);
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_MANAGER_ID:
                adapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_MANAGER_ID:
                adapter.swapCursor(null);
                break;
        }
    }
/*
    public void select(View v) {
        String name = adapter.getCursor().getString(1);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("display_name", name);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }*/
}
