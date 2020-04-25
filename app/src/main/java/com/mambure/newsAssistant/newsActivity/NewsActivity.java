package com.mambure.newsAssistant.newsActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mambure.newsAssistant.BaseFragment;
import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.MyApplication;
import com.mambure.newsAssistant.R;
import com.mambure.newsAssistant.dependencyInjection.NewsActivityComponent;

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

    private void openFragement(BaseFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, fragment);
        transaction.addToBackStack(fragment.getTag());
        transaction.setReorderingAllowed(true);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_headlines:
                openFragement(NewsFragment.newInstance(Constants.REMOTE));
                break;
            case R.id.menu_item_saved:
                openFragement(NewsFragment.newInstance(Constants.LOCAL));
        }
        return false;
    }
}
