package com.example.closetwear.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.closetwear.parse.OutfitPost;
import com.example.closetwear.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SearchViewFragment extends Fragment {

    private static final String TAG = "SearchFragment";
    private RecyclerView searchRecyclerView;
    protected List<OutfitPost> searchPosts = new ArrayList<>();
    protected SearchViewAdapter adapter;
    private ExtendedFloatingActionButton filter;
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
        adapter = new SearchViewAdapter(getContext(), searchPosts);
        adapter.addAll(searchPosts);
        // set the adapter on the recycler view
        searchRecyclerView.setAdapter(adapter);

        // set the layout manager on the recycler view
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        searchRecyclerView.setLayoutManager(gridLayoutManager);

        // Allows user to filter search for more specific results
        filter.setOnClickListener(view1 -> {
            boolean[] checkedItems = new boolean[5];
            String[] multiChoiceItems = getResources().getStringArray(R.array.filter_array);
            options = new HashMap<>();
            new MaterialAlertDialogBuilder(getContext())
                    .setTitle("Filter Your Search")
                    .setMultiChoiceItems(multiChoiceItems, checkedItems, (dialogInterface, i, b) -> checkedItems[i] = b)
                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                        // Set options map boolean values after Ok click
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
                        filterSearch();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    // If Ok is clicked on dialog, filters search based on options selected
    public void filterSearch() {
        // 1. Clear adapter and find all fits that contain items with one of the filters in their fields
        adapter.clear();
        Set<String> filteredFitIds = FilterQuery.queryItem(query, itemIdSet, options);

        // 2. Find the OutfitPost obj for each fit ID and update adapter
        List<OutfitPost> filteredFits = FilterQuery.queryFits(filteredFitIds);
        adapter.addAll(filteredFits);

    }

    // Used by MainActivity to update posts on adapter
    public void addToAdapter(List<OutfitPost> posts) {
        searchPosts.addAll(posts);
    }

    // Used by MainActivity to pass item IDs for filtered search
    public void setItemIds(Set<String> itemIdSet) {
        this.itemIdSet = itemIdSet;
    }

    // Used by MainActivity to pass original query for filtered search
    public void setQuery(String query) {
        this.query = query;
    }


}
