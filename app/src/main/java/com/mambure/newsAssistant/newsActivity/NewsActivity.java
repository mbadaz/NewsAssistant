package com.mambure.newsAssistant.newsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mambure.newsAssistant.MyApplication;
import com.mambure.newsAssistant.R;
import com.mambure.newsAssistant.customTabs.CustomTabActivityHelper;
import com.mambure.newsAssistant.dependencyInjection.NewsActivityComponent;
import com.mambure.newsAssistant.wakthroughActivity.WalkThroughActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mambure.newsAssistant.Constants.*;

public class NewsActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener, CustomTabActivityHelper.ConnectionCallback
            {
    private static final String TAG = NewsActivity.class.getSimpleName();

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    NewsActivityComponent component;
    private CustomTabActivityHelper customTabActivityHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        component = ((MyApplication) getApplication()).getComponent().newsActivityComponent().create();
        ButterKnife.bind(this);

        customTabActivityHelper = new CustomTabActivityHelper();

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.menu_item_headlines);
    }

    private Fragment getFragment(String id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.findFragmentByTag(id);
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.show(fragment);
        transaction.commit();
    }

    private void hideFragment(Fragment fragment) {
        if(fragment == null) return;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragment);
        transaction.commit();
    }

    private boolean addFragment(String id) {
        Fragment fragment = getFragment(id);
        if (fragment == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (id.equals(REMOTE)){
                transaction.add(R.id.main_fragment_container, NewsArticleListFragment.newInstance(id), id);
            }
            else {
                transaction.add(R.id.main_fragment_container, SavedArticleListFragment.newInstance(id), id);
            }
            transaction.commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_headlines:
                if (!addFragment(REMOTE)) {
                    showFragment(getFragment(REMOTE));
                }
                hideFragment(getFragment(LOCAL));
                break;
            case R.id.menu_item_saved:
                if (!addFragment(LOCAL)) {
                    showFragment(getFragment(LOCAL));
                }
                hideFragment(getFragment(REMOTE));
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_news_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_preferred_sources:
                Intent intent = new Intent(this, WalkThroughActivity.class);
                intent.putExtra(WalkThroughActivity.LOADING_PAGE_POSITION,
                        WalkThroughActivity.SOURCES_PAGE_POSITION);
                startActivity(intent);
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        customTabActivityHelper.bindCustomTabsService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        customTabActivityHelper.unbindCustomTabsService(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        customTabActivityHelper.setConnectionCallback(null);
    }

    /**
     * Called when the service is connected.
     */
    @Override
    public void onCustomTabsConnected() {

    }

    /**
     * Called when the service is disconnected.
     */
    @Override
    public void onCustomTabsDisconnected() {

    }

    public CustomTabActivityHelper getCustomTabActivityHelper() {
        return customTabActivityHelper;
    }
}
