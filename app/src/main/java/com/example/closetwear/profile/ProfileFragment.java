package com.example.closetwear.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

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
import com.example.closetwear.Navigation;
import com.example.closetwear.R;
import com.example.closetwear.newitem.BrandFragment;
import com.example.closetwear.newitem.CategoryFragment;
import com.example.closetwear.newitem.ColorFragment;
import com.example.closetwear.newitem.NameFragment;
import com.example.closetwear.newitem.NewItemTabAdapter;
import com.example.closetwear.newitem.SubcategoryFragment;
import com.google.android.material.tabs.TabLayout;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {


    public static final String TAG = "ProfileFragment";
    private Button logoutBtn;
    private Button editProfileBtn;
    private TextView name;
    private TextView username;
    private ImageView profileImg;
    private ProfileTabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        bindData();
        adapter = new ProfileTabAdapter(getActivity().getSupportFragmentManager(),
                getLifecycle());
        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        setOnClickListener();

    }

    private void bindViews(View view) {
        name = view.findViewById(R.id.name);
        username = view.findViewById(R.id.username);
        profileImg = view.findViewById(R.id.profileImg);
        editProfileBtn = view.findViewById(R.id.editProfileBtn);
        logoutBtn = view.findViewById(R.id.logoutBtn);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.pager);
    }

    private void bindData() {
        ParseUser user = ParseUser.getCurrentUser();
        // Upload profile pic from server to fill view
        if (user != null) {
            ParseFile img = user.getParseFile("profilePic");
            GlideApp.with(this)
                    .load(img.getUrl()).placeholder(R.drawable.ic_profileicon)
                    .transform(new CircleCrop())
                    .into(profileImg);
            name.setText(user.getString("name"));
            username.setText("@" + user.getUsername());
        } else {
            GlideApp.with(this)
                    .load(R.drawable.ic_profileicon)
                    .transform(new CircleCrop())
                    .into(profileImg);
            name.setText("undefined");
            username.setText("undefined");
        }
    }

    private void setOnClickListener() {
        // listener to log out user and start LoginActivity()
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutUser();
            }
        });
        // TODO: editProfileBtn listener
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
                Navigation.goLoginActivity(getActivity());
                Toast.makeText(getContext(), "You have successfully logged out!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}