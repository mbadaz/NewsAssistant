package com.peruzal.newsassistant.data.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.peruzal.newsassistant.data.DataSource;
import com.peruzal.newsassistant.models.Source;
import com.peruzal.newsassistant.models.SourcesResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SourcesDataSource implements DataSource<LiveData<SourcesResult>>, Callback<List<Source>> {

    private NewsService newsService;
    private MutableLiveData<SourcesResult> dataStream = new MutableLiveData<>();

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
    public void onResponse(Call<List<Source>> call, Response<List<Source>> response) {
        SourcesResult sourcesResult = new SourcesResult();
        if (response.isSuccessful()) {
            sourcesResult.status = "ok";
            sourcesResult.sources = response.body();
            dataStream.postValue(sourcesResult);
        }else {
            sourcesResult.status = "error";
            dataStream.postValue(sourcesResult);
        }
    }

    @Override
    public void onFailure(Call<List<Source>> call, Throwable t) {
        SourcesResult sourcesResult = new SourcesResult();
        sourcesResult.status = "error";
        dataStream.postValue(sourcesResult);
    }
}
