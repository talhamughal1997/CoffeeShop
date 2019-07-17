package com.example.coffeeshop.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.example.coffeeshop.Controllers.Utils;
import com.example.coffeeshop.Fragments.CartDialog;
import com.example.coffeeshop.Models.MenuItemModel;
import com.example.coffeeshop.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MenuDetailAdapter extends RecyclerView.Adapter<MenuDetailAdapter.MyViewHolder> {

    AppCompatActivity activity;
    ArrayList<MenuItemModel> arrayList;
    private StorageReference storageReference;
    Dialog progressDialog;

    public MenuDetailAdapter(AppCompatActivity activity, ArrayList<MenuItemModel> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
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
        myViewHolder.mTextView_price.setText(String.valueOf(arrayList.get(i).getPrice()));
        storageReference = FirebaseStorage.getInstance().getReference();
//        progressDialog.show();
        Glide.with(activity).asBitmap().apply(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888)).load(arrayList.get(i).getImageUrl()).into(myViewHolder.mImageView_item);

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartDialog dialog = new CartDialog();
                Bundle bundle = new Bundle();
                bundle.putSerializable("menuItem", arrayList.get(i));
                dialog.setArguments(bundle);
                dialog.show(activity.getSupportFragmentManager(), "cart");
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
        ImageView img_Like;
        TextView mTextView_name, mTextView_taste, mTextView_price;
        ImageView mImageView_item;
        int a = 0;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.menulist_cardview);
            img_Like = itemView.findViewById(R.id.img_like);
            mImageView_item = itemView.findViewById(R.id.img_menu_item);
            mTextView_name = itemView.findViewById(R.id.txt_item_title);
            mTextView_taste = itemView.findViewById(R.id.txt_item_taste);
            mTextView_price = itemView.findViewById(R.id.txt_cost);


            img_Like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setImageAnimation();
                }
            });
        }


        public void ImageViewAnimatedChange(Context c, final ImageView v, final int new_image) {
            final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
            final Animation anim_in = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
            anim_out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setImageResource(new_image);
                    anim_in.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }
                    });
                    v.startAnimation(anim_in);
                }
            });
            v.startAnimation(anim_out);
        }

        private void setImageAnimation() {
            if (a > 0) {
                a--;
                ImageViewAnimatedChange(itemView.getContext(), img_Like, R.drawable.heart_outline);
            } else {
                a++;
                ImageViewAnimatedChange(itemView.getContext(), img_Like, R.drawable.heart_fillcolor);
            }

        }


    }

}
