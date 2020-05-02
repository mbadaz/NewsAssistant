package com.mambure.newsAssistant.dependencyInjection;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, WalkthroughModule.class, DataManagerModule.class, SharedPreferenceModule.class})
public interface AppComponent {
    NewsActivityComponent.Factory newsActivityComponent();

    WalkThroughComponent.Factory walkThroughActivityComponent();

}
