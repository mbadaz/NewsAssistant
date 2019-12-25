package com.mambure.newsassistant.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.mambure.newsassistant.data.Constants.*;

public class AppSettingsRepository implements SettingsRepository {

    private static final String ENABLE_EXTERNAL_BROWSER = "EnableExternalBrowser";
    private static final String IS_FIRST_TIME_LOGIN = "IsFirstTimeLogin";
    private SharedPreferences apiPrefs;
    private SharedPreferences appPrefs;
    private Map<String, ?> userPrefs = new HashMap<>();

    public AppSettingsRepository(Context context) {
        apiPrefs = context.getSharedPreferences("api", Context.MODE_PRIVATE);
        appPrefs = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        apiPrefs.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> loadPreferences());
    }


    @Override
    public void loadPreferences() {
        Thread thread = new Thread(() -> {
            userPrefs = apiPrefs.getAll();
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Map<String, ?> getUserPrefs() {
        return userPrefs;
    }

    public Set<String> getPreferredSources() {
        return (Set<String>) userPrefs.get(SOURCES_QUERY_PARAM);
    }

    public void savePreferredSources(Set<String> sources) {
        apiPrefs.edit().putStringSet(SOURCES_QUERY_PARAM, sources).commit();
    }

    public void savePreferredCategories(Set<String> categories) {
        apiPrefs.edit().putStringSet(CATEGORY_QUERY_PARAM, categories).commit();
    }

    @Override
    public void savePreferredLanguages(Set<String> languages) {
        apiPrefs.edit().putStringSet(LANGUAGE_QUERY_PARAM, languages).commit();
    }

    @Override
    public Set<String> getPreferredLanguages() {
        return (Set<String>) userPrefs.get(LANGUAGE_QUERY_PARAM);
    }

    public Set<String> getPreferredCategories() {
        return (Set<String>) userPrefs.get(CATEGORY_QUERY_PARAM);
    }

    @Override
    public boolean getIsFirstTimeLaunch(){
        return appPrefs.getBoolean(IS_FIRST_TIME_LOGIN, true);
    }

    @Override
    public void isFirstTimeLaunch(boolean value) {
        appPrefs.edit().putBoolean(IS_FIRST_TIME_LOGIN, value).apply();
    }

    @Override
    public void setEnableExternalBrowser(boolean value) {
        appPrefs.edit().putBoolean(ENABLE_EXTERNAL_BROWSER, value).apply();
    }

    @Override
    public boolean isExternalBrowserEnabled() {
            return (Boolean) userPrefs.get(ENABLE_EXTERNAL_BROWSER);

    }

    @Override
    public void setArticlesLoadingLimit(int value) {
        apiPrefs.edit().putInt(PAGE_SIZE_QUERY_PARAM, value).apply();
    }

    @Override
    public int getArticlesLoadingLimit() {
        if (userPrefs.containsKey(PAGE_SIZE_QUERY_PARAM) ){
            return (Integer) userPrefs.get(PAGE_SIZE_QUERY_PARAM);
        } else {
            return 15;
        }
    }
}
