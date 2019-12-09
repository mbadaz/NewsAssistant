package com.mbadasoft.newsassistant.dependencyInjection;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.mbadasoft.newsassistant.data.DataController;
import com.mbadasoft.newsassistant.dependencyinjection.DataControllerModule;
import com.mbadasoft.newsassistant.newsActivity.NewsActivityViewModel;
import com.mbadasoft.newsassistant.wakthroughActivity.WalkthroughActivityViewModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.MapKey;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module(includes = {AppModule.class, DataControllerModule.class})
public class ViewModelsModule {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @MapKey
    @interface ViewModelKey{
        Class<? extends ViewModel> value();
    }

    @Provides
    @Singleton
    @IntoMap
    @ViewModelKey(WalkthroughActivityViewModel.class)
    ViewModel provideWalkhroughVieModel(DataController dataController) {
        return new WalkthroughActivityViewModel(dataController);
    }

    @Provides
    @Singleton
    @IntoMap
    @ViewModelKey(NewsActivityViewModel.class)
    ViewModel provideNewsActivityViewModel(Application application) {
        return new NewsActivityViewModel(application);
    }

    @Provides
    @Singleton
    ViewModelsFactory provideViewModelFactory(
            Map<Class<? extends ViewModel>, Provider<ViewModel>> providers) {
        return new ViewModelsFactory(providers);
    }
}
