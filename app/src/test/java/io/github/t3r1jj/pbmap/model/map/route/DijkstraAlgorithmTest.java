package io.github.t3r1jj.pbmap.model.map.route;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import io.github.t3r1jj.pbmap.model.map.Coordinate;

import static junit.framework.Assert.assertTrue;

public class DijkstraAlgorithmTest {

    private DijkstraAlgorithm dijkstra;
    private List<Coordinate> vertexes = new LinkedList<>();
    private List<Edge> edges = new LinkedList<>();
    private Coordinate a;
    private Coordinate e;
    private Coordinate c;
    private Coordinate d;
    private Coordinate notConnected;

    @Before
    public void setUp() {
        a = new Coordinate(1d, 1d);
        Coordinate b = new Coordinate(2d, 2d);
        c = new Coordinate(3d, 3d);
        d = new Coordinate(4d, 4d);
        e = new Coordinate(5d, 5d);
        Coordinate f = new Coordinate(6d, 6d);
        notConnected = new Coordinate(7d, 7d);
        vertexes.add(a);
        vertexes.add(b);
        vertexes.add(c);
        vertexes.add(d);
        vertexes.add(e);
        vertexes.add(f);
        edges.add(new Edge(a, b));
        edges.add(new Edge(a, c));
        edges.add(new Edge(a, f));
        edges.add(new Edge(b, c));
        edges.add(new Edge(b, d));
        edges.add(new Edge(c, d));
        edges.add(new Edge(c, f));
        edges.add(new Edge(d, e));
        edges.add(new Edge(e, f));
        dijkstra = new DijkstraAlgorithmStub(vertexes, edges);

    }

    @Test
    public void execute_shortestPathFound() throws NoPathException {
        dijkstra.setSource(a);
        dijkstra.setTarget(e);
        dijkstra.execute();
        List<Coordinate> shortestPath = dijkstra.getShortestPath();
        LinkedList<Coordinate> expectedPath = new LinkedList<>();
        expectedPath.add(a);
        expectedPath.add(c);
        expectedPath.add(d);
        expectedPath.add(e);
        System.out.println(expectedPath.toString());
        System.out.println(shortestPath.toString());
        assertTrue(expectedPath.equals(shortestPath));
    }

    @Test(expected = NoPathException.class)
    public void execute_noPathFound() throws NoPathException {
        dijkstra.setSource(a);
        dijkstra.setTarget(notConnected);
        dijkstra.execute();
        dijkstra.getShortestPath();
    }

    @Test(expected = RuntimeException.class)
    public void execute_noSourceTarget() throws NoPathException {
        dijkstra.execute();
    }

    private class DijkstraAlgorithmStub extends DijkstraAlgorithm {

        public DijkstraAlgorithmStub(List<Coordinate> vertexes, List<Edge> edges) {
            super(vertexes, edges);
        }

        @Override
        protected Double length(Coordinate u, Coordinate v) {
            if (u.lat == 1d) {
                if (v.lat == 2d) {
                    return 7d;
                } else if (v.lat == 3d) {
                    return 9d;
                } else if (v.lat == 6d) {
                    return 14d;
                }
            } else if (u.lat == 2d) {
                if (v.lat == 3d) {
                    return 10d;
                } else if (v.lat == 4d) {
                    return 15d;
                }
            } else if (u.lat == 3d) {
                if (v.lat == 4d) {
                    return 11d;
                } else if (v.lat == 6d) {
                    return 2d;
                }
            } else if (u.lat == 4d) {
                if (v.lat == 5d) {
                    return 6d;
                }
            } else if (u.lat == 5d) {
                if (v.lat == 6d) {
                    return 9d;
                }
            }
            throw new RuntimeException("No connection: " + u.toString() + " -> " + v.toString());
        }
    }

}