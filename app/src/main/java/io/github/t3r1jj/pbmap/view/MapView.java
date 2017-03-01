package io.github.t3r1jj.pbmap.view;

import android.content.Context;
import android.util.Log;

import com.qozix.tileview.TileView;
import com.qozix.tileview.paths.CompositePathView;

import java.util.HashMap;
import java.util.Map;

import io.github.t3r1jj.pbmap.main.Controller;
import io.github.t3r1jj.pbmap.model.map.PBMap;
import io.github.t3r1jj.pbmap.model.map.Space;

public class MapView extends TileView implements PlaceView {
    private static Map<String, MapViewPosition> positionsCache = new HashMap<>();
    private Controller controller;
    private final PBMap map;

    public MapView(Context context, PBMap map) {
        super(context);
        this.map = map;
        PBMap.BoundingBox boundingBox = map.getBoundingBox();
        Log.d(getClass().getSimpleName(), boundingBox.toString());
        defineBounds(boundingBox.getMinLng(), boundingBox.getMaxLat(), boundingBox.getMaxLng(), boundingBox.getMinLat());
    }

    public void addPlaceView(PlaceView place) {
        place.addToMap(this);
    }

    @Override
    public void addToMap(MapView pbMapView) {
        positionsCache.put(map.getName(), new MapViewPosition(getCenterX(), getCenterY(), getScale()));
    }

    public void addRoute(Route route) {
        for (CompositePathView.DrawablePath drawablePath : route.getDrawablePaths()) {
            getCompositePathView().addPath(drawablePath);
        }
    }

    public void removeRoute(Route route) {
        if (route == null) {
            return;
        }
        for (CompositePathView.DrawablePath drawablePath : route.getDrawablePaths()) {
            getCompositePathView().removePath(drawablePath);
        }
    }

    public void initializeZoom() {
        if (!positionsCache.containsKey(map.getName())) {
            post(new Runnable() {
                @Override
                public void run() {
                    setMinimumScaleMode(MinimumScaleMode.FIT);
                    setScale(0);
                    positionsCache.put(map.getName(), new MapViewPosition(getCenterX(), getCenterY(), getScale()));
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
        final MapViewPosition previousPosition = positionsCache.get(map.getName());
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
    }

}
