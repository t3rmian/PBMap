package io.github.t3r1jj.pbmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import io.github.t3r1jj.pbmap.model.dictionary.MeasurementSystem;
import io.github.t3r1jj.pbmap.view.map.routing.FullRoute;
import io.github.t3r1jj.pbmap.view.map.routing.Route;

public class Config {
    private static Config instance = new Config();
    private boolean debug;
    private MeasurementSystem measurementSystem;

    private Config() {
    }

    public static Config getInstance() {
        return instance;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public MeasurementSystem getMeasurementSystem() {
        return measurementSystem;
    }

    @SuppressWarnings("deprecation")
    public Route createRoute(Context context) {
        if (isDebug()) {
            return new FullRoute(context);
        } else {
            return new Route(context);
        }
    }

    public void initPreferences(@NotNull Context context, @NotNull Locale locale) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (!preferences.contains(MapApplication.UNIT_SYSTEM)) {
            if (locale == Locale.US) {
                preferences.edit().putString(MapApplication.UNIT_SYSTEM, MeasurementSystem.US.toString()).apply();
            } else {
                preferences.edit().putString(MapApplication.UNIT_SYSTEM, MeasurementSystem.SI.toString()).apply();
            }
        }
        measurementSystem = MeasurementSystem.valueOf(preferences.getString(MapApplication.UNIT_SYSTEM,  MeasurementSystem.SI.toString()));
        debug = preferences.getBoolean(MapApplication.DEBUG, debug);
    }
}
