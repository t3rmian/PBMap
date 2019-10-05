package io.github.t3r1jj.pbmap.model.map;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoordinateTest {

    @Test
    public void routedDistance_0() {
        Coordinate a = new Coordinate(0, 1, 2);
        Coordinate b = new Coordinate(0, 1, 2);
        assertEquals(0, a.routedDistance(b), 0.001);
    }

    @Test
    public void routedDistance_HeightCheck() {
        Coordinate a = new Coordinate(0, 1, 2);
        Coordinate b = new Coordinate(0, 1, 2.5);
        assertEquals(0.5, a.routedDistance(b), 0.001);
    }

    @Test
    public void routedDistance_HeightCheck_Penalty() {
        Coordinate a = new Coordinate(0, 1, 2);
        Coordinate b = new Coordinate(0, 1, 3.0);
        assertThat(a.routedDistance(b)).isAtLeast(1000d);
    }

    @Test
    public void routedDistance_AllCheck() {
        Coordinate a = new Coordinate(0, 1, 2);
        Coordinate b = new Coordinate(0.25, 1.3, 2.5);
        double c = new Coordinate(a.lat, a.lng).distanceTo(new Coordinate(b.lat, b.lng));
        assertEquals(Math.sqrt(c * c + 0.25d), a.routedDistance(b), 0.001);
    }

    @Test
    public void hasAltitude() {
        assertTrue(new Coordinate(0d, 0d, 0d).hasAltitude());
        assertFalse(new Coordinate(0d, 0d).hasAltitude());
        assertTrue(new Coordinate().hasAltitude());
    }

}