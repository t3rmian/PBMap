package io.github.t3r1jj.pbmap.sample.integration;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class PBMapIntegratorTest {

    private PBMapIntegrator integrator;

    @Rule
    public ActivityTestRule<IntegrationActivity> testRule =
            new ActivityTestRule<>(IntegrationActivity.class, true, true);

    @Before
    public void setUp() {
        IntegrationActivity activity = testRule.getActivity();
        integrator = spy(activity.pbMapIntegrator);
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test(expected = ActivityNotFoundException.class)
    public void foundNothingInstalled() {
        doThrow(new ActivityNotFoundException("Mock activity not found")).when(integrator).startActivity(any(Intent.class));
        integrator.startActivity("query");
    }

    @Test
    public void appNotInstalled_openMarket() {
        doAnswer(__ -> {
            doCallRealMethod().when(integrator).startActivity(any(Intent.class));
            throw new ActivityNotFoundException("Mock activity not found");
        }).when(integrator).startActivity(any(Intent.class));
        integrator.startActivity("query");
        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse("market://details?id=io.github.t3r1jj.pbmap"))
        ));
    }

    @Test
    public void appAndMarketNotInstalled_openGooglePlay() {
        doAnswer(__ -> {
            doAnswer(___ -> {
                doCallRealMethod().when(integrator).startActivity(any(Intent.class));
                throw new ActivityNotFoundException("Mock activity 2 not found");
            }).when(integrator).startActivity(any(Intent.class));
            throw new ActivityNotFoundException("Mock activity 1 not found");
        }).when(integrator).startActivity(any(Intent.class));
        integrator.startActivity("query");
        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse("https://play.google.com/store/apps/details?id=io.github.t3r1jj.pbmap"))
        ));
    }

    @Test(expected = ActivityNotFoundException.class)
    public void foundNothingInstalled_locationVariant() {
        doThrow(new ActivityNotFoundException("Mock activity not found")).when(integrator).startActivity(any(Intent.class));
        integrator.startActivity("query", new Location(""));
    }

    @Test
    public void appNotInstalled_openMarket_locationVariant() {
        doAnswer(__ -> {
            doCallRealMethod().when(integrator).startActivity(any(Intent.class));
            throw new ActivityNotFoundException("Mock activity not found");
        }).when(integrator).startActivity(any(Intent.class));
        integrator.startActivity("query", new Location(""));
        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse("market://details?id=io.github.t3r1jj.pbmap"))
        ));
    }

    @Test
    public void appAndMarketNotInstalled_openGooglePlay_locationVariant() {
        doAnswer(__ -> {
            doAnswer(___ -> {
                doCallRealMethod().when(integrator).startActivity(any(Intent.class));
                throw new ActivityNotFoundException("Mock activity 2 not found");
            }).when(integrator).startActivity(any(Intent.class));
            throw new ActivityNotFoundException("Mock activity 1 not found");
        }).when(integrator).startActivity(any(Intent.class));
        integrator.startActivity("query", new Location(""));
        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse("https://play.google.com/store/apps/details?id=io.github.t3r1jj.pbmap"))
        ));
    }

}