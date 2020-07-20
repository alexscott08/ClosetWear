package com.example.closetwear;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.Date;

@ParseClassName("OutfitPost")
public class OutfitPost extends ParseObject {
    public static final String  KEY_USER = "user";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_COMMENTS = "commentsCount";
    public static final String KEY_CAPTION = "caption";
    public static final String KEY_LIKES = "likesCount";
    public static final String KEY_CREATED_KEY = "createdAt";
    public static final String KEY_ITEMS = "fitItems";

    public OutfitPost() { }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public int getCommentsCount() {
        return (int) getNumber(KEY_COMMENTS);
    }

    public void setCommentsCount(String comments) {
        put(KEY_COMMENTS, comments);
    }

    public String getCaption() {
        return getString(KEY_CAPTION);
    }

    public void setCaption(String caption) {
        put(KEY_CAPTION, caption);
    }

    public int getLikesCount() {
        return (int) getNumber(KEY_LIKES);
    }

    public void setLikesCount(int likes) {
        put(KEY_LIKES, (Number) likes);
    }

    public JSONArray getFitItems() { return getJSONArray(KEY_ITEMS); }

    public void setFitItems(ClothingPost item) { put(KEY_ITEMS, item); }
}
