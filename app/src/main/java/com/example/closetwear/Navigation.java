package com.example.closetwear;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.example.closetwear.newitem.NewItemActivity;
import com.example.closetwear.newoutfit.NewOutfitActivity;
import com.example.closetwear.newoutfit.TagItemActivity;
import com.example.closetwear.parse.ClothingPost;
import com.example.closetwear.parse.OutfitPost;

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

    public static void goOutfitDetailsActivity(Context context, OutfitPost outfitPost) {
        Intent intent = new Intent(context, OutfitDetailsActivity.class);
        intent.putExtra("post", Parcels.wrap(outfitPost));
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
}
