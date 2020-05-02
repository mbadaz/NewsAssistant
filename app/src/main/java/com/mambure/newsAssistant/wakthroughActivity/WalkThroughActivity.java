package com.mambure.newsAssistant.wakthroughActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.MyApplication;
import com.mambure.newsAssistant.R;
import com.mambure.newsAssistant.dependencyInjection.ViewModelsFactory;
import com.mambure.newsAssistant.dependencyInjection.WalkThroughComponent;
import com.mambure.newsAssistant.newsActivity.NewsActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalkThroughActivity extends AppCompatActivity implements View.OnClickListener,  ViewPager.OnPageChangeListener{
    @BindView(R.id.viewpager_walkthrough) ViewPager viewPager;
    @BindView(R.id.tab_indicator1) View circleIndicator1;
    @BindView(R.id.tab_indicator2) View circleIndicator2;
    @BindView(R.id.tab_indicator3) View circleIndicator3;
    @BindView(R.id.txt_finish) TextView txtFinish;
    @BindView(R.id.txt_skip) TextView txtSkip;
    private View[] progressIndicatorViews = null;
    @Inject
    ViewModelsFactory viewModelsFactory;
    WalkthroughActivityViewModel viewModel;
    WalkThroughComponent component;
    private WalkThroughViewPagerAdapter adapter;
    private LiveData<String> userDataSavingStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        component = ((MyApplication) getApplication()).getComponent().walkThroughActivityComponent().create();
        component.inject(this);
        viewModel = new ViewModelProvider(this, viewModelsFactory).get(WalkthroughActivityViewModel.class);

        if(!viewModel.isFirstRun()){
            lauchNewsActivity();
            finish();
        }

        setContentView(R.layout.activity_walk_through);
        ButterKnife.bind(this);
        progressIndicatorViews = new View[]{circleIndicator1, circleIndicator2, circleIndicator3};

        txtFinish.setOnClickListener(this);
        txtSkip.setOnClickListener(this);
        adapter = new WalkThroughViewPagerAdapter(getSupportFragmentManager(), initializeFragments());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
    }

    private Fragment[] initializeFragments() {
        Fragment[] fragments = new Fragment[3];
        fragments[0] = new WalkthroughWelcomeFragment();
        fragments[1] = new WalkthroughSourcesFragment();
        fragments[2] = new WalkthroughFinishFragment();
        return fragments;
    }

    private void updatePositionIndicatorsColor(int position) {
        txtFinish.setVisibility(position == 2 ? View.VISIBLE : View.INVISIBLE);
        for (int x = 0; x < 3; x++){
            if(x == position) progressIndicatorViews[x].
                    setBackgroundColor(getResources().getColor(R.color.colorAccent));
            else progressIndicatorViews[x].
                    setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_skip:
                lauchNewsActivity();
                finish();
                break;
            case R.id.txt_finish:
                saveUserData();
                break;
        }
    }

    private void lauchNewsActivity() {
        viewModel.setIsFirstRun(false);
        Intent intent = new Intent(this, NewsActivity.class);
        startActivity(intent);
    }

    private void saveUserData() {
       userDataSavingStatus = viewModel.savePreferredSources();
        if (userDataSavingStatus != null) {
            userDataSavingStatus.observe(this, s -> {
                if (s.equals(Constants.RESULT_OK)) {
                    lauchNewsActivity();
                }
            });
            return;
        }
        lauchNewsActivity();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        updatePositionIndicatorsColor(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void onDestroy() {
        if (userDataSavingStatus != null) {
            userDataSavingStatus.removeObservers(this);
        }
        super.onDestroy();
    }
}
