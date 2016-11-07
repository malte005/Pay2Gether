package com.maltedammann.pay2gether.pay2gether.friends;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.model.User;

import java.util.List;

import static com.maltedammann.pay2gether.pay2gether.friends.FriendsActivity.uid;

/**
 * Created by damma on 03.11.2016.
 */

public class UserHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

    private final Context context;
    private View mView;
    private List<User> users;
    public static String userName;
    private TextView textViewUserName;
    private CardView cv;

    //Constants
    public static final int CONTEXT_EDIT_ENTRY = 0;
    public static final int CONTEXT_DELETE_ENTRY = 1;
    public final int PROFILE_FRIEND = 124;

    public UserHolder(final View itemView, final List<User> users) {
        super(itemView);
        context = itemView.getContext();
        cv = (CardView) itemView.findViewById(R.id.card_view_user);
        mView = itemView;
        textViewUserName = (TextView) itemView.findViewById(R.id.card_username);
        this.users = users;

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                uid = v.getId();
                System.out.println("IDXAXAXA: " + uid);
                return false;
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent go2UserProfile = new Intent(context, UserProfileActivity.class);
                go2UserProfile.putExtra("user_id", users.get())
                context.startActivity(go2UserProfile);*/
            }
        });

        itemView.setOnCreateContextMenuListener(this);
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
}
