package com.mbadasoft.newsassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mbadasoft.newsassistant.data.AppNewsRepository;
import com.mbadasoft.newsassistant.fragments.FragmentMyNews;
import com.mbadasoft.newsassistant.models.ArticlesResult;
import com.mbadasoft.newsassistant.models.SourcesResult;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    MainActivityViewModel viewModel;

    @BindView(R.id.main_fragment_container)
    FrameLayout frameLayout;

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        openFragement("Your News");
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_item_your_news:
                openFragement("Your News");
                break;
            case R.id.menu_item_headlines:
                openFragement("Headlines");
                break;
            case R.id.menu_item_saved:
                openFragement("Saved");
        }
        return false;
    }

    private void openFragement(String arg) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, FragmentMyNews.newInstance(arg));
        transaction.commit();
    }
}
