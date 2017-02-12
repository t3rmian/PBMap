package io.github.t3r1jj.pbmap;

import android.util.Log;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import io.github.t3r1jj.pbmap.model.PBMap;
import io.github.t3r1jj.pbmap.model.Space;
import io.github.t3r1jj.pbmap.view.MapView;

public class Controller {
    MainActivity mainActivity;
    PBMap map;
    MapView mapView;

    Controller(MainActivity base, String assetsMapPath) {
        this.mainActivity = base;
        try {
            loadMap(assetsMapPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadMap(String assetsMapPath) throws Exception {
        Serializer serializer = new Persister();
        map = serializer.read(PBMap.class, mainActivity.getAssets().open(assetsMapPath));
        Log.d("MainActivity", map.toString());
        MapView nextMapView = map.createView(mainActivity);
        nextMapView.setController(this);
        mainActivity.setContentView(nextMapView);
        if (mapView != null) {
            mapView.addToMap(nextMapView);
        }
        mapView = nextMapView;
        mapView.loadPreviousPosition();
    }

    public void onNavigationPerformed(Space space) {
        try {
            loadMap(space.getMapReference());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
