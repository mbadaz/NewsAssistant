package com.mambure.newsassistant.data;

import androidx.lifecycle.LiveData;

import com.mambure.newsassistant.models.ArticlesResult;
import com.mambure.newsassistant.models.SourcesResult;

import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

public interface NewsProvider {
    
    LiveData<SourcesResult> getSources();
    
    LiveData<ArticlesResult> getArticles(@Nullable String id, @Nullable Map<String, Object> args);

    void setUpdatesListener(UpdatesListener listener);


}
