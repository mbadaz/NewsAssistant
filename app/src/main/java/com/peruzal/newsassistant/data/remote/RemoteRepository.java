package com.peruzal.newsassistant.data.remote;

import androidx.lifecycle.LiveData;

import com.peruzal.newsassistant.data.models.ArticlesResult;
import com.peruzal.newsassistant.data.models.SourcesResult;

import java.util.Map;

public interface RemoteRepository {
    LiveData<ArticlesResult> getArticleStream();

    LiveData<SourcesResult> getSourceStream();

    void fetchArticles(Map<String, String> params);

    void fetchSources();
}
