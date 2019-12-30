package io.github.t3r1jj.pbmap.model.map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import io.github.t3r1jj.pbmap.model.map.BoundingBox;
import io.github.t3r1jj.pbmap.model.map.PBMap;
import io.github.t3r1jj.pbmap.view.map.MapView;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.spy;

@RunWith(Parameterized.class)
public class MapViewTest {

    @Parameterized.Parameter
    public int width;
    @Parameterized.Parameter(1)
    public int scrollX;
    @Parameterized.Parameter(2)
    public int height;
    @Parameterized.Parameter(3)
    public int scrollY;
    @Parameterized.Parameter(4)
    public boolean initializing;

    @Parameterized.Parameters(name = "{index}: width = {0}, scrollX = {1}, height = {2}, scrollY = {3}, initializing = {4}")
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {0, 0, 0, 0, true},
                {0, 0, 0, 1, false},
                {0, 0, 1, 0, false},
                {0, 1, 0, 0, false},
                {1, 0, 0, 0, false},
                {0, 0, 1, 1, false},
                {0, 1, 1, 0, false},
                {1, 1, 0, 0, false},
                {1, 0, 0, 1, false},
                {1, 0, 1, 0, false},
                {0, 1, 0, 1, false},
                {1, 1, 1, 0, false},
                {1, 1, 0, 1, false},
                {1, 0, 1, 1, false},
                {0, 1, 1, 1, false},
                {1, 1, 1, 1, false},
        };
        return Arrays.asList(data);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void addToMap() {
        PBMap modelMock = mock(PBMap.class);
        BoundingBox box = new BoundingBox();
        box.add(new Coordinate(1, 2, 3));
        when(modelMock.getBoundingBox()).thenReturn(box);
        MapView map = spy(new MapView(null, modelMock));

        when(map.getWidth()).thenReturn(width);
        when(map.getScrollX()).thenReturn(scrollX);
        when(map.getHeight()).thenReturn(height);
        when(map.getScrollY()).thenReturn(scrollY);

        map.addToMap(map);
        verify(modelMock, initializing ? never() : times(1)).getId();
    }
}