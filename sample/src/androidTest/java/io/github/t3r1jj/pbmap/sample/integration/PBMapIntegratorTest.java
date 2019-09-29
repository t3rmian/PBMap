package io.github.t3r1jj.pbmap.sample.integration;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import java.util.regex.Pattern;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.filters.MediumTest;
import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import io.github.t3r1jj.pbmap.testing.ScreenshotOnTestFailedRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.uiautomator.By.text;
import static io.github.t3r1jj.pbmap.testing.TestUtils.pressDoubleBack;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PBMapIntegratorTest {

    private static final int TOAST_TIMEOUT_MS = 3500;
    private final ActivityTestRule<IntegrationActivity> activityRule = new IntentsTestRule<>(IntegrationActivity.class, true, true);
    @Rule
    public RuleChain testRule = RuleChain
            .outerRule(activityRule)
            .around(new ScreenshotOnTestFailedRule());
    private PBMapIntegrator integrator;
    private IntegrationActivity activity;

    @Before
    public void setUp() throws InterruptedException {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject2 waitButton = device.findObject(text(Pattern.compile("^(?i)(WAIT)$")));
        if (waitButton != null) {
            waitButton.click();
        } else {
            Thread.sleep(TOAST_TIMEOUT_MS);
        }
        activity = activityRule.getActivity();
        integrator = activity.pbMapIntegrator = spy(activity.pbMapIntegrator);
    }

    @After
    public void tearDown() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        pressDoubleBack(device);
    }

    @Test(expected = ActivityNotFoundException.class)
    @MediumTest
    public void foundNothingInstalled() {
        doThrow(new ActivityNotFoundException("Mock activity not found"))
                .when(integrator).startActivity(any(Intent.class));
        integrator.startActivity("query");
    }

    @Test
    @MediumTest
    public void foundNothingInstalled_activityToast_onDefinedPinpoint() {
        doThrow(new ActivityNotFoundException("Mock activity not found"))
                .when(integrator).startActivity(any(Intent.class));
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> activity.onDefinedPinpoint(null));
        onView(withText(R.string.could_not_open_android_market))
                .inRoot(withDecorView(not(activity.getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    @MediumTest
    public void foundNothingInstalled_activityToast_onCustomPinpoint() {
        doThrow(new ActivityNotFoundException("Mock activity not found"))
                .when(integrator).startActivity(any(Intent.class));
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> activity.onCustomPinpoint(null));
        onView(withText(R.string.could_not_open_android_market))
                .inRoot(withDecorView(not(activity.getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    @MediumTest
    public void invalidNumberFormat_lat_activityToast_onCustomPinpoint() {
        doThrow(new NumberFormatException("Mock location parsing"))
                .when(integrator).startActivity(any(Intent.class));

        onView(withId(R.id.lat_text))
                .perform(clearText())
                .perform(typeText(""), closeSoftKeyboard());

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> activity.onCustomPinpoint(null));

        onView(withText(R.string.incorrect_location_format))
                .inRoot(withDecorView(not(activity.getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    @MediumTest
    public void invalidNumberFormat_lng_activityToast_onCustomPinpoint() {
        doThrow(new NumberFormatException("Mock location parsing"))
                .when(integrator).startActivity(any(Intent.class));

        onView(withId(R.id.lng_text))
                .perform(clearText())
                .perform(typeText(""), closeSoftKeyboard());

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> activity.onCustomPinpoint(null));

        onView(withText(R.string.incorrect_location_format))
                .inRoot(withDecorView(not(activity.getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    @MediumTest
    public void appNotInstalled_openMarket() {
        doAnswer(__ -> {
            doCallRealMethod().when(integrator).startActivity(any(Intent.class));
            throw new ActivityNotFoundException("Mock activity not found");
        }).when(integrator).startActivity(any(Intent.class));
        integrator.startActivity("query");
        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse("market://details?id=io.github.t3r1jj.pbmap"))
        ));
    }

    @Test
    @MediumTest
    public void appAndMarketNotInstalled_openGooglePlay() {
        doAnswer(__ -> {
            doAnswer(___ -> {
                doCallRealMethod().when(integrator).startActivity(any(Intent.class));
                throw new ActivityNotFoundException("Mock activity 2 not found");
            }).when(integrator).startActivity(any(Intent.class));
            throw new ActivityNotFoundException("Mock activity 1 not found");
        }).when(integrator).startActivity(any(Intent.class));
        integrator.startActivity("query");
        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse("https://play.google.com/store/apps/details?id=io.github.t3r1jj.pbmap"))
        ));
    }

    @Test(expected = ActivityNotFoundException.class)
    @MediumTest
    public void foundNothingInstalled_locationVariant() {
        doThrow(new ActivityNotFoundException("Mock activity not found"))
                .when(integrator).startActivity(any(Intent.class));
        integrator.startActivity("query", new Location(""));
    }

    @Test
    @MediumTest
    public void appNotInstalled_openMarket_locationVariant() {
        doAnswer(__ -> {
            doCallRealMethod().when(integrator).startActivity(any(Intent.class));
            throw new ActivityNotFoundException("Mock activity not found");
        }).when(integrator).startActivity(any(Intent.class));
        integrator.startActivity("query", new Location(""));
        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse("market://details?id=io.github.t3r1jj.pbmap"))
        ));
    }

    @Test
    @MediumTest
    public void appAndMarketNotInstalled_openGooglePlay_locationVariant() {
        doAnswer(__ -> {
            doAnswer(___ -> {
                doCallRealMethod().when(integrator).startActivity(any(Intent.class));
                throw new ActivityNotFoundException("Mock activity 2 not found");
            }).when(integrator).startActivity(any(Intent.class));
            throw new ActivityNotFoundException("Mock activity 1 not found");
        }).when(integrator).startActivity(any(Intent.class));
        integrator.startActivity("query", new Location(""));
        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse("https://play.google.com/store/apps/details?id=io.github.t3r1jj.pbmap"))
        ));
    }
}