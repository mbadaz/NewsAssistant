package com.mambure.newsAssistant.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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
}
