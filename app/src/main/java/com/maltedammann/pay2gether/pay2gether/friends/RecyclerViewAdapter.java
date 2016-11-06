package com.maltedammann.pay2gether.pay2gether.friends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.model.User;

import java.util.List;

/**
 * Created by damma on 03.11.2016.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<UserHolder>{
    private List<User> user;
    protected Context context;

    public RecyclerViewAdapter(Context context, List<User> user) {
        this.context = context;
        this.user = user;
    }
    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserHolder viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        viewHolder = new UserHolder(layoutView, user);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        holder.setAttributes(user.get(position));
    }
    @Override
    public int getItemCount() {
        return this.user.size();
    }
}