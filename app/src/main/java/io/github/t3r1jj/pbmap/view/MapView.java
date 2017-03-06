package io.github.t3r1jj.pbmap.view;

import android.content.Context;
import android.view.MotionEvent;

import com.qozix.tileview.TileView;

import java.util.HashMap;
import java.util.Map;

import io.github.t3r1jj.pbmap.main.Controller;
import io.github.t3r1jj.pbmap.model.map.PBMap;
import io.github.t3r1jj.pbmap.model.map.Space;

public class MapView extends TileView implements PlaceView {
    private static Map<String, MapViewPosition> positionsCache = new HashMap<>();
    private final PBMap map;
    private Controller controller;

    public MapView(Context context, PBMap map) {
        super(context);
        this.map = map;
        PBMap.BoundingBox boundingBox = map.getBoundingBox();
        defineBounds(boundingBox.getMinLng(), boundingBox.getMaxLat(), boundingBox.getMaxLng(), boundingBox.getMinLat());
    }

    public void addPlaceView(PlaceView place) {
        place.addToMap(this);
    }

    @Override
    public void addToMap(MapView pbMapView) {
        positionsCache.put(map.getId(), new MapViewPosition(getCenterX(), getCenterY(), getScale()));
    }

    public void initializeZoom() {
        if (!positionsCache.containsKey(map.getId())) {
            post(new Runnable() {
                @Override
                public void run() {
                    setMinimumScaleMode(MinimumScaleMode.FIT);
                    setScale(0);
                    positionsCache.put(map.getId(), new MapViewPosition(getCenterX(), getCenterY(), getScale()));
                }
            });
        }
    }

    protected int getCenterX() {
        return (int) (getWidth() / 2f + getScrollX() + 0.5f);
    }

    protected int getCenterY() {
        return (int) (getHeight() / 2f + getScrollY() + 0.5f);
    }

    public void loadPreviousPosition() {
        final MapViewPosition previousPosition = positionsCache.get(map.getId());
        if (previousPosition != null) {
            post(new Runnable() {
                @Override
                public void run() {
                    setMinimumScaleMode(MinimumScaleMode.FIT);
                    slideToAndCenterWithScale(previousPosition.centerX, previousPosition.centerY, previousPosition.zoom);
                }
            });
        }
    }

    public void fireNavigationPerformed(Space space) {
        controller.onNavigationPerformed(space);
    }

    public void setController(Controller controller) {
        this.controller = controller;
        controller.loadLogo(map);
        controller.loadTitle(map);
    }

    @Override
    public void onLongPress(MotionEvent event) {
        super.onLongPress(event);
        controller.onLongPress(event);
    }

    private class MapViewPosition {
        int centerX;
        int centerY;
        float zoom;

        MapViewPosition(int centerX, int centerY, float zoom) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.zoom = zoom;
        }

        @Override
        public String toString() {
            return "MapViewPosition{" +
                    "centerX=" + centerX +
                    ", centerY=" + centerY +
                    ", zoom=" + zoom +
                    '}';
        }
    }
}
