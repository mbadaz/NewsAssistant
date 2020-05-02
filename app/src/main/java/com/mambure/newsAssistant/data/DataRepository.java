package com.mambure.newsAssistant.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class DataRepository implements DataManager {
    private NewsService newsRepository;
    private LocalDataRepository localDataRepository;
    private List<Source> preferredSources = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public DataRepository(LocalDataRepository localDataRepository, NewsService newsService) {
        this.newsRepository = newsService;
        this.localDataRepository = localDataRepository;
        initializeData();
    }

    private void initializeData() {
       compositeDisposable.add(
               localDataRepository.getAllSources().
                       subscribeOn(Schedulers.io()).
                       subscribe(sources -> preferredSources.addAll(sources))
       );
    }

    @Override
    public Maybe<ArticlesResult> fetchArticlesFromRemote(Map<String, String> params) {
        final Map<String, String> params2 = new HashMap<>(params);
        params2.put(Constants.SOURCES, ParsingUtils.createStringList(preferredSources));
        return newsRepository.getArticles(params2);
    }

    @Override
    public Flowable<List<Article>> fetchArticlesFromLocal() {
        return localDataRepository.getAllArticles();
    }

    @Override
    public void saveArticle(Article article) {
        localDataRepository.insertArticle(article);
    }

    @Override
    public Completable deleteArticle(Article article) {
       return localDataRepository.deleteArticle(article);
    }

    @Override
    public Flowable<List<Source>> fetchSourcesFromLocal() {
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
    public Completable deleteSource(Source source) {
        return localDataRepository.deleteSource(source);
    }

    @Override
    public void cleanUp() {
        compositeDisposable.clear();
    }
}
