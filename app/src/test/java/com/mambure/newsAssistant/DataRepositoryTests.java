package com.mambure.newsAssistant;

import com.mambure.newsAssistant.data.DataRepository;
import com.mambure.newsAssistant.data.local.LocalRepositoryDAO;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.remote.NewsRepository;
import com.mambure.newsAssistant.data.remote.NewsService;
import com.mambure.newsAssistant.data.remote.RemoteRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class DataRepositoryTests {
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock public NewsService newsService;
    @Mock public RemoteRepository remoteRepository;
    @Mock public LocalRepositoryDAO localRepositoryDAO;
    @Mock public Call<ArticlesResult> articlesResultCall;
    DataRepository dataRepository;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);
        dataRepository = new DataRepository(
                localRepositoryDAO, new NewsRepository(newsService)
        );
    }

    @Test
    public void getArticlesFromRemoteTest() {
        // Given
        Map<String, String> params = new HashMap<>();
        params.put("sources", "associated-press");
        Mockito.when(newsService.getArticles(params)).thenReturn(articlesResultCall);

        // Perform
        dataRepository.fetchArticles(params, Constants.REMOTE);

        // Verify
        Mockito.verify(newsService).getArticles(params);
    }

    @Test
    public void getArticlesFromLocalTest() {
        // Given
        Map<String, String> params = new HashMap<>();
        params.put("sources", "associated-press");

        // Perform
        dataRepository.fetchArticles(params, Constants.LOCAL);

        // Verify
        Mockito.verify(localRepositoryDAO).getAllArticles();

    }
}
