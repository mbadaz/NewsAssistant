package com.peruzal.newsassistant.data.remote;

import androidx.lifecycle.MutableLiveData;

import com.peruzal.newsassistant.data.DataSource;
import com.peruzal.newsassistant.models.ArticlesResult;

import org.json.JSONObject;

public class RemoteDataSource implements DataSource<JSONObject> {
    NewsService newsService;

    MutableLiveData<ArticlesResult> articlesStream = new MutableLiveData<>();

    @Override
    public JSONObject getDataStream() {
        return articlesStream;
    }
}
