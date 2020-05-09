package com.mambure.newsAssistant.data.remote;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.Source;
import com.mambure.newsAssistant.data.models.SourcesResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface NewsService {
    String API_KEY = "X-Api-Key: df66e036a5e14417b37c2cb31d5034f3";
    @Headers(API_KEY)
    @GET(Constants.SOURCES_API_ENDPOINT)
    Observable<SourcesResult> getSources();

    @Headers(API_KEY)
    @GET(Constants.HEADLINES_API_ENDPOINT)
    Observable<ArticlesResult> getArticles(@QueryMap Map<String, Object> params);

}
