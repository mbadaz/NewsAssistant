package com.mambure.newsAssistant.data;

import com.mambure.newsAssistant.data.models.Article;
import com.mambure.newsAssistant.data.models.Source;

import java.util.ArrayList;
import java.util.List;

public class MockingUtils {

    public static List<Source> generateMockSources() {
        List<Source> sources = new ArrayList<>();
        for (int x = 1; x <= 5; x++) {
            sources.add(createSource(x));
        }
        return sources;
    }

    public static List<Article> generateMockArticles() {
        List<Article> articles = new ArrayList<>();
        for (int x = 1; x <= 5; x++) {
            Article article = new Article();
            article.author = "author " + x;
            article.description = "description " + x;
            article.id = x;
            article.publishedAt = "2020-04-18T14:38:37Z";
            article.source = createSource(x);
            article.title = "title " + x;
            articles.add(article);
        }
        return articles;
    }

    private static Source createSource(int x) {
        Source source = new Source();
        source.id = "id " + x;
        source.category = "category " + x;
        source.country = "country " + x;
        source.description = "description " + x;
        source.language = "language " + x;
        source.name = "name " + x;
        return source;
    }
}
