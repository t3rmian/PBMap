package io.github.t3r1jj.pbmap.model.map.route;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import io.github.t3r1jj.pbmap.model.map.Coordinate;

@Root(name = "route", strict = false)
public class RouteGraph {
    @ElementList
    private List<Edge> edges;
    private DijkstraAlgorithm algorithm;
    private List<Coordinate> vertexes;
    private String path;

    public List<Coordinate> getRoute(Coordinate source, Coordinate destination) {
        if (source == null || destination == null) {
            return Collections.EMPTY_LIST;
        }
        if (algorithm == null) {
            vertexes = getVertexes();
            algorithm = new DijkstraAlgorithm(vertexes, edges);
        }
        Coordinate destinationVertex = findClosest(destination);
        algorithm.setTarget(destinationVertex);
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
            double distance = target.flatDistance(coordinate);
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
