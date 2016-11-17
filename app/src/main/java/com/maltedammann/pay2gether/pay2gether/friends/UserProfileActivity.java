package com.maltedammann.pay2gether.pay2gether.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.control.DbUtils;
import com.maltedammann.pay2gether.pay2gether.model.User;

public class UserProfileActivity extends AppCompatActivity {

    // UI
    private Toolbar toolbar;
    private FloatingActionButton fab;

    private String userID;

    //DB
    private DbUtils dbUtils;

    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar_userProfile);
        setSupportActionBar(toolbar);

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
        dbUtils.mFirebaseReference.child(dbUtils.USER_REF).child(userID).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
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
        });

    }

    private void setToolbar() {
        getSupportActionBar().setTitle(user.getName());
    }
}
