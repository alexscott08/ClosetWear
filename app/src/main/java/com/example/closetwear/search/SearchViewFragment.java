package com.example.closetwear.search;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import java.util.ArrayList;
import java.util.List;

public class SearchViewFragment extends Fragment {

    private static final String TAG = "SearchFragment";
    private RecyclerView searchRecyclerView;
    protected List<OutfitPost> searchPosts;
    protected SearchViewAdapter adapter;
    private ExtendedFloatingActionButton filter;
    private boolean[] checkedItems;

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
                                // Nothing needs to be done
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

    }

    public void addToAdapter(List<OutfitPost> posts) {
        adapter.clear();
        searchPosts.addAll(posts);
        adapter.addAll(searchPosts);
    }

}
