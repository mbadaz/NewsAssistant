package com.mambure.newsAssistant.data.remote;

import androidx.lifecycle.LiveData;

import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.SourcesResult;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;

public interface RemoteRepository {
    Observable<ArticlesResult> getArticleStream();

    Observable<SourcesResult> getSourceStream();

    void fetchArticles(Map<String, String> params);

    void fetchSources();
}
