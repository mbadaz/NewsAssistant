package com.mbadasoft.newsassistant.wakthroughActivity;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.mbadasoft.newsassistant.data.AppNewsRepository;
import com.mbadasoft.newsassistant.data.AppPreferencesRepository;
import com.mbadasoft.newsassistant.data.DataController;
import com.mbadasoft.newsassistant.models.Category;
import com.mbadasoft.newsassistant.models.Source;
import com.mbadasoft.newsassistant.models.SourcesResult;

import java.util.HashSet;
import java.util.Set;

public class WalkthroughActivityViewModel extends AndroidViewModel {

    private Set<String> selectedSources = new HashSet<>();
    private Set<String> selectedCategories = new HashSet<>();

    private DataController dataController;

    public WalkthroughActivityViewModel(@NonNull Application application) {
        super(application);
        dataController = new DataController(
                new AppNewsRepository(application),
                new AppPreferencesRepository(application));
    }

    LiveData<SourcesResult> getAvailableSources() {
        return dataController.getSources();
    }

    boolean IsFirstTimeLogin() {
        return dataController.IsFirstTimeLogin();
    }

    void setIsFirstTimeLogin(boolean value) {
        dataController.setIsFirstTimeLogin(value);
    }

    void saveUserData() {
        dataController.savePreferredSources(selectedSources);
        dataController.savePreferredCategories(selectedCategories);
    }

    void addSourceToSelection(Source source) {
        selectedSources.add(source.id);
    }

    void removeSourceFromSelection(Source source) {
        selectedSources.remove(source.id);
    }

    void addCategoryToSelection(Category category) {
        selectedCategories.add(category.title);
    }

    void removeCategoryToSelected(Category category) {
        selectedCategories.remove(category.title);

    }

    public Set<String> getSelectedCategories() {
        return selectedCategories;
    }
}
