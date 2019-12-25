package com.mambure.newsassistant.newsActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mambure.newsassistant.MyApplication;
import com.mambure.newsassistant.R;
import com.mambure.newsassistant.dependencyInjection.ViewModelsFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mambure.newsassistant.data.Constants.FOLLOWED_STORIES_FRAGMENT;
import static com.mambure.newsassistant.data.Constants.MY_NEWS_FRAGMENT;
import static com.mambure.newsassistant.data.Constants.SAVED_STORIES_FRAGMENT;

public class NewsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = NewsActivity.class.getSimpleName();
    private NewsActivityViewModel viewModel;
    @Inject
    public ViewModelsFactory viewModelsFactory;

    @BindView(R.id.main_fragment_container)
    FrameLayout frameLayout;

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    private final FragmentManager supportFragmentManager = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((MyApplication)getApplication()).getAppComponent().inject(this);
        viewModel = ViewModelProviders.of(this,viewModelsFactory).get(NewsActivityViewModel.class);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navMenuItemMyNews);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navMenuItemMyNews:
                openFragement(NewsFragment.newInstance(MY_NEWS_FRAGMENT));
                break;
            case R.id.navMenuItemFollowed:
                openFragement(NewsFragment.newInstance(FOLLOWED_STORIES_FRAGMENT));
                break;
            case R.id.navMenuItemSaved:
                openFragement(NewsFragment.newInstance(SAVED_STORIES_FRAGMENT));
        }
        return true;
    }

    private void openFragement(Fragment fragment) {
//        frameLayout.removeAllViews();
//        Fragment fragment = supportFragmentManager.findFragmentByTag(args);
//        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
//        if (fragment == null) {
//            fragment = NewsFragment.newInstance(args);
//            transaction.add(R.id.main_fragment_container, fragment, args);
//        } else {
//            transaction.replace(R.id.main_fragment_container, fragment, args);
//        }
        supportFragmentManager.beginTransaction().
                replace(R.id.main_fragment_container, fragment).
                addToBackStack(null).commit();
    }
}
