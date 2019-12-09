package com.mbadasoft.newsassistant.wakthroughActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mbadasoft.newsassistant.MyApplication;
import com.mbadasoft.newsassistant.dependencyInjection.ViewModelsFactory;
import com.mbadasoft.newsassistant.newsActivity.NewsActivity;
import com.mbadasoft.newsassistant.R;

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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        txtFinish.setOnClickListener(this);
        txtSkip.setOnClickListener(this);
    }

    private Fragment[] initializeFragments() {
        Fragment[] fragments = new Fragment[3];
        fragments[0] = new WalkthroughSplashScreenFragment();
        fragments[1] = new WalkthroughCategorySelectionFragment();
        fragments[2] = new WalkthroughSourcesSelectionFragment();
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
        viewModel.saveUserData();
        Intent intent1 = new Intent(this, NewsActivity.class);
        startActivity(intent1);
        finish();
    }
}
