package com.mbadasoft.newsassistant.data;

import androidx.lifecycle.LiveData;

import com.mbadasoft.newsassistant.models.ArticlesResult;
import com.mbadasoft.newsassistant.models.SourcesResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataController {

    private NewsRepository newsRepository;
    private PreferencesRepository preferencesRepository;
    private Map<String, Object> userPreferences = new HashMap<>();

    public DataController(NewsRepository newsRepository, PreferencesRepository preferencesRepository) {
        this.newsRepository = newsRepository;
        this.preferencesRepository = preferencesRepository;
        loadUserPrefs();
    }

    private void loadUserPrefs() {
        userPreferences.put("Sources", preferencesRepository.getPreferredSources());
        userPreferences.put("Load_limit", preferencesRepository.getArticlesLoadingLimit());
        userPreferences.put("Categories", preferencesRepository.getPreferredCategories());
    }

    public LiveData<ArticlesResult> getArticles(String fragmentId) {
        return newsRepository.getArticles(fragmentId, userPreferences);
    }

    public LiveData<SourcesResult> getSources() {
        return newsRepository.getSources();
    }

    public boolean IsFirstTimeLogin() {
        return preferencesRepository.getIsFirstTimeLogin();
    }

    public void setIsFirstTimeLogin(boolean value) {
        preferencesRepository.saveisFirstTimeLogin(value);
    }


    public void savePreferredSources(Set<String> sources) {
        if (!sources.isEmpty()) {
            preferencesRepository.savePreferredSources(sources);
        }
    }

    public void savePreferredCategories(Set<String> sources) {
        if (!sources.isEmpty()) {
            preferencesRepository.savePreferredSources(sources);
        }
    }
}
