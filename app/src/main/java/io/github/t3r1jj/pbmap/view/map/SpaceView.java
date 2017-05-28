package io.github.t3r1jj.pbmap.view.map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;

import com.qozix.tileview.geom.CoordinateTranslater;
import com.qozix.tileview.hotspots.HotSpot;
import com.qozix.tileview.paths.CompositePathView;

import java.util.LinkedList;
import java.util.List;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.model.map.Space;

public class SpaceView extends CompositePathView.DrawablePath implements PlaceView {

    private final Space space;
    private final SpotView spotView;
    private final Context context;

    public SpaceView(Context context, Space space) {
        this.space = space;
        this.context = context;
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        Resources resources = context.getResources();
        paint.setStrokeWidth(resources.getDimension(R.dimen.space_stroke_width));
        if (space.getReferenceMapPath() != null) {
            paint.setColor(resources.getColor(R.color.space_bounds_interactive));
        } else {
            paint.setColor(resources.getColor(R.color.space_bounds));
        }
        spotView = new SpotView(context, space, resources.getColor(R.color.space_text));
    }

    @Override
    public void addToMap(final MapView pbMapView) {
        scalePath(pbMapView);
        pbMapView.getCompositePathView().addPath(this);
        pbMapView.addPlaceView(spotView);
        if (space.getLogoPath() != null) {
            Coordinate center = space.getCenter();
            pbMapView.addMarker(space.createLogo(context), center.lng, center.lat, -0.5f, -1.5f);
        }
        if (space.getReferenceMapPath() != null || space.hasInfo(context)) {
            HotSpot hotSpot = prepareHotspot();
            hotSpot.setHotSpotTapListener(new HotSpot.HotSpotTapListener() {
                @Override
                public void onHotSpotTap(HotSpot hotSpot, int x, int y) {
                    pbMapView.fireNavigationPerformed(space);
                }
            });
            pbMapView.addHotSpot(hotSpot);
        }

    }

    private void scalePath(MapView pbMapView) {
        CoordinateTranslater coordinateTranslater = pbMapView.getCoordinateTranslater();
        List<double[]> positions = new LinkedList<>();
        for (Coordinate coordinate : space.getCoordinates()) {
            positions.add(new double[]{coordinate.lng, coordinate.lat});
        }
        path = coordinateTranslater.pathFromPositions(positions, true);
    }

    private HotSpot prepareHotspot() {
        RectF bounds = new RectF();
        path.computeBounds(bounds, true);
        Rect rect = new Rect();
        bounds.round(rect);
        Region clip = new Region(rect);
        HotSpot hotSpot = new HotSpot();
        hotSpot.setPath(path, clip);
        return hotSpot;
    }

}
