package com.mbadasoft.newsassistant.data;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mbadasoft.newsassistant.models.Article;
import com.mbadasoft.newsassistant.models.ArticlesResult;
import com.mbadasoft.newsassistant.models.SourcesResult;
import com.mbadasoft.newsassistant.utils.JsonParsingUtils;
import com.mbadasoft.newsassistant.utils.RequestUriBuilderUtil;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppNewsRepository implements NewsRepository, Response.Listener<JSONObject>, Response.ErrorListener {

    public static final String TAG = AppNewsRepository.class.getSimpleName();
    private RequestQueue requestQueue;
    private MutableLiveData<ArticlesResult> articlesResultMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<SourcesResult> sourcesResultMutableLiveData = new MutableLiveData<>();
    private static Map<String, List<Article>>  dataCache = new HashMap<>();
    private String currentId;
    private UpdatesListener updatesListener;

    public AppNewsRepository(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.addRequestFinishedListener( request -> {
            request.getCacheEntry();

                });
    }

    @Override
    public LiveData<SourcesResult> getSources() {
        try {
            List<String> requestUrls = RequestUriBuilderUtil.buildUris(Constants.SOURCES_API_ENDPOINT, null);
            for (String url : requestUrls) {
                requestQueue.add(createRequest(url));
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return sourcesResultMutableLiveData;
    }

    @Override
    public void setUpdatesListener(UpdatesListener listener) {
        this.updatesListener = listener;
    }

    @Override
    public void getArticles(String id, Map<String, Object> args) {

        try {
            List<String> requestUrls = RequestUriBuilderUtil.buildUris(mapFragmentToApiEndpoint(id), args);
            for (String url : requestUrls) {
                MutableLiveData<ArticlesResult> liveData = new MutableLiveData<>();

                if (!dataCache.containsKey(url)) {
                    Request<JSONObject> request = requestQueue.add(
                            createRequest(url)
                    );

                    request.setRequestQueue(requestQueue);

                    request.addMarker(url);
                    List<Article> requestResponse = new ArrayList<>();
                    //dataCache.put(url, requestResponse);

                    request.setTag(requestResponse);

                } else {
                    updatesListener.onArticlesUpdate(dataCache.get(url));
                }

            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }


    private void reloadCacheItem(Map<String, MutableLiveData<ArticlesResult>> requestResult, String id) {
        Request<JSONObject> request = requestQueue.add(createRequest(id));



    }

    private String mapFragmentToApiEndpoint(String fragment) {
        switch (fragment) {
            default:
                return Constants.HEADLINES_API_ENDPOINT;
        }
    }

    private JsonObjectRequest createRequest(String url) {
        return new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this,this
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-Api-Key", "df66e036a5e14417b37c2cb31d5034f3");
                return headers;
            }
        };
    }

    private void parseJson(JSONObject response, String responseId) {
        if (response.has("sources")) {
            updatesListener.onSourcesUpdate(JsonParsingUtils.parseSourcesJson(response));
            Log.d(TAG, "UPDATED WITH SOURCES DATA");
        } else {
            updatesListener.onArticlesUpdate(JsonParsingUtils.parseArticlesJson(response));
            Log.d(TAG, "UPDATED WITH ARTICLES DATA");
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.d(TAG, "RESPONSE FROM API SERVER: \n" + response.toString());
        parseJson(response, "Response");
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d(TAG, error.toString());
        ArticlesResult result = new ArticlesResult();
        result.status = "error";
    }


}
