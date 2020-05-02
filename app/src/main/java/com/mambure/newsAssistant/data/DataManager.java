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


    public Maybe<ArticlesResult> fetchArticlesFromRemote(Map<String, String> params);

    public Flowable<List<Article>> fetchArticlesFromLocal();

    public void saveArticle(Article article);

    public Completable deleteArticle(Article article);

    public Flowable<List<Source>> fetchSourcesFromLocal();

    public Observable<SourcesResult> fetchSourcesFromRemote();

    public Completable saveSources(List<Source> sources);

    public Completable deleteSource(Source source);

    public void cleanUp();
}
