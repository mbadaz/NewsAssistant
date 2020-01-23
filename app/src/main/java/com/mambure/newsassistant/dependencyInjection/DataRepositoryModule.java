package com.mambure.newsassistant.dependencyInjection;

import com.mambure.newsassistant.data.DataRepository;
import com.mambure.newsassistant.data.NewsProvider;
import com.mambure.newsassistant.data.DefaultRepository;
import com.mambure.newsassistant.data.SettingsStore;
import com.mambure.newsassistant.dependencyinjection.RepositoriesModule;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module (includes = RepositoriesModule.class)
public class DataRepositoryModule {

    @Provides
    @Singleton
    public static DefaultRepository provideDataRepository(NewsProvider newsProvider, DataRepository dataRepository ) {

        return new DefaultRepository(
                newsProvider,
                dataRepository
        );
    }
}
