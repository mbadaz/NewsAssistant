package com.mambure.newsAssistant.wakthroughActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.idescout.sql.SqlScoutServer;
import com.mambure.newsAssistant.MyApplication;
import com.mambure.newsAssistant.R;
import com.mambure.newsAssistant.dependencyInjection.ViewModelsFactory;
import com.mambure.newsAssistant.newsActivity.NewsActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalkThroughActivity extends AppCompatActivity implements View.OnClickListener,  ViewPager.OnPageChangeListener{
    @BindView(R.id.viewpager_walkthrough) ViewPager viewPager;
    @BindView(R.id.tab_circle) View circleIndicator1;
    @BindView(R.id.tab_circle2) View circleIndicator2;
    @BindView(R.id.tab_circle3) View circleIndicator3;
    @BindView(R.id.txt_finish) TextView txtFinish;
    @BindView(R.id.txt_skip) TextView txtSkip;

    SqlScoutServer sqlScoutServer;

    @Inject
    ViewModelsFactory viewModelsFactory;
    WalkthroughActivityViewModel viewModel;

    private WalkThroughViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getApplication()).getComponent().inject(this);
        viewModel = new ViewModelProvider(this, viewModelsFactory).get(WalkthroughActivityViewModel.class);

        if(!viewModel.isFirstRun()){
            lauchNewsActivity();
            finish();
        }

        setContentView(R.layout.activity_walk_through);
        ButterKnife.bind(this);
        sqlScoutServer = SqlScoutServer.create(this, "dbserver" );

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

    private void updatePositionIndicators(int position) {
        switch (position) {
            case 0:
                circleIndicator1.setBackground(getResources().getDrawable(R.drawable.circle_accent,null));
                circleIndicator2.setBackground(getResources().getDrawable(R.drawable.circle_blue, null));
                circleIndicator3.setBackground(getResources().getDrawable(R.drawable.circle_blue, null));
                break;
            case 1:
                circleIndicator2.setBackground(getResources().getDrawable(R.drawable.circle_accent, null));
                circleIndicator3.setBackground(getResources().getDrawable(R.drawable.circle_blue, null));
                circleIndicator1.setBackground(getResources().getDrawable(R.drawable.circle_blue, null));
                txtFinish.setVisibility(View.INVISIBLE);
                break;
            case 2:
                circleIndicator3.setBackground(getResources().getDrawable(R.drawable.circle_accent, null));
                circleIndicator1.setBackground(getResources().getDrawable(R.drawable.circle_blue, null));
                circleIndicator2.setBackground(getResources().getDrawable(R.drawable.circle_blue, null));
                txtFinish.setVisibility(View.VISIBLE);
                break;
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
                lauchNewsActivity();
                finish();
                break;
        }
    }

    private void lauchNewsActivity() {
        viewModel.setIsFirstRun(false);
        Intent intent = new Intent(this, NewsActivity.class);
        startActivity(intent);
    }

    private void saveUserData() {
        viewModel.savePreferredSources();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        updatePositionIndicators(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void onDestroy() {
        sqlScoutServer.destroy();
        super.onDestroy();
    }
}
