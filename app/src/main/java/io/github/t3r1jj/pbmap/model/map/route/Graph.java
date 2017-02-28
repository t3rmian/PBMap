package io.github.t3r1jj.pbmap.model.map.route;

import org.simpleframework.xml.ElementList;

import java.util.List;

import io.github.t3r1jj.pbmap.view.MapView;
import io.github.t3r1jj.pbmap.view.Route;

public class Graph {
    @ElementList
    private List<Edge> edges;

    public Route createView(MapView mapView) {
        return new Route(mapView, this);
    }

    public List<Edge> getRoute() {
        return edges;
    }
}
