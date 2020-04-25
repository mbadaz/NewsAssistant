package com.mambure.newsAssistant.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.local.LocalRepositoryDAO;
import com.mambure.newsAssistant.data.models.Article;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.Source;
import com.mambure.newsAssistant.data.models.SourcesResult;
import com.mambure.newsAssistant.data.remote.RemoteRepository;
import com.mambure.newsAssistant.utils.ParsingUtils;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataRepository {
    private RemoteRepository newsRepository;
    private LocalRepositoryDAO localRepositoryDAO;
    private MediatorLiveData<ArticlesResult> articlesStreamsMerger = new MediatorLiveData<>();
    private MediatorLiveData<SourcesResult> sourcesStreamsMerger = new MediatorLiveData<>();
    private LiveData<List<Source>> localSourcesStream = new MutableLiveData<>();
    private LiveData<List<Article>> localArticlesStream = new MutableLiveData<>();
    private MutableLiveData<Boolean> isBusyStatusStream = new MutableLiveData<>(false);
    private List<Source> preferredSources = new ArrayList<>();

    @Inject
    public DataRepository(LocalRepositoryDAO localRepositoryDAO, RemoteRepository remoteRepository) {
        this.newsRepository = remoteRepository;
        this.localRepositoryDAO = localRepositoryDAO;
        initializeData();
    }

    private void initializeData() {
        localSourcesStream = localRepositoryDAO.getAllSources();
        sourcesStreamsMerger.addSource(
                localSourcesStream, sources -> {
                    preferredSources.addAll(sources);
                    SourcesResult result = new SourcesResult();
                    result.status = Constants.RESULT_OK;
                    result.sources = sources;
                    sourcesStreamsMerger.postValue(result);
                    isBusyStatusStream.postValue(false);
                });

        sourcesStreamsMerger.addSource(newsRepository.getSourceStream(),
                sourcesResult -> {
            sourcesStreamsMerger.postValue(sourcesResult);
            isBusyStatusStream.postValue(false);
        });

        localArticlesStream = localRepositoryDAO.getAllArticles();
        articlesStreamsMerger.addSource(localArticlesStream, articles -> {
            ArticlesResult result = new ArticlesResult();
            result.status = Constants.RESULT_OK;
            result.articles = articles;
            articlesStreamsMerger.postValue(result);
        });

        articlesStreamsMerger.addSource(newsRepository.getArticleStream(),
                articlesResult -> {
            articlesStreamsMerger.postValue(articlesResult);
        });

    }

    public LiveData<Boolean> getIsBusyStatusStream() {
        return isBusyStatusStream;
    }

    public LiveData<ArticlesResult> getArticlesStream() {
        return articlesStreamsMerger;
    }

    public void fetchArticles(String source, Map<String, String> params) {
        if (source.equals(Constants.LOCAL)) {
            fetchArticlesFromLocal();
        }else {
            final Map<String, String> params2 = new HashMap<>(params);
            params.put(Constants.SOURCES, ParsingUtils.createStringList(preferredSources));
            fetchArticlesFromRemote(params2);
        }
    }

    private void fetchArticlesFromRemote(Map<String, String> params) {
       final Map<String, String> requestParams = new HashMap<>(params);
        requestParams.put(Constants.SOURCES, ParsingUtils.createStringList(preferredSources));
       newsRepository.fetchArticles(requestParams);

    }

    private void fetchArticlesFromLocal() {
       localArticlesStream = localRepositoryDAO.getAllArticles();
    }

    public void saveArticle(Article article) {
        localRepositoryDAO.insertArticle(article);
    }

    public void deleteArticle(Article article) {
        localRepositoryDAO.deleteArticle(article);
    }

    public LiveData<SourcesResult> getSourcesStream() {
        return sourcesStreamsMerger;
    }

    public void fetchSources(String source) {
        if (source.equals(Constants.LOCAL)) {
            fetchSourcesFromLocal();
            isBusyStatusStream.setValue(true);
        } else {
            fetchSourcesFromRemote();
        }

    }

    private void fetchSourcesFromLocal() {
        localSourcesStream = localRepositoryDAO.getAllSources();
    }

    private void fetchSourcesFromRemote() {
        newsRepository.fetchSources();
    }

    public void saveSources(List<Source> sources) {
        new Thread(() -> localRepositoryDAO.insertSources(sources)).start();
    }

    public void deleteSource(Source source) {
        localRepositoryDAO.deleteSource(source);
    }
}
