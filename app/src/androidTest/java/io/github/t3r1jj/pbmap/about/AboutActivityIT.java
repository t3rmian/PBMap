package io.github.t3r1jj.pbmap.about;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.filters.MediumTest;
import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import java.util.regex.Pattern;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.testing.RetryRunner;
import io.github.t3r1jj.pbmap.testing.ScreenshotOnTestFailedRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActionsExt.swipeLeftExt;
import static androidx.test.espresso.action.ViewActionsExt.swipeRightExt;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasType;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.uiautomator.By.text;
import static io.github.t3r1jj.pbmap.testing.TestUtils.nthChildOf;
import static io.github.t3r1jj.pbmap.testing.TestUtils.withIntents;
import static io.github.t3r1jj.pbmap.testing.TestUtils.withMenuIdOrContentDescription;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@RunWith(RetryRunner.class)
public class AboutActivityIT {

    @Rule
    public RuleChain testRule = RuleChain
            .outerRule(new ActivityTestRule<>(AboutActivity.class, true, true))
            .around(new ScreenshotOnTestFailedRule());

    @After
    public void tearDown() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.waitForIdle();
        if (device.findObject(text(Pattern.compile(".*(?i)(Open with).*"))) != null) {
            device.pressBack();
        }
    }

    @Test
    @SmallTest
    public void onAboutCreate() {
        onView(ViewMatchers.withId(R.id.about_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.about_rate)).check(matches(isDisplayed()));
        onView(withId(R.id.about_report)).check(matches(isDisplayed()));
        onView(withId(R.id.about_support)).check(matches(isDisplayed()));
        onView(withText(R.string.about_attributions)).check(matches(isDisplayed()));
        onView(withText(R.string.about_licenses)).check(matches(isDisplayed()));
    }

    @Test
    @MediumTest
    public void onBugReportPress_correctIntent() {
        withIntents(() -> {
            onView(withId(R.id.about_report)).perform(click());
            intended(allOf(
                    hasAction(Intent.ACTION_VIEW),
                    hasData(Uri.parse(getInstrumentation().getTargetContext().getString(R.string.about_report_link)))
            ));
        });
    }

    @Test
    @MediumTest
    public void onSupportPress_correctIntent() {
        withIntents(() -> {
            onView(withId(R.id.about_support)).perform(click());
            intended(allOf(
                    hasAction(Intent.ACTION_VIEW),
                    hasData(Uri.parse(getInstrumentation().getTargetContext().getString(R.string.about_support_link)))
            ));
        });
    }

    @Test
    @MediumTest
    public void onRatePress_correctIntent() {
        withIntents(() -> {
            onView(withId(R.id.about_rate)).perform(click());
            intended(allOf(
                    hasAction(Intent.ACTION_VIEW),
                    hasData(Uri.parse("market://details?id=" + getInstrumentation().getTargetContext().getPackageName()))
            ));
        });
    }

    @Test
    @MediumTest
    public void onProjectPress_correctIntent() {
        withIntents(() -> {
            onView(withId(R.id.about_icon)).perform(click());
            intended(allOf(
                    hasAction(Intent.ACTION_VIEW),
                    hasData(Uri.parse(getInstrumentation().getTargetContext().getString(R.string.about_project_link)))
            ));
        });
    }

    @Test
    @LargeTest
    public void onShare_correctIntent() {
        withIntents(() -> {
            SystemClock.sleep(1000);
            try {
                onView(withMenuIdOrContentDescription(R.id.action_share, R.string.action_share)).perform(click());
                SystemClock.sleep(5000);
                onView(nthChildOf(withContentDescription(containsString("Choose")), 0)).perform(click());
            } catch (NoMatchingViewException no) {
                Log.w(AboutActivityIT.class.getSimpleName(), no);
                onView(withId(R.id.default_activity_button)).perform(click());
            }
            intended(allOf(
                    hasAction(Intent.ACTION_SEND),
                    hasType("text/plain"),
                    hasComponent(is(any(ComponentName.class))),
                    hasExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id="
                            + getInstrumentation().getTargetContext().getPackageName())
            ));
        });
    }

    @Test
    @LargeTest
    public void onLicensesClick_correctContent() throws InterruptedException {
        onView(withId(R.id.about_licenses)).perform(click());
        Thread.sleep(30000);
        onView(withId(R.id.dialog_licenses)).check(matches(isDisplayed()));
        onView(withText(R.string.about_licenses)).check(matches(isDisplayed()));
    }

    @Test
    @LargeTest
    public void onAttributionsSwipe_correctContent() throws InterruptedException {
        int creditIndex = 0;
        String[] titles = getInstrumentation().getTargetContext().getResources().getStringArray(R.array.attribution_titles);
        String[] descriptions = getInstrumentation().getTargetContext().getResources().getStringArray(R.array.attribution_descriptions);
        do { // go right till the end
            Thread.sleep(1000);
            onView(withText(titles[creditIndex])).check(matches(isDisplayed()));
            onView(withText(descriptions[creditIndex])).check(matches(isDisplayed()));
            onView(withText(descriptions[creditIndex])).perform(swipeLeftExt());
        } while (++creditIndex < titles.length);
        creditIndex--;
        do { // go left till the start
            Thread.sleep(1000);
            onView(withText(titles[creditIndex])).check(matches(isDisplayed()));
            onView(withText(descriptions[creditIndex])).check(matches(isDisplayed()));
            onView(withText(descriptions[creditIndex])).perform(swipeRightExt());
        } while (--creditIndex >= 0);
    }
}