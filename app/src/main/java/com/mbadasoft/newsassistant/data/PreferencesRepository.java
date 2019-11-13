package com.mbadasoft.newsassistant.data;

import android.util.SparseIntArray;

import com.mbadasoft.newsassistant.models.Source;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public interface PreferencesRepository {

   void loadPreferences();

    Set<String> getPreferredSources();

    void savePreferredSources(Set<String> sources);

    void savePreferredCategories(Set<String> categories);

    Set<String> getPreferredCategories();

    boolean getIsFirstTimeLogin();

    void saveisFirstTimeLogin(boolean value);

    void setEnableExternalBrowser(boolean value);

    boolean isExternalBrowserEnabled();

    void setArticlesLoadingLimit(int value);

    int getArticlesLoadingLimit();
}
