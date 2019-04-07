package io.github.t3r1jj.pbmap;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import io.github.t3r1jj.pbmap.testing.ScreenshotOnTestFailedRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AboutActivityIT {

    @Rule
    public RuleChain testRule = RuleChain
            .outerRule(new ActivityTestRule<>(AboutActivity.class, true, true))
            .around(new ScreenshotOnTestFailedRule());

    @Test
    public void onAboutCreate() {
        onView(withId(R.id.about_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.about_rate)).check(matches(isDisplayed()));
        onView(withId(R.id.about_report)).check(matches(isDisplayed()));
        onView(withId(R.id.about_support)).check(matches(isDisplayed()));
        onView(withText(R.string.about_attributions)).check(matches(isDisplayed()));
        onView(withText(R.string.about_licenses)).check(matches(isDisplayed()));
    }

    @Test
    public void onBugReportPress_correctIntent() {
    }

    @Test
    public void onSupportPress_correctIntent() {
    }

    @Test
    public void onRatePress_correctIntent() {
    }

    @Test
    public void onProjectPress_correctIntent() {
    }

    @Test
    public void onShare_correctIntent() {
    }

    @Test
    public void onLicensesClick_correctContent() {
    }

    @Test
    public void onAttributionsSwipe_correctContent() {
    }
}