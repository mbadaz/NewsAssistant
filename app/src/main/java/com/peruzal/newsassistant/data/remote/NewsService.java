package com.peruzal.newsassistant.data.remote;

import com.peruzal.newsassistant.data.ApiInfo;
import com.peruzal.newsassistant.models.ArticlesResult;
import com.peruzal.newsassistant.models.SourcesResult;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

public interface NewsService {

    @Headers("X-Api-Key: df66e036a5e14417b37c2cb31d5034f3")
    @GET(ApiInfo.SOURCES_API_ENDPOINT)
    Call<SourcesResult> getSources();

    @Headers("X-Api-Key: df66e036a5e14417b37c2cb31d5034f3")
    @GET(ApiInfo.HEADLINES_API_ENDPOINT)
    Call<ArticlesResult> getArticles(@QueryMap Map<String, String> params);
}
