package com.mambure.newsAssistant.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.mambure.newsAssistant.data.models.Article;
import com.mambure.newsAssistant.data.models.Source;

@Database(entities = {Article.class, Source.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocalDataRepository localDatabaseDAO();
}
