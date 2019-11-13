package com.mbadasoft.newsassistant.data;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.mbadasoft.newsassistant.models.ArticlesResult;
import com.mbadasoft.newsassistant.models.SourcesResult;
import com.mbadasoft.newsassistant.newsActivity.NewsActivity;
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
    private static Map<String, MutableLiveData<ArticlesResult>>  dataCache = new HashMap<>();
    private String currentId;
    public AppNewsRepository(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }


    @Override
    public LiveData<SourcesResult> getSources() {
        requestQueue.add(JsonObjectRequestUtils.
                createJsonObjectRequest(ApiInfo.SOURCES_API_ENDPOINT, null, this, this));
        return sourcesResultMutableLiveData;
    }

    @Override
    public LiveData<ArticlesResult> getArticles(String id, Map<String, Object> args) {
        currentId = id;
        if (!dataCache.containsKey(id)) {
            MutableLiveData<ArticlesResult> liveData = new MutableLiveData<>();
            dataCache.put(id, liveData);
            Request<JSONObject> request = requestQueue.add(JsonObjectRequestUtils.
                    createJsonObjectRequest(mapFragmentToApiEndpoint(id), args, this, this));
            request.setTag(id);
            return liveData;
        } else {
            if (dataCache.get(id).getValue().status.equals("error")) {
                Request<JSONObject> request = requestQueue.add(JsonObjectRequestUtils.
                        createJsonObjectRequest(mapFragmentToApiEndpoint(id), args, this, this));
            }

            return dataCache.get(id);
        }

    }

    private String mapFragmentToApiEndpoint(String fragment) {
        switch (fragment) {
            case NewsActivity.FOLLOWED_STORIES_FRAGMENT:
                return ApiInfo.EVERYTHING_API_ENDPOINT;
            default:
                return ApiInfo.HEADLINES_API_ENDPOINT;
        }
    }

    private void parseJson(JSONObject response) {
        if (response.has("sources")) {
            sourcesResultMutableLiveData.setValue(JsonParsingUtils.parseSourcesJson(response));
            Log.d(TAG, "UPDATED WITH SOURCES DATA");
        } else {
            dataCache.get(currentId).setValue(JsonParsingUtils.parseArticlesJson(response));
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
        ArticlesResult result = new ArticlesResult();
        result.status = "error";
        dataCache.get(currentId).setValue(result);
    }


}
