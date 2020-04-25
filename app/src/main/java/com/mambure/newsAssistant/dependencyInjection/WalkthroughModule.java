package com.mambure.newsAssistant.dependencyInjection;

import dagger.Module;

@Module (includes = {ViewModelsModule.class}, subcomponents = {WalkThroughComponent.class})
public class WalkthroughModule {

}
