package io.github.t3r1jj.pbmap.sample.integration;


import android.content.pm.PackageManager;
import android.os.SystemClock;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import java.util.regex.Pattern;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.filters.MediumTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import io.github.t3r1jj.pbmap.testing.ScreenshotOnTestFailedRule;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.uiautomator.By.text;
import static io.github.t3r1jj.pbmap.testing.TestUtils.pressDoubleBack;
import static junit.framework.TestCase.fail;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EndToEndIntegrationTest {

    private static final int TIMEOUT_MS = 5 * 60 * 1000;

    @Rule
    public RuleChain testRule = RuleChain
            .outerRule(new ActivityTestRule<>(IntegrationActivity.class, true, true))
            .around(new ScreenshotOnTestFailedRule());

    @Before
    public void setUp() {
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
            SystemClock.sleep(10000);
            fail("Could not find UI text: " + menuText);
        }
        String placeText = "L2 [WI]";
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
        String placeText = "BUT campus";
        if (!device.findObject(new UiSelector().textContains(placeText)).exists()) {
            fail("Could not find UI text: " + placeText);
        }
    }
}
