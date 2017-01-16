package com.maltedammann.pay2gether.pay2gether.events;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.model.Event;
import com.maltedammann.pay2gether.pay2gether.utils.interfaces.LongClickListener;

import java.util.List;

/**
 * Created by damma on 17.11.2016.
 */

public class RecyclerViewAdapterEvent extends RecyclerView.Adapter<EventHolder> {
    private List<Event> events;
    protected Context context;
    private Event currentEvent;
    private int currentPosition;

    // Constants
    public static final String INTENT_EVENT_ID = "event_id";

    public RecyclerViewAdapterEvent(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EventHolder viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);
        viewHolder = new EventHolder(layoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventHolder holder, final int position) {
        holder.setAttributes(events.get(position));

        holder.setLongClickListener(new LongClickListener() {
            @Override
            public void onItemLongClick(int pos) {
                currentEvent = events.get(pos);
                currentPosition = pos;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go2EventProfile = new Intent(context, EventProfileActivity.class);
                go2EventProfile.putExtra(INTENT_EVENT_ID, events.get(position).getId());
                context.startActivity(go2EventProfile);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.events.size();
    }

    public Event getSelectedItem(MenuItem item) {
        return currentEvent;
    }

    public void remove() {
        events.remove(currentPosition);
        this.notifyItemRemoved(currentPosition);
        //this.notifyItemRangeChanged(currentPosition, getItemCount());
        notifyDataSetChanged();
    }

}
