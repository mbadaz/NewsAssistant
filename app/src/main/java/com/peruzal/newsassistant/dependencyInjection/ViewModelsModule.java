package com.peruzal.newsassistant.dependencyInjection;

import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;

import com.peruzal.newsassistant.data.DataRepository;
import com.peruzal.newsassistant.newsActivity.NewsActivityViewModel;
import com.peruzal.newsassistant.wakthroughActivity.WalkthroughActivityViewModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import javax.inject.Provider;

import dagger.MapKey;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module(includes = {RepositoriesModule.class})
public class ViewModelsModule {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @MapKey
    @interface ViewModelKey {
        Class<? extends ViewModel> value();
    }

    @Provides
    ViewModelsFactory providesViewModelsFactory(Map<Class<?extends ViewModel>, Provider<ViewModel>> providerMap) {
        return new ViewModelsFactory(providerMap);
    }

    @Provides
    @IntoMap
    @ViewModelKey(WalkthroughActivityViewModel.class)
    ViewModel providesWalkthroughViewModel(DataRepository dataRepository, SharedPreferences sharedPreferences) {
        return new WalkthroughActivityViewModel(dataRepository, sharedPreferences);
    }

    @Provides
    @IntoMap
    @ViewModelKey(NewsActivityViewModel.class)
    ViewModel providesNewsActivityViewModel(DataRepository dataRepository, SharedPreferences sharedPreferences) {
        return new NewsActivityViewModel(dataRepository, sharedPreferences);
    }
}
