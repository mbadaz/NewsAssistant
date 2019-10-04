package com.mbadasoft.newsassistant.utils;

import android.util.Log;

import com.mbadasoft.newsassistant.models.Article;
import com.mbadasoft.newsassistant.models.ArticlesResult;
import com.mbadasoft.newsassistant.models.Source;
import com.mbadasoft.newsassistant.models.SourcesResult;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Rfc3339DateJsonAdapter;
import com.squareup.moshi.Types;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class JsonParsingUtils {
    public static final String TAG = JsonParsingUtils.class.getSimpleName();

    public static ArticlesResult parseArticlesJson(JSONObject json) {
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(List.class, Article.class);
        JsonAdapter<List<Article>> jsonAdapter = moshi.adapter(type);
        ArticlesResult articlesResult = new ArticlesResult();

        try {
            JSONArray jsonArray = json.getJSONArray("articles");
            articlesResult.articles = jsonAdapter.fromJson(jsonArray.toString());
            articlesResult.status = json.getString("status");
            Log.d(TAG, "PARSED ARTICLE JSON DATA: \n" + articlesResult.articles.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return articlesResult;
    }

    public static SourcesResult parseSourcesJson(JSONObject json) {
        Moshi moshi = new Moshi.Builder().add(Date.class, new Rfc3339DateJsonAdapter()).build();
        Type type = Types.newParameterizedType(List.class, Source.class);
        JsonAdapter<List<Source>> jsonAdapter = moshi.adapter(type);
        SourcesResult sourcesResult = new SourcesResult();

        try {
            JSONArray jsonArray = json.getJSONArray("sources");
            sourcesResult.sources = jsonAdapter.fromJson(jsonArray.toString());
            sourcesResult.status = json.getString("status");
            Log.d(TAG, "PARSED SOURCES DATA: \n" + sourcesResult.sources.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return sourcesResult;
    }
}
