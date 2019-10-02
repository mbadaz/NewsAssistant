package com.mbadasoft.newsassistant.data;

import com.mbadasoft.newsassistant.models.Article;
import com.mbadasoft.newsassistant.models.Source;

import java.util.List;

public interface NewsRepository {
    List<Source> getSources();
    List<Article> getArticles();
}
