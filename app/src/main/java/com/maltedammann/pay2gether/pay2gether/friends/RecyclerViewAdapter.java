package com.maltedammann.pay2gether.pay2gether.friends;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.model.User;
import com.maltedammann.pay2gether.pay2gether.utils.interfaces.LongClickListener;

import java.util.List;

/**
 * Created by damma on 03.11.2016.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<UserHolder> {
    private List<User> users;
    protected Context context;
    private User currentUser;
    private int currentPosition;
    // Constants
    public static final String INTENT_USER_ID = "user_id";

    public RecyclerViewAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserHolder viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        viewHolder = new UserHolder(layoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserHolder holder, final int position) {
        holder.setAttributes(users.get(position));

        holder.setLongClickListener(new LongClickListener() {
            @Override
            public void onItemLongClick(int pos) {
                currentUser = users.get(pos);
                currentPosition = pos;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go2UserProfile = new Intent(context, UserProfileActivity.class);
                go2UserProfile.putExtra(INTENT_USER_ID, users.get(position).getId());
                context.startActivity(go2UserProfile);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    public User getSelectedItem(MenuItem item) {
        return currentUser;
    }

    public void remove() {
        System.out.println("CURRENT: " + currentPosition);
        users.remove(currentPosition);
        this.notifyItemRemoved(currentPosition);
        this.notifyItemRangeChanged(currentPosition, getItemCount());
        notifyDataSetChanged();
    }

}