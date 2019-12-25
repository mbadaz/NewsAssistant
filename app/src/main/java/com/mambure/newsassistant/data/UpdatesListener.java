package com.mambure.newsassistant.data;

import com.mambure.newsassistant.models.Article;
import com.mambure.newsassistant.models.Source;

import java.util.List;

public interface UpdatesListener {

    void onArticlesUpdate(List<Article> articles);

    void onSourcesUpdate(List<Source> sources);
}
