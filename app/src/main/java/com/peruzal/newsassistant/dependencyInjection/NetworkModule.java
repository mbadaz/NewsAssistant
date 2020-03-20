package com.peruzal.newsassistant.dependencyInjection;

import com.peruzal.newsassistant.Constants;
import com.peruzal.newsassistant.data.remote.NewsService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    NewsService providesNewsService() {
        return new Retrofit.Builder().
                baseUrl(Constants.BASE_URL).
                addConverterFactory(MoshiConverterFactory.create()).
                build().
                create(NewsService.class);
    }
}
