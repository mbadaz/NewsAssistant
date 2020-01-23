package com.mambure.newsassistant.data;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mambure.newsassistant.models.Article;
import com.mambure.newsassistant.models.ArticlesResult;
import com.mambure.newsassistant.models.Source;
import com.mambure.newsassistant.models.SourcesResult;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

public class LocalDataRepository implements DataRepository {

    private Realm realm;
    private UpdatesListener listener;
    private MutableLiveData<SourcesResult> sourcesLiveData = new MutableLiveData<>();
    private MutableLiveData<ArticlesResult> articlesLiveData = new MutableLiveData<>();
    private Map<String, Object> newsPrefs;
    private SharedPreferences sharedPreferences;
    private RealmResults<Article> results;

    @Inject
    public LocalDataRepository(SharedPreferences preferences, Realm realm) {
        this.realm = realm;
        sharedPreferences = preferences;

    }

    @Override
    public LiveData<SourcesResult> getSources() {
        realm.where(Source.class).findAllAsync().addChangeListener(sources -> {
            SourcesResult result = new SourcesResult();
            result.sources = sources;
            result.status = "ok";
            sourcesLiveData.postValue(result);
        });
        return sourcesLiveData;
    }

    @Override
    public LiveData<ArticlesResult> getArticles(String id, Map<String, Object> args) {
        if (id == null && args == null) {

        }
        results = realm.where(Article.class).findAllAsync();
        results.addChangeListener(articles -> {
            ArticlesResult result = new ArticlesResult();
            result.articles = articles;
            result.status = "ok";
            articlesLiveData.postValue(result);
        });

        return articlesLiveData;
    }

    @Override
    public void setUpdatesListener(UpdatesListener listener) {
        this.listener = listener;
    }

    @Override
    public void savePreferredSources(Set<Source> sources) {
        realm.executeTransactionAsync(realm1 -> realm1.insert(sources));
    }

    @Override
    public void savePreferredCategories(Set<String> categories) {
        sharedPreferences.edit().
                putStringSet("categories", categories).commit();
    }

    @Override
    public void savePreferredLanguages(Set<String> languages) {
        sharedPreferences.edit().
                putStringSet("languages", languages).commit();
    }

    @Override
    public void saveArticle(Article article) {
        realm.executeTransaction(realm1 -> realm1.insert(article));
    }


    @Override
    public Set<String> getPreferredLanguages() {
        Set<String> defaultLanguages = new HashSet<>();
        defaultLanguages.add(Locale.getDefault().getLanguage());
        return sharedPreferences.getStringSet("languages",defaultLanguages);
    }

    @Override
    public Set<String> getPreferredCategories() {
        Set<String> defaultCategories = new HashSet<>();
        defaultCategories.add("general");
        return sharedPreferences.getStringSet("categories", defaultCategories);

    }

    @Override
    public boolean getIsFirstTimeLaunch() {
        return sharedPreferences.getBoolean("IsFirstTimeLogin", true);
    }

    @Override
    public void isFirstTimeLaunch(boolean value) {
        sharedPreferences.edit().putBoolean("IsFirstTimeLogin", value).commit();
    }

    @Override
    public void setEnableExternalBrowser(boolean value) {
        sharedPreferences.edit().putBoolean("EnableExternalBrowser", value).commit();
    }

    @Override
    public boolean isExternalBrowserEnabled() {
        return sharedPreferences.getBoolean("EnableExternalBrowser", false);
    }

    @Override
    public void setArticlesLoadingLimit(int value) {
        sharedPreferences.edit().putInt("ArticlesLoadingLimit", value).commit();
    }

    @Override
    public int getArticlesLoadingLimit() {
        return sharedPreferences.getInt("ArticlesLoadingLimit", 20);
    }

}
