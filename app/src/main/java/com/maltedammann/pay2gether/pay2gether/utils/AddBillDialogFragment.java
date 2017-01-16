package com.maltedammann.pay2gether.pay2gether.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.events.EventProfileActivity;
import com.maltedammann.pay2gether.pay2gether.model.Bill;
import com.maltedammann.pay2gether.pay2gether.model.User;
import com.maltedammann.pay2gether.pay2gether.utils.interfaces.ItemAddedHandler;

import java.util.ArrayList;

/**
 * Created by damma on 17.11.2016.
 */

public class AddBillDialogFragment extends AppCompatDialogFragment {

    ItemAddedHandler mBillAddedHandler;
    private String eventId;
    ArrayList<User> users;
    private String buyerId;
    private User selectedUser;
    private boolean ownerSet = false;

    public interface MyMessageDialogListener {
        public void onClosed(String ship, String scientist, String email, String volume, String color);
    }

    public static AddBillDialogFragment newInstance(String eventId) {
        AddBillDialogFragment f = new AddBillDialogFragment();
        f.setCancelable(false);
        Bundle args = new Bundle();
        args.putString("eventid", eventId);
        f.setArguments(args);
        return f;
    }

    public AddBillDialogFragment() {
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //get passed argument name
        eventId = getArguments().getString("eventid");

        final View dialogView = inflater.inflate(R.layout.add_dialog_bill, null);
        final TextView billname = (TextView) dialogView.findViewById(R.id.addBillName);
        final TextView billamount = (TextView) dialogView.findViewById(R.id.addBillAmount);
        final Button setOwner = (Button) dialogView.findViewById(R.id.addBillOwner);

        setOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                users = EventProfileActivity.users;

                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(getActivity(), setOwner);

                for (User user : users) {
                    popup.getMenu().add(user.getName());
                }

                if (popup.getMenu().size() == 0) {
                    Toast.makeText(getActivity(), "Couldn´t load Users, check your connection", Toast.LENGTH_SHORT).show();
                }

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        setOwner.setText("'" + item.getTitle() + "'" + " selected");
                        selectedUser = new User("TEST");
                        ownerSet = true;
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });//closing the setOnClickListener method


        builder.setView(dialogView)
                .setMessage(R.string.dialog_add_bill_msg)
                .setPositiveButton(R.string.button_text_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Bill bill;
                        boolean entriesValid = true;
                        // get the edit text values here and pass them back via the listener

                        if (TextUtils.isEmpty(billname.getText().toString())) {
                            billname.setError("Please fill the name");
                            entriesValid = false;
                            billname.requestFocus();
                        } else if (TextUtils.isEmpty(billamount.getText().toString())) {
                            billamount.setError("Please fill the amount");
                            entriesValid = false;
                            billamount.requestFocus();
                        } else if (!ownerSet) {
                            entriesValid = false;
                            //Toast.makeText(getActivity(), "Please fill the form", Toast.LENGTH_SHORT).show();
                        }
                        if (entriesValid) {
                            bill = new Bill(Float.parseFloat(billamount.getText().toString()), billname.getText().toString(), eventId, selectedUser);
                            System.out.println(bill.toString());
                            mBillAddedHandler.onItemAdded(bill);
                            dismiss();
                        }
                    }
                })
                .setNegativeButton(R.string.btnCancel, null);


        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mBillAddedHandler = (ItemAddedHandler) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

  /*  public void selectBelongers(View v) {
        AppCompatDialogFragment addBelongersFragment = AddBillBelongersFragment.newInstance();
        addBelongersFragment.setTargetFragment(AddBillDialogFragment.this, 300);
        addBelongersFragment.show(getFragmentManager(), "fragment_add_belongers");
    }*/

}
