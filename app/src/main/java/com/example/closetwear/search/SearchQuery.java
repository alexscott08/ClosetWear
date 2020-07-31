package com.example.closetwear.search;

import android.util.Log;

import com.example.closetwear.parse.*;
import com.parse.ParseQuery;

import org.json.*;

import java.util.*;

public class SearchQuery {
    public static final String TAG = "SearchQuery";
    private String query;
    private Set<String> fitIdSet = new HashSet<>();
    private Set<String> clothingIdSet = new HashSet<>();
    private SearchViewFragment searchViewFragment;

    public SearchQuery(String query, SearchViewFragment searchViewFragment) {
        this.query = query.toLowerCase();
        this.searchViewFragment = searchViewFragment;
        querySearch();
    }

    // Queries all ClothingPost IDs that are attached to some OutfitPost
    private void querySearch() {
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
            queryItem(clothingIdSet);
        });
    }

    // Gets all clothing posts that correlate to the set of itemIds
    private void queryItem(Set<String> itemIds) {
        // Item IDs to be used in case of filter search
        searchViewFragment.setItemIds(itemIds);

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

    // Gets OutfitPost objects from the set of their objectIds and adds to fragment adapter
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
                // Update search adapter to bind posts to new view
                searchViewFragment.addToAdapter(fits);
            }
        });
    }

    /** Getters for variables for testing purposes and encapsulation **/
    public String getQuery() {
        return query;
    }

    public Set<String> getFitIdSet() {
        Set<String> copyFitIdSet = new HashSet<>();
        for (String s : fitIdSet) {
            copyFitIdSet.add(s);
        }
        return copyFitIdSet;
    }

    public Set<String> getClothingIdSet() {
        Set<String> copyClothingIdSet = new HashSet<>();
        for (String s : clothingIdSet) {
            copyClothingIdSet.add(s);
        }
        return copyClothingIdSet;
    }

    public SearchViewFragment getSearchViewFragment() {
        return searchViewFragment;
    }
}
