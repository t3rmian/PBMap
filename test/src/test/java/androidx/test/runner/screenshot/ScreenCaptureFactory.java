package androidx.test.runner.screenshot;

import android.graphics.Bitmap;

public class ScreenCaptureFactory {
    public static ScreenCapture createScreenCapture(Bitmap bitmap) {
        return new ScreenCapture(bitmap);
    }
}
