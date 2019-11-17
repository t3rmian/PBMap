package io.github.t3r1jj.pbmap.main.drawer;

import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import io.github.t3r1jj.pbmap.about.AboutActivity;
import io.github.t3r1jj.pbmap.testing.RetryRunner;
import io.github.t3r1jj.pbmap.testing.ScreenshotOnTestFailedRule;

@RunWith(RetryRunner.class)
public class MapsDrawerFragmentIncorrectIT {

    private final ActivityTestRule<AboutActivity> activityRule =
            new ActivityTestRule<>(AboutActivity.class, false, true);
    @Rule
    public RuleChain testRule = RuleChain
            .outerRule(activityRule)
            .around(new ScreenshotOnTestFailedRule());

    @Test(expected = ClassCastException.class)
    @SmallTest
    public void onAttach() {
        new MapsDrawerFragment().onAttach(activityRule.getActivity());
    }
}