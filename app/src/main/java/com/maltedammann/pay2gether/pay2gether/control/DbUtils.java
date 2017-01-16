package com.maltedammann.pay2gether.pay2gether.control;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.model.Bill;
import com.maltedammann.pay2gether.pay2gether.model.Event;
import com.maltedammann.pay2gether.pay2gether.model.User;
import com.maltedammann.pay2gether.pay2gether.utils.FirebaseRefFactory;
import com.maltedammann.pay2gether.pay2gether.utils.UIHelper;
import com.maltedammann.pay2gether.pay2gether.utils.interfaces.ProgressDialogHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.maltedammann.pay2gether.pay2gether.events.BillHolder.billName;
import static com.maltedammann.pay2gether.pay2gether.events.EventHolder.eventName;
import static com.maltedammann.pay2gether.pay2gether.user.UserHolder.userName;

/**
 * Created by damma on 27.10.2016.
 */

public class DbUtils implements ProgressDialogHandler {

    private Context context;
    private ProgressDialog pd;

    //Constants
    public final static String USER_REF = "users";
    public final static String BILL_REF = "bills";
    public final static String EVENT_REF = "events";

    //Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mFirebaseDbReference;
    private DatabaseReference mFirebaseReferenceUsers;
    private DatabaseReference mFirebaseReferenceEvents;
    private DatabaseReference mFirebaseReferenceBills;
    public Firebase mFirebaseUserRef = FirebaseRefFactory.getRef("users");
    public Firebase mFirebaseEventsRef = FirebaseRefFactory.getRef("events");
    public Firebase mFirebaseBillsRef = FirebaseRefFactory.getRef("bills");

    public DbUtils(Context c) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDbReference = mFirebaseDatabase.getInstance().getReference();
        this.context = c;
    }

    // USER
    public ArrayList<User> getAllUsers() {
        final ArrayList<User> users = new ArrayList<User>();
        final Firebase ref = FirebaseRefFactory.getRef().child(USER_REF);


        ref.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot user : snapshot.getChildren()) {
                    users.add(user.getValue(User.class));
                    System.out.println("Listener " + user.toString());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        System.out.println(users.size() + " users back");
        return users;

    }

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

    public void deleteEvent(final String key) {
        pd = new ProgressDialog(context);
        showProgressDialog(context.getResources().getString(R.string.deleting));

        mFirebaseEventsRef.child(key).removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    System.out.println("Error deleting data: " + firebaseError.getMessage());
                }
                deleteAllRelatedBills(key);
                hideProgressDialog();
                UIHelper.snack(((Activity) context).findViewById(R.id.clEvents), eventName + " deleted");
            }
        });
    }

    private void deleteAllRelatedBills(String eventId) {
        Firebase ref = (Firebase) mFirebaseBillsRef.orderByChild("eventId").equalTo(eventId);

    }

    public void editEvent(Event event) {
        pd = new ProgressDialog(context);
        //showProgressDialog(context.getResources().getString(R.string.updating));

        mFirebaseEventsRef = FirebaseRefFactory.getRef("events");

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> update = new HashMap<>();
        childUpdates.put("id", event.getId());

        System.out.println("Event to update: " + event.toString());

        //update title
        if (event.getTitle() != null) {
            System.out.println("Update title");
            childUpdates.put("title", event.getTitle());
            System.out.println("Updated title");
        }
        //update time
        if (event.getTime() != null) {
            System.out.println("Update time: " + event.getTime());
            childUpdates.put("time", event.getTime());
            System.out.println("Updated time");
        }
        //update date
        if (event.getDate() != null) {
            System.out.println("Update date: " + event.getDate());
            childUpdates.put("date", event.getDate());
            System.out.println("Updated date");
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
        mFirebaseReferenceBills = mFirebaseDbReference.child(BILL_REF).push();
        String key = mFirebaseReferenceBills.getKey();
        bill.setId(key);
        mFirebaseReferenceBills.setValue(bill);
        return key;
    }

    public void deleteBill(final String key) {
        pd = new ProgressDialog(context);
        showProgressDialog(context.getResources().getString(R.string.deleting));

        mFirebaseBillsRef.child(key).removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    System.out.println("Error deleting data: " + firebaseError.getMessage());
                }
                hideProgressDialog();
                UIHelper.snack(((Activity) context).findViewById(R.id.clEventProfile), billName + " deleted");
            }
        });
    }


    // BIll


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
