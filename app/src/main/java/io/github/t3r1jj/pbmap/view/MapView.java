package io.github.t3r1jj.pbmap.view;

import android.content.Context;

import com.qozix.tileview.TileView;

import java.util.HashMap;
import java.util.Map;

import io.github.t3r1jj.pbmap.Controller;
import io.github.t3r1jj.pbmap.model.PBMap;
import io.github.t3r1jj.pbmap.model.Space;

public class MapView extends TileView implements PlaceView {
    private static Map<String, MapViewPosition> positionsCache = new HashMap<>();
    private Controller controller;
    private final PBMap map;

    public MapView(Context context, PBMap map) {
        super(context);
        this.map = map;
    }

    public void addPlaceView(PlaceView place) {
        place.addToMap(this);
    }

    @Override
    public void addToMap(MapView pbMapView) {
        positionsCache.put(map.getName(), new MapViewPosition(getCenterX(), getCenterY(), getScale()));
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
