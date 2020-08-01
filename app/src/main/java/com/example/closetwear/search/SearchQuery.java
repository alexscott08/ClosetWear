package com.example.closetwear.search;

import android.util.Log;

import com.example.closetwear.parse.*;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.*;

import java.util.*;

public class SearchQuery {
    public static final String TAG = "SearchQuery";

    // Queries all ClothingPost IDs that are attached to some OutfitPost
    public static Set<String> querySearch() {
        Set<String> clothingIdSet = new HashSet<>();
        try {
            ParseQuery<OutfitPost> parseQuery = ParseQuery.getQuery(OutfitPost.class);
            parseQuery.include(OutfitPost.KEY_USER);
            List<OutfitPost> fits = parseQuery.find();
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
        } catch (ParseException e) {
            Log.e(TAG, "Problem  with querying users: ", e);
        }
        return clothingIdSet;
    }

    // Gets all clothing posts that correlate to the set of itemIds
    public static Set<String> queryItem(Set<String> itemIds, String query) {
        // Item IDs to be used in case of filter search
        Set<String> fitIdSet = new HashSet<>();
        try {
            ParseQuery<ClothingPost> parseQuery = ParseQuery.getQuery(ClothingPost.class);
            parseQuery.whereContainedIn("objectId", itemIds);
            List<ClothingPost> items = parseQuery.find();
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
        } catch (ParseException e) {
            Log.e(TAG, "Problem  with querying item", e);
        }
        return fitIdSet;
    }

    // Gets OutfitPost objects from the set of their objectIds and adds to fragment adapter
    public static List<OutfitPost> queryFits(Set<String> fitIds, SearchViewFragment searchViewFragment) {
        List<OutfitPost> fitsList = new ArrayList<>();
        try {
            ParseQuery<OutfitPost> query = ParseQuery.getQuery(OutfitPost.class);
            query.whereContainedIn("objectId", fitIds);
            fitsList = query.find();
            searchViewFragment.addToAdapter(fitsList);
        } catch (ParseException e) {
            Log.e(TAG, "Problem  with querying item", e);
        }
        return fitsList;
    }
}
