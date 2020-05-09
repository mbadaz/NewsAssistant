package com.mambure.newsAssistant.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.mambure.newsAssistant.data.models.Article;
import com.mambure.newsAssistant.data.models.Source;

@Database(entities = {Article.class, Source.class}, version = 2, exportSchema = false)
@androidx.room.TypeConverters(SourceConvertor.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocalDataRepository localDatabaseDAO();
}
