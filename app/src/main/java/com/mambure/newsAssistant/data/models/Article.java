package com.mambure.newsAssistant.data.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Objects;

@Entity(tableName = "articles")
public class Article {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @Ignore
    public Source source;

    @ColumnInfo public String author;
    @ColumnInfo public String title;
    @ColumnInfo public String description;
    @ColumnInfo public String url;
    @ColumnInfo public String urlToImage;
    @ColumnInfo public String publishedAt;

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof Article &&
                ((Article) obj).title.equals(title) &&
                ((Article) obj).description.equals(description);
    }

    @NonNull
    @Override
    public String toString() {
        return title + " : " + description;
    }
}
