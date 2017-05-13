package io.github.t3r1jj.pbmap.view.map.routing;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Path;

import com.qozix.tileview.paths.CompositePathView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.map.route.Edge;
import io.github.t3r1jj.pbmap.view.map.MapView;

/**
 * @deprecated use only for testing
 */
@Deprecated
public class FullRoute extends Route {

    private final CompositePathView.DrawablePath drawablePath = new CompositePathView.DrawablePath();
    private List<CompositePathView.DrawablePath> paths = new LinkedList<>();

    public FullRoute(Context context) {
        super(context);
        Resources resources = context.getResources();
        drawablePath.paint = getPaint(resources.getColor(R.color.route), resources.getDimension(R.dimen.route_stroke_width));
        drawablePath.path = new Path();
        drawablePath.paint.setColor(Color.RED);
    }


    @Override
    public void removeFromMap(MapView pbMapView) {
        super.removeFromMap(pbMapView);
        CompositePathView compositePathView = pbMapView.getCompositePathView();
        for (CompositePathView.DrawablePath path : paths) {
            compositePathView.removePath(path);
        }
    }

    @Override
    public void addToMap(MapView pbMapView) {
        super.addToMap(pbMapView);
        paths.clear();
        List<Edge> edges = routeGraph.getEdges();
        for (Edge edge : edges) {
//            if (edge.getStart().alt != 160d) {
//                continue;
//            }
            List<double[]> positions = new ArrayList<>();
            positions.add(new double[]{edge.getStart().lng, edge.getStart().lat});
            positions.add(new double[]{edge.getEnd().lng, edge.getEnd().lat});
            CompositePathView.DrawablePath drawablePath = new CompositePathView.DrawablePath();
            drawablePath.paint = this.drawablePath.paint;
            drawablePath.path = pathFromPositions(pbMapView.getCoordinateTranslater(), positions);
            paths.add(drawablePath);
        }
        CompositePathView compositePathView = pbMapView.getCompositePathView();
        for (CompositePathView.DrawablePath path : paths) {
            compositePathView.addPath(path);
        }
    }
}
