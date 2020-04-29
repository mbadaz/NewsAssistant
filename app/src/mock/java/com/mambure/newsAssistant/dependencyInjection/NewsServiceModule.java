package com.mambure.newsAssistant.dependencyInjection;

import com.mambure.newsAssistant.data.remote.MockNewsService;
import com.mambure.newsAssistant.data.remote.NewsService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NewsServiceModule {

    @Provides
    @Singleton
    NewsService provideNewsService() {
        return new MockNewsService();
    }
}
