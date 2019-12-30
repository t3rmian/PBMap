package io.github.t3r1jj.pbmap.model.map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class CoordinateParametrizedTest {

    private static final double ALT = 5.5;
    @Parameterized.Parameter
    public boolean thisHasAlt;
    @Parameterized.Parameter(1)
    public double thatAlt;
    @Parameterized.Parameter(2)
    public boolean thatHasAlt;
    @Parameterized.Parameter(3)
    public boolean same;

    @Parameterized.Parameters(name = "{index}: ALT = " + ALT + ", thisHasAlt = {0}, thatAlt = {1}, thatHasAlt = {2}, same = {3}")
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {false, 5.5, false, true},
                {true, 5.5, false, true},
                {false, 5.5, true, true},
                {true, 5.5, true, true},
                {true, 10, true, false},
                {true, 10, false, true},
                {false, 10, true, true},
                {false, 10, false, true},
        };
        return Arrays.asList(data);
    }

    @Test
    public void setAltitude() {
        Coordinate thisCoordinate = spy(new Coordinate(1, 2, ALT));
        when(thisCoordinate.hasAltitude()).thenReturn(thisHasAlt);
        Coordinate thatCoordinate = spy(new Coordinate(3, 4, thatAlt));
        when(thatCoordinate.hasAltitude()).thenReturn(thatHasAlt);
        assertEquals(same, thisCoordinate.sameAltitude(thatCoordinate));
    }
}