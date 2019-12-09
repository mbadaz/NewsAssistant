package com.mbadasoft.newsassistant.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AppPreferencesRepository implements PreferencesRepository {

    private static final String ARTICLES_LOADING_LIMIT = "ArticlesLoadingLimit";
    private static final String ENABLE_EXTERNAL_BROWSER = "EnableExternalBrowser";
    private static final String IS_FIRST_TIME_LOGIN = "IsFirstTimeLogin";
    private static final String SOURCES = "Sources";
    private static final String CATEGORIES = "Categories";
    private SharedPreferences appPreferences;
    private Map<String, ?> userPrefs = new HashMap<>();

    public AppPreferencesRepository(Context context) {
        appPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        appPreferences.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> loadPreferences());
    }


    @Override
    public void loadPreferences() {
        Thread thread = new Thread(() -> {
            userPrefs = appPreferences.getAll();
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getPreferredSources() {
        return (Set<String>) userPrefs.get(SOURCES);
    }

    public void savePreferredSources(Set<String> sources) {
        appPreferences.edit().putStringSet(SOURCES, sources).commit();
    }

    public void savePreferredCategories(Set<String> categories) {
        appPreferences.edit().putStringSet(CATEGORIES, categories).commit();
    }

    public Set<String> getPreferredCategories() {
        return (Set<String>) userPrefs.get(CATEGORIES);
    }

    public boolean getIsFirstTimeLogin(){

        return appPreferences.getBoolean(IS_FIRST_TIME_LOGIN, true);
    }

    public void saveisFirstTimeLogin(boolean value) {
        appPreferences.edit().putBoolean(IS_FIRST_TIME_LOGIN, value).apply();
    }

    public void setEnableExternalBrowser(boolean value) {
        appPreferences.edit().putBoolean(ENABLE_EXTERNAL_BROWSER, value).apply();
    }

    public boolean isExternalBrowserEnabled() {
        if(userPrefs.containsKey(ENABLE_EXTERNAL_BROWSER)){
            return (Boolean) userPrefs.get(ENABLE_EXTERNAL_BROWSER);
        }else {
            return false;
        }
    }

    public void setArticlesLoadingLimit(int value) {
        appPreferences.edit().putInt(ARTICLES_LOADING_LIMIT, value).apply();
    }

    public int getArticlesLoadingLimit() {
        if (userPrefs.containsKey(ARTICLES_LOADING_LIMIT)) {
            return (Integer) userPrefs.get(ARTICLES_LOADING_LIMIT);
        } else {
            return 15;
        }
    }
}
