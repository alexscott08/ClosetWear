package com.example.closetwear.search;

import android.util.Log;

import com.example.closetwear.parse.*;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.*;

import java.util.*;

public class FilterQuery {
    public static final String TAG = "FilterQuery";

    // Gets all clothing posts that correlate to the set of itemIds
    public static Set<String> queryItem(String query, Set<String> itemIdSet, Map<String, Boolean> options) {
        Set<String> filteredFitIds = new HashSet<>();
        try {
            ParseQuery<ClothingPost> parseQuery = ParseQuery.getQuery(ClothingPost.class);
            parseQuery.whereContainedIn("objectId", itemIdSet);
            List<ClothingPost> items = parseQuery.find();
            for (ClothingPost item : items) {
                JSONArray itemFits = item.getFit();
                if (itemFits != null) {
                    try {
                        for (int i = 0; i < itemFits.length(); i++) {
                            if (options.get("Category")) {
                                if (item.getCategory().toLowerCase().contains(query)) {
                                    filteredFitIds.add(itemFits.getString(i));
                                }
                            }
                            if (options.get("Subcategory")) {
                                if (item.getSubcategory().toLowerCase().contains(query)) {
                                    filteredFitIds.add(itemFits.getString(i));
                                }
                            }
                            if (options.get("Name")) {
                                if (item.getName().toLowerCase().contains(query)) {
                                    filteredFitIds.add(itemFits.getString(i));
                                }
                            }
                            if (options.get("Brand")) {
                                if (item.getBrand().toLowerCase().contains(query)) {
                                    filteredFitIds.add(itemFits.getString(i));
                                }
                            }
                            if (options.get("Color")) {
                                if (item.getColor().toLowerCase().contains(query)) {
                                    filteredFitIds.add(itemFits.getString(i));
                                }
                            }
                        }
                    } catch (JSONException ex) {
                        Log.e(TAG, "Problem  with JSON method: ", ex);
                    }
                }
            }
        } catch (ParseException e) {
            Log.e(TAG, "Problem  with querying item: ", e);
        }
        return filteredFitIds;
    }

    // After filter search, finds all fits that apply; gets the object and adds to adapter
    public static List<OutfitPost> queryFits(Set<String> filteredFitIds) {
        List<OutfitPost> fitsList = new ArrayList<>();
        try {
            ParseQuery<OutfitPost> query = ParseQuery.getQuery(OutfitPost.class);
            query.whereContainedIn("objectId", filteredFitIds);
            fitsList = query.find();
        } catch (ParseException e) {
            Log.e(TAG, "Problem  with querying item: ", e);
        }
        return fitsList;
    }

}

