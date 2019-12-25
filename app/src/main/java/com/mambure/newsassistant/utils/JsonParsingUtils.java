package com.mambure.newsassistant.utils;

import android.util.Log;

import com.mambure.newsassistant.models.Article;
import com.mambure.newsassistant.models.Source;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Rfc3339DateJsonAdapter;
import com.squareup.moshi.Types;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JsonParsingUtils {
    public static final String TAG = JsonParsingUtils.class.getSimpleName();

    public static List<Article> parseArticlesJson(JSONObject json) {
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(List.class, Article.class);
        JsonAdapter<List<Article>> jsonAdapter = moshi.adapter(type);
        List<Article> articles = new ArrayList<>();

        try {
            JSONArray jsonArray = json.getJSONArray("articles");
            articles = jsonAdapter.fromJson(jsonArray.toString());
            Log.d(TAG, "PARSED ARTICLE JSON DATA: \n" + articles.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return articles;
    }

    public static List<Source> parseSourcesJson(JSONObject json) {
        Moshi moshi = new Moshi.Builder().add(Date.class, new Rfc3339DateJsonAdapter()).build();
        Type type = Types.newParameterizedType(List.class, Source.class);
        JsonAdapter<List<Source>> jsonAdapter = moshi.adapter(type);
        List<Source> sources = new ArrayList<>();

        try {
            JSONArray jsonArray = json.getJSONArray("sources");
            sources = jsonAdapter.fromJson(jsonArray.toString());
            Log.d(TAG, "PARSED SOURCES DATA: \n" + sources.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return sources;
    }
}
