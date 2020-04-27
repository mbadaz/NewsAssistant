package com.mambure.newsAssistant.dependencyInjection;

import com.mambure.newsAssistant.data.remote.NewsRepository;
import com.mambure.newsAssistant.data.remote.NewsService;
import com.mambure.newsAssistant.data.remote.RemoteRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = DataRepositoryModule.class)
public class RemoteRepositoryModule {
    @Provides
    @Singleton
    public static RemoteRepository providesRemoteRepository(NewsService newsService) {
        return new NewsRepository();
    }
}
