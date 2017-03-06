package io.github.t3r1jj.pbmap.view.routing;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;

import com.qozix.tileview.geom.CoordinateTranslater;
import com.qozix.tileview.paths.CompositePathView;

import java.util.ArrayList;
import java.util.List;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.model.map.PBMap;
import io.github.t3r1jj.pbmap.model.map.route.RouteGraph;
import io.github.t3r1jj.pbmap.view.MapView;

public class Route implements RemovableView {
    private GeoMarker source;
    private GeoMarker destination;
    private PBMap map;
    private RouteGraph routeGraph;
    private final CompositePathView.DrawablePath drawablePath = new CompositePathView.DrawablePath();

    public Route(Context context) {
        Resources resources = context.getResources();
        drawablePath.paint = getPaint(resources.getColor(R.color.route), resources.getDimension(R.dimen.route_stroke_width));
        drawablePath.path = new Path();
    }

    public void setMap(PBMap map) {
        System.out.println("new map set");
        this.map = map;
    }

    public RouteGraph getRouteGraph() {
        return routeGraph;
    }

    public void setRouteGraph(RouteGraph routeGraph) {
        this.routeGraph = routeGraph;
    }

    public void setSource(GeoMarker source) {
        this.source = source;
    }

    public void setDestination(GeoMarker destination) {
        this.destination = destination;
    }

    @NonNull
    private Paint getPaint(int color, float strokeWidth) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        return paint;
    }

    @Override
    public void removeFromMap(MapView pbMapView) {
        CompositePathView compositePathView = pbMapView.getCompositePathView();
        compositePathView.removePath(drawablePath);
    }

    @Override
    public void addToMap(MapView pbMapView) {
        updatePath(pbMapView.getCoordinateTranslater());
        CompositePathView compositePathView = pbMapView.getCompositePathView();
        compositePathView.addPath(drawablePath);
    }

    private void updatePath(CoordinateTranslater coordinateTranslater) {
        if (routeGraph == null) {
            drawablePath.path = new Path();
            return;
        }
        List<Coordinate> route = routeGraph.getRoute(source.getCoordinate(), destination.getCoordinate());
        map.removeDifferentAltitudePoints(route);
        if (route.isEmpty()) {
            drawablePath.path = new Path();
            return;
        }
        List<double[]> positions = new ArrayList<>();
        for (Coordinate coordinate : route) {
            positions.add(new double[]{coordinate.lng, coordinate.lat});
        }
        drawablePath.path = coordinateTranslater.pathFromPositions(positions, false);
    }

}
