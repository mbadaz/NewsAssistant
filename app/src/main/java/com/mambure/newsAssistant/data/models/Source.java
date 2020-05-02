package com.mambure.newsAssistant.data.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.squareup.moshi.Json;

@Entity(tableName = "sources")
public class Source {
    @PrimaryKey(autoGenerate = true)
    public int pKey;

    @ColumnInfo
    @Json(name = "id")
    public String id;

    @ColumnInfo
    @Json(name = "name")
    public String name;

    @ColumnInfo
    @Json(name = "description")
    public String description;

    @ColumnInfo
    @Json(name = "url")
    public String url;

    @ColumnInfo
    @Json(name = "category")
    public String category;

    @ColumnInfo
    @Json(name = "language")
    public String language;

    @ColumnInfo
    @Json(name = "country")
    public String country;

    @Ignore
    private boolean checked = false;

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof Source && this.id.equals(((Source) obj).id);
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
