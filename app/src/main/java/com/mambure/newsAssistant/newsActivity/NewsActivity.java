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
import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.MyApplication;
import com.mambure.newsAssistant.R;
import com.mambure.newsAssistant.dependencyInjection.NewsActivityComponent;
import com.mambure.newsAssistant.wakthroughActivity.WalkThroughActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = NewsActivity.class.getSimpleName();

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    NewsActivityComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        component = ((MyApplication) getApplication()).getComponent().newsActivityComponent().create();
        ButterKnife.bind(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.menu_item_headlines);
    }

    private Fragment getFragment(String id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(id);
        if(fragment == null) {
           return NewsListFragment.newInstance(id);
        }
        return fragment;
    }

    private void showFragment(Fragment fragment, String id) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, fragment, id);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_headlines:
                showFragment(getFragment(Constants.REMOTE), Constants.REMOTE);
                break;
            case R.id.menu_item_saved:
                showFragment(getFragment(Constants.LOCAL), Constants.LOCAL);
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
}
