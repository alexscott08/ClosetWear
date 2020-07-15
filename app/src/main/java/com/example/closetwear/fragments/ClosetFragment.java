package com.example.closetwear.fragments;

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

import com.example.closetwear.ClothingPost;
import com.example.closetwear.R;
import com.example.closetwear.adapters.ClosetAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ClosetFragment extends Fragment {
    public static final String TAG = "ClosetFragment";
    private RecyclerView closetRecyclerView;
    protected ClosetAdapter adapter;
    protected List<ClothingPost> allPosts;

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
        allPosts = new ArrayList<>();
        // Create adapter
        adapter = new ClosetAdapter(getContext(), allPosts);
        // set the adapter on the recycler view
        closetRecyclerView.setAdapter(adapter);
        // set the layout manager on the recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        closetRecyclerView.setLayoutManager(linearLayoutManager);
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
        query.findInBackground(new FindCallback<ClothingPost>() {
            @Override
            public void done(List<ClothingPost> posts, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                // update adapter with posts list
                adapter.clear();
                adapter.addAll(posts);
            }
        });
    }
}