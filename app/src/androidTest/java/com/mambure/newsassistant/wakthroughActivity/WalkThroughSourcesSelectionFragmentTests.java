package com.mambure.newsassistant.wakthroughActivity;

import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.mambure.newsassistant.MyFragmentScenario;
import com.mambure.newsassistant.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.mambure.newsassistant.MyFragmentScenario.launchInContainer;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4ClassRunner.class)
public class WalkThroughSourcesSelectionFragmentTests {


    @Test
    public void sourcesSelectionTest() {

        MyFragmentScenario<WalkthroughSourcesSelectionFragment> fragmentScenario = launchInContainer(
                WalkthroughSourcesSelectionFragment.class);


        onView(withId(R.id.rv_walkthrough)).perform(RecyclerViewActions.actionOnItemAtPosition(9, click()));
        onView(allOf(
                withId(R.id.item_source_check),
                hasSibling(withText("Source 10name"))
                )
        ).check(ViewAssertions.matches(isChecked()));

        onView(withId(R.id.rv_walkthrough)).perform(RecyclerViewActions.actionOnItemAtPosition(9, click()));

        onView(allOf(
                withId(R.id.item_source_check),
                hasSibling(withText("Source 10name"))
                )
        ).check(ViewAssertions.matches(isNotChecked()));

        fragmentScenario.moveToState(Lifecycle.State.DESTROYED);
    }

    private static Matcher<SourcesAdapter.SourcesViewHolder> childAtPosition(int position) {
        return new TypeSafeMatcher<SourcesAdapter.SourcesViewHolder>() {
            @Override
            protected boolean matchesSafely(SourcesAdapter.SourcesViewHolder item) {
                return item.position == position;

            }

            @Override
            public void describeTo(Description description) {
                description.appendText("item at position");
            }
        };
    }
}
