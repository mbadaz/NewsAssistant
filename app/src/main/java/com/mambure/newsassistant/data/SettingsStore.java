package com.mambure.newsassistant.data;

import java.util.Map;
import java.util.Set;

public interface SettingsStore {

    Set<String> getPreferredLanguages();

    Set<String> getPreferredCategories();

    boolean getIsFirstTimeLaunch();

    void isFirstTimeLaunch(boolean value);

    void setEnableExternalBrowser(boolean value);

    boolean isExternalBrowserEnabled();

    void setArticlesLoadingLimit(int value);

    int getArticlesLoadingLimit();
}
