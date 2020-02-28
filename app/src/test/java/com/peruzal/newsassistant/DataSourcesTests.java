package com.peruzal.newsassistant;

import com.peruzal.newsassistant.data.remote.NewsService;
import com.peruzal.newsassistant.data.remote.RemoteDataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

@RunWith(JUnit4.class)
public class DataSourcesTests {

    NewsService newsService;
    RemoteDataSource articleDataSource;

    @Before
    public void initialize() {
        newsService = new Retrofit.Builder().
                addConverterFactory(MoshiConverterFactory.create()).
                baseUrl(Constants.BASE_URL).build().create(NewsService.class);
        articleDataSource = new RemoteDataSource(newsService);
    }

    @Test
    public void getArticlesTest() {
        Map<String, String> params = new HashMap<>();
        params.put("sources", "associated-press");


    }
}
