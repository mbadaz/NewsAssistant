package com.mambure.newsAssistant;

import android.content.Context;

import androidx.room.Room;

import com.mambure.newsAssistant.data.DataRepository;
import com.mambure.newsAssistant.data.local.AppDatabase;
import com.mambure.newsAssistant.data.local.LocalRepositoryDAO;
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
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.reactivex.schedulers.Schedulers;

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
        LocalRepositoryDAO localRepositoryDAO = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).
                build().localDatabaseDAO();

        MockitoAnnotations.initMocks(this);
        dataRepository = new DataRepository(
                localRepositoryDAO, newsService
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
        // Given
        List<Source> sources = TestMockingUtils.generateMockSources();
        List<Source> retrievedSources = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);
        // When
        dataRepository.saveSources(sources);


        // Verify
        Assert.assertEquals(sources, retrievedSources);

    }
}
