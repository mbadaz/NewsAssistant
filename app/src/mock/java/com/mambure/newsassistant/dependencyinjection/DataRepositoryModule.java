package com.mambure.newsassistant.dependencyinjection;

import com.mambure.newsassistant.data.DataRepository;
import com.mambure.newsassistant.data.MockRemoteNewsRepository;
import com.mambure.newsassistant.data.MockSettingsRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataRepositoryModule {

    @Provides
    @Singleton
    public static DataRepository provideDataRepository() {
        return new DataRepository(
                new MockRemoteNewsRepository(),
                new MockSettingsRepository()
        );
    }
}
