package com.example.closetwear.newoutfit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.closetwear.ClothingPost;
import com.example.closetwear.GlideApp;
import com.example.closetwear.Navigation;
import com.example.closetwear.OutfitPost;
import com.example.closetwear.R;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class NewOutfitActivity extends AppCompatActivity {

    public static final String TAG = "NewOutfitActivity";
    private ImageView outfitImg;
    private RecyclerView taggedItemsRecyclerView;
    private Button tagBtn;
    private Button addBtn;
    private OutfitPost outfitPost;
    protected NewOutfitAdapter adapter;
    protected List<ClothingPost> allPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_outfit);
        addBtn = findViewById(R.id.addBtn);
        tagBtn = findViewById(R.id.tagBtn);
        outfitImg = findViewById(R.id.outfitImg);
        outfitPost = (OutfitPost) Parcels.unwrap(getIntent().getParcelableExtra("outfit"));
        GlideApp.with(this).load(outfitPost.getImage().getUrl()).into(outfitImg);
        taggedItemsRecyclerView = (RecyclerView) findViewById(R.id.taggedItemsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        taggedItemsRecyclerView.setLayoutManager(linearLayoutManager);

        allPosts = new ArrayList<>();

        // Create adapter
        adapter = new NewOutfitAdapter(this, allPosts);
        // set the adapter on the recycler view
        taggedItemsRecyclerView.setAdapter(adapter);
        outfitImg = findViewById(R.id.outfitImg);
        ParseFile image = Parcels.unwrap(getIntent().getParcelableExtra("image"));
        if (image != null) {
            GlideApp.with(this).load(image.getUrl()).into(outfitImg);
        } else {
            // Load placeholder
        }
        allPosts.add((ClothingPost) Parcels.unwrap(getIntent().getParcelableExtra("post")));

        try {
            getItems();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        outfitImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // full screen view when clicked
            }
        });
        tagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.goTagItemActivity(NewOutfitActivity.this, outfitPost);
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePost(view);
            }
        });
    }

    // Adds current user to ClothingPost and saves post to Parse server, allowing user to cancel
    private void savePost(final View view) {
        outfitPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(NewOutfitActivity.this, "Error while saving fit!", Toast.LENGTH_SHORT).show();
                }
                Snackbar.make(view, "New fit added to closet!", Snackbar.LENGTH_LONG).setAction("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        outfitPost.deleteInBackground();
                    }
                }).show();
                Log.i(TAG, "New fit added to closet!");

                // Navigates to MainActivity after OutfitPost has been uploaded
                Navigation.goMainActivity(NewOutfitActivity.this);
            }
        });
    }

    // Uses JSONArray of objectIds to create list full of ClothingPosts for the adapter
    protected void getItems() throws JSONException {
        JSONArray jsonItems = outfitPost.getFitItems();
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
                //update item's list of fits it is in
                for (ClothingPost item : clothing) {
                    item.setFit(outfitPost.getObjectId());
                }
                // update adapter with clothing list
                adapter.clear();
                adapter.addAll(clothing);
            }
        });
    }
}