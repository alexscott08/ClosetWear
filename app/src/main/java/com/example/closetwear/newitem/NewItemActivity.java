package com.example.closetwear.newitem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.closetwear.ClothingPost;
import com.example.closetwear.GlideApp;
import com.example.closetwear.Navigation;
import com.example.closetwear.R;
import com.google.android.material.tabs.TabLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;


public class NewItemActivity extends AppCompatActivity implements CategoryFragment.OnItemSelectedListener,
        SubcategoryFragment.OnItemTypedListener, BrandFragment.OnItemTypedListener,
        NameFragment.OnItemTypedListener, ColorFragment.OnItemTypedListener {

    public static final String TAG = "NewItemActivity";
    private ImageView itemImg;
    private Button addBtn;
    private String category;
    private String subcategory;
    private String brand;
    private String name;
    private String color;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    NewItemTabAdapter adapter;
    CategoryFragment categoryFragment;
    SubcategoryFragment subcategoryFragment;
    BrandFragment brandFragment;
    NameFragment nameFragment;
    ColorFragment colorFragment;
    File image;
    ClothingPost item = new ClothingPost();

    public NewItemActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        itemImg = findViewById(R.id.itemImg);
        addBtn = findViewById(R.id.addBtn);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.pager);
        image = (File) getIntent().getSerializableExtra("image");
        GlideApp.with(this).load(image).into(itemImg);

        adapter = new NewItemTabAdapter(getSupportFragmentManager(),
                getLifecycle());
        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        categoryFragment = new CategoryFragment();
        subcategoryFragment = new SubcategoryFragment();
        brandFragment = new BrandFragment();
        nameFragment = new NameFragment();
        colorFragment = new ColorFragment();

        // If all fields have been filled, uploads new ClothingPost instance to Parse
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**TODO: ensure all info is filled out by passing fragment info back to activity**/
                if (item.isFilled()) {
                    saveParseFile(image);
                    item.setUser(ParseUser.getCurrentUser());
                    item.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "Error while saving", e);
                                Toast.makeText(NewItemActivity.this, "Error while saving item!", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(NewItemActivity.this, "New item added to closet!", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "New item added to closet!");
                            Navigation.goMainActivity(NewItemActivity.this);
                        }
                    });
                }
            }
        });
    }

    // Adds item picture to parse server in background
    private void saveParseFile(final File file) {
        final ParseFile img = new ParseFile(file);
        img.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error saving image: ", e);
                    Toast.makeText(NewItemActivity.this, "Error with saving image.", Toast.LENGTH_SHORT).show();
                }
                item.put("image", img);
            }
        });
    }

    /** Sets information to ClothingPost obj when listener is fired with action on each fragment **/
    @Override
    public void onCategoryItemSelected(String link) {
        if (categoryFragment != null && tabLayout.getSelectedTabPosition() == 0) {
            item.setCategory(link);
        }
    }

    @Override
    public void onSubcategoryItemTyped(String link) {
        if (subcategoryFragment != null && tabLayout.getSelectedTabPosition() == 1) {
            item.setSubcategory(link);
        }
    }

    @Override
    public void onBrandItemTyped(String link) {
        if (brandFragment != null && tabLayout.getSelectedTabPosition() == 2) {
            item.setBrand(link);
        }
    }

    @Override
    public void onNameItemTyped(String link) {
        if (nameFragment != null && tabLayout.getSelectedTabPosition() == 3) {
            item.setName(link);
        }
    }

    @Override
    public void onColorItemTyped(String link) {
        if (colorFragment != null && tabLayout.getSelectedTabPosition() == 4) {
            item.setColor(link);
        }
    }

}