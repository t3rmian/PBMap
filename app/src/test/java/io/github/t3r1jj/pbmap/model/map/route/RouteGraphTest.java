package io.github.t3r1jj.pbmap.model.map.route;

import org.junit.Before;
import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.util.Arrays;
import java.util.List;

import io.github.t3r1jj.pbmap.model.map.Coordinate;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class RouteGraphTest {

    private static final String data = "<route id=\"pb_campus\">\n" +
            "    <edges>\n" +
            "        <edge>\n" +
            "            <start lat=\"1\" lng=\"1\" /><!--a-->\n" +
            "            <end lat=\"2\" lng=\"1\" /><!--b-->\n" +
            "        </edge>\n" +
            "        <edge>\n" +
            "            <start lat=\"1\" lng=\"1\" /><!--a-->\n" +
            "            <end lat=\"3\" lng=\"3\" /><!--c-->\n" +
            "        </edge>\n" +
            "        <edge>\n" +
            "            <start lat=\"1\" lng=\"1\" /><!--a-->\n" +
            "            <end lat=\"6\" lng=\"6\" /><!--f-->\n" +
            "        </edge>\n" +
            "\t\t<edge>\n" +
            "            <start lat=\"2\" lng=\"1\" /><!--b-->\n" +
            "            <end lat=\"3\" lng=\"3\" /><!--c-->\n" +
            "        </edge>\n" +
            "\t\t<edge>\n" +
            "            <start lat=\"2\" lng=\"1\" /><!--b-->\n" +
            "            <end lat=\"4\" lng=\"4\" /><!--d-->\n" +
            "        </edge>\n" +
            "\t\t<edge>\n" +
            "            <start lat=\"3\" lng=\"3\" /><!--c-->\n" +
            "            <end lat=\"4\" lng=\"4\" /><!--d-->\n" +
            "        </edge>\n" +
            "\t\t<edge>\n" +
            "            <start lat=\"3\" lng=\"3\" /><!--c-->\n" +
            "            <end lat=\"6\" lng=\"6\" /><!--f-->\n" +
            "        </edge>\n" +
            "\t\t<edge>\n" +
            "            <start lat=\"4\" lng=\"4\" /><!--d-->\n" +
            "            <end lat=\"5\" lng=\"5\" /><!--e-->\n" +
            "        </edge>\n" +
            "\t\t<edge>\n" +
            "            <start lat=\"5\" lng=\"5\" /><!--e-->\n" +
            "            <end lat=\"6\" lng=\"6\" /><!--f-->\n" +
            "        </edge>\n" +
            "\t\t<edge>\n" +
            "            <start lat=\"10\" lng=\"10\" /><!--not connected-->\n" +
            "            <end lat=\"11\" lng=\"11\" /><!--not connected-->\n" +
            "        </edge>\n" +
            "\t</edges>\n" +
            "</route>";

    private RouteGraph routeGraph;

    private Coordinate a;
    private Coordinate e;
    private Coordinate c;
    private Coordinate d;

    @Before
    public void setUp() throws Exception {
        Serializer serializer = new Persister();
        routeGraph = serializer.read(RouteGraph.class, data);
        a = new Coordinate(1d, 1d);
        c = new Coordinate(3d, 3d);
        d = new Coordinate(4d, 4d);
        e = new Coordinate(5d, 5d);
    }

    @Test
    public void getRoute() {
        List<Coordinate> route = routeGraph.getRoute(a, c);
        List<Coordinate> expectedRoute = Arrays.asList(a, c);
        assertEquals(expectedRoute, route);
    }

    @Test
    public void findRoute_Complex() {
        List<Coordinate> route = routeGraph.getRoute(a, e);
        List<Coordinate> expectedRoute = Arrays.asList(a, c, d, e);
        assertEquals(expectedRoute, route);
    }

    @Test(expected = NoPathException.class)
    public void findRoute_NotConnected() {
        routeGraph.getRoute(a, new Coordinate(10d, 10d));
    }

    @Test
    public void findClosest() {
        assertEquals(a, routeGraph.findClosest(a));
    }

    @Test
    public void findClosest_Complex() {
        assertThat(routeGraph.findClosest(new Coordinate(3.5, 3.5)),
                anyOf(is(equalTo(c)), is(equalTo(d))));
    }

    @Test
    public void getEdges() {
        assertEquals(10, routeGraph.getEdges().size());
    }

}