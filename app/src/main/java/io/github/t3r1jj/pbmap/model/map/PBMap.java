package io.github.t3r1jj.pbmap.model.map;


import android.content.Context;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;

import java.util.List;

import io.github.t3r1jj.pbmap.view.MapView;

public class PBMap extends Space {

    @Attribute
    private int width;
    @Attribute
    private int height;
    @Attribute(name = "route_path", required = false)
    private String routePath;
    @Attribute(name = "previous_map_path", required = false)
    private String previousMapPath;
    @ElementListUnion({
            @ElementList(entry = "space", type = Space.class, required = false, inline = true),
            @ElementList(entry = "spot", type = Spot.class, required = false, inline = true)
    })
    private List<Place> places;
    @ElementList(name = "tiles_configs")
    private List<TilesConfig> tilesConfigs;


    @Override
    public String toString() {
        return "...placeId=" + places.toString();
    }

    @Override
    public MapView createView(Context context) {
        MapView pbMapView = new MapView(context, this);
        pbMapView.setSize(width, height);
        for (TilesConfig tilesConfig : tilesConfigs) {
            pbMapView.addDetailLevel(tilesConfig.zoom, tilesConfig.path, tilesConfig.width, tilesConfig.height);
        }
        for (Place place : places) {
            pbMapView.addPlaceView(place.createView(context));
        }
        pbMapView.initializeZoom();
        return pbMapView;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public String getPreviousMapPath() {
        return previousMapPath;
    }

    public String getGraphPath() {
        return routePath;
    }

    /**
     *
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

    /**
     * Represents max / min lng / lat
     */
    public static class BoundingBox {
        private double maxLng = Double.NEGATIVE_INFINITY;
        private double minLng = Double.POSITIVE_INFINITY;
        private double maxLat = Double.NEGATIVE_INFINITY;
        private double minLat = Double.POSITIVE_INFINITY;

        private void add(Coordinate coordinate) {
            maxLng = Math.max(coordinate.lng, maxLng);
            minLng = Math.min(coordinate.lng, minLng);
            maxLat = Math.max(coordinate.lat, maxLat);
            minLat = Math.min(coordinate.lat, minLat);
        }

        /**
         * @return @see {@link Coordinate#lng}
         */
        public double getMaxLng() {
            return maxLng;
        }

        /**
         * @return @see {@link Coordinate#lng}
         */
        public double getMinLng() {
            return minLng;
        }


        /**
         * @return @see {@link Coordinate#lat}
         */
        public double getMaxLat() {
            return maxLat;
        }

        /**
         * @return @see {@link Coordinate#lat}
         */
        public double getMinLat() {
            return minLat;
        }

        @Override
        public String toString() {
            return "(" + minLat + "," + minLng + "," + maxLat + "," + maxLng + ")";
        }
    }
}
