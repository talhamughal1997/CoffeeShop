package com.example.coffeeshop.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.coffeeshop.Models.UserCartModel;
import com.example.coffeeshop.R;

import java.util.ArrayList;

public class CartsAdapter extends RecyclerView.Adapter<CartsAdapter.CartViewHolder> {

    ArrayList<UserCartModel> arrayList;
    UserCartModel mRecentlyDeletedItem;
    int mRecentlyDeletedItemPosition;
    Activity activity;

    public CartsAdapter(ArrayList<UserCartModel> arrayList, Activity activity) {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.carts_list_items, viewGroup, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i) {

        cartViewHolder.mTextView_name.setText(arrayList.get(i).getName());
        cartViewHolder.mTextView_qty.setText(arrayList.get(i).getQty() + "x");
        cartViewHolder.mTextView_price.setText("$ " + arrayList.get(i).getPrice());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView_name, mTextView_qty, mTextView_price;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView_name = itemView.findViewById(R.id.txt_item_name);
            mTextView_qty = itemView.findViewById(R.id.txt_item_qty);
            mTextView_price = itemView.findViewById(R.id.txt_item_price);
        }
    }


    public void deleteItem(int position) {
        mRecentlyDeletedItem = arrayList.get(position);
        mRecentlyDeletedItemPosition = position;
        arrayList.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = activity.findViewById(R.id.card_constraint);
        Snackbar snackbar = Snackbar.make(view, "Deleted",
                Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoDelete();
            }
        });
        snackbar.show();
    }

    private void undoDelete() {
        arrayList.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

}
