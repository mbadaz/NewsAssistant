package com.mambure.newsAssistant.dependencyInjection;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppModule.class)
public class SharedPreferenceModule {
    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Context context) {
        return context.getSharedPreferences("app preferences", Context.MODE_PRIVATE);
    }
}
