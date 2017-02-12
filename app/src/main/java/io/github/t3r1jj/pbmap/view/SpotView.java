package io.github.t3r1jj.pbmap.view;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.qozix.tileview.markers.MarkerLayout;

import io.github.t3r1jj.pbmap.model.Coordinate;
import io.github.t3r1jj.pbmap.model.Place;

public class SpotView extends MarkerLayout implements PlaceView {

    final TextView textView;
    final Coordinate center;

    public SpotView(Context context, Place place) {
        super(context);
        textView = new TextView(context);
        textView.setText(place.getName());
        textView.setTextColor(Color.RED);
        textView.setTextSize(12f);
        center = place.getCenter();
    }

    @Override
    public void addToMap(PBMapView pbMapView) {
        pbMapView.addMarker(textView, center.lng, center.lat, -0.5f, -1.0f);
    }
}
