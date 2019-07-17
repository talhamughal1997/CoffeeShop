package com.example.coffeeshop.Controllers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeshop.R;

import java.lang.reflect.Field;


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


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Dialog getProgressDialog(Context context) {

        int llPadding = 30;
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setGravity(Gravity.CENTER);
        ll.setBackground(context.getDrawable(R.drawable.rounded_dialog));
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        ll.setLayoutParams(llParam);

        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, llPadding, 0);
        progressBar.setLayoutParams(llParam);

        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        TextView tvText = new TextView(context);
        tvText.setText("Loading ...");
        tvText.setTextColor(Color.parseColor("#000000"));
        tvText.setTextSize(20);
        tvText.setLayoutParams(llParam);

        ll.addView(progressBar);
        ll.addView(tvText);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setView(ll);

        AlertDialog dialog = builder.create();

        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        return dialog;
    }


    public static void setSpinnerHeight(Spinner spinner) {
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

            popupWindow.setHeight(500);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
    }

}
