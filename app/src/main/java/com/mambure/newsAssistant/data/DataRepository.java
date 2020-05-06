package com.mambure.newsAssistant.data;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.local.LocalDataRepository;
import com.mambure.newsAssistant.data.models.Article;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.Source;
import com.mambure.newsAssistant.data.models.SourcesResult;
import com.mambure.newsAssistant.data.remote.NewsService;
import com.mambure.newsAssistant.utils.ParsingUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

@Singleton
public class DataRepository implements DataManager {
    private NewsService newsRepository;
    private LocalDataRepository localDataRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public DataRepository(LocalDataRepository localDataRepository, NewsService newsService) {
        this.newsRepository = newsService;
        this.localDataRepository = localDataRepository;
    }

    @Override
    public Observable<ArticlesResult> fetchArticlesFromRemote(Map<String, String> params, List<Source> preferredSources) {
        final Map<String, String> params2 = new HashMap<>(params);
        params2.put(Constants.SOURCES, ParsingUtils.createStringList(preferredSources));
        return newsRepository.getArticles(params2);
    }

    @Override
    public Flowable<List<Article>> fetchArticlesFromLocal() {
        return localDataRepository.getAllArticles();
    }

    @Override
    public Completable saveArticle(Article article) {
        return localDataRepository.saveArticle(article);
    }

    @Override
    public Maybe<Article> getArticleByTitle(String title) {
        return localDataRepository.getArticleByTitle(title);
    }

    @Override
    public Completable deleteArticle(Article article) {
       return localDataRepository.deleteArticle(article);
    }

    @Override
    public Maybe<List<Source>> fetchSourcesFromLocal() {
       return localDataRepository.getAllSources();
    }

    @Override
    public Observable<SourcesResult> fetchSourcesFromRemote() {
       return newsRepository.getSources();
    }

    @Override
    public Completable saveSources(List<Source> sources) {
        return localDataRepository.insertSources(sources);
    }

    @Override
    public Completable deleteSources(List<Source> sources) {
        return localDataRepository.deleteSources(sources);
    }

    @Override
    public void cleanUp() {
        compositeDisposable.clear();
    }
}
