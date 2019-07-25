package com.example.coffeeshop.Adapters;

import android.support.annotation.NonNull;
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

    public CartsAdapter(ArrayList<UserCartModel> arrayList) {
        this.arrayList = arrayList;
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
}
