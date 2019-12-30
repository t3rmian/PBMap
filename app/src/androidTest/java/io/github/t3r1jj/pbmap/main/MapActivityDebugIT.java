package io.github.t3r1jj.pbmap.main;

import android.app.SearchManager;
import android.content.Intent;
import android.os.SystemClock;

import androidx.test.filters.LargeTest;
import androidx.test.filters.MediumTest;
import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import java.util.regex.Pattern;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.settings.Config;
import io.github.t3r1jj.pbmap.testing.RetryRunner;
import io.github.t3r1jj.pbmap.testing.ScreenshotOnTestFailedRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;

@RunWith(RetryRunner.class)
public class MapActivityDebugIT {

    private final ActivityTestRule<MapActivity> activityRule =
            new ActivityTestRule<>(MapActivity.class, true, false);

    @Rule
    public RuleChain testRule = RuleChain
            .outerRule(activityRule)
            .around(new ScreenshotOnTestFailedRule());

    @After
    public void tearDown() {
        Config.getInstance().setDebug(false);
    }

    @Test
    @SmallTest
    public void showFullRoute() {
        Config.getInstance().setDebug(true);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.putExtra(SearchManager.QUERY, "berlin@pb_wz_campus");
        activityRule.launchActivity(sendIntent);
    }

    @Test
    @MediumTest
    public void showDebug() {
        Config.getInstance().setDebug(true);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.putExtra(SearchManager.QUERY, "berlin@pb_wz_campus");
        activityRule.launchActivity(sendIntent);

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        final int x = device.getDisplayWidth() / 2;
        final int y = device.getDisplayHeight() / 2;
        device.click(x, y);
        SystemClock.sleep(100);

        device.click(x, y);
        SystemClock.sleep(100);

        device.click(x, y);
        SystemClock.sleep(1000);

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        device.click(x, y);

        onView(withText(allOf(containsString("lat"), containsString("lng"))))
                .inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    @LargeTest
    public void removeDestinationMark() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.putExtra(SearchManager.QUERY, "berlin@pb_wz_campus");
        activityRule.launchActivity(sendIntent);

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        final int x = device.getDisplayWidth() / 2;
        final int y = device.getDisplayHeight() / 2;
        device.click(x, y);
        SystemClock.sleep(100);

        device.click(x, y);
        SystemClock.sleep(100);

        device.click(x, y);
        SystemClock.sleep(100);

        device.swipe(x, y, x, y, 300);
        SystemClock.sleep(250);

        device.findObject(By.text(Pattern.compile("^.*(?i)(DESTINATION).*$"))).click();
        SystemClock.sleep(250);

        onView(withContentDescription(R.string.destination)).perform(longClick());
        SystemClock.sleep(250);
        onView(withContentDescription(R.string.destination)).check(doesNotExist());
        SystemClock.sleep(250);
    }

    @Test
    @LargeTest
    public void removeSourceMark() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.putExtra(SearchManager.QUERY, "berlin@pb_wz_campus");
        activityRule.launchActivity(sendIntent);

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        final int x = device.getDisplayWidth() / 2;
        final int y = device.getDisplayHeight() / 2;
        device.click(x, y);
        SystemClock.sleep(100);

        device.click(x, y);
        SystemClock.sleep(100);

        device.click(x, y);
        SystemClock.sleep(100);

        device.swipe(x, y, x, y, 300);
        SystemClock.sleep(250);

        device.findObject(By.text(Pattern.compile("^.*(?i)(SOURCE).*$"))).click();
        SystemClock.sleep(250);

        onView(withContentDescription(R.string.source)).perform(longClick());
        SystemClock.sleep(250);
        onView(withContentDescription(R.string.source)).check(doesNotExist());
        SystemClock.sleep(250);
    }

}