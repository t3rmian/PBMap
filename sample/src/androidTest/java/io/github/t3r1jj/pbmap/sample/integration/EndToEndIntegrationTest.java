package io.github.t3r1jj.pbmap.sample.integration;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.StringRes;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.filters.MediumTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import java.util.regex.Pattern;

import io.github.t3r1jj.pbmap.testing.ScreenshotOnTestFailedRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.uiautomator.By.text;
import static io.github.t3r1jj.pbmap.testing.TestUtils.pressDoubleBack;
import static junit.framework.TestCase.fail;
import static org.hamcrest.core.IsNot.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EndToEndIntegrationTest {

    private static final int TIMEOUT_MS = 5 * 60 * 1000;

    private final ActivityTestRule<IntegrationActivity> activityRule =
            new ActivityTestRule<>(IntegrationActivity.class, true, true);
    @Rule
    public RuleChain testRule = RuleChain
            .outerRule(activityRule)
            .around(new ScreenshotOnTestFailedRule());

    private Activity activity;

    @Before
    public void setUp() {
        activity = activityRule.getActivity();
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject2 waitButton = device.findObject(text(Pattern.compile("^(?i)(WAIT)$")));
        if (waitButton != null) {
            waitButton.click();
        }
    }

    @After
    public void tearDown() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        pressDoubleBack(device);
    }

    @Test
    @MediumTest
    public void pinpointPlace() throws UiObjectNotFoundException, PackageManager.NameNotFoundException {
        getInstrumentation().getTargetContext().getPackageManager()
                .getPackageInfo("io.github.t3r1jj.pbmap", 0);
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        device.waitForIdle();
        device.findObject(new UiSelector().textMatches("^(?i)(PINPOINT DEFINED PLACE)$")).clickAndWaitForNewWindow();
        String menuText = "PBMap";
        if (!device.findObject(new UiSelector().textContains(menuText)).waitForExists(TIMEOUT_MS)) {
            fail("Could not find UI text: " + menuText);
        }
        String placeText = "PB WI L2";
        if (!device.findObject(new UiSelector().textContains(placeText)).exists()) {
            fail("Could not find UI text: " + placeText);
        }
    }

    @Test
    @MediumTest
    public void customPinpoint() throws UiObjectNotFoundException, PackageManager.NameNotFoundException {
        getInstrumentation().getTargetContext().getPackageManager()
                .getPackageInfo("io.github.t3r1jj.pbmap", 0);
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        device.waitForIdle();
        device.findObject(new UiSelector().textMatches("^(?i)(PINPOINT CUSTOM LOCATION)$")).clickAndWaitForNewWindow();
        String menuText = "PBMap";
        if (!device.findObject(new UiSelector().textContains(menuText)).waitForExists(TIMEOUT_MS)) {
            fail("Could not find UI text: " + menuText);
        }
        String placeText = "PB campus";
        if (!device.findObject(new UiSelector().textContains(placeText)).exists()) {
            fail("Could not find UI text: " + placeText);
        }
    }

    @Test
    @MediumTest
    public void displayPlaceIds() {
        onView(withId(R.id.show_place_ids)).perform(click());
        UiDevice device = verifyCommonDisplayPlaces(R.string.places_by_id);
        if (!device.findObject(new UiSelector().textContains("PB_")).exists()) {
            fail("Could not find UI text: " + "PB_");
        }
        if (device.findObject(new UiSelector().textContains("PB ")).exists()) {
            fail("Could (sic!) find UI text: " + "PB ");
        }
    }

    @Test
    @MediumTest
    public void displayPlaceNames() {
        onView(withId(R.id.show_place_names)).perform(click());
        UiDevice device = verifyCommonDisplayPlaces(R.string.places_by_name);
        if (!device.findObject(new UiSelector().textContains("PB ")).exists()) {
            fail("Could not find UI text: " + "PB ");
        }
        if (device.findObject(new UiSelector().textContains("PB_")).exists()) {
            fail("Could (sic!) find UI text: " + "PB_");
        }
    }

    private UiDevice verifyCommonDisplayPlaces(@StringRes int titleRes) {
        onView(withText(R.string.loading))
                .inRoot(withDecorView(not(activity.getWindow().getDecorView())))
                .check(matches(isDisplayed()));
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        Context ctx = getInstrumentation().getTargetContext();
        String placesText = ctx.getString(titleRes);
        if (!device.findObject(new UiSelector().textContains(placesText)).waitForExists(TIMEOUT_MS)) {
            fail("Could not find UI text: " + placesText);
        }
        if (!device.findObject(new UiSelector().text(PBMapIntegrator.ContentMapping.MAP_COLUMN.getColumnName())).exists()) {
            fail("Could not find UI text: " + PBMapIntegrator.ContentMapping.MAP_COLUMN.getColumnName());
        }
        if (!device.findObject(new UiSelector().text(PBMapIntegrator.ContentMapping.PLACE_COLUMN.getColumnName())).exists()) {
            fail("Could not find UI text: " + PBMapIntegrator.ContentMapping.PLACE_COLUMN.getColumnName());
        }
        return device;
    }
}
