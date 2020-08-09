package com.example.closetwear;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.closetwear.newitem.NewItemActivity;
import com.example.closetwear.newoutfit.*;
import com.example.closetwear.parse.*;
import com.example.closetwear.profile.TaggedActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.*;
import org.parceler.Parcels;

import java.io.File;
import java.util.*;

public class Navigation {

    public static final String TAG = "Navigation";
    /**
     * Once new user account has been created or user is logged out, navigates to login activity
     * which will automatically
     * go to main activity
     */
    public static void goLoginActivity(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
        activity.finish();
    }

    /**
     * If signup button is pressed, navigates to signup activity to create new user account
     */
    public static void goSignupActivity(Activity activity) {
        // Navigates to signup activity to create new account
        activity.startActivity(new Intent(activity, SignupActivity.class));
        activity.finish();
    }

    /**
     * Overloaded method if Google signup button is pressed, navigates to signup activity to
     * create new user account using information from their Google profile
     */
    public static void goSignupActivity(Activity activity, GoogleSignInAccount account) {
        Intent i = new Intent(activity, SignupActivity.class);
        i.putExtra("Google Account", Parcels.wrap(account));
        activity.startActivity(i);
        activity.finish();
    }

    /**
     * After login is completed, navigates to main activity
     */
    public static void goMainActivity(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }

    public static void goOutfitDetailsActivity(Context context, OutfitPost outfitPost) {
        Intent intent = new Intent(context, OutfitDetailsActivity.class);
        intent.putExtra("post", Parcels.wrap(outfitPost));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void goNewItemActivity(Activity activity, File image) {
        Intent i = new Intent(activity, NewItemActivity.class);
        i.putExtra("image", image);
        activity.startActivity(i);
    }

    public static void goNewOutfitActivity(Activity activity, OutfitPost outfitPost) {
        Intent i = new Intent(activity, NewOutfitActivity.class);
        i.putExtra("outfit", Parcels.wrap(outfitPost));
        activity.startActivity(i);
    }

    // Overloaded method to navigate from TagItemActivity back to NewOutfitActivity
    public static void goNewOutfitActivity(Context context, ClothingPost clothingPost, OutfitPost outfitPost) {
        Intent i = new Intent(context, NewOutfitActivity.class);
        i.putExtra("post", Parcels.wrap(clothingPost));
        i.putExtra("outfit",  Parcels.wrap(outfitPost));
        context.startActivity(i);
    }

    public static void goTagItemActivity(Activity activity, OutfitPost outfitPost) {
        Intent i = new Intent(activity, TagItemActivity.class);
        i.putExtra("outfit",  Parcels.wrap(outfitPost));
        activity.startActivity(i);
        activity.finish();
    }

    public static void goTaggedActivity(Context context, JSONArray fitsArray) {
        ArrayList<String> fitIds = new ArrayList<>();
        if (fitsArray != null) {
            for (int i = 0; i < fitsArray.length(); i++) {
                try {
                    fitIds.add(fitsArray.getString(i));
                } catch (JSONException e) {
                    Log.e(TAG, "JsonException pulling out fitIds: " + e);
                }
            }
        }
        Intent i = new Intent(context, TaggedActivity.class);
        i.putExtra("fits", fitIds);
        context.startActivity(i);
    }

    public static void goFullOutfitActivity(Activity activity, OutfitPost outfitPost) {
        Intent i = new Intent(activity, FullOutfitActivity.class);
        i.putExtra("outfit", Parcels.wrap(outfitPost));
        activity.startActivity(i);
    }
}
