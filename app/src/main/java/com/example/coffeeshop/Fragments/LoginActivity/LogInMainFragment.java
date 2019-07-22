package com.example.coffeeshop.Fragments.LoginActivity;


import android.os.Build;
import android.os.Bundle;
import android.support.transition.TransitionInflater;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.coffeeshop.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogInMainFragment extends Fragment {

    Button mButton_SignIn, mButton_SignUp;
    ImageView mImageView;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_log_in_main, container, false);
        mButton_SignIn = rootView.findViewById(R.id.btn_sign_in);
        mButton_SignUp = rootView.findViewById(R.id.btn_sign_up);
        mImageView = rootView.findViewById(R.id.image);


        mButton_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(mImageView, "image", new SignInFragment());
            }
        });
        mButton_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(mImageView, "image", new SignUpFragment());
            }
        });

        return rootView;
    }

    private void changeFragment(ImageView imageView, String elementName, Fragment fragment) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));
            setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.explode));

            // Create new fragment to add (Fragment B)
            fragment.setSharedElementEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));
            fragment.setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.explode));

            // Our shared element (in Fragment A)

            // Add Fragment B
            FragmentTransaction ft = getFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack("transaction")
                    .addSharedElement(imageView, "MyTransition");
            ft.commit();
        } else {
            // Code to run on older devices
        }
    }


}
