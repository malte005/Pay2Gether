package com.maltedammann.pay2gether.pay2gether.friends;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.control.DbUtils;
import com.maltedammann.pay2gether.pay2gether.model.User;

public class EditFriendActivity extends AppCompatActivity {

    private DbUtils dbUtils;

    //UI
    private NavigationView navigationView;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    //Constants
    private static final String TAG = EditFriendActivity.class.getSimpleName();

    private String userID;
    private User user = null;

    //UI
    private EditText name;
    private EditText mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friend);

        name = (EditText) findViewById(R.id.editTextName);
        mail = (EditText) findViewById(R.id.editTextMail);
        dbUtils = new DbUtils(this);

        userID = getIntent().getStringExtra(FriendsActivity.INTENT_USER_ID);

        //Firebase init
        setupFirebase();
    }

    private void setupFirebase() {
        dbUtils.mFirebaseDbReference.child(dbUtils.USER_REF).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                System.out.println("USER: " + user.toString());
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
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
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
