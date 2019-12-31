package io.github.t3r1jj.pbmap.model.map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(Parameterized.class)
public class BoundingBoxParametrizedTest {

    private static final int MIN = 0;
    private static final int MAX = 10;
    @Parameterized.Parameter
    public double lat;
    @Parameterized.Parameter(1)
    public double lng;
    @Parameterized.Parameter(2)
    public boolean inside;

    @Parameterized.Parameters(name = "{index}: MIN = " + MIN + ", MAX = " + MAX + ", lat = {0}, lng = {1}, inside = {2}")
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {0, 0, true},
                {0, 10, true},
                {10, 0, true},
                {10, 10, true},
                {5, 5, true},
                {-1, 0, false},
                {11, 0, false},
                {0, -1, false},
                {0, 11, false},
                {-1, -1, false},
                {11, 11, false},
                {-1, 11, false},
                {11, -1, false},
        };
        return Arrays.asList(data);
    }

    @Test
    public void isInside() {
        BoundingBox boundingBox = new BoundingBox();
        boundingBox.add(new Coordinate(MIN, MIN));
        boundingBox.add(new Coordinate(MAX, MAX));
        assertEquals(inside, boundingBox.isInside(new Coordinate(lat, lng)));
    }
}