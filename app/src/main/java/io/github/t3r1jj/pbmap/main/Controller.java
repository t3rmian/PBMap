package io.github.t3r1jj.pbmap.main;

import android.widget.ImageView;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.Info;
import io.github.t3r1jj.pbmap.model.gps.Person;
import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.model.map.PBMap;
import io.github.t3r1jj.pbmap.model.map.Place;
import io.github.t3r1jj.pbmap.model.map.Space;
import io.github.t3r1jj.pbmap.model.map.route.Graph;
import io.github.t3r1jj.pbmap.search.MapsDao;
import io.github.t3r1jj.pbmap.search.SearchSuggestion;
import io.github.t3r1jj.pbmap.view.MapView;
import io.github.t3r1jj.pbmap.view.Route;

public class Controller {
    private final MapActivity mapActivity;
    private final MapsDao mapsDao;
    private PBMap map;
    private MapView mapView;
    private ImageView destinationMarker;
    private ImageView personMarker;
    private Coordinate destination;
    private Route destinationRoute;
    private Graph graph;

    Controller(MapActivity mapActivity) {
        this.mapActivity = mapActivity;
        this.mapsDao = new MapsDao(mapActivity);
    }


    void loadMap() {
        map = mapsDao.loadMap();
        updateView();
    }

    void loadMap(SearchSuggestion suggestion) {
        map = mapsDao.loadMap(suggestion.mapPath);
        updateView();
        pinpointPlace(suggestion.place);
    }

    void loadPreviousMap() {
        map = mapsDao.loadMap(map.getPreviousMapPath());
        updateView();
    }

    private void updateView() {
        clearContext();
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
        addAllRoutes();
    }

    /**
     * @deprecated use only for testing, no need to display all routes for user
     */
    @Deprecated
    private void addAllRoutes() {
        if (graph != null) {
            Route route = graph.createView(mapView);
            mapView.addRoute(route);
        }
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
