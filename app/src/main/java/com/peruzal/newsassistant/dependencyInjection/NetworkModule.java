package com.peruzal.newsassistant.dependencyInjection;

import com.peruzal.newsassistant.Constants;
import com.peruzal.newsassistant.data.remote.NewsRepository;
import com.peruzal.newsassistant.data.remote.NewsService;
import com.peruzal.newsassistant.data.remote.RemoteRepository;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;

import org.json.JSONObject;

import java.io.IOException;

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

    @Provides
    RemoteRepository providesRemoteRepository(NewsService newsService) {
        return new NewsRepository(newsService);
    }
}
