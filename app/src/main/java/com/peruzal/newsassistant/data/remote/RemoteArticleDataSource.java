package com.peruzal.newsassistant.data.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.peruzal.newsassistant.data.DataSource;
import com.peruzal.newsassistant.models.Article;
import com.peruzal.newsassistant.models.ArticlesResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteArticleDataSource implements DataSource<LiveData<ArticlesResult>>,
        Callback<List<Article>> {

    private NewsService newsService;
    private MutableLiveData<ArticlesResult> articlesStream = new MutableLiveData<>();

    public RemoteArticleDataSource(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public LiveData<ArticlesResult> getDataStream() {
        return articlesStream;
    }

    public void fetch(Map<String, String> params) {
        Call<List<Article>> call = newsService.getArticles(params);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
        ArticlesResult articlesResult = new ArticlesResult();
        if (response.isSuccessful()) {
            articlesResult.status = "ok";
            articlesResult.articles = response.body();
            articlesStream.postValue(articlesResult);
        }else {
            articlesResult.status = "error";
            articlesStream.postValue(articlesResult);
        }
    }

    @Override
    public void onFailure(Call<List<Article>> call, Throwable t) {
        ArticlesResult articlesResult = new ArticlesResult();
        articlesResult.status = "error";
        articlesStream.postValue(articlesResult);
    }
}
