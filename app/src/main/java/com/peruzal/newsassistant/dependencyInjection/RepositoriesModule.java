package com.peruzal.newsassistant.dependencyInjection;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Room;

import com.peruzal.newsassistant.data.local.AppDatabase;
import com.peruzal.newsassistant.data.local.LocalRepositoryDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppModule.class)
public class RepositoriesModule {

    @Provides
    @Singleton
    LocalRepositoryDAO providesLocalRepositoryDao(Context context) {
        return Room.databaseBuilder(
                context, AppDatabase.class, "App-database.db").build().localDatabaseDAO();
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Context context) {
        return context.getSharedPreferences("app preferences", Context.MODE_PRIVATE);
    }

}
