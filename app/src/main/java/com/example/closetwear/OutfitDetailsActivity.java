package com.example.closetwear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    private ImageView favoritesIcon;
    private ParseUser postUser;
    private ParseUser currentUser = ParseUser.getCurrentUser();
    private OutfitPost post = new OutfitPost();

    public static final String TAG = "OutfitDetailsActivity";
    private RecyclerView clothingRecyclerView;
    protected OutfitDetailsAdapter adapter;
    protected List<ClothingPost> allPosts;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_details);
        post = Parcels.unwrap(getIntent().getParcelableExtra("post"));

       bindViews();
       bindData();
        // Fill Recycler View
        try {
            getItems();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Log.i(TAG, "ID: " + post.getObjectId());
        outfitImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // full screen view when clicked
            }
        });
        favoritesIcon.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(OutfitDetailsActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Log.d(TAG, "Double tapped");
                    if (!doesLikesContainId(post.getObjectId())) {
                        // Updates user array with post's object ID
                        currentUser.addUnique("likes", post.getObjectId());
                        post.setLikedBy(currentUser.getObjectId());
                        // Adds one to the post's like count and updates states
                        post.setLikesCount(post.getLikesCount() + 1);
                        favoritesIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite));
                        updateParse();
                    } else {

                        // Removes post's object ID from user's array of liked posts
                        removeLike();
//                        currentUser.getParseObject("likes").remove(post.getObjectId());
                        // Decrements post's like count by one and updates states
                        post.setLikesCount(post.getLikesCount() - 1);
                        favoritesIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border));
                        updateParse();
                    }
                    return super.onDoubleTap(e);
                }
                @Override
                public boolean onSingleTapConfirmed(MotionEvent event) {
                    Log.d(TAG, "onSingleTap");
                    return false;
                }
            });
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    private void removeLike() {
        JSONArray likesJsonArray = currentUser.getJSONArray("likes");
        if (likesJsonArray != null) {
            for (int i = 0; i < likesJsonArray.length(); i++) {
                try {
                    if (likesJsonArray.get(i).equals(currentUser.getObjectId())) {
                        likesJsonArray.remove(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Saves new states to Parse server
    private void updateParse() {
        try {
            currentUser.save();
            post.save();
        } catch (ParseException ex) {
            Log.e(TAG, "Error saving to Parse server: " + ex);
        }
    }

    private void bindViews() {
        clothingRecyclerView = (RecyclerView) findViewById(R.id.clothingRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        clothingRecyclerView.setLayoutManager(linearLayoutManager);
        username = findViewById(R.id.username);
        outfitImg = findViewById(R.id.outfitImg);
        date = findViewById(R.id.dateCreated);
        profileImg = findViewById(R.id.profileImg);
        favoritesCount = findViewById(R.id.favoritesCount);
        favoritesIcon = findViewById(R.id.favoritesIcon);
    }

    private void bindData() {
        // Set bottom screen RV full of outfit's items
        allPosts = new ArrayList<>();
        // Create adapter
        adapter = new OutfitDetailsAdapter(this, allPosts);
        // set the adapter on the recycler view
        clothingRecyclerView.setAdapter(adapter);

        // Get all other info about the post from the intent and set on screen
        try {
            postUser = post.getUser().fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        username.setText("@" + postUser.getUsername());
        favoritesCount.setText(post.getLikesCount() + "");
        date.setText(DateUtils.getRelativeTimeSpanString(post.getCreatedAt().getTime()) + "");

        ParseFile image = post.getImage();
        if (image != null) {
            GlideApp.with(this).load(image.getUrl()).placeholder(R.drawable.ic_profileicon).into(outfitImg);
        } else {
            // Load placeholder
        }
        // Gets profile pic of user who posted
        final ParseFile profilePic = postUser.getParseFile("profilePic");
        GlideApp.with(this).load(profilePic.getUrl()).transform(new CircleCrop())
                .into(profileImg);
        updateParse();
        if (doesLikesContainId(post.getObjectId())) {
            favoritesIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite));
        } else {
            favoritesIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border));
        }
    }

    private boolean doesLikesContainId(String objectId) {
        if (currentUser.get("likes") != null) {
            List<String> likesList = (List) currentUser.get("likes");
            return likesList.contains(objectId);
        }
        return false;
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