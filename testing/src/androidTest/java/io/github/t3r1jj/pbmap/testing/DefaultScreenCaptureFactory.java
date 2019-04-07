package io.github.t3r1jj.pbmap.testing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import androidx.test.runner.screenshot.ScreenCapture;
import androidx.test.runner.screenshot.ScreenCaptureFactory;

class DefaultScreenCaptureFactory extends ScreenCaptureFactory {
    private Bitmap createScreenCaptureBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.GREEN);
        return bitmap;
    }

    ScreenCapture createScreenCapture() {
        return createScreenCapture(createScreenCaptureBitmap());
    }
}
