package com.example.coffeeshop.Controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.coffeeshop.R;


public class Utils {

    public static String DEVICE_ID;
    public static final String DISCOVER = "DISCOVER_PREF";

    public static void changeFragment(AppCompatActivity context, Fragment fragment) {

        FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void changeFragment_Act(AppCompatActivity context, Fragment fragment, int id) {

        FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
        transaction.replace(id, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void changeFragment(AppCompatActivity context, Fragment fragment, boolean AddToBackStack) {
        context.getSupportFragmentManager().popBackStack();
        FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    public static void changeFragment(Activity context, Fragment fragment, FragmentManager fm) {

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void changeFragmentAndRemoveParent(AppCompatActivity context, Fragment fragment) {

        FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    public static void changeActivity(Activity activity, Class scndActivity) {

        Intent intent = new Intent(activity, scndActivity);
        activity.startActivity(intent);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void changeActivityAndFinish(Activity activity, Class scndActivity) {

        Intent intent = new Intent(activity, scndActivity);
        activity.startActivity(intent);
        activity.finishAffinity();

    }

    public static void changeActivityAndFinish(Activity activity, Class scndActivity, boolean clearStacks) {

        Intent intent = new Intent(activity, scndActivity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);

    }


    public static void printToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void setSwipeRefreshLayout(Activity activity, SwipeRefreshLayout swipeRefreshLayout) {
        int c1 = activity.getResources().getColor(R.color.colorPrimary);
        int c2 = activity.getResources().getColor(R.color.colorPrimaryDark);
        int c3 = activity.getResources().getColor(R.color.colorPrimary);
        swipeRefreshLayout.setColorSchemeColors(c1, c2, c3);
    }

    public static void changeTitle(Activity activity, String title) {
        ((AppCompatActivity) activity).getSupportActionBar().setTitle(title);
    }

    public static void changeTitle(Activity activity, boolean isDashboard) {
        ((AppCompatActivity) activity).getSupportActionBar().setTitle("DashBoard");
    }


}
