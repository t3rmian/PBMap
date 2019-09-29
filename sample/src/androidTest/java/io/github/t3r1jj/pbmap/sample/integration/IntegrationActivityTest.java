package io.github.t3r1jj.pbmap.sample.integration;

import android.app.SearchManager;
import android.content.Intent;
import android.location.Location;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
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
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import io.github.t3r1jj.pbmap.testing.ScreenshotOnTestFailedRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.uiautomator.By.text;
import static io.github.t3r1jj.pbmap.testing.TestUtils.pressDoubleBack;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class IntegrationActivityTest {

    @Rule
    public RuleChain testRule = RuleChain
            .outerRule(new IntentsTestRule<>(IntegrationActivity.class, true, true))
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
    @SmallTest
    @MediumTest
    @LargeTest
    public void onCreate() {
        onView(withId(R.id.description)).check(matches(isDisplayed()));
        onView(withId(R.id.search_query_text)).check(matches(isDisplayed()));
        onView(withId(R.id.map_id_text)).check(matches(isDisplayed()));
        onView(withId(R.id.lat_text)).check(matches(isDisplayed()));
        onView(withId(R.id.lng_text)).check(matches(isDisplayed()));
        onView(withId(R.id.pinpoint_place_button)).check(matches(isDisplayed()));
        onView(withId(R.id.pinpoint_location_button)).check(matches(isDisplayed()));
    }

    @Test
    @MediumTest
    public void onDefinedPinpoint() {
        String query = "130@pb_wi_l2";
        onView(withId(R.id.search_query_text))
                .perform(clearText())
                .perform(typeText(query), closeSoftKeyboard());
        onView(withId(R.id.pinpoint_place_button)).perform(click());
        intended(allOf(
                hasAction(Intent.ACTION_SEARCH),
                hasExtra(SearchManager.QUERY, query),
                toPackage("io.github.t3r1jj.pbmap")
        ));
    }

    @Test
    @MediumTest
    public void onCustomPinpoint() {
        String map = "pb_campus";
        double lat = 53.11878;
        double lng = 23.14878;
        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lng);
        onView(withId(R.id.map_id_text))
                .perform(clearText())
                .perform(typeText(map), closeSoftKeyboard());
        onView(withId(R.id.lat_text))
                .perform(clearText())
                .perform(typeText(Double.toString(lat)), closeSoftKeyboard());
        onView(withId(R.id.lng_text))
                .perform(clearText())
                .perform(typeText(Double.toString(lng)), closeSoftKeyboard());
        onView(withId(R.id.pinpoint_location_button)).perform(click());
        intended(allOf(
                hasAction(Intent.ACTION_SEARCH),
                hasExtra(SearchManager.QUERY, map),
                hasExtra(equalTo(SearchManager.EXTRA_DATA_KEY), new LocationMatcher(location)),
                toPackage("io.github.t3r1jj.pbmap")
        ));
    }

    private static class LocationMatcher extends TypeSafeMatcher<Location> {
        private final Location location;

        private LocationMatcher(Location location) {
            this.location = location;
        }

        @Override
        public boolean matchesSafely(Location item) {
            return item.getLatitude() == location.getLatitude() &&
                    item.getLongitude() == location.getLongitude();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("with location that matches lat: "
                    + location.getLatitude() + ", lng: " + location.getLongitude());
        }
    }
}