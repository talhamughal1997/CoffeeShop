package com.example.coffeeshop.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.example.coffeeshop.Controllers.Utils;
import com.example.coffeeshop.Models.MenuItemModel;
import com.example.coffeeshop.Models.UserCartModel;
import com.example.coffeeshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartDialog extends DialogFragment {
    View rootView;
    MenuItemModel menuItemModel;
    TextView mTextView_close, mTextView_title, mTextView_name, mTextView_cost, mTextView_price;
    ImageView mImageView_item, mImageView_fav;
    Button btnAddToCart;
    Spinner mSpinner_quanity;
    String name, itemId, uid;
    boolean isFav;
    int qty;
    long price;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_addtocart, null, false);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        menuItemModel = (MenuItemModel) getArguments().getSerializable("menuItem");
        itemId = getArguments().getString("itemId");
        getAlreadyFav(itemId);
        viewInit();
        setData();

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddToCart();
            }
        });

        mTextView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return new AlertDialog.Builder(getActivity(), R.style.full_screen_dialog).setView(rootView).create();
    }

    private void viewInit() {
        mTextView_name = rootView.findViewById(R.id.txt_item_name);
        mTextView_title = rootView.findViewById(R.id.txt_item_title);
        mTextView_close = rootView.findViewById(R.id.txt_close);
        mTextView_price = rootView.findViewById(R.id.txt_price);
        mTextView_cost = rootView.findViewById(R.id.txt_cost);
        mImageView_fav = rootView.findViewById(R.id.img_like);
        setImgFav();
        mImageView_item = rootView.findViewById(R.id.img_item);
        mSpinner_quanity = rootView.findViewById(R.id.spinner_quantity);
        btnAddToCart = rootView.findViewById(R.id.btn_add_to_cart);
    }

    private void setData() {
        mTextView_name.setText(menuItemModel.getName());
        mTextView_title.setText(menuItemModel.getName());
        mTextView_cost.setText("$ " + menuItemModel.getPrice());
        mImageView_fav.setImageResource(R.drawable.heart_outline);
        Glide.with(getActivity()).asBitmap().apply(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888)).load(menuItemModel.getImageUrl()).into(mImageView_item);
        setSpinnerQuanity();
        Utils.setSpinnerHeight(mSpinner_quanity);
    }

    private void setSpinnerQuanity() {
        List<Integer> spinnerArray = new ArrayList<>();
        for (int a = 0; a <= 20; a++) {
            spinnerArray.add(a);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(
                getActivity(), R.layout.spinner_item, spinnerArray);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mSpinner_quanity.setAdapter(adapter);
        setTotalPrice();
    }

    private void setTotalPrice() {
        mSpinner_quanity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int a = (int) parent.getSelectedItem();
                price = menuItemModel.getPrice() * a;

                mTextView_price.setText("$ " + price);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setAddToCart() {
        if (mSpinner_quanity.getSelectedItem().equals(0) || price == 0) {
            Toast.makeText(getActivity(), "Select Quantity", Toast.LENGTH_SHORT).show();
        } else {
            AddOrderToDataBase();
        }
    }

    private void AddOrderToDataBase() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Carts").child(uid);
        String pushId = ref.push().getKey();
        name = menuItemModel.getName();
        qty = Integer.parseInt(mSpinner_quanity.getSelectedItem().toString());
        UserCartModel model = new UserCartModel(name, qty, price);
        ref.child(pushId).setValue(model, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(getActivity(), "Added To Cart Successfully", Toast.LENGTH_SHORT).show();
                    CartDialog.this.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Data Could not be Saved . " + databaseError, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setImgFav() {
        mImageView_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Favourites").child(uid);

                if (isFav == false) {
                    ref.child(itemId).setValue(menuItemModel);
                    ImageViewAnimatedChange(getActivity(), mImageView_fav, R.drawable.heart_fillcolor);
                    isFav = true;
                } else {
                    ref.child(itemId).removeValue();
                    ImageViewAnimatedChange(getActivity(), mImageView_fav, R.drawable.heart_outline);
                    isFav = false;
                }
            }
        });
    }

    private void getAlreadyFav(final String id) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Favourites").child(uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (id.equals(ds.getKey())) {
                        mImageView_fav.setImageResource(R.drawable.heart_fillcolor);
                        isFav = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ImageViewAnimatedChange(Context c, final ImageView v, final int new_image) {
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
}
