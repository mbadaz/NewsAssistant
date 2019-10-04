package com.mbadasoft.newsassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mbadasoft.newsassistant.R;
import com.mbadasoft.newsassistant.adapters.WalkThroughViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityWalkThrough extends AppCompatActivity{
    @BindView(R.id.viewpager_walkthrough)
    ViewPager viewPager;

    WalkthroughActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_through);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(WalkthroughActivityViewModel.class);
        if (!viewModel.IsFirstTimeLogin()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            viewModel.setIsFirstTimeLogin(false);
        }
        WalkThroughViewPagerAdapter adapter = new WalkThroughViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

}
