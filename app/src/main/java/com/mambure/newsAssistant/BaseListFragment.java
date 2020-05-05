package com.mambure.newsAssistant;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mambure.newsAssistant.newsActivity.NewsListFragment;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String SOURCE = "Fragment Id";
    protected String source;

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    @BindView(R.id.txtMessage)
    public TextView errorMessageTextView;

    @BindView(R.id.swipeRefresh)
    public SwipeRefreshLayout swipeRefreshLayout;

    public static BaseListFragment newInstance(String arg) {
        Bundle bundle = new Bundle();
        bundle.putString(SOURCE, arg);
        NewsListFragment newsFragment = new NewsListFragment();
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            source = bundle.getString(SOURCE);
        }
    }

    protected void hideProgessBar() {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    protected void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    protected void showStatusMessage(String message) {
        hideProgessBar();
        errorMessageTextView.setVisibility(View.VISIBLE);
        errorMessageTextView.setText(message);
    }

    public void hideErrorMessage() {
        errorMessageTextView.setVisibility(View.GONE);
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        errorMessageTextView.setVisibility(View.GONE);
    }
}
