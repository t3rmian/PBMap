package io.github.t3r1jj.pbmap.test;

import android.graphics.Bitmap;
import android.util.Log;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.IOException;
import java.util.HashSet;

import androidx.test.runner.screenshot.ScreenCapture;
import androidx.test.runner.screenshot.ScreenCaptureProcessor;
import androidx.test.runner.screenshot.Screenshot;

public class ScreenshotOnTestFailedRule extends TestWatcher {
    private static final String TAG = ScreenshotOnTestFailedRule.class.getSimpleName();

    @Override
    protected void failed(Throwable e, Description description) {
        super.failed(e, description);
        takeScreenshot(description);
    }

    private void takeScreenshot(Description description) {
        Log.i(TAG, "Taking a screenshot of failed test");

        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        String filename = description.getTestClass().getSimpleName() + "-" + description.getMethodName() + "." + format;
        ScreenCapture capture = Screenshot.capture();
        capture.setName(filename);
        capture.setFormat(format);

        HashSet<ScreenCaptureProcessor> processors = new HashSet<>();
        UploadScreenCaptureProcessor captureProcessor = new UploadScreenCaptureProcessor();
        processors.add(captureProcessor);

        try {
            Log.i(TAG, "Processing the screenshot");
            capture.process(processors);
        } catch (IOException e) {
            Log.e(TAG, "Failed to process the screenshot", e);
        }
    }
}