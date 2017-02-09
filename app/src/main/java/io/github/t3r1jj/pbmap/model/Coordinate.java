package io.github.t3r1jj.pbmap.model;

import org.simpleframework.xml.Attribute;

public class Coordinate {
    @Attribute
    public double lng;    // x
    @Attribute
    public double lat;    // y
    @Attribute
    public double mamsl;
}
