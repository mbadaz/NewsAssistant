package com.peruzal.newsassistant.data.remote;

import com.peruzal.newsassistant.Constants;
import com.peruzal.newsassistant.data.models.ArticlesResult;
import com.peruzal.newsassistant.data.models.SourcesResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

public interface NewsService {

    @Headers("X-Api-Key: df66e036a5e14417b37c2cb31d5034f3")
    @GET(Constants.SOURCES_API_ENDPOINT)
    Call<SourcesResult> getSources();

    @GET(Constants.HEADLINES_API_ENDPOINT)
    Call<ArticlesResult> getArticles(@QueryMap Map<String, String> params);

}
