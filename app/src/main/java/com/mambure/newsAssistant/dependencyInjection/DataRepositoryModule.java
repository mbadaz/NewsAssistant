package com.mambure.newsAssistant.dependencyInjection;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.remote.NewsService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module(includes = {RoomDatabaseModule.class, RemoteRepositoryModule.class})
public class DataRepositoryModule {

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



