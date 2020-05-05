package com.mambure.newsAssistant;

import android.content.Context;
import android.view.View;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.ViewPagerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import com.mambure.newsAssistant.wakthroughActivity.WalkThroughActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static com.mambure.newsAssistant.CustomViewMatchers.hasBackGroundColor;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4ClassRunner.class)
public class WalkthroughActivityTest {

    private Context context;
    @Rule
    public ActivityScenarioRule<WalkThroughActivity> activityScenarioRule =
            new ActivityScenarioRule<>(WalkThroughActivity.class);
    private IdlingRegistry idlingRegistry;

    @Before
    public void initialize() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        idlingRegistry = IdlingRegistry.getInstance();
        idlingRegistry.register(MyIdlingResource.getInstance());
    }

    @After
    public void cleanUp() {
        idlingRegistry.unregister(MyIdlingResource.getInstance());
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

    @Test
    public void showErrorMessageTest() {
        onView(withId(R.id.viewpager_walkthrough)).perform(ViewPagerActions.scrollRight());
        onView(childMatcher(R.id.txtMessage, R.id.sources_selection_fragment)).
                check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

    }

    private Matcher<View> childMatcher(int childId, int parentId) {
        return allOf(withId(childId),
                withParent(withId(parentId)));
    }

}
