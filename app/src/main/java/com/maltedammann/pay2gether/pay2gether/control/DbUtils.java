package com.maltedammann.pay2gether.pay2gether.control;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maltedammann.pay2gether.pay2gether.model.User;

/**
 * Created by damma on 27.10.2016.
 */

public class DbUtils {

    final static String USER_REF = "users";
    final static String CHILD_REF_BILLS = "bills";
    final static String BILL_REF = "bills";
    final static String EVENT_REF = "events";
    final static String CHILD_REF_PARTICIPANTS = "participants";
    public DatabaseReference db;

    public DbUtils() {
        db = FirebaseDatabase.getInstance().getReference();
    }

    public String addUser(User user) {
        DatabaseReference temp_user = db.child(USER_REF).push();
        String key = temp_user.getKey();
        user.setId(key);
        temp_user.setValue(user);
        return key;
    }

    public void deleteUser(String key) {
        db.child(USER_REF).child(key).removeValue();;
    }
}
