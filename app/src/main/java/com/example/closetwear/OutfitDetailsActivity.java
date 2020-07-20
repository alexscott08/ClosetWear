package com.example.closetwear;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.closetwear.adapters.OutfitDetailsAdapter;
import com.example.closetwear.adapters.OutfitsAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
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





        try {
            getItems();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get all other info about the post from the intent and set on screen
        username = findViewById(R.id.username);
        outfitImg = findViewById(R.id.outfitImg);
//        caption = findViewById(R.id.caption);
        date = findViewById(R.id.date);
        profileImg = findViewById(R.id.profileImg);
        favoritesCount = findViewById(R.id.favoritesCount);
        user = Parcels.unwrap(getIntent().getParcelableExtra("KEY_USER"));
        username.setText("@" + user.getUsername());
        favoritesCount.setText(getIntent().getIntExtra("KEY_LIKES", 0));
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

        outfitImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // full screen view when clicked
            }
        });
    }

    // Converts JSONArray of items to a list to add to adapter
    protected void getItems() throws JSONException {
        JSONArray jsonItems = Parcels.unwrap(getIntent().getParcelableExtra("KEY_ITEMS"));

        // Converts JSONArray to list
        List<ClothingPost> items = new ArrayList<ClothingPost>();
        if (jsonItems != null) {
            for (int i = 0; i < jsonItems.length(); i++){
                items.add((ClothingPost) (Object) jsonItems.getJSONObject(i));
            }
        }
        // Update adapter
        adapter.clear();
        adapter.addAll(items);
    }
}