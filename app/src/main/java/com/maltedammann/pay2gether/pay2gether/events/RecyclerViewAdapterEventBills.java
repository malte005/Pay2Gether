package com.maltedammann.pay2gether.pay2gether.events;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.maltedammann.pay2gether.pay2gether.R;
import com.maltedammann.pay2gether.pay2gether.model.Bill;
import com.maltedammann.pay2gether.pay2gether.utils.interfaces.LongClickListener;

import java.util.List;

/**
 * Created by damma on 15.01.2017.
 */

public class RecyclerViewAdapterEventBills extends RecyclerView.Adapter<BillHolder> {
    private List<Bill> bills;
    protected Context context;
    private Bill currentBill;
    private int currentPosition;

    // Constants
    public static final String INTENT_BILL_ID = "bill_id";

    public RecyclerViewAdapterEventBills(Context context, List<Bill> bills) {
        this.context = context;
        this.bills = bills;
    }

    @Override
    public BillHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BillHolder viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_card, parent, false);
        viewHolder = new BillHolder(layoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BillHolder holder, final int position) {
        holder.setAttributes(bills.get(position));

        holder.setLongClickListener(new LongClickListener() {
            @Override
            public void onItemLongClick(int pos) {
                currentBill = bills.get(pos);
                currentPosition = pos;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Intent go2EditBill = new Intent(context, EventProfileActivity.class);
                go2EditBill.putExtra(INTENT_BILL_ID, bills.get(position).getId());
                context.startActivity(go2EditBill);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.bills.size();
    }

    public Bill getSelectedItem(MenuItem item) {
        return currentBill;
    }

    public void remove() {
        bills.remove(currentPosition);
        this.notifyItemRemoved(currentPosition);
        //this.notifyItemRangeChanged(currentPosition, getItemCount());
        notifyDataSetChanged();
    }

}
