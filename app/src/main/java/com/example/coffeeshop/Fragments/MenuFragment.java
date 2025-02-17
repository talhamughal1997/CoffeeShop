package com.example.coffeeshop.Fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coffeeshop.Adapters.MenuAdapter;
import com.example.coffeeshop.Controllers.Utils;
import com.example.coffeeshop.Models.MenuModel;
import com.example.coffeeshop.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    View rootView;
    RecyclerView recyclerView;
    DatabaseReference ref;
    ArrayList<MenuModel> cofeeArray;
    Dialog progressDialog;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        Utils.changeTitle(getActivity(),"MENU",true);
        progressDialog = Utils.getProgressDialog(getActivity());

        setDataintoArray();

        return rootView;
    }



        private void setDataintoArray() {
        progressDialog.show();
        cofeeArray = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference().child("Menu");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    cofeeArray.add(ds.child("define").getValue(MenuModel.class));
                }
                setRecyclerView(cofeeArray);
                progressDialog.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.hide();
            }
        });
    }

    private void setRecyclerView(ArrayList<MenuModel> cofeeArray) {
        recyclerView = rootView.findViewById(R.id.menu_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(new MenuAdapter((AppCompatActivity) getActivity(), cofeeArray));
    }

}
