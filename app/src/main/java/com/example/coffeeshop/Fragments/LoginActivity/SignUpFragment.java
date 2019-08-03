package com.example.coffeeshop.Fragments.LoginActivity;


import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.transition.TransitionInflater;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.coffeeshop.Controllers.Utils;
import com.example.coffeeshop.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    EditText mEditText_email, mEditText_password, mEditText_cnfrm_password;
    Button mButton_continue;
    ImageView mImageView;
    Dialog progressDialog;
    View rootView;


    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        progressDialog = Utils.getProgressDialog(getActivity());
        mEditText_email = rootView.findViewById(R.id.edittext_name);
        mEditText_password = rootView.findViewById(R.id.edittext_number);
        mEditText_cnfrm_password = rootView.findViewById(R.id.edittext_address);
        mImageView = rootView.findViewById(R.id.image);
        mButton_continue = rootView.findViewById(R.id.btn_continue);

        mButton_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateForm()) {
                    return;
                }
                Fragment fragment = new UserDataFragment();
                Bundle bundle = new Bundle();
                bundle.putString("email", mEditText_email.getText().toString());
                bundle.putString("pswd", mEditText_password.getText().toString());
                fragment.setArguments(bundle);
                changeFragment(mImageView, "image", fragment);
            }
        });

        return rootView;
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEditText_email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEditText_email.setError("Required.");
            valid = false;
        } else {
            mEditText_email.setError(null);
        }

        String password = mEditText_password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mEditText_password.setError("Required.");
            valid = false;
        } else {
            mEditText_password.setError(null);
        }
        String cnfrm_password = mEditText_cnfrm_password.getText().toString();
        if (!password.equals(cnfrm_password)) {
            mEditText_cnfrm_password.setError("Confirm Password Not Matched");
            valid = false;
        } else {
            mEditText_cnfrm_password.setError(null);
        }

        return valid;
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
