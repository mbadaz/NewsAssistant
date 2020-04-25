package com.mambure.newsAssistant.dependencyInjection;

import com.mambure.newsAssistant.newsActivity.NewsActivity;
import com.mambure.newsAssistant.newsActivity.NewsFragment;
import com.mambure.newsAssistant.wakthroughActivity.WalkThroughActivity;
import com.mambure.newsAssistant.wakthroughActivity.WalkthroughSourcesFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, WalkthroughModule.class, DataRepositoryModule.class, SharedPreferenceModule.class})
public interface AppComponent {
    NewsActivityComponent.Factory newsActivityComponent();

    WalkThroughComponent.Factory walkThroughActivityComponent();

}
