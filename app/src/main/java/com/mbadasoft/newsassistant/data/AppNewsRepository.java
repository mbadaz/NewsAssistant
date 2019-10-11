package com.mbadasoft.newsassistant.data;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mbadasoft.newsassistant.models.Article;
import com.mbadasoft.newsassistant.models.ArticlesResult;
import com.mbadasoft.newsassistant.models.SourcesResult;
import com.mbadasoft.newsassistant.utils.JsonObjectRequestUtils;
import com.mbadasoft.newsassistant.utils.JsonParsingUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AppNewsRepository implements NewsRepository, Response.Listener<JSONObject>, Response.ErrorListener {
    public static final String TAG = AppNewsRepository.class.getSimpleName();
    private RequestQueue requestQueue;
    private MutableLiveData<ArticlesResult> articlesResultMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<SourcesResult> sourcesResultMutableLiveData = new MutableLiveData<>();
    private static AppNewsRepository INSTANCE = null;
    private static Map<String, MutableLiveData<ArticlesResult>>  dataCache = new HashMap<>();
    private String currentEndpoint;

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
                createJsonObjectRequest(ApiInfo.SOURCES_API_ENDPOINT, null, this, this));
        return sourcesResultMutableLiveData;
    }

    @Override
    public LiveData<ArticlesResult> getArticles(Map<String, Object> args, String endpoint) {
        currentEndpoint = endpoint;
        if(endpoint.equals(ApiInfo.LOCAL)){

        }
        if (!dataCache.containsKey(endpoint)) {
            MutableLiveData<ArticlesResult> liveData = new MutableLiveData<>();
            dataCache.put(endpoint, liveData);
            Request<JSONObject> request = requestQueue.add(JsonObjectRequestUtils.
                    createJsonObjectRequest(endpoint, args, this, this));
            request.setTag(endpoint);
            return liveData;
        } else {
            return dataCache.get(endpoint);
        }


    }

    private void parseJson(JSONObject response) {
        if (response.has("sources")) {
            sourcesResultMutableLiveData.setValue(JsonParsingUtils.parseSourcesJson(response));
            Log.d(TAG, "UPDATED WITH SOURCES DATA");
        } else {
            dataCache.get(currentEndpoint).setValue(JsonParsingUtils.parseArticlesJson(response));
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
