package com.peruzal.newsassistant.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.peruzal.newsassistant.models.Article;

import java.util.List;

@Dao
public interface ArticlesDAO {
    @Query("SELECT * FROM articles")
    LiveData<List<Article>> getAll();

    @Insert
    void insertAll(Article... articles);

    @Delete
    void delete(Article article);


}
