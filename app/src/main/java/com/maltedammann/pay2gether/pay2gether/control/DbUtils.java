package com.maltedammann.pay2gether.pay2gether.control;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.model.Bill;
import com.maltedammann.pay2gether.pay2gether.model.Event;
import com.maltedammann.pay2gether.pay2gether.model.User;
import com.maltedammann.pay2gether.pay2gether.utils.FirebaseRefFactory;
import com.maltedammann.pay2gether.pay2gether.utils.UIHelper;
import com.maltedammann.pay2gether.pay2gether.utils.interfaces.ProgressDialogHandler;

import java.util.HashMap;
import java.util.Map;

import static com.maltedammann.pay2gether.pay2gether.events.EventHolder.eventName;
import static com.maltedammann.pay2gether.pay2gether.friends.UserHolder.userName;

/**
 * Created by damma on 27.10.2016.
 */

public class DbUtils implements ProgressDialogHandler {

    private Context context;
    private ProgressDialog pd;

    //Constants
    public final static String USER_REF = "users";
    public final static String CHILD_REF_BILLS = "bills";
    public final static String BILL_REF = "bills";
    public final static String EVENT_REF = "events";
    public final static String CHILD_REF_PARTICIPANTS = "participants";

    //Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mFirebaseDbReference;
    private DatabaseReference mFirebaseReferenceUsers;
    private DatabaseReference mFirebaseReferenceEvents;
    private DatabaseReference mFirebaseReferenceBills;
    private Firebase mFirebaseUserRef;
    private Firebase mFirebaseEventsRef;

    public DbUtils(Context c) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDbReference = mFirebaseDatabase.getInstance().getReference();
        this.context = c;
    }

    // USER
    public String addUser(User user) {
        mFirebaseReferenceUsers = mFirebaseDbReference.child(USER_REF).push();
        String key = mFirebaseReferenceUsers.getKey();
        user.setId(key);
        mFirebaseReferenceUsers.setValue(user);
        return key;
    }

    public void deleteUser(String key) {
        pd = new ProgressDialog(context);
        showProgressDialog(context.getResources().getString(R.string.deleting));

        mFirebaseUserRef = FirebaseRefFactory.getRef("users");

        mFirebaseUserRef.child(key).removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    System.out.println("Error deleting data: " + firebaseError.getMessage());
                }
                hideProgressDialog();
                UIHelper.snack(((Activity) context).findViewById(R.id.clFriends), userName + " deleted");
            }
        });
    }

    public void editUser(User user) {
        pd = new ProgressDialog(context);
        //showProgressDialog(context.getResources().getString(R.string.updating));

        mFirebaseUserRef = FirebaseRefFactory.getRef("users");

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

        mFirebaseUserRef.updateChildren(update, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    System.out.println("Error updating data: " + firebaseError.getMessage());
                }
                //hideProgressDialog();
            }
        });


    }
    // USER

    // EVENT
    public String addEvent(Event event) {
        mFirebaseReferenceEvents = mFirebaseDbReference.child(EVENT_REF).push();
        String key = mFirebaseReferenceEvents.getKey();
        event.setId(key);
        mFirebaseReferenceEvents.setValue(event);
        return key;
    }

    public void deleteEvent(String key) {
        pd = new ProgressDialog(context);
        showProgressDialog(context.getResources().getString(R.string.deleting));

        mFirebaseEventsRef = FirebaseRefFactory.getRef("events");

        mFirebaseEventsRef.child(key).removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    System.out.println("Error deleting data: " + firebaseError.getMessage());
                }
                hideProgressDialog();
                UIHelper.snack(((Activity) context).findViewById(R.id.clEvents), eventName + " deleted");
            }
        });
    }

    public void editEvent(Event event) {
        pd = new ProgressDialog(context);
        //showProgressDialog(context.getResources().getString(R.string.updating));

        mFirebaseEventsRef = FirebaseRefFactory.getRef("events");

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> update = new HashMap<>();
        childUpdates.put("id", event.getId());

        //update title
        if (event.getTitle() != null) {
            childUpdates.put("title", event.getTitle());
        }
        //update date
        if (event.getDate() != null) {
            childUpdates.put("date", event.getDate());
        }

        update.put("/" + event.getId(), childUpdates);

        mFirebaseEventsRef.updateChildren(update, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    System.out.println("Error updating data: " + firebaseError.getMessage());
                }
                //hideProgressDialog();
            }
        });
    }


    // BIll
    public String addBill(Bill bill) {
        mFirebaseReferenceBills = mFirebaseDbReference.child(EVENT_REF).push();
        String key = mFirebaseReferenceBills.getKey();
        bill.setId(key);
        mFirebaseReferenceBills.setValue(bill);
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
