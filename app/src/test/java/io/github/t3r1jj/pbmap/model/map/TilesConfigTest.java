package io.github.t3r1jj.pbmap.model.map;

import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import static org.junit.Assert.*;

public class TilesConfigTest {

    @Test
    public void load() throws Exception {
        String input = "<tiles_config height=\"200\" path=\"tiles/pb_campus/1000/tile-%d_%d.png\" width=\"300\"\n" +
                "            zoom=\"1\" />";
        Serializer serializer = new Persister();
        TilesConfig config = serializer.read(TilesConfig.class, input);
        assertEquals(200, config.height);
        assertEquals(300, config.width);
        assertEquals(1, config.zoom, 0.001);
        assertEquals("tiles/pb_campus/1000/tile-%d_%d.png", config.path);
    }
}