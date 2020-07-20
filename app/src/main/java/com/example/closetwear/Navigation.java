package com.example.closetwear;

import android.app.Activity;
import android.content.Intent;

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
}
