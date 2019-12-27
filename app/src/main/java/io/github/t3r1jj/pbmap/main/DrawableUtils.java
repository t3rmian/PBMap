package io.github.t3r1jj.pbmap.main;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class DrawableUtils {
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(Math.max(1, drawable.getIntrinsicWidth()),
                Math.max(1, drawable.getIntrinsicHeight()), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    static Drawable rotateDrawable(Resources resources, Drawable drawable, float angle) {
        Bitmap originalBitmap = drawableToBitmap(drawable);
        Bitmap rotatedBitmap = Bitmap.createBitmap(originalBitmap.getHeight(), originalBitmap.getWidth(), Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(rotatedBitmap);
        int pivot = originalBitmap.getHeight() / 2;
        tempCanvas.rotate(angle, pivot, pivot);
        tempCanvas.drawBitmap(originalBitmap, 0, 0, null);
        return new BitmapDrawable(resources, rotatedBitmap);
    }
}
