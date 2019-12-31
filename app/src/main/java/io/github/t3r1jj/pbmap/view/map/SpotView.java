package io.github.t3r1jj.pbmap.view.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import com.qozix.tileview.markers.MarkerLayout;
import com.qozix.tileview.widgets.ZoomPanLayout;

import io.github.t3r1jj.pbmap.BuildConfig;
import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.model.map.Place;

@SuppressLint("ViewConstructor")
public class SpotView extends MarkerLayout implements PlaceView {

    private final TextView textView;
    private final Coordinate center;
    private final float textSize;
    private final boolean isBold;

    public SpotView(Context context, Place place) {
        this(context, place, R.color.spot_text);
    }

    public SpotView(Context context, Place place, @ColorRes int color) {
        super(context);
        isBold = color == R.color.space_text;
        textView = new TextView(context);
        textView.setText(place.getName(context));
        Resources resources = context.getResources();
        textView.setTextColor(ContextCompat.getColor(context, color));
        if (isUnicodeText()) {
            textSize = resources.getDimension(R.dimen.spot_unicode_text);
        } else {
            textSize = resources.getDimension(R.dimen.spot_text);
        }
        textView.setGravity(Gravity.CENTER);
        setTypeFace(context);
        center = place.getCenter();
    }

    private boolean isUnicodeText() {
        CharSequence text = textView.getText();
        int maxLength = Math.min(2, text.length());
        for (int i = 0; i < maxLength; i++) {
            if (Character.isLetterOrDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private void setTypeFace(Context context) {
        final AssetManager assetManager = context.getAssets();
        textView.setPaintFlags(textView.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        if (isBold || isUnicodeText()) {
            final Typeface typeface = Typeface.createFromAsset(assetManager, BuildConfig.FONT_BOLD_PATH);
            textView.setTypeface(typeface);
        } else {
            final Typeface typeface = Typeface.createFromAsset(assetManager, BuildConfig.FONT_PATH);
            textView.setTypeface(typeface);
        }
    }

    @Override
    public void addToMap(final MapView pbMapView) {
        // Double post delays getScale() enough for map to update
        pbMapView.post(() -> pbMapView.post(() -> setTextSize(pbMapView.getScale())));
        pbMapView.addMarker(textView, center.lng, center.lat, -0.5f, -.5f);
        pbMapView.addZoomPanListener(new ZoomListener(pbMapView));
    }

    private void setTextSize(float scale) {
        textView.setTextSize(textSize * scale);
    }

    private class ZoomListener implements ZoomPanLayout.ZoomPanListener {
        private final MapView pbMapView;

        private ZoomListener(MapView pbMapView) {
            this.pbMapView = pbMapView;
        }

        @Override
        public void onPanBegin(int x, int y, Origination origin) {
            // ignore, process zoom only
        }

        @Override
        public void onPanUpdate(int x, int y, Origination origin) {
            // ignore, process zoom only
        }

        @Override
        public void onPanEnd(int x, int y, Origination origin) {
            // ignore, process zoom only
        }

        @Override
        public void onZoomBegin(float scale, Origination origin) {
            onZoomUpdate(scale, origin);
        }

        @Override
        public void onZoomUpdate(float scale, Origination origin) {
            setTextSize(pbMapView.getScale());
        }

        @Override
        public void onZoomEnd(float scale, Origination origin) {
            onZoomUpdate(scale, origin);
        }
    }
}
