package com.mambure.newsAssistant;

import android.content.Context;

import androidx.room.Room;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.mambure.newsAssistant.data.DataRepository;
import com.mambure.newsAssistant.data.local.AppDatabase;
import com.mambure.newsAssistant.data.local.LocalRepositoryDAO;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.SourcesResult;
import com.mambure.newsAssistant.data.remote.NewsRepository;
import com.mambure.newsAssistant.data.remote.NewsService;
import com.mambure.newsAssistant.data.remote.RemoteRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;

@RunWith(AndroidJUnit4ClassRunner.class)
public class DataRepositoryTests {
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock public NewsService newsService;
    @Mock public RemoteRepository remoteRepository;
    @Mock public LocalRepositoryDAO localRepositoryDAO;
    @Mock public Call<ArticlesResult> articlesResultCall;
    @Mock public Call<SourcesResult> sourcesResultCall;
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
        localRepositoryDAO = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).
                                build().localDatabaseDAO();

        MockitoAnnotations.initMocks(this);
        dataRepository = new DataRepository(
                localRepositoryDAO, new NewsRepository(newsService)
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
        dataRepository.saveSources(sourcesResult.sources);
        final CountDownLatch latch = new CountDownLatch(1);
        final SourcesResult sourcesResultFromLocal = null;
        dataRepository.getSourcesStream().observeForever(sourcesResult -> {
            sourcesResultFromLocal.sources = sourcesResult.sources;
            latch.countDown();
        });

        // Perform
        dataRepository.fetchSources(Constants.LOCAL);

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify
        Assert.assertEquals(sourcesResultFromLocal.sources, sourcesResult.sources);
    }
}
