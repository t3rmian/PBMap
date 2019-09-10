package io.github.t3r1jj.pbmap.main;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.fragment.app.FragmentActivity;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.android21buttons.fragmenttestrule.FragmentTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.Info;
import io.github.t3r1jj.pbmap.testing.ScreenshotOnTestFailedRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class InfoSheetDialogFragmentIT {

    private final InfoSheetDialogFragment fragment = new InfoSheetDialogFragment();
    private final FragmentTestRule<?, InfoSheetDialogFragment> outerRule =
            new FragmentTestRule<FragmentActivity, InfoSheetDialogFragment>
                    (FragmentActivity.class, InfoSheetDialogFragment.class, true, true, false) {
                protected InfoSheetDialogFragment createFragment() {
                    return fragment;
                }
            };

    @Rule
    public RuleChain testRule = RuleChain
            .outerRule(outerRule)
            .around(new ScreenshotOnTestFailedRule());
    private Info info;

    public void lazySetUp() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(InfoSheetDialogFragment.INFO_KEY, info);

        fragment.setArguments(bundle);
        fragment.show(outerRule.getActivity().getSupportFragmentManager(), "test");
    }

    @Test
    public void onCreate() {
        info = mock(Info.class);
        lazySetUp();
        onView(withId(R.id.info_title)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.info_address)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.info_description)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.info_logo)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.info_url)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void onCreate_InfoIsDisplayed() {
        info = mock(Info.class);
        when(info.getName(any())).thenReturn("test title");
        when(info.getAddress(any())).thenReturn("test address");
        when(info.getDescription(any())).thenReturn("test description");
        when(info.getUrl()).thenReturn("test url");
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RING);
        shape.setColor(Color.WHITE);
        shape.setStroke(5, Color.BLACK);
        when(info.createLogo(any())).thenReturn(shape);

        lazySetUp();

        onView(withId(R.id.info_title)).check(matches(isDisplayed()));
        onView(withId(R.id.info_title)).check(matches(withText("test title")));
        onView(withId(R.id.info_address)).check(matches(isDisplayed()));
        onView(withId(R.id.info_address)).check(matches(withText("test address")));
        onView(withId(R.id.info_description)).check(matches(isDisplayed()));
        onView(withId(R.id.info_description)).check(matches(withText("test description")));
        onView(withId(R.id.info_logo)).check(matches(isDisplayed()));
        onView(withId(R.id.info_url)).check(matches(isDisplayed()));
        onView(withId(R.id.info_url)).check(matches(withText("test url")));
    }

    @Test
    @LargeTest
    public void onCreateAndRotate_InfoIsDisplayed() {
        info = mock(Info.class);
        when(info.getName(any())).thenReturn("test title");
        when(info.getAddress(any())).thenReturn("test address");
        when(info.getDescription(any())).thenReturn("test description");
        when(info.getUrl()).thenReturn("test url");
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RING);
        shape.setColor(Color.WHITE);
        shape.setStroke(5, Color.BLACK);
        when(info.createLogo(any())).thenReturn(shape);

        lazySetUp();

        outerRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        outerRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        SystemClock.sleep(3000);

        onView(withId(R.id.info_title)).check(matches(isDisplayed()));
        onView(withId(R.id.info_title)).check(matches(withText("test title")));
        onView(withId(R.id.info_address)).check(matches(isDisplayed()));
        onView(withId(R.id.info_address)).check(matches(withText("test address")));
        onView(withId(R.id.info_description)).check(matches(isDisplayed()));
        onView(withId(R.id.info_description)).check(matches(withText("test description")));
        onView(withId(R.id.info_logo)).check(matches(isDisplayed()));
        onView(withId(R.id.info_url)).check(matches(isDisplayed()));
        onView(withId(R.id.info_url)).check(matches(withText("test url")));
    }

}