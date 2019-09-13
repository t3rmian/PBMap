package io.github.t3r1jj.pbmap.main.drawer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.main.MapActivity;
import io.github.t3r1jj.pbmap.testing.ScreenshotOnTestFailedRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static io.github.t3r1jj.pbmap.testing.TestUtils.withIndex;
import static org.junit.Assert.assertTrue;

public class MapsDrawerFragmentIT {

    private final ActivityTestRule<MapActivity> activityRule =
            new ActivityTestRule<>(MapActivity.class, true, false);

    @Rule
    public RuleChain testRule = RuleChain
            .outerRule(activityRule)
            .around(new ScreenshotOnTestFailedRule());

    @Test
    public void testDrawer_OpenOnFirstTry() {
        SharedPreferences preferences = autoOpenDrawerReturningPreferences(true);
        preferences.edit().remove(NavigationDrawerFragment.PREF_USER_LEARNED_DRAWER).apply();
        activityRule.launchActivity(new Intent());
        onView(withId(R.id.navigation_drawer)).check(matches(isDisplayed()));
        assertTrue(preferences.getAll().containsKey(NavigationDrawerFragment.PREF_USER_LEARNED_DRAWER));
    }

    @Test
    public void testDrawer_ClosedOnSecondTry() {
        SharedPreferences preferences = autoOpenDrawerReturningPreferences(false);
        activityRule.launchActivity(new Intent());
        onView(withId(R.id.navigation_drawer)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        assertTrue(preferences.getAll().containsKey(NavigationDrawerFragment.PREF_USER_LEARNED_DRAWER));
    }

    @Test
    public void testDrawer_OpenManually_CloseManually() {
        autoOpenDrawerReturningPreferences(false);
        activityRule.launchActivity(new Intent());
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withId(R.id.navigation_drawer)).check(matches(isDisplayed()));
        onView(withContentDescription(R.string.navigation_drawer_close)).perform(click());
        onView(withId(R.id.navigation_drawer)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    @Test
    public void testDrawer_DisplaysSpaces() {
        autoOpenDrawerReturningPreferences(true);
        activityRule.launchActivity(new Intent());
        onView(withText("PB ACS")).check(matches(isDisplayed()));
        onView(withIndex(withText("PB campus"), 0)).check(matches(isDisplayed()));
    }

    @Test
    public void testDrawer_NavigatesToCNK() {
        autoOpenDrawerReturningPreferences(true);
        activityRule.launchActivity(new Intent());
        onView(withText("PB CNK")).perform(click());
        onView(withId(R.id.navigation_drawer)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withIndex(withText("PB CNK"), 0)).check(matches(isDisplayed()));
    }

    @Test
    public void testDrawer_SelectionState() {
        autoOpenDrawerReturningPreferences(true);
        activityRule.launchActivity(new Intent());
        onView(withIndex(withText("PB campus"), 1)).check(matches(isChecked()));
    }

    @Test
    public void testDrawer_NavigatesToCNK_SelectionState() {
        autoOpenDrawerReturningPreferences(true);
        activityRule.launchActivity(new Intent());
        onView(withText("PB CNK")).perform(click());
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withIndex(withText("PB CNK"), 1)).check(matches(isChecked()));
    }

    private SharedPreferences autoOpenDrawerReturningPreferences(boolean doOpen) {
        Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        if (doOpen) {
            preferences.edit().remove(NavigationDrawerFragment.PREF_USER_LEARNED_DRAWER).apply();
        } else {
            preferences.edit().putBoolean(NavigationDrawerFragment.PREF_USER_LEARNED_DRAWER, true).apply();
        }
        return preferences;
    }
}