package com.maltedammann.pay2gether.pay2gether.friends;

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
import com.maltedammann.pay2gether.pay2gether.model.User;

public class EditFriendActivity extends AppCompatActivity {

    //DB
    private DbUtils dbUtils;

    //UI
    private Toolbar toolbar;

    //Constants
    private static final String TAG = EditFriendActivity.class.getSimpleName();

    private String userID;
    private User user = null;

    //UI
    private EditText name;
    private EditText mail;

    //Firebase
    private ValueEventListener mListener;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friend);
        toolbar = (Toolbar) findViewById(R.id.toolbar_editUser);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (EditText) findViewById(R.id.editTextName);
        mail = (EditText) findViewById(R.id.editTextMail);
        dbUtils = new DbUtils(this);

        userID = getIntent().getStringExtra(FriendsActivity.INTENT_USER_ID);

        //Firebase init
        setupFirebase();
    }

    private void setupFirebase() {
        mRef = dbUtils.mFirebaseDbReference.child(dbUtils.USER_REF).child(userID);
        if (mListener == null) {
            mListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);
                    if (user.getName() != null) {
                        name.setText(user.getName());
                    }
                    if (user.getMail() != null) {
                        mail.setText(user.getMail());
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
                user.setName(name.getText().toString());
                user.setMail(mail.getText().toString());
                System.out.println("NEUER USER: " + user.toString());
                dbUtils.editUser(user);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
