package io.github.t3r1jj.pbmap;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.Log;
import android.widget.TextView;

import com.qozix.tileview.TileView;
import com.qozix.tileview.paths.CompositePathView;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import io.github.t3r1jj.pbmap.model.Coordinate;
import io.github.t3r1jj.pbmap.model.Map;
import io.github.t3r1jj.pbmap.model.Place;

public class Controller extends ContextWrapper {
    Map map;

    public Controller(Context base, String assetsMapPath) {
        super(base);
        Serializer serializer = new Persister();
        try {
            map = serializer.read(Map.class, getAssets().open(assetsMapPath));
            Log.d("MainActivity", map.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addData(TileView tileView) {
        for (Place place : map.getPlaces()) {
            Coordinate[] coordinates = place.getCoordinates();
            Coordinate center = new Coordinate();
            if (coordinates.length != 1) {
                CompositePathView.DrawablePath drawablePath = new CompositePathView.DrawablePath();
                drawablePath.path = new Path();
                drawablePath.paint = new Paint();
                drawablePath.path.moveTo((float) coordinates[0].lng, (float) coordinates[0].lat);
                drawablePath.paint.setStyle(Paint.Style.STROKE);
                drawablePath.paint.setStrokeWidth(3);
                drawablePath.paint.setColor(Color.GRAY);
                for (Coordinate coordinate : coordinates) {
                    drawablePath.path.lineTo((float) coordinates[0].lng, (float) coordinates[0].lat);
                    center.lng += coordinate.lng;
                    center.lat += coordinate.lat;
                }
                center.lng /= (double) coordinates.length;
                center.lat /= (double) coordinates.length;
                tileView.getCompositePathView().addPath(drawablePath);
            } else {
                center = coordinates[0];
            }
            TextView textView = new TextView(this);
            textView.setText(place.getName());
            textView.setTextColor(Color.RED);
            textView.setTextSize(12f);
            tileView.addMarker(textView, center.lng, center.lat, -0.5f, -1.0f);
        }
    }
}
