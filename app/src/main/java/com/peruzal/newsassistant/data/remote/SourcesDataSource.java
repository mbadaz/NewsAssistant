package com.peruzal.newsassistant.data.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.peruzal.newsassistant.data.DataSource;
import com.peruzal.newsassistant.models.SourcesResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SourcesDataSource implements DataSource<LiveData<SourcesResult>>, Callback<SourcesResult> {

    private MutableLiveData<SourcesResult> sourcesStream = new MutableLiveData<>();
    NewsService newsService;

    public SourcesDataSource(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public LiveData<SourcesResult> getDataStream() {
        return sourcesStream;
    }

    public void fetch() {
        newsService.getSources().enqueue(this);
    }

    @Override
    public void onResponse(Call<SourcesResult> call, Response<SourcesResult> response) {
        if (response.isSuccessful()) {
            sourcesStream.postValue(response.body());
        }else {
            sourcesStream.postValue(null);
        }
    }

    @Override
    public void onFailure(Call<SourcesResult> call, Throwable t) {
        sourcesStream.postValue(null);
    }
}
