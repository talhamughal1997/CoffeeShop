package com.example.coffeeshop.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.coffeeshop.Adapters.DiscoverAdapter;
import com.example.coffeeshop.Controllers.DepthPageTransformer;
import com.example.coffeeshop.Controllers.Utils;
import com.example.coffeeshop.R;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    TextView btn_GetStarted;
    private ViewPager mImageViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        btn_GetStarted = findViewById(R.id.txtGetStarted);
        mImageViewPager = findViewById(R.id.discover_pager);
        mImageViewPager.setPageTransformer(true, new DepthPageTransformer());
        mImageViewPager.setAdapter(new DiscoverAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = findViewById(R.id.discover_tablayout);
        tabLayout.setupWithViewPager(mImageViewPager, true);
        showButton();

        btn_GetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSharedPreferences();
                Utils.changeActivityAndFinish(MainActivity.this, LoginActivity.class, true);
            }
        });


    }

    private void showButton() {
        mImageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (mImageViewPager.getCurrentItem() == 2)
                    btn_GetStarted.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setSharedPreferences() {
        sharedpreferences = getSharedPreferences(Utils.DISCOVER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("shown", true);
        editor.commit();
    }
}
