package com.maltedammann.pay2gether.pay2gether.friends;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.model.User;
import com.maltedammann.pay2gether.pay2gether.utils.FirebaseRefFactory;

public class UserProfileActivity extends AppCompatActivity {

    // UI
    Toolbar toolbar;
    FloatingActionButton fab;

    // Firebase
    Firebase userRef;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Fab init
        setupFab();

        TextView text = (TextView) findViewById(R.id.text);

        // User init
        getUser();


        // UI
        //this.setTitle(user.getName().toString());
        //System.out.println("NAME: " + user.getName());
        //text.setText(user.getName());

    }

    private void setupFab() {
        fab = (FloatingActionButton) findViewById(R.id.fab_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    public void getUser() {
        final String id = getIntent().getStringExtra(RecyclerViewAdapter.INTENT_USER_ID);
        System.out.println("DRIN ID: " + id);
        userRef = FirebaseRefFactory.getUsersRef();
        userRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //User data = snapshot.getValue(User.class);
                //System.out.println("User: " + data.toString());
                //toolbar.setTitle(data.getName());
                //getSupportActionBar().setTitle(data.getName());
            }

            @Override
            public void onCancelled(FirebaseError error) {
                System.out.println("ERROR: " + error);
            }
        });
    }
}
