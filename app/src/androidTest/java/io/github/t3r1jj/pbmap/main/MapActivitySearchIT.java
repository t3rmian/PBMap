package io.github.t3r1jj.pbmap.main;

import android.content.Context;

import androidx.annotation.StringRes;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.filters.MediumTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.testing.RetryRunner;
import io.github.t3r1jj.pbmap.testing.ScreenshotOnTestFailedRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActionsUtils.tap;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static io.github.t3r1jj.pbmap.testing.TestUtils.withIndex;
import static org.hamcrest.core.IsNot.not;

@RunWith(RetryRunner.class)
public class MapActivitySearchIT {

    private static final long TIMEOUT_MS = 30000L;
    private final ActivityTestRule<MapActivity> activityRule =
            new ActivityTestRule<>(MapActivity.class, true, true);

    @Rule
    public RuleChain testRule = RuleChain
            .outerRule(activityRule)
            .around(new ScreenshotOnTestFailedRule());

    @Test
    @MediumTest
    public void testSearch_PlaceInSpace() {
        onView(withId(R.id.action_search)).perform(tap());
        onView(withId(R.id.search_src_text)).perform(typeText("116@wi"), pressImeActionButton());
        onView(withContentDescription("Collapse")).perform(click());
        onView(withText("116")).check(matches(isDisplayed()));
        onView(withText(R.string.name_pb_wi_l2)).check(matches(isDisplayed()));
    }

    @Test
    @LargeTest
    public void testSearch_Space() {
        onView(withId(R.id.action_search)).perform(tap());
        onView(withId(R.id.search_src_text)).perform(typeText("[WI]"), pressImeActionButton());
        onView(withContentDescription("Clear query")).perform(click());
        onView(withContentDescription("Collapse")).perform(click());
        onView(withIndex(withText(R.string.name_pb_wi), 0)).check(matches(isDisplayed()));
    }

    @Test
    @MediumTest
    public void testSearch_NotFound() {
        onView(withId(R.id.action_search)).perform(tap());
        onView(withId(R.id.search_src_text)).perform(typeText("PB WI L3"), pressImeActionButton());
        onView(withContentDescription("Collapse")).perform(click());
        onView(withText(R.string.not_found))
                .inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    @MediumTest
    public void testSearch_ListOfSpaces() {
        onView(withId(R.id.action_search)).perform(tap());
        onView(withId(R.id.search_src_text)).perform(typeText("Libr"));
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
                .wait(Until.findObject(By.text(getFormattedString(R.string.name_main_library))), TIMEOUT_MS);
        onView(withText(getFormattedString(R.string.name_main_library)))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()));
        onView(withText(getFormattedString(R.string.name_library)))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()));
    }

    @Test
    @MediumTest
    public void testSearch_ListOfSpaces_SelectOne() {
        onView(withId(R.id.action_search)).perform(tap());
        onView(withId(R.id.search_src_text)).perform(typeText("CNK"));
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
                .wait(Until.findObject(By.text(getFormattedString(R.string.name_pb_campus))), TIMEOUT_MS);
        onView(withText(getFormattedString(R.string.name_pb_campus)))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        onView(withContentDescription("Collapse")).perform(click());
        onView(withText(getUnFormattedString(R.string.name_cnk))).check(matches(isDisplayed()));
    }

    @Test
    @MediumTest
    public void testSearch_ListOfPlacesInSpaces() {
        onView(withId(R.id.action_search)).perform(tap());
        onView(withId(R.id.search_src_text)).perform(typeText("10"));
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
                .wait(Until.findObject(By.text("010")), TIMEOUT_MS);
        onView(withIndex(withText("010"), 0))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()));
        onView(withIndex(withText("010"), 1))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()));
        onView(withIndex(withText(getFormattedString(R.string.name_pb_wm_l2)), 0))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()));
        onView(withIndex(withText(getFormattedString(R.string.name_pb_wi_l0)), 0))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()));
    }

    static String getFormattedString(@StringRes int resId) {
        return getUnFormattedString(resId)
                .toUpperCase()
                .replace("\n", " ")
                .trim();
    }

    static String getUnFormattedString(@StringRes int resId) {
        Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        return ctx.getString(resId);
    }
}