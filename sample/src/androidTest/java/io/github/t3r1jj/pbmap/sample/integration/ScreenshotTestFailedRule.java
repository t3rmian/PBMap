package io.github.t3r1jj.pbmap.sample.integration;

import android.graphics.Bitmap;
import androidx.test.runner.screenshot.CustomScreenCaptureProcessor;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.IOException;
import java.util.HashSet;

import androidx.test.runner.screenshot.ScreenCapture;
import androidx.test.runner.screenshot.ScreenCaptureProcessor;
import androidx.test.runner.screenshot.Screenshot;

public class ScreenshotTestFailedRule extends TestWatcher {

    @Override
    protected void failed(Throwable e, Description description) {
        super.failed(e, description);
        takeScreenshot(description);
    }

    private void takeScreenshot(Description description) {
        String filename = description.getTestClass().getSimpleName() + "-" + description.getMethodName();

        ScreenCapture capture = Screenshot.capture();
        capture.setName(filename);
        capture.setFormat(Bitmap.CompressFormat.JPEG);

        HashSet<ScreenCaptureProcessor> processors = new HashSet<>();
        processors.add(new CustomScreenCaptureProcessor());

        try {
            capture.process(processors);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}