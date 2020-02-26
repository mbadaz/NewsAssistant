package com.peruzal.newsassistant.data.remote;

import com.peruzal.newsassistant.models.Article;
import com.peruzal.newsassistant.models.Source;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface NewsService {
    @GET(Constants.SOURCES_API_ENDPOINT)
    Call<List<Source>> getSources();

    @GET(Constants.HEADLINES_API_ENDPOINT)
    Call<List<Article>> getArticles(@QueryMap Map<String, String> params);
}
