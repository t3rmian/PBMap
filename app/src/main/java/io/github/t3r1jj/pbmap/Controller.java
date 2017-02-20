package io.github.t3r1jj.pbmap;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.io.InputStream;

import io.github.t3r1jj.pbmap.model.Coordinate;
import io.github.t3r1jj.pbmap.model.PBMap;
import io.github.t3r1jj.pbmap.model.Place;
import io.github.t3r1jj.pbmap.model.Space;
import io.github.t3r1jj.pbmap.search.SearchSuggestion;
import io.github.t3r1jj.pbmap.view.MapView;

public class Controller {
    private PBMap map;
    private MapView mapView;
    private ImageView marker;
    private final MapActivity mapActivity;

    Controller(MapActivity base) {
        this.mapActivity = base;
    }

    void loadMap(SearchSuggestion suggestion) throws Exception {
        loadNewMap(suggestion.mapPath);
        pinpointPlace(suggestion.place);
    }

    void loadMap(String assetsMapPath) throws Exception {
        loadNewMap(assetsMapPath);
        mapView.loadPreviousPosition();
    }

    void loadPreviousMap() throws Exception {
        loadNewMap(map.getPreviousMapPath());
    }

    private void loadNewMap(String assetsMapPath) throws Exception {
        Serializer serializer = new Persister();
        map = serializer.read(PBMap.class, mapActivity.getAssets().open(assetsMapPath));
        MapView nextMapView = map.createView(mapActivity);
        nextMapView.setController(this);
        mapActivity.setMapView(nextMapView);
        if (isInitialized()) {
            mapView.addToMap(nextMapView);
        }
        mapView = nextMapView;
        mapActivity.setBackButtonVisible(map.getPreviousMapPath() != null);
    }

    private void pinpointPlace(final String placeName) {
        for (Place place : map.getPlaces()) {
            if (place.getName().equals(placeName)) {
                final Coordinate center = place.getCenter();
                if (marker != null) {
                    mapView.removeMarker(marker);
                }
                marker = new ImageView(mapView.getContext());
                marker.setImageDrawable(mapView.getContext().getResources().getDrawable(R.drawable.marker));
                mapView.post(new Runnable() {
                    @Override
                    public void run() {
                        mapView.setScale(1f);
                        mapView.scrollToAndCenter(center.lng, center.lat);
                        mapView.setScaleFromCenter(getPinpointScale());
                        mapView.addMarker(marker, center.lng, center.lat, -0.5f, -1.0f);
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
            loadMap(space.getReferenceMapPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isInitialized() {
        return mapView != null;
    }

    public void loadLogo(Place map) {
        ImageView logo = null;
        try {
            InputStream inputStream = mapActivity.getAssets().open(map.getLogoPath());
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            logo = new ImageView(mapActivity);
            logo.setImageDrawable(drawable);
        } catch (IllegalArgumentException | IOException e) {
            if (map.getLogoPath() != null) {
                e.printStackTrace();
            }
        }
        mapActivity.setLogo(logo);
    }

}
