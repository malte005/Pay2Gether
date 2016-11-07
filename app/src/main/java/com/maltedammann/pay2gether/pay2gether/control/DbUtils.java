package com.maltedammann.pay2gether.pay2gether.control;

import android.app.ProgressDialog;
import android.content.Context;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.model.Event;
import com.maltedammann.pay2gether.pay2gether.model.User;
import com.maltedammann.pay2gether.pay2gether.utils.FirebaseRefFactory;
import com.maltedammann.pay2gether.pay2gether.utils.interfaces.ProgressDialogHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by damma on 27.10.2016.
 */

public class DbUtils implements ProgressDialogHandler{

    private Context context;
    private ProgressDialog pd;
    final static String USER_REF = "users";
    final static String CHILD_REF_BILLS = "bills";
    final static String BILL_REF = "bills";
    final static String EVENT_REF = "events";
    final static String CHILD_REF_PARTICIPANTS = "participants";
    public DatabaseReference db;

    public DbUtils(Context c) {
        db = FirebaseDatabase.getInstance().getReference();
        this.context = c;
    }

    // USER
    public String addUser(User user) {
        DatabaseReference temp_user = db.child(USER_REF).push();
        String key = temp_user.getKey();
        user.setId(key);
        temp_user.setValue(user);
        return key;
    }

    public void deleteUser(String key) {
        pd = new ProgressDialog(context);
        showProgressDialog(context.getResources().getString(R.string.deleting));
        Firebase user = FirebaseRefFactory.getUsersRef();
        user.child(key).removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    System.out.println("Error deleting data: " + firebaseError.getMessage());
                }
                hideProgressDialog();
                //UIHelper.snack(((Activity)context).findViewById(R.id.clFriends), userName + " deleted");
            }
        });
    }

    public void editUser(User user) {
        pd = new ProgressDialog(context);
        showProgressDialog(context.getResources().getString(R.string.updating));

        Firebase userRef = FirebaseRefFactory.getUsersRef();

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> update = new HashMap<>();
        childUpdates.put("id", user.getId());

        //update name
        if (user.getName() != null) {
            childUpdates.put("name", user.getName());
        }
        //update mail
        if (user.getMail() != null) {
            childUpdates.put("mail", user.getMail());
        }

        update.put("/" + user.getId(), childUpdates);


       userRef.updateChildren(update, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    System.out.println("Error updating data: " + firebaseError.getMessage());
                }
                hideProgressDialog();
            }
        });


    }
    // USER

    // EVENT
    public String addEvent(Event event) {
        DatabaseReference temp_event = db.child(EVENT_REF).push();
        String key = temp_event.getKey();
        event.setId(key);
        temp_event.setValue(event);
        return key;
    }

    @Override
    public void showProgressDialog(String message) {
        if (pd == null) {
            pd = new ProgressDialog(context);
            pd.setMessage(message);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
        }
        pd.show();
    }

    @Override
    public void hideProgressDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }
}
