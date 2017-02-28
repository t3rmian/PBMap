package io.github.t3r1jj.pbmap.view;

import android.graphics.Color;
import android.graphics.Paint;

import com.qozix.tileview.geom.CoordinateTranslater;
import com.qozix.tileview.paths.CompositePathView;

import java.util.ArrayList;
import java.util.List;

import io.github.t3r1jj.pbmap.model.map.route.Edge;
import io.github.t3r1jj.pbmap.model.map.route.Graph;

public class Route {
    private List<CompositePathView.DrawablePath> drawablePaths = new ArrayList<>();

    public Route(MapView mapView, Graph graph) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        CoordinateTranslater coordinateTranslater = mapView.getCoordinateTranslater();

        for (Edge edge : graph.getRoute()) {
            CompositePathView.DrawablePath drawablePath = new CompositePathView.DrawablePath();
            List<double[]> positions = new ArrayList<>();
            positions.add(new double[]{edge.getStart().lng, edge.getStart().lat});
            positions.add(new double[]{edge.getEnd().lng, edge.getEnd().lat});
            drawablePath.path = coordinateTranslater.pathFromPositions(positions, false);
            drawablePath.paint = paint;
            drawablePaths.add(drawablePath);
        }
    }

    public List<CompositePathView.DrawablePath> getDrawablePaths() {
        return drawablePaths;
    }
}
