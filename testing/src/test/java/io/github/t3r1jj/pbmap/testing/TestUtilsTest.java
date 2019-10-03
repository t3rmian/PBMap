package io.github.t3r1jj.pbmap.testing;

import android.view.View;
import android.view.ViewGroup;

import androidx.test.uiautomator.UiDevice;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.mockito.Mockito;

import static io.github.t3r1jj.pbmap.testing.TestUtils.nthChildOf;
import static io.github.t3r1jj.pbmap.testing.TestUtils.pressDoubleBack;
import static io.github.t3r1jj.pbmap.testing.TestUtils.withIndex;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

    @Test
    public void testDoubleBack() {
        UiDevice deviceMock = mock(UiDevice.class);
        pressDoubleBack(deviceMock);
        verify(deviceMock, times(2)).pressBack();
        verify(deviceMock, times(2)).waitForIdle();
    }

    @Test
    public void testDoubleBack_WithSilentException() {
        UiDevice deviceMock = mock(UiDevice.class);
        when(deviceMock.pressBack()).thenThrow(new RuntimeException("mocked exception"));
        pressDoubleBack(deviceMock);
        verify(deviceMock, times(1)).pressBack();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNthChild_ParentHasLessChildren() {
        final int index = 2;
        Matcher viewMatcherMock = mock(Matcher.class);
        Matcher indexMatcher = nthChildOf(viewMatcherMock, index);

        ViewGroup viewGroup = mock(ViewGroup.class);
        View childViewMock = mock(View.class);
        when(viewGroup.getChildCount()).thenReturn(index);
        when(viewGroup.getChildAt(index)).thenReturn(childViewMock);
        when(viewMatcherMock.matches(ViewGroup.class)).thenReturn(true);
        when(childViewMock.getParent()).thenReturn(viewGroup);

        assertFalse(indexMatcher.matches(mock(String.class)));
        assertFalse(indexMatcher.matches(mock(View.class)));
        assertFalse(indexMatcher.matches(mock(ViewGroup.class)));

        assertFalse(indexMatcher.matches(childViewMock));
        when(viewGroup.getChildCount()).thenReturn(index + 1);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNthChild_HasChildAtDifferentIndex() {
        final int index = 2;
        Matcher viewMatcherMock = mock(Matcher.class);
        Matcher indexMatcher = nthChildOf(viewMatcherMock, index);

        ViewGroup viewGroup = mock(ViewGroup.class);
        View childViewMock = mock(View.class);
        when(viewGroup.getChildCount()).thenReturn(index + 1);
        when(viewGroup.getChildAt(index)).thenReturn(mock(View.class));
        when(viewMatcherMock.matches(viewGroup)).thenReturn(true);
        when(childViewMock.getParent()).thenReturn(viewGroup);

        assertFalse(indexMatcher.matches(childViewMock));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNthChild() {
        final int index = 2;
        Matcher viewMatcherMock = mock(Matcher.class);
        Matcher indexMatcher = nthChildOf(viewMatcherMock, index);

        ViewGroup viewGroup = mock(ViewGroup.class);
        View childViewMock = mock(View.class);
        when(viewGroup.getChildCount()).thenReturn(index + 1);
        when(viewGroup.getChildAt(index)).thenReturn(childViewMock);
        when(viewMatcherMock.matches(viewGroup)).thenReturn(true);
        when(childViewMock.getParent()).thenReturn(viewGroup);

        assertTrue(indexMatcher.matches(childViewMock));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNthChildOfDescription() {
        final int index = 2;
        Matcher viewMatcherMock = mock(Matcher.class);
        Matcher indexMatcher = nthChildOf(viewMatcherMock, index);
        StringDescription mismatchDescription = new StringDescription();
        assertFalse(indexMatcher.matches(mock(View.class)));
        indexMatcher.describeTo(mismatchDescription);
        String description = mismatchDescription.toString();
        assertThat(description, containsString("index"));
        assertThat(description, containsString(String.valueOf(index)));
        assertThat(description, containsString("parent"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNthChildOfDescription_None() {
        final int index = 2;
        Matcher viewMatcherMock = mock(Matcher.class);
        Matcher indexMatcher = nthChildOf(viewMatcherMock, index);
        indexMatcher.describeTo(Description.NONE);
    }
}