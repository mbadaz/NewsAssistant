package com.mambure.newsAssistant.dependencyInjection;

import com.mambure.newsAssistant.data.Repository;
import com.mambure.newsAssistant.data.MockFailingDataRepository;
import com.mambure.newsAssistant.data.local.LocalDataRepository;
import com.mambure.newsAssistant.data.remote.NewsService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module (includes = {NewsServiceModule.class, RoomDatabaseModule.class})
public class DataManagerModule {
    @Provides
    @Singleton
    public Repository providesDataManager(LocalDataRepository localDataRepository, NewsService newsService) {
        return new MockFailingDataRepository();
    }
}
