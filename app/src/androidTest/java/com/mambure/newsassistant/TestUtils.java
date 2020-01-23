package com.mambure.newsassistant;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class TestUtils {

    public static Matcher<View> recyclerViewChildatPosition(final int position, final Matcher<View> itemMatcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class){
            @Override
            protected boolean matchesSafely(RecyclerView item) {
                RecyclerView.ViewHolder viewHolder = item.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    return false;
                }
                return itemMatcher.matches(viewHolder);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position" + position + ": ");
                itemMatcher.describeTo(description);
            }
        };
    }
}
