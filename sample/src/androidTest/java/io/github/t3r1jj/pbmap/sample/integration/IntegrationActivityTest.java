package io.github.t3r1jj.pbmap.sample.integration;

import android.app.SearchManager;
import android.content.Intent;
import android.location.Location;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

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
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class IntegrationActivityTest {

    @Rule
    public ActivityTestRule<IntegrationActivity> activityRule =
            new ActivityTestRule<>(IntegrationActivity.class, true, true);

    @Rule
    public ScreenshotTestFailedRule screenshotRule = new ScreenshotTestFailedRule();

    @Test
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
    public void onDefinedPinpoint() {
        String query = "130@pb_wi_l2";
        onView(withId(R.id.search_query_text))
                .perform(clearText())
                .perform(typeText(query), closeSoftKeyboard());
        Intents.init();
        onView(withId(R.id.pinpoint_place_button)).perform(click());
        intended(allOf(
                hasAction(Intent.ACTION_SEARCH),
                hasExtra(SearchManager.QUERY, query),
                toPackage("io.github.t3r1jj.pbmap")
        ));
        Intents.release();
    }

    @Test
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
        Intents.init();
        onView(withId(R.id.pinpoint_location_button)).perform(click());
        intended(allOf(
                hasAction(Intent.ACTION_SEARCH),
                hasExtra(SearchManager.QUERY, map),
                hasExtra(equalTo(SearchManager.EXTRA_DATA_KEY), new LocationMatcher(location)),
                toPackage("io.github.t3r1jj.pbmap")
        ));
        Intents.release();
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
            description.appendText("with location that matches lat: " + location.getLatitude() + ", lng: " + location.getLongitude());
        }
    }
}