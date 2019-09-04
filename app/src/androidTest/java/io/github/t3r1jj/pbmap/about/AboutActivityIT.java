package io.github.t3r1jj.pbmap.about;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.filters.MediumTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import io.github.t3r1jj.pbmap.R;
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
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static io.github.t3r1jj.pbmap.testing.TestUtils.withIndex;
import static io.github.t3r1jj.pbmap.testing.TestUtils.withIntents;
import static io.github.t3r1jj.pbmap.testing.TestUtils.withMenuIdOrContentDescription;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class AboutActivityIT {

    @Rule
    public RuleChain testRule = RuleChain
            .outerRule(new ActivityTestRule<>(AboutActivity.class, true, true))
            .around(new ScreenshotOnTestFailedRule());

    @After
    public void tearDown() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.pressBack();
        device.pressBack();
        Log.e("AAA", "BAAAAAAACK");
    }

    @Test
    public void onAboutCreate() {
        onView(ViewMatchers.withId(R.id.about_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.about_rate)).check(matches(isDisplayed()));
        onView(withId(R.id.about_report)).check(matches(isDisplayed()));
        onView(withId(R.id.about_support)).check(matches(isDisplayed()));
        onView(withText(R.string.about_attributions)).check(matches(isDisplayed()));
        onView(withText(R.string.about_licenses)).check(matches(isDisplayed()));
    }

    @Test
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
    public void onShare_correctIntent() {
        withIntents(() -> {
            onView(withMenuIdOrContentDescription(R.id.action_share, R.string.action_share)).perform(click());
            onView(withIndex(not(withText(isEmptyOrNullString())), 1)).perform(click());
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
    @MediumTest
    public void onLicensesClick_correctContent() throws InterruptedException {
        onView(withId(R.id.about_licenses)).perform(click());
        Thread.sleep(1000);
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