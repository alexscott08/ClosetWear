package com.example.closetwear;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.parceler.Parcels;

public class NewItemActivity extends AppCompatActivity {

    private ImageView itemImg;
    private Spinner category;
    private EditText subcategory;
    private EditText brand;
    private EditText itemName;
    private EditText color;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        itemImg = findViewById(R.id.itemImg);

        Spinner staticSpinner = (Spinner) findViewById(R.id.categorySpinner);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.category_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);
        category = findViewById(R.id.categorySpinner);
        subcategory = findViewById(R.id.subcat);
        brand = findViewById(R.id.brand);
        itemName = findViewById(R.id.itemName);
        color = findViewById(R.id.color);
        addBtn = findViewById(R.id.addBtn);

        GlideApp.with(this).load(getIntent().getSerializableExtra("image")).into(itemImg);
    }
}