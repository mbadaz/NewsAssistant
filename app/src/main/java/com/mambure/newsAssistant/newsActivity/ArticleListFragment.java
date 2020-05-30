package com.mambure.newsAssistant.newsActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.mambure.newsAssistant.BaseListFragment;
import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.R;
import com.mambure.newsAssistant.customTabs.CustomTabActivityHelper;
import com.mambure.newsAssistant.data.models.Article;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.dependencyInjection.ViewModelsFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleListFragment extends BaseListFragment implements ArticlesAdapter.OnItemClickListener,
    PopupMenu.OnMenuItemClickListener{
    private NewsActivityViewModel mViewModel;
    @Inject
    public ViewModelsFactory viewModelsFactory;
    private ArticlesAdapter adapter;
    @BindView(R.id.rv_new_articles_list) public RecyclerView recyclerView;
    private Snackbar snackbar;
    private LiveData<ArticlesResult> articlesStream;
    private LiveData<Boolean> articleActionStatusLiveData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((NewsActivity) requireActivity()).component.inject(this);
        mViewModel = new ViewModelProvider(requireActivity(), viewModelsFactory).
                get(NewsActivityViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_list,
                container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ArticlesAdapter(this);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        showProgressBar();
        hideStatusMessage();

        articlesStream = mViewModel.getArticlesStream(fragmentId);
        articlesStream.observe(this, articlesResult -> {
            if (articlesResult.result == Constants.Result.OK) {
                adapter.addItems(articlesResult.articles);
                hideProgessBar();
                hideStatusMessage();
            } else if (articlesResult.result == Constants.Result.ERROR) {
                showStatusMessage(getResources().getString(R.string.requestErrorMessage));
                hideProgessBar();
            } else if (articlesResult.result == Constants.Result.NO_DATA) {
                showStatusMessage(getResources().getString(R.string.requestNoData));
                hideProgessBar();
            }
        });
            mViewModel.getArticles(fragmentId);
    }

    @Override
    public void onStop() {
        mViewModel.cleanUp();
        if(articlesStream != null) articlesStream.removeObservers(this);
        if(articleActionStatusLiveData != null) articleActionStatusLiveData.removeObservers(this);
        adapter.clearData();
        super.onStop();
    }

    @Override
    public void onItemClick(View view, Article article) {
        mViewModel.setCurrentArticleToProcess(article);
        int id = view.getId();
        switch (id){
            case R.id.article_list_item:
                openArticle(article.url);
                break;
            case R.id.article_menu:
                openPopupMenu(view);
                break;
        }
    }

    private void openArticle(String url) {
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder(customTabActivityHelper.getSession()).
                setToolbarColor(getResources().getColor(R.color.colorPrimary, null)).
                addDefaultShareMenuItem().
                build();

        CustomTabActivityHelper.openCustomTab(requireActivity(), customTabsIntent, Uri.parse(url),
                (activity, uri) -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                });

    }

    private void openPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.article_popup_menu);
        if(fragmentId == Constants.FragmentId.NEW_ARTICLES) popupMenu.getMenu().removeItem(R.id.popup_menu_delete);
        else popupMenu.getMenu().removeItem(R.id.popup_menu_save);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public void onRefresh() {
        mViewModel.refresh(fragmentId);
        super.onRefresh();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popup_menu_save:
               articleActionStatusLiveData = mViewModel.saveArticle();
               articleActionStatusLiveData.observe(this, isSuccessful -> {
                   if (isSuccessful) {
                       snackbar = Snackbar.make(recyclerView, R.string.snackbar_articleSaved, Snackbar.LENGTH_SHORT);
                   }else {
                       snackbar = Snackbar.make(recyclerView, R.string.snackbar_error_saving, Snackbar.LENGTH_SHORT);
                   }
                   snackbar.show();
               });
               break;
            case R.id.popup_menu_delete:
                articleActionStatusLiveData = mViewModel.deleteArticle();
                articleActionStatusLiveData.observe(this, isSuccessful ->{
                    if (isSuccessful) {
                        adapter.removeItem(mViewModel.getCurrentArticle());
                        snackbar = Snackbar.make(recyclerView, R.string.snackbar_articleDeleted, Snackbar.LENGTH_SHORT);
                        snackbar.setAction(R.string.snackbar_action_undo, v ->{
                             articleActionStatusLiveData = mViewModel.saveArticle();
                             articleActionStatusLiveData.observe(this, isSuccessful2 -> {
                                 adapter.addItem(mViewModel.getCurrentArticle());
                             });
                            snackbar.dismiss();
                        });
                    } else {
                        snackbar = Snackbar.make(recyclerView, R.string.snackbar_error_deleting, Snackbar.LENGTH_SHORT);
                    }
                    snackbar.show();
                });
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
