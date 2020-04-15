package com.mambure.newsAssistant.data.remote;

import androidx.lifecycle.LiveData;

import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.SourcesResult;

import java.util.Map;

public interface RemoteRepository {
    LiveData<ArticlesResult> getArticleStream();

    LiveData<SourcesResult> getSourceStream();

    void fetchArticles(Map<String, String> params);

    void fetchSources();
}
