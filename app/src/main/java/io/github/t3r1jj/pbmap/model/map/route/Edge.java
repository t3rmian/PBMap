package io.github.t3r1jj.pbmap.model.map.route;

import org.simpleframework.xml.Element;

import io.github.t3r1jj.pbmap.model.map.Coordinate;

public class Edge {
    @Element
    public Coordinate start;
    @Element
    public Coordinate end;

    public Coordinate getStart() {
        return start;
    }

    public Coordinate getEnd() {
        return end;
    }
}
