package com.maltedammann.pay2gether.pay2gether.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.control.DbUtils;

import static com.maltedammann.pay2gether.pay2gether.main.MainActivity.PREF_UID;

/**
 * Created by damma on 24.10.2016.
 */

public class LogoutUtils {

    private static DbUtils db;

    public static Dialog showLogoutDeleteDialog(Context c, String text, final String posivitve) {
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(c);
        myBuilder.setMessage(text);
        myBuilder.setCancelable(true);
        final Context context = c;

        myBuilder.setPositiveButton(
                posivitve,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (("Sign Out").equals(posivitve)) {
                            logout();
                            System.out.println("Signed out...");
                        } else {
                            delete(context);
                            System.out.println("user deleted...");
                        }
                    }
                });

        myBuilder.setNegativeButton(
                c.getString(R.string.btnCancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return myBuilder.create();
    }

    private static void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    private static void delete(Context context) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        db  = new DbUtils(context);
        if (user != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String mainUserKey = prefs.getString(PREF_UID, null);
            //Delete mainUser from DB
            try {
                db.deleteUser(mainUserKey);
            } catch (NullPointerException ex) {
                Log.d("LogoutUtils", ex.toString());
            }

            //Delete user from sharePrefs
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();

            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseAuth.getInstance().signOut();
                                Log.d("LogoutUtils", "User account deleted.");
                            }
                        }
                    });
        }
    }
}
