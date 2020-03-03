package com.peruzal.newsassistant.wakthroughActivity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.peruzal.newsassistant.data.ApiInfo;
import com.peruzal.newsassistant.data.remote.NewsService;
import com.peruzal.newsassistant.data.remote.SourcesDataSource;
import com.peruzal.newsassistant.models.SourcesResult;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class WalkthroughActivityViewModel extends AndroidViewModel {

    SourcesDataSource sourcesDataSource;

    public WalkthroughActivityViewModel(@NonNull Application application) {
        super(application);

        NewsService newsService = new Retrofit.Builder().
                baseUrl(ApiInfo.BASE_URL).addConverterFactory(MoshiConverterFactory.create()).
                build().create(NewsService.class);

        sourcesDataSource = new SourcesDataSource(newsService);

    }

    public LiveData<SourcesResult> getSourcesStream() {
        return sourcesDataSource.getDataStream();
    }

    public void fetchSources() {
        sourcesDataSource.fetch();
    }
}
