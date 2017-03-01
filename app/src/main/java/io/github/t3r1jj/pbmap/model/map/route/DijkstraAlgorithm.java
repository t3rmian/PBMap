package io.github.t3r1jj.pbmap.model.map.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import io.github.t3r1jj.pbmap.model.map.Coordinate;
//TODO: Integrate with the rest of the logic
public class DijkstraAlgorithm {

    private final List<Coordinate> vertexes;
    private final List<Edge> edges;

    private Set<Coordinate> Q;
    private HashMap<Coordinate, Double> dist;
    private HashMap<Coordinate, Coordinate> prev;

    private Coordinate source;
    private Coordinate target;

    public DijkstraAlgorithm(List<Coordinate> vertexes, List<Edge> edges) {
        this.vertexes = vertexes;
        this.edges = edges;
    }

    public void setSource(Coordinate source) {
        this.source = source;
    }

    public void setTarget(Coordinate target) {
        this.target = target;
    }

    /**
     * Remember to set source and target coordinates.
     * After this call, the shortest path will be available through {@link #getShortestPath()}
     */
    public void execute() {
        if (source == null || target == null) {
            throw new RuntimeException("Source or target not set");
        }
        Q = new HashSet<>();
        dist = new HashMap<>();
        prev = new LinkedHashMap<>();
        for (Coordinate v : vertexes) {
            dist.put(v, Double.POSITIVE_INFINITY);
            prev.put(v, null);
            Q.add(v);
        }

        dist.put(source, 0d);

        while (!Q.isEmpty()) {
            Coordinate u = vMinDistQ();
            if (u == target) {
                return;
            }
            Q.remove(u);

            for (Coordinate v : getNeighbors(u)) {
                double alt = dist.get(u) + length(u, v);
                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    prev.put(v, u);
                }
            }
        }
    }

    /**
     * @return flat distance between two geographical points (for small map)
     */
    protected Double length(Coordinate u, Coordinate v) {
        double a = v.lng - u.lng;
        double b = v.lat - u.lat;
        return Math.sqrt(a * a + b * b);
    }

    private Coordinate vMinDistQ() {
        Coordinate minCoordinate = null;
        Double minDist = 0d;
        for (Coordinate vertex : Q) {
            if (minCoordinate == null) {
                minCoordinate = vertex;
                minDist = dist.get(minCoordinate);
            } else {
                double vDist = dist.get(vertex);
                if (minDist > vDist) {
                    minCoordinate = vertex;
                    minDist = vDist;
                }
            }
        }
        return minCoordinate;
    }

    private List<Coordinate> getNeighbors(Coordinate vertex) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (Edge edge : edges) {
            Coordinate start = edge.getStart();
            if (start.equals(vertex)) {
                coordinates.add(edge.getEnd());
            }
        }
        return coordinates;
    }

    public List<Coordinate> getShortestPath() throws NoPathException {
        LinkedList<Coordinate> S = new LinkedList<>();
        Coordinate u = target;
        while (prev.get(u) != null) {
            S.addFirst(u);
            u = prev.get(u);
        }
        if (u != source) {
            throw new NoPathException("Cannot get from " + source.toString() + " to " + target.toString());
        }
        S.addFirst(u);
        return S;
    }
}
