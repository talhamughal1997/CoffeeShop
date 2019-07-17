package com.example.coffeeshop.Activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.coffeeshop.Controllers.Utils;
import com.example.coffeeshop.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (getSharedPref())
//                    Utils.changeActivityAndFinish(SplashActivity.this, DashboardActivity.class, true);
//                else
//                    Utils.changeActivityAndFinish(SplashActivity.this, MainActivity.class,true);
                Utils.changeActivity(SplashActivity.this,LoginActivity.class);
            }
        }, 1500);

    }

    private boolean getSharedPref() {
        return getSharedPreferences(Utils.DISCOVER, Context.MODE_PRIVATE).getBoolean("shown", false);
    }
}
