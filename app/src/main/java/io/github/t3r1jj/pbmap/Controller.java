package io.github.t3r1jj.pbmap;

import android.widget.ImageView;

import com.qozix.tileview.paths.CompositePathView;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.util.List;

import io.github.t3r1jj.pbmap.model.gps.Person;
import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.model.map.PBMap;
import io.github.t3r1jj.pbmap.model.map.Place;
import io.github.t3r1jj.pbmap.model.map.Space;
import io.github.t3r1jj.pbmap.model.map.route.Graph;
import io.github.t3r1jj.pbmap.search.SearchSuggestion;
import io.github.t3r1jj.pbmap.view.MapView;
import io.github.t3r1jj.pbmap.view.Route;

public class Controller {
    private PBMap map;
    private MapView mapView;
    private ImageView destinationMarker;
    private ImageView personMarker;
    private Coordinate destination;
    private Route destinationRoute;
    private final MapActivity mapActivity;
    private Graph graph;

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

        graph = map.getGraph();
        if (graph != null) {
            List<CompositePathView.DrawablePath> drawablePaths = graph.createView(mapView).getDrawablePaths();
            for (CompositePathView.DrawablePath drawablePath : drawablePaths) {
                mapView.getCompositePathView().addPath(drawablePath);
            }
        }
        clearContext();
    }

    private void clearContext() {
        personMarker = null;
        destinationMarker = null;
    }

    private void pinpointPlace(final String placeName) {
        for (Place place : map.getPlaces()) {
            if (place.getName().equals(placeName)) {
                destination = place.getCenter();
                if (destinationMarker != null) {
                    mapView.removeMarker(destinationMarker);
                } else {
                    destinationMarker = new ImageView(mapView.getContext());
                }
                destinationMarker.setImageDrawable(mapView.getContext().getResources().getDrawable(R.drawable.marker));
                mapView.post(new Runnable() {
                    @Override
                    public void run() {
                        mapView.setScale(1f);
                        mapView.scrollToAndCenter(destination.lng, destination.lat);
                        mapView.setScaleFromCenter(getPinpointScale());
                        mapView.addMarker(destinationMarker, destination.lng, destination.lat, -0.5f, 0f);
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

    public void updatePosition(final Person person) {
        removePosition();
        setPosition(person);
    }

    private void setPosition(final Person person) {
        if (personMarker == null) {
            personMarker = person.createMarker(mapActivity);
        }
        if (mapView != null) {
            mapView.post(new Runnable() {
                @Override
                public void run() {
                    Coordinate center = person.getCoordinate();
                    mapView.scrollToAndCenter(center.lng, center.lat);
                    mapView.addMarker(personMarker, center.lng, center.lat, -0.5f, -0.5f);
                }
            });
            if (destinationMarker != null && personMarker != null) {
                updateRoute(person);
            }
        }
    }

    public void removePosition() {
        mapView.removeMarker(personMarker);
        mapView.removeRoute(destinationRoute);
    }

    private void updateRoute(Person person) {
        if (graph != null) {
            destinationRoute = new Route(mapView, graph.getRoute(person.getCoordinate(), destination));
            mapView.addRoute(destinationRoute);
        }
    }
}
