package io.github.t3r1jj.pbmap.view;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.qozix.tileview.geom.CoordinateTranslater;
import com.qozix.tileview.paths.CompositePathView;

import java.util.ArrayList;
import java.util.List;

import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.model.map.route.Edge;
import io.github.t3r1jj.pbmap.model.map.route.Graph;

public class Route {
    private List<CompositePathView.DrawablePath> drawablePaths = new ArrayList<>();

    public Route(MapView mapView, Graph graph) {
        Paint paint = getPaint(Color.RED);
        CoordinateTranslater coordinateTranslater = mapView.getCoordinateTranslater();

        for (Edge edge : graph.getPaths()) {
            CompositePathView.DrawablePath drawablePath = new CompositePathView.DrawablePath();
            List<double[]> positions = new ArrayList<>();
            positions.add(new double[]{edge.getStart().lng, edge.getStart().lat});
            positions.add(new double[]{edge.getEnd().lng, edge.getEnd().lat});
            drawablePath.path = coordinateTranslater.pathFromPositions(positions, false);
            drawablePath.paint = paint;
            drawablePaths.add(drawablePath);
        }
    }

    public Route(MapView mapView, List<Coordinate> coordinates) {
        Paint paint = getPaint(Color.YELLOW);
        CoordinateTranslater coordinateTranslater = mapView.getCoordinateTranslater();

        CompositePathView.DrawablePath drawablePath = new CompositePathView.DrawablePath();
        List<double[]> positions = new ArrayList<>();
        for (Coordinate coordinate : coordinates) {
            positions.add(new double[]{coordinate.lng, coordinate.lat});
        }
        drawablePath.path = coordinateTranslater.pathFromPositions(positions, false);
        drawablePath.paint = paint;
        drawablePaths.add(drawablePath);
    }

    @NonNull
    private Paint getPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        return paint;
    }

    public List<CompositePathView.DrawablePath> getDrawablePaths() {
        return drawablePaths;
    }
}
