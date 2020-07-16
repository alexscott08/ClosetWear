package com.example.closetwear;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.closetwear.adapters.OutfitDetailsAdapter;
import com.example.closetwear.adapters.OutfitsAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class OutfitDetailsActivity extends AppCompatActivity {

    private TextView username;
    private ImageView outfitImg;
//    private TextView caption;
    private TextView date;
    private ImageView profileImg;
    private TextView favoritesCount;
    private ParseUser user;

    public static final String TAG = "OutfitDetailsActivity";
    private RecyclerView closetRecyclerView;
    protected OutfitDetailsAdapter adapter;
    protected List<ClothingPost> allPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_details);

        // Set bottom screen RV full of outfit's items
        closetRecyclerView = findViewById(R.id.closetRecyclerView);
        allPosts = new ArrayList<>();
        // Create adapter
        adapter = new OutfitDetailsAdapter(this, allPosts);
        // set the adapter on the recycler view
        closetRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        closetRecyclerView.setLayoutManager(linearLayoutManager);
        queryPosts();

        // Get all other info about the post from the intent and set on screen
        username = findViewById(R.id.username);
        outfitImg = findViewById(R.id.outfitImg);
//        caption = findViewById(R.id.caption);
        date = findViewById(R.id.date);
        profileImg = findViewById(R.id.profileImg);
        favoritesCount = findViewById(R.id.favoritesCount);
        user = Parcels.unwrap(getIntent().getParcelableExtra("KEY_USER"));
        username.setText("@" + user.getUsername());
        favoritesCount.setText(getIntent().getStringExtra("KEY_LIKES"));
        date.setText(getIntent().getStringExtra("KEY_CREATED_KEY"));
        ParseFile image = Parcels.unwrap(getIntent().getParcelableExtra("KEY_IMAGE"));
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(outfitImg);
        } else {
            // Load placeholder
        }
        // Gets profile pic of user who posted
        final ParseFile profilePic = user.getParseFile("profilePic");
        GlideApp.with(this).load(profilePic.getUrl()).transform(new CircleCrop())
                .into(profileImg);
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
        query.addDescendingOrder(OutfitPost.KEY_CREATED_KEY);
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