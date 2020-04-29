package com.mambure.newsAssistant.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.local.LocalRepositoryDAO;
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
public class DataRepository {
    private NewsService newsRepository;
    private LocalRepositoryDAO localRepositoryDAO;
    private Observable<ArticlesResult> articlesStreamsMerger;
    private Observable<SourcesResult> sourcesStreamsMerger ;
    private LiveData<List<Source>> localSourcesStream = new MutableLiveData<>();
    private LiveData<List<Article>> localArticlesStream = new MutableLiveData<>();
    private MutableLiveData<Boolean> isBusyStatusStream = new MutableLiveData<>(false);
    private List<Source> preferredSources = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public DataRepository(LocalRepositoryDAO localRepositoryDAO, NewsService newsService) {
        this.newsRepository = newsService;
        this.localRepositoryDAO = localRepositoryDAO;
        initializeData();
    }

    private void initializeData() {
       compositeDisposable.add(
               localRepositoryDAO.getAllSources().
                       subscribeOn(Schedulers.io()).
                       subscribe(sources -> preferredSources.addAll(sources))
       );
    }

    public LiveData<Boolean> getIsBusyStatusStream() {
        return isBusyStatusStream;
    }


    public Maybe<ArticlesResult> fetchArticlesFromRemote(Map<String, String> params) {
        final Map<String, String> params2 = new HashMap<>(params);
        params2.put(Constants.SOURCES, ParsingUtils.createStringList(preferredSources));
        return newsRepository.getArticles(params2);
    }

    public Flowable<List<Article>> fetchArticlesFromLocal() {
        return localRepositoryDAO.getAllArticles();
    }

    public void saveArticle(Article article) {
        localRepositoryDAO.insertArticle(article);
    }

    public Completable deleteArticle(Article article) {
       return localRepositoryDAO.deleteArticle(article);
    }

    public Flowable<List<Source>> fetchSourcesFromLocal() {
       return localRepositoryDAO.getAllSources();
    }

    public Observable<SourcesResult> fetchSourcesFromRemote() {
       return newsRepository.getSources();
    }

    public Completable saveSources(List<Source> sources) {
        return localRepositoryDAO.insertSources(sources);
    }

    public Completable deleteSource(Source source) {
        return localRepositoryDAO.deleteSource(source);
    }

    public void cleanUp() {
        compositeDisposable.clear();
    }
}
