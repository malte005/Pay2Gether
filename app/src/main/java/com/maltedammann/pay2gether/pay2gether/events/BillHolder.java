package com.maltedammann.pay2gether.pay2gether.events;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.model.Bill;
import com.maltedammann.pay2gether.pay2gether.utils.interfaces.LongClickListener;

import java.text.DecimalFormat;

/**
 * Created by damma on 15.01.2017.
 */

public class BillHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnLongClickListener {

    private View mView;
    public static String billName;
    private TextView textViewBillName, textViewBillAmount, getTextViewBillOwner;
    private CardView cv;
    private LongClickListener longClickListener;

    //Constants
    public static final int CONTEXT_EDIT_ENTRY = 0;
    public static final int CONTEXT_DELETE_ENTRY = 1;
    public final int PROFILE_EVENT = 124;

    public BillHolder(final View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.card_view_bill);
        mView = itemView;
        textViewBillName = (TextView) itemView.findViewById(R.id.card_bill_name);
        textViewBillAmount = (TextView) itemView.findViewById(R.id.card_bill_amount);
        getTextViewBillOwner = (TextView) itemView.findViewById(R.id.card_bill_owner);

        itemView.setOnLongClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void setLongClickListener(LongClickListener cl) {
        this.longClickListener = cl;
    }

    public void setAttributes(Bill bill) {
        textViewBillName.setText(bill.getTitle());
        textViewBillAmount.setText(new DecimalFormat("#.00").format(bill.getAmount()));
        getTextViewBillOwner.setText(bill.getOwnerId());
        billName = bill.getTitle();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Edit bill");
        menu.add(0, CONTEXT_EDIT_ENTRY, 0, "Edit");
        menu.add(0, CONTEXT_DELETE_ENTRY, 0, "Delete");
    }

    @Override
    public boolean onLongClick(View v) {
        this.longClickListener.onItemLongClick(getLayoutPosition());
        return false;
    }
}
