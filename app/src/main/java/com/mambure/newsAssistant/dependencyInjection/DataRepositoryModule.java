package com.mambure.newsAssistant.dependencyInjection;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.remote.NewsService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module(includes = {RoomDatabaseModule.class, RemoteRepositoryModule.class})
public class DataRepositoryModule {

    @Provides
    @Singleton
    NewsService providesNewsService() {
        return new Retrofit.Builder().
                baseUrl(Constants.BASE_URL).
                addConverterFactory(MoshiConverterFactory.create()).
                addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                build().
                create(NewsService.class);
    }

}



