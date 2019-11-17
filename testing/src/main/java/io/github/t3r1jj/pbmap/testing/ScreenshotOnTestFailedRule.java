package io.github.t3r1jj.pbmap.testing;

import android.graphics.Bitmap;
import android.util.Log;

import org.junit.runner.Description;

import java.io.IOException;
import java.util.HashSet;

import androidx.test.runner.screenshot.ScreenCapture;
import androidx.test.runner.screenshot.ScreenCaptureProcessor;
import androidx.test.runner.screenshot.Screenshot;

public class ScreenshotOnTestFailedRule extends RetryRule {

    private static final String TAG = ScreenshotOnTestFailedRule.class.getSimpleName();

    public ScreenshotOnTestFailedRule() {
        super(3);
    }

    public ScreenshotOnTestFailedRule(int retryCount) {
        super(retryCount);
    }

    @Override
    protected void failed(Throwable e, Description description) {
        super.failed(e, description);
        takeScreenshot(description);
    }

    private void takeScreenshot(Description description) {
        Log.i(TAG, "Taking a screenshot of failed test");

        String testName = description.getTestClass().getSimpleName() + "-" + description.getMethodName();
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        String filename = testName + "." + format;
        ScreenCapture capture = Screenshot.capture();
        capture.setName(filename);
        capture.setFormat(format);

        HashSet<ScreenCaptureProcessor> processors = new HashSet<>();
        UploadScreenCaptureProcessor captureProcessor = new UploadScreenCaptureProcessor();
        processors.add(captureProcessor);

        Log.i(TAG, String.format("Processing the screenshot (%s)", testName));
        try {
            capture.process(processors);
        } catch (IOException e) {
            Log.e(TAG, String.format("Failed to process the screenshot (%s)", testName), e);
        }
    }
}