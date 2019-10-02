package io.github.t3r1jj.pbmap.testing;

import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.mockito.Mockito;

import static io.github.t3r1jj.pbmap.testing.TestUtils.withIndex;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class TestUtilsTest {
    @SuppressWarnings("unchecked")
    @Test
    public void testWithIndex() {
        final int index = 2;
        Matcher viewMatcherMock = mock(Matcher.class);
        Matcher indexMatcher = withIndex(viewMatcherMock, index);

        when(viewMatcherMock.matches(any())).thenReturn(true);
        assertFalse(indexMatcher.matches(null));
        assertFalse(indexMatcher.matches(null));
        assertFalse(indexMatcher.matches("a"));
        assertFalse(indexMatcher.matches("b"));

        assertFalse(indexMatcher.matches(mock(View.class)));
        assertFalse(indexMatcher.matches(mock(View.class)));
        assertTrue(indexMatcher.matches(mock(View.class)));
        assertFalse(indexMatcher.matches(mock(View.class)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testWithIndexDescription() {
        final int index = 2;
        Matcher viewMatcherMock = mock(Matcher.class);
        Matcher indexMatcher = withIndex(viewMatcherMock, index);
        StringDescription mismatchDescription = new StringDescription();
        assertFalse(indexMatcher.matches(mock(View.class)));
        indexMatcher.describeTo(mismatchDescription);
        String description = mismatchDescription.toString();
        assertThat(description, containsString("index"));
        assertThat(description, containsString(String.valueOf(index)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testWithIndexDescription_None() {
        final int index = 2;
        Matcher viewMatcherMock = mock(Matcher.class);
        Matcher indexMatcher = withIndex(viewMatcherMock, index);
        indexMatcher.describeTo(Description.NONE);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testWithIndexAndDifferentViews_MatchesSameHashCode() {
        View viewMock = Mockito.mock(View.class);
        View viewMock2 = Mockito.mock(View.class);
        View viewMock3 = Mockito.mock(View.class);
        final int index = 1;
        final int knownHashCode2 = viewMock2.hashCode();
        final int knownHashCode3 = viewMock3.hashCode();
        Matcher viewMatcherMock = new TypeSafeMatcher() {
            @Override
            public void describeTo(Description description) {

            }

            @Override
            protected boolean matchesSafely(Object item) {
                return item.hashCode() == knownHashCode2 || item.hashCode() == knownHashCode3;
            }
        };
        Matcher indexMatcher = withIndex(viewMatcherMock, index);

        assertFalse(indexMatcher.matches(viewMock));
        assertFalse(indexMatcher.matches(viewMock2));
        assertFalse(indexMatcher.matches(viewMock));
        assertTrue(indexMatcher.matches(viewMock3));
        assertFalse(indexMatcher.matches(viewMock));
        assertFalse(indexMatcher.matches(viewMock2));
        assertTrue(indexMatcher.matches(viewMock3));
    }
}