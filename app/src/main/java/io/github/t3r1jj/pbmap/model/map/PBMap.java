package io.github.t3r1jj.pbmap.model.map;


import android.content.Context;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;

import java.util.Iterator;
import java.util.List;

import io.github.t3r1jj.pbmap.view.map.MapView;

public class PBMap extends Space {

    @Attribute
    private int width;
    @Attribute
    private int height;
    @Attribute(name = "route_path", required = false)
    private String routePath;
    @Attribute(name = "previous_map_path", required = false)
    private String previousMapPath;
    @Attribute(name = "up_map_path", required = false)
    private String upMapPath;
    @Attribute(name = "left_map_path", required = false)
    private String leftMapPath;
    @Attribute(name = "right_map_path", required = false)
    private String rightMapPath;
    @Attribute(name = "down_map_path", required = false)
    private String downMapPath;
    @Attribute(name = "unfinished", required = false)
    private boolean unfinished;
    @ElementListUnion({
            @ElementList(entry = "space", type = Space.class, required = false, inline = true),
            @ElementList(entry = "spot", type = Spot.class, required = false, inline = true)
    })
    private List<Place> places;
    @ElementList(name = "tiles_configs")
    private List<TilesConfig> tilesConfigs;
    @Attribute(name = "rank", required = false)
    private int rank;

    @Override
    public MapView createView(Context context) {
        MapView pbMapView = new MapView(context, this);
        pbMapView.setSize(width, height);
        for (TilesConfig tilesConfig : tilesConfigs) {
            pbMapView.addDetailLevel(tilesConfig.zoom, tilesConfig.path, tilesConfig.width, tilesConfig.height);
        }
        if (places != null) {
            for (Place place : places) {
                pbMapView.addPlaceView(place.createView(context));
            }
        }
        pbMapView.initializeZoom();
        return pbMapView;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public String getGraphPath() {
        return routePath;
    }

    /**
     * @param navigation direction
     * @return path to the map or null if there is no map in the requested direction
     */
    public String getNavigationMapPath(Navigation navigation) {
        if (navigation == Navigation.UP) {
            return upMapPath;
        } else if (navigation == Navigation.DOWN) {
            return downMapPath;
        } else if (navigation == Navigation.LEFT) {
            return leftMapPath;
        } else if (navigation == Navigation.RIGHT) {
            return rightMapPath;
        } else {
            return previousMapPath;
        }
    }

    /**
     * @param coordinate Coordinate compared to this map
     * @return 0 if same altitude, -1 if compared coordinate is below, 1 if above
     */
    public int compareAltitude(Coordinate coordinate) {
        if (coordinate == null) return 0;
        return Math.abs(getCenter().alt - coordinate.alt) < 1d ? 0 : (getCenter().alt - coordinate.alt) > 0d ? -1 : 1;
    }

    /**
     * @return BoundingBox representing max / min lng / lat for coordinates of this route
     */
    public BoundingBox getBoundingBox() {
        BoundingBox box = new BoundingBox();
        for (Coordinate coordinate : coordinates) {
            box.add(coordinate);
        }
        return box;
    }

    public void removeDifferentAltitudePoints(List<Coordinate> route) {
        Coordinate prev = null;
        for (Iterator<Coordinate> routeIterator = route.iterator(); routeIterator.hasNext(); ) {
            Coordinate next = routeIterator.next();
            if (compareAltitude(next) != 0) {
                routeIterator.remove();
                if (prev != null) {
                    prev.setDetachedFromNext(true);
                }
            } else {
                prev = next;
            }
        }
    }

    public boolean isUnfinished() {
        return unfinished;
    }

    public Place findClosest(Coordinate coordinate) {
        Place closest = null;
        double distance = 0;
        for (Place place : places) {
            if (closest == null) {
                closest = place;
                distance = coordinate.distanceTo(place.getCenter());
            } else {
                double comparedDistance = coordinate.distanceTo(place.getCenter());
                if (comparedDistance < distance) {
                    closest = place;
                    distance = comparedDistance;
                }
            }
        }
        return closest;
    }

    public enum Navigation {
        UP, DOWN, LEFT, RIGHT, BACK
    }
}
