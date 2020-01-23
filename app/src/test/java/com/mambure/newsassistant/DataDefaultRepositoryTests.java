package com.mambure.newsassistant;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.mambure.newsassistant.data.Constants;
import com.mambure.newsassistant.data.DataRepository;
import com.mambure.newsassistant.data.DefaultRepository;
import com.mambure.newsassistant.data.NewsProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataDefaultRepositoryTests {

    private static Map<String,Object> args = new HashMap<>();
    static {
        args.put(Constants.PAGE_SIZE_QUERY_PARAM, 10);
        List<String> dummySources = Arrays.asList("fakenews.com", "tabloids.com");
        Set<String> sources = new HashSet<>(dummySources);
        args.put(Constants.SOURCES_QUERY_PARAM, sources);
    }

    private DefaultRepository defaultRepository;

    @Mock
    public NewsProvider newsProvider;

    @Mock
    public DataRepository dataRepository;

    @Captor
    public ArgumentCaptor<Map<String, Object>> apiArgsArgumentCaptor;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initializeRepository() {
        defaultRepository = new DefaultRepository(newsProvider, dataRepository);
        newsProvider.setUpdatesListener(defaultRepository);
    }

    @Test
    public void getArticlesFromRemoteTest()  {

        // WHEN
        defaultRepository.getArticles("fragment", args);

        // VERIFY
        Mockito.verify(newsProvider).getArticles(eq("fragment"), eq(args));

    }

    @Test
    public void getSavedArticles() {
        //GIVEN
        String id = Constants.SAVED_STORIES_FRAGMENT;

        // WHEN
        defaultRepository.getArticles(id, args);

        // VERIFY
        Mockito.verify(dataRepository).getArticles(eq(null), eq(args));

    }

}
