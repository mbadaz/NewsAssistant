package com.mambure.newsAssistant;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.mambure.newsAssistant.wakthroughActivity.WalkthroughSourcesFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4ClassRunner.class)
public class SourcesFragmentTest {


    public FragmentScenario<WalkthroughSourcesFragment> fragmentScenario;



    @Test
    public void onResponseErrorTest() throws InterruptedException {
        FragmentScenario.launchInContainer(WalkthroughSourcesFragment.class);
        Thread.sleep(4000);
        onView(withText("Error occured!")).check(matches(isDisplayed()));
    }
}
