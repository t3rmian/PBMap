package androidx.test.runner.screenshot;

import android.graphics.Bitmap;

public class ScreenCaptureFactory {
    protected static ScreenCapture createScreenCapture(Bitmap bitmap) {
        return new ScreenCapture(bitmap);
    }
}
