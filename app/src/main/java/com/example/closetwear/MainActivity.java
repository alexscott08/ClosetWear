package com.example.closetwear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.closetwear.fragments.ComposeFragment;
import com.example.closetwear.fragments.HomeFragment;
import com.example.closetwear.fragments.OutfitsFragment;
import com.example.closetwear.fragments.ProfileFragment;
import com.example.closetwear.fragments.SearchViewFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseQuery;
import com.paulrybitskyi.persistentsearchview.PersistentSearchView;
import com.paulrybitskyi.persistentsearchview.utils.VoiceRecognitionDelegate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private PersistentSearchView persistentSearchView;
    private SearchViewFragment searchViewFragment;
    private List<OutfitPost> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//         Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        // Display icon in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.drawable.nav_logo_whiteout);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        searchResults = new ArrayList<>();
        persistentSearchView = findViewById(R.id.persistentSearchView);
        createSearchView();
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            Fragment fragment;
            switch (menuItem.getItemId()) {
                case R.id.action_home:
                    fragment = new HomeFragment();
                    persistentSearchView.setVisibility(View.VISIBLE);
                    break;
                case R.id.action_compose:
                    fragment = new ComposeFragment();
                    persistentSearchView.setVisibility(View.GONE);
                    break;
                case R.id.action_outfits:
                    fragment = new OutfitsFragment();
                    persistentSearchView.setVisibility(View.GONE);
                    break;
                case R.id.action_profile:
                default:
                    fragment = new ProfileFragment();
                    persistentSearchView.setVisibility(View.GONE);
                    break;
            }
            fragmentManager.beginTransaction().replace(R.id.containerFrameLayout, fragment).commit();
            return true;
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    private void createSearchView() {
        persistentSearchView.setOnLeftBtnClickListener(view -> {
            Fragment fragment = new HomeFragment();
            persistentSearchView.setVisibility(View.VISIBLE);
            fragmentManager.beginTransaction().replace(R.id.containerFrameLayout, fragment).commit();
        });

        persistentSearchView.setOnClearInputBtnClickListener(view -> {
            // Handle the clear input button click
        });

        // Setting a delegate for the voice recognition input
        persistentSearchView.setVoiceRecognitionDelegate(new VoiceRecognitionDelegate(this));

        persistentSearchView.setOnSearchConfirmedListener((searchView, query) -> {
            querySearch(query);
            persistentSearchView.collapse();
            searchViewFragment = new SearchViewFragment(new ArrayList<>());
            persistentSearchView.setVisibility(View.GONE);
            fragmentManager.beginTransaction().replace(R.id.containerFrameLayout, searchViewFragment).commit();
        });

        // Disabling the suggestions since they are unused in
        // the simple implementation
        persistentSearchView.setSuggestionsDisabled(true);
    }

    private void querySearch(String query) {
        ParseQuery<OutfitPost> parseQuery = ParseQuery.getQuery(OutfitPost.class);
        parseQuery.include(OutfitPost.KEY_USER);
        parseQuery.whereContains(OutfitPost.KEY_USER, query);
        parseQuery.findInBackground((fits, e) -> {
            if (e != null) {
                Log.e(TAG, "Problem  with querying fits: ", e);
                return;
            }
            for (OutfitPost fit : fits) {
                Log.i(TAG,
                        "User: " + fit.getUser());
            }
//            searchResults.clear();

            searchViewFragment.addToAdapter(fits);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Calling the voice recognition delegate to properly handle voice input results
        VoiceRecognitionDelegate.handleResult(persistentSearchView, requestCode, resultCode, data);
    }
}