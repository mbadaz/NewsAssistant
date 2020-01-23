package com.mambure.newsassistant.wakthroughActivity;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.mambure.newsassistant.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4ClassRunner.class)
public class WalkThroughCategorySelectionFragmentTests {

    @Test
    public void SelectCategoriesTest() {
        FragmentScenario fragmentScenario = FragmentScenario.launchInContainer(WalkthroughCategorySelectionFragment.class);
        final String[][] categories = new String[1][7];
        fragmentScenario.onFragment(fragment -> categories[0] = fragment.getContext().getResources().getStringArray(R.array.categories));

        // Perform clicks on ListView to select categories
        getAdapterViewInteractionObject(0).perform(click());
        getAdapterViewInteractionObject(1).perform(click());
        getAdapterViewInteractionObject(2).perform(click());

        // Check check that corresponding category tags are showing
        onView(getTableLayoutViewMatcher(categories, 0)).check(matches(isDisplayed()));
        onView(getTableLayoutViewMatcher(categories, 1)).check(matches(isDisplayed()));
        onView(getTableLayoutViewMatcher(categories, 2)).check(matches(isDisplayed()));

        // Perform clicks on ListView to deselect category
        getAdapterViewInteractionObject(1).perform(click());

        // Check that deselected category's tag is not showing
        onView(getTableLayoutViewMatcher(categories, 1)).check(ViewAssertions.doesNotExist());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    private DataInteraction getAdapterViewInteractionObject(int position) {
       return onData(anything())
                .inAdapterView(allOf(withId(R.id.list_walkthrough_category),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)))
                .atPosition(position);
    }

    private Matcher<View> getTableLayoutViewMatcher(String[][] labels, int position) {
       return allOf(
                withText(labels[0][position]), withParent(
                        withParent(withId(R.id.tableLayout))
                )
        );
    }
}
