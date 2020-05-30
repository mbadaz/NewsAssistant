package com.mambure.newsAssistant;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;
import android.widget.TextView;

import com.mambure.newsAssistant.Constants.FragmentId;
import com.mambure.newsAssistant.customTabs.CustomTabActivityHelper;
import com.mambure.newsAssistant.newsActivity.ArticleListFragment;
import com.mambure.newsAssistant.newsActivity.NewsActivity;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    protected static final String FRAGMENT_ID = "Fragment Id";
    protected FragmentId fragmentId;
    @BindView(R.id.txtMessage)
    public TextView errorMessageTextView;
    @BindView(R.id.swipeRefresh)
    public SwipeRefreshLayout swipeRefreshLayout;
    protected CustomTabActivityHelper customTabActivityHelper;

    public static BaseListFragment newInstance(String arg) {
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_ID, arg);
        ArticleListFragment newsFragment = new ArticleListFragment();
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            fragmentId = FragmentId.valueOf(bundle.getString(FRAGMENT_ID));
        }
        FragmentActivity activity = requireActivity();
        if(activity instanceof NewsActivity) customTabActivityHelper = ((NewsActivity) activity).getCustomTabActivityHelper();
    }

    protected void hideProgessBar() {
        swipeRefreshLayout.setRefreshing(false);
    }

    protected void showProgressBar() {
       swipeRefreshLayout.setRefreshing(true);
    }

    protected void showStatusMessage(String message) {
        errorMessageTextView.setVisibility(View.VISIBLE);
        errorMessageTextView.setText(message);
    }

    protected void hideStatusMessage() {
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
