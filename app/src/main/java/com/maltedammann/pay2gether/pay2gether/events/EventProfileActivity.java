package com.maltedammann.pay2gether.pay2gether.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.control.DbUtils;
import com.maltedammann.pay2gether.pay2gether.model.Bill;
import com.maltedammann.pay2gether.pay2gether.model.Event;
import com.maltedammann.pay2gether.pay2gether.model.User;
import com.maltedammann.pay2gether.pay2gether.utils.AddBillDialogFragment;
import com.maltedammann.pay2gether.pay2gether.utils.UIHelper;
import com.maltedammann.pay2gether.pay2gether.utils.interfaces.ItemAddedHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class EventProfileActivity extends AppCompatActivity implements ItemAddedHandler {

    //helper
    private DbUtils db;
    public static ArrayList<User> users;
    private double currentAmount;

    // Object Holder
    private ArrayList<Bill> bills = new ArrayList<>();

    //Firebase instance variables
    private com.google.firebase.database.ValueEventListener amountListener;
    private com.google.firebase.database.ValueEventListener toolbarListener;
    private ChildEventListener mChildBillListener;

    //Constants
    private String eventId = RecyclerViewAdapterEvent.INTENT_EVENT_ID;
    private DatabaseReference dbBillsRef = FirebaseDatabase.getInstance().getReference().child(db.BILL_REF);
    private DatabaseReference dbEventsRef = FirebaseDatabase.getInstance().getReference().child(db.EVENT_REF);
    private static final String TAG = EventProfileActivity.class.getSimpleName();

    //UI
    private TextView tvAmount;
    private Toolbar toolbar;
    private RecyclerViewAdapterEventBills adapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_profile);

        setupToolbar();
        tvAmount = (TextView) findViewById(R.id.summaryAmount);

        //get eventId
        eventId = getIntent().getStringExtra(RecyclerViewAdapterEvent.INTENT_EVENT_ID);
        //rename Toolbar
        renameToolbar();

        //DB Helper connection
        db = new DbUtils(this);

        // get available users
        users = db.getAllUsers();

        //Fab init
        setupFab();

        // View + adapter init
        setupRecyclerView();

        //Firebase init
        setupFirebase();
    }

    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_bills);
        //mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        lm.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new RecyclerViewAdapterEventBills(EventProfileActivity.this, bills);
        mRecyclerView.setAdapter(adapter);

        registerForContextMenu(mRecyclerView);
    }

    private void renameToolbar() {
        //get Name of event from id
        if (toolbarListener == null) {
            toolbarListener = new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                    Event temp = snapshot.getValue(Event.class);
                    System.out.println("Listener EVENT " + temp.getTitle());
                    getSupportActionBar().setTitle(temp.getTitle());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            dbEventsRef.child(eventId).addListenerForSingleValueEvent(toolbarListener);
        }
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabEditEvent);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatDialogFragment addBillFragment = AddBillDialogFragment.newInstance(eventId);
                addBillFragment.show(getSupportFragmentManager(), "add_bill");
            }
        });
    }

    private void setupFirebase() {
        if (amountListener == null) {
            amountListener = new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                    currentAmount = 0;
                    Bill temp;
                    for (com.google.firebase.database.DataSnapshot bill : snapshot.getChildren()) {
                        temp = bill.getValue(Bill.class);
                        if (temp != null) {
                            currentAmount += temp.getAmount();
                            tvAmount.setText(new DecimalFormat("0.00").format(currentAmount));
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            dbBillsRef.orderByChild("eventId").equalTo(eventId).addValueEventListener(amountListener);
        }

        if (mChildBillListener == null) {
            mChildBillListener = new ChildEventListener() {
                @Override
                public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                    getAllBills(dataSnapshot, true);
//                Log.d(TAG, "onChildAdd:" + dataSnapshot.getValue(User.class).getName());
                }

                @Override
                public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                    getAllBills(dataSnapshot, false);
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getValue(Event.class).getTitle());
                }

                @Override
                public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildDeleted:" + dataSnapshot.getValue(User.class).getName());
                    //getAllUser(dataSnapshot, false);
                    adapter.remove();
                }

                @Override
                public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                    Log.d(TAG, "onChildMoved:" + dataSnapshot.getValue(User.class).getName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "onChildChanged:" + " CANCELED");
                }
            };

            dbBillsRef.orderByChild("eventId").equalTo(eventId).addChildEventListener(mChildBillListener);
        }

    }

    private void getAllBills(DataSnapshot dataSnapshot, boolean addBill) {
        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
            try {
                if (addBill) {
                    bills.add(dataSnapshot.getValue(Bill.class));
                }
                adapter = new RecyclerViewAdapterEventBills(EventProfileActivity.this, bills);
                mRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        }
    }

    private void attachDatabaseListener() {
        dbBillsRef.orderByChild("eventId").equalTo(eventId).addValueEventListener(amountListener);
        dbEventsRef.child(eventId).addListenerForSingleValueEvent(toolbarListener);
    }

    private void detachDatabaseListener() {
        //Listener for specific bills
        if (amountListener != null) {
            dbBillsRef.orderByChild("eventId").equalTo(eventId).removeEventListener(amountListener);
            amountListener = null;
            System.out.println("amount Listener detached");
        }//Listener for event name
        if (toolbarListener != null) {
            dbEventsRef.child(eventId).removeEventListener(toolbarListener);
            toolbarListener = null;
            System.out.println("toolbar Listener detached");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_editEvent) {
            Intent go2Edit = new Intent(EventProfileActivity.this, EditEventActivity.class);
            go2Edit.putExtra(EventsActivity.INTENT_EVENT_ID, eventId);
            startActivity(go2Edit);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        attachDatabaseListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        attachDatabaseListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        detachDatabaseListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        detachDatabaseListener();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //int index = info.position;
        Bill temp = adapter.getSelectedItem(item);
        switch (item.getItemId()) {
            case BillHolder.CONTEXT_EDIT_ENTRY:
                /*Intent go2Edit = new Intent(EventsActivity.this, EditEventActivity.class);
                go2Edit.putExtra(INTENT_EVENT_ID, temp.getId());
                startActivity(go2Edit);*/
                break;
            case BillHolder.CONTEXT_DELETE_ENTRY:
                db.deleteBill(temp.getId());
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemAdded(Object obj) {
        Bill newBill = (Bill) obj;
        db.addBill(newBill);
        UIHelper.snack((View) findViewById(R.id.clEventProfile), "New bill '" + newBill.getTitle() + ", " + newBill.getAmount() + "' added");
    }

}