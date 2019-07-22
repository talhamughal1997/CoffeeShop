package com.example.coffeeshop.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.coffeeshop.Controllers.Utils;
import com.example.coffeeshop.Fragments.LoginActivity.LogInMainFragment;
import com.example.coffeeshop.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Utils.changeFragment(this, new LogInMainFragment(), false);
    }
}
