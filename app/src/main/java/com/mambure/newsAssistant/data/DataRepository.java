package com.mambure.newsAssistant.data;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.local.LocalDataRepository;
import com.mambure.newsAssistant.data.models.Article;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.Source;
import com.mambure.newsAssistant.data.models.SourcesResult;
import com.mambure.newsAssistant.data.remote.NewsService;
import com.mambure.newsAssistant.utils.ParsingUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class DataRepository implements Repository {
    private static final String TAG = DataRepository.class.getSimpleName();
    private NewsService newsRepository;
    private LocalDataRepository localDataRepository;
    private List<Source> preferredSources = new ArrayList<>();
    private Maybe<ArticlesResult> articleObservable;

    @Inject
    public DataRepository(LocalDataRepository localDataRepository, NewsService newsService) {
        this.newsRepository = newsService;
        this.localDataRepository = localDataRepository;
    }

    @Override
    public Maybe<ArticlesResult> getArticles(Map<String, String> params, boolean update) {

        if (preferredSources.isEmpty()) {
            loadPreferredSources();
        }

        Map<String, String> extendedParams = new HashMap<>(params);
        extendedParams.put(Constants.SOURCES, ParsingUtils.createStringList(preferredSources));

        if (articleObservable == null) {
            articleObservable = newsRepository.getArticles(extendedParams).subscribeOn(Schedulers.io());
            return articleObservable;
        }

        if (update) {
            articleObservable = newsRepository.getArticles(extendedParams).subscribeOn(Schedulers.io());
        }

        return articleObservable;
    }

    private void loadPreferredSources() {
        preferredSources = localDataRepository.getSources().subscribeOn(Schedulers.io()).blockingGet();
    }

    @Override
    public Observable<List<Article>> getSavedArticles() {
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
    }
}
