package com.mambure.newsAssistant.dependencyInjection;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.remote.NewsService;
import com.mambure.newsAssistant.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;

import javax.inject.Named;
import javax.inject.Singleton;

import butterknife.internal.Utils;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module(includes = AppModule.class)
public class NewsServiceModule {

    @Provides
    @Singleton
    NewsService provideNewsService(OkHttpClient httpClient) {
        return new Retrofit.Builder().baseUrl(Constants.BASE_URL).
                addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                addConverterFactory(MoshiConverterFactory.create())
                .client(httpClient).
                build().create(NewsService.class);
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient(@Named("Interceptor") Interceptor interceptor, @Named("Network Interceptor") Interceptor interceptor2, Context context) {
        File httpCacheDir = new File(context.getCacheDir(), "http cache");
        int cacheSize = 2 * 1024 * 1024;
        Cache cache = new Cache(httpCacheDir, cacheSize);
        return new OkHttpClient.Builder().
                addInterceptor(interceptor).addNetworkInterceptor(interceptor2).
                cache(cache).build();
    }

    @Provides
    @Singleton
    @Named("Interceptor")
    Interceptor providesInterceptor(Context context) {
        return chain -> {
            Response originalResponse = chain.proceed(chain.request());
            if (NetworkUtils.isNetworkConnected(context)) {
                int maxAge = 60; // read from cache for 1 minute
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale).
                        removeHeader("Pragma")
                        .build();
            }
        };
    }

    @Provides
    @Named("Network Interceptor")
    @Singleton
    Interceptor providesNetworkInterceptor() {
        return chain -> {
            Response originalResponse = chain.proceed(chain.request());
            String cacheControl = originalResponse.header("Cache-Control");

            if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                    cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + 2419200).removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse;
            }
        };
    }
}
