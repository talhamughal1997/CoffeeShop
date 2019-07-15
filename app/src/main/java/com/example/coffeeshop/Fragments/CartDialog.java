package com.example.coffeeshop.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.example.coffeeshop.Models.MenuItemModel;
import com.example.coffeeshop.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CartDialog extends DialogFragment {
    View rootView;
    MenuItemModel menuItemModel;
    TextView mTextView_close, mTextView_title, mTextView_name, mTextView_cost, mTextView_price;
    ImageView mImageView_item, mImageView_fav;
    private StorageReference storageReference;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_cart, null, false);
        menuItemModel = (MenuItemModel) getArguments().getSerializable("menuItem");
        viewInit();
        setData();

        return new AlertDialog.Builder(getActivity(), R.style.full_screen_dialog).setView(rootView).create();
    }

    private void viewInit() {

        mTextView_name = rootView.findViewById(R.id.txt_item_name);
        mTextView_title = rootView.findViewById(R.id.txt_item_title);
        mTextView_close = rootView.findViewById(R.id.txt_close);
        mTextView_price = rootView.findViewById(R.id.txt_price);
        mTextView_cost = rootView.findViewById(R.id.txt_cost);
        mImageView_fav = rootView.findViewById(R.id.img_like);
        mImageView_item = rootView.findViewById(R.id.img_item);
    }

    private void setData() {
        mTextView_name.setText(menuItemModel.getName());
        mTextView_title.setText(menuItemModel.getName());
        mImageView_fav.setImageResource(R.drawable.heart_fillcolor);
        // Glide.with(getActivity()).load(menuItemModel.getImageUrl()).into(mImageView_item);

        storageReference = FirebaseStorage.getInstance().getReference();
        Glide.with(getActivity()).asBitmap().apply(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888)).load(menuItemModel.getImageUrl()).into(mImageView_item);

    }


}
