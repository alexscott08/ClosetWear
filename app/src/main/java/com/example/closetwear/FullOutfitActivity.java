package com.example.closetwear;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.closetwear.parse.OutfitPost;

import org.parceler.Parcels;

public class FullOutfitActivity extends AppCompatActivity {

    private ImageView fullFitImg;
    private OutfitPost outfit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_outfit);
        bindViews();
        bindData();
    }

    private void bindViews() {
        fullFitImg = findViewById(R.id.fullFitImg);
    }

    private void bindData() {
        outfit = Parcels.unwrap(getIntent().getParcelableExtra("outfit"));
        GlideApp.with(this).load(outfit.getImage().getUrl()).into(fullFitImg);
    }
}