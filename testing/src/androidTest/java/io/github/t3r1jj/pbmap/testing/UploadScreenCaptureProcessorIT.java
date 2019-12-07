package io.github.t3r1jj.pbmap.testing;

import androidx.test.filters.SmallTest;
import androidx.test.runner.screenshot.ScreenCapture;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static android.graphics.Bitmap.CompressFormat.JPEG;
import static com.google.common.truth.Truth.assertThat;

@RunWith(RetryRunner.class)
public class UploadScreenCaptureProcessorIT {

    private final DefaultScreenCaptureFactory factory = new DefaultScreenCaptureFactory();

    @Test
    @SmallTest
    public void processProperImage() throws IOException {
        UploadScreenCaptureProcessor processor = new UploadScreenCaptureProcessor();
        ScreenCapture screenCapture = factory.createScreenCapture();
        screenCapture.setFormat(JPEG);
        screenCapture.setName("test-image.jpg");
        String result = processor.process(screenCapture);
        assertThat(result).contains("http");
    }

    @Test
    @SmallTest
    public void processInvalidImage() throws IOException {
        UploadScreenCaptureProcessor processor = new UploadScreenCaptureProcessor();
        ScreenCapture screenCapture = factory.createScreenCapture();
        String result = processor.process(screenCapture);
        assertThat(result).contains("400");
    }

}