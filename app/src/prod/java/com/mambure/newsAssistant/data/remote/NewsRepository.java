package com.mambure.newsAssistant.data.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.SourcesResult;

import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository implements RemoteRepository, Callback<ArticlesResult> {
    private NewsService newsService;
    private MutableLiveData<ArticlesResult> articlesStream = new MutableLiveData<>();
    private MutableLiveData<SourcesResult> sourcesStream = new MutableLiveData<>();

    @Inject
    public NewsRepository(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public LiveData<ArticlesResult> getArticleStream() {
        return articlesStream;
    }

    @Override
    public LiveData<SourcesResult> getSourceStream() {
        return sourcesStream;
    }

    @Override
    public void fetchArticles(Map<String, String> params) {
        Call<ArticlesResult> call = newsService.getArticles(params);
        call.enqueue(this);
    }

    @Override
    public void fetchSources() {
        Call<SourcesResult> call = newsService.getSources();
        call.enqueue(new Callback<SourcesResult>() {
            @Override
            public void onResponse(Call<SourcesResult> call, Response<SourcesResult> response) {
                if (response.isSuccessful()) {
                    sourcesStream.postValue(response.body());
                }else {
                    processError();
                }
            }

            @Override
            public void onFailure(Call<SourcesResult> call, Throwable t) {
                processError();
            }
        });
    }

    @Override
    public void onResponse(Call<ArticlesResult> call, Response<ArticlesResult> response) {
        if (response.isSuccessful()) {
            articlesStream.postValue(response.body());
        }else {
            processError();
        }
    }

    @Override
    public void onFailure(Call<ArticlesResult> call, Throwable t) {
        processError();
        t.printStackTrace();
    }

    private void processError() {
        ArticlesResult articlesResult = new ArticlesResult();
        articlesResult.status = Constants.RESULT_ERROR;
        SourcesResult sourcesResult = new SourcesResult();
        sourcesResult.status = Constants.RESULT_ERROR;
        sourcesStream.postValue(sourcesResult);
        articlesStream.postValue(articlesResult);
    }
}
