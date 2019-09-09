package io.github.t3r1jj.pbmap.main;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;

import androidx.test.platform.app.InstrumentationRegistry;

import com.android21buttons.fragmenttestrule.FragmentTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.testing.ScreenshotOnTestFailedRule;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ImproveDialogFragmentIT {

    private final ImproveDialogFragment fragment = new ImproveDialogFragment();
    private final FragmentTestRule<?, ImproveDialogFragment> outerRule =
            new FragmentTestRule<MapActivity, ImproveDialogFragment>
                    (MapActivity.class, ImproveDialogFragment.class, true, true, false) {
                protected ImproveDialogFragment createFragment() {
                    return fragment;
                }
            };
    private final String typedText = "test";

    @Rule
    public RuleChain testRule = RuleChain
            .outerRule(outerRule)
            .around(new ScreenshotOnTestFailedRule());

    private int improvementCount;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        Bundle bundle = new Bundle();
        MotionEvent motionEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                MotionEvent.ACTION_DOWN, 0, 0, 0);
        bundle.putParcelable(MarkerDialogFragment.MOTION_EVENT_KEY, motionEvent);
        fragment.setArguments(bundle);
        MapActivity map = (MapActivity) outerRule.getActivity();
        Controller controller = new Controller(map) {
            @Override
            void onImprovePressed(MotionEvent event, String description) {
                assertEquals(motionEvent, event);
                assertEquals(typedText, description);
                improvementCount++;
            }
        };
        Field field = map.getClass().getDeclaredField("controller");
        field.setAccessible(true);
        field.set(map, controller);
        fragment.show(map.getSupportFragmentManager(), "test");
    }

    @Test
    public void onCreateDialog() {
        onView(withText(R.string.improve)).check(matches(isDisplayed()));
        onView(withText(R.string.improve_message)).check(matches(isDisplayed()));
        onView(withText(R.string.report)).check(matches(isDisplayed()));
        onView(withText(R.string.cancel)).check(matches(isDisplayed()));
    }

    @Test
    public void onCreateDialog_Cancel() {
        onView(withText(R.string.cancel)).perform(click());
        onView(withText(R.string.improve)).check(doesNotExist());
        onView(withText(R.string.improve_message)).check(doesNotExist());
        onView(withText(R.string.report)).check(doesNotExist());
        onView(withText(R.string.cancel)).check(doesNotExist());
    }

    @Test
    public void onCreateDialog_ReportEmpty() {
        String errorText = InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.required);
        closeSoftKeyboard();
        onView(withText(R.string.report)).perform(click());
        onView(withId(android.R.id.edit)).check(matches(hasErrorText(errorText)));
    }

    @Test
    public void onCreateDialog_ReportOk() {
        onView(withId(android.R.id.edit)).perform(typeText(typedText));
        closeSoftKeyboard();
        onView(withText(R.string.report)).perform(click());
        onView(withText(R.string.improve)).check(doesNotExist());
        onView(withText(R.string.improve_message)).check(doesNotExist());
        onView(withText(R.string.report)).check(doesNotExist());
        onView(withText(R.string.cancel)).check(doesNotExist());
        assertEquals(1, improvementCount);
    }

}