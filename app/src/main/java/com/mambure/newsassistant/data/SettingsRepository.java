package com.mambure.newsassistant.data;

import java.util.Map;
import java.util.Set;

public interface SettingsRepository {

    Map<String, ?> getUserPrefs();

    void loadPreferences();

    Set<String> getPreferredSources();

    void savePreferredSources(Set<String> sources);

    void savePreferredCategories(Set<String> categories);

    void savePreferredLanguages(Set<String> languages);

    Set<String> getPreferredLanguages();

    Set<String> getPreferredCategories();

    boolean getIsFirstTimeLaunch();

    void isFirstTimeLaunch(boolean value);

    void setEnableExternalBrowser(boolean value);

    boolean isExternalBrowserEnabled();

    void setArticlesLoadingLimit(int value);

    int getArticlesLoadingLimit();
}
