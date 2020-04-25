package com.mambure.newsAssistant.dependencyInjection;

import com.mambure.newsAssistant.wakthroughActivity.WalkThroughActivity;
import com.mambure.newsAssistant.wakthroughActivity.WalkthroughActivityViewModel;
import com.mambure.newsAssistant.wakthroughActivity.WalkthroughSourcesFragment;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent()
public interface WalkThroughComponent {

    @Subcomponent.Factory
    interface Factory{
        WalkThroughComponent create();
    }
    void inject(WalkThroughActivity activity);
    void inject(WalkthroughSourcesFragment fragment);
}

