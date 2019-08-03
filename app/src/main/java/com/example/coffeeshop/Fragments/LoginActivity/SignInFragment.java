package com.example.coffeeshop.Fragments.LoginActivity;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coffeeshop.Activities.DashboardActivity;
import com.example.coffeeshop.Controllers.Utils;
import com.example.coffeeshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import static android.support.constraint.Constraints.TAG;

public class SignInFragment extends Fragment {

    View rootView;
    EditText mEditText_email, mEditText_password;
    Button mButton_sigIn;
    Dialog progressDialog;

    private FirebaseAuth mAuth;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mAuth = FirebaseAuth.getInstance();
        mEditText_email = rootView.findViewById(R.id.edittext_name);
        mEditText_password = rootView.findViewById(R.id.edittext_number);
        mButton_sigIn = rootView.findViewById(R.id.btn_signIn);
        progressDialog = Utils.getProgressDialog(getActivity());

        mButton_sigIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSignIn(mEditText_email.getText().toString(), mEditText_password.getText().toString());
            }
        });

        return rootView;
    }

    private void setSignIn(String email, String password) {

        if (!validateForm()) {
            return;
        }
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.hide();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            progressDialog.hide();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            getExceptions(task);
                        }
                    }
                });

    }

    private void updateUI() {
        Utils.changeActivityAndFinish(getActivity(), DashboardActivity.class);
    }

    @Override
    public void onStart() {
        super.onStart();
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

        return valid;
    }

    private void getExceptions(Task<AuthResult> task) {

        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

        switch (errorCode) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(getActivity(), "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(getActivity(), "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(getActivity(), "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(getActivity(), "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                mEditText_email.setError("The email address is badly formatted.");
                mEditText_email.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(getActivity(), "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                mEditText_password.setError("password is incorrect ");
                mEditText_password.requestFocus();
                mEditText_password.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(getActivity(), "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(getActivity(), "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(getActivity(), "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(getActivity(), "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                mEditText_email.setError("The email address is already in use by another account.");
                mEditText_email.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(getActivity(), "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(getActivity(), "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(getActivity(), "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(getActivity(), "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(getActivity(), "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(getActivity(), "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(getActivity(), "The given password is invalid.", Toast.LENGTH_LONG).show();
                mEditText_password.setError("The password is invalid it must 6 characters at least");
                mEditText_password.requestFocus();
                break;

        }
    }
}

