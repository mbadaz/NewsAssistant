package com.peruzal.newsassistant.wakthroughActivity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.peruzal.newsassistant.data.DataRepository;
import com.peruzal.newsassistant.data.local.AppDatabase;
import com.peruzal.newsassistant.data.remote.Constants;
import com.peruzal.newsassistant.data.remote.NewsService;
import com.peruzal.newsassistant.data.remote.RemoteArticleDataSource;
import com.peruzal.newsassistant.data.remote.SourcesDataSource;
import com.peruzal.newsassistant.models.SourcesResult;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class WalkthroughActivityViewModel extends AndroidViewModel {
    DataRepository dataRepository;

    public WalkthroughActivityViewModel(@NonNull Application application) {
        super(application);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(MoshiConverterFactory.create()).
                baseUrl(Constants.BASE_URL).build();
        NewsService newsService = retrofit.create(NewsService.class);
        dataRepository = new DataRepository(
                new SourcesDataSource(newsService),
                new RemoteArticleDataSource(newsService),
                Room.databaseBuilder(application, AppDatabase.class, "app-database").build()
        );
    }

    public LiveData<SourcesResult> getSourcesStream() {
        return dataRepository.getSourcesStream();
    }

    public void fetchSources() {
        dataRepository.fetchSources();
    }
}
