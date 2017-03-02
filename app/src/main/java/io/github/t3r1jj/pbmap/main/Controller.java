package io.github.t3r1jj.pbmap.main;

import android.content.res.Resources;
import android.view.MotionEvent;
import android.widget.ImageView;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.Info;
import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.model.map.PBMap;
import io.github.t3r1jj.pbmap.model.map.Place;
import io.github.t3r1jj.pbmap.model.map.Space;
import io.github.t3r1jj.pbmap.model.map.route.Graph;
import io.github.t3r1jj.pbmap.search.MapsDao;
import io.github.t3r1jj.pbmap.search.SearchSuggestion;
import io.github.t3r1jj.pbmap.view.MapView;
import io.github.t3r1jj.pbmap.view.routing.GeoMarker;
import io.github.t3r1jj.pbmap.view.routing.Route;

public class Controller implements GeoMarker.MapListener {
    private final MapActivity mapActivity;
    private final MapsDao mapsDao;
    private PBMap map;
    private MapView mapView;
    private Graph graph;
    private GeoMarker source;
    private GeoMarker destination;
    private Route destinationRoute;

    Controller(MapActivity mapActivity) {
        this.mapActivity = mapActivity;
        this.mapsDao = new MapsDao(mapActivity);
    }

    void loadMap() {
        map = mapsDao.loadMap();
        updateView();
    }

    void loadMap(SearchSuggestion suggestion) {
        if (map != null && !map.getReferenceMapPath().equals(suggestion.mapPath)) {
            map = mapsDao.loadMap(suggestion.mapPath);
            updateView();
        }
        pinpointPlace(suggestion.place);
    }

    void loadPreviousMap() {
        map = mapsDao.loadMap(map.getPreviousMapPath());
        updateView();
    }

    private void updateView() {
        MapView nextMapView = map.createView(mapActivity);
        nextMapView.setController(this);
        mapActivity.setMapView(nextMapView);
        if (isInitialized()) {
            mapView.addToMap(nextMapView);
        }
        mapView = nextMapView;
        mapActivity.setBackButtonVisible(map.getPreviousMapPath() != null);
        mapActivity.setInfoButtonVisible(map.getDescriptionResName() != null || map.getUrl() != null);

        graph = map.getGraph();
        reloadContext();
        addAllRoutes();
    }

    /**
     * @deprecated use only for testing, no need to display all routes for user
     */
    @Deprecated
    private void addAllRoutes() {
        if (graph != null) {
            Route route = graph.createView(mapView);
            route.addToMap(mapView);
        }
    }

    private void reloadContext() {
        destination = GeoMarker.recreate(destination, mapActivity, this);
        Resources resources = mapActivity.getResources();
        destination.setImageDrawable(resources.getDrawable(R.drawable.destination_marker));
        source = GeoMarker.recreate(source, mapActivity, this);
        source.setImageDrawable(resources.getDrawable(R.drawable.source_marker));
        destination.addToMap(mapView);
        source.addToMap(mapView);
    }

    private void pinpointPlace(final String placeName) {
        for (Place place : map.getPlaces()) {
            if (place.getName().equals(placeName)) {
                destination.setCoordinate(place.getCenter());
                destination.pinpointOnMap(mapView);
                return;
            }
        }
    }

    public void onLongPress(MotionEvent event) {
        destination.addToMap(mapView, event);
    }

    public void onNavigationPerformed(Space space) {
        if (space.getReferenceMapPath() != null) {
            map = mapsDao.loadMap(space.getReferenceMapPath());
            updateView();
            mapView.loadPreviousPosition();
        } else if (space.getDescriptionResName() != null) {
            mapActivity.popupInfo(new Info(space));
        }
    }

    boolean isInitialized() {
        return mapView != null;
    }

    public void loadLogo(Place map) {
        ImageView logo = map.createLogo(mapActivity);
        mapActivity.setLogo(logo);
    }

    public void loadDescription() {
        mapActivity.popupInfo(new Info(map));
    }

    public void updatePosition(final Coordinate locationCoordinate) {
        source.setCoordinate(locationCoordinate);
        source.pinpointOnMap(mapView);
    }

    private void updateRoute() {
        if (graph != null) {
            if (destinationRoute != null) {
                destinationRoute.removeFromMap(mapView);
            }
            destinationRoute = new Route(mapView, graph.getRoute(source.getCoordinate(), destination.getCoordinate()));
            destinationRoute.addToMap(mapView);
        }
    }

    @Override
    public void onMapPositionChange() {
        if (mapView != null) {
            updateRoute();
        }
    }

}
