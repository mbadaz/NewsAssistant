package com.mambure.newsAssistant.data.models;

import androidx.annotation.NonNull;

import java.util.List;

public class ArticlesResult {

    public String status;
    public List<Article> articles;

    @NonNull
    @Override
    public String toString() {
        return "Result status: " + status + ", Articles: " +
                (articles == null ? 0 : articles.size());
    }
}
