package io.github.t3r1jj.pbmap.model.map;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoundingBoxTest {

    @Test
    public void add_Initial() {
        BoundingBox boundingBox = new BoundingBox();
        assertEquals(Double.POSITIVE_INFINITY, boundingBox.getMinLat(), 0.01);
        assertEquals(Double.POSITIVE_INFINITY, boundingBox.getMinLng(), 0.01);
        assertEquals(Double.NEGATIVE_INFINITY, boundingBox.getMaxLat(), 0.01);
        assertEquals(Double.NEGATIVE_INFINITY, boundingBox.getMaxLng(), 0.01);
    }

    @Test
    public void add_OneCoordinate() {
        BoundingBox boundingBox = new BoundingBox();
        Coordinate coordinate = new Coordinate(1d, 2d);
        boundingBox.add(coordinate);
        assertEquals(coordinate.lat, boundingBox.getMinLat(), 0.01);
        assertEquals(coordinate.lng, boundingBox.getMinLng(), 0.01);
        assertEquals(coordinate.lat, boundingBox.getMaxLat(), 0.01);
        assertEquals(coordinate.lng, boundingBox.getMaxLng(), 0.01);
    }

    @Test
    public void add_TwoCoordinates() {
        BoundingBox boundingBox = new BoundingBox();
        Coordinate coordinate = new Coordinate(1d, 2d);
        Coordinate coordinate2 = new Coordinate(2d, 3d);
        boundingBox.add(coordinate);
        boundingBox.add(coordinate2);
        assertEquals(coordinate.lat, boundingBox.getMinLat(), 0.01);
        assertEquals(coordinate.lng, boundingBox.getMinLng(), 0.01);
        assertEquals(coordinate2.lat, boundingBox.getMaxLat(), 0.01);
        assertEquals(coordinate2.lng, boundingBox.getMaxLng(), 0.01);
    }

    @Test
    public void add_ThreeCoordinates() {
        BoundingBox boundingBox = new BoundingBox();
        Coordinate coordinate = new Coordinate(1d, 2d);
        Coordinate coordinate2 = new Coordinate(2d, 3d);
        Coordinate coordinate3 = new Coordinate(-1d, 4d);
        boundingBox.add(coordinate);
        boundingBox.add(coordinate2);
        boundingBox.add(coordinate3);
        assertEquals(coordinate3.lat, boundingBox.getMinLat(), 0.01);
        assertEquals(coordinate.lng, boundingBox.getMinLng(), 0.01);
        assertEquals(coordinate2.lat, boundingBox.getMaxLat(), 0.01);
        assertEquals(coordinate3.lng, boundingBox.getMaxLng(), 0.01);
    }
}