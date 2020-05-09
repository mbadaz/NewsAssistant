package com.mambure.newsAssistant;

import android.content.Context;

import androidx.room.Room;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import com.mambure.newsAssistant.data.local.AppDatabase;
import com.mambure.newsAssistant.data.local.LocalDataRepository;
import com.mambure.newsAssistant.data.models.Article;
import com.mambure.newsAssistant.data.models.Source;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@RunWith(AndroidJUnit4ClassRunner.class)
public class RoomDatabaseTests {

    private Context context;
    private LocalDataRepository localDataRepository;

    @Before
    public void initialize() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        localDataRepository = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).
                build().localDatabaseDAO();
    }

    @Test
    public void saveAndRetrieveSourcesTests() {
        // Given
        List<Source> sources = TestMockingUtils.generateMockSources();
        List<Source> retrievedSources = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(2);

        // When
       localDataRepository.saveSources(sources).subscribe(latch::countDown);
       localDataRepository.getSources().subscribe(sources1 -> {
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
        localDataRepository.saveArticle(articles.get(0));
        for (Article article : articles) {
                localDataRepository.saveArticle(article).
                        subscribe(aLong -> countDownLatch.countDown());
            }

        localDataRepository.getArticles().subscribe(articles1 -> {
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
