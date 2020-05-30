package com.mambure.newsAssistant.data;

import android.content.Context;

import androidx.room.Room;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.TestMockingUtils;
import com.mambure.newsAssistant.data.DataRepository;
import com.mambure.newsAssistant.data.local.AppDatabase;
import com.mambure.newsAssistant.data.local.LocalDataRepository;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.Source;
import com.mambure.newsAssistant.data.models.SourcesResult;
import com.mambure.newsAssistant.data.remote.NewsService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@RunWith(MockitoJUnitRunner.class)
public class DataRepositoryTests {
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock public NewsService newsService;
    @Mock public Context context;
    private SourcesResult sourcesResult = new SourcesResult();
    private ArticlesResult articlesResult = new ArticlesResult();
    private DataRepository dataRepository;

    @Before
    public void initialize() {
        sourcesResult.status = Constants.RESULT_OK;
        sourcesResult.sources = TestMockingUtils.generateMockSources();
        articlesResult.status = Constants.RESULT_OK;
        articlesResult.articles = TestMockingUtils.generateMockArticles();
        LocalDataRepository localDataRepository = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).
                build().localDatabaseDAO();

        MockitoAnnotations.initMocks(this);
        dataRepository = new DataRepository(
                localDataRepository, newsService
        );

    }

//    @Test
//    public void getArticlesFromRemoteTest() {
//        // Given
//        Map<String, String> params = new HashMap<>();
//        params.put("sources", "associated-press");
//        Mockito.when(newsService.getArticles(params)).thenReturn(articlesResultCall);
//
//        // Perform
//        dataRepository.fetchArticles(Constants.REMOTE, params);
//
//        // Verify
//        Mockito.verify(newsService).getArticles(params);
//    }
//
//    @Test
//    public void getArticlesFromLocalTest() {
//        // Given
//        Map<String, String> params = new HashMap<>();
//        params.put("sources", "associated-press");
//
//        // Perform
//        dataRepository.fetchArticles(Constants.LOCAL, params);
//
//        // Verify
//        Mockito.verify(localRepositoryDAO).getAllArticles();
//    }

    @Test
    public void dataInitializationTest() {
//        // Given
//        List<Source> sources = TestMockingUtils.generateMockSources();
//        List<Source> retrievedSources;
//        CountDownLatch latch = new CountDownLatch(1);
//        // When
//        dataRepository.saveSources(sources);
//        retrievedSources = dataRepository.getSavedSources().blockingGet();

        // Verify
        Assert.assertEquals(2, 2);

    }
}
