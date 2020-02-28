package com.peruzal.newsassistant.data;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.room.Room;

import com.peruzal.newsassistant.data.local.AppDatabase;
import com.peruzal.newsassistant.Constants;
import com.peruzal.newsassistant.data.remote.NewsService;
import com.peruzal.newsassistant.data.remote.RemoteDataSource;
import com.peruzal.newsassistant.data.remote.SourcesDataSource;
import com.peruzal.newsassistant.data.models.Article;
import com.peruzal.newsassistant.data.models.ArticlesResult;
import com.peruzal.newsassistant.data.models.SourcesResult;

import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class DataRepository {

    private SourcesDataSource sourcesDataSource;
    private RemoteDataSource articlesDataSource;
    private MediatorLiveData<ArticlesResult> articlesStreamsMerger = new MediatorLiveData<>();
    private AppDatabase appDatabase;

    public DataRepository(Context context) {
        NewsService newsService = new Retrofit.Builder().
                addConverterFactory(MoshiConverterFactory.create()).
                baseUrl(Constants.BASE_URL).build().create(NewsService.class);
        this.sourcesDataSource = new SourcesDataSource(newsService);
        this.articlesDataSource = new RemoteDataSource(newsService);
        this.appDatabase = Room.databaseBuilder(context, AppDatabase.class, "app-database").build();
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

    public void saveArticle(Article article) {
        appDatabase.articlesDAO().insert(article);
    }

    public void deleteArticle(Article article) {
        appDatabase.articlesDAO().delete(article);
    }


}
