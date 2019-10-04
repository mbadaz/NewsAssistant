package com.mbadasoft.newsassistant.data;

import java.util.Map;

public interface PreferencesRepository {

    Map<String, ?> getPreferredCategories();
    Map<String, ?> getPreferredSources();
    Map<String, ?> getAppPreferences();
    void savePreferredSource(String value);
    void savePreferredCategory(String value);
    void saveisFirstTimeLogin(boolean value);
    void saveEnableExternalBroweser(boolean value);
    void saveArticlesLoadingLimit(int value);
}
