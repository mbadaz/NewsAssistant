package com.mambure.newsAssistant.data.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.util.HashMap;
import java.util.Objects;

@Entity(tableName = "articles")
public class Article {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo
    public Source source;

    @ColumnInfo public String author;
    @ColumnInfo public String title;
    @ColumnInfo public String description;
    @ColumnInfo public String url;
    @ColumnInfo public String urlToImage;
    @ColumnInfo public String publishedAt;

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof Article && title != null &&
                title.equals(((Article) obj).title) && description != null &&
                description.equals(((Article) obj).description);
    }

    @NonNull
    @Override
    public String toString() {
        return title + " : " + description;
    }
}
