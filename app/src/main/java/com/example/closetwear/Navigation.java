package com.example.closetwear;

import android.app.Activity;
import android.content.Intent;

public class Navigation {

    /**
     * Once new user account has been created, navigates to login activity which will automatically
     * go to main activity
     */
    protected static void goLoginActivity(Activity activity) {
        Intent i = new Intent(activity, MainActivity.class);
        activity.startActivity(i);
        activity.finish();
    }

    /**
     * If signup button is pressed, navigates to signup activity to create new user account
     */
    protected static void goSignupActivity(Activity activity) {
        // Navigates to signup activity to create new account
        Intent i = new Intent(activity, SignupActivity.class);
        activity.startActivity(i);
        activity.finish();
    }

    /**
     * After login is completed, navigates to main activity
     */
    protected static void goMainActivity(Activity activity) {
        Intent i = new Intent(activity, MainActivity.class);
        activity.startActivity(i);
        activity.finish();
    }
}
