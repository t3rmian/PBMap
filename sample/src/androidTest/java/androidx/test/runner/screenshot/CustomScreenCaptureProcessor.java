package androidx.test.runner.screenshot;

import java.io.File;

import androidx.test.platform.app.InstrumentationRegistry;

import static android.os.Environment.DIRECTORY_PICTURES;

public class CustomScreenCaptureProcessor extends BasicScreenCaptureProcessor {
    public CustomScreenCaptureProcessor() {
        super(
                new File(
                        InstrumentationRegistry.getInstrumentation().getTargetContext().getExternalFilesDir(DIRECTORY_PICTURES),
                        "test_screenshots"
                )
        );
    }
}
