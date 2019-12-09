package com.mbadasoft.newsassistant.data;

import com.mbadasoft.newsassistant.models.Article;
import com.mbadasoft.newsassistant.models.Source;

import java.util.List;

public interface UpdatesListener {

    void onArticlesUpdate(List<Article> articles);

    void onSourcesUpdate(List<Source> sources);
}
