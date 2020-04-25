package com.mambure.newsAssistant.data.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.SourcesResult;

import java.util.Map;

public class MockRemoteRepository implements RemoteRepository {
    private MutableLiveData<ArticlesResult> articlesStream = new MutableLiveData<>();
    private MutableLiveData<SourcesResult> sourcesStream = new MutableLiveData<>();


    @Override
    public LiveData<ArticlesResult> getArticleStream() {
        return articlesStream;
    }

    @Override
    public LiveData<SourcesResult> getSourceStream() {
        return sourcesStream;
    }

    @Override
    public void fetchArticles(Map<String, String> params) {

    }

    @Override
    public void fetchSources() {
        Thread thread = new Thread(this::mockFailedRequest);
        thread.start();
    }

    private void mockFailedRequest() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SourcesResult result = new SourcesResult();
        result.status = Constants.RESULT_ERROR;
        sourcesStream.postValue(result);
    }
}
