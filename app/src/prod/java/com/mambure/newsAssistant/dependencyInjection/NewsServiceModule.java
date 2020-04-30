package com.mambure.newsAssistant.dependencyInjection;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.remote.NewsService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public class NewsServiceModule {

    @Provides
    @Singleton
    NewsService provideNewsService() {
        return new Retrofit.Builder().baseUrl(Constants.BASE_URL).
                addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                addConverterFactory(MoshiConverterFactory.create()).
                build().create(NewsService.class);
    }
}
