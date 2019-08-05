package com.example.coffeeshop.Activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeshop.Controllers.Utils;
import com.example.coffeeshop.Fragments.CartsFragment;
import com.example.coffeeshop.Fragments.FavouritesFragment;
import com.example.coffeeshop.Fragments.MenuFragment;
import com.example.coffeeshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    TextView mTextView_logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Toast.makeText(this, user.getDisplayName(), Toast.LENGTH_SHORT).show();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        mTextView_logOut = findViewById(R.id.txt_logout);
        mTextView_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    FirebaseAuth.getInstance().signOut();
                    Utils.changeActivityAndFinish(DashboardActivity.this, LoginActivity.class, true);
                }
            }
        });

        //   Utils.changeFragment(DashboardActivity.this, new MenuFragment());

        setDrawer();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
            // Handle the camera action
        } else if (id == R.id.nav_menu) {
            Utils.changeFragment(DashboardActivity.this, new MenuFragment(), false);
        } else if (id == R.id.nav_favourites) {
            Utils.changeFragment(DashboardActivity.this, new FavouritesFragment(), false);
        } else if (id == R.id.nav_cart) {
            Utils.changeFragment(DashboardActivity.this, new CartsFragment(), false);

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_setting) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(true);
        Utils.changeFragment(this, new MenuFragment(), false);
    }

}
