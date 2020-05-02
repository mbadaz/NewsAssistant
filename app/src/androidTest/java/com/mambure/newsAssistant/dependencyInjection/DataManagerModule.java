package com.mambure.newsAssistant.dependencyInjection;

import com.mambure.newsAssistant.data.DataManager;
import com.mambure.newsAssistant.data.MockFailingDataRepository;
import com.mambure.newsAssistant.data.local.LocalDataRepository;
import com.mambure.newsAssistant.data.models.Article;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.Source;
import com.mambure.newsAssistant.data.models.SourcesResult;
import com.mambure.newsAssistant.data.remote.NewsService;

import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

@Module (includes = {NewsServiceModule.class, RoomDatabaseModule.class})
public class DataManagerModule {
    @Provides
    @Singleton
    public DataManager providesDataManager(LocalDataRepository localDataRepository, NewsService newsService) {
        return new MockFailingDataRepository();
    }
}
