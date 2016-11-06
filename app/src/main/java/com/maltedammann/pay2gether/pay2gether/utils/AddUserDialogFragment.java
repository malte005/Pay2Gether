package com.maltedammann.pay2gether.pay2gether.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.model.User;
import com.maltedammann.pay2gether.pay2gether.utils.interfaces.UserAddedHandler;

/**
 * Created by damma on 26.10.2016.
 */

public class AddUserDialogFragment extends AppCompatDialogFragment {

    private String name;
    private String mail;
    UserAddedHandler mUserAddedHandler;

    public AddUserDialogFragment() {
        super();
    }

    public static AddUserDialogFragment newInstance(String name, String mail) {
        AddUserDialogFragment f = new AddUserDialogFragment();
        f.setCancelable(false);
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("mail", mail);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //get passed argument name
        name = getArguments().getString("name");
        mail = getArguments().getString("create");

        final View dialogView = inflater.inflate(R.layout.add_dialog_friend, null);
        final TextView username = (TextView) dialogView.findViewById(R.id.addUserName);
        final TextView usermail = (TextView) dialogView.findViewById(R.id.addUserMail);

        username.setText(name);

        builder.setView(dialogView)
                .setMessage(R.string.dialog_add_friend_msg)
                .setPositiveButton(R.string.dialog_add_btn_friend, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (username.getText().length() == 0) {
                            //username.setHint(getResources().getString(R.string.dialog_add_friend_error));
                            username.setError(getResources().getString(R.string.dialog_add_friend_error));
                            username.requestFocus();
                        } else {
                            User user;
                            if (usermail.getText().toString().equals("") || usermail == null) {
                                user = new User(username.getText().toString());
                            } else {
                                user = new User(username.getText().toString(), usermail.getText().toString());
                            }
                            mUserAddedHandler.onItemAdded(user);
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
            mUserAddedHandler = (UserAddedHandler) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

}
