package com.peruzal.newsassistant.data.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.peruzal.newsassistant.data.DataSource;
import com.peruzal.newsassistant.data.models.ArticlesResult;
import com.peruzal.newsassistant.data.models.SourcesResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteDataSource implements DataSource<LiveData<ArticlesResult>>,
        Callback<ArticlesResult> {

    private NewsService newsService;
    private MutableLiveData<ArticlesResult> articlesStream = new MutableLiveData<>();
    private MutableLiveData<SourcesResult> sourcesStream = new MutableLiveData<>();

    public RemoteDataSource(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public LiveData<ArticlesResult> getDataStream() {
        return articlesStream;
    }

    public void fetchArticles(Map<String, String> params) {
        Call<ArticlesResult> call = newsService.getArticles(params);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ArticlesResult> call, Response<ArticlesResult> response) {
        ArticlesResult articlesResult = new ArticlesResult();
        if (response.isSuccessful()) {
            articlesResult = response.body();
            articlesStream.postValue(articlesResult);
        }else {
            articlesResult.status = "error";
            articlesStream.postValue(articlesResult);
        }
    }

    @Override
    public void onFailure(Call<ArticlesResult> call, Throwable t) {
        ArticlesResult articlesResult = new ArticlesResult();
        articlesResult.status = "error";
        articlesStream.postValue(articlesResult);
    }
}
