package io.github.t3r1jj.pbmap.model.gps;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import io.github.t3r1jj.pbmap.main.Controller;
import io.github.t3r1jj.pbmap.model.map.Coordinate;

public class PBLocationListener implements LocationListener {

    private final Controller controller;

    public PBLocationListener(Controller controller) {
        this.controller = controller;
    }


    @Override
    public void onLocationChanged(Location location) {
        Coordinate coordinate = new Coordinate(location);
        controller.updatePosition(coordinate);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        // don't care about status change
    }

    @Override
    public void onProviderEnabled(String s) {
        // don't care about turning on, only the location and turning off is important
    }

    @Override
    public void onProviderDisabled(String s) {
        controller.updatePosition(null);
    }
}
