package com.mambure.newsAssistant;

import android.content.Context;

import androidx.room.Room;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import com.mambure.newsAssistant.data.DataRepository;
import com.mambure.newsAssistant.data.local.AppDatabase;
import com.mambure.newsAssistant.data.local.LocalRepositoryDAO;
import com.mambure.newsAssistant.data.models.Article;
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
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@RunWith(AndroidJUnit4ClassRunner.class)
public class RoomDatabaseTests {

    private Context context;
    private LocalRepositoryDAO localRepositoryDAO;

    @Before
    public void initialize() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        localRepositoryDAO = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).
                build().localDatabaseDAO();
    }

    @Test
    public void saveAndRetrieveSourcesTests() {
        // Given
        List<Source> sources = TestMockingUtils.generateMockSources();
        List<Source> retrievedSources = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(2);

        // When
       localRepositoryDAO.insertSources(sources).subscribe(latch::countDown);
       localRepositoryDAO.getAllSources().subscribe(sources1 -> {
            retrievedSources.addAll(sources1);
            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify
        Assert.assertEquals(sources, retrievedSources);
    }

    @Test
    public void saveAndRetrieveArticlesTest() {
        // Given
        List<Article> articles = TestMockingUtils.generateMockArticles();
        List<Article> retrievedArticles = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(6);

        // When
        localRepositoryDAO.insertArticle(articles.get(0));
        for (Article article : articles) {
                localRepositoryDAO.insertArticle(article).
                        subscribe(aLong -> countDownLatch.countDown());
            }

        localRepositoryDAO.getAllArticles().subscribe(articles1 -> {
            retrievedArticles.addAll(articles1);
            countDownLatch.countDown();
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify
        Assert.assertEquals(articles, retrievedArticles);

    }
}
