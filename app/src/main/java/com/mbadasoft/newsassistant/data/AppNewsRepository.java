package com.mbadasoft.newsassistant.data;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.mbadasoft.newsassistant.models.ArticlesResult;
import com.mbadasoft.newsassistant.models.SourcesResult;
import com.mbadasoft.newsassistant.utils.JsonObjectRequestUtils;
import com.mbadasoft.newsassistant.utils.JsonParsingUtils;

import org.json.JSONObject;

import java.net.URI;

public class AppNewsRepository implements NewsRepository, Response.Listener<JSONObject>, Response.ErrorListener {
    public static final String TAG = AppNewsRepository.class.getSimpleName();
    public static final String SOURCES_API_ENDPOINT = "https://newsapi.org/v2/sources";
    public static final String HEADLINES_API_ENDPOINT = "https://newsapi.org/v2/top-headlines?language=en";
    public static final String EVERYTHING_API_ENDPOINT = "https://newsapi.org/v2/everything";
    private RequestQueue requestQueue;
    private URI requesUri;
    private MutableLiveData<ArticlesResult> articlesResultMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<SourcesResult> sourcesResultMutableLiveData = new MutableLiveData<>();
    private static AppNewsRepository INSTANCE = null;



    private AppNewsRepository(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public static AppNewsRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppNewsRepository(context);
        }
        return INSTANCE;
    }

    @Override
    public LiveData<SourcesResult> getSources() {
        requestQueue.add(JsonObjectRequestUtils.
                createJsonObjectRequest(SOURCES_API_ENDPOINT,this, this));
        return sourcesResultMutableLiveData;
    }

    @Override
    public LiveData<ArticlesResult> getArticles() {
        requestQueue.add(JsonObjectRequestUtils.
                createJsonObjectRequest(HEADLINES_API_ENDPOINT,this, this::onErrorResponse));
        return articlesResultMutableLiveData;
    }

    private void parseJson(JSONObject response) {
        if (response.has("sources")) {
            sourcesResultMutableLiveData.setValue(JsonParsingUtils.parseSourcesJson(response));
            Log.d(TAG, "UPDATED WITH SOURCES DATA");
        } else {
            articlesResultMutableLiveData.setValue(JsonParsingUtils.parseArticlesJson(response));
            Log.d(TAG, "UPDATED WITH ARTICLES DATA");
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.d(TAG, "RESPONSE FROM API SERVER: \n" + response.toString());
        parseJson(response);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d(TAG, error.toString());
    }


}
