package io.github.t3r1jj.pbmap.model.map;

import org.junit.Before;
import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SpotTest {

    private static final String data = "<spot id=\"gym/test\" logo_path=\"test_path\">" +
            "\t<coordinates>\t\t<coordinate lat=\"53.11883933267212\" lng=\"23.14608460083008\" alt=\"150.0\"/>\t</coordinates>" +
            "</spot>";

    private Spot spot;

    @Before
    public void setUp() throws Exception {
        Serializer serializer = new Persister();
        spot = serializer.read(Spot.class, data);
    }

    @Test
    public void getNameResIdString() {
        assertEquals("gym_test_name", spot.getNameResIdString());
    }

    @Test
    public void getDescriptionResIdString() {
        assertEquals("gym_test_description", spot.getDescriptionResIdString());
    }

    @Test
    public void getId() {
        assertEquals("gym/test", spot.getId());
    }

    @Test
    public void getCoordinates() {
        assertEquals(Collections.singletonList(new Coordinate(53.11883933267212d, 23.14608460083008, 150.0d)),
                spot.getCoordinates());
    }

    @Test
    public void getLogoPath() {
        assertEquals("test_path", spot.getLogoPath());
    }

    @Test
    public void getCenter() {
        assertEquals(new Coordinate(53.11883933267212d, 23.14608460083008, 150.0d), spot.getCenter());
    }

    @Test
    public void spotIsPlace() {
        assertThat(spot, isA(Place.class));
    }
}