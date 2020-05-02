package com.mambure.newsAssistant.dependencyInjection;

import com.mambure.newsAssistant.data.DataManager;
import com.mambure.newsAssistant.data.DataRepository;
import com.mambure.newsAssistant.data.local.LocalDataRepository;
import com.mambure.newsAssistant.data.remote.NewsService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {RoomDatabaseModule.class, NewsServiceModule.class})
class DataManagerModule {

    @Provides
    @Singleton
    public DataManager providesDataManager(LocalDataRepository localDataRepository, NewsService newsService) {
        return new DataRepository(localDataRepository, newsService);
    }
}





