package io.github.t3r1jj.pbmap.model.map.route;

import org.simpleframework.xml.ElementList;

import java.util.LinkedList;
import java.util.List;

import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.view.MapView;
import io.github.t3r1jj.pbmap.view.Route;

public class Graph {
    @ElementList
    private List<Edge> edges;
    private DijkstraAlgorithm algorithm;
    private List<Coordinate> vertexes;

    public Route createView(MapView mapView) {
        return new Route(mapView, this);
    }

    public List<Edge> getPaths() {
        return edges;
    }

    public List<Coordinate> getRoute(Coordinate source, Coordinate destination) {
        if (algorithm == null) {
            vertexes = getVertexes();
            algorithm = new DijkstraAlgorithm(vertexes, edges);
        }
        Coordinate destinationVertex = findClosest(destination);
        algorithm.setTarget(destination);
        Coordinate sourceVertex = findClosest(source);
        algorithm.setSource(sourceVertex);
        algorithm.execute();
        LinkedList<Coordinate> shortestPath = algorithm.getShortestPath();
        if (!source.equals(sourceVertex)) {
            shortestPath.addFirst(source);
        }
        if (!destination.equals(destinationVertex)) {
            shortestPath.addLast(destination);
        }
        return shortestPath;

    }

    private Coordinate findClosest(Coordinate target) {
        if (vertexes.contains(target)) {
            return target;
        }
        Coordinate closest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Coordinate coordinate : vertexes) {
            double distance = target.distance(coordinate);
            if (minDistance > distance) {
                minDistance = distance;
                closest = coordinate;
            }
        }
        return closest;
    }

    private List<Coordinate> getVertexes() {
        List<Coordinate> coordinates = new LinkedList<>();
        for (Edge edge : edges) {
            Coordinate start = edge.getStart();
            if (!coordinates.contains(start)) {
                coordinates.add(start);
            }
            Coordinate end = edge.getEnd();
            if (!coordinates.contains(end)) {
                coordinates.add(end);
            }
        }
        return coordinates;
    }

}
