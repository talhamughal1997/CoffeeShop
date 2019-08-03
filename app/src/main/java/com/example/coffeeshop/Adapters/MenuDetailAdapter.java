package com.example.coffeeshop.Adapters;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
import com.example.coffeeshop.Controllers.Utils;
import com.example.coffeeshop.Fragments.CartDialog;
import com.example.coffeeshop.Models.MenuItemModel;
import com.example.coffeeshop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MenuDetailAdapter extends RecyclerView.Adapter<MenuDetailAdapter.MyViewHolder> {

    String menuId;
    AppCompatActivity activity;
    ArrayList<MenuItemModel> arrayList;
    private StorageReference storageReference;
    Dialog progressDialog;
    boolean isFav;

    public MenuDetailAdapter(AppCompatActivity activity, ArrayList<MenuItemModel> arrayList, String menuId) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.menuId = menuId;
        progressDialog = Utils.getProgressDialog(activity);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menudetails_list_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.mTextView_name.setText(arrayList.get(i).getName());
        myViewHolder.mTextView_taste.setText(arrayList.get(i).getTaste());
        myViewHolder.mTextView_price.setText("$ " + arrayList.get(i).getPrice());
//        progressDialog.show();
        Glide.with(activity).asBitmap().apply(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888)).load(arrayList.get(i).getImageUrl()).into(myViewHolder.mImageView_item);

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewHolder.OpenCartDialog(i);

            }
        });
        // progressDialog.hide();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
//        ImageView img_Like;
        TextView mTextView_name, mTextView_taste, mTextView_price;
        ImageView mImageView_item;
        int count = 0;
        boolean isFav;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.menulist_cardview);
//            img_Like = itemView.findViewById(R.id.img_like);
            mImageView_item = itemView.findViewById(R.id.img_menu_item);
            mTextView_name = itemView.findViewById(R.id.txt_item_title);
            mTextView_taste = itemView.findViewById(R.id.txt_item_taste);
            mTextView_price = itemView.findViewById(R.id.txt_cost);
        }




        /*private void setImageAnimation(boolean isFav) {
            if (isFav == false) {
                ImageViewAnimatedChange(itemView.getContext(), img_Like, R.drawable.heart_fillcolor);
            } else {
                ImageViewAnimatedChange(itemView.getContext(), img_Like, R.drawable.heart_outline);

            }
        }

        private void setImageAnimation(boolean isFav, DataSnapshot ds) {
            if (isFav == false) {
                ImageViewAnimatedChange(itemView.getContext(), img_Like, R.drawable.heart_fillcolor);
                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Favourites").child(uid);
                reference.child(ds.getKey()).setValue(ds.getValue());
            } else {
                ImageViewAnimatedChange(itemView.getContext(), img_Like, R.drawable.heart_outline);
                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Favourites").child(uid).child(ds.getKey());
                reference.removeValue();

            }
        }
*/
        private void OpenCartDialog(final int pos){
            final int[] a = {0};
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Menu").child(menuId).child("items");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        if (a[0] == pos){
                            CartDialog dialog = new CartDialog();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("menuItem", arrayList.get(pos));
                            bundle.putString("itemId",ds.getKey());
                            dialog.setArguments(bundle);
                            dialog.show(activity.getSupportFragmentManager(), "cart");
                        }
                        a[0]++;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}