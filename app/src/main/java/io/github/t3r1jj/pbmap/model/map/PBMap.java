package io.github.t3r1jj.pbmap.model.map;


import android.content.Context;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;

import java.util.List;

import io.github.t3r1jj.pbmap.model.map.route.Graph;
import io.github.t3r1jj.pbmap.view.MapView;

public class PBMap extends Space {

    @Attribute
    private int width;
    @Attribute
    private int height;
    @Attribute(name = "is_primary", required = false)
    private boolean primary;
    @Attribute(name = "previous_map_path", required = false)
    private String previousMapPath;
    @ElementListUnion({
            @ElementList(entry = "space", type = Space.class, required = false, inline = true),
            @ElementList(entry = "spot", type = Spot.class, required = false, inline = true)
    })
    private List<Place> places;
    @ElementList(name = "tiles_configs")
    private List<TilesConfig> tilesConfigs;
    @Element(required = false)
    private Graph route;

    @Override
    public String toString() {
        return "...place=" + places.toString();
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

    public boolean isPrimary() {
        return primary;
    }

    public String getPreviousMapPath() {
        return previousMapPath;
    }

    public Graph getGraph() {
        return route;
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
