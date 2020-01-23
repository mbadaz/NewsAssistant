package com.mambure.newsassistant.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MockSettingsRepository implements SettingsRepository {

    private Map<String, Object> userPrefs = new HashMap<>();
    private Set<String> preferredSources = new HashSet<>();
    private Set<String> preferredCategories = new HashSet<>();
    private Set<String> preferredLanguages = new HashSet<>();
    private boolean isFirstTimeLaunch = true;
    private int articleLoadLimit = 10;
    private boolean useExternalBrowser = false;

    public Map<String, Object> getApiPrefs() {
        return userPrefs;
    }

    @Override
    public void loadPreferences() {

    }

    @Override
    public Set<String> getPreferredSources() {
        return preferredSources;
    }

    @Override
    public void savePreferredSources(Set<String> sources) {
        preferredSources.addAll(sources);
    }

    @Override
    public void savePreferredCategories(Set<String> categories) {
        preferredCategories.addAll(categories);
    }

    @Override
    public void savePreferredLanguages(Set<String> languages) {
        preferredLanguages.addAll(languages);
    }

    @Override
    public Set<String> getPreferredLanguages() {
        return preferredLanguages;
    }

    @Override
    public Set<String> getPreferredCategories() {
        return preferredCategories;
    }

    @Override
    public boolean getIsFirstTimeLaunch(){
        return isFirstTimeLaunch;
    }

    @Override
    public void isFirstTimeLaunch(boolean value) {
        isFirstTimeLaunch = value;
    }

    @Override
    public void setEnableExternalBrowser(boolean value) {
        useExternalBrowser = value;
    }

    @Override
    public boolean isExternalBrowserEnabled() {
        return useExternalBrowser;
    }

    @Override
    public void setArticlesLoadingLimit(int value) {
        articleLoadLimit = value;
    }

    @Override
    public int getArticlesLoadingLimit() {
        return articleLoadLimit;
    }
}
