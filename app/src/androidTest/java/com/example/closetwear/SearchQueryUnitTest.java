package com.example.closetwear;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.closetwear.parse.ClothingPost;
import com.example.closetwear.parse.OutfitPost;
import com.example.closetwear.search.SearchQuery;
import com.example.closetwear.search.SearchViewFragment;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SearchQueryUnitTest {

    @Before
    public void setup() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ParseObject.registerSubclass(ClothingPost.class);
        ParseObject.registerSubclass(OutfitPost.class);

        // Use for troubleshooting -- remove this line for production
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        // Use for monitoring Parse OkHttp traffic
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(appContext)
                .applicationId("alex-closetwear") // should correspond to APP_ID env variable
                .clientKey("personalProject")  // set explicitly unless clientKey is explicitly configured on Parse server
                .clientBuilder(builder)
                .server("https://alex-closetwear.herokuapp.com/parse").build());
    }

    @Test
    public void testQuerySearch() {
        Set<String> queryTest = SearchQuery.querySearch();
        assertNotNull(queryTest);
        assertEquals(2, queryTest.size());
        assertTrue(queryTest.contains("XpMBD1kx0y"));
        assertTrue(queryTest.contains("sO3JqmvYDR"));
    }

    @Test
    public void testQueryItem() {
        Set<String> queryTest = SearchQuery.queryItem(SearchQuery.querySearch(), "jeans");
        assertNotNull(queryTest);
        assertEquals(2, queryTest.size());
        assertTrue(queryTest.contains("1HrvOBSvig"));
        assertTrue(queryTest.contains("EnGTjsWX97"));
    }

    @Test
    public void testQueryFits() {
        Set<String> fitIds = SearchQuery.queryItem(SearchQuery.querySearch(), "jeans");
        List<OutfitPost> queryTest = SearchQuery.queryFits(fitIds, new SearchViewFragment());
        assertNotNull(queryTest);
        assertEquals(2, queryTest.size());
        OutfitPost post1 = queryTest.get(0);
        assertNotNull(post1);
        String actualUser1 = null;
        try {
            actualUser1 = post1.getUser().fetchIfNeeded().getUsername();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals("josh", actualUser1);
        assertEquals(0, post1.getCommentsCount());
        assertEquals(0, post1.getLikesCount());
        JSONArray fitItems1 = new JSONArray();
        fitItems1.put("sO3JqmvYDR");
        assertEquals(fitItems1, post1.getFitItems());

        OutfitPost post2 = queryTest.get(1);
        String actualUser2 = null;
        try {
            actualUser2 = post2.getUser().fetchIfNeeded().getUsername();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals("alex", actualUser2);
        assertEquals(0, post2.getCommentsCount());
        assertEquals(0, post2.getLikesCount());
        JSONArray fitItems2 = new JSONArray();
        fitItems2.put("XpMBD1kx0y");
        assertEquals(fitItems2, post2.getFitItems());
    }
}