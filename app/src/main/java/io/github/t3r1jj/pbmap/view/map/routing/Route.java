package io.github.t3r1jj.pbmap.view.map.routing;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Path;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.qozix.tileview.geom.CoordinateTranslater;
import com.qozix.tileview.paths.CompositePathView;

import java.util.List;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.model.map.PBMap;
import io.github.t3r1jj.pbmap.model.map.route.RouteGraph;
import io.github.t3r1jj.pbmap.view.map.MapView;

public class Route implements RemovableView {
    RouteGraph routeGraph;
    private final CompositePathView.DrawablePath drawablePath = new CompositePathView.DrawablePath();
    private GeoMarker source;
    private GeoMarker destination;
    private PBMap map;
    private List<Coordinate> route;

    public Route(Context context) {
        Resources resources = context.getResources();
        drawablePath.paint = createPaint(ContextCompat.getColor(context, R.color.route), resources.getDimension(R.dimen.route_stroke_width));
        drawablePath.path = new Path();
    }

    public void setMap(PBMap map) {
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
    private Paint createPaint(int color, float strokeWidth) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        return paint;
    }

    @NonNull
    Path createPath() {
        return new Path();
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
        route = routeGraph.getRoute(source.getCoordinate(), destination.getCoordinate());
        map.removeDifferentAltitudePoints(route);
        if (route.isEmpty()) {
            drawablePath.path = new Path();
            return;
        }
        drawablePath.path = pathFromPositions(coordinateTranslater, route);
    }

    /**
     * Convenience method to convert a List of coordinates (pairs of doubles) to a Path instance.
     *
     * @param positions List of coordinates (pairs of doubles).
     * @return The Path instance created from the positions supplied.
     */
    Path pathFromPositions(CoordinateTranslater coordinateTranslater, List<Coordinate> positions) {
        Path path = createPath();
        Coordinate start = positions.get(0);
        path.moveTo(coordinateTranslater.translateX(start.lng), coordinateTranslater.translateY(start.lat));
        prepareLinearPath(coordinateTranslater, positions, path);
        return path;
    }

    private void prepareLinearPath(CoordinateTranslater coordinateTranslater, List<Coordinate> positions, Path path) {
        for (int i = 1; i < positions.size(); i++) {
            Coordinate from = positions.get(i - 1);
            Coordinate to = positions.get(i);
            if (from.isDetachedFromNext()) {
                path.moveTo(coordinateTranslater.translateX(to.lng), coordinateTranslater.translateY(to.lat));
            } else {
                path.lineTo(coordinateTranslater.translateX(to.lng), coordinateTranslater.translateY(to.lat));
            }
        }
    }

    public double calculateDistance() {
        if (route == null) {
            return 0;
        }
        double distance = 0;
        for (int i = 1; i < route.size(); i++) {
            distance += route.get(i - 1).distanceTo(route.get(i));
        }
        return distance;
    }

}
