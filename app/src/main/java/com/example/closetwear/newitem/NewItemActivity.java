package com.example.closetwear.newitem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.closetwear.GlideApp;
import com.example.closetwear.R;

import org.parceler.Parcels;

public class NewItemActivity extends AppCompatActivity {

    private ImageView itemImg;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        itemImg = findViewById(R.id.itemImg);
        addBtn = findViewById(R.id.addBtn);

        GlideApp.with(this).load(getIntent().getSerializableExtra("image")).into(itemImg);
    }
}