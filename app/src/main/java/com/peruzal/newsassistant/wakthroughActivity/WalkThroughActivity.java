package com.peruzal.newsassistant.wakthroughActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.peruzal.newsassistant.MyApplicaction;
import com.peruzal.newsassistant.dependencyInjection.AppComponent;
import com.peruzal.newsassistant.dependencyInjection.ViewModelsFactory;
import com.peruzal.newsassistant.newsActivity.NewsActivity;
import com.peruzal.newsassistant.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalkThroughActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.viewpager_walkthrough) ViewPager viewPager;
    @BindView(R.id.tab_circle) View circleIndicator1;
    @BindView(R.id.tab_circle2) View circleIndicator2;
    @BindView(R.id.tab_circle3) View circleIndicator3;
    @BindView(R.id.txt_finish) TextView txtFinish;
    @BindView(R.id.txt_skip) TextView txtSkip;

    @Inject
    ViewModelsFactory viewModelsFactory;
    WalkthroughActivityViewModel viewModel;

    private int currentPosition = 0;
    private static WalkThroughViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_through);
        ButterKnife.bind(this);

        ((MyApplicaction) getApplication()).getComponent().inject(this);

        viewModel = new ViewModelProvider(this, viewModelsFactory).get(WalkthroughActivityViewModel.class);

        viewModel.getSourcesStream().observe(this, sourcesResult -> Log.d(WalkThroughActivity.class.getSimpleName(), sourcesResult.sources.toString()));
        viewModel.fetchSources();
        txtFinish.setOnClickListener(this);
        txtSkip.setOnClickListener(this);

        adapter = new WalkThroughViewPagerAdapter(getSupportFragmentManager(), initializeFragments());
        viewPager.setAdapter(adapter);
    }

    private Fragment[] initializeFragments() {
        Fragment[] fragments = new Fragment[3];
        fragments[0] = new WalkthroughWelcomeFragment();
        fragments[1] = new WalkthroughSourcesFragment();
        fragments[2] = new WalkthroughFinishFragment();
        return fragments;
    }

    private void updatePositionIndicators(int position) {
        switch (position) {
            case 0:
                circleIndicator1.setBackground(getResources().getDrawable(R.drawable.circle_accent));
                circleIndicator2.setBackground(getResources().getDrawable(R.drawable.circle_blue));
                circleIndicator3.setBackground(getResources().getDrawable(R.drawable.circle_blue));
                break;
            case 1:
                circleIndicator2.setBackground(getResources().getDrawable(R.drawable.circle_accent));
                circleIndicator3.setBackground(getResources().getDrawable(R.drawable.circle_blue));
                circleIndicator1.setBackground(getResources().getDrawable(R.drawable.circle_blue));
                txtFinish.setVisibility(View.INVISIBLE);
                break;
            case 2:
                circleIndicator3.setBackground(getResources().getDrawable(R.drawable.circle_accent));
                circleIndicator1.setBackground(getResources().getDrawable(R.drawable.circle_blue));
                circleIndicator2.setBackground(getResources().getDrawable(R.drawable.circle_blue));
                txtFinish.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_skip:
                Intent intent = new Intent(this, NewsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.txt_finish:
                saveUserData();
                break;
        }
    }

    private void saveUserData() {

    }
}
