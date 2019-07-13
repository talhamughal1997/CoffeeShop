package com.example.coffeeshop.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coffeeshop.Adapters.MenuAdapter;
import com.example.coffeeshop.Models.CoffeeModel;
import com.example.coffeeshop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    View rootView;
    RecyclerView recyclerView;
    DatabaseReference ref;
    ArrayList<CoffeeModel> cofeeArray;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        ref = FirebaseDatabase.getInstance().getReference().child("Menu").child("CoffeeItems");

        setDataintoArray();


        return rootView;
    }

    private void setDataintoArray() {
        cofeeArray = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    cofeeArray.add(ds.getValue(CoffeeModel.class));
                }
                setRecyclerView(cofeeArray);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setRecyclerView(ArrayList<CoffeeModel> cofeeArray) {
        recyclerView = rootView.findViewById(R.id.menu_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(new MenuAdapter((AppCompatActivity) getActivity(), cofeeArray));
    }

}
