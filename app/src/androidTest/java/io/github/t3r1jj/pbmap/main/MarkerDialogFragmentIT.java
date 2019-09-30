package io.github.t3r1jj.pbmap.main;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.filters.MediumTest;

import com.android21buttons.fragmenttestrule.FragmentTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.testing.ScreenshotOnTestFailedRule;
import io.github.t3r1jj.pbmap.view.map.routing.GeoMarker;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MarkerDialogFragmentIT {
    private final MarkerDialogFragment fragment = new MarkerDialogFragment();
    private final FragmentTestRule<?, MarkerDialogFragment> outerRule =
            new FragmentTestRule<MapActivity, MarkerDialogFragment>
                    (MapActivity.class, MarkerDialogFragment.class, true, true, false) {
                protected MarkerDialogFragment createFragment() {
                    return fragment;
                }
            };

    @Rule
    public RuleChain testRule = RuleChain
            .outerRule(outerRule)
            .around(new ScreenshotOnTestFailedRule());

    private MotionEvent motionEvent;
    private MotionEvent capturedEvent;
    private GeoMarker.Marker capturedMarker;

    @Before
    public void setUp() {
        Bundle bundle = new Bundle();
        motionEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                MotionEvent.ACTION_DOWN, 0, 0, 0);
        bundle.putParcelable(MarkerDialogFragment.MOTION_EVENT_KEY, motionEvent);
        fragment.setArguments(bundle);
        MapActivity map = (MapActivity) outerRule.getActivity();
        Controller controller = new ControllerTestProxy(map);
        ImproveDialogFragmentIT.injectMapController(map, controller);
        fragment.show(map.getSupportFragmentManager(), "test");
    }

    @Test
    @MediumTest
    public void onCreate() {
        onView(withText(R.string.place_destination_marker)).check(matches(isDisplayed()));
        onView(withText(R.string.place_source_marker)).check(matches(isDisplayed()));
        onView(withText(R.string.improve)).check(matches(isDisplayed()));
        onView(withText(R.string.place_destination_marker)).check(matches(isDisplayed()));
    }

    @Test
    @MediumTest
    public void onDestinationClick() {
        onView(withText(R.string.place_destination_marker)).perform(click());
        assertEquals(motionEvent, capturedEvent);
        assertEquals(GeoMarker.Marker.DESTINATION, capturedMarker);
        verifyFragmentDoesNotExist();
    }

    @Test
    @MediumTest
    public void onSourceClick() {
        onView(withText(R.string.place_source_marker)).perform(click());
        assertEquals(motionEvent, capturedEvent);
        assertEquals(GeoMarker.Marker.SOURCE, capturedMarker);
        verifyFragmentDoesNotExist();
    }

    @Test
    @LargeTest
    public void onImproveClick() {
        onView(withText(R.string.improve)).perform(click());
        assertNull(capturedEvent);
        assertNull(capturedMarker);

        onView(withText(R.string.improve)).check(matches(isDisplayed()));
        onView(withText(R.string.improve_message)).check(matches(isDisplayed()));
        onView(withText(R.string.report)).check(matches(isDisplayed()));
        onView(withText(R.string.cancel)).check(matches(isDisplayed()));
    }

    @Test
    @MediumTest
    public void onCancel() {
        onView(withText(R.string.place_destination_marker)).perform(click());
        verifyFragmentDoesNotExist();
    }

    private void verifyFragmentDoesNotExist() {
        onView(withText(R.string.place_destination_marker)).check(doesNotExist());
        onView(withText(R.string.place_source_marker)).check(doesNotExist());
        onView(withText(R.string.improve)).check(doesNotExist());
        onView(withText(R.string.place_destination_marker)).check(doesNotExist());
    }

    private class ControllerTestProxy extends Controller {
        private final Controller oldController;

        private ControllerTestProxy(MapActivity map) {
            super(map);
            this.oldController = map.getController();
        }

        @Override
        void onUserMarkerChoice(MotionEvent event, GeoMarker.Marker markerChoice) {
            MarkerDialogFragmentIT.this.capturedEvent = event;
            MarkerDialogFragmentIT.this.capturedMarker = markerChoice;
        }

        @Override
        Memento getCurrentState() {
            return oldController.getCurrentState();
        }
    }
}