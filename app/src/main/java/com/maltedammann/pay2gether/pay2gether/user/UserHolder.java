package com.maltedammann.pay2gether.pay2gether.user;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.model.User;
import com.maltedammann.pay2gether.pay2gether.utils.interfaces.LongClickListener;

/**
 * Created by damma on 03.11.2016.
 */

public class UserHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnLongClickListener {

    private View mView;
    public static String userName;
    private TextView textViewUserName;
    private CardView cv;
    private LongClickListener longClickListener;

    //Constants
    public static final int CONTEXT_EDIT_ENTRY = 0;
    public static final int CONTEXT_DELETE_ENTRY = 1;
    public final int PROFILE_FRIEND = 124;

    public UserHolder(final View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.card_view_user);
        mView = itemView;
        textViewUserName = (TextView) itemView.findViewById(R.id.card_username);

        itemView.setOnLongClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void setLongClickListener(LongClickListener cl) {
        this.longClickListener = cl;
    }

    public void setAttributes(User user) {
        textViewUserName = (TextView) mView.findViewById(R.id.card_username);
        textViewUserName.setText(user.getName());
        userName = user.getName();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Edit friend");
        menu.add(0, CONTEXT_EDIT_ENTRY, 0, "Edit");
        menu.add(0, CONTEXT_DELETE_ENTRY, 0, "Delete");
    }

    @Override
    public boolean onLongClick(View v) {
        this.longClickListener.onItemLongClick(getLayoutPosition());
        return false;
    }
}
