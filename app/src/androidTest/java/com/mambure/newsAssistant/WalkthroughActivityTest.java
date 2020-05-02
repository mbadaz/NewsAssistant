package com.mambure.newsAssistant;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.ViewPagerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import com.mambure.newsAssistant.wakthroughActivity.WalkThroughActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.mambure.newsAssistant.CustomViewMatchers.*;

@RunWith(AndroidJUnit4ClassRunner.class)
public class WalkthroughActivityTest {

    private Context context;
    @Rule
    public ActivityScenarioRule<WalkThroughActivity> activityScenarioRule =
            new ActivityScenarioRule<>(WalkThroughActivity.class);

    @Before
    public void initialize() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void progressIndicatorsTest() {
        int accentColor = context.getResources().getColor(R.color.colorAccent);
        int primaryColor = context.getResources().getColor(R.color.colorPrimary);

        // Verify
        onView(withId(R.id.tab_indicator1)).check(matches(hasBackGroundColor(accentColor)));
        onView(withId(R.id.tab_indicator2)).check(matches(hasBackGroundColor(primaryColor)));
        onView(withId(R.id.tab_indicator3)).check(matches(hasBackGroundColor(primaryColor)));

        // perform
        onView(withId(R.id.viewpager_walkthrough)).perform(ViewPagerActions.scrollRight());

        // Verify
        onView(withId(R.id.tab_indicator1)).check(matches(hasBackGroundColor(primaryColor)));
        onView(withId(R.id.tab_indicator2)).check(matches(hasBackGroundColor(accentColor)));
        onView(withId(R.id.tab_indicator3)).check(matches(hasBackGroundColor(primaryColor)));

        // perform
        onView(withId(R.id.viewpager_walkthrough)).perform(ViewPagerActions.scrollRight());

        // Verify
        onView(withId(R.id.tab_indicator1)).check(matches(hasBackGroundColor(primaryColor)));
        onView(withId(R.id.tab_indicator2)).check(matches(hasBackGroundColor(primaryColor)));
        onView(withId(R.id.tab_indicator3)).check(matches(hasBackGroundColor(accentColor)));
    }

    @Test
    public void walkThroughFinishTextShowAndHideTest() {
        // PERFORM: navigate to last page in walkthrough
        onView(withId(R.id.viewpager_walkthrough)).perform(ViewPagerActions.scrollRight());
        onView(withId(R.id.viewpager_walkthrough)).perform(ViewPagerActions.scrollRight());

        // VERIFY
        onView(withId(R.id.txt_finish)).check(
                ViewAssertions.matches(
                        withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // PERFORM: navigate to previous page in walkthrough
        onView(withId(R.id.viewpager_walkthrough)).perform(ViewPagerActions.scrollLeft());

        // VERIFY
        onView(withId(R.id.txt_finish)).check(
                ViewAssertions.matches(
                        withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

}
