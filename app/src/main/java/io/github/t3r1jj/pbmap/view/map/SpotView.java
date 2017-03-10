package io.github.t3r1jj.pbmap.view.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.TextView;

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

    public SpotView(Context context, Place place) {
        this(context, place, context.getResources().getColor(R.color.spot_text));
    }

    public SpotView(Context context, Place place, int color) {
        super(context);
        textView = new TextView(context);
        textView.setText(place.getName(context));
        Resources resources = context.getResources();
        textView.setTextColor(color);
        textSize = resources.getDimension(R.dimen.spot_text);
        setTypeFace(context);
        center = place.getCenter();
    }

    private void setTypeFace(Context context) {
        final AssetManager assetManager = context.getAssets();
        final Typeface typeface = Typeface.createFromAsset(assetManager, BuildConfig.FONT_PATH);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        textView.setTypeface(typeface);
    }

    @Override
    public void addToMap(final MapView pbMapView) {
        pbMapView.post(new Runnable() {
            @Override
            public void run() {
                pbMapView.post(new Runnable() { // Double post delays getScale() enough for map to update
                    @Override
                    public void run() {
                        setTextSize(pbMapView.getScale());
                    }
                });
            }
        });
//        pbMapView.addMarker(textView, center.lng, center.lat, -0.5f, -1.0f);
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
        }

        @Override
        public void onPanUpdate(int x, int y, Origination origin) {
        }

        @Override
        public void onPanEnd(int x, int y, Origination origin) {
        }

        @Override
        public void onZoomBegin(float scale, Origination origin) {
        }

        @Override
        public void onZoomUpdate(float scale, Origination origin) {
            setTextSize(pbMapView.getScale());
        }

        @Override
        public void onZoomEnd(float scale, Origination origin) {
        }
    }
}
