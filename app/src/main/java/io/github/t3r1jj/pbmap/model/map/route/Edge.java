package io.github.t3r1jj.pbmap.model.map.route;

import org.simpleframework.xml.Element;

import io.github.t3r1jj.pbmap.model.map.Coordinate;

public class Edge {
    @Element
    private Coordinate start;
    @Element
    private Coordinate end;

    @SuppressWarnings("unused")
    private Edge() {
    }

    Edge(Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
    }

    public Coordinate getStart() {
        return start;
    }

    public Coordinate getEnd() {
        return end;
    }

    public boolean sameAltitude() {
        return getStart().sameAltitude(getEnd());
    }
}
