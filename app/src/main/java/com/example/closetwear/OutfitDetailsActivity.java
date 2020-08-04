package com.example.closetwear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.closetwear.adapters.OutfitDetailsAdapter;
import com.example.closetwear.parse.ClothingPost;
import com.example.closetwear.parse.OutfitPost;
import com.parse.*;

import org.json.*;
import org.parceler.Parcels;

import java.util.*;

public class OutfitDetailsActivity extends AppCompatActivity {

    private TextView username;
    private ImageView outfitImg;
    private TextView date;
    private ImageView profileImg;
    private TextView favoritesCount;
    private ParseUser user;
    private OutfitPost post = new OutfitPost();

    public static final String TAG = "OutfitDetailsActivity";
    private RecyclerView clothingRecyclerView;
    protected OutfitDetailsAdapter adapter;
    protected List<ClothingPost> allPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_details);
        // Set bottom screen RV full of outfit's items
        clothingRecyclerView = (RecyclerView) findViewById(R.id.clothingRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        clothingRecyclerView.setLayoutManager(linearLayoutManager);

        allPosts = new ArrayList<>();

        // Create adapter
        adapter = new OutfitDetailsAdapter(this, allPosts);
        // set the adapter on the recycler view
        clothingRecyclerView.setAdapter(adapter);
        post = Parcels.unwrap(getIntent().getParcelableExtra("post"));
        // Get all other info about the post from the intent and set on screen
        username = findViewById(R.id.username);
        outfitImg = findViewById(R.id.outfitImg);
        date = findViewById(R.id.dateCreated);
        profileImg = findViewById(R.id.profileImg);
        favoritesCount = findViewById(R.id.favoritesCount);

        try {
            user = post.getUser().fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        username.setText("@" + user.getUsername());
        favoritesCount.setText(post.getLikesCount() + "");
        date.setText(DateUtils.getRelativeTimeSpanString(post.getCreatedAt().getTime()) + "");
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(outfitImg);
        } else {
            // Load placeholder
        }
        // Gets profile pic of user who posted
        final ParseFile profilePic = user.getParseFile("profilePic");
        GlideApp.with(this).load(profilePic.getUrl()).transform(new CircleCrop())
                .into(profileImg);

        // Fill Recycler View
        try {
            getItems();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "ID: " + post.getObjectId());
        outfitImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // full screen view when clicked
            }
        });
    }

    // Converts JSONArray of items to a list to add to adapter
    protected void getItems() throws JSONException {
        JSONArray jsonItems = post.getFitItems();
        // Converts JSONArray to list
        List<String> itemIds = new ArrayList<>();
        if (jsonItems != null) {
            for (int i = 0; i < jsonItems.length(); i++) {
                itemIds.add(jsonItems.getString(i));
            }
            queryTags(itemIds);
        } else {
            adapter.clear();
        }
    }

    // Gets all items that the outfit has tagged
    protected void queryTags(List<String> itemIds) throws JSONException {
        ParseQuery<ClothingPost> query = ParseQuery.getQuery(ClothingPost.class);
        // include data that have one of the given objectIds
        query.whereContainedIn("objectId", itemIds);
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder(ClothingPost.KEY_CREATED_KEY);
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<ClothingPost>() {
            @Override
            public void done(List<ClothingPost> clothing, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                // update adapter with clothing list
                adapter.clear();
                adapter.addAll(clothing);
            }
        });
    }
}