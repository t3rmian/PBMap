package io.github.t3r1jj.pbmap.main;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.qozix.tileview.geom.CoordinateTranslater;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.logging.Config;
import io.github.t3r1jj.pbmap.logging.Message;
import io.github.t3r1jj.pbmap.logging.WebLogger;
import io.github.t3r1jj.pbmap.model.Info;
import io.github.t3r1jj.pbmap.model.dictionary.MeasurementSystem;
import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.model.map.PBMap;
import io.github.t3r1jj.pbmap.model.map.Place;
import io.github.t3r1jj.pbmap.model.map.Space;
import io.github.t3r1jj.pbmap.model.map.route.RouteGraph;
import io.github.t3r1jj.pbmap.search.MapsDao;
import io.github.t3r1jj.pbmap.search.SearchSuggestion;
import io.github.t3r1jj.pbmap.view.map.MapView;
import io.github.t3r1jj.pbmap.view.map.routing.GeoMarker;
import io.github.t3r1jj.pbmap.view.map.routing.Route;

public class Controller implements GeoMarker.MapListener {
    static final String PARCELABLE_KEY_CONTROLLER_MEMENTO = "PARCELABLE_KEY_CONTROLLER_MEMENTO";
    private MapActivity mapActivity;
    private MapsDao mapsDao;
    private PBMap map;
    private MapView mapView;
    private GeoMarker source;
    private GeoMarker destination;
    private Route route;
    private SearchSuggestion preloadedSuggestion;

    Controller(MapActivity mapActivity) {
        this.mapActivity = mapActivity;
        this.mapsDao = new MapsDao(mapActivity);
        this.route = Config.getInstance().createRoute(mapActivity);
    }

    void restoreState(@NotNull Memento memento, @NotNull MapActivity mapActivity) {
        this.mapActivity = mapActivity;
        this.mapsDao = new MapsDao(mapActivity);
        this.route = Config.getInstance().createRoute(mapActivity);
        this.map = mapsDao.loadMap(memento.mapReferencePath);
        loadRouteGraph();
        if (source == null) {
            source = new GeoMarker(mapActivity);
        }
        source.setCoordinate(memento.source);
        if (destination == null) {
            destination = new GeoMarker(mapActivity);
        }
        destination.setCoordinate(memento.destination);
        updateView();
    }

    /**
     * Loads default map
     */
    void loadMap() {
        map = mapsDao.loadMap();
        loadRouteGraph();
    }

    void loadMap(SearchSuggestion suggestion, boolean preload) {
        if (map == null || !map.getReferenceMapPath().equals(suggestion.getMapPath())) {
            map = mapsDao.loadMap(suggestion.getMapPath());
            loadRouteGraph();
            if (!preload) {
                updateView();
            }
        }
        if (!preload) {
            pinpointSuggestion(suggestion);
        } else {
            preloadedSuggestion = suggestion;
        }
    }

    void postLoad() {
        updateView();
        if (preloadedSuggestion != null) {
            pinpointSuggestion(preloadedSuggestion);
            preloadedSuggestion = null;
        }
    }

    private void pinpointSuggestion(SearchSuggestion suggestion) {
        Coordinate coordinate = suggestion.getCoordinate();
        if (coordinate != null) {
            pinpointLocation(coordinate);
        } else {
            pinpointPlace(suggestion.getPlaceId());
        }
    }

    private void loadRouteGraph() {
        route.setMap(map);
        RouteGraph routeGraph = route.getRouteGraph();
        if (routeGraph == null || !routeGraph.getPath().equals(map.getGraphPath())) {
            routeGraph = mapsDao.loadGraph(map);
            route.setRouteGraph(routeGraph);
        }
    }

    private void updateView() {
        MapView nextMapView = map.createView(mapActivity);
        nextMapView.setController(this);
        mapActivity.setMapView(nextMapView);
        if (isInitialized()) {
            mapView.addToMap(mapView);
        }
        mapView = nextMapView;
        mapActivity.setInfoButtonVisible(map.getDescription(mapActivity) != null || map.getUrl() != null);
        mapActivity.setLevelMenuVisible(map.getNavigationMapPath(PBMap.Navigation.UP) != null || map.getNavigationMapPath(PBMap.Navigation.DOWN) != null);
        for (PBMap.Navigation navigation : PBMap.Navigation.values()) {
            mapActivity.setLevelButtonVisible(navigation, map.getNavigationMapPath(navigation) != null);
        }
        reloadContext();
    }

    private void reloadContext() {
        destination = GeoMarker.recreate(destination, mapActivity, this);
        destination.setLevel(map.compareAltitude(destination.getCoordinate()), GeoMarker.Marker.DESTINATION);
        source = GeoMarker.recreate(source, mapActivity, this);
        source.setLevel(map.compareAltitude(source.getCoordinate()), GeoMarker.Marker.SOURCE);
        destination.addToMap(mapView);
        source.addToMap(mapView);
    }

    private void pinpointLocation(Coordinate target) {
        target.alt = map.getCenter().alt;
        destination.setCoordinate(target);
        destination.setLevel(map.compareAltitude(destination.getCoordinate()), GeoMarker.Marker.DESTINATION);
        destination.pinpointOnMap(mapView);
    }

    private void pinpointPlace(final String placeId) {
        List<Place> places = map.getPlaces();
        if (places == null) {
            return;
        }
        for (Place place : places) {
            if (place.getId().equals(placeId)) {
                Coordinate target = place.getCenter();
                destination.setCoordinate(target);
                destination.setLevel(map.compareAltitude(destination.getCoordinate()), GeoMarker.Marker.DESTINATION);
                destination.pinpointOnMap(mapView);
                return;
            }
        }
    }

