package io.github.t3r1jj.pbmap.main;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

public class DeviceServices extends ContextWrapper {
    public DeviceServices(Context ctx) {
        super(ctx);
    }

    boolean doesNotHaveGpsPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    boolean isLocationEnabled(LocationManager locationManager) {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    boolean isWifiDisabled() {
        return getWifi().isAvailable();
    }

    boolean isAirplaneOn() {
        int airplaneSetting = Settings.System.getInt(this.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
        return airplaneSetting != 0;
    }

    public LocationManager getLocationManager() {
        return (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    public boolean isWifiConnected() {
        return true;
    }

    private NetworkInfo getWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    }
}