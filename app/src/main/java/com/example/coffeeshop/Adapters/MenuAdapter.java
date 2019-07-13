package com.example.coffeeshop.Adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.example.coffeeshop.Activities.MenuDetailsActivity;
import com.example.coffeeshop.Controllers.Utils;
import com.example.coffeeshop.Models.CoffeeModel;
import com.example.coffeeshop.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    AppCompatActivity activity;
    ArrayList<CoffeeModel> arrayList;
    StorageReference storageReference;

    public MenuAdapter(AppCompatActivity activity, ArrayList<CoffeeModel> coffeeArray) {
        this.activity = activity;
        this.arrayList = coffeeArray;

    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_list_item, viewGroup, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuViewHolder menuViewHolder, int i) {

        menuViewHolder.mTextView_name.setText(arrayList.get(i).getName());
        menuViewHolder.mTextView_taste.setText(arrayList.get(i).getTaste());
        storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(arrayList.get(i).getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(menuViewHolder.itemView.getContext()).asBitmap().apply(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888)).load(uri).into(menuViewHolder.mImageView_item);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        menuViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.changeActivity(activity, MenuDetailsActivity.class);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView mTextView_name, mTextView_taste;
        ImageView mImageView_item;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.menulist_cardview);
            mImageView_item = itemView.findViewById(R.id.img_menu_item);
            mTextView_name = itemView.findViewById(R.id.txt_item_name);
            mTextView_taste = itemView.findViewById(R.id.txt_item_taste);
        }
    }
}
