package com.example.closetwear.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.closetwear.EndlessRecyclerViewScrollListener;
import com.example.closetwear.parse.OutfitPost;
import com.example.closetwear.R;
import com.example.closetwear.adapters.HomeAdapter;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is the adapter to handle the content from {@link com.example.closetwear.fragments.HomeFragment}
 * to bind onto the app's home page.
 */
public class HomeFragment extends Fragment {

    public static final String TAG = "PostsFragment";
    private RecyclerView homeRecyclerView;
    private HomeAdapter adapter;
    private List<OutfitPost> allPosts;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;
    private Date oldestPost;

    /**
     * An empty public constructor required to create new instances.
     */
    public HomeFragment() {
    }

    /**
     * {@link Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    /**
     * {@link androidx.fragment.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)}
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeRecyclerView = view.findViewById(R.id.homeRecyclerView);
        allPosts = new ArrayList<>();
        // Create adapter
        adapter = new HomeAdapter(getContext(), allPosts);
        // set the adapter on the recycler view
        homeRecyclerView.setAdapter(adapter);
        // set the layout manager on the recycler view
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        homeRecyclerView.setLayoutManager(gridLayoutManager);

        // Getting SwipeContainerLayout
        swipeContainer = view.findViewById(R.id.swipeContainer);

        // Adding Listener
        swipeContainer.setOnRefreshListener(() -> {
            Log.i(TAG, "Fetching more data!");
            queryPosts();
        });

        // Scheme colors for animation
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        // Set a scroll listener that extends more data to bottom of page
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore: " + page);
                loadMoreData();
            }
        };
        // Adds the scroll listener to RecyclerView
        homeRecyclerView.addOnScrollListener(scrollListener);
        queryPosts();
    }

    /**
     * Makes a call to the Parse server to get a list of the 20 most recent {@link OutfitPost} to
     * bind onto the Home view.
     */
    protected void queryPosts() {
        // specify what type of data we want to query - OutfitPost.class
        ParseQuery<OutfitPost> query = ParseQuery.getQuery(OutfitPost.class);
        // include data referred by user key
        query.include(OutfitPost.KEY_USER);
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder(OutfitPost.KEY_CREATED_KEY);
        // start an asynchronous call for posts
        query.findInBackground((posts, e) -> {
            // check for errors
            if (e != null) {
                Log.e(TAG, "Issue with getting posts", e);
                return;
            }
            // Save oldest post in query for loading more posts
            if (posts.size () > 0) {
                oldestPost = posts.get(posts.size() - 1).getCreatedAt();
            }
            // update adapter with posts list
            adapter.clear();
            adapter.addAll(posts);
            swipeContainer.setRefreshing(false);
        });
    }

    /**
     * Makes a call to the Parse server to get a list of the next 20 {@link OutfitPost} to
     * bind onto the Home view, that haven't already been queried.
     */
    private void loadMoreData() {
        ParseQuery<OutfitPost> query = ParseQuery.getQuery(OutfitPost.class);
        // include data referred by user key
        query.include(OutfitPost.KEY_USER);
        //  Limit query to posts older than previous query
        if (oldestPost != null) {
            query.whereLessThan("createdAt", oldestPost);
        }
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder(OutfitPost.KEY_CREATED_KEY);
        // start an asynchronous call for posts
        query.findInBackground((posts, e) -> {
            // check for errors
            if (e != null) {
                Log.e(TAG, "Issue with getting posts", e);
                return;
            }
            // Save oldest post in query for loading more posts
            if (posts.size() > 0) {
                oldestPost = posts.get(posts.size() - 1).getCreatedAt();
            }
            // update adapter with posts list
            adapter.addAll(posts);
        });
    }
}