package io.github.t3r1jj.pbmap.model;


import android.content.Context;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;

import java.util.List;

import io.github.t3r1jj.pbmap.view.MapView;

public class PBMap extends Place {

    @Attribute
    private int width;
    @Attribute
    private int height;
    @Attribute(required = false)
    private String url;
    @ElementListUnion({
            @ElementList(entry = "space", type = Space.class, required = false, inline = true),
            @ElementList(entry = "spot", type = Spot.class, required = false, inline = true)
    })
    private List<Place> places;
    @ElementArray(name = "tiles_configs")
    private TilesConfig[] tilesConfigs;
    @Element(required = false)
    private String description;

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
}
