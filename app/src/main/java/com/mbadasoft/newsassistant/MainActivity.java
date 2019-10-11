package com.mbadasoft.newsassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mbadasoft.newsassistant.fragments.NewsFragment;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String MY_NEWS = "My News";
    public static final String HEADLINES = "Headlines";
    public static final String SAVED = "Saved";

    MainActivityViewModel viewModel;

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
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.menu_item_your_news);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_item_your_news:
                openFragement(NewsFragment.newInstance(MY_NEWS));
                break;
            case R.id.menu_item_headlines:
                openFragement(NewsFragment.newInstance(HEADLINES));
                break;
            case R.id.menu_item_saved:
                openFragement(NewsFragment.newInstance(SAVED));
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
