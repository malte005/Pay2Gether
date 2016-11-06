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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.friends.FriendsActivity;
import com.maltedammann.pay2gether.pay2gether.main.MainActivity;
import com.maltedammann.pay2gether.pay2gether.utils.BaseActivity;
import com.maltedammann.pay2gether.pay2gether.utils.LogoutUtils;

public class EventsActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseUser currentUser;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    NavigationView navigationView;

    private static final String TAG = EventsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_events);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(true);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + currentUser.getDisplayName());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    finish();
                }
            }
        };
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
            alert = (AlertDialog) LogoutUtils.showLogoutDeleteDialog(this, getString(R.string.signOutText), getString(R.string.signOut));
            alert.show();
        } else if (id == R.id.nav_delete_acc) {
            alert = (AlertDialog) LogoutUtils.showLogoutDeleteDialog(this, getString(R.string.signOutDeleteUser), getString(R.string.signOutDelete));
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
}
