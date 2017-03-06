package io.github.t3r1jj.pbmap.main;

import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.List;

import io.github.t3r1jj.pbmap.model.Info;
import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.model.map.PBMap;
import io.github.t3r1jj.pbmap.model.map.Place;
import io.github.t3r1jj.pbmap.model.map.Space;
import io.github.t3r1jj.pbmap.model.map.route.RouteGraph;
import io.github.t3r1jj.pbmap.search.MapsDao;
import io.github.t3r1jj.pbmap.search.SearchSuggestion;
import io.github.t3r1jj.pbmap.view.MapView;
import io.github.t3r1jj.pbmap.view.routing.GeoMarker;
import io.github.t3r1jj.pbmap.view.routing.Route;

//TODO: refactor Route?
public class Controller implements GeoMarker.MapListener {
    private final MapActivity mapActivity;
    private final MapsDao mapsDao;
    private PBMap map;
    private MapView mapView;
    private RouteGraph routeGraph;
    private GeoMarker source;
    private GeoMarker destination;
    private Route destinationRoute;

    Controller(MapActivity mapActivity) {
        this.mapActivity = mapActivity;
        this.mapsDao = new MapsDao(mapActivity);
    }

    void loadMap() {
        map = mapsDao.loadMap();
        loadRouteGraph();
        updateView();
    }

    void loadMap(SearchSuggestion suggestion) {
        if (map != null && !map.getReferenceMapPath().equals(suggestion.mapPath)) {
            map = mapsDao.loadMap(suggestion.mapPath);
            loadRouteGraph();
            updateView();
        }
        pinpointPlace(suggestion.placeId);
    }

    void loadPreviousMap() {
        map = mapsDao.loadMap(map.getPreviousMapPath());
        loadRouteGraph();
        updateView();
    }

    private void loadRouteGraph() {
        if (map == null || routeGraph == null || !routeGraph.getPath().equals(map.getGraphPath())) {
            routeGraph = mapsDao.loadGraph(map);
        }
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
        mapActivity.setInfoButtonVisible(map.getDescription(mapActivity) != null || map.getUrl() != null);

        reloadContext();
        addAllRoutes();
    }

    /**
     * @deprecated use only for testing, no need to display all routes for user
     */
    @Deprecated
    private void addAllRoutes() {
        if (routeGraph != null) {
            Route route = routeGraph.createView(mapView);
            route.addToMap(mapView);
        }
    }

    private void reloadContext() {
        destination = GeoMarker.recreate(destination, mapActivity, this);
        destination.setLevel(map.compareAltitude(destination.getCoordinate()), GeoMarker.Marker.DESTINATION);
        source = GeoMarker.recreate(source, mapActivity, this);
        source.setLevel(map.compareAltitude(source.getCoordinate()), GeoMarker.Marker.SOURCE);
        destination.addToMap(mapView);
        source.addToMap(mapView);
    }

    private void pinpointPlace(final String placeId) {
        for (Place place : map.getPlaces()) {
            if (place.getId().equals(placeId)) {
                Coordinate target = place.getCenter();
                target.alt = map.getCenter().alt;
                destination.setCoordinate(target);
                destination.setLevel(map.compareAltitude(destination.getCoordinate()), GeoMarker.Marker.DESTINATION);
                destination.pinpointOnMap(mapView);
                return;
            }
        }
    }

    public void onLongPress(MotionEvent event) {
        if (destination.isAtPosition(mapView, event, map.getCenter().alt)) {
            destination.setCoordinate(null);
            destination.removeFromMap(mapView);
        } else if (source.isAtPosition(mapView, event, map.getCenter().alt)) {
            source.setCoordinate(null);
            source.removeFromMap(mapView);
        } else {
            mapActivity.askUserForMarkerChoice(event);
        }
    }

    public void onUserMarkerChoice(MotionEvent event, GeoMarker.Marker markerChoice) {
        GeoMarker marker = null;
        if (markerChoice == GeoMarker.Marker.SOURCE) {
            marker = source;
        } else {
            marker = destination;
        }
        marker.addToMap(mapView, event, map.getCenter().alt);
        int level = 0;
        marker.setLevel(level, markerChoice);
    }

    public void onNavigationPerformed(Space space) {
        if (space.getReferenceMapPath() != null) {
            map = mapsDao.loadMap(space.getReferenceMapPath());
            updateView();
            mapView.loadPreviousPosition();
        } else if (space.getDescription(mapActivity) != null) {
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
        source.setLevel(map.compareAltitude(source.getCoordinate()), GeoMarker.Marker.SOURCE);
        source.pinpointOnMap(mapView);
    }

    private void updateRoute() {
        if (routeGraph != null) {
            if (destinationRoute != null) {
                destinationRoute.removeFromMap(mapView);
            }
            List<Coordinate> route = routeGraph.getRoute(source.getCoordinate(), destination.getCoordinate());
            map.removeDifferentAltitudePoints(route);
            destinationRoute = new Route(mapView, route);
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
