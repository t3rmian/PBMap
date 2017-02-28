package io.github.t3r1jj.pbmap;

import android.widget.ImageView;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import io.github.t3r1jj.pbmap.model.Coordinate;
import io.github.t3r1jj.pbmap.model.PBMap;
import io.github.t3r1jj.pbmap.model.Place;
import io.github.t3r1jj.pbmap.model.Space;
import io.github.t3r1jj.pbmap.model.gps.Person;
import io.github.t3r1jj.pbmap.search.SearchSuggestion;
import io.github.t3r1jj.pbmap.view.MapView;

public class Controller {
    private PBMap map;
    private MapView mapView;
    private ImageView marker;
    private ImageView personMarker;
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
                        mapView.addMarker(marker, center.lng, center.lat, -0.5f, 0f);
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
            if (space.getReferenceMapPath() != null) {
                loadMap(space.getReferenceMapPath());
            } else if (space.getDescriptionResName() != null) {
                mapActivity.popupInfo(new MapActivity.Info(space.getName(), space.getDescriptionResName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isInitialized() {
        return mapView != null;
    }

    public void loadLogo(Place map) {
        ImageView logo = map.getLogo(mapActivity);
        mapActivity.setLogo(logo);
    }

    public void loadDescription() {
        mapActivity.popupInfo(new MapActivity.Info(map.getName(), map.getDescriptionResName()));
    }

    //TODO: optimize markers creation
    public void updatePosition(final Person person) {
        if (personMarker != null) {
            mapView.removeMarker(personMarker);
        }
        personMarker = person.getMarker(mapActivity);
        mapView.post(new Runnable() {
            @Override
            public void run() {
                Coordinate center = person.getCoordinate();
                mapView.scrollToAndCenter(center.lng, center.lat);
                mapView.addMarker(personMarker, center.lng, center.lat, -0.5f, -0.5f);
            }
        });
    }
}
