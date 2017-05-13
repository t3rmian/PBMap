package io.github.t3r1jj.pbmap.view.map.routing;

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
import io.github.t3r1jj.pbmap.view.map.MapView;

public class Route implements RemovableView {
    private static final boolean LINE_SMOOTH = false;
    RouteGraph routeGraph;
    private final CompositePathView.DrawablePath drawablePath = new CompositePathView.DrawablePath();
    private GeoMarker source;
    private GeoMarker destination;
    private PBMap map;

    public Route(Context context) {
        Resources resources = context.getResources();
        drawablePath.paint = getPaint(resources.getColor(R.color.route), resources.getDimension(R.dimen.route_stroke_width));
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
    Paint getPaint(int color, float strokeWidth) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
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
        drawablePath.path = pathFromPositions(coordinateTranslater, positions);
    }

    /**
     * Convenience method to convert a List of coordinates (pairs of doubles) to a Path instance.
     *
     * @param positions List of coordinates (pairs of doubles).
     * @return The Path instance created from the positions supplied.
     */
    Path pathFromPositions(CoordinateTranslater coordinateTranslater, List<double[]> positions) {
        Path path = new Path();
        double[] start = positions.get(0);
        path.moveTo(coordinateTranslater.translateX(start[0]), coordinateTranslater.translateY(start[1]));
        if (LINE_SMOOTH) {
            if (positions.size() < 3) {
                prepareLinearPath(coordinateTranslater, positions, path);
            } else {
                prepareQuadPath(coordinateTranslater, positions, path);
            }
        } else {
            prepareLinearPath(coordinateTranslater, positions, path);
        }
        return path;
    }

    private void prepareLinearPath(CoordinateTranslater coordinateTranslater, List<double[]> positions, Path path) {
        for (int i = 1; i < positions.size(); i++) {
            double[] position = positions.get(i);
            path.lineTo(coordinateTranslater.translateX(position[0]), coordinateTranslater.translateY(position[1]));
        }
    }

    private void prepareQuadPath(CoordinateTranslater coordinateTranslater, List<double[]> positions, Path path) {
        for (int i = 1; i < positions.size() - 1; i += 2) {
            double[] second = positions.get(i);
            double[] third = positions.get(i + 1);
            path.quadTo(coordinateTranslater.translateX(second[0]), coordinateTranslater.translateY(second[1]),
                    coordinateTranslater.translateX(third[0]), coordinateTranslater.translateY(third[1]));
        }
        if (positions.size() % 2 == 0) {
            double[] position = positions.get(positions.size() - 1);
            path.lineTo(coordinateTranslater.translateX(position[0]), coordinateTranslater.translateY(position[1]));
        }
    }

}
