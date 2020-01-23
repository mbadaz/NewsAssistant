package com.mambure.newsassistant;

import com.mambure.newsassistant.data.DataRepository;
import com.mambure.newsassistant.data.DefaultRepository;
import com.mambure.newsassistant.data.FakeRemoteNewsProvider;
import com.mambure.newsassistant.data.NewsProvider;
import com.mambure.newsassistant.data.SettingsStore;
import com.mambure.newsassistant.models.Source;
import com.mambure.newsassistant.wakthroughActivity.WalkthroughActivityViewModel;

import org.junit.Before;
import org.junit.Test;

public class WalkthroughViewModelTests {
    private static final String TEST_SOURCE_ID = "id";
    WalkthroughActivityViewModel viewModel;
    DefaultRepository defaultRepository;
    DataRepository dataRepository;
    NewsProvider newsDataProvider;

    @Before
    void initialiseVM() {

    }

    @Test
    void addSourceAndCheck() {
        Source source = new Source();
        source.id = TEST_SOURCE_ID;

    }
}
