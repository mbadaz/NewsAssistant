package com.mambure.newsassistant.data;

import androidx.lifecycle.LiveData;

import com.mambure.newsassistant.models.SourcesResult;

import java.util.Map;

public interface NewsRepository {
    LiveData<SourcesResult> getSources();
    void getArticles(String id, Map<String, ?> args);

    void setUpdatesListener(UpdatesListener listener);
}
