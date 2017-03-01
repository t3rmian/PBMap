package io.github.t3r1jj.pbmap.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.widget.TextView;

import com.qozix.tileview.markers.MarkerLayout;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.model.map.Place;

public class SpotView extends MarkerLayout implements PlaceView {

    private final TextView textView;
    private final Coordinate center;

    public SpotView(Context context, Place place) {
        super(context);
        textView = new TextView(context);
        textView.setText(place.getName());
        Resources resources = context.getResources();
        textView.setTextColor(resources.getColor(R.color.spot_text));
        textView.setTextSize(resources.getDimension(R.dimen.spot_text));
        center = place.getCenter();
    }

    @Override
    public void addToMap(MapView pbMapView) {
        pbMapView.addMarker(textView, center.lng, center.lat, -0.5f, -1.0f);
    }
}
