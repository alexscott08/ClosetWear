package com.example.closetwear;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
public class NewOutfitActivity extends AppCompatActivity {

    private ImageView outfitImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_outfit);
        outfitImg = findViewById(R.id.outfitImg);
        GlideApp.with(this).load(getIntent().getSerializableExtra("image")).into(outfitImg);
    }
}