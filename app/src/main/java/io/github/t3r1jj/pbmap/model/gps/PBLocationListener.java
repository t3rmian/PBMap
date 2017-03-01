package io.github.t3r1jj.pbmap.model.gps;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import io.github.t3r1jj.pbmap.main.Controller;
import io.github.t3r1jj.pbmap.model.map.Coordinate;

public class PBLocationListener implements LocationListener {

    private final Person person = new Person();
    private final Controller controller;

    public PBLocationListener(Controller controller) {
        this.controller = controller;
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d(getClass().getSimpleName(), "Location has been changed: " + location);
        person.setCoordinate(new Coordinate(location.getLongitude(), location.getLatitude(), location.getAltitude()));
        controller.updatePosition(person);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
        controller.removePosition();
    }
}
