package com.peruzal.newsassistant.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.peruzal.newsassistant.data.local.AppDatabase;
import com.peruzal.newsassistant.data.remote.Constants;
import com.peruzal.newsassistant.data.remote.RemoteArticleDataSource;
import com.peruzal.newsassistant.data.remote.SourcesDataSource;
import com.peruzal.newsassistant.models.Article;
import com.peruzal.newsassistant.models.ArticlesResult;
import com.peruzal.newsassistant.models.SourcesResult;

import java.util.List;
import java.util.Map;

public class DataRepository {

    private SourcesDataSource sourcesDataSource;
    private RemoteArticleDataSource articlesDataSource;
    private MediatorLiveData<ArticlesResult> articlesStreamsMerger = new MediatorLiveData<>();
    private AppDatabase appDatabase;

    public DataRepository(SourcesDataSource sourcesDataSource, RemoteArticleDataSource articlesDataSource,
                          AppDatabase appDatabase) {
        this.sourcesDataSource = sourcesDataSource;
        this.articlesDataSource = articlesDataSource;
        this.appDatabase = appDatabase;
    }

    public LiveData<ArticlesResult> getArticlesStream() {
        articlesStreamsMerger.addSource(
                articlesDataSource.getDataStream(),
                articlesResult -> articlesStreamsMerger.postValue(articlesResult));
        return articlesStreamsMerger;
    }

    private void fetchArticlesFromRemote(Map<String, String> params) {
       articlesDataSource.fetch(params);
    }

    private void fetchArticlesFromLocal() {
        articlesStreamsMerger.addSource(appDatabase.articlesDAO().getAll(), articles -> {
            ArticlesResult articlesResult = new ArticlesResult();
            articlesResult.status = "ok";
            articlesResult.articles = articles;
            articlesStreamsMerger.postValue(articlesResult);
        });
    }

    public void fetchArticles(Map<String, String> params, String source) {
        if (source.equals(Constants.LOCAL)) {
            fetchArticlesFromLocal();
        }else {
            fetchArticlesFromRemote(params);
        }
    }

    public void fetchSources() {
        sourcesDataSource.fetch();
    }

    public LiveData<SourcesResult> getSourcesStream() {
        return sourcesDataSource.getDataStream();
    }


}
