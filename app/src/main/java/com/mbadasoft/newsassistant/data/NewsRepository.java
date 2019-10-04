package com.mbadasoft.newsassistant.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mbadasoft.newsassistant.models.Article;
import com.mbadasoft.newsassistant.models.ArticlesResult;
import com.mbadasoft.newsassistant.models.Source;
import com.mbadasoft.newsassistant.models.SourcesResult;

import java.util.List;

public interface NewsRepository {
    LiveData<SourcesResult> getSources();
    LiveData<ArticlesResult> getArticles();
}
