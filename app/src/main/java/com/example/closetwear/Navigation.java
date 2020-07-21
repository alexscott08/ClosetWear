package com.example.closetwear;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.fragment.app.FragmentActivity;

import org.parceler.Parcels;

import java.io.File;

public class Navigation {

    /**
     * Once new user account has been created or user is logged out, navigates to login activity
     * which will automatically
     * go to main activity
     */
    public static void goLoginActivity(Activity activity) {
        Intent i = new Intent(activity, LoginActivity.class);
        activity.startActivity(i);
        activity.finish();
    }

    /**
     * If signup button is pressed, navigates to signup activity to create new user account
     */
    public static void goSignupActivity(Activity activity) {
        // Navigates to signup activity to create new account
        Intent i = new Intent(activity, SignupActivity.class);
        activity.startActivity(i);
        activity.finish();
    }

    /**
     * After login is completed, navigates to main activity
     */
    public static void goMainActivity(Activity activity) {
        Intent i = new Intent(activity, MainActivity.class);
        activity.startActivity(i);
        activity.finish();
    }

    public static void goNewItemActivity(Activity activity, File image) {
        Intent i = new Intent(activity, NewItemActivity.class);
        i.putExtra("image", image);
        activity.startActivity(i);
    }

    public static void goNewOutfitActivity(Activity activity, File image) {
        Intent i = new Intent(activity, NewOutfitActivity.class);
        i.putExtra("image", image);
        activity.startActivity(i);
    }
}
