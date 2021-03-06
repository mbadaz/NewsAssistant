package com.mambure.newsassistant.newsActivity;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mambure.newsassistant.MyApplication;
import com.mambure.newsassistant.R;
import com.mambure.newsassistant.dependencyInjection.ViewModelsFactory;
import com.mambure.newsassistant.models.Article;

import javax.inject.Inject;

public class NewsFragment extends Fragment implements ArticlesAdapter.ItemMenuClickListener {
    public static final String FRAGMENT_ID = "Fragment Id";


    private NewsActivityViewModel mViewModel;
    @Inject
    public ViewModelsFactory viewModelsFactory;

    private ArticlesAdapter adapter;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private String args;


    public static NewsFragment newInstance(String arg) {
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_ID, arg);
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            args = bundle.getString(FRAGMENT_ID);
        }

        ((MyApplication)getActivity().getApplication()).getAppComponent().inject(this);
        mViewModel = ViewModelProviders.of(getActivity(), viewModelsFactory).get(NewsActivityViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_list, container, false);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = getView().findViewById(R.id.progressbar_loading);
        recyclerView = getView().findViewById(R.id.rv_articles_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ArticlesAdapter();
        adapter.setItemMenuClickListener(this);
        recyclerView.setAdapter(adapter);
        mViewModel.getArticles(args).observe(this, articlesResult -> {
            if (articlesResult.status.equals("ok")) {
                adapter.addData(articlesResult.articles);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onItemMenuClick(Article article) {
        mViewModel.saveArticle(article);
    }
}
