package com.example.coffeeshop.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.coffeeshop.Adapters.MenuAdapter;
import com.example.coffeeshop.Adapters.MenuDetailAdapter;
import com.example.coffeeshop.Models.CoffeeModel;
import com.example.coffeeshop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference ref;
    ArrayList<CoffeeModel> cofeeArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_details);


        ref = FirebaseDatabase.getInstance().getReference().child("Menu").child("Coffee");
        setDataintoArray();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

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
        recyclerView = findViewById(R.id.menu_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new MenuDetailAdapter(this, cofeeArray));
    }


}
