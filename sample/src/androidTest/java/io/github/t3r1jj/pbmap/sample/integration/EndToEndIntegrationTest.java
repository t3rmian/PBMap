package io.github.t3r1jj.pbmap.sample.integration;


import android.content.pm.PackageManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.fail;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EndToEndIntegrationTest {

    private static final int TIMEOUT_MS = 5000;

    @Rule
    public ActivityTestRule<IntegrationActivity> testRule =
            new ActivityTestRule<>(IntegrationActivity.class, true, true);

    @Test
    public void pinpointPlace() throws UiObjectNotFoundException, PackageManager.NameNotFoundException {
        getInstrumentation().getTargetContext().getPackageManager().getPackageInfo("io.github.t3r1jj.pbmap", 0);
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        device.waitForIdle();
        device.findObject(new UiSelector().textMatches("(?i)(PINPOINT DEFINED PLACE)")).clickAndWaitForNewWindow();
        String menuText = "PBMap";
        if (!device.findObject(new UiSelector().textContains(menuText)).waitForExists(TIMEOUT_MS)) {
            fail("Could not find UI text: " + menuText);
        }
        String placeText = "130";
        if (!device.findObject(new UiSelector().textContains(placeText)).waitForExists(TIMEOUT_MS)) {
            fail("Could not find UI text: " + placeText);
        }
    }

    @Test
    public void customPinpoint() throws UiObjectNotFoundException, PackageManager.NameNotFoundException {
        getInstrumentation().getTargetContext().getPackageManager().getPackageInfo("io.github.t3r1jj.pbmap", 0);
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        device.waitForIdle();
        device.findObject(new UiSelector().textMatches("(?i)(PINPOINT CUSTOM LOCATION)")).clickAndWaitForNewWindow();
        String menuText = "PBMap";
        if (!device.findObject(new UiSelector().textContains(menuText)).waitForExists(TIMEOUT_MS)) {
            fail("Could not find UI text: " + menuText);
        }
        String placeText = "PB WI L2";
        if (!device.findObject(new UiSelector().textContains(placeText)).waitForExists(TIMEOUT_MS)) {
            fail("Could not find UI text: " + placeText);
        }
    }
}
