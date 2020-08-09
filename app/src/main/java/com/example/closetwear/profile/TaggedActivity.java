package com.example.closetwear.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.closetwear.EndlessRecyclerViewScrollListener;
import com.example.closetwear.R;
import com.example.closetwear.parse.OutfitPost;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaggedActivity extends AppCompatActivity {

    public static final String TAG = "TaggedActivity";
    private RecyclerView tagsRecyclerView;
    protected OutfitsAdapter adapter;
    protected List<OutfitPost> allPosts;
    private StaggeredGridLayoutManager gridLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    private Date oldestPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagged);
        bindViews();
        bindData();
    }

    private void bindViews() {
        tagsRecyclerView = findViewById(R.id.tagsRecyclerView);

        allPosts = new ArrayList<>();
        // Create adapter
        adapter = new OutfitsAdapter(this, allPosts);
        // set the adapter on the recycler view
        tagsRecyclerView.setAdapter(adapter);
        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        tagsRecyclerView.setLayoutManager(gridLayoutManager);


    }

    private void bindData() {
        ArrayList<String> fits = getIntent().getStringArrayListExtra("fits");
        if (fits != null) {
            queryTaggedFits(fits);
        }
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore: " + page);
                if (fits != null) {
                    loadMoreData(fits);
                }
            }
        };
        // Adds the scroll listener to RecyclerView
        tagsRecyclerView.addOnScrollListener(scrollListener);
    }

    private void queryTaggedFits(ArrayList<String> fits) {
        ParseQuery<OutfitPost> query = ParseQuery.getQuery(OutfitPost.class);
        query.whereContainedIn("objectId", fits);
        // limit query to latest 20 items
        query.setLimit(20);
        query.whereEqualTo(OutfitPost.KEY_USER, ParseUser.getCurrentUser());
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

        query.whereContainedIn("objectId", fits);
        query.whereEqualTo(OutfitPost.KEY_USER, ParseUser.getCurrentUser());
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