package com.mambure.newsassistant.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mambure.newsassistant.models.Article;
import com.mambure.newsassistant.models.ArticlesResult;
import com.mambure.newsassistant.models.Source;
import com.mambure.newsassistant.models.SourcesResult;
import com.mambure.newsassistant.utils.HttpRequestsBuilder;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FakeRemoteNewsProvider implements NewsProvider {

    public MutableLiveData<SourcesResult> sourcesResult = new MutableLiveData<>();
    private UpdatesListener listener;


    @Override
    public LiveData<SourcesResult> getSources() {
        simulateSourceResponse();
        return null;
    }

    @Override
    public LiveData<ArticlesResult> getArticles(String id, Map<String, Object> args) {
        try {
            simulateArticlesResponse(
                    (int) (Object) (args.get(Constants.PAGE_SIZE_QUERY_PARAM)),
                    HttpRequestsBuilder.createRequests("path", args).size()
            );
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void setUpdatesListener(UpdatesListener listener) {
        this.listener = listener;
    }

    private void simulateSourceResponse() {
        List<Source> sources = new ArrayList<>();
        for (int x = 1; x <= 15; x++) {
            Source source = new Source();
            source.category = "Source " + x + "category";
            source.description = "Source " + x + "description";
            source.name = "Source " + x + "name";
            source.language = "en";
            source.country = "c" + 1;
            sources.add(source);
        }
        listener.onSourcesUpdate(sources);
    }

    private void simulateArticlesResponse(int pageSize, int requestUrls) {
        for (int x = 1; x <= requestUrls; x++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            listener.onArticlesUpdate(createMockArticles(pageSize));
        }
    }

    private List<Article> createMockArticles(int pageSize) {
        List<Article> articles = new ArrayList<>();
        for (int x = 0; x <= pageSize; x++) {
            Article article = new Article();
            article.author = "Article" + x + "author";
            article.description = "Lorem ipsum leorem dolor lorem ipsums lorem dolor lorem";
            article.publishedAt = "12/08/2019";
            Source source = new Source();
            source.category = "Source " + x + "category";
            source.description = "Source " + x + "description";
            source.name = "Source " + x + "name";
            source.language = "en";
            source.country = "c" + 1;
            article.source = source;
            article.title = "Article" + x + "title";
            articles.add(article);
        }
        return articles;
    }
}
