package com.example.coffeeshop.Fragments.LoginActivity;


import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coffeeshop.Activities.DashboardActivity;
import com.example.coffeeshop.Controllers.Utils;
import com.example.coffeeshop.Models.UserModel;
import com.example.coffeeshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserDataFragment extends Fragment {

    EditText mEditText_name, mEditText_phone, mEditText_adres;
    Button mButton_signUp;
    Dialog progressDialog;
    View rootView;

    String email, pswd, name, phone, address;

    private FirebaseAuth mAuth;

    public UserDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_user_data, container, false);
        mAuth = FirebaseAuth.getInstance();
        viewInit();

        mButton_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataIntoDatabase(mAuth.getCurrentUser().getUid());
            }
        });

        return rootView;
    }

    private void viewInit() {
        progressDialog = Utils.getProgressDialog(getActivity());
        mEditText_name = rootView.findViewById(R.id.edittext_name);
        mEditText_phone = rootView.findViewById(R.id.edittext_number);
        mEditText_adres = rootView.findViewById(R.id.edittext_address);
        mButton_signUp = rootView.findViewById(R.id.btn_sign_up);

        email = getArguments().getString("email");
        pswd = getArguments().getString("pswd");
    }



    private boolean validateForm() {
        boolean valid = true;

        String email = mEditText_name.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEditText_name.setError("Required.");
            valid = false;
        } else {
            mEditText_name.setError(null);
        }

        String password = mEditText_phone.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mEditText_phone.setError("Required.");
            valid = false;
        } else {
            mEditText_phone.setError(null);
        }
        String address = mEditText_adres.getText().toString();
        if (TextUtils.isEmpty(address)) {
            mEditText_adres.setError("Required.");
            valid = false;
        } else {
            mEditText_adres.setError(null);
        }

        return valid;
    }

    private void saveDataIntoDatabase(String uid) {
        progressDialog.show();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        UserModel model = new UserModel(name, email, pswd, phone, address);
        reference.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(), "Your Account Successfully Created ", Toast.LENGTH_SHORT).show();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName("Jane Q. User")
                            .build();
                    user.updateProfile(profileUpdates);
                    Utils.changeActivityAndFinish(getActivity(), DashboardActivity.class, true);
                }
                else
                {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
                progressDialog.hide();
            }
        });





    }

}
