package com.example.closetwear.search;

import android.util.Log;

import com.example.closetwear.parse.*;
import com.parse.ParseQuery;

import org.json.*;

import java.util.*;

public class FilterQuery {
    public static final String TAG = "FilterQuery";
    private final String query;
    private Set<String> itemIdSet;
    private Map<String, Boolean> options;
    private SearchViewAdapter adapter;

    public FilterQuery(Set<String> itemIdSet, String query, Map<String, Boolean> options, SearchViewAdapter adapter) {
        this.itemIdSet = itemIdSet;
        this.query = query;
        this.options = options;
        this.adapter = adapter;
        queryItem();
    }

    // Gets all clothing posts that correlate to the set of itemIds
    private void queryItem() {
        Set<String> filteredItemIds = new HashSet<>();
        ParseQuery<ClothingPost> parseQuery = ParseQuery.getQuery(ClothingPost.class);
        parseQuery.whereContainedIn("objectId", itemIdSet);
        parseQuery.findInBackground((items, e) -> {
            if (e != null) {
                Log.e(TAG, "Problem  with querying item", e);
                return;
            } else {
                // if filter option is selected, adds all fits that apply to filter
                for (ClothingPost item : items) {
                    JSONArray itemFits = item.getFit();
                    if (itemFits != null) {
                        try {
                            for (int i = 0; i < itemFits.length(); i++) {
                                if (options.get("Category")) {
                                    if (item.getCategory().toLowerCase().contains(query)) {
                                        filteredItemIds.add(itemFits.getString(i));
                                    }
                                }
                                if (options.get("Subcategory")) {
                                    if (item.getSubcategory().toLowerCase().contains(query)) {
                                        filteredItemIds.add(itemFits.getString(i));
                                    }
                                }
                                if (options.get("Name")) {
                                    if (item.getName().toLowerCase().contains(query)) {
                                        filteredItemIds.add(itemFits.getString(i));
                                    }
                                }
                                if (options.get("Brand")) {
                                    if (item.getBrand().toLowerCase().contains(query)) {
                                        filteredItemIds.add(itemFits.getString(i));
                                    }
                                }
                                if (options.get("Color")) {
                                    if (item.getColor().toLowerCase().contains(query)) {
                                        filteredItemIds.add(itemFits.getString(i));
                                    }
                                }
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                queryFits(filteredItemIds);
            }
        });
    }

    // After filter search, finds all fits that apply; gets the object and adds to adapter
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
                adapter.addAll(fits);
            }
        });
    }

    /** Getters for variables for testing purposes and encapsulation **/
    public String getQuery() {
        return query;
    }

    public Set<String> getItemIdSet() {
        Set<String> itemIdSetCopy = new HashSet<>();
        for (String s : itemIdSet) {
            itemIdSetCopy.add(s);
        }
        return itemIdSetCopy;
    }

    public Map<String, Boolean> getOptions() {
        Map<String, Boolean> optionsCopy = new HashMap<>();
        for (String s : options.keySet()) {
            optionsCopy.put(s, options.get(s));
        }
        return optionsCopy;
    }

    public SearchViewAdapter getAdapter() {
        return adapter;
    }
}

