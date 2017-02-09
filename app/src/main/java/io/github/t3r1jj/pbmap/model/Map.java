package io.github.t3r1jj.pbmap.model;


import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;

import java.util.List;

public class Map extends Place {

    @ElementListUnion({
            @ElementList(entry = "space", type = Space.class, required = false, inline = true),
            @ElementList(entry = "spot", type = Spot.class, required = false, inline = true)
    })
    List<Place> places;

    @Override
    public String toString() {
        return "...place=" + places.toString();
    }

    public List<Place> getPlaces() {
        return places;
    }
}
