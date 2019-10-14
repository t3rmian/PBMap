package io.github.t3r1jj.pbmap.main;

import android.Manifest;
import android.app.SearchManager;
import android.app.UiAutomation;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.FlakyTest;
import androidx.test.filters.LargeTest;
import androidx.test.filters.MediumTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.Until;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.gps.PBLocationListener;
import io.github.t3r1jj.pbmap.testing.ScreenshotOnTestFailedRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static io.github.t3r1jj.pbmap.main.MapActivitySearchIT.getFormattedString;
import static io.github.t3r1jj.pbmap.testing.TestUtils.allowPermissionsIfNeeded;
import static io.github.t3r1jj.pbmap.testing.TestUtils.containsIgnoringCase;
import static io.github.t3r1jj.pbmap.testing.TestUtils.withIndex;
import static io.github.t3r1jj.pbmap.testing.TestUtils.withIntents;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class MapActivityNavigationIT {

    private final ActivityTestRule<MapActivity> activityRule =
            new ActivityTestRule<>(MapActivity.class, true, false);

    @Rule
    public RuleChain testRule = RuleChain
            .outerRule(activityRule)
            .around(new ScreenshotOnTestFailedRule());

    @After
    public void tearDown(){
        UiAutomation uiAutomation = InstrumentationRegistry.getInstrumentation().getUiAutomation();
        uiAutomation.
                executeShellCommand("pm revoke ${getTargetContext().packageName} " + Manifest.permission.ACCESS_COARSE_LOCATION);
        uiAutomation.
                executeShellCommand("pm revoke ${getTargetContext().packageName} " + Manifest.permission.ACCESS_FINE_LOCATION);
    }

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
            locationListener.onProviderEnabled(null);
            locationListener.onStatusChanged(null, 0, null);
            locationListener.onLocationChanged(mockLocation);
        });

        onView(withText("69B")).check(matches(isDisplayed()));
        onView(withContentDescription(R.string.source)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.distance)).check(matches(isDisplayed()));
    }

    @Test
    @MediumTest
    public void pinpointStartEnd() {
        Intent sendIntent = new Intent();
        activityRule.launchActivity(sendIntent);
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        int x = 200;
        final int y = device.getDisplayHeight() / 2;
        final int steps = 200;
        device.swipe(x, y, x, y, steps);
        SystemClock.sleep(1000);
        device.findObject(By.text(Pattern.compile("^.*(?i)(SOURCE).*$"))).click();

        SystemClock.sleep(1000);
        x = device.getDisplayWidth() - 200;
        device.swipe(x, y, x, y, steps);
        SystemClock.sleep(1000);
        device.findObject(By.text(Pattern.compile("^.*(?i)(DESTINATION).*$"))).click();;
        SystemClock.sleep(1000);

        device.wait(Until.findObject(By.textContains("Distance")), 250);
        device.wait(Until.findObject(By.descContains("Source")), 50);
        device.wait(Until.findObject(By.descContains("Destination")), 50);
    }

    @Test
    @FlakyTest
    public void pinpointStartEnd_Rotate() {
        Intent sendIntent = new Intent();
        activityRule.launchActivity(sendIntent);
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        int x = 200;
        final int y = device.getDisplayHeight() / 2;
        final int steps = 200;
        device.swipe(x, y, x, y, steps);
        SystemClock.sleep(1000);
        device.findObject(By.text(Pattern.compile("^.*(?i)(SOURCE).*$"))).click();

        SystemClock.sleep(1000);
        x = device.getDisplayWidth() - 200;
        device.swipe(x, y, x, y, steps);
        SystemClock.sleep(1000);
        device.findObject(By.text(Pattern.compile("^.*(?i)(DESTINATION).*$"))).click();;
        SystemClock.sleep(1000);

        activityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        activityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        SystemClock.sleep(5000);

        device.wait(Until.findObject(By.textContains("Distance")), 250);
        device.wait(Until.findObject(By.descContains("Source")), 50);
        device.wait(Until.findObject(By.descContains("Destination")), 50);
    }

    @Test
    @MediumTest
    public void onSpaceNavigation() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.putExtra(SearchManager.QUERY, "wi@pb_wb");
        activityRule.launchActivity(sendIntent);
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        maxZoomOut(device);
        device.findObject(By.textContains("PB WI")).click();
        device.wait(Until.findObject(By.descContains("12b")), 250);
    }

    @Test
    @MediumTest
    public void onSpaceNavigationInfo() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.putExtra(SearchManager.QUERY, "pb_campus");
        Location customLocation = new Location("");
        customLocation.setLatitude(53.1186219);
        customLocation.setLongitude(23.1505355);
        sendIntent.putExtra(SearchManager.EXTRA_DATA_KEY, customLocation);
        activityRule.launchActivity(sendIntent);
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        maxZoomOut(device);
        device.findObject(By.text(Pattern.compile("^.*(?i)(GWINT).*$", Pattern.DOTALL))).click();
        SystemClock.sleep(2500);
        onView(withId(R.id.design_bottom_sheet)).check(matches(isDisplayed()));
        onView(withId(R.id.info_address))
                .check(matches(allOf(isDisplayed(), withText(R.string.gwint_address))));
    }

    private void maxZoomOut(UiDevice device) {
        device.findObject(By.res("android:id/zoomIn")).click();
        SystemClock.sleep(250);
        for (int i = 0; i < 25; i++) {
            device.findObject(By.res("android:id/zoomOut")).click();
            SystemClock.sleep(250);
        }
    }

    @Test
    @FlakyTest
    public void onGpsOff() throws UiObjectNotFoundException {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.putExtra(SearchManager.QUERY, "12b@pb_wi");
        activityRule.launchActivity(sendIntent);
        LocationManager locationManagerMock = mock(LocationManager.class);
        when(locationManagerMock.isProviderEnabled(anyString())).thenReturn(false);
        injectLocationManager(activityRule.getActivity(), locationManagerMock);

        onView(withContentDescription(R.string.more_features)).perform(click());
        onView(withId(R.id.gps_fab)).perform(click());
        allowPermissionsIfNeeded(Manifest.permission.ACCESS_COARSE_LOCATION);
        allowPermissionsIfNeeded(Manifest.permission.ACCESS_FINE_LOCATION);

        onView(withSubstring("access")).check(matches(isDisplayed()));
    }

    @Test
    @FlakyTest
    public void onGpsOff_Cancel() throws UiObjectNotFoundException {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.putExtra(SearchManager.QUERY, "12b@pb_wi");
        activityRule.launchActivity(sendIntent);
        LocationManager locationManagerMock = mock(LocationManager.class);
        when(locationManagerMock.isProviderEnabled(anyString())).thenReturn(false);
        injectLocationManager(activityRule.getActivity(), locationManagerMock);

        onView(withContentDescription(R.string.more_features)).perform(click());
        onView(withId(R.id.gps_fab)).perform(click());
        allowPermissionsIfNeeded(Manifest.permission.ACCESS_COARSE_LOCATION);
        allowPermissionsIfNeeded(Manifest.permission.ACCESS_FINE_LOCATION);

        onView(withText(R.string.cancel)).perform(click());
        onView(withSubstring("access")).check(doesNotExist());
    }

    @Test
    @FlakyTest
    public void onGpsOff_Enable() throws UiObjectNotFoundException {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.putExtra(SearchManager.QUERY, "12b@pb_wi");
        activityRule.launchActivity(sendIntent);
        LocationManager locationManagerMock = mock(LocationManager.class);
        when(locationManagerMock.isProviderEnabled(anyString())).thenReturn(false);
        injectLocationManager(activityRule.getActivity(), locationManagerMock);
        onView(withContentDescription(R.string.more_features)).perform(click());
        onView(withId(R.id.gps_fab)).perform(click());
        allowPermissionsIfNeeded(Manifest.permission.ACCESS_COARSE_LOCATION);
        allowPermissionsIfNeeded(Manifest.permission.ACCESS_FINE_LOCATION);

        withIntents(() -> {
            onView(withText(R.string.enable)).perform(click());
            intended(hasAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        });
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.wait(Until.findObject(By.res("com.android.settings")), 3000);
    }

    private void injectLocationManager(MapActivity map, LocationManager locationManager) {
        try {
            Field field = map.getClass().getDeclaredField("locationManager");
            field.setAccessible(true);
            field.set(map, locationManager);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @FlakyTest
    public void onRequestPermission_Other_Ignore() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.putExtra(SearchManager.QUERY, "12b@pb_wi");
        activityRule.launchActivity(sendIntent);
        activityRule.getActivity().runOnUiThread(() -> activityRule.getActivity()
                .onRequestPermissionsResult(-1, new String[]{}, new int[]{})
        );
    }

    @Test
    @FlakyTest
    public void onRequestPermission_1_DidNotGet_ClearLocation() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.putExtra(SearchManager.QUERY, "12b@pb_wi");
        activityRule.launchActivity(sendIntent);
        activityRule.getActivity().runOnUiThread(() -> activityRule.getActivity()
                .onRequestPermissionsResult(1, new String[]{}, new int[]{})
        );
    }

    @Test
    @FlakyTest
    public void onRequestPermission_1_DidNotGet_AskedExplicitly() throws UiObjectNotFoundException {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.putExtra(SearchManager.QUERY, "12b@pb_wi");
        activityRule.launchActivity(sendIntent);

        onView(withContentDescription(R.string.more_features)).perform(click());
        onView(withId(R.id.gps_fab)).perform(click());
        allowPermissionsIfNeeded(Manifest.permission.ACCESS_COARSE_LOCATION);
        allowPermissionsIfNeeded(Manifest.permission.ACCESS_FINE_LOCATION);
        setExplicitlyAskedForPermissions(activityRule.getActivity());

        activityRule.getActivity().runOnUiThread(() -> activityRule.getActivity()
                .onRequestPermissionsResult(1, new String[]{}, new int[]{})
        );

        onView(withSubstring("Please allow")).check(matches(isDisplayed()));
        onView(withText(R.string.ok)).perform(click());
        onView(withSubstring("Please allow")).check(doesNotExist());
    }


    private void setExplicitlyAskedForPermissions(MapActivity map) {
        try {
            Field field = map.getClass().getDeclaredField("explicitlyAskedForPermissions");
            field.setAccessible(true);
            field.set(map, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}