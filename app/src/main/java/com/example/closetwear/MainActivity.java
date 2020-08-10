package com.example.closetwear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;

import com.example.closetwear.fragments.*;
import com.example.closetwear.parse.*;
import com.example.closetwear.profile.ProfileFragment;
import com.example.closetwear.search.SearchQuery;
import com.example.closetwear.search.SearchViewFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.paulrybitskyi.persistentsearchview.PersistentSearchView;
import com.paulrybitskyi.persistentsearchview.utils.VoiceRecognitionDelegate;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    final private FragmentManager fragmentManager = getSupportFragmentManager();
    private PersistentSearchView persistentSearchView;
    private SearchViewFragment searchViewFragment;
    private List<OutfitPost> searchResults;
    private Set<String> clothingIdSet;
    private Set<String> fitIdSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                case R.id.action_profile:
                default:
                    fragment = new ProfileFragment();
                    persistentSearchView.setVisibility(View.GONE);
                    break;
            }
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_left,
                    R.anim.slide_out_right).replace(R.id.containerFrameLayout, fragment).commit();
            return true;
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    private void createSearchView() {
        searchViewFragment = new SearchViewFragment(searchResults);
        persistentSearchView.setLeftButtonDrawable(R.drawable.ic_search);

        // PersistentSearchView Listeners
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
            query = query.toLowerCase();

            // To be used in case of filter search
            searchViewFragment.setQuery(query);

            // 1. Find all ClothingPosts that are tagged to a fit
            clothingIdSet = SearchQuery.querySearch();
            searchViewFragment.setItemIds(clothingIdSet);

            // 2. Find the ids of all OutfitPosts that the items of clothingIdSet are tagged to
            fitIdSet = SearchQuery.queryItem(clothingIdSet, query);

            // 3. Use the set of IDs to find the OutfitPost instances and add to SearchViewAdapter
            SearchQuery.queryFits(fitIdSet, searchViewFragment);

            persistentSearchView.collapse(true);

            // Change drawable and switch view to fragment
            persistentSearchView.setLeftButtonDrawable(R.drawable.ic_left_arrow);
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_left,
                    R.anim.slide_out_right).replace(R.id.containerFrameLayout, searchViewFragment).commit();
        });
    }


}
