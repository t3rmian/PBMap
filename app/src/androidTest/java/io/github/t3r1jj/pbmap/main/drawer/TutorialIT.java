package io.github.t3r1jj.pbmap.main.drawer;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.main.MapActivity;
import io.github.t3r1jj.pbmap.testing.ScreenshotOnTestFailedRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static io.github.t3r1jj.pbmap.testing.TestUtils.withIndex;

@RunWith(AndroidJUnit4.class)
public class TutorialIT {

    private static final int TIMEOUT_MS = 100;
    private final ActivityTestRule<MapActivity> activityRule =
            new ActivityTestRule<>(MapActivity.class, true, false);

    @Rule
    public RuleChain testRule = RuleChain
            .outerRule(activityRule)
            .around(new ScreenshotOnTestFailedRule());

    @Test
    @LargeTest
    public void testTutorial() {
        MapsDrawerFragmentIT.autoOpenDrawerReturningPreferences(true);
        activityRule.launchActivity(new Intent());

        for (int i = 0; i < 20; i++) {
            onView(withIndex(withId(R.id.design_menu_item_text), 1)).perform(swipeUp());
        }

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        Context ctx = getInstrumentation().getTargetContext();

        device.wait(Until.findObjects(By.text(ctx.getString(R.string.help))), TIMEOUT_MS);
        for (UiObject2 obj : device.findObjects(By.text(ctx.getString(R.string.help)))) {
            obj.click();
        }

        verifyTutorial(device, ctx);
    }

    public static void verifyTutorial(UiDevice device, Context ctx) {
        device.wait(Until.findObject(By.text(ctx.getString(R.string.action_search))), TIMEOUT_MS);
        device.wait(Until.findObject(By.text(ctx.getString(R.string.action_search_description))), TIMEOUT_MS);
        device.findObject(By.res("io.github.t3r1jj.pbmap:id/action_search")).click();

        device.wait(Until.findObject(By.text(ctx.getString(R.string.menu))), TIMEOUT_MS);
        device.wait(Until.findObject(By.text(ctx.getString(R.string.menu_description))), TIMEOUT_MS);
        device.findObject(By.descContains(ctx.getString(R.string.navigation_drawer_open))).click();

        device.wait(Until.findObject(By.text(ctx.getString(R.string.floor))), TIMEOUT_MS);
        device.wait(Until.findObject(By.text(ctx.getString(R.string.floor_description))), TIMEOUT_MS);
        device.findObject(By.res("io.github.t3r1jj.pbmap:id/level_fab_menu")).click();

        device.wait(Until.findObject(By.text(ctx.getString(R.string.more_features))), TIMEOUT_MS);
        device.wait(Until.findObject(By.text(ctx.getString(R.string.more_features_description))), TIMEOUT_MS);
        device.findObject(By.res("io.github.t3r1jj.pbmap:id/more_fab_menu")).click();

        device.wait(Until.findObject(By.text(ctx.getString(R.string.maps))), TIMEOUT_MS);
        device.wait(Until.findObject(By.text(ctx.getString(R.string.maps_description))), TIMEOUT_MS);
        device.findObject(By.res("io.github.t3r1jj.pbmap:id/content_main")).click();

        device.wait(Until.findObject(By.text(ctx.getString(R.string.action_back))), TIMEOUT_MS);
        device.wait(Until.findObject(By.text(ctx.getString(R.string.action_back_description))), TIMEOUT_MS);
        device.findObject(By.res("io.github.t3r1jj.pbmap:id/action_back")).click();
        onView(withId(R.id.help_fab)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

}