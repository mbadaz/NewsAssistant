package com.mambure.newsAssistant.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.local.LocalRepositoryDAO;
import com.mambure.newsAssistant.data.models.Article;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.Source;
import com.mambure.newsAssistant.data.models.SourcesResult;
import com.mambure.newsAssistant.data.remote.RemoteRepository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class DataRepository {
    private RemoteRepository newsRepository;
    private LocalRepositoryDAO localRepositoryDAO;
    private MediatorLiveData<ArticlesResult> articlesStreamsMerger = new MediatorLiveData<>();

    @Inject
    public DataRepository(LocalRepositoryDAO localRepositoryDAO, RemoteRepository remoteRepository) {
        this.newsRepository = remoteRepository;
        this.localRepositoryDAO = localRepositoryDAO;
    }

    public LiveData<ArticlesResult> getArticlesStream() {
        articlesStreamsMerger.addSource(
                newsRepository.getArticleStream(),
                articlesResult -> articlesStreamsMerger.postValue(articlesResult));
        return articlesStreamsMerger;
    }

    private void fetchArticlesFromRemote(Map<String, String> params) {
       newsRepository.fetchArticles(params);
    }

    private void fetchArticlesFromLocal() {
        articlesStreamsMerger.addSource(localRepositoryDAO.getAllArticles(), articles -> {
            ArticlesResult articlesResult = new ArticlesResult();
            articlesResult.status = Constants.RESULT_OK;
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
        localRepositoryDAO.insertArticle(article);
    }

    public void deleteArticle(Article article) {
        localRepositoryDAO.deleteArticle(article);
    }

    public void fetchSources() {
        newsRepository.fetchSources();
    }

    public LiveData<SourcesResult> getSourcesStream() {
        return newsRepository.getSourceStream();
    }

    public void saveSources(List<Source> sources) {
        new Thread(() -> localRepositoryDAO.insertSources(sources)).start();
    }

    public void deleteSource(Source source) {
        localRepositoryDAO.deleteSource(source);
    }
}
