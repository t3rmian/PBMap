package io.github.t3r1jj.pbmap.model;

import android.content.Context;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;

import io.github.t3r1jj.pbmap.view.PBMapView;
import io.github.t3r1jj.pbmap.view.PlaceView;

public abstract class Place {
    @Attribute
    protected String name;
    @ElementArray
    protected Coordinate[] coordinates;
    @Element(required = false)
    protected String logoPath;

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

    public Coordinate getCenter() {
        Coordinate center = new Coordinate();
        for (Coordinate coordinate : coordinates) {
            center.lng += coordinate.lng;
            center.lat += coordinate.lat;
            center.mamsl += coordinate.mamsl;
        }
        center.lng /= coordinates.length;
        center.lat /= coordinates.length;
        center.mamsl /= coordinates.length;
        return center;
    }

    abstract public PlaceView createView(Context context);

}
