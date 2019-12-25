package com.mambure.newsassistant.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mambure.newsassistant.models.Article;
import com.mambure.newsassistant.models.Source;
import com.mambure.newsassistant.models.SourcesResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

public class DataController implements UpdatesListener {

    private NewsRepository newsRepository;
    private SettingsRepository settingsRepository;
    private Map<String, ?> userPreferences = new HashMap<>();
    private MutableLiveData<List<Article>> articlesLiveData = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<SourcesResult> sourcesLiveData = new MutableLiveData<>();

    @Inject
    public DataController(NewsRepository newsRepository, SettingsRepository settingsRepository) {
        this.newsRepository = newsRepository;
        newsRepository.setUpdatesListener(this);
        this.settingsRepository = settingsRepository;
        loadUserPrefs();
    }

    private void loadUserPrefs() {
        settingsRepository.loadPreferences();
//        userPreferences.put(SOURCES_QUERY_PARAM, settingsRepository.getPreferredSources());
//        userPreferences.put(PAGE_SIZE_QUERY_PARAM, settingsRepository.getArticlesLoadingLimit());
//        userPreferences.put(CATEGORY_QUERY_PARAM, settingsRepository.getPreferredCategories());
//        userPreferences.put(LANGUAGE_QUERY_PARAM, settingsRepository.getPreferredLanguages());
        userPreferences = settingsRepository.getUserPrefs();
    }

    public LiveData<List<Article>> getArticles(String fragmentId) {
        newsRepository.getArticles(fragmentId, userPreferences);
        return articlesLiveData;
    }

    public LiveData<SourcesResult> getSources() {
        newsRepository.getSources();
        return sourcesLiveData;
    }

    public boolean IsFirstTimeLogin() {
        return settingsRepository.getIsFirstTimeLaunch();
    }

    public void setIsFirstTimeLogin(boolean value) {
        settingsRepository.isFirstTimeLaunch(value);
    }


    public void savePreferredSources(Set<String> sources) {
        if (!sources.isEmpty()) {
            settingsRepository.savePreferredSources(sources);
        }
    }

    public void savePreferredCategories(Set<String> sources) {
        if (!sources.isEmpty()) {
            settingsRepository.savePreferredCategories(sources);
        }
    }

    public void savePreferredLanguages(Set<String> languages) {
        if (!languages.isEmpty()) {
            settingsRepository.savePreferredLanguages(languages);
        }
    }

    @Override
    public void onArticlesUpdate(List<Article> articles) {
        List<Article> temp = articlesLiveData.getValue();
        temp.addAll(articles);
        articlesLiveData.setValue(articles);
    }

    @Override
    public void onSourcesUpdate(List<Source> sources) {
        SourcesResult result = new SourcesResult();
        result.sources = sources;
        result.status = "ok";
        sourcesLiveData.setValue(result);
    }
}
