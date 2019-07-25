package com.example.coffeeshop.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.coffeeshop.Adapters.CartsAdapter;
import com.example.coffeeshop.Controllers.Utils;
import com.example.coffeeshop.Models.UserCartModel;
import com.example.coffeeshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartsFragment extends Fragment {

    View rootView;

    public CartsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_carts, container, false);
        Utils.changeTitle(getActivity(),"CARTS",true);
        getData();
        return rootView;
    }

    private void setRecyclerView(ArrayList<UserCartModel> arrayList) {
        RecyclerView recyclerView = rootView.findViewById(R.id.cart_recyclerview);
        recyclerView.setAdapter(new CartsAdapter(arrayList));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void getData() {
        final ArrayList<UserCartModel> arrayList = new ArrayList<>();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Carts").child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    arrayList.add(ds.getValue(UserCartModel.class));
                }
                setRecyclerView(arrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
