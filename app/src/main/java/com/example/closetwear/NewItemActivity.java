package com.example.closetwear;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class NewItemActivity extends AppCompatActivity {

    private ImageView itemImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        itemImg = findViewById(R.id.itemImg);
    }
}