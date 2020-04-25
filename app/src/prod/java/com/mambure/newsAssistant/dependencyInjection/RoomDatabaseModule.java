package com.mambure.newsAssistant.dependencyInjection;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Room;

import com.mambure.newsAssistant.data.local.AppDatabase;
import com.mambure.newsAssistant.data.local.LocalRepositoryDAO;
import com.mambure.newsAssistant.data.remote.RemoteRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomDatabaseModule {

    @Provides
    @Singleton
    LocalRepositoryDAO providesLocalRepositoryDao(Context context) {
        return Room.databaseBuilder(
                context, AppDatabase.class, "App-database.db").build().localDatabaseDAO();
    }

}
