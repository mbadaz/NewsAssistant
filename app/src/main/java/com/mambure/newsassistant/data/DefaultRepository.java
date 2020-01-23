package com.mambure.newsassistant.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.mambure.newsassistant.models.Article;
import com.mambure.newsassistant.models.ArticlesResult;
import com.mambure.newsassistant.models.Source;
import com.mambure.newsassistant.models.SourcesResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class DefaultRepository implements UpdatesListener, DataRepository {

    private NewsProvider newsDataProvider;
    private DataRepository dataRepository;
    private MutableLiveData<List<Article>> articlesLiveData = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<SourcesResult> sourcesLiveData = new MutableLiveData<>();
    private final Map<String, Object> preferences = new HashMap<>();

    @Inject
    public DefaultRepository(NewsProvider newsProvider, DataRepository dataRepository) {
        this.newsDataProvider = newsProvider;
        this.dataRepository = dataRepository;
        newsProvider.setUpdatesListener(this);
        loadUserPrefs();
    }

    private void loadUserPrefs() {
        final LiveData<SourcesResult> sourcesResultLiveData = getSources();
        sourcesResultLiveData.observeForever(new Observer<SourcesResult>() {
            @Override
            public void onChanged(SourcesResult sourcesResult) {
                preferences.put(Constants.SOURCES_QUERY_PARAM, sourcesResult.sources);
                sourcesResultLiveData.removeObserver(this);
            }
        });

        preferences.put(Constants.CATEGORY_QUERY_PARAM, getPreferredCategories());
        preferences.put(Constants.LANGUAGE_QUERY_PARAM, getPreferredLanguages());
        preferences.put(Constants.PAGE_SIZE_QUERY_PARAM, getArticlesLoadingLimit());
    }

    public LiveData<ArticlesResult> getArticles(String fragmentId) {
        return getArticles(fragmentId, preferences);
    }

    @Override
    public LiveData<ArticlesResult> getArticles(@Nullable String id, @Nullable Map<String, Object> args) {
        if (id.equals(Constants.SAVED_STORIES_FRAGMENT)) {
           return dataRepository.getArticles(null, args);
        }
        return newsDataProvider.getArticles(id, args);
    }

    @Override
    public LiveData<SourcesResult> getSources() {
        newsDataProvider.getSources();
        return sourcesLiveData;
    }

    @Override
    public void savePreferredSources(Set<Source> sources) {
        dataRepository.savePreferredSources(sources);
    }

    @Override
    public void savePreferredCategories(Set<String> categories) {
        dataRepository.savePreferredCategories(categories);
    }

    @Override
    public void savePreferredLanguages(Set<String> languages) {
        dataRepository.savePreferredLanguages(languages);
    }

    @Override
    public void saveArticle(Article article) {
        dataRepository.saveArticle(article);
    }

    @Override
    public Set<String> getPreferredLanguages() {
        return dataRepository.getPreferredLanguages();
    }

    @Override
    public Set<String> getPreferredCategories() {
        return dataRepository.getPreferredCategories();
    }

    @Override
    public boolean getIsFirstTimeLaunch() {
        return dataRepository.getIsFirstTimeLaunch();
    }

    @Override
    public void isFirstTimeLaunch(boolean value) {
        dataRepository.isFirstTimeLaunch(value);
    }

    @Override
    public void setEnableExternalBrowser(boolean value) {
        dataRepository.setEnableExternalBrowser(value);
    }

    @Override
    public boolean isExternalBrowserEnabled() {
        return dataRepository.isExternalBrowserEnabled();
    }

    @Override
    public void setArticlesLoadingLimit(int value) {
        dataRepository.setArticlesLoadingLimit(value);
    }

    @Override
    public int getArticlesLoadingLimit() {
        return dataRepository.getArticlesLoadingLimit();
    }


    @Override
    public void setUpdatesListener(UpdatesListener listener) {

    }

    @Override
    public void onArticlesUpdate(List<Article> articles) {
        List<Article> temp = articlesLiveData.getValue();
        temp.addAll(articles);
        articlesLiveData.postValue(articles);
    }

    @Override
    public void onSourcesUpdate(List<Source> sources) {
        SourcesResult result = new SourcesResult();
        result.sources = sources;
        result.status = "ok";
        sourcesLiveData.postValue(result);
    }
}
