package com.mambure.newsassistant.data;

import com.mambure.newsassistant.models.Article;
import com.mambure.newsassistant.models.Source;

import java.util.Set;

public interface DataRepository extends NewsProvider {

    void savePreferredSources(Set<Source> sources);

    void savePreferredCategories(Set<String> categories);

    void savePreferredLanguages(Set<String> languages);

    void saveArticle(Article article);

    Set<String> getPreferredLanguages();

    Set<String> getPreferredCategories();

    boolean getIsFirstTimeLaunch();

    void isFirstTimeLaunch(boolean value);

    void setEnableExternalBrowser(boolean value);

    boolean isExternalBrowserEnabled();

    void setArticlesLoadingLimit(int value);

    int getArticlesLoadingLimit();

}
