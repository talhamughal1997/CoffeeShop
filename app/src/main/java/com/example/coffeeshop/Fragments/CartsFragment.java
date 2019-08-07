package com.example.coffeeshop.Fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeshop.Activities.DashboardActivity;
import com.example.coffeeshop.Adapters.CartsAdapter;
import com.example.coffeeshop.Controllers.SwipeToDeleteCarts;
import com.example.coffeeshop.Controllers.Utils;
import com.example.coffeeshop.Models.UserCartModel;
import com.example.coffeeshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartsFragment extends Fragment implements CartsAdapter.itemAddedListener {

    View rootView;
    TextView mTextView_Total, mTextView_netTotal;
    Button mButton_Checkout;
    DatabaseReference reference;
    Dialog progressDialog;
    CoordinatorLayout mlayout;
    BottomSheetBehavior behavior;

    CartsAdapter adapter;

    public CartsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_carts, container, false);
        Utils.changeTitle(getActivity(), "CARTS", true);
        mTextView_Total = rootView.findViewById(R.id.txt_total);
        mTextView_netTotal = rootView.findViewById(R.id.net_total);
        mButton_Checkout = rootView.findViewById(R.id.btn_checkout);
        mlayout = rootView.findViewById(R.id.coordinatorLayout);
        progressDialog = Utils.getProgressDialog(getActivity());
        initBottomSheet();
        getData();

        mButton_Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheckout();
            }
        });
        return rootView;
    }

    private void initBottomSheet(){
        View bottomSheet = mlayout.findViewById(R.id.img_profile);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setPeekHeight(0);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

    }

    private void setRecyclerView(ArrayList<UserCartModel> arrayList) {
        RecyclerView recyclerView = rootView.findViewById(R.id.cart_recyclerview);
        adapter = new CartsAdapter(arrayList, getActivity(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCarts(adapter, getActivity()));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                    {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    else {
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                    // Now I have to check if the user has scrolled down or up.

                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                /*if (dy < -30){
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else if (dy > 30){
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }*/
            }
        });
    }

    private void getData() {
        final ArrayList<UserCartModel> arrayList = new ArrayList<>();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference = FirebaseDatabase.getInstance().getReference().child("Carts").child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    arrayList.add(ds.getValue(UserCartModel.class));
                }
                setRecyclerView(arrayList);
                setPrice(arrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void setPrice(ArrayList<UserCartModel> arrayList) {
        long total = 0;

        for (int a = 0; a < arrayList.size(); a++) {
            total += arrayList.get(a).getPrice();
        }
        long net = total + ((total * 13) / 100);
        mTextView_Total.setText("$ " + total);
        mTextView_netTotal.setText("$ " + net);
    }

    private void setCheckout() {
        progressDialog.show();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Carts").child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount()>0) {

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String pushId = reference.push().getKey();

                    DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("Checkouts").child(uid).child(pushId).child("Date");
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    reference3.setValue(formatter.format(date));

                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("Checkouts").child(uid).child(pushId).child("Items");
                    reference2.setValue(dataSnapshot.getValue());

                    reference.removeValue();
                    Utils.changeActivityAndFinish(getActivity(), DashboardActivity.class);
                    Toast.makeText(getActivity(), "Order Placed SuccessFully", Toast.LENGTH_LONG).show();
                }
                progressDialog.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
