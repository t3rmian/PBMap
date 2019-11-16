package io.github.t3r1jj.pbmap.model.map;

import org.junit.Before;
import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.*;

public class SpaceTest {

    private static final String data = "    <space id=\"PB/WI\" reference_map_path=\"data/pb_wi.xml\" url=\"https://wi.pb.edu.pl\">\n" +
            "        <coordinates>\n" +
            "            <coordinate alt=\"150\" lat=\"53.11696\" lng=\"23.14564\" />\n" +
            "            <coordinate alt=\"150\" lat=\"53.11726\" lng=\"23.14709\" />\n" +
            "            <coordinate alt=\"150\" lat=\"53.11641\" lng=\"23.14759\" />\n" +
            "            <coordinate alt=\"150\" lat=\"53.11611\" lng=\"23.14614\" />\n" +
            "        </coordinates>\n" +
            "    </space>";

    private Space space;

    @Before
    public void setUp() throws Exception {
        Serializer serializer = new Persister();
        space = serializer.read(Space.class, data);
    }

    @Test
    public void getReferenceMapPath() {
        assertEquals("data/pb_wi.xml", space.getReferenceMapPath());
    }

    @Test
    public void setReferenceMapPath() {
        space.setReferenceMapPath("different");
        assertEquals("different", space.getReferenceMapPath());
    }

    @Test
    public void getUrl() {
        assertEquals("https://wi.pb.edu.pl", space.getUrl());
    }

    @Test
    public void getAddressResId() {
        assertEquals("address_pb_wi", space.getAddressResId());
    }

    @Test
    public void spaceIsPlace() {
        assertThat(space, isA(Place.class));
    }
}