package com.peruzal.newsassistant.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.peruzal.newsassistant.Constants;
import com.peruzal.newsassistant.data.local.AppDatabase;
import com.peruzal.newsassistant.data.models.Article;
import com.peruzal.newsassistant.data.models.ArticlesResult;
import com.peruzal.newsassistant.data.models.Source;
import com.peruzal.newsassistant.data.models.SourcesResult;
import com.peruzal.newsassistant.data.remote.ArticlesDataSource;
import com.peruzal.newsassistant.data.remote.SourcesDataSource;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class DataRepository {

    private SourcesDataSource sourcesDataSource;
    private ArticlesDataSource articlesDataSource;
    private AppDatabase appDatabase;
    private MediatorLiveData<ArticlesResult> articlesStreamsMerger = new MediatorLiveData<>();

    @Inject
    public DataRepository(SourcesDataSource sourcesDataSource, ArticlesDataSource articlesDataSource, AppDatabase appDatabase) {
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
       articlesDataSource.fetchArticles(params);
    }

    private void fetchArticlesFromLocal() {
        articlesStreamsMerger.addSource(appDatabase.localDatabaseDAO().getAllArticles(), articles -> {
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

    public void saveArticle(Article article) {
        appDatabase.localDatabaseDAO().insertArticle(article);
    }

    public void deleteArticle(Article article) {
        appDatabase.localDatabaseDAO().deleteArticle(article);
    }

    public void fetchSources() {
        sourcesDataSource.fetch();
    }

    public LiveData<SourcesResult> getSourcesStream() {
        return sourcesDataSource.getDataStream();
    }

    public void saveSources(List<Source> sources) {
        appDatabase.localDatabaseDAO().insertSources(sources);
    }

    public void deleteSource(Source source) {
        appDatabase.localDatabaseDAO().deleteSource(source);
    }


}
