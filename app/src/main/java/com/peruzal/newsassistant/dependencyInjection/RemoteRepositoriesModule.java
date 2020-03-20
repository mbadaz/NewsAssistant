package com.peruzal.newsassistant.dependencyInjection;

import com.peruzal.newsassistant.data.remote.ArticlesDataSource;
import com.peruzal.newsassistant.data.remote.NewsService;
import com.peruzal.newsassistant.data.remote.SourcesDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RemoteRepositoriesModule {

//    @Provides
//    @Singleton
//    ArticlesDataSource providesArticlesDataSource(NewsService newsService) {
//        return new ArticlesDataSource(newsService);
//    }
//
//    @Provides
//    @Singleton
//    SourcesDataSource providesSourcesDataSource(NewsService newsService) {
//        return new SourcesDataSource(newsService);
//    }
}
