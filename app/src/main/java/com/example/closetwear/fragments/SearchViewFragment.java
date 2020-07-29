package com.example.closetwear.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.closetwear.OutfitPost;
import com.example.closetwear.R;
import com.example.closetwear.adapters.SearchViewAdapter;
import com.parse.FindCallback;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class SearchViewFragment extends Fragment {

    private static final String TAG = "SearchFragment";
    private RecyclerView searchRecyclerView;
    protected List<OutfitPost> searchPosts;
    protected SearchViewAdapter adapter;
    public boolean isShowing;

    public SearchViewFragment() {
        // Required empty public constructor
    }

    public SearchViewFragment(List<OutfitPost> searchPosts) {
        this.searchPosts = searchPosts;
        isShowing = true;
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
        if (!isShowing) {
            queryPosts();
        }
    }

    public void addToAdapter(List<OutfitPost> posts) {
        searchPosts.addAll(posts);
        adapter.addAll(searchPosts);
    }

    public void queryPosts() {
        // specify what type of data we want to query - OutfitPost.class
        ParseQuery<OutfitPost> query = ParseQuery.getQuery(OutfitPost.class);
        query.include(OutfitPost.KEY_USER);
        query.findInBackground(new FindCallback<OutfitPost>() {
            @Override
            public void done(List<OutfitPost> objects, com.parse.ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Problem  with getting items", e);
                    return;
                }
                for (OutfitPost item : searchPosts) {
                    Log.i(TAG, "User: " + item.getUser().toString());
                }
                searchPosts.clear();
                searchPosts.addAll(objects);
                adapter.addAll(searchPosts);
            }
        });
    }

}