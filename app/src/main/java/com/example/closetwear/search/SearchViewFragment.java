package com.example.closetwear.search;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.closetwear.parse.ClothingPost;
import com.example.closetwear.parse.OutfitPost;
import com.example.closetwear.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;
import org.json.*;

import java.util.*;

public class SearchViewFragment extends Fragment {

    private static final String TAG = "SearchFragment";
    private RecyclerView searchRecyclerView;
    protected List<OutfitPost> searchPosts;
    protected SearchViewAdapter adapter;
    private ExtendedFloatingActionButton filter;
    private boolean[] checkedItems;
    private Set<String> itemIdSet;
    private Map<String, Boolean> options;
    private String query;

    public SearchViewFragment() {
        // Required empty public constructor
    }

    // Constructor for fragment, will be called from MainActivity
    public SearchViewFragment(List<OutfitPost> searchPosts) {
        this.searchPosts = searchPosts;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_view, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filter = view.findViewById(R.id.filterFAB);
        searchRecyclerView = view.findViewById(R.id.searchRecyclerView);
        searchPosts = new ArrayList<>();
        // Create adapter
        adapter = new SearchViewAdapter(getContext(), searchPosts);
        // set the adapter on the recycler view
        searchRecyclerView.setAdapter(adapter);
        // set the layout manager on the recycler view
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        searchRecyclerView.setLayoutManager(gridLayoutManager);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedItems = new boolean[5];
                String[] multiChoiceItems = getResources().getStringArray(R.array.filter_array);
                options = new HashMap<>();
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Filter Your Search")
                        .setMultiChoiceItems(multiChoiceItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                checkedItems[i] = b;
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                for (int j = 0; j < checkedItems.length; j++) {
                                    if (j == 0) {
                                        options.put("Category", checkedItems[j]);
                                    } else if (j == 1) {
                                        options.put("Subcategory", checkedItems[j]);
                                    } else if (j == 2) {
                                        options.put("Name", checkedItems[j]);
                                    } else if (j == 3) {
                                        options.put("Brand", checkedItems[j]);
                                    } else {
                                        options.put("Color", checkedItems[j]);
                                    }
                                }
                                startFilterSearch();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
    }

    public void startFilterSearch() {
        adapter.clear();
        queryItem(itemIdSet, query, options);
    }
    public void addToAdapter(List<OutfitPost> posts) {
        adapter.clear();
        searchPosts.addAll(posts);
        adapter.addAll(searchPosts);
    }

    public void setItemIds(Set<String> itemIdSet) {
        this.itemIdSet = itemIdSet;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    // Gets all clothing posts that correlate to the set of itemIds
    private void queryItem(Set<String> itemIds, String query, Map<String, Boolean> optionMap) {
        ParseQuery<ClothingPost> parseQuery = ParseQuery.getQuery(ClothingPost.class);
        parseQuery.whereContainedIn("objectId", itemIds);
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
                                if (optionMap.get("Category")) {
                                    if (item.getCategory().toLowerCase().contains(query)) {
                                        itemIdSet.add(itemFits.getString(i));
                                    }
                                }
                                if (optionMap.get("Subcategory")) {
                                    if (item.getSubcategory().toLowerCase().contains(query)) {
                                        itemIdSet.add(itemFits.getString(i));
                                    }
                                }
                                if (optionMap.get("Name")) {
                                    if (item.getName().toLowerCase().contains(query)) {
                                        itemIdSet.add(itemFits.getString(i));
                                    }
                                }
                                if (optionMap.get("Brand")) {
                                    if (item.getBrand().toLowerCase().contains(query)) {
                                        itemIdSet.add(itemFits.getString(i));
                                    }
                                }
                                if (optionMap.get("Color")) {
                                    if (item.getColor().toLowerCase().contains(query)) {
                                        itemIdSet.add(itemFits.getString(i));
                                    }
                                }
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                queryFits(itemIdSet);
            }
        });
    }

    // After filter search finds all fits that apply, gets the object and adds to adapter
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
}
