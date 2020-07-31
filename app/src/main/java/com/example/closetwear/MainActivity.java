package com.example.closetwear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;

import com.example.closetwear.fragments.*;
import com.example.closetwear.parse.*;
import com.example.closetwear.search.SearchQuery;
import com.example.closetwear.search.SearchViewFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.paulrybitskyi.persistentsearchview.PersistentSearchView;
import com.paulrybitskyi.persistentsearchview.utils.VoiceRecognitionDelegate;

import java.util.*;

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
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        searchResults = new ArrayList<>();
        persistentSearchView = findViewById(R.id.persistentSearchView);
        persistentSearchView.setLeftButtonDrawable(R.drawable.ic_search);
        createSearchView();
        // Handles all actions done on bottom navigation menu
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
        searchViewFragment = new SearchViewFragment(searchResults);
        persistentSearchView.setLeftButtonDrawable(R.drawable.ic_search);
        persistentSearchView.setOnLeftBtnClickListener(view -> {
            Navigation.goMainActivity(this);
        });

        persistentSearchView.setOnClearInputBtnClickListener(view -> {
            // Handle the clear input button click
            Navigation.goMainActivity(this);
        });

        // Setting a delegate for the voice recognition input
        persistentSearchView.setVoiceRecognitionDelegate(new VoiceRecognitionDelegate(this));

        persistentSearchView.setOnSearchConfirmedListener((searchView, query) -> {
            SearchQuery searchQuery = new SearchQuery(query, searchViewFragment);
            // To be used in case of filter search
            searchViewFragment.setQuery(query);
            persistentSearchView.collapse(true);

            persistentSearchView.setLeftButtonDrawable(R.drawable.ic_left_arrow);
            fragmentManager.beginTransaction().replace(R.id.containerFrameLayout, searchViewFragment).commit();
        });
    }


}
