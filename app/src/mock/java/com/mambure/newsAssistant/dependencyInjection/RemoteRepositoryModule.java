package com.mambure.newsAssistant.dependencyInjection;

import com.mambure.newsAssistant.data.remote.MockRemoteRepository;
import com.mambure.newsAssistant.data.remote.RemoteRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RemoteRepositoryModule {

    @Provides
    @Singleton
    static RemoteRepository providesRepositoryModule() {
        return new MockRemoteRepository();
    }
}
