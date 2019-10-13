package com.mbadasoft.newsassistant.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AppPreferencesRepository implements PreferencesRepository {

    public static final String ARTICLES_LOADING_LIMIT = "ArticlesLoadingLimit";
    public static final String ENABLE_EXTERNAL_BROWSER = "EnableExternalBrowser";
    public static final String IS_FIRST_TIME_LOGIN = "IsFirstTimeLogin";
    public static final String SOURCES = "Sources";
    public static final String CATEGORIES = "categories";
    private SharedPreferences appPreferences;
    private static AppPreferencesRepository INSTANCE = null;
    private Map<String, ?> userPrefs = new HashMap<>();

    private AppPreferencesRepository(Context context) {
        appPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
    }

    public static AppPreferencesRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppPreferencesRepository(context);
            INSTANCE.loadPreferences();
        }
        return INSTANCE;
    }

    @Override
    public void loadPreferences() {
        Thread thread = new Thread(() -> {
            userPrefs = appPreferences.getAll();
        });
        thread.start();
    }

    public Set<String> getPreferredSources() {
        return (Set<String>) userPrefs.get(SOURCES);
    }

    public void savePreferredSources(Set<String> sources) {
        appPreferences.edit().putStringSet(SOURCES, sources).apply();
    }

    public void savePreferredCategories(Set<String> categories) {
        appPreferences.edit().putStringSet(CATEGORIES, categories).apply();
    }

    public Set<String> getPreferredCategories() {
        return (Set<String>) userPrefs.get(CATEGORIES);
    }

    public boolean getIsFirstTimeLogin(){
        return userPrefs.containsKey(IS_FIRST_TIME_LOGIN);
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
