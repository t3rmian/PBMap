package io.github.t3r1jj.pbmap.main;

import android.Manifest;
import android.app.SearchManager;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.SystemClock;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.filters.MediumTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.Until;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.gps.PBLocationListener;
import io.github.t3r1jj.pbmap.testing.ScreenshotOnTestFailedRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static io.github.t3r1jj.pbmap.main.MapActivitySearchIT.getFormattedString;
import static io.github.t3r1jj.pbmap.testing.TestUtils.allowPermissionsIfNeeded;
import static io.github.t3r1jj.pbmap.testing.TestUtils.containsIgnoringCase;
import static io.github.t3r1jj.pbmap.testing.TestUtils.withIndex;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class MapActivityNavigationIT {

    private final ActivityTestRule<MapActivity> activityRule =
            new ActivityTestRule<>(MapActivity.class, true, false);

    @Rule
    public RuleChain testRule = RuleChain
            .outerRule(activityRule)
            .around(new ScreenshotOnTestFailedRule());

    @Test
    @LargeTest
    public void navigateBack_ActionBar() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.putExtra(SearchManager.QUERY, "wc@pb_wb");
        activityRule.launchActivity(sendIntent);
        onView(withIndex(withText(getFormattedString(R.string.pb_wb_name)), 0)).check(matches(isDisplayed()));
        onView(withContentDescription(R.string.action_back)).perform(click());
        onView(withIndex(withText(R.string.pb_campus_name), 0)).check(matches(isDisplayed()));
    }

    @Test
    @LargeTest
    public void navigateUp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.putExtra(SearchManager.QUERY, "wc@pb_wb");
        activityRule.launchActivity(sendIntent);
        onView(withIndex(withText(getFormattedString(R.string.pb_wb_name)), 0)).check(matches(isDisplayed()));
        onView(withContentDescription(R.string.floor)).perform(click());
        onView(withId(R.id.up_fab)).perform(click());
        onView(withIndex(withText("PB WB L2"), 0)).check(matches(isDisplayed()));
    }

    @Test
    @LargeTest
    public void navigateDown() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.putExtra(SearchManager.QUERY, "wc@pb_wb");
        activityRule.launchActivity(sendIntent);
        onView(withIndex(withText(getFormattedString(R.string.pb_wb_name)), 0)).check(matches(isDisplayed()));
        onView(withContentDescription(R.string.floor)).perform(click());
        onView(withId(R.id.down_fab)).perform(click());
        onView(withIndex(withText("PB WB L0"), 0)).check(matches(isDisplayed()));
    }

    @Test
    @LargeTest
    public void navigateRight() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.putExtra(SearchManager.QUERY, "wc@pb_wb");
        activityRule.launchActivity(sendIntent);
        onView(withIndex(withText(getFormattedString(R.string.pb_wb_name)), 0)).check(matches(isDisplayed()));
        onView(withContentDescription(R.string.floor)).perform(click());
        onView(withId(R.id.right_fab)).perform(click());
        onView(withIndex(withText("PB WB IET"), 0)).check(matches(isDisplayed()));
    }

    @Test
    @LargeTest
    public void navigateRightLeft() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.putExtra(SearchManager.QUERY, "wc@pb_wb");
        activityRule.launchActivity(sendIntent);
        onView(withIndex(withText(getFormattedString(R.string.pb_wb_name)), 0)).check(matches(isDisplayed()));
        onView(withContentDescription(R.string.floor)).perform(click());
        onView(withId(R.id.right_fab)).perform(click());
        onView(withId(R.id.left_fab)).perform(click());
        onView(withIndex(withText(getFormattedString(R.string.pb_wb_name)), 0)).check(matches(isDisplayed()));
    }

    @Test
    @LargeTest
    public void navigateShowHide() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.putExtra(SearchManager.QUERY, "wc@pb_wb");
        activityRule.launchActivity(sendIntent);
        onView(withIndex(withText(getFormattedString(R.string.pb_wb_name)), 0)).check(matches(isDisplayed()));
        onView(withContentDescription(R.string.floor)).perform(click());
        onView(withId(R.id.up_fab)).check(matches(isDisplayed()));
        onView(withId(R.id.right_fab)).check(matches(isDisplayed()));
        onView(withId(R.id.down_fab)).check(matches(isDisplayed()));
        onView(withContentDescription(R.string.floor)).perform(longClick());
        onView(withId(R.id.up_fab)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.right_fab)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.down_fab)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    @Test
    @LargeTest
    public void showInfo() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.putExtra(SearchManager.QUERY, "wc@pb_wb");
        activityRule.launchActivity(sendIntent);
        onView(withIndex(withText(getFormattedString(R.string.pb_wb_name)), 0)).check(matches(isDisplayed()));
        onView(withContentDescription(R.string.more_features)).perform(click());
        onView(withId(R.id.info_fab)).perform(click());
        onView(withId(R.id.design_bottom_sheet)).check(matches(isDisplayed()));
        onView(withId(R.id.info_title))
                .check(matches(allOf(isDisplayed(), withText(getFormattedString(R.string.pb_wb_name)))));
        onView(withId(R.id.info_url))
                .check(matches(allOf(isDisplayed(), withText("https://wb.pb.edu.pl/"))));
        onView(withId(R.id.info_address))
                .check(matches(allOf(isDisplayed(), withText(containsIgnoringCase(getFormattedString(R.string.pb_wb_address))))));
    }

    @Test
    @LargeTest
    public void trackLocation() throws UiObjectNotFoundException {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.putExtra(SearchManager.QUERY, "wc@pb_wb");
        activityRule.launchActivity(sendIntent);
        onView(withIndex(withText(getFormattedString(R.string.pb_wb_name)), 0)).check(matches(isDisplayed()));
        onView(withContentDescription(R.string.destination)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withContentDescription(R.string.more_features)).perform(click());
        onView(withId(R.id.gps_fab)).perform(click());
        allowPermissionsIfNeeded(Manifest.permission.ACCESS_FINE_LOCATION);
        allowPermissionsIfNeeded(Manifest.permission.ACCESS_COARSE_LOCATION);

        PBLocationListener locationListener = activityRule.getActivity().getLocationListener();
        Location mockLocation = new Location("");
        mockLocation.setLatitude(53.1177825529975);
        mockLocation.setLongitude(23.15214421044988);
        mockLocation.setAltitude(150);
        mockLocation.setTime(System.currentTimeMillis());
        mockLocation.setAccuracy(1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        }
        activityRule.getActivity().runOnUiThread(() -> {
            locationListener.onLocationChanged(mockLocation);
        });

        onView(withText("69B")).check(matches(isDisplayed()));
        onView(withContentDescription(R.string.source)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.distance)).check(matches(isDisplayed()));
    }

    @Test
    @MediumTest
    public void pinpointStartEndWithCustomLocation() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.putExtra(SearchManager.QUERY, "pb_campus");
        Location customLocation = new Location("");
        customLocation.setLatitude(53.11878);
        customLocation.setLongitude(23.14878);
        sendIntent.putExtra(SearchManager.EXTRA_DATA_KEY, customLocation);
        activityRule.launchActivity(sendIntent);

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        int x = 200;
        final int y = device.getDisplayHeight() / 2;
        final int steps = 200;
        device.swipe(x, y, x, y, steps);
        device.findObject(By.text(getFormattedString(R.string.place_source_marker))).click();

        SystemClock.sleep(250);
        x = device.getDisplayWidth() - 200;
        device.swipe(x, y, x, y, steps);
        device.findObject(By.text(getFormattedString(R.string.place_destination_marker))).click();;

        device.wait(Until.findObject(By.textContains("Distance")), 250);
        device.wait(Until.findObject(By.descContains("Source")), 50);
        device.wait(Until.findObject(By.descContains("Destination")), 50);
    }

    @Test
    @MediumTest
    public void onSpaceNavigation() {
        activityRule.launchActivity(new Intent());
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.findObject(By.textContains("PB WI")).click();
        device.wait(Until.findObject(By.descContains("12b")), 250);
    }

    @Test
    @MediumTest
    public void onSpaceNavigationInfo() {
        activityRule.launchActivity(new Intent());
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.findObject(By.textContains("Gwint\n" +
                "club")).click();
        SystemClock.sleep(2500);
        onView(withId(R.id.design_bottom_sheet)).check(matches(isDisplayed()));
        onView(withId(R.id.info_address))
                .check(matches(allOf(isDisplayed(), withText(R.string.gwint_address))));
    }

    @Test
    @MediumTest
    public void onZoom() {
        activityRule.launchActivity(new Intent());
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.findObject(By.res("android:id/zoomIn")).click();
        device.findObject(By.res("android:id/zoomOut")).click();
    }

}