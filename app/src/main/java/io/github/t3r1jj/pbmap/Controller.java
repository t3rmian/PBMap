package io.github.t3r1jj.pbmap;

import android.util.Log;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import io.github.t3r1jj.pbmap.model.Coordinate;
import io.github.t3r1jj.pbmap.model.PBMap;
import io.github.t3r1jj.pbmap.model.Place;
import io.github.t3r1jj.pbmap.model.Space;
import io.github.t3r1jj.pbmap.search.SearchSuggestion;
import io.github.t3r1jj.pbmap.view.MapView;

public class Controller {
    private PBMap map;
    private MapView mapView;
    private final MainActivity mainActivity;

    Controller(MainActivity base) {
        this.mainActivity = base;
    }

    void loadMap(SearchSuggestion suggestion) throws Exception {
        loadNewMap(suggestion.mapPath);
        pinpointPlace(suggestion.place);
    }

    void loadMap(String assetsMapPath) throws Exception {
        loadNewMap(assetsMapPath);
        mapView.loadPreviousPosition();
    }

    private void loadNewMap(String assetsMapPath) throws Exception {
        Serializer serializer = new Persister();
        map = serializer.read(PBMap.class, mainActivity.getAssets().open(assetsMapPath));
        MapView nextMapView = map.createView(mainActivity);
        nextMapView.setController(this);
        mainActivity.setMapView(nextMapView);
        if (mapView != null) {
            mapView.addToMap(nextMapView);
        }
        mapView = nextMapView;
    }

    private void pinpointPlace(final String placeName) {
        for (Place place : map.getPlaces()) {
            if (place.getName().equals(placeName)) {
                final Coordinate center = place.getCenter();
                mapView.post(new Runnable() {
                    @Override
                    public void run() {
                        mapView.setScale(1f);
                        mapView.scrollToAndCenter(center.lng, center.lat);
                        mapView.setScaleFromCenter(getPinpointScale());
                    }
                });
                return;
            }
        }
    }

    private float getPinpointScale() {
        return 1f;
    }

    public void onNavigationPerformed(Space space) {
        try {
            loadMap(space.getMapReference());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isInitialized() {
        return mapView != null;
    }

}
