package com.mambure.newsAssistant;

import android.graphics.drawable.ColorDrawable;
import android.view.View;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class CustomViewMatchers {

    private static class ViewBackgroundColorMatcher extends BaseMatcher<View> {
        int color;

        private ViewBackgroundColorMatcher(int color) {
            this.color = color;
        }

        @Override
        public boolean matches(Object item) {
            return item instanceof View &&
                    ((ColorDrawable) ((View) item).getBackground()).
                            getColor() == color;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("with background color: ");
            description.appendValue(color);
        }
    }

    public static Matcher<View> hasBackGroundColor(int color){
        return new ViewBackgroundColorMatcher(color);
    }



}
