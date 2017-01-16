package com.maltedammann.pay2gether.pay2gether.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.control.DbUtils;
import com.maltedammann.pay2gether.pay2gether.model.User;

import java.util.ArrayList;

/**
 * Created by damma on 10.01.2017.
 */

public class AddBillBelongersFragment extends AppCompatDialogFragment {

    private ArrayList mSelectedItems;
    private ArrayList<User> allUsers;
    private DbUtils db;

    public static AddBillDialogFragment newInstance() {
        AddBillDialogFragment f = new AddBillDialogFragment();
        f.setCancelable(false);
        return f;
    }

    public AddBillBelongersFragment() {
        super();
    }

    private ArrayList<User> getAllUsers() {
        db = new DbUtils(getActivity().getApplicationContext());
        return db.getAllUsers();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mSelectedItems = new ArrayList<String>();  // Where we track the selected items
        final CharSequence[] dialogList = allUsers.toArray(new CharSequence[allUsers.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int count = dialogList.length;
        boolean[] is_checked = new boolean[count];

        //get passed argument name
        mSelectedItems = getArguments().getStringArrayList("selectedUserIds");

        builder.setTitle(R.string.dialog_add_bill_owner_header)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(dialogList, is_checked,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(allUsers.get(which));
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton(R.string.btnCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });

        return builder.create();
    }

    private ArrayList<User> getUsers() {
        allUsers = getAllUsers();
        return allUsers;
    }
}
