package io.github.t3r1jj.pbmap.model.map;

import org.junit.Before;
import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PBMapTest {

    private static final String data = "<map down_map_path=\"data/pb_acs_0.xml\" height=\"2200\" id=\"PB_ACS\" unfinished=\"true\"\n" +
            "    previous_map_path=\"data/pb_campus.xml\" route_path=\"routes/pb_campus.xml\"\n" +
            "    up_map_path=\"data/pb_acs_2.xml\" url=\"https://pb.edu.pl/swfis/informacje-ogolne\" width=\"1820\">\n" +
            "    <tiles_configs>\n" +
            "        <tiles_config height=\"256\" path=\"tiles/pb_campus/acs/main/tile-%d_%d.png\" width=\"256\"\n" +
            "            zoom=\"1\" />\n" +
            "    </tiles_configs>\n" +
            "    <coordinates>\n" +
            "        <coordinate alt=\"150\" lat=\"53.11903\" lng=\"23.14564\" />\n" +
            "        <coordinate alt=\"150\" lat=\"53.11832\" lng=\"23.14662\" />\n" +
            "    </coordinates>\n" +
            "\n" +
            "    <spot id=\"gym\">\t<coordinates>\t\t<coordinate lat=\"53.11883933267212\" lng=\"23.14608460083008\" alt=\"150.0\"/>\t</coordinates></spot>\n" +
            "\n" +
            "    <space id=\"PB_WI\" reference_map_path=\"data/pb_wi.xml\" url=\"https://wi.pb.edu.pl\">\n" +
            "        <coordinates>\n" +
            "            <coordinate alt=\"150\" lat=\"53.11696\" lng=\"23.14564\" />\n" +
            "            <coordinate alt=\"150\" lat=\"53.11726\" lng=\"23.14709\" />\n" +
            "            <coordinate alt=\"150\" lat=\"53.11641\" lng=\"23.14759\" />\n" +
            "            <coordinate alt=\"150\" lat=\"53.11611\" lng=\"23.14614\" />\n" +
            "        </coordinates>\n" +
            "    </space>" +
            "</map>";

    private PBMap map;

    @Before
    public void setUp() throws Exception {
        Serializer serializer = new Persister();
        map = serializer.read(PBMap.class, data);
    }

    @Test
    public void getPlaces() {
        assertEquals(2, map.getPlaces().size());
        assertThat(map.getPlaces(), hasItem(isA(Spot.class)));
        assertThat(map.getPlaces(), hasItem(isA(Space.class)));
    }

    @Test
    public void getGraphPath() {
        assertEquals("routes/pb_campus.xml", map.getGraphPath());
    }

    @Test
    public void getNavigationMapPath() {
        assertEquals("data/pb_acs_0.xml", map.getNavigationMapPath(PBMap.Navigation.DOWN));
        assertEquals("data/pb_acs_2.xml", map.getNavigationMapPath(PBMap.Navigation.UP));
        assertNull(map.getNavigationMapPath(PBMap.Navigation.LEFT));
        assertNull(map.getNavigationMapPath(PBMap.Navigation.RIGHT));
        assertEquals("data/pb_campus.xml", map.getNavigationMapPath(PBMap.Navigation.BACK));
    }

    @Test
    public void compareAltitude() {
        assertEquals(0, map.compareAltitude(new Coordinate(0d, 0d, 150d)));
        assertEquals(1, map.compareAltitude(new Coordinate(0d, 0d, 151d)));
        assertEquals(-1, map.compareAltitude(new Coordinate(0d, 0d, 149d)));
    }

    @Test
    public void getBoundingBox() {
        BoundingBox boundingBox = map.getBoundingBox();
        assertEquals(53.11832d, boundingBox.getMinLat(), 0.00001);
        assertEquals(53.11903d, boundingBox.getMaxLat(), 0.00001);
        assertEquals(23.14564d, boundingBox.getMinLng(), 0.00001);
        assertEquals(23.14662d, boundingBox.getMaxLng(), 0.00001);
    }

    @Test
    public void removeDifferentAltitudePoints() {
        Coordinate sameAltCoordinate1 = new Coordinate(0d, 0d, 150d);
        Coordinate sameAltCoordinate2 = new Coordinate(1d, 1d, 150d);
        Coordinate sameAltCoordinate3 = new Coordinate(2d, 2d, 150d);
        List<Coordinate> route = new ArrayList<>(Arrays.asList(
                sameAltCoordinate1,
                sameAltCoordinate2,
                sameAltCoordinate3,
                new Coordinate(3d, 3d, 149d),
                new Coordinate(4d, 4d, 151d)
        ));
        map.removeDifferentAltitudePoints(route);
        assertEquals(Arrays.asList(sameAltCoordinate1, sameAltCoordinate2, sameAltCoordinate3), route);
    }

    @Test
    public void isUnfinished() {
        assertTrue(map.isUnfinished());
    }

    @Test
    public void findClosest() {
        Place closestPlace = map.findClosest(new Coordinate(53.11696d, 23.14564d));
        assertEquals("PB_WI", closestPlace.id);
    }
}