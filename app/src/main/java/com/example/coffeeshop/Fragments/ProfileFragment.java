package com.example.coffeeshop.Fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.example.coffeeshop.Controllers.Utils;
import com.example.coffeeshop.Models.UserModel;
import com.example.coffeeshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    View rootView;

    EditText mEditText_name, mEditText_email, mEditText_pswd, mEditText_phone, mEditText_adress;
    CircleImageView mImageView_profile;
    Button mButton_save;
    Dialog progressDialog;
    String old_email, old_pswd, name, email, phone, pswd, adress;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 007;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        Utils.changeTitle(getActivity(), "Profile", true);
        viewInit();
        getProfile();
        getImageFromFirebase();

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
                uploadImage();
            }
        });

        mImageView_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
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
        mImageView_profile = rootView.findViewById(R.id.img_profile);
        progressDialog = Utils.getProgressDialog(getActivity());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
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

        AuthCredential credential = EmailAuthProvider
                .getCredential(old_email, old_pswd);

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
                                                                Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();
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

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                mImageView_profile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child("images/" + Utils.AUTH_UID);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Updated SuccessFully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    private void getImageFromFirebase() {
        progressDialog.show();
        storageReference.child("images/").child(Utils.AUTH_UID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
             //  Glide.with(getActivity()).load(uri).placeholder(R.drawable.ic_add_a_photo).into(new BitmapImageViewTarget(mImageView_profile);
                Glide.with(getActivity())
                        .load(uri)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressDialog.hide();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressDialog.hide();
                                return false;

                            }
                        }).into(mImageView_profile);
            }
        });
    }
}
