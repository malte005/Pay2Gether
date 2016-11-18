package com.maltedammann.pay2gether.pay2gether.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.control.DbUtils;
import com.maltedammann.pay2gether.pay2gether.friends.FriendsActivity;
import com.maltedammann.pay2gether.pay2gether.main.MainActivity;
import com.maltedammann.pay2gether.pay2gether.model.Event;
import com.maltedammann.pay2gether.pay2gether.model.User;
import com.maltedammann.pay2gether.pay2gether.utils.AddEventDialogFragment;
import com.maltedammann.pay2gether.pay2gether.utils.AuthUtils;
import com.maltedammann.pay2gether.pay2gether.utils.UIHelper;
import com.maltedammann.pay2gether.pay2gether.utils.extendables.BaseActivity;
import com.maltedammann.pay2gether.pay2gether.utils.interfaces.ItemAddedHandler;
import com.maltedammann.pay2gether.pay2gether.utils.interfaces.ReadDataInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.maltedammann.pay2gether.pay2gether.friends.UserHolder.CONTEXT_DELETE_ENTRY;
import static com.maltedammann.pay2gether.pay2gether.friends.UserHolder.CONTEXT_EDIT_ENTRY;

public class EventsActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ReadDataInterface, ItemAddedHandler {

    // Object Holder
    private ArrayList<Event> events = new ArrayList<>();

    //DB
    private DbUtils db;

    //Firebase instance variables
    private FirebaseUser currentUser;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ChildEventListener mChildEventListener;

    //UI
    private NavigationView navigationView;
    private RecyclerViewAdapterEvent adapter;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    //Constants
    private static final String TAG = FriendsActivity.class.getSimpleName();
    public static final String INTENT_EVENT_ID = "event_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        toolbar = (Toolbar) findViewById(R.id.toolbar_userProfile);
        setSupportActionBar(toolbar);

        //DB Helper connection
        db = new DbUtils(this);

        // Fab init
        setupFab();

        //DrawMenu init
        setupDrawer();

        // View + adapter init
        setupRecyclerView();

        //Firebase init
        setupFirebase();
    }

    private void setupDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(true);
    }

    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_events);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new RecyclerViewAdapterEvent(EventsActivity.this, events);
        mRecyclerView.setAdapter(adapter);

        registerForContextMenu(mRecyclerView);
    }

    private void setupFab() {
        fab = (FloatingActionButton) findViewById(R.id.fab_events);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sDate = "2016-11-03";
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                AppCompatDialogFragment addEventFragment = new AddEventDialogFragment();
                addEventFragment.show(getSupportFragmentManager(), "Add Event");

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupFirebase() {
        /**
         * Firebase - Auth
         */
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + currentUser.getDisplayName());
                    onSignInInitializer();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    onSignOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(
                                            AuthUI.GOOGLE_PROVIDER
                                            , AuthUI.EMAIL_PROVIDER
                                            //,AuthUI.FACEBOOK_PROVIDER
                                    )
                                    .build(),
                            MainActivity.RC_SIGN_IN);
                }
            }
        };
    }

    public void onSignOutCleanup() {
        detachDatabaseListener();
    }

    public void onSignInInitializer() {
        attachDatabaseReadListener();
    }

    public void attachDatabaseReadListener() {
        /**
         * Firebase - Read Event
         */
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                    getAllEvents(dataSnapshot, true);
//                Log.d(TAG, "onChildAdd:" + dataSnapshot.getValue(User.class).getName());
                }

                @Override
                public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                    getAllEvents(dataSnapshot, false);
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

            db.mFirebaseDbReference.child(db.EVENT_REF).addChildEventListener(mChildEventListener);
        }
    }

    public void detachDatabaseListener() {
        if (mChildEventListener != null) {
            db.mFirebaseDbReference.child(db.EVENT_REF).removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    private void getAllEvents(DataSnapshot dataSnapshot, boolean addEvent) {
        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
            try {
                if (addEvent) {
                    events.add(dataSnapshot.getValue(Event.class));
                }
                adapter = new RecyclerViewAdapterEvent(EventsActivity.this, events);
                mRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        navigationView.getMenu().getItem(1).setChecked(true);
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(1).setChecked(true);
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
        detachDatabaseListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
        detachDatabaseListener();
        //adapter.cleanup();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        AlertDialog alert;

        if (id == R.id.nav_news) {
            Intent openNewsFeed = new Intent(this, MainActivity.class);
            startActivity(openNewsFeed);
            finish();
        } else if (id == R.id.nav_events) {
        } else if (id == R.id.nav_friends) {
            Intent openFriends = new Intent(this, FriendsActivity.class);
            startActivity(openFriends);
            finish();
        } else if (id == R.id.nav_logout) {
            alert = (AlertDialog) AuthUtils.showLogoutDeleteDialog(this, getString(R.string.signOutText), getString(R.string.signOut));
            alert.show();
        } else if (id == R.id.nav_delete_acc) {
            alert = (AlertDialog) AuthUtils.showLogoutDeleteDialog(this, getString(R.string.signOutDeleteUser), getString(R.string.signOutDelete));
            alert.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemAdded(Object event) {
        Event newEvent = (Event) event;
        db.addEvent(newEvent);
        UIHelper.snack((View) findViewById(R.id.clEvents), newEvent.getTitle() + " added");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //int index = info.position;
        Event temp = adapter.getSelectedItem(item);
        switch (item.getItemId()) {
            case CONTEXT_EDIT_ENTRY:
                Intent go2Edit = new Intent(EventsActivity.this, EditEventActivity.class);
                go2Edit.putExtra(INTENT_EVENT_ID, temp.getId());
                startActivity(go2Edit);
                break;
            case CONTEXT_DELETE_ENTRY:
                db.deleteEvent(temp.getId());
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
