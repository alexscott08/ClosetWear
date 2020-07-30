package com.example.closetwear.newoutfit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.closetwear.parse.ClothingPost;
import com.example.closetwear.parse.OutfitPost;
import com.example.closetwear.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class TagItemActivity extends AppCompatActivity {

    public static final String TAG = "TagItemActivity";
    private RecyclerView itemRecyclerView;
    private OutfitPost outfitPost;
    protected List<ClothingPost> allPosts;
    protected TagItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_item);
        itemRecyclerView = (RecyclerView) findViewById(R.id.itemRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        itemRecyclerView.setLayoutManager(linearLayoutManager);

        allPosts = new ArrayList<>();
        outfitPost = (OutfitPost) Parcels.unwrap(getIntent().getParcelableExtra("outfit"));
        // Create adapter
        adapter = new TagItemAdapter(this, allPosts, outfitPost);
        // set the adapter on the recycler view
        itemRecyclerView.setAdapter(adapter);

        try {
            getAllItems();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // Converts JSONArray of items to a list to add to adapter
    protected void getAllItems() throws JSONException {
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