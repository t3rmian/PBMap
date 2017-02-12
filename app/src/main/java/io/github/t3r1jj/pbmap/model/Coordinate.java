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
}
