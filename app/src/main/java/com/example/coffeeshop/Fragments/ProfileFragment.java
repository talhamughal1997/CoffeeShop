package com.example.coffeeshop.Fragments;


import android.app.Dialog;
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

import com.example.coffeeshop.Controllers.Utils;
import com.example.coffeeshop.Models.UserModel;
import com.example.coffeeshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    View rootView;

    EditText mEditText_name, mEditText_email, mEditText_pswd, mEditText_phone, mEditText_adress;
    Button mButton_save;
    Dialog progressDialog;
    String old_email, old_pswd, name, email, phone, pswd, adress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        Utils.changeTitle(getActivity(), "Profile", true);
        viewInit();
        getProfile();

        mButton_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mEditText_name.getText().toString();
                email = mEditText_email.getText().toString();
                phone = mEditText_phone.getText().toString();
                adress = mEditText_adress.getText().toString();
                pswd = mEditText_pswd.getText().toString();
                if (!validateForm()) {
                    return;
                }
                UserModel model = new UserModel(name, email, pswd, phone, adress);
                setProfile(model);
            }
        });
        return rootView;
    }

    private void viewInit() {
        mEditText_adress = rootView.findViewById(R.id.edt_adress);
        mEditText_phone = rootView.findViewById(R.id.edt_phone);
        mEditText_name = rootView.findViewById(R.id.edt_name);
        mEditText_email = rootView.findViewById(R.id.edt_email);
        mEditText_pswd = rootView.findViewById(R.id.edt_pswd);
        mButton_save = rootView.findViewById(R.id.btn_save);
        progressDialog = Utils.getProgressDialog(getActivity());
    }

    private void getProfile() {
        progressDialog.show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Utils.AUTH_UID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel model = dataSnapshot.getValue(UserModel.class);
                mEditText_adress.setText(model.getAddress());
                mEditText_name.setText(model.getName());
                mEditText_phone.setText(model.getPhone());
                mEditText_email.setText(model.getEmail());
                old_email = model.getEmail();
                old_pswd = model.getPassword();
                progressDialog.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.hide();
            }
        });
    }

    private void setProfile(final UserModel model) {
        progressDialog.show();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(old_email, old_pswd);

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updateEmail(model.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(model.getPassword()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Utils.AUTH_UID);
                                                    ref.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                                                progressDialog.hide();
                                                            } else {
                                                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                progressDialog.hide();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    progressDialog.hide();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        progressDialog.hide();
                                    }
                                }
                            });
                        }
                    }
                });
    }


    private boolean validateForm() {
        boolean valid = true;

        if (TextUtils.isEmpty(name)) {
            mEditText_name.setError("Required.");
            valid = false;
        } else {
            mEditText_name.setError(null);
        }

        if (TextUtils.isEmpty(phone)) {
            mEditText_phone.setError("Required.");
            valid = false;
        } else {
            mEditText_phone.setError(null);
        }

        if (TextUtils.isEmpty(adress)) {
            mEditText_adress.setError("Required.");
            valid = false;
        } else {
            mEditText_adress.setError(null);
        }

        if (TextUtils.isEmpty(email)) {
            mEditText_email.setError("Required.");
            valid = false;
        } else {
            mEditText_email.setError(null);
        }
        if (TextUtils.isEmpty(pswd)) {
            mEditText_pswd.setError("Required.");
            valid = false;
        } else {
            mEditText_pswd.setError(null);
        }

        return valid;
    }


}
