package io.github.t3r1jj.pbmap.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.qozix.tileview.paths.CompositePathView;

import io.github.t3r1jj.pbmap.model.Coordinate;
import io.github.t3r1jj.pbmap.model.Space;

public class SpaceView extends CompositePathView.DrawablePath implements PlaceView {

    SpotView spotView;

    public SpaceView(Context context, Space space) {
        path = new Path();
        paint = new Paint();
        Coordinate[] coordinates = space.getCoordinates();
        path.moveTo((float) coordinates[0].lng, (float) coordinates[0].lat);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(Color.GRAY);
        for (Coordinate coordinate : coordinates) {
            path.lineTo((float) coordinate.lng, (float) coordinate.lat);
        }
        spotView = new SpotView(context, space);
    }

    @Override
    public void addToMap(PBMapView pbMapView) {
        pbMapView.getCompositePathView().addPath(this);
        pbMapView.addPlaceView(spotView);
    }
}
