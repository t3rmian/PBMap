package io.github.t3r1jj.pbmap.main;

import android.content.Context;

import androidx.annotation.StringRes;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.testing.ScreenshotOnTestFailedRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static io.github.t3r1jj.pbmap.testing.TestUtils.withIndex;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class MapActivitySearchIT {

    private final ActivityTestRule<MapActivity> activityRule =
            new ActivityTestRule<>(MapActivity.class, true, true);

    @Rule
    public RuleChain testRule = RuleChain
            .outerRule(activityRule)
            .around(new ScreenshotOnTestFailedRule());

    @Test
    @LargeTest
    public void testSearch_PlaceInSpace() {
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText("116@wi"), pressImeActionButton());
        onView(withContentDescription("Collapse")).perform(click());
        onView(withText("116")).check(matches(isDisplayed()));
        onView(withText("PB WI L2")).check(matches(isDisplayed()));
    }

    @Test
    @LargeTest
    public void testSearch_Space() {
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText("PB WI"), pressImeActionButton());
        onView(withContentDescription("Clear query")).perform(click());
        onView(withContentDescription("Collapse")).perform(click());
        onView(withIndex(withText("PB WI"), 1)).check(matches(isDisplayed()));
    }

    @Test
    @LargeTest
    public void testSearch_NotFound() {
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText("PB WI L3"), pressImeActionButton());
        onView(withContentDescription("Collapse")).perform(click());
        onView(withText(R.string.not_found))
                .inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    @LargeTest
    public void testSearch_ListOfSpaces() {
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText("PB"));
        onView(withText(getFormattedString(R.string.main_library_name)))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()));
        onView(withText(getFormattedString(R.string.pb_wb_name)))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()));
        onView(withIndex(withText(getFormattedString(R.string.pb_campus_name)), 0))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()));
    }

    @Test
    @LargeTest
    public void testSearch_ListOfSpaces_SelectOne() {
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText("PB"));
        onView(withText(getFormattedString(R.string.main_library_name)))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        onView(withContentDescription("Collapse")).perform(click());
        onView(withText("CNK")).check(matches(isDisplayed()));
    }

    @Test
    @LargeTest
    public void testSearch_ListOfPlacesInSpaces() {
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText("10"));
        onView(withIndex(withText("010"), 0))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()));
        onView(withIndex(withText("010"), 1))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()));
        onView(withIndex(withText(getFormattedString(R.string.pb_we_name)), 0))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()));
        onView(withIndex(withText("PB WM L2"), 0))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()));
    }

    static String getFormattedString(@StringRes int resId) {
        Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        return ctx.getString(resId)
                .toUpperCase()
                .replace("\n", " ")
                .trim();
    }
}