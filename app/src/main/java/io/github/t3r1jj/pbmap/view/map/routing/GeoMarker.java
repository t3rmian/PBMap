package io.github.t3r1jj.pbmap.view.map.routing;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.qozix.tileview.geom.CoordinateTranslater;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.view.map.MapView;

public class GeoMarker extends ImageView implements RemovableView {

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

    public void setAnchor(PointF anchor) {
        this.anchor = anchor;
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
            pbMapView.post(new Runnable() {
                @Override
                public void run() {
                    pbMapView.removeMarker(GeoMarker.this);
                    if (coordinate != null) {
                        pbMapView.addMarker(GeoMarker.this, coordinate.lng, coordinate.lat, anchor.x, anchor.y);
                    }
                }
            });
        }
        listener.onMapPositionChange();
    }

    public void setLevel(int level, Marker marker) {
        if (this.level != level) {
            this.level = level;
            Drawable drawable = getResources().getDrawable(marker.getLevelDrawableId(level));
            setImageDrawable(drawable);
        }
    }

    public void setListener(MapListener listener) {
        this.listener = listener;
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
            if (sameMarkerPressed(mapView, event, lngPx, latPx)) {
                return true;
            }
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
        pbMapView.post(new Runnable() {
            @Override
            public void run() {
                if (coordinate != null) {
                    pbMapView.slideToAndCenterWithScale(coordinate.lng, coordinate.lat, 1f);
                }
                addToMap(pbMapView);
            }
        });

    }

    public enum Marker {
        SOURCE(new int[]{R.drawable.source_down_marker, R.drawable.source_marker, R.drawable.source_up_marker}),
        DESTINATION(new int[]{R.drawable.destination_down_marker, R.drawable.destination_marker, R.drawable.destination_up_marker});

        private final int[] drawables;

        Marker(int[] drawables) {
            this.drawables = drawables;
        }

        private int getLevelDrawableId(int level) {
            return drawables[level + 1];
        }
    }

    public interface MapListener {
        void onMapPositionChange();
    }
}
