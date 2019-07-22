package com.example.coffeeshop.Fragments.LoginActivity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.coffeeshop.R;

public class SignInFragment extends Fragment {

    View rootView;
    ImageView mImageView_top;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        return rootView;
    }

}
