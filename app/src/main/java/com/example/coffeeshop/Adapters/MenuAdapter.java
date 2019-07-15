package com.example.coffeeshop.Adapters;

import android.content.Intent;
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
import com.example.coffeeshop.Models.MenuModel;
import com.example.coffeeshop.R;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    AppCompatActivity activity;
    ArrayList<MenuModel> arrayList;

    public MenuAdapter(AppCompatActivity activity, ArrayList<MenuModel> coffeeArray) {
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
    public void onBindViewHolder(@NonNull final MenuViewHolder menuViewHolder, final int i) {

        menuViewHolder.mTextView_name.setText(arrayList.get(i).getName());
        menuViewHolder.mTextView_taste.setText(arrayList.get(i).getTaste());
        Glide.with(menuViewHolder.itemView.getContext()).asBitmap().apply(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888)).load(arrayList.get(i).getImageUrl()).into(menuViewHolder.mImageView_item);

        menuViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MenuDetailsActivity.class);
                intent.putExtra("menuId", arrayList.get(i).getId());
                activity.startActivity(intent);
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
            mTextView_name = itemView.findViewById(R.id.txt_item_title);
            mTextView_taste = itemView.findViewById(R.id.txt_item_taste);
        }
    }
}
