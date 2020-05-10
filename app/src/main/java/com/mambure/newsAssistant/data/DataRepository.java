package com.mambure.newsAssistant.data;

import android.util.Log;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.local.LocalDataRepository;
import com.mambure.newsAssistant.data.models.Article;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.Source;
import com.mambure.newsAssistant.data.models.SourcesResult;
import com.mambure.newsAssistant.data.remote.NewsService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class DataRepository implements Repository {
    private static final String TAG = DataRepository.class.getSimpleName();
    private NewsService newsRepository;
    private LocalDataRepository localDataRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Set<Source> preferredSources = new HashSet<>();
    private Observable<ArticlesResult> articlesResultObservable;

    @Inject
    public DataRepository(LocalDataRepository localDataRepository, NewsService newsService) {
        this.newsRepository = newsService;
        this.localDataRepository = localDataRepository;
    }

    @Override
    public Observable<ArticlesResult> getNewArticles(Map<String, String> params) {
        if (preferredSources.isEmpty()) {
            List<Source> result = localDataRepository.getSources().subscribeOn(Schedulers.io()).blockingGet();
            preferredSources.addAll(result);
        }

        Map<String, Object> params2 = new HashMap<>(params);
        params2.put(Constants.SOURCES, preferredSources);
        return newsRepository.getArticles(params2);
    }

    @Override
    public Maybe<List<Article>> getSavedArticles() {
        return localDataRepository.getArticles();
    }

    @Override
    public Completable saveArticle(Article article) {
        return localDataRepository.saveArticle(article);
    }

    @Override
    public Observable<SourcesResult> getSources() {
        return newsRepository.getSources();
    }

    @Override
    public Maybe<Article> findSavedArticle(String title) {
        return localDataRepository.getArticleByTitle(title);
    }

    @Override
    public Completable deleteSavedArticle(Article article) {
       return localDataRepository.deleteArticle(article);
    }

    @Override
    public Maybe<List<Source>> getSavedSources() {
       return localDataRepository.getSources();
    }

    @Override
    public Completable saveSources(List<Source> sources) {
        return localDataRepository.saveSources(sources);
    }

    @Override
    public Completable deletePreferredSources(List<Source> sources) {
        return localDataRepository.deleteSources(sources);
    }

    @Override
    public void cleanUp() {
        preferredSources.clear();
        compositeDisposable.clear();
    }
}
