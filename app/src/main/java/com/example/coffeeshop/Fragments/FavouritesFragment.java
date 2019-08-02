package com.example.coffeeshop.Fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coffeeshop.Adapters.FavouritesAdapter;
import com.example.coffeeshop.Controllers.SwipeToDeleteCarts;
import com.example.coffeeshop.Controllers.SwipeToDeleteFavourites;
import com.example.coffeeshop.Controllers.Utils;
import com.example.coffeeshop.Models.MenuItemModel;
import com.example.coffeeshop.Models.UserCartModel;
import com.example.coffeeshop.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends Fragment {


    View rootView;
    FavouritesAdapter adapter;
    DatabaseReference reference;
    private Dialog progressDialog;

    public FavouritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_favourites, container, false);
        progressDialog = Utils.getProgressDialog(getActivity());
        Utils.changeTitle(getActivity(), "Favourites", true);
        getData();
        return rootView;
    }

    private void setRecyclerView(ArrayList<MenuItemModel> arrayList) {
        RecyclerView recyclerView = rootView.findViewById(R.id.fav_recyclerview);
        adapter = new FavouritesAdapter(arrayList, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteFavourites(adapter, getActivity()));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void getData() {


    }


}

