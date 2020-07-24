package com.example.closetwear.newoutfit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.closetwear.ClothingPost;
import com.example.closetwear.GlideApp;
import com.example.closetwear.R;
import com.parse.ParseFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class NewOutfitActivity extends AppCompatActivity {

    private ImageView outfitImg;
    private RecyclerView taggedItemsRecyclerView;
    private Button tagBtn;
    private Button addBtn;
    protected NewOutfitAdapter adapter;
    protected List<ClothingPost> allPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_outfit);
        addBtn = findViewById(R.id.addBtn);
        tagBtn = findViewById(R.id.tagBtn);
        outfitImg = findViewById(R.id.outfitImg);
        GlideApp.with(this).load(getIntent().getSerializableExtra("image")).into(outfitImg);
        taggedItemsRecyclerView = (RecyclerView) findViewById(R.id.taggedItemsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        taggedItemsRecyclerView.setLayoutManager(linearLayoutManager);

        allPosts = new ArrayList<>();

        // Create adapter
        adapter = new NewOutfitAdapter(this, allPosts);
        // set the adapter on the recycler view
        taggedItemsRecyclerView.setAdapter(adapter);

        try {
            getItems();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get all other info about the post from the intent and set on screen
        outfitImg = findViewById(R.id.outfitImg);
        ParseFile image = Parcels.unwrap(getIntent().getParcelableExtra("image"));
        if (image != null) {
            GlideApp.with(this).load(image.getUrl()).into(outfitImg);
        } else {
            // Load placeholder
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
                showDialog();
            }
        });
    }

    public void showDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        NewOutfitDialogFragment newFragment = new NewOutfitDialogFragment();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit();
    }

    // Converts JSONArray of items to a list to add to adapter
    protected void getItems() throws JSONException {
        JSONArray jsonItems = Parcels.unwrap(getIntent().getParcelableExtra("KEY_ITEMS"));

        // Converts JSONArray to list
        List<ClothingPost> items = new ArrayList<ClothingPost>();
        if (jsonItems != null) {
            for (int i = 0; i < jsonItems.length(); i++) {
                items.add((ClothingPost) (Object) jsonItems.getJSONObject(i));
            }
        }
        // Update adapter
        adapter.clear();
        adapter.addAll(items);
    }
}