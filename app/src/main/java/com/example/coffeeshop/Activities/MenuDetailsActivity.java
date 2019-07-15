package com.example.coffeeshop.Activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.coffeeshop.Adapters.MenuDetailAdapter;
import com.example.coffeeshop.Controllers.Utils;
import com.example.coffeeshop.Models.MenuItemModel;
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
    ArrayList<MenuItemModel> cofeeArray;
    String menuId;
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_details);
        menuId = getIntent().getStringExtra("menuId");
        setToolbar();
        progressDialog = Utils.getProgressDialog(this);

        ref = FirebaseDatabase.getInstance().getReference().child("Menu").child(menuId).child("items");
        setDataintoArray();

    }

    private void setDataintoArray() {
        progressDialog.show();
        cofeeArray = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    cofeeArray.add(ds.getValue(MenuItemModel.class));
                }
                setRecyclerView(cofeeArray);
                progressDialog.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setRecyclerView(ArrayList<MenuItemModel> cofeeArray) {
        recyclerView = findViewById(R.id.menudetail_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new MenuDetailAdapter(this, cofeeArray));
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        final TextView title = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        ref = FirebaseDatabase.getInstance().getReference().child("Menu").child(menuId).child("define");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                title.setText(dataSnapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
