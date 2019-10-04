package com.mbadasoft.newsassistant.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class AppPreferencesRepository implements PreferencesRepository {

    public static final String ARTICLES_LOADING_LIMIT = "ArticlesLoadingLimit";
    public static final String ENABLE_EXTERNAL_BROWSER = "EnableExternalBrowser";
    public static final String IS_FIRST_TIME_LOGIN = "IsFirstTimeLogin";
    private SharedPreferences categoryPreferences;
    private SharedPreferences sourcesPreferences;
    private SharedPreferences appPreferences;
    private static AppPreferencesRepository INSTANCE = null;
    private Map<String, ?> appPrefs;
    private Map<String, ?> categoryPrefs;
    private Map<String, ?> sourcePrefs;


    private AppPreferencesRepository(Context context) {
        sourcesPreferences = context.getSharedPreferences("source", Context.MODE_PRIVATE);
        categoryPreferences = context.getSharedPreferences("categories", Context.MODE_PRIVATE);
        appPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
    }

    public static AppPreferencesRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppPreferencesRepository(context);
            INSTANCE.loadPreferences();
        }
        return INSTANCE;
    }

    private void loadPreferences() {
        Thread thread = new Thread(() -> {
            appPrefs = appPreferences.getAll();
            categoryPrefs = categoryPreferences.getAll();
            sourcePrefs = sourcesPreferences.getAll();
        });
        thread.start();
    }

    @Override
    public Map<String, ?> getAppPreferences() {
        return appPrefs;
    }

    @Override
    public Map<String, ?> getPreferredCategories() {
        return categoryPrefs;
    }

    @Override
    public Map<String, ?> getPreferredSources() {
        return sourcePrefs;
    }

    @Override
    public void savePreferredSource(String source) {

    }

    @Override
    public void savePreferredCategory(String category) {

    }

    @Override
    public void saveisFirstTimeLogin(boolean value) {
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putBoolean(IS_FIRST_TIME_LOGIN, value);
        editor.apply();
    }

    @Override
    public void saveEnableExternalBroweser(boolean value) {
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putBoolean(ENABLE_EXTERNAL_BROWSER, value);
        editor.apply();
    }

    @Override
    public void saveArticlesLoadingLimit(int value) {
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putInt(ARTICLES_LOADING_LIMIT, value);
        editor.apply();
    }
}
