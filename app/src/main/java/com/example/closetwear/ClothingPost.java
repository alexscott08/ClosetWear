package com.example.closetwear;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("ClothingPost")
public class ClothingPost extends ParseObject {
    public static final String KEY_NAME = "itemName";
    public static final String KEY_IMAGE = "image";
    public static final String  KEY_USER = "user";
    public static final String KEY_BRAND = "brand";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_SUBCATEGORY = "subcategory";
    public static final String KEY_SIZE = "size";
    public static final String KEY_COLOR = "color";
    public static final String KEY_CREATED_KEY = "createdAt";

    public ClothingPost() { }

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getBrand() {
        return getString(KEY_BRAND);
    }

    public void setBrand(String brand) {
        put(KEY_BRAND, brand);
    }

    public String getCategory() {
        return getString(KEY_CATEGORY);
    }

    public void setCategory(String category) {
        put(KEY_CATEGORY, category);
    }

    public String getSubcategory() {
        return getString(KEY_SUBCATEGORY);
    }

    public void setSubcategory(String subcategory) {
        put(KEY_SUBCATEGORY, subcategory);
    }

    public String getSize() {
        return getString(KEY_SIZE);
    }

    public void setSize(String size) {
        put(KEY_SIZE, size);
    }

    public String getColor() {
        return getString(KEY_COLOR);
    }

    public void setColor(String color) {
        put(KEY_COLOR, color);
    }

    // Helper function to determine if all required item fields have been set
    public boolean isFilled() {
        return (getName() != null && getCategory() != null &&
                getSubcategory() != null && getBrand() != null && getColor() != null);

    }
}
