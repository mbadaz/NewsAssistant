package com.mambure.newsassistant.wakthroughActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mambure.newsassistant.MyApplication;
import com.mambure.newsassistant.dependencyInjection.ViewModelsFactory;
import com.mambure.newsassistant.newsActivity.NewsActivity;
import com.mambure.newsassistant.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalkThroughActivity extends AppCompatActivity {
    @BindView(R.id.viewpager_walkthrough) ViewPager viewPager;
    @BindView(R.id.tab_circle) View circleIndicator1;
    @BindView(R.id.tab_circle2) View circleIndicator2;
    @BindView(R.id.tab_circle3) View circleIndicator3;
    @BindView(R.id.tab_circle4) View circleIndicator4;
    View highlightedCircleIndicator;
    @BindView(R.id.txt_finish) TextView txtNext;
    @BindView(R.id.txt_skip) TextView txtSkip;


    private WalkthroughActivityViewModel viewModel;
    @Inject
    public ViewModelsFactory viewModelsFactory;

    private int currentPosition = 0;
    private static WalkThroughViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_through);
        ButterKnife.bind(this);
        ((MyApplication)getApplication()).getAppComponent().inject(this);

        viewModel = ViewModelProviders.of(this, viewModelsFactory).get(WalkthroughActivityViewModel.class);

        if (viewModel.IsFirstTimeLogin()) {
            viewModel.setIsFirstTimeLogin(false);
        } else {
            Intent intent = new Intent(this, NewsActivity.class);
            startActivity(intent);
            finish();

        }

        adapter = new WalkThroughViewPagerAdapter(getSupportFragmentManager(), initializeFragments());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                updatePositionIndicators(position);
                if (isCurrentLastItem(currentPosition)) {
                    txtNext.setText(R.string.label_walkthrough_activity_finish);
                } else if (isCurrentLastItem(currentPosition + 1)) {
                    txtNext.setText(R.string.label_walkthrough_activity_next);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private Fragment[] initializeFragments() {
        Fragment[] fragments = new Fragment[4];
        fragments[0] = new WalkthroughSplashScreenFragment();
        fragments[1] = new WalkthroughCategorySelectionFragment();
        fragments[2] = new WalkthroughSourcesSelectionFragment();
        fragments[3] = new WalkthroughLanguageSelectionFragment();
        return fragments;
    }

    private void updatePositionIndicators(int position) {

        if (highlightedCircleIndicator != null) {
            highlightedCircleIndicator.setBackground(getResources().getDrawable(R.drawable.circle_blue));
        }

        switch (position) {
            case 0:
                circleIndicator1.setBackground(getResources().getDrawable(R.drawable.circle_accent));
                highlightedCircleIndicator = circleIndicator1;
                break;
            case 1:
                circleIndicator2.setBackground(getResources().getDrawable(R.drawable.circle_accent));
                highlightedCircleIndicator = circleIndicator2;
                break;
            case 2:
                circleIndicator3.setBackground(getResources().getDrawable(R.drawable.circle_accent));
                highlightedCircleIndicator = circleIndicator3;
                break;
            case 3:
                circleIndicator4.setBackground(getResources().getDrawable(R.drawable.circle_accent));
                highlightedCircleIndicator = circleIndicator4;
                txtNext.setVisibility(View.VISIBLE);

        }
    }

    @OnClick(R.id.txt_finish)
    public void onNextClick() {
        if (!isCurrentLastItem(currentPosition)) {
            viewPager.setCurrentItem(currentPosition + 1, true);
            return;
        }

        saveUserData();
    }

    @OnClick(R.id.txt_skip)
    public void onSkipClick() {
        Intent intent = new Intent(this, NewsActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isCurrentLastItem(int currentPos) {
        return currentPos == adapter.fragments.length - 1;
    }

    private void saveUserData() {
        viewModel.saveUserData();
        Intent intent1 = new Intent(this, NewsActivity.class);
        startActivity(intent1);
        finish();
    }
}
