package io.github.t3r1jj.pbmap.search;

import android.location.Location;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchSuggestionTest {

    @Test
    public void testEquals() {
        assertEquals(
                new SearchSuggestion("a", "b"),
                new SearchSuggestion("a", "b"));
        assertEquals(
                new SearchSuggestion("", ""),
                new SearchSuggestion("", ""));
        assertEquals(
                new SearchSuggestion("", ""),
                new SearchSuggestion("", "c"));
    }

    @Test
    public void testEquals_ByMapAndLocation() {
        SearchSuggestion a = new SearchSuggestion("a", "a");
        a.setMapId("a");
        SearchSuggestion aSameCoordinate = new SearchSuggestion("a", "a");
        aSameCoordinate.setMapId("a");

        assertEquals(a, aSameCoordinate);
        a.setMapId(null);
        aSameCoordinate.setMapId(null);

        Location location = mock(Location.class);
        when(location.getLatitude()).thenReturn(1d);
        when(location.getLongitude()).thenReturn(2d);
        a.setLocationCoordinate(location);

        location = mock(Location.class);
        when(location.getLatitude()).thenReturn(1d);
        when(location.getLongitude()).thenReturn(2d);
        aSameCoordinate.setLocationCoordinate(location);

        assertEquals(a, aSameCoordinate);
        a.setMapId("a");
        aSameCoordinate.setMapId("a");
        assertEquals(a, aSameCoordinate);
    }

    @Test
    public void testNotEquals() {
        assertNotEquals(
                new SearchSuggestion("_", "b"),
                new SearchSuggestion("a", "b"));
    }

    @Test
    public void testNotEquals_ByCoordinate() {
        SearchSuggestion a = new SearchSuggestion("a", "a");
        SearchSuggestion notTheSameCoordinate = new SearchSuggestion("a", "a");

        Location location = mock(Location.class);
        when(location.getLatitude()).thenReturn(1d);
        when(location.getLongitude()).thenReturn(2d);
        a.setLocationCoordinate(location);

        location = mock(Location.class);
        when(location.getLatitude()).thenReturn(3d);
        when(location.getLongitude()).thenReturn(4d);
        notTheSameCoordinate.setLocationCoordinate(location);

        assertNotEquals(a, notTheSameCoordinate);
    }

    @Test
    public void testNotEquals_ByMap() {
        SearchSuggestion a = new SearchSuggestion("a", "a");
        a.setMapId("a");
        SearchSuggestion notTheSameMap = new SearchSuggestion("a", "a");
        notTheSameMap.setMapId("b");

        assertNotEquals(a, notTheSameMap);
    }

    @Test
    public void testHashCode() {
        assertEquals(
                new SearchSuggestion("a", "b").hashCode(),
                new SearchSuggestion("a", "b").hashCode());
        assertEquals(
                new SearchSuggestion("a", "b").hashCode(),
                new SearchSuggestion("a", "_").hashCode());

        SearchSuggestion a = new SearchSuggestion("a", "a");
        SearchSuggestion aSameCoordinate = new SearchSuggestion("a", "a");

        a.setMapId("a");
        aSameCoordinate.setMapId("a");
        assertEquals(a.hashCode(), aSameCoordinate.hashCode());
        a.setMapId(null);
        aSameCoordinate.setMapId(null);

        Location location = mock(Location.class);
        when(location.getLatitude()).thenReturn(1d);
        when(location.getLongitude()).thenReturn(2d);
        a.setLocationCoordinate(location);

        location = mock(Location.class);
        when(location.getLatitude()).thenReturn(1d);
        when(location.getLongitude()).thenReturn(2d);
        aSameCoordinate.setLocationCoordinate(location);

        assertEquals(a.hashCode(), aSameCoordinate.hashCode());

        a.setMapId("a");
        aSameCoordinate.setMapId("a");
        assertEquals(a.hashCode(), aSameCoordinate.hashCode());
    }

}