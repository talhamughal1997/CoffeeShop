package com.example.coffeeshop.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.example.coffeeshop.Fragments.CartDialog;
import com.example.coffeeshop.Models.MenuItemModel;
import com.example.coffeeshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.CartViewHolder> {

    ArrayList<MenuItemModel> arrayList;
    MenuItemModel mRecentlyDeletedItem;
    int mRecentlyDeletedItemPosition;
    Activity activity;
    boolean isUndo;

    public FavouritesAdapter(ArrayList<MenuItemModel> arrayList, Activity activity) {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fav_list_items, viewGroup, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i) {

        cartViewHolder.mTextView_name.setText(arrayList.get(i).getName());
        cartViewHolder.mTextView_desc.setText(arrayList.get(i).getDescription() + "x");
        cartViewHolder.mTextView_price.setText("$ " + arrayList.get(i).getPrice());
        Glide.with(activity).asBitmap().apply(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888)).load(arrayList.get(i).getImageUrl()).into(cartViewHolder.mImageView_item);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView_name, mTextView_desc, mTextView_price;
        ImageView mImageView_item;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView_name = itemView.findViewById(R.id.txt_item_name);
            mTextView_desc = itemView.findViewById(R.id.txt_item_desc);
            mTextView_price = itemView.findViewById(R.id.txt_item_price);
            mImageView_item = itemView.findViewById(R.id.img_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCartDialog(getAdapterPosition());
                }
            });
        }

    }


    private void showCartDialog(final int pos) {
        final int[] a = new int[1];
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Favourites").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (a[0] == pos) {
                        CartDialog dialog = new CartDialog();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("menuItem", arrayList.get(pos));
                        bundle.putString("itemId", ds.getKey());
                        Toast.makeText(activity, ds.getKey(), Toast.LENGTH_SHORT).show();
                        dialog.setArguments(bundle);
                        ((AppCompatActivity)activity).getSupportFragmentManager().popBackStack();
                        dialog.show(((AppCompatActivity) activity).getSupportFragmentManager(), "cart");
                        break;
                    }
                    a[0]++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void deleteItem(int position) {
        mRecentlyDeletedItem = arrayList.get(position);
        mRecentlyDeletedItemPosition = position;
        arrayList.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar(position);
    }

    private void showUndoSnackbar(final int position) {
        View view = activity.findViewById(R.id.card_constraint);
        Snackbar snackbar = Snackbar.make(view, "Deleted",
                Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoDelete();
            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                final int[] count = {0};
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Favourites").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (count[0] == position && isUndo == false) {
                                ds.getRef().removeValue();
                                break;
                            }
                            count[0]++;
                        }
                        isUndo = false;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onShown(Snackbar sb) {

            }
        });
    }

    private void undoDelete() {
        arrayList.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
        isUndo = true;
    }


}
