package com.mambure.newsassistant;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import com.mambure.newsassistant.data.LocalDataRepository;
import com.mambure.newsassistant.data.MockSharedPreferences;
import com.mambure.newsassistant.models.Article;
import com.mambure.newsassistant.models.ArticlesResult;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import io.realm.Realm;
import io.realm.RealmConfiguration;

@RunWith(AndroidJUnit4ClassRunner.class)
public class LocalDatabaseTests {

    public Realm realm;


    @Before
    public void initDatabase() {
       Context context = InstrumentationRegistry.getInstrumentation().getContext();
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder().
                name("Test").
                inMemory().
                build();
        realm = Realm.getInstance(config);
    }

    @After
    public void closeRealm() {
        realm.beginTransaction();
        realm.delete(Article.class);
        realm.commitTransaction();
        realm.close();
    }

    @Test
    public void saveCategoriesTest() {
        Set<String> cats = new HashSet<>();
        cats.add("Tech");
        LocalDataRepository repository = new LocalDataRepository(new MockSharedPreferences(), realm);
        repository.savePreferredCategories(cats);

        Assert.assertTrue(repository.getPreferredCategories().contains("Tech"));

    }

    @Test
    public void saveArticleTest() throws InterruptedException {
        Article article = new Article();
        article.title = "tapiwa";
        article.author = "muzira";

        Article article2 = new Article();
        article.title = "tapiwa2";
        article.author = "muzira2";

        LocalDataRepository repository = new LocalDataRepository(new MockSharedPreferences(), realm);
//        repository.saveArticle(article);
//        repository.saveArticle(article2);

        LiveData<ArticlesResult> result = repository.getArticles(null, null);

        final List<Article> articles = new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(1);

        result.observeForever(new Observer<ArticlesResult>() {
            @Override
            public void onChanged(ArticlesResult articlesResult) {
                articles.addAll(articlesResult.articles);
                latch.countDown();
            }
        });
        latch.await();
        Assert.assertEquals(article.title, articles.get(0).author);
        Assert.assertEquals(article2.title, articles.get(1).author);

    }




}
