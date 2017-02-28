package io.github.t3r1jj.pbmap.model.map;

import org.simpleframework.xml.Attribute;

public class Coordinate {
    /**
     * x coordinate [deg]
     */
    @Attribute
    public double lng;
    /**
     * y coordinate [deg]
     */
    @Attribute
    public double lat;
    /**
     * meters [m] above mean sea level
     */
    @Attribute(required = false)
    public double alt;

    public Coordinate() {
    }

    public Coordinate(double lng, double lat, double alt) {
        this.lng = lng;
        this.lat = lat;
        this.alt = alt;
    }
}
