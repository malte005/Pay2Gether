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
import com.maltedammann.pay2gether.pay2gether.model.Event;
import com.maltedammann.pay2gether.pay2gether.utils.interfaces.ItemAddedHandler;

/**
 * Created by damma on 17.11.2016.
 */

public class AddEventDialogFragment extends AppCompatDialogFragment {

    ItemAddedHandler mEventAddedHandler;

    public AddEventDialogFragment() {
        super();
        setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.add_dialog_event, null);
        final TextView eventname = (TextView) dialogView.findViewById(R.id.addEventTitle);

        builder.setView(dialogView)
                .setMessage(R.string.dialog_add_event_msg)
                .setPositiveButton(R.string.dialog_add_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (eventname.getText().length() == 0) {
                            //username.setHint(getResources().getString(R.string.dialog_add_friend_error));
                            eventname.setError(getResources().getString(R.string.dialog_add_event_error));
                            eventname.requestFocus();
                        } else {
                            Event event = new Event(eventname.getText().toString());
                            mEventAddedHandler.onItemAdded(event);

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
            mEventAddedHandler = (ItemAddedHandler) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

}
