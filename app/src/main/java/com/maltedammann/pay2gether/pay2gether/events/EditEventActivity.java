package com.maltedammann.pay2gether.pay2gether.events;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.control.DbUtils;
import com.maltedammann.pay2gether.pay2gether.model.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditEventActivity extends AppCompatActivity {

    //DB
    private DbUtils dbUtils;

    //Constants
    private static final String TAG = EditEventActivity.class.getSimpleName();
    private Calendar timeCalendar = Calendar.getInstance();
    private Calendar dateCalendar = Calendar.getInstance();

    //UI
    private Toolbar toolbar;
    private EditText etTitle;
    private TextView tvDate;
    private TextView tvTime;
    private TimePickerDialog.OnTimeSetListener eventSetTimeListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timeCalendar.set(Calendar.MINUTE, minute);

            updateTimeTextView();
        }
    };
    private DatePickerDialog.OnDateSetListener eventSetDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateCalendar.set(Calendar.YEAR, year);
            dateCalendar.set(Calendar.MONTH, month);
            dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateDateTextView();
        }
    };

    //helpers
    private String eventID;
    private Event event = null;

    //Firebase
    private ValueEventListener mListener;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        //setup toolbar
        setupToolbar();

        dbUtils = new DbUtils(this);
        eventID = getIntent().getStringExtra(EventsActivity.INTENT_EVENT_ID);

        //Firebase init
        setupFirebase();

        //UI
        setupUi();
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_editEvent);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupUi() {
        //get tv
        etTitle = (EditText) findViewById(R.id.editTextTitle);
        tvTime = (TextView) findViewById(R.id.event_time);
        tvDate = (TextView) findViewById(R.id.event_date);

        //set listener to time picker
        tvTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new TimePickerDialog(
                        EditEventActivity.this,
                        eventSetTimeListener,
                        timeCalendar.get(Calendar.HOUR_OF_DAY),
                        timeCalendar.get(Calendar.MINUTE),
                        true
                ).show();
            }
        });

        //set listener to datepicker
        tvDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new DatePickerDialog(
                        EditEventActivity.this,
                        0,
                        eventSetDateListener,
                        dateCalendar.get(Calendar.YEAR),
                        dateCalendar.get(Calendar.MONTH),
                        dateCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
    }

    private void updateTimeTextView() {
        DateFormat dateFormatHm = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        tvTime.setText(dateFormatHm.format(timeCalendar.getTime()));
    }


    private void updateDateTextView() {
        DateFormat dateFormatHm = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        tvDate.setText(dateFormatHm.format(dateCalendar.getTime()));
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
                            System.out.println("set title");
                            etTitle.setText(event.getTitle());
                        }
                        if (event.getTime() != null) {
                            System.out.println("set time: " + event.getTime());
                            tvTime.setText(event.getTime());
                        }
                        if (event.getDate() != null) {
                            System.out.println("set date: " + event.getDate());
                            tvDate.setText(event.getDate());
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
                System.out.println("in save");
                event.setTitle(etTitle.getText().toString());
                event.setDate(tvDate.getText().toString());
                event.setTime(tvTime.getText().toString());
                dbUtils.editEvent(event);
                finish();
                System.out.println("nach save");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
