package com.example.closetwear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.closetwear.fragments.ComposeFragment;
import com.example.closetwear.fragments.HomeFragment;
import com.example.closetwear.fragments.OutfitsFragment;
import com.example.closetwear.fragments.ProfileFragment;
import com.example.closetwear.search.SearchViewFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseQuery;
import com.paulrybitskyi.persistentsearchview.PersistentSearchView;
import com.paulrybitskyi.persistentsearchview.utils.VoiceRecognitionDelegate;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private PersistentSearchView persistentSearchView;
    private SearchViewFragment searchViewFragment;
    private List<OutfitPost> searchResults;
    private Set<ClothingPost> clothingSet = new HashSet<>();
    private Set<String> fitIdSet = new HashSet<>();
    private Set<String> clothingIdSet = new HashSet<>();

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
        persistentSearchView.setLeftButtonDrawable(R.drawable.ic_search);
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
        searchViewFragment = new SearchViewFragment(searchResults);
        persistentSearchView.setLeftButtonDrawable(R.drawable.ic_search);
        persistentSearchView.setOnLeftBtnClickListener(view -> {
            Fragment fragment = new HomeFragment();
            persistentSearchView.setVisibility(View.VISIBLE);
            fragmentManager.beginTransaction().replace(R.id.containerFrameLayout, fragment).commit();
        });

        persistentSearchView.setOnClearInputBtnClickListener(view -> {
            // Handle the clear input button click
            searchViewFragment.queryPosts();
        });

        // Setting a delegate for the voice recognition input
        persistentSearchView.setVoiceRecognitionDelegate(new VoiceRecognitionDelegate(this));

        persistentSearchView.setOnSearchConfirmedListener((searchView, query) -> {

            querySearch(query);
            persistentSearchView.collapse(true);

            persistentSearchView.setLeftButtonDrawable(R.drawable.ic_left_arrow);
            fragmentManager.beginTransaction().replace(R.id.containerFrameLayout, searchViewFragment).commit();
        });

        // Disabling the suggestions since they are unused in
        // the simple implementation
//        persistentSearchView.setSuggestionsDisabled(true);
    }

    private void querySearch(String query) {
        ParseQuery<OutfitPost> parseQuery = ParseQuery.getQuery(OutfitPost.class);
        parseQuery.include(OutfitPost.KEY_USER);
        parseQuery.findInBackground((fits, e) -> {
            if (e != null) {
                Log.e(TAG, "Problem  with querying users: ", e);
                return;
            }
            for (OutfitPost fit : fits) {
                JSONArray fitItems = fit.getFitItems();
                if (fitItems != null) {
                    for (int i = 0; i < fitItems.length(); i++) {
                        try {
                            clothingIdSet.add(fitItems.getString(i));
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
            queryItem(clothingIdSet, query.toLowerCase());
        });
    }

    // Gets all clothing posts that correlate to the set of itemIds
    private void queryItem(Set<String> itemIds, String query) {
        ParseQuery<ClothingPost> parseQuery = ParseQuery.getQuery(ClothingPost.class);
        parseQuery.whereContainedIn("objectId", itemIds);
        parseQuery.findInBackground((items, e) -> {
            if (e != null) {
                Log.e(TAG, "Problem  with querying item", e);
                return;
            } else {
                // finds all fits the items is in for each ClothingPost instance and adds to clothingIdSet, convert from JsonObject
                for (ClothingPost item : items) {
                    JSONArray itemFits = item.getFit();
                    if (itemFits != null) {
                        for (int i = 0; i < itemFits.length(); i++) {
                            // If a field of item contains the query, add to list
                            if (item.getName().toLowerCase().contains(query) ||
                                    item.getBrand().toLowerCase().contains(query) ||
                                    item.getColor().toLowerCase().contains(query) ||
                                    item.getCategory().toLowerCase().contains(query) ||
                                    item.getSubcategory().toLowerCase().contains(query)) {
                                try {
                                    fitIdSet.add(itemFits.getString(i));
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                }
                queryFits(fitIdSet);
            }
        });
    }

    private void queryFits(Set<String> fitIds) {
        ParseQuery<OutfitPost> query = ParseQuery.getQuery(OutfitPost.class);
        query.whereContainedIn("objectId", fitIds);
        query.findInBackground((fits, e) -> {
            if (e != null) {
                Log.e(TAG, "Problem  with querying item", e);
                return;
            } else {
                for (OutfitPost fit : fits) {
                    Log.i(TAG, "Fit: " + fit.getObjectId());
                }
                searchViewFragment.addToAdapter(fits);
            }
        });
    }
}
