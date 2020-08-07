package com.example.closetwear.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.closetwear.EndlessRecyclerViewScrollListener;
import com.example.closetwear.parse.ClothingPost;
import com.example.closetwear.R;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClosetFragment extends Fragment {
    public static final String TAG = "ClosetFragment";
    private RecyclerView closetRecyclerView;
    protected ClosetAdapter adapter;
    protected List<ClothingPost> allPosts;
    private EndlessRecyclerViewScrollListener scrollListener;
    private Date oldestPost;

    public ClosetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_closet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        closetRecyclerView = view.findViewById(R.id.closetRecyclerView);
        closetRecyclerView.setPadding(120, 0, 0, 0);
        allPosts = new ArrayList<>();
        // Create adapter
        adapter = new ClosetAdapter(getContext(), allPosts, getParentFragmentManager());
        // set the adapter on the recycler view
        closetRecyclerView.setAdapter(adapter);
        // set the layout manager on the recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        closetRecyclerView.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore: " + page);
                loadMoreData();
            }
        };
        // Adds the scroll listener to RecyclerView
        closetRecyclerView.addOnScrollListener(scrollListener);
        queryPosts();
    }

    protected void queryPosts() {
        // specify what type of data we want to query - ClothingPost.class
        ParseQuery<ClothingPost> query = ParseQuery.getQuery(ClothingPost.class);
        // only include data referred by user key
        query.include(ClothingPost.KEY_USER);
        query.whereEqualTo(ClothingPost.KEY_USER, ParseUser.getCurrentUser());
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder(ClothingPost.KEY_CREATED_KEY);
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
            adapter.clear();
            adapter.addAll(posts);
        });
    }

    private void loadMoreData() {
        ParseQuery<ClothingPost> query = ParseQuery.getQuery(ClothingPost.class);
        // include data referred by user key
        query.include(ClothingPost.KEY_USER);
        query.whereEqualTo(ClothingPost.KEY_USER, ParseUser.getCurrentUser());
        //  Limit query to posts older than previous query
        if (oldestPost != null) {
            query.whereLessThan("createdAt", oldestPost);
        }
        // limit query to latest 20 items
        query.setLimit(10);
        // order posts by creation date (newest first)
        query.addDescendingOrder(ClothingPost.KEY_CREATED_KEY);
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