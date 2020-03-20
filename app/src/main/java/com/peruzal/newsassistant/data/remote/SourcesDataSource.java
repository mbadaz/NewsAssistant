package com.peruzal.newsassistant.data.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.peruzal.newsassistant.data.DataSource;
import com.peruzal.newsassistant.data.models.SourcesResult;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SourcesDataSource implements DataSource<LiveData<SourcesResult>>, Callback<SourcesResult> {

    private NewsService newsService;
    private MutableLiveData<SourcesResult> dataStream = new MutableLiveData<>();

    @Inject
    public SourcesDataSource(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public LiveData<SourcesResult> getDataStream() {
        return dataStream;
    }

    public void fetch() {
        newsService.getSources().enqueue(this);
    }

    @Override
    public void onResponse(Call<SourcesResult> call, Response<SourcesResult> response) {

        if (response.isSuccessful()) {
            dataStream.postValue(response.body());
        }else {
            SourcesResult sourcesResult = new SourcesResult();
            sourcesResult.status = "error";
            dataStream.postValue(sourcesResult);
        }
    }

    @Override
    public void onFailure(Call<SourcesResult> call, Throwable t) {
        SourcesResult sourcesResult = new SourcesResult();
        sourcesResult.status = "error";
        dataStream.postValue(sourcesResult);
    }
}
