package com.example.closetwear.profile;

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

import com.example.closetwear.EndlessRecyclerViewScrollListener;
import com.example.closetwear.parse.OutfitPost;
import com.example.closetwear.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FavoritesFragment extends Fragment {

    public static final String TAG = "OutfitsFragment";
    private RecyclerView favsRecyclerView;
    protected OutfitsAdapter adapter;
    protected List<OutfitPost> allPosts;
    private StaggeredGridLayoutManager gridLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    private Date oldestPost;


    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        bindData();
    }

    private void bindViews(View view) {
        favsRecyclerView = view.findViewById(R.id.favsRecyclerView);

        allPosts = new ArrayList<>();
        // Create adapter
        adapter = new OutfitsAdapter(getContext(), allPosts);
        // set the adapter on the recycler view
        favsRecyclerView.setAdapter(adapter);
        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        favsRecyclerView.setLayoutManager(gridLayoutManager);


    }

    private void bindData() {
        JSONArray favFits = ParseUser.getCurrentUser().getJSONArray("likes");
        ArrayList<String> fitIds = new ArrayList<>();
        if (favFits != null) {
            for (int i = 0; i < favFits.length(); i++) {
                try {
                    fitIds.add(favFits.getString(i));
                } catch (JSONException e) {
                    Log.e(TAG, "JsonException pulling out fitIds: " + e);
                }
            }
            queryLikedFits(fitIds);
        }
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore: " + page);
                if (fitIds != null) {
                    loadMoreData(fitIds);
                }
            }
        };
        // Adds the scroll listener to RecyclerView
        favsRecyclerView.addOnScrollListener(scrollListener);
    }

    private void queryLikedFits(ArrayList<String> fits) {
        ParseQuery<OutfitPost> query = ParseQuery.getQuery(OutfitPost.class);
        query.whereContainedIn("objectId", fits);
        query.include(OutfitPost.KEY_USER);
        query.addDescendingOrder(OutfitPost.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<OutfitPost>() {
            @Override
            public void done(List<OutfitPost> posts, ParseException e) {
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
            }
        });
    }

    private void loadMoreData(ArrayList<String> fits) {
        ParseQuery<OutfitPost> query = ParseQuery.getQuery(OutfitPost.class);
        //  Limit query to posts older than previous query
        if (oldestPost != null) {
            query.whereLessThan("createdAt", oldestPost);
        }

        query.include(OutfitPost.KEY_USER);

        query.whereContainedIn("objectId", fits);
        // limit query to latest 20 items
        query.setLimit(20);
        query.findInBackground((posts, e) -> {
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