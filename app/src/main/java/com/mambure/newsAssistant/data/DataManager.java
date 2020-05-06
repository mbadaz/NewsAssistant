package com.mambure.newsAssistant.data;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.models.Article;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.Source;
import com.mambure.newsAssistant.data.models.SourcesResult;
import com.mambure.newsAssistant.utils.ParsingUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

public interface DataManager {


    Observable<ArticlesResult> fetchArticlesFromRemote(Map<String, String> params, List<Source> preferredSources);

    Flowable<List<Article>> fetchArticlesFromLocal();

    Completable saveArticle(Article article);

    Maybe<Article> getArticleByTitle(String title);

    Completable deleteArticle(Article article);

    Maybe<List<Source>> fetchSourcesFromLocal();

    Observable<SourcesResult> fetchSourcesFromRemote();

    Completable saveSources(List<Source> sources);

    Completable deleteSources(List<Source> sources);

    void cleanUp();
}
