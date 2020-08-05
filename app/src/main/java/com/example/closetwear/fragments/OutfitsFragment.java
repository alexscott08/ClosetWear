package com.example.closetwear.fragments;

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
import android.widget.TextView;

import com.example.closetwear.parse.OutfitPost;
import com.example.closetwear.R;
import com.example.closetwear.adapters.OutfitsAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class OutfitsFragment extends Fragment {

    public static final String TAG = "OutfitsFragment";
    private RecyclerView fitsRecyclerView;
    protected OutfitsAdapter adapter;
    protected List<OutfitPost> allPosts;
    protected TextView closetText;


    public OutfitsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_outfits, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fitsRecyclerView = view.findViewById(R.id.fitsRecyclerView);
        closetText = view.findViewById(R.id.closetText);

        allPosts = new ArrayList<>();
        // Create adapter
        adapter = new OutfitsAdapter(getContext(), allPosts);
        // set the adapter on the recycler view
        fitsRecyclerView.setAdapter(adapter);
        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        fitsRecyclerView.setLayoutManager(gridLayoutManager);
        Bundle bundle = this.getArguments();
        ArrayList<String> fits;
        if (bundle == null) {
            closetText.setText("Your Fits");
            queryPosts();
        } else if (bundle.get("view").equals("taggedFits")) {
            closetText.setText("Tagged Fits");
            fits = bundle.getStringArrayList("fits");
            queryTaggedFits(fits);
        } else {
            closetText.setText("Your Favorites");
            fits = bundle.getStringArrayList("fits");
            queryLikedFits(fits);
        }

    }

    private void queryLikedFits(ArrayList<String> fits) {
        ParseQuery<OutfitPost> query = ParseQuery.getQuery(OutfitPost.class);
        query.whereContainedIn("objectId", fits);
        query.include(OutfitPost.KEY_USER);
        query.findInBackground(new FindCallback<OutfitPost>() {
            @Override
            public void done(List<OutfitPost> posts, ParseException e) {
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

    private void queryTaggedFits(ArrayList<String> fits) {
        ParseQuery<OutfitPost> query = ParseQuery.getQuery(OutfitPost.class);
        query.whereContainedIn("objectId", fits);
        query.whereEqualTo(OutfitPost.KEY_USER, ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<OutfitPost>() {
            @Override
            public void done(List<OutfitPost> posts, ParseException e) {
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

    protected void queryPosts() {
        // specify what type of data we want to query - OutfitPost.class
        ParseQuery<OutfitPost> query = ParseQuery.getQuery(OutfitPost.class);
        // only include data referred by user key
        query.include(OutfitPost.KEY_USER);
        query.whereEqualTo(OutfitPost.KEY_USER, ParseUser.getCurrentUser());
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder(OutfitPost.KEY_CREATED_KEY);
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<OutfitPost>() {
            @Override
            public void done(List<OutfitPost> posts, ParseException e) {
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