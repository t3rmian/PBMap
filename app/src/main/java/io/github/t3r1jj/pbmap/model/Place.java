package io.github.t3r1jj.pbmap.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementArray;

public abstract class Place {
    @Attribute
    protected String name;
    @ElementArray
    protected Coordinate[] coordinates;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinate[] getCoordinates() {
        return coordinates;
    }

    @Override
    public String toString() {
        return "Place{" +
                "name='" + name + '\'' +
                ", shape=" + coordinates +
                '}';
    }
}
