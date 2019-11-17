package io.github.t3r1jj.pbmap.main;

import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.widget.ImageView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.testing.ScreenshotOnTestFailedRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MapActivityIT {

    private final ActivityTestRule<MapActivity> activityRule =
            new ActivityTestRule<>(MapActivity.class, true, true);

    @Rule
    public RuleChain testRule = RuleChain
            .outerRule(activityRule)
            .around(new ScreenshotOnTestFailedRule());

    @Test
    @LargeTest
    public void initiateImprovementUseCase() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        int x = device.getDisplayWidth() / 2;
        int y = device.getDisplayHeight() / 2;
        device.swipe(x, y, x, y, 500);

        onView(withText(R.string.place_destination_marker)).check(matches(isDisplayed()));
        onView(withText(R.string.place_source_marker)).check(matches(isDisplayed()));
        onView(withText(R.string.improve)).check(matches(isDisplayed()));
        onView(withText(R.string.place_destination_marker)).check(matches(isDisplayed()));
    }

    @Test
    @SmallTest
    public void setLogo() {
        AtomicBoolean successfulResult = new AtomicBoolean();
        Drawable drawable = InstrumentationRegistry.getInstrumentation().getContext().getDrawable(io.github.t3r1jj.pbmap.test.R.drawable.test_logo);
        ImageView logo = new ImageView(InstrumentationRegistry.getInstrumentation().getTargetContext());
        logo.setImageDrawable(drawable);
        activityRule.getActivity().runOnUiThread(() -> {
            activityRule.getActivity().setLogo(logo);
            successfulResult.set(true);
        });
        SystemClock.sleep(1000);
        assertTrue(successfulResult.get());
    }

}