package com.maltedammann.pay2gether.pay2gether.events;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.control.DbUtils;
import com.maltedammann.pay2gether.pay2gether.model.Event;

public class EditEventActivity extends AppCompatActivity {

    //DB
    private DbUtils dbUtils;

    //Constants
    private static final String TAG = EditEventActivity.class.getSimpleName();

    //UI
    private Toolbar toolbar;
    private EditText title;

    private String eventID;
    private Event event = null;

    //Firebase
    private ValueEventListener mListener;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        toolbar = (Toolbar) findViewById(R.id.toolbar_editEvent);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        System.out.println("IN EDIT EVENT ?");
        title = (EditText) findViewById(R.id.editTextTitle);
        dbUtils = new DbUtils(this);

        eventID = getIntent().getStringExtra(EventsActivity.INTENT_EVENT_ID);
        System.out.println("EVENTID: " + eventID);

        //Firebase init
        setupFirebase();

    }

    private void setupFirebase() {
        mRef = dbUtils.mFirebaseDbReference.child(dbUtils.EVENT_REF).child(eventID);
        if (mListener == null) {
            mListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    event = snapshot.getValue(Event.class);
                    if (event != null) {
                        if (event.getTitle() != null) {
                            title.setText(event.getTitle());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mRef.addValueEventListener(mListener);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mRef.addValueEventListener(mListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRef.addValueEventListener(mListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mListener != null) {
            mRef.removeEventListener(mListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mListener != null) {
            mRef.removeEventListener(mListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mRef.removeEventListener(mListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                event.setTitle(title.getText().toString());
                System.out.println("NEUER EVENT: " + event.toString());
                dbUtils.editEvent(event);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
