package io.github.t3r1jj.pbmap.model;


import android.content.Context;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;

import java.util.List;

import io.github.t3r1jj.pbmap.view.PBMapView;

public class PBMap extends Place {

    @Attribute(required = false)
    String url;
    //TODO: add tilesPath attributes
    @ElementListUnion({
            @ElementList(entry = "space", type = Space.class, required = false, inline = true),
            @ElementList(entry = "spot", type = Spot.class, required = false, inline = true)
    })
    List<Place> places;
    @Element(required = false)
    String description;

    @Override
    public String toString() {
        return "...place=" + places.toString();
    }

    @Override
    public PBMapView createView(Context context) {
        PBMapView pbMapView = new PBMapView(context);
        pbMapView.setSize(8 * 256, 8 * 256);  // the original size of the untiled image
        pbMapView.addDetailLevel(1f, "tiles/1/1000/1000.png", 256, 256);
        pbMapView.addDetailLevel(.5f, "tiles/1/500/500.png", 256, 256);
        for (Place place : places) {
            pbMapView.addPlaceView(place.createView(context));
        }
        return pbMapView;
    }

    public List<Place> getPlaces() {
        return places;
    }
}
