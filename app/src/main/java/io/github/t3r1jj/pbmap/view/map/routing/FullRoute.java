package io.github.t3r1jj.pbmap.view.map.routing;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;

import com.qozix.tileview.paths.CompositePathView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.model.map.route.Edge;
import io.github.t3r1jj.pbmap.view.map.MapView;

/**
 * @deprecated use only for testing
 */
@Deprecated
public class FullRoute extends Route {

    private final int[] colors = new int[]{Color.GREEN, Color.BLUE, Color.CYAN, Color.GRAY, Color.YELLOW, Color.MAGENTA};
    private DashPathEffect[] effects = new DashPathEffect[colors.length];
    private float strokeWidth;

    private List<CompositePathView.DrawablePath> paths = new LinkedList<>();

    @Deprecated
    public FullRoute(Context context) {
        super(context);
        Resources resources = context.getResources();
        strokeWidth = resources.getDimension(R.dimen.route_stroke_width);
        Random random = new Random();
        for (int i = 0; i < colors.length; i++) {
            float randomMultiplier = random.nextFloat() + 1;
            effects[i] = new DashPathEffect(new float[]{randomMultiplier * 5, randomMultiplier * 10}, 0);
        }
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
            List<Coordinate> positions = new ArrayList<>();
            positions.add(edge.getStart());
            positions.add(edge.getEnd());
            CompositePathView.DrawablePath drawablePath = new CompositePathView.DrawablePath();
            drawablePath.paint = getPaint(edge);
            drawablePath.path = pathFromPositions(pbMapView.getCoordinateTranslater(), positions);
            paths.add(drawablePath);
        }
        CompositePathView compositePathView = pbMapView.getCompositePathView();
        for (CompositePathView.DrawablePath path : paths) {
            compositePathView.addPath(path);
        }
    }

    private Paint getPaint(Edge edge) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        if (edge.sameAltitude()) {
            paint.setStrokeWidth(strokeWidth);
            int diffIndex = ((int) (edge.getStart().alt / 5)) % colors.length;
            paint.setColor(colors[diffIndex]);
            paint.setPathEffect(effects[diffIndex]);
        } else {
            paint.setStrokeCap(Paint.Cap.SQUARE);
            paint.setStrokeWidth(strokeWidth * 5);
            paint.setColor(Color.RED);
        }
        return paint;
    }
}
