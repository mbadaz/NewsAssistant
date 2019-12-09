package com.mbadasoft.newsassistant.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mbadasoft.newsassistant.models.Article;
import com.mbadasoft.newsassistant.models.Source;
import com.mbadasoft.newsassistant.models.SourcesResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.mbadasoft.newsassistant.data.Constants.*;

public class DataController implements UpdatesListener {

    private NewsRepository newsRepository;
    private PreferencesRepository preferencesRepository;
    private Map<String, Object> userPreferences = new HashMap<>();
    private MutableLiveData<List<Article>> articlesLiveData = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<SourcesResult> sourcesLiveData = new MutableLiveData<>();


    public DataController(NewsRepository newsRepository, PreferencesRepository preferencesRepository) {
        this.newsRepository = newsRepository;
        newsRepository.setUpdatesListener(this);
        this.preferencesRepository = preferencesRepository;
        loadUserPrefs();
    }

    private void loadUserPrefs() {
        preferencesRepository.loadPreferences();
        userPreferences.put(SOURCES_QUERY_PARAM, preferencesRepository.getPreferredSources());
        userPreferences.put(PAGE_SIZE_QUERY_PARAM, preferencesRepository.getArticlesLoadingLimit());
        userPreferences.put(CATEGORY_QUERY_PARAM, preferencesRepository.getPreferredCategories());
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
            preferencesRepository.savePreferredCategories(sources);
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
