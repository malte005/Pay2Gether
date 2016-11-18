package com.maltedammann.pay2gether.pay2gether.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.control.DbUtils;
import com.maltedammann.pay2gether.pay2gether.model.User;

public class UserProfileActivity extends AppCompatActivity {

    // UI
    private Toolbar toolbar;
    private FloatingActionButton fab;

    private String userID;
    private User user = null;

    //DB
    private DbUtils dbUtils;

    //Firebase
    private ValueEventListener mListener;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar_userProfile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //UI
        TextView text = (TextView) findViewById(R.id.text);
        //get user ID
        userID = getIntent().getStringExtra(FriendsActivity.INTENT_USER_ID);
        //DB init
        dbUtils = new DbUtils(this);
        // User init
        getUser();
        // Fab init
        setupFab();

    }

    private void setupFab() {
        fab = (FloatingActionButton) findViewById(R.id.fab_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go2Edit = new Intent(UserProfileActivity.this, EditFriendActivity.class);
                go2Edit.putExtra(FriendsActivity.INTENT_USER_ID, userID);
                startActivity(go2Edit);
            }
        });
    }

    private void getUser() {
        mRef = dbUtils.mFirebaseDbReference.child(dbUtils.USER_REF).child(userID);
        if (mListener == null) {
            mListener = new ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                    user = snapshot.getValue(User.class);
                    System.out.println("USER: " + user.toString());
                    if (user.getName() != null) {
                        setToolbar(); //TODO geht noch nicht
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
        }
        mRef.addValueEventListener(mListener);
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

    private void setToolbar() {
        getSupportActionBar().setTitle(user.getName());
    }
}
