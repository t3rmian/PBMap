package io.github.t3r1jj.pbmap.testing;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.test.runner.screenshot.ScreenCapture;
import androidx.test.runner.screenshot.ScreenCaptureFactory;
import androidx.test.runner.screenshot.Screenshot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static org.junit.runner.Description.createSuiteDescription;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {Log.class, Screenshot.class})
@PowerMockIgnore("javax.net.ssl.*")
public class ScreenshotOnTestFailedRuleTest {

    private final VerifyAnswer verifyAnswer = new VerifyAnswer();

    @Test
    public void testScreenshotOnTestFailedRule_takingScreenshot() throws IOException {
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Screenshot.class);
        ScreenshotOnTestFailedRule rule = new ScreenshotOnTestFailedRule();
        ScreenCapture screenCapture = spy(ScreenCaptureFactory.createScreenCapture(null));
        doThrow(new IOException("mock")).when(screenCapture).process(any());
        when(Screenshot.capture()).thenReturn(screenCapture);
        doAnswer(a -> new IOException("mock")).when(screenCapture).process();
        doAnswer(a -> new IOException("mock")).when(screenCapture).process(any());
        when(Log.i(eq(ScreenshotOnTestFailedRule.class.getSimpleName()), contains("Taking a screenshot")))
                .then(verifyAnswer);
        rule.failed(new RuntimeException(), createSuiteDescription(getClass()));
        verifyAnswer.assertCalled();
    }

    @Test
    public void testScreenshotOnTestFailedRule_processingScreenshot() {
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Screenshot.class);
        ScreenCapture screenCapture = spy(ScreenCaptureFactory.createScreenCapture(null));
        when(screenCapture.getBitmap()).thenReturn(mock(Bitmap.class));
        when(Screenshot.capture()).thenReturn(screenCapture);
        when(Log.i(eq(ScreenshotOnTestFailedRule.class.getSimpleName()), contains("Processing the screenshot")))
                .then(verifyAnswer);
        ScreenshotOnTestFailedRule rule = new ScreenshotOnTestFailedRule();
        rule.failed(new RuntimeException(), createSuiteDescription(getClass()));
        verifyAnswer.assertCalled();
    }

    @Test
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
        ).then(verifyAnswer);
        ScreenshotOnTestFailedRule rule = new ScreenshotOnTestFailedRule();
        rule.failed(new RuntimeException(), createSuiteDescription(getClass()));
        verifyAnswer.assertCalled();
    }

}