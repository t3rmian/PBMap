package io.github.t3r1jj.pbmap.view.map.routing;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatImageView;

import com.qozix.tileview.geom.CoordinateTranslater;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.view.map.MapView;

public class GeoMarker extends AppCompatImageView implements RemovableView {

    private Coordinate coordinate;
    private PointF anchor;
    private MapListener listener;
    private int level = Integer.MIN_VALUE;

    public GeoMarker(Context context) {
        this(context, new PointF(-0.5f, -0.5f));
    }

    public GeoMarker(Context context, PointF anchor) {
        super(context);
        this.anchor = anchor;
    }

    public static GeoMarker recreate(@Nullable GeoMarker geoMarker, Context context, MapListener mapListener) {
        if (geoMarker != null) {
            GeoMarker newGeoMarker = new GeoMarker(context, geoMarker.anchor);
            newGeoMarker.setCoordinate(geoMarker.coordinate);
            newGeoMarker.listener = mapListener;
            return newGeoMarker;
        } else {
            GeoMarker newGeoMarker = new GeoMarker(context);
            newGeoMarker.listener = mapListener;
            return newGeoMarker;
        }
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public void addToMap(final MapView pbMapView) {
        if (coordinate != null && anchor != null) {
            pbMapView.post(() -> {
                pbMapView.removeMarker(GeoMarker.this);
                if (coordinate != null) {
                    pbMapView.addMarker(GeoMarker.this, coordinate.lng, coordinate.lat, anchor.x, anchor.y);
                }
            });
        }
        listener.onMapPositionChange();
    }

    public void setLevel(int level, Marker marker) {
        if (this.level != level) {
            this.level = level;
            Drawable drawable = getResources().getDrawable(marker.getLevelDrawableId(level));
            setContentDescription(getResources().getString(marker.getContentDescriptionResId()));
            setImageDrawable(drawable);
        }
    }

    public void addToMap(MapView mapView, MotionEvent event, double alt) {
        CoordinateTranslater coordinateTranslater = mapView.getCoordinateTranslater();
        double lng = coordinateTranslater.translateAndScaleAbsoluteToRelativeX(mapView.getScrollX() + event.getX() - mapView.getOffsetX(), mapView.getScale());
        double lat = coordinateTranslater.translateAndScaleAbsoluteToRelativeY(mapView.getScrollY() + event.getY() - mapView.getOffsetY(), mapView.getScale());
        coordinate = new Coordinate(lat, lng, alt);
        addToMap(mapView);
    }

    public boolean isAtPosition(MapView mapView, MotionEvent event, double alt) {
        CoordinateTranslater coordinateTranslater = mapView.getCoordinateTranslater();
        double lng = coordinateTranslater.translateAndScaleAbsoluteToRelativeX(mapView.getScrollX() + event.getX() - mapView.getOffsetX(), mapView.getScale());
        double lat = coordinateTranslater.translateAndScaleAbsoluteToRelativeY(mapView.getScrollY() + event.getY() - mapView.getOffsetY(), mapView.getScale());

        if (!coordinateTranslater.contains(lng, lat)) {
            return true;
        }
        if (coordinate != null && Math.abs(coordinate.alt - alt) < 1d) {
            double lngPx = coordinateTranslater.translateAndScaleX(coordinate.lng, mapView.getScale()) - mapView.getScrollX() + mapView.getOffsetX();
            double latPx = coordinateTranslater.translateAndScaleY(coordinate.lat, mapView.getScale()) - mapView.getScrollY() + mapView.getOffsetY();
            return sameMarkerPressed(mapView, event, lngPx, latPx);
        }
        return false;
    }

    private boolean sameMarkerPressed(MapView mapView, MotionEvent event, double lngPx, double latPx) {
        float xRange = mapView.getContext().getResources().getDimension(R.dimen.marker_size_bordered);
        float yRange = mapView.getContext().getResources().getDimension(R.dimen.marker_size_bordered);
        return Math.abs(event.getX() - lngPx) < xRange && Math.abs(event.getY() - latPx) < yRange;
    }

    @Override
    public void removeFromMap(MapView pbMapView) {
        pbMapView.removeMarker(this);
        listener.onMapPositionChange();
    }

    public void pinpointOnMap(final MapView pbMapView) {
        pbMapView.post(() -> {
            if (coordinate != null) {
                pbMapView.slideToAndCenterWithScale(coordinate.lng, coordinate.lat, 1f);
            }
            addToMap(pbMapView);
        });

    }

    public enum Marker {
        SOURCE(new int[]{R.drawable.source_down_marker, R.drawable.source_marker, R.drawable.source_up_marker}, R.string.source),
        DESTINATION(new int[]{R.drawable.destination_down_marker, R.drawable.destination_marker, R.drawable.destination_up_marker}, R.string.destination);

        private final int[] drawables;
        private final int contentDescriptionResId;

        Marker(int[] drawables, @StringRes int contentDescriptionResId) {
            this.drawables = drawables;
            this.contentDescriptionResId = contentDescriptionResId;
        }

        private int getLevelDrawableId(int level) {
            return drawables[level + 1];
        }

        @StringRes
        public int getContentDescriptionResId() {
            return contentDescriptionResId;
        }
    }

    public interface MapListener {
        void onMapPositionChange();
    }
}
