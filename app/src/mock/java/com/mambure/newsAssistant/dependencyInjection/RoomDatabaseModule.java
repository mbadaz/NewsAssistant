package com.mambure.newsAssistant.dependencyInjection;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mambure.newsAssistant.data.local.AppDatabase;
import com.mambure.newsAssistant.data.local.LocalRepositoryDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomDatabaseModule {

    @Provides
    @Singleton
    LocalRepositoryDAO providesLocalRepositoryDao(Context context) {
        return Room.inMemoryDatabaseBuilder(
                context, AppDatabase.class).build().localDatabaseDAO();
    }

}
