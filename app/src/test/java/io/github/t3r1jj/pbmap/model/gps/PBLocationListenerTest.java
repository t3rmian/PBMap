package io.github.t3r1jj.pbmap.model.gps;

import android.location.Location;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.github.t3r1jj.pbmap.main.Controller;
import io.github.t3r1jj.pbmap.model.map.Coordinate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PBLocationListenerTest {

    @Mock
    private Controller controller;
    private PBLocationListener locationListener;

    @Before
    public void setUp() {
        locationListener = new PBLocationListener(controller);
    }

    @Test
    public void onLocationChanged() {
        Location location = new Location("");
        location.setLatitude(1);
        location.setLongitude(22);
        location.setAltitude(333);
        locationListener.onLocationChanged(location);
        verify(controller).updatePosition(eq(new Coordinate(location.getLatitude(), location.getLongitude(), location.getAltitude())));
    }

    @Test
    public void onLocationChanged_noAltitude() {
        Location location = new Location("");
        location.setLatitude(1);
        location.setLongitude(22);
        locationListener.onLocationChanged(location);
        Coordinate coordinate = new Coordinate(location.getLatitude(), location.getLongitude());
        coordinate.setAltitude(false);
        verify(controller).updatePosition(eq(coordinate));
    }

    @Test
    public void onProviderDisabled() {
        locationListener.onProviderDisabled("");
        verify(controller).updatePosition(null);
    }
}