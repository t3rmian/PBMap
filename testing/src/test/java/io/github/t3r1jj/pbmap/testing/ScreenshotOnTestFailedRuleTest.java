package io.github.t3r1jj.pbmap.testing;

import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import androidx.test.runner.screenshot.ScreenCapture;
import androidx.test.runner.screenshot.ScreenCaptureFactory;
import androidx.test.runner.screenshot.Screenshot;

import static org.junit.runner.Description.createSuiteDescription;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {Log.class, Screenshot.class})
public class ScreenshotOnTestFailedRuleTest {
    @Test(expected = MockedOutcome.class)
    public void testScreenshotOnTestFailedRule_takingScreenshot() {
        PowerMockito.mockStatic(Log.class);
        ScreenshotOnTestFailedRule rule = new ScreenshotOnTestFailedRule();
        when(Log.i(eq(ScreenshotOnTestFailedRule.class.getSimpleName()), contains("Taking a screenshot")))
                .thenThrow(MockedOutcome.class);
        rule.failed(new RuntimeException(), createSuiteDescription(getClass()));
    }

    @Test(expected = MockedOutcome.class)
    public void testScreenshotOnTestFailedRule_processingScreenshot() {
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Screenshot.class);
        when(Screenshot.capture()).thenReturn(ScreenCaptureFactory.createScreenCapture(null));
        when(Log.i(eq(ScreenshotOnTestFailedRule.class.getSimpleName()), contains("Processing the screenshot")))
                .thenThrow(MockedOutcome.class);
        ScreenshotOnTestFailedRule rule = new ScreenshotOnTestFailedRule();
        rule.failed(new RuntimeException(), createSuiteDescription(getClass()));
    }

    @Test(expected = MockedOutcome.class)
    public void testScreenshotOnTestFailedRule_failedToProcessScreenshot() throws IOException {
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Screenshot.class);
        ScreenCapture screenCapture = spy(ScreenCaptureFactory.createScreenCapture(null));
        when(Screenshot.capture()).thenReturn(screenCapture);
        IOException ioException = new IOException();
        doThrow(ioException).when(screenCapture).process(any());
        when(Log.e(eq(ScreenshotOnTestFailedRule.class.getSimpleName()),
                contains("Failed to process the screenshot"),
                eq(ioException))
        ).thenThrow(MockedOutcome.class);
        ScreenshotOnTestFailedRule rule = new ScreenshotOnTestFailedRule();
        rule.failed(new RuntimeException(), createSuiteDescription(getClass()));
    }

}