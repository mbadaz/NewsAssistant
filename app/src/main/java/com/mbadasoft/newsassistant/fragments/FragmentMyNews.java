package com.mbadasoft.newsassistant.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
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

import com.mbadasoft.newsassistant.MainActivityViewModel;
import com.mbadasoft.newsassistant.R;
import com.mbadasoft.newsassistant.adapters.ArticlesAdapter;
import com.mbadasoft.newsassistant.models.ArticlesResult;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentMyNews extends Fragment {

    private MainActivityViewModel mViewModel;
    private ArticlesAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private String args;


    public static FragmentMyNews newInstance(String args) {
        Bundle bundle = new Bundle();
        bundle.putString("TYPE", args);
        FragmentMyNews fragmentMyNews = new FragmentMyNews();
        fragmentMyNews.setArguments(bundle);
        return fragmentMyNews;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            args = getArguments().getString("TYPE");
        }
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
        recyclerView.setAdapter(adapter);
        mViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        mViewModel.getArticles().observe(this, articlesResult -> {
            if (articlesResult.status.equals("ok")) {
                adapter.addData(articlesResult.articles);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
