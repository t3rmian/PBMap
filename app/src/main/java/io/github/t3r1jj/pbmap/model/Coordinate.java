package io.github.t3r1jj.pbmap.model;

import org.simpleframework.xml.Attribute;

public class Coordinate {
    /**
     * x coordinate
     */
    @Attribute
    public double lng;
    /**
     * y coordinate
     */
    @Attribute
    public double lat;
    /**
     * meters above mean sea level
     */
    @Attribute
    public double mamsl;

    public Coordinate() {
    }

    public Coordinate(double lng, double lat, double mamsl) {
        this.lng = lng;
        this.lat = lat;
        this.mamsl = mamsl;
    }
}
