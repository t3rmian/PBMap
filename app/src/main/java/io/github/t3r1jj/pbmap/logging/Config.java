package io.github.t3r1jj.pbmap.logging;

import android.content.Context;

import io.github.t3r1jj.pbmap.view.map.routing.FullRoute;
import io.github.t3r1jj.pbmap.view.map.routing.Route;

public class Config {
    private static Config instance = new Config();
    private boolean debug;
    private boolean smooth;

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

    public Route createRoute(Context context) {
        if (isDebug()) {
            return new FullRoute(context);
        } else {
            return new Route(context);
        }
    }

    public boolean isSmooth() {
        return smooth;
    }

    public void setSmooth(boolean smooth) {
        this.smooth = smooth;
    }
}
