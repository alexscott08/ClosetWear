package com.example.closetwear.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.closetwear.GlideApp;
import com.example.closetwear.LoginActivity;
import com.example.closetwear.R;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {


    public static final String TAG = "ProfileFragment";
    private Button logoutBtn;
    private TextView name;
    private TextView username;
    private ImageView profileImg;
    private ImageView closetIcon;
    private ImageView fitsIcon;
    private ImageView favoritesIcon;
    private ParseUser user = ParseUser.getCurrentUser();

    public ProfileFragment() {
        // Required empty public constructor
    }
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getChildFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
//            @Override
//            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
//                // We use a String here, but any type that can be put in a Bundle is supported
//                String result = bundle.getString("bundleKey");
//                // Do something with the result...
//            }
//        });
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = view.findViewById(R.id.name);
        username = view.findViewById(R.id.username);
        profileImg = view.findViewById(R.id.profileImg);
        closetIcon = view.findViewById(R.id.closetIcon);
        fitsIcon = view.findViewById(R.id.fitsIcon);
        favoritesIcon = view.findViewById(R.id.favoritesIcon);

        // Upload profile pic from server to fill view
        ParseFile img = user.getParseFile("profilePic");
//        GlideApp.with(view.getContext()).load(img.getUrl()).into(profileImg);
        GlideApp.with(this)
                .load(img.getUrl())
                .transform(new CircleCrop())
                .into(profileImg);
        name.setText(user.getString("name"));
        username.setText("@" + user.getUsername());
        logoutBtn = view.findViewById(R.id.logoutBtn);

        // listener to log out user and start LoginActivity()
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutUser();
            }
        });

        // Click icons to transition to new fragments
        final FragmentManager fragmentManager = getParentFragmentManager();
        closetIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ClosetFragment();
                fragmentManager.beginTransaction().replace(R.id.containerFrameLayout, fragment).addToBackStack(null).commit();
            }
        });

        fitsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new OutfitsFragment();
                fragmentManager.beginTransaction().replace(R.id.containerFrameLayout, fragment).addToBackStack(null).commit();
            }
        });

        favoritesIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new OutfitsFragment();
                fragmentManager.beginTransaction().replace(R.id.containerFrameLayout, fragment).addToBackStack(null).commit();
            }
        });

    }

    // Logs out the current user and starts LoginActivity()
    private void logOutUser() {
        Log.i(TAG, "Attempting to signout user");
        // Navigates to login activity if logout is successful
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with login", e);
                    Toast.makeText(getContext(), "Issue with logout!", Toast.LENGTH_SHORT).show();
                    return;
                }
                goLoginActivity();
                Toast.makeText(getContext(), "You have successfully logged out!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper for logOutUser() to start LoginActivity()
    private void goLoginActivity() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        getContext().startActivity(intent);
    }

}