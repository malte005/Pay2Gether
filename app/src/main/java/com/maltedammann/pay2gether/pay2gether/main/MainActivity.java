package com.maltedammann.pay2gether.pay2gether.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.control.DbUtils;
import com.maltedammann.pay2gether.pay2gether.events.EventsActivity;
import com.maltedammann.pay2gether.pay2gether.user.UserActivity;
import com.maltedammann.pay2gether.pay2gether.model.User;
import com.maltedammann.pay2gether.pay2gether.utils.AuthUtils;
import com.maltedammann.pay2gether.pay2gether.utils.extendables.BaseActivity;
import com.maltedammann.pay2gether.pay2gether.utils.interfaces.ReadDataInterface;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, ReadDataInterface {

    //Firebase instance variables
    private FirebaseUser currentUser;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //DB connection
    private DbUtils dbUtils;

    //UI
    private Toolbar toolbar;
    private NavigationView navigationView;
    private AdView mAdView;

    //Instance variables
    private User user;
    private SharedPreferences prefs;

    //Constants
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String PREF_UID = "userKey";
    public static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar_userProfile);
        setSupportActionBar(toolbar);

        //DrawMenu init
        setupDrawer();

        //DB Connection
        dbUtils = new DbUtils(this);

        // AdMob
        setUpAdMob();

        //Initialize Firebase Auth
        setUpFirebase();

        //Write Main-User
        getMainUser();
    }

    private void setupDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    private void setUpFirebase() {
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

    private void setUpAdMob() {
        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id));
        mAdView = (AdView) findViewById(R.id.adView);

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);
    }

    public void onSignOutCleanup() {
        detachDatabaseListener();
    }

    public void onSignInInitializer() {
        attachDatabaseReadListener();
    }

    public void attachDatabaseReadListener() {

    }

    public void detachDatabaseListener() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        navigationView.getMenu().getItem(0).setChecked(true);

        if (mAdView != null) {
            mAdView.resume();
        }

        if (mAuthListener == null) {
            mFirebaseAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
        navigationView.getMenu().getItem(0).setChecked(true);

        if (mAdView != null) {
            mAdView.resume();
        }

        if (mAuthListener == null) {
            mFirebaseAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdView != null) {
            mAdView.pause();
        }

        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mAdView != null) {
            mAdView.pause();
        }

        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdView != null) {
            mAdView.destroy();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        AlertDialog alert;

        if (id == R.id.nav_news) {
        } else if (id == R.id.nav_events) {
            Intent openEvents = new Intent(this, EventsActivity.class);
            startActivity(openEvents);
        } else if (id == R.id.nav_friends) {
            Intent openFriends = new Intent(this, UserActivity.class);
            startActivity(openFriends);
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

    public String getMainUser() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String mainUserKey = prefs.getString(PREF_UID, null);
        if (mFirebaseAuth.getCurrentUser() != null) {
            if (mainUserKey == null) {
                User main = new User(mFirebaseAuth.getCurrentUser().getDisplayName(), mFirebaseAuth.getCurrentUser().getEmail());
                mainUserKey = dbUtils.addUser(main);
                main.setId(mainUserKey);
                System.out.println("MAINUSER war null :" + mainUserKey);

                SharedPreferences.Editor prefEditor = prefs.edit();
                prefEditor.putString(PREF_UID, mainUserKey);
                prefEditor.apply();
            }
        }
        return mainUserKey;
    }
}
