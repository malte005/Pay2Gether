package com.maltedammann.pay2gether.pay2gether.events;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.model.Event;
import com.maltedammann.pay2gether.pay2gether.utils.interfaces.LongClickListener;

/**
 * Created by damma on 17.11.2016.
 */

public class EventHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnLongClickListener {

    private View mView;
    public static String eventName;
    private TextView textViewEventName;
    private CardView cv;
    private LongClickListener longClickListener;

    //Constants
    public static final int CONTEXT_EDIT_ENTRY = 0;
    public static final int CONTEXT_DELETE_ENTRY = 1;
    public final int PROFILE_EVENT = 124;

    public EventHolder(final View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.card_view_event);
        mView = itemView;
        textViewEventName = (TextView) itemView.findViewById(R.id.card_eventname);

        itemView.setOnLongClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void setLongClickListener(LongClickListener cl) {
        this.longClickListener = cl;
    }

    public void setAttributes(Event event) {
        textViewEventName = (TextView) mView.findViewById(R.id.card_eventname);
        textViewEventName.setText(event.getTitle());
        eventName = event.getTitle();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Edit event");
        menu.add(0, CONTEXT_EDIT_ENTRY, 0, "Edit");
        menu.add(0, CONTEXT_DELETE_ENTRY, 0, "Delete");
    }

    @Override
    public boolean onLongClick(View v) {
        this.longClickListener.onItemLongClick(getLayoutPosition());
        return false;
    }
}
