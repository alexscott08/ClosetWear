package com.example.closetwear.newoutfit;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.closetwear.ClothingPost;
import com.example.closetwear.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import java.util.List;

public class NewOutfitDialogFragment extends DialogFragment {

    public static final String TAG = "NewOutfitDialogFragment";
    private RecyclerView dialogRecyclerView;
    protected List<ClothingPost> allPosts;
    protected NewOutfitAdapter adapter;


    public NewOutfitDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate layout with recycler view
        View v = inflater.inflate(R.layout.fragment_new_outfit_dialog, container, false);
        dialogRecyclerView = v.findViewById(R.id.dialogRecyclerView);
        dialogRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //setadapter
        adapter = new NewOutfitAdapter(getContext(), allPosts);
        dialogRecyclerView.setAdapter(adapter);
        //get your recycler view and populate it.
        try {
            getAllItems();
        } catch (JSONException e) {
            Log.e(TAG, "Error getting items: ", e);
        }
        return v;
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