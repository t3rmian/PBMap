package androidx.test.runner.screenshot;

import android.os.Environment;

import java.io.File;

import androidx.test.platform.app.InstrumentationRegistry;

public class CustomScreenCaptureProcessor extends BasicScreenCaptureProcessor {
    public CustomScreenCaptureProcessor() {
        super(getSdcardFilePath());
    }

    private static File getSdcardFilePath() {
        String pathName = getPath();
        File path = new File(pathName);
        if (!path.exists()) {
            //noinspection ResultOfMethodCallIgnored
            path.mkdirs();
        }
        return path;
    }

    public static String getPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/test_screenshots/" + InstrumentationRegistry.getInstrumentation().getTargetContext().getPackageName();
    }
}