    /**
     * @param event press event
     * @deprecated Use this only for logging coordinates
     */
    @Deprecated
    public void printPressedCoordinate(MotionEvent event) {
        CoordinateTranslater coordinateTranslater = mapView.getCoordinateTranslater();
        double lng = coordinateTranslater.translateAndScaleAbsoluteToRelativeX(mapView.getScrollX() + event.getX() - mapView.getOffsetX(), mapView.getScale());
        double lat = coordinateTranslater.translateAndScaleAbsoluteToRelativeY(mapView.getScrollY() + event.getY() - mapView.getOffsetY(), mapView.getScale());
        System.out.println(new Coordinate(lat, lng, map.getCenter().alt));
        Toast.makeText(mapActivity, String.format("lat=%f; lng=%f; alt=%f", lat, lng, map.getCenter().alt), Toast.LENGTH_SHORT).show();
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

    void onUserMarkerChoice(MotionEvent event, GeoMarker.Marker markerChoice) {
        GeoMarker marker;
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
            loadRouteGraph();
            updateView();
        } else if (space.hasInfo(mapActivity)) {
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

    public void loadTitle(PBMap map) {
        mapActivity.setTitle(map.getId());
    }

    void loadDescription() {
        mapActivity.popupInfo(new Info(map));
    }

    public void updatePosition(@Nullable final Coordinate locationCoordinate) {
        if (locationCoordinate == null) {
            return;
        }
        if (!locationCoordinate.hasAltitude()) {
            locationCoordinate.alt = map.getCenter().alt;
        }
        Coordinate sourceCoordinate = source.getCoordinate();
        source.setCoordinate(locationCoordinate);
        if (!map.getBoundingBox().isInside(sourceCoordinate) &&
                map.getBoundingBox().isInside(locationCoordinate)) {
            source.pinpointOnMap(mapView);
        }
        source.setLevel(map.compareAltitude(sourceCoordinate), GeoMarker.Marker.SOURCE);
    }

    private void updateRoute() {
        route.removeFromMap(mapView);
        route.setSource(source);
        route.setDestination(destination);
        route.addToMap(mapView);
        updateDistance();
    }

    private void updateDistance() {
        if (map.isUnfinished()) {
            mapActivity.setDistance(mapActivity.getString(R.string.unfinished));
            return;
        }
        long distance = Math.round(route.calculateDistance());
        if (distance == 0) {
            mapActivity.setDistance(null);
        } else {
            MeasurementSystem measurementSystem = Config.getInstance().getMeasurementSystem();
            mapActivity.setDistance(mapActivity.getResources().getString(R.string.distance, measurementSystem.fromMeters(distance), measurementSystem.getUnit()));
        }
    }


    @Override
    public void onMapPositionChange() {
        if (mapView != null) {
            updateRoute();
        }
    }

    Memento getCurrentState() {
        mapView.addToMap(mapView);
        return new Memento(source.getCoordinate(), destination.getCoordinate(), map.getReferenceMapPath());
    }

    void onZoom(boolean zoomIn) {
        if (mapView != null) {
            if (zoomIn) {
                mapView.smoothScaleFromCenter(mapView.getScale() * 1.125f);
            } else {
                mapView.smoothScaleFromCenter(mapView.getScale() / 1.125f);
            }
        }
    }

    String getCurrentMapId() {
        return map == null ? null : map.getId();
    }

    void onNavigationPerformed(PBMap.Navigation navigation) {
        String navigationMapPath = map.getNavigationMapPath(navigation);
        if (navigationMapPath == null) {
            return;
        }
        map = mapsDao.loadMap(navigationMapPath);
        loadRouteGraph();
        updateView();
    }

    void onImprovePressed(MotionEvent event, String description) {
        CoordinateTranslater coordinateTranslater = mapView.getCoordinateTranslater();
        double lat = coordinateTranslater.translateAndScaleAbsoluteToRelativeY(mapView.getScrollY() + event.getY() - mapView.getOffsetY(), mapView.getScale());
        double lng = coordinateTranslater.translateAndScaleAbsoluteToRelativeX(mapView.getScrollX() + event.getX() - mapView.getOffsetX(), mapView.getScale());
        Coordinate coordinate = new Coordinate(lat, lng, map.getCenter().alt);
        Message message = new Message(map.getId(), coordinate, route.getRouteGraph().findClosest(coordinate), map.findClosest(coordinate));
        message.setDescription(description);
        WebLogger logger = new WebLogger(mapActivity);
        logger.logMessage(message);
        logger.trySendingMessages();
    }

    public static class Memento implements Parcelable {
        private Coordinate source;
        private Coordinate destination;
        private String mapReferencePath;

        private Memento(Coordinate source, Coordinate destination, String mapReferencePath) {
            this.source = source;
            this.destination = destination;
            this.mapReferencePath = mapReferencePath;
        }

        private Memento(Parcel in) {
            mapReferencePath = in.readString();
            Coordinate[] coordinates = new Coordinate[2];
            in.readTypedArray(coordinates, Coordinate.CREATOR);
            source = coordinates[0];
            destination = coordinates[1];
        }

        public static final Creator<Memento> CREATOR = new Creator<Memento>() {
            @Override
            public Memento createFromParcel(Parcel in) {
                return new Memento(in);
            }

            @Override
            public Memento[] newArray(int size) {
                return new Memento[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mapReferencePath);
            Coordinate[] coordinates = new Coordinate[]{source, destination};
            dest.writeTypedArray(coordinates, flags);
        }
    }

}
